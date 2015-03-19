package synergynet3.web.apps.numbernet.client.service;

import java.util.List;
import java.util.Map;

import synergynet3.web.apps.numbernet.shared.CalculatorKey;
import synergynet3.web.apps.numbernet.shared.Expression;
import synergynet3.web.apps.numbernet.shared.Participant;
import synergynet3.web.apps.numbernet.shared.ProjectionDisplayMode;
import synergynet3.web.apps.numbernet.shared.TableTarget;


import com.google.gwt.user.client.rpc.AsyncCallback;

public interface NumberNetServiceAsync {
	void setCalculatorVisibility(boolean visible, AsyncCallback<Void> callback);
	void addExpectedTable(String name, AsyncCallback<Void> callback);
	void getExpectedTables(AsyncCallback<List<String>> callback);
	void addNameToTable(String name, String table, AsyncCallback<Void> callback);
	void getNamesForTable(String table, AsyncCallback<List<Participant>> callback);
	void removeFromTable(String name, String table, AsyncCallback<Void> callback);
	void getTableTargets(AsyncCallback<List<TableTarget>> callback);
	void setTableTargets(List<TableTarget> targets, AsyncCallback<Void> callback);
	void getExpressionsForTable(String table, AsyncCallback<List<Expression>> callback);
	void setTableInputEnabled(boolean enabled, AsyncCallback<Void> callback);
	void projectTable(String table, String projector,
			AsyncCallback<Void> callback);
	void rotateTableContentAndTargets(List<TableTarget> to, AsyncCallback<Void> callback);
	void setCalculatorKeyStateForAllTables(CalculatorKey key, boolean state,
			AsyncCallback<Void> callback);
	void setCalculatorKeyStateForTable(String tableName, CalculatorKey key,
			boolean state, AsyncCallback<Void> callback);
	void getCalculatorKeyStateForTable(String tableName, CalculatorKey key,
			AsyncCallback<Boolean> callback);
	void getCalculatorAllKeyStatesForTable(String tableName,
			AsyncCallback<Map<CalculatorKey, Boolean>> callback);
	void setIncorrectExpressionsVisibleForAllTables(boolean shouldBeVisible,
			AsyncCallback<Void> callback);
	void setScoresVisibleForAllTables(boolean shouldBeVisible,
			AsyncCallback<Void> asyncCallback);
	void setCorrectExpressionsVisibleForAllTables(boolean shouldBeVisible,
			AsyncCallback<Void> callback);
	void setGraphingModeEnabled(boolean graphingModeOn,
			AsyncCallback<Void> callback);
	void updatePositionInformationFromProjector(String projector, AsyncCallback<Void> callback);
	void setOthersIncorrectExpressionsVisibleForAllTables(
			boolean shouldBeVisible, AsyncCallback<Void> callback);
	void setOthersCorrectExpressionsVisibleForAllTables(
			boolean shouldBeVisible, AsyncCallback<Void> callback);
	void getAllTargetsThatHaveOneOrMoreExpressions(
			AsyncCallback<List<Double>> callback);
	void getAllParticipants(AsyncCallback<List<Participant>> callback);
	void getExpressionsForTarget(double target,
			AsyncCallback<List<Expression>> callback);
	void getExpressionsForPerson(String person,
			AsyncCallback<List<Expression>> callback);
	void setUnifyRotationModeEnabled(String projector, boolean enabled,
			AsyncCallback<Void> callback);
	void setProjectorDisplayMode(String projector, ProjectionDisplayMode mode,
			AsyncCallback<Void> callback);
}
