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

/**
 * The Interface NumberNetService.
 */
@RemoteServiceRelativePath("NumberNetService")
public interface NumberNetService extends RemoteService {

	/**
	 * Utility class for simplifying access to the instance of async service.
	 */
	public static class Util {

		/** The instance. */
		private static NumberNetServiceAsync instance;

		/**
		 * Gets the single instance of Util.
		 *
		 * @return single instance of Util
		 */
		public static NumberNetServiceAsync getInstance() {
			if (instance == null) {
				instance = GWT.create(NumberNetService.class);
			}
			return instance;
		}
	}

	// table management
	/**
	 * Adds the expected table.
	 *
	 * @param name the name
	 */
	public void addExpectedTable(String name);

	// participant management
	/**
	 * Adds the name to table.
	 *
	 * @param name the name
	 * @param table the table
	 */
	public void addNameToTable(String name, String table);

	/**
	 * Gets the all participants.
	 *
	 * @return the all participants
	 */
	public List<Participant> getAllParticipants();

	/**
	 * Gets the all targets that have one or more expressions.
	 *
	 * @return the all targets that have one or more expressions
	 */
	public List<Double> getAllTargetsThatHaveOneOrMoreExpressions();

	/**
	 * Gets the calculator all key states for table.
	 *
	 * @param tableName the table name
	 * @return the calculator all key states for table
	 */
	public Map<CalculatorKey, Boolean> getCalculatorAllKeyStatesForTable(
			String tableName);

	// calculator keys
	/**
	 * Gets the calculator key state for table.
	 *
	 * @param tableName the table name
	 * @param key the key
	 * @return the calculator key state for table
	 */
	public boolean getCalculatorKeyStateForTable(String tableName,
			CalculatorKey key);

	/**
	 * Gets the expected tables.
	 *
	 * @return the expected tables
	 */
	public List<String> getExpectedTables();

	/**
	 * Gets the expressions for person.
	 *
	 * @param person the person
	 * @return the expressions for person
	 */
	public List<Expression> getExpressionsForPerson(String person);

	// expressions
	/**
	 * Gets the expressions for table.
	 *
	 * @param table the table
	 * @return the expressions for table
	 */
	public List<Expression> getExpressionsForTable(String table);

	/**
	 * Gets the expressions for target.
	 *
	 * @param target the target
	 * @return the expressions for target
	 */
	public List<Expression> getExpressionsForTarget(double target);

	/**
	 * Gets the names for table.
	 *
	 * @param table the table
	 * @return the names for table
	 */
	public List<Participant> getNamesForTable(String table);

	// target setting
	/**
	 * Gets the table targets.
	 *
	 * @return the table targets
	 */
	public List<TableTarget> getTableTargets();

	// projection
	/**
	 * Project table.
	 *
	 * @param table the table
	 * @param projector the projector
	 */
	public void projectTable(String table, String projector);

	/**
	 * Removes the from table.
	 *
	 * @param name the name
	 * @param table the table
	 */
	public void removeFromTable(String name, String table);

	/**
	 * Rotate table content and targets.
	 *
	 * @param to the to
	 */
	public void rotateTableContentAndTargets(List<TableTarget> to);

	/**
	 * Sets the calculator key state for all tables.
	 *
	 * @param key the key
	 * @param state the state
	 */
	public void setCalculatorKeyStateForAllTables(CalculatorKey key,
			boolean state);

	/**
	 * Sets the calculator key state for table.
	 *
	 * @param tableName the table name
	 * @param key the key
	 * @param state the state
	 */
	public void setCalculatorKeyStateForTable(String tableName,
			CalculatorKey key, boolean state);

	/**
	 * Sets the calculator visibility.
	 *
	 * @param visible the new calculator visibility
	 */
	public void setCalculatorVisibility(boolean visible);

	/**
	 * Sets the correct expressions visible for all tables.
	 *
	 * @param shouldBeVisible the new correct expressions visible for all tables
	 */
	public void setCorrectExpressionsVisibleForAllTables(boolean shouldBeVisible);

	/**
	 * Sets the graphing mode enabled.
	 *
	 * @param graphingModeOn the new graphing mode enabled
	 */
	public void setGraphingModeEnabled(boolean graphingModeOn);

	/**
	 * Sets the incorrect expressions visible for all tables.
	 *
	 * @param shouldBeVisible the new incorrect expressions visible for all
	 *            tables
	 */
	public void setIncorrectExpressionsVisibleForAllTables(
			boolean shouldBeVisible);

	/**
	 * Sets the others correct expressions visible for all tables.
	 *
	 * @param shouldBeVisible the new others correct expressions visible for all
	 *            tables
	 */
	public void setOthersCorrectExpressionsVisibleForAllTables(
			boolean shouldBeVisible);

	/**
	 * Sets the others incorrect expressions visible for all tables.
	 *
	 * @param shouldBeVisible the new others incorrect expressions visible for
	 *            all tables
	 */
	public void setOthersIncorrectExpressionsVisibleForAllTables(
			boolean shouldBeVisible);

	/**
	 * Sets the projector display mode.
	 *
	 * @param projector the projector
	 * @param mode the mode
	 */
	public void setProjectorDisplayMode(String projector,
			ProjectionDisplayMode mode);

	/**
	 * Sets the scores visible for all tables.
	 *
	 * @param shouldBeVisible the new scores visible for all tables
	 */
	public void setScoresVisibleForAllTables(boolean shouldBeVisible);

	// class-wide control variables
	/**
	 * Sets the table input enabled.
	 *
	 * @param enabled the new table input enabled
	 */
	public void setTableInputEnabled(boolean enabled);

	/**
	 * Sets the table targets.
	 *
	 * @param targets the new table targets
	 */
	public void setTableTargets(List<TableTarget> targets);

	/**
	 * Sets the unify rotation mode enabled.
	 *
	 * @param projector the projector
	 * @param enabled the enabled
	 */
	public void setUnifyRotationModeEnabled(String projector, boolean enabled);

	/**
	 * Update position information from projector.
	 *
	 * @param projector the projector
	 */
	public void updatePositionInformationFromProjector(String projector);

}
