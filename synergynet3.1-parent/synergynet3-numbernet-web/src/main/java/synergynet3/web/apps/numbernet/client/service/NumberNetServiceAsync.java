package synergynet3.web.apps.numbernet.client.service;

import java.util.List;
import java.util.Map;

import synergynet3.web.apps.numbernet.shared.CalculatorKey;
import synergynet3.web.apps.numbernet.shared.Expression;
import synergynet3.web.apps.numbernet.shared.Participant;
import synergynet3.web.apps.numbernet.shared.ProjectionDisplayMode;
import synergynet3.web.apps.numbernet.shared.TableTarget;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The Interface NumberNetServiceAsync.
 */
public interface NumberNetServiceAsync
{

	/**
	 * Adds the expected table.
	 *
	 * @param name
	 *            the name
	 * @param callback
	 *            the callback
	 */
	void addExpectedTable(String name, AsyncCallback<Void> callback);

	/**
	 * Adds the name to table.
	 *
	 * @param name
	 *            the name
	 * @param table
	 *            the table
	 * @param callback
	 *            the callback
	 */
	void addNameToTable(String name, String table, AsyncCallback<Void> callback);

	/**
	 * Gets the all participants.
	 *
	 * @param callback
	 *            the callback
	 * @return the all participants
	 */
	void getAllParticipants(AsyncCallback<List<Participant>> callback);

	/**
	 * Gets the all targets that have one or more expressions.
	 *
	 * @param callback
	 *            the callback
	 * @return the all targets that have one or more expressions
	 */
	void getAllTargetsThatHaveOneOrMoreExpressions(AsyncCallback<List<Double>> callback);

	/**
	 * Gets the calculator all key states for table.
	 *
	 * @param tableName
	 *            the table name
	 * @param callback
	 *            the callback
	 * @return the calculator all key states for table
	 */
	void getCalculatorAllKeyStatesForTable(String tableName, AsyncCallback<Map<CalculatorKey, Boolean>> callback);

	/**
	 * Gets the calculator key state for table.
	 *
	 * @param tableName
	 *            the table name
	 * @param key
	 *            the key
	 * @param callback
	 *            the callback
	 * @return the calculator key state for table
	 */
	void getCalculatorKeyStateForTable(String tableName, CalculatorKey key, AsyncCallback<Boolean> callback);

	/**
	 * Gets the expected tables.
	 *
	 * @param callback
	 *            the callback
	 * @return the expected tables
	 */
	void getExpectedTables(AsyncCallback<List<String>> callback);

	/**
	 * Gets the expressions for person.
	 *
	 * @param person
	 *            the person
	 * @param callback
	 *            the callback
	 * @return the expressions for person
	 */
	void getExpressionsForPerson(String person, AsyncCallback<List<Expression>> callback);

	/**
	 * Gets the expressions for table.
	 *
	 * @param table
	 *            the table
	 * @param callback
	 *            the callback
	 * @return the expressions for table
	 */
	void getExpressionsForTable(String table, AsyncCallback<List<Expression>> callback);

	/**
	 * Gets the expressions for target.
	 *
	 * @param target
	 *            the target
	 * @param callback
	 *            the callback
	 * @return the expressions for target
	 */
	void getExpressionsForTarget(double target, AsyncCallback<List<Expression>> callback);

	/**
	 * Gets the names for table.
	 *
	 * @param table
	 *            the table
	 * @param callback
	 *            the callback
	 * @return the names for table
	 */
	void getNamesForTable(String table, AsyncCallback<List<Participant>> callback);

	/**
	 * Gets the table targets.
	 *
	 * @param callback
	 *            the callback
	 * @return the table targets
	 */
	void getTableTargets(AsyncCallback<List<TableTarget>> callback);

	/**
	 * Project table.
	 *
	 * @param table
	 *            the table
	 * @param projector
	 *            the projector
	 * @param callback
	 *            the callback
	 */
	void projectTable(String table, String projector, AsyncCallback<Void> callback);

	/**
	 * Removes the from table.
	 *
	 * @param name
	 *            the name
	 * @param table
	 *            the table
	 * @param callback
	 *            the callback
	 */
	void removeFromTable(String name, String table, AsyncCallback<Void> callback);

	/**
	 * Rotate table content and targets.
	 *
	 * @param to
	 *            the to
	 * @param callback
	 *            the callback
	 */
	void rotateTableContentAndTargets(List<TableTarget> to, AsyncCallback<Void> callback);

	/**
	 * Sets the calculator key state for all tables.
	 *
	 * @param key
	 *            the key
	 * @param state
	 *            the state
	 * @param callback
	 *            the callback
	 */
	void setCalculatorKeyStateForAllTables(CalculatorKey key, boolean state, AsyncCallback<Void> callback);

	/**
	 * Sets the calculator key state for table.
	 *
	 * @param tableName
	 *            the table name
	 * @param key
	 *            the key
	 * @param state
	 *            the state
	 * @param callback
	 *            the callback
	 */
	void setCalculatorKeyStateForTable(String tableName, CalculatorKey key, boolean state, AsyncCallback<Void> callback);

	/**
	 * Sets the calculator visibility.
	 *
	 * @param visible
	 *            the visible
	 * @param callback
	 *            the callback
	 */
	void setCalculatorVisibility(boolean visible, AsyncCallback<Void> callback);

	/**
	 * Sets the correct expressions visible for all tables.
	 *
	 * @param shouldBeVisible
	 *            the should be visible
	 * @param callback
	 *            the callback
	 */
	void setCorrectExpressionsVisibleForAllTables(boolean shouldBeVisible, AsyncCallback<Void> callback);

	/**
	 * Sets the graphing mode enabled.
	 *
	 * @param graphingModeOn
	 *            the graphing mode on
	 * @param callback
	 *            the callback
	 */
	void setGraphingModeEnabled(boolean graphingModeOn, AsyncCallback<Void> callback);

	/**
	 * Sets the incorrect expressions visible for all tables.
	 *
	 * @param shouldBeVisible
	 *            the should be visible
	 * @param callback
	 *            the callback
	 */
	void setIncorrectExpressionsVisibleForAllTables(boolean shouldBeVisible, AsyncCallback<Void> callback);

	/**
	 * Sets the others correct expressions visible for all tables.
	 *
	 * @param shouldBeVisible
	 *            the should be visible
	 * @param callback
	 *            the callback
	 */
	void setOthersCorrectExpressionsVisibleForAllTables(boolean shouldBeVisible, AsyncCallback<Void> callback);

	/**
	 * Sets the others incorrect expressions visible for all tables.
	 *
	 * @param shouldBeVisible
	 *            the should be visible
	 * @param callback
	 *            the callback
	 */
	void setOthersIncorrectExpressionsVisibleForAllTables(boolean shouldBeVisible, AsyncCallback<Void> callback);

	/**
	 * Sets the projector display mode.
	 *
	 * @param projector
	 *            the projector
	 * @param mode
	 *            the mode
	 * @param callback
	 *            the callback
	 */
	void setProjectorDisplayMode(String projector, ProjectionDisplayMode mode, AsyncCallback<Void> callback);

	/**
	 * Sets the scores visible for all tables.
	 *
	 * @param shouldBeVisible
	 *            the should be visible
	 * @param asyncCallback
	 *            the async callback
	 */
	void setScoresVisibleForAllTables(boolean shouldBeVisible, AsyncCallback<Void> asyncCallback);

	/**
	 * Sets the table input enabled.
	 *
	 * @param enabled
	 *            the enabled
	 * @param callback
	 *            the callback
	 */
	void setTableInputEnabled(boolean enabled, AsyncCallback<Void> callback);

	/**
	 * Sets the table targets.
	 *
	 * @param targets
	 *            the targets
	 * @param callback
	 *            the callback
	 */
	void setTableTargets(List<TableTarget> targets, AsyncCallback<Void> callback);

	/**
	 * Sets the unify rotation mode enabled.
	 *
	 * @param projector
	 *            the projector
	 * @param enabled
	 *            the enabled
	 * @param callback
	 *            the callback
	 */
	void setUnifyRotationModeEnabled(String projector, boolean enabled, AsyncCallback<Void> callback);

	/**
	 * Update position information from projector.
	 *
	 * @param projector
	 *            the projector
	 * @param callback
	 *            the callback
	 */
	void updatePositionInformationFromProjector(String projector, AsyncCallback<Void> callback);
}
