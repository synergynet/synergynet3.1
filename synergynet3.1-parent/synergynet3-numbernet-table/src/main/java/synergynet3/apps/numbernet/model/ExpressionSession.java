package synergynet3.apps.numbernet.model;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.hazelcast.core.IMap;
import com.hazelcast.core.Member;

import synergynet3.cluster.sharedmemory.DistributedMap;
import synergynet3.cluster.sharedmemory.DistributedPropertyChangedAction;
import synergynet3.cluster.sharedmemory.DistributedMap.ItemAddedAction;
import synergynet3.cluster.sharedmemory.DistributedMap.ItemRemovedAction;
import synergynet3.web.apps.numbernet.comms.table.NumberNetStudentTableClusteredData;
import synergynet3.web.apps.numbernet.comms.table.TargetMaps;
import synergynet3.web.apps.numbernet.shared.Expression;

public class ExpressionSession {
	private static final Logger log = Logger.getLogger(ExpressionSession.class.getName());

	private DecimalFormat targetValueFormatter;
	private Double targetValue = null;
	private List<Expression> expressions;
	private List<IExpressionSessionChangeListener> listeners;
	private DistributedMap<String, Expression> distributedExpressionMap;
	private ItemAddedAction<String, Expression> itemAddedAction;
	private ItemRemovedAction<String, Expression> itemRemovedAction;

	public ExpressionSession(NumberNetStudentTableClusteredData dataCluster) {
		listeners = new ArrayList<IExpressionSessionChangeListener>();

		targetValueFormatter = new DecimalFormat();
		targetValueFormatter.setDecimalSeparatorAlwaysShown(false);
		targetValueFormatter.setMaximumFractionDigits(10);

		expressions = new ArrayList<Expression>();

		dataCluster.getTargetValueControlVariable().registerChangeListener(new DistributedPropertyChangedAction<Double>() {			
			@Override
			public void distributedPropertyDidChange(Member member, Double oldValue,
					Double newValue) {
				log.fine("Target value changed from " + oldValue + " to " + newValue);
				setTarget(newValue);
			}
		});		
	}	

	
	public void setTarget(double newTarget) {
		this.targetValue = newTarget;

		removeAllExpressionsLocally();
		switchDistributedMapAndUpdateMapActions();
		addAllExpressionsFromDistributedMap();

		for(IExpressionSessionChangeListener listener : listeners) {
			listener.targetChanged(newTarget);
		}
	}

	


	protected void removeAllExpressionsLocally() {
		expressions.clear();
		for(IExpressionSessionChangeListener listener : listeners) {
			listener.allExpressionsRemoved();
		}
	}

	public void addAllExpressionsFromDistributedMap() {
		if(distributedExpressionMap == null) return;
		log.info("Distributed map has " + distributedExpressionMap.size() + " entries.");
		for(Expression e : distributedExpressionMap.values()) {
			addExpressionFromAlreadyExistingEntryInDistributedMap(e);
		}
	}


	public void addExpressionCreatedByCalculator(Expression e) {
		addExpressionToSession(e, true);
	}

	public boolean containsExpression(String expressionString) {
		return getExpressionForExpressionString(expressionString) != null;
	}

	public Expression getExpressionForExpressionString(String expressionString) {
		for(int i = 0; i < expressions.size(); i++) {
			if(expressions.get(i).getExpression().equals(expressionString)) {
				return expressions.get(i);
			}
		}
		return null;
	}

	public Double getTargetValue() {
		return targetValue;
	}

	public String getCurrentTargetValueAsString() {
		if(getTargetValue() == null) return null;
		return targetValueFormatter.format(getTargetValue());
	}

	public void removeChangeListener(IExpressionSessionChangeListener listener) {
		this.listeners.remove(listener);
	}

	public void addChangeListener(IExpressionSessionChangeListener listener) {
		if(listeners.contains(listener)) return;
		this.listeners.add(listener);
	}

	public void removeExpressionWithText(String expressionText) {
		log.info("Attempting to remove expression >" + expressionText + "<");
		log.info("Finding appropriate object...");
		Expression expression = getExpressionForExpressionString(expressionText);
		if(expression == null) {
			log.info("Did not find one.");
			return;
		}else{
			log.info("Found expression " + expression.getFullString());
			removeExpressionFromSession(expression, true);
		}
	}
	
	public ExpressionSessionScores getScoresForTable(String tableID) {
		int correctCount = 0;
		int incorrectCount = 0;
		for(int i = 0; i < expressions.size(); i++) {
			if(expressions.get(i).getCreatedOnTable().equals(tableID)) {
				if(expressions.get(i).isCorrect()) {
					correctCount++;
				}else{
					incorrectCount++;
				}
			}
		}
		return new ExpressionSessionScores(correctCount, incorrectCount);
	}

	// ********************* private ***********
	
	private void switchDistributedMapAndUpdateMapActions() {
		if(this.distributedExpressionMap != null) {
			this.distributedExpressionMap.unregisterItemAddedAction(itemAddedAction);
			this.distributedExpressionMap.unregisterItemRemovedAction(itemRemovedAction);
		}

		this.distributedExpressionMap = TargetMaps.get().getDistributedMapForTarget(targetValue);
//		this.distributedExpressionMap = dataCluster.getDistributedExpressionMapForTarget(this.targetValue);
		this.distributedExpressionMap.registerItemAddedAction(createAndGetItemAddedAction());
		this.distributedExpressionMap.registerItemRemovedAction(createAndGetItemRemovedAction());		
	}


	private void addExpressionFromAlreadyExistingEntryInDistributedMap(
			Expression e) {
		log.info("Adding " + e + " to display");
		addExpressionToSession(e, false);
	}


	private void addExpressionToSession(Expression e, boolean addToDistributedMap) {
		expressions.add(e);

		if(addToDistributedMap) {
			for(IExpressionSessionChangeListener listener : listeners) {
				listener.expressionAddedFromCalculator(e);
			}
			if(distributedExpressionMap != null) {
				distributedExpressionMap.put(e.getId(), e);
			}else{
				log.warning("Attempt to add an expression when there's no distributed map");
			}
		}else{			
			for(IExpressionSessionChangeListener listener : listeners) {
				listener.expressionAddedFromNetwork(e);
			}
		}
	}

	private void removeExpressionFromSession(Expression expression, boolean deleteFromDistributedMap) {
		expressions.remove(expression);		
		if(deleteFromDistributedMap) {
			if(distributedExpressionMap != null) {
				distributedExpressionMap.remove(expression.getId());
			}
		}
		for(IExpressionSessionChangeListener listener : listeners) {
			listener.expressionRemoved(expression);
		}
	}
	
	private ItemRemovedAction<String, Expression> createAndGetItemRemovedAction() {
		itemRemovedAction = new ItemRemovedAction<String, Expression>() {
			@Override
			public void itemRemovedFromCollection(
					IMap<String, Expression> collection, String itemKey,
					Expression itemValue, Member member) {
				if(member.localMember()) return;
				removeExpressionFromSession(itemValue, true);				
			}
		};
		return itemRemovedAction;
	}


	private ItemAddedAction<String, Expression> createAndGetItemAddedAction() {
		itemAddedAction = new ItemAddedAction<String, Expression>() {
			@Override
			public void itemAddedToCollection(
					IMap<String, Expression> collection, String itemKey,
					Expression itemValue, Member member) {
				if(member.localMember()) return;
				addExpressionFromAlreadyExistingEntryInDistributedMap(itemValue);
			}
		};
		return itemAddedAction;
	}
}
