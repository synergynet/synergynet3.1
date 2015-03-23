package synergynet3.apps.numbernet.model;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import synergynet3.cluster.sharedmemory.DistributedMap;
import synergynet3.cluster.sharedmemory.DistributedMap.ItemAddedAction;
import synergynet3.cluster.sharedmemory.DistributedMap.ItemRemovedAction;
import synergynet3.cluster.sharedmemory.DistributedPropertyChangedAction;
import synergynet3.web.apps.numbernet.comms.table.NumberNetStudentTableClusteredData;
import synergynet3.web.apps.numbernet.comms.table.TargetMaps;
import synergynet3.web.apps.numbernet.shared.Expression;

import com.hazelcast.core.IMap;
import com.hazelcast.core.Member;

/**
 * The Class ExpressionSession.
 */
public class ExpressionSession {

	/** The Constant log. */
	private static final Logger log = Logger.getLogger(ExpressionSession.class
			.getName());

	/** The distributed expression map. */
	private DistributedMap<String, Expression> distributedExpressionMap;

	/** The expressions. */
	private List<Expression> expressions;

	/** The item added action. */
	private ItemAddedAction<String, Expression> itemAddedAction;

	/** The item removed action. */
	private ItemRemovedAction<String, Expression> itemRemovedAction;

	/** The listeners. */
	private List<IExpressionSessionChangeListener> listeners;

	/** The target value. */
	private Double targetValue = null;

	/** The target value formatter. */
	private DecimalFormat targetValueFormatter;

	/**
	 * Instantiates a new expression session.
	 *
	 * @param dataCluster the data cluster
	 */
	public ExpressionSession(NumberNetStudentTableClusteredData dataCluster) {
		listeners = new ArrayList<IExpressionSessionChangeListener>();

		targetValueFormatter = new DecimalFormat();
		targetValueFormatter.setDecimalSeparatorAlwaysShown(false);
		targetValueFormatter.setMaximumFractionDigits(10);

		expressions = new ArrayList<Expression>();

		dataCluster.getTargetValueControlVariable().registerChangeListener(
				new DistributedPropertyChangedAction<Double>() {
					@Override
					public void distributedPropertyDidChange(Member member,
							Double oldValue, Double newValue) {
						log.fine("Target value changed from " + oldValue
								+ " to " + newValue);
						setTarget(newValue);
					}
				});
	}

	/**
	 * Adds the all expressions from distributed map.
	 */
	public void addAllExpressionsFromDistributedMap() {
		if (distributedExpressionMap == null) {
			return;
		}
		log.info("Distributed map has " + distributedExpressionMap.size()
				+ " entries.");
		for (Expression e : distributedExpressionMap.values()) {
			addExpressionFromAlreadyExistingEntryInDistributedMap(e);
		}
	}

	/**
	 * Adds the change listener.
	 *
	 * @param listener the listener
	 */
	public void addChangeListener(IExpressionSessionChangeListener listener) {
		if (listeners.contains(listener)) {
			return;
		}
		this.listeners.add(listener);
	}

	/**
	 * Adds the expression created by calculator.
	 *
	 * @param e the e
	 */
	public void addExpressionCreatedByCalculator(Expression e) {
		addExpressionToSession(e, true);
	}

	/**
	 * Contains expression.
	 *
	 * @param expressionString the expression string
	 * @return true, if successful
	 */
	public boolean containsExpression(String expressionString) {
		return getExpressionForExpressionString(expressionString) != null;
	}

	/**
	 * Gets the current target value as string.
	 *
	 * @return the current target value as string
	 */
	public String getCurrentTargetValueAsString() {
		if (getTargetValue() == null) {
			return null;
		}
		return targetValueFormatter.format(getTargetValue());
	}

	/**
	 * Gets the expression for expression string.
	 *
	 * @param expressionString the expression string
	 * @return the expression for expression string
	 */
	public Expression getExpressionForExpressionString(String expressionString) {
		for (int i = 0; i < expressions.size(); i++) {
			if (expressions.get(i).getExpression().equals(expressionString)) {
				return expressions.get(i);
			}
		}
		return null;
	}

	/**
	 * Gets the scores for table.
	 *
	 * @param tableID the table id
	 * @return the scores for table
	 */
	public ExpressionSessionScores getScoresForTable(String tableID) {
		int correctCount = 0;
		int incorrectCount = 0;
		for (int i = 0; i < expressions.size(); i++) {
			if (expressions.get(i).getCreatedOnTable().equals(tableID)) {
				if (expressions.get(i).isCorrect()) {
					correctCount++;
				} else {
					incorrectCount++;
				}
			}
		}
		return new ExpressionSessionScores(correctCount, incorrectCount);
	}

	/**
	 * Gets the target value.
	 *
	 * @return the target value
	 */
	public Double getTargetValue() {
		return targetValue;
	}

	/**
	 * Removes the change listener.
	 *
	 * @param listener the listener
	 */
	public void removeChangeListener(IExpressionSessionChangeListener listener) {
		this.listeners.remove(listener);
	}

	/**
	 * Removes the expression with text.
	 *
	 * @param expressionText the expression text
	 */
	public void removeExpressionWithText(String expressionText) {
		log.info("Attempting to remove expression >" + expressionText + "<");
		log.info("Finding appropriate object...");
		Expression expression = getExpressionForExpressionString(expressionText);
		if (expression == null) {
			log.info("Did not find one.");
			return;
		} else {
			log.info("Found expression " + expression.getFullString());
			removeExpressionFromSession(expression, true);
		}
	}

	/**
	 * Sets the target.
	 *
	 * @param newTarget the new target
	 */
	public void setTarget(double newTarget) {
		this.targetValue = newTarget;

		removeAllExpressionsLocally();
		switchDistributedMapAndUpdateMapActions();
		addAllExpressionsFromDistributedMap();

		for (IExpressionSessionChangeListener listener : listeners) {
			listener.targetChanged(newTarget);
		}
	}

	/**
	 * Adds the expression from already existing entry in distributed map.
	 *
	 * @param e the e
	 */
	private void addExpressionFromAlreadyExistingEntryInDistributedMap(
			Expression e) {
		log.info("Adding " + e + " to display");
		addExpressionToSession(e, false);
	}

	// ********************* private ***********

	/**
	 * Adds the expression to session.
	 *
	 * @param e the e
	 * @param addToDistributedMap the add to distributed map
	 */
	private void addExpressionToSession(Expression e,
			boolean addToDistributedMap) {
		expressions.add(e);

		if (addToDistributedMap) {
			for (IExpressionSessionChangeListener listener : listeners) {
				listener.expressionAddedFromCalculator(e);
			}
			if (distributedExpressionMap != null) {
				distributedExpressionMap.put(e.getId(), e);
			} else {
				log.warning("Attempt to add an expression when there's no distributed map");
			}
		} else {
			for (IExpressionSessionChangeListener listener : listeners) {
				listener.expressionAddedFromNetwork(e);
			}
		}
	}

	/**
	 * Creates the and get item added action.
	 *
	 * @return the item added action
	 */
	private ItemAddedAction<String, Expression> createAndGetItemAddedAction() {
		itemAddedAction = new ItemAddedAction<String, Expression>() {
			@Override
			public void itemAddedToCollection(
					IMap<String, Expression> collection, String itemKey,
					Expression itemValue, Member member) {
				if (member.localMember()) {
					return;
				}
				addExpressionFromAlreadyExistingEntryInDistributedMap(itemValue);
			}
		};
		return itemAddedAction;
	}

	/**
	 * Creates the and get item removed action.
	 *
	 * @return the item removed action
	 */
	private ItemRemovedAction<String, Expression> createAndGetItemRemovedAction() {
		itemRemovedAction = new ItemRemovedAction<String, Expression>() {
			@Override
			public void itemRemovedFromCollection(
					IMap<String, Expression> collection, String itemKey,
					Expression itemValue, Member member) {
				if (member.localMember()) {
					return;
				}
				removeExpressionFromSession(itemValue, true);
			}
		};
		return itemRemovedAction;
	}

	/**
	 * Removes the expression from session.
	 *
	 * @param expression the expression
	 * @param deleteFromDistributedMap the delete from distributed map
	 */
	private void removeExpressionFromSession(Expression expression,
			boolean deleteFromDistributedMap) {
		expressions.remove(expression);
		if (deleteFromDistributedMap) {
			if (distributedExpressionMap != null) {
				distributedExpressionMap.remove(expression.getId());
			}
		}
		for (IExpressionSessionChangeListener listener : listeners) {
			listener.expressionRemoved(expression);
		}
	}

	/**
	 * Switch distributed map and update map actions.
	 */
	private void switchDistributedMapAndUpdateMapActions() {
		if (this.distributedExpressionMap != null) {
			this.distributedExpressionMap
					.unregisterItemAddedAction(itemAddedAction);
			this.distributedExpressionMap
					.unregisterItemRemovedAction(itemRemovedAction);
		}

		this.distributedExpressionMap = TargetMaps.get()
				.getDistributedMapForTarget(targetValue);
		// this.distributedExpressionMap =
		// dataCluster.getDistributedExpressionMapForTarget(this.targetValue);
		this.distributedExpressionMap
				.registerItemAddedAction(createAndGetItemAddedAction());
		this.distributedExpressionMap
				.registerItemRemovedAction(createAndGetItemRemovedAction());
	}

	/**
	 * Removes the all expressions locally.
	 */
	protected void removeAllExpressionsLocally() {
		expressions.clear();
		for (IExpressionSessionChangeListener listener : listeners) {
			listener.allExpressionsRemoved();
		}
	}
}
