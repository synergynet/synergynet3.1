package synergynet3.web.apps.numbernet.client.service;

import java.util.List;
import java.util.Map;

import synergynet3.web.apps.numbernet.shared.CalculatorKey;
import synergynet3.web.apps.numbernet.shared.Expression;
import synergynet3.web.apps.numbernet.shared.Participant;
import synergynet3.web.apps.numbernet.shared.ProjectionDisplayMode;
import synergynet3.web.apps.numbernet.shared.TableTarget;


import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("NumberNetService")
public interface NumberNetService extends RemoteService {
	

	
	// table management
	public void addExpectedTable(String name);
	public List<String> getExpectedTables();

	// participant management
	public void addNameToTable(String name, String table);
	public List<Participant> getAllParticipants();
	public List<Participant> getNamesForTable(String table);
	public void removeFromTable(String name, String table);
	
	// class-wide control variables
	public void setTableInputEnabled(boolean enabled);	
	public void setCalculatorVisibility(boolean visible);
	public void setScoresVisibleForAllTables(boolean shouldBeVisible);	
	public void setGraphingModeEnabled(boolean graphingModeOn);

	// target setting
	public List<TableTarget> getTableTargets();
	public void setTableTargets(List<TableTarget> targets);
	public void rotateTableContentAndTargets(List<TableTarget> to);
	
	// expressions
	public List<Expression> getExpressionsForTable(String table);
	public List<Expression> getExpressionsForTarget(double target);
	public List<Expression> getExpressionsForPerson(String person);
	public void setIncorrectExpressionsVisibleForAllTables(boolean shouldBeVisible);
	public void setCorrectExpressionsVisibleForAllTables(boolean shouldBeVisible);
	public void setOthersIncorrectExpressionsVisibleForAllTables(boolean shouldBeVisible);
	public void setOthersCorrectExpressionsVisibleForAllTables(boolean shouldBeVisible);
	public List<Double> getAllTargetsThatHaveOneOrMoreExpressions();
	
	
	// calculator keys
	public boolean getCalculatorKeyStateForTable(String tableName, CalculatorKey key);
	public Map<CalculatorKey,Boolean> getCalculatorAllKeyStatesForTable(String tableName);
	public void setCalculatorKeyStateForTable(String tableName, CalculatorKey key, boolean state);
	public void setCalculatorKeyStateForAllTables(CalculatorKey key, boolean state);
	
	// projection
	public void projectTable(String table, String projector);
	public void setProjectorDisplayMode(String projector, ProjectionDisplayMode mode);
	public void updatePositionInformationFromProjector(String projector);
	public void setUnifyRotationModeEnabled(String projector, boolean enabled);
	
	/**
	 * Utility class for simplifying access to the instance of async service.
	 */
	public static class Util {
		private static NumberNetServiceAsync instance;
		public static NumberNetServiceAsync getInstance(){
			if (instance == null) {
				instance = GWT.create(NumberNetService.class);
			}
			return instance;
		}
	}

	
}
