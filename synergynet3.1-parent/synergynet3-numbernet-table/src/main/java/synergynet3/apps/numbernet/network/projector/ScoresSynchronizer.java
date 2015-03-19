package synergynet3.apps.numbernet.network.projector;

import java.util.Collection;
import java.util.logging.Logger;

import synergynet3.apps.numbernet.ui.projection.scores.ProjectScoresUI;
import synergynet3.apps.numbernet.ui.projection.scores.TableScoresUI;
import synergynet3.cluster.SynergyNetCluster;
import synergynet3.cluster.sharedmemory.DistributedMap;
import synergynet3.cluster.sharedmemory.DistributedMap.ItemAddedAction;
import synergynet3.cluster.sharedmemory.DistributedMap.ItemRemovedAction;
import synergynet3.cluster.sharedmemory.DistributedMap.ItemUpdatedAction;
import synergynet3.cluster.xmpp.presence.IPresenceListener;
import synergynet3.web.apps.numbernet.comms.table.TargetMaps;
import synergynet3.web.apps.numbernet.comms.teachercontrol.TeacherControlComms;
import synergynet3.web.apps.numbernet.shared.CalculatorKey;
import synergynet3.web.apps.numbernet.shared.Expression;
import synergynet3.web.apps.numbernet.shared.TableTarget;

import com.hazelcast.core.IMap;
import com.hazelcast.core.Member;

public class ScoresSynchronizer {
	private static final Logger log = Logger.getLogger(ScoresSynchronizer.class.getName());

	private ProjectScoresUI projectScoresUI;

	public ScoresSynchronizer(ProjectScoresUI projectScoresUI) {
		this.projectScoresUI = projectScoresUI;
		SynergyNetCluster.get().getPresenceManager().registerPresenceListener(new IPresenceListener() {
			
			@Override
			public void deviceUnavailable(String id) {}
			
			@Override
			public void deviceAvailable(String id) {
				update();	
			}
		});
		update();
	}

	public void update() {
		log.fine("Updating map listeners.");
		updateMapListeners();
	}

	private void updateMapListeners() {
		for(String table : TeacherControlComms.get().getExpectedTablesList()) {
			Double target = TeacherControlComms.get().getStudentTableDeviceForName(table).getTargetValueControlVariable().getValue();
			if(target == null) continue;
			log.fine("Listening to changes to target map: " + target);
			DistributedMap<String,Expression> map = TargetMaps.get().getDistributedMapForTarget(target);
			registerAsListenerForChangesInDistributedMap(target, map);
		}
	}

	private void registerAsListenerForChangesInDistributedMap(final Double target, DistributedMap<String, Expression> map) {
		map.registerItemAddedAction(new ItemAddedAction<String, Expression>() {
			@Override
			public void itemAddedToCollection(
					IMap<String, Expression> collection, String itemKey,
					Expression itemValue, Member member) {
				mapChangedForTarget(target);
			}
		});
		
		map.registerItemRemovedAction(new ItemRemovedAction<String, Expression>() {
			@Override
			public void itemRemovedFromCollection(
					IMap<String, Expression> collection, String itemKey,
					Expression itemValue, Member member) {
				mapChangedForTarget(target);
			}
		});
		
		map.registerItemUpdatedAction(new ItemUpdatedAction<String, Expression>() {
			@Override
			public void itemUpdatedInCollection(
					IMap<String, Expression> collection, String itemKey,
					Expression itemOldValue, Expression itemNewValue,
					Member member) {
				mapChangedForTarget(target);
			}
		});
		
		mapChangedForTarget(target);
	}

	protected void mapChangedForTarget(Double target) {
		String table = getTableForTarget(target);
		if(table != null) {
			updateTableUI(target, table);
		}
	}

	private void updateTableUI(Double target, String table) {
		log.fine("Updating scores ui for " + table + " currently assigned target " + target);
		TableScoresUI tableScoresUI = projectScoresUI.getTableUI(table);
		tableScoresUI.setCorrectExpressionCount(getCorrectExpressionCount(target, table));
		tableScoresUI.setIncorrectExpressionCount(getIncorrectExpressionCount(target, table));
		tableScoresUI.setBracketUseCount(getBracketUseCount(target, table));
		tableScoresUI.setUsedAllOperators(getHasUsedAllOperators(target, table));
		tableScoresUI.setMoreThanOneOperatorCount(getMoreThanOneOperatorCount(target, table));
	}

	private int getMoreThanOneOperatorCount(Double target, String table) {
		int count = 0;

		Collection<Expression> expressions = TargetMaps.get().getDistributedMapForTarget(target).values();
		for(Expression e : expressions) {
			if(e.isCorrect()) {
				boolean plus = false;
				boolean minus = false;
				boolean multiply = false;
				boolean divide = false;
				
				if(e.getExpression().indexOf(CalculatorKey.PLUS.getStringRepresentation()) != -1) plus = true;
				if(e.getExpression().indexOf(CalculatorKey.MINUS.getStringRepresentation()) != -1) minus = true;
				if(e.getExpression().indexOf(CalculatorKey.DIVIDE.getStringRepresentation()) != -1) divide = true;
				if(e.getExpression().indexOf(CalculatorKey.MULTIPLY.getStringRepresentation()) != -1) multiply = true;
				
				if(countTrue(plus, minus, multiply, divide) > 1) {
					count++;
				}
			}
		}
		return count;
	}

	private int countTrue(boolean... bools) {
		int count = 0;
		for(int i = 0; i < bools.length; i++) {
			if(bools[i]) count++;
		}
		return count;
	}

	private boolean getHasUsedAllOperators(Double target, String table) {
		boolean plus = false;
		boolean minus = false;
		boolean multiply = false;
		boolean divide = false;
		
		Collection<Expression> expressions = TargetMaps.get().getDistributedMapForTarget(target).values();
		for(Expression e : expressions) {
			if(e.isCorrect()) {
				if(e.getExpression().indexOf(CalculatorKey.PLUS.getStringRepresentation()) != -1) plus = true;
				if(e.getExpression().indexOf(CalculatorKey.MINUS.getStringRepresentation()) != -1) minus = true;
				if(e.getExpression().indexOf(CalculatorKey.DIVIDE.getStringRepresentation()) != -1) divide = true;
				if(e.getExpression().indexOf(CalculatorKey.MULTIPLY.getStringRepresentation()) != -1) multiply = true;
			}
		}
		
		return plus && minus && multiply && divide;
	}

	private int getBracketUseCount(Double target, String table) {
		Collection<Expression> expressions = TargetMaps.get().getDistributedMapForTarget(target).values();
		int count = 0;
		for(Expression e : expressions) {
			if(e.isCorrect() && 
			   e.getCreatedOnTable().equals(table) && 
			   (e.getExpression().contains("(") && e.getExpression().contains(")"))) {
				count++;
			}
		}
		return count;
	}

	private int getCorrectExpressionCount(Double target, String table) {
		Collection<Expression> expressions = TargetMaps.get().getDistributedMapForTarget(target).values();
		int count = 0;
		for(Expression e : expressions) {
			if(e.isCorrect() && e.getCreatedOnTable().equals(table)) {
				count++;
			}
		}
		return count;
	}
	
	private int getIncorrectExpressionCount(Double target, String table) {
		Collection<Expression> expressions = TargetMaps.get().getDistributedMapForTarget(target).values();
		int count = 0;
		for(Expression e : expressions) {
			if((!e.isCorrect()) && e.getCreatedOnTable().equals(table)) {
				count++;
			}
		}
		return count;
	}

	private String getTableForTarget(Double target) {
		for(TableTarget tt : TeacherControlComms.get().getTableTargets()) {
			if(tt.getTarget().equals(target)) {
				return tt.getTable();
			}
		}
		return null;
	}
	
	
}
