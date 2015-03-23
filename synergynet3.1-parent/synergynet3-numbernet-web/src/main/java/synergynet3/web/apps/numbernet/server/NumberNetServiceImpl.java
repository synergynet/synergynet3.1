package synergynet3.web.apps.numbernet.server;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import synergynet3.web.apps.numbernet.client.service.NumberNetService;
import synergynet3.web.apps.numbernet.comms.teachercontrol.TeacherControlComms;
import synergynet3.web.apps.numbernet.persistence.DataWriter;
import synergynet3.web.apps.numbernet.shared.CalculatorKey;
import synergynet3.web.apps.numbernet.shared.Expression;
import synergynet3.web.apps.numbernet.shared.Participant;
import synergynet3.web.apps.numbernet.shared.ProjectionDisplayMode;
import synergynet3.web.apps.numbernet.shared.TableTarget;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The Class NumberNetServiceImpl.
 */
public class NumberNetServiceImpl extends RemoteServiceServlet implements
		NumberNetService {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -8692351104428199812L;

	static {
		new DataWriter();
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.web.apps.numbernet.client.service.NumberNetService#
	 * addExpectedTable(java.lang.String)
	 */
	@Override
	public void addExpectedTable(String name) {
		TeacherControlComms.get().addExpectedTable(name);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynet3.web.apps.numbernet.client.service.NumberNetService#addNameToTable
	 * (java.lang.String, java.lang.String)
	 */
	@Override
	public void addNameToTable(String name, String table) {
		TeacherControlComms.get().assignParticipantToTable(table, name);
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.web.apps.numbernet.client.service.NumberNetService#
	 * getAllParticipants()
	 */
	@Override
	public List<Participant> getAllParticipants() {
		return TeacherControlComms.get().getAllParticipants();
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.web.apps.numbernet.client.service.NumberNetService#
	 * getAllTargetsThatHaveOneOrMoreExpressions()
	 */
	@Override
	public List<Double> getAllTargetsThatHaveOneOrMoreExpressions() {
		return TeacherControlComms.get()
				.getAllTargetsThatHaveOneOrMoreExpressions();
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.web.apps.numbernet.client.service.NumberNetService#
	 * getCalculatorAllKeyStatesForTable(java.lang.String)
	 */
	@Override
	public Map<CalculatorKey, Boolean> getCalculatorAllKeyStatesForTable(
			String tableName) {
		return TeacherControlComms.get().getCalculatorAllKeyStatesForTable(
				tableName);
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.web.apps.numbernet.client.service.NumberNetService#
	 * getCalculatorKeyStateForTable(java.lang.String,
	 * synergynet3.web.apps.numbernet.shared.CalculatorKey)
	 */
	@Override
	public boolean getCalculatorKeyStateForTable(String tableName,
			CalculatorKey key) {
		Map<CalculatorKey, Boolean> map = TeacherControlComms.get()
				.getStudentTableDeviceForName(tableName)
				.getCalculatorKeyStateMap().getValue();
		if (map == null) {
			map = new HashMap<CalculatorKey, Boolean>();
		}
		if (map.containsKey(key)) {
			return map.get(key);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.web.apps.numbernet.client.service.NumberNetService#
	 * getExpectedTables()
	 */
	@Override
	public List<String> getExpectedTables() {
		return TeacherControlComms.get().getExpectedTablesList();
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.web.apps.numbernet.client.service.NumberNetService#
	 * getExpressionsForPerson(java.lang.String)
	 */
	@Override
	public List<Expression> getExpressionsForPerson(String person) {
		return TeacherControlComms.get().getExpressionsForPerson(person);
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.web.apps.numbernet.client.service.NumberNetService#
	 * getExpressionsForTable(java.lang.String)
	 */
	@Override
	public List<Expression> getExpressionsForTable(String table) {
		return TeacherControlComms.get().getExpressionsForTable(table);
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.web.apps.numbernet.client.service.NumberNetService#
	 * getExpressionsForTarget(double)
	 */
	@Override
	public List<Expression> getExpressionsForTarget(double target) {
		return TeacherControlComms.get().getExpressionsForTarget(target);
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.web.apps.numbernet.client.service.NumberNetService#
	 * getNamesForTable(java.lang.String)
	 */
	@Override
	public List<Participant> getNamesForTable(String table) {
		return TeacherControlComms.get().getParticipantListForTable(table);
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.web.apps.numbernet.client.service.NumberNetService#
	 * getTableTargets()
	 */
	@Override
	public List<TableTarget> getTableTargets() {
		return TeacherControlComms.get().getTableTargets();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynet3.web.apps.numbernet.client.service.NumberNetService#projectTable
	 * (java.lang.String, java.lang.String)
	 */
	@Override
	public void projectTable(String table, String projector) {
		TeacherControlComms.get().projectTable(table, projector);
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.web.apps.numbernet.client.service.NumberNetService#
	 * removeFromTable(java.lang.String, java.lang.String)
	 */
	@Override
	public void removeFromTable(String name, String table) {
		TeacherControlComms.get().removeParticipantFromTable(table, name);
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.web.apps.numbernet.client.service.NumberNetService#
	 * rotateTableContentAndTargets(java.util.List)
	 */
	@Override
	public void rotateTableContentAndTargets(List<TableTarget> to) {
		TeacherControlComms.get().rotateTableContentAndTargets(to);
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.web.apps.numbernet.client.service.NumberNetService#
	 * setCalculatorKeyStateForAllTables
	 * (synergynet3.web.apps.numbernet.shared.CalculatorKey, boolean)
	 */
	@Override
	public void setCalculatorKeyStateForAllTables(CalculatorKey key,
			boolean state) {
		TeacherControlComms.get().setCalculatorKeyStateForAllTables(key, state);
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.web.apps.numbernet.client.service.NumberNetService#
	 * setCalculatorKeyStateForTable(java.lang.String,
	 * synergynet3.web.apps.numbernet.shared.CalculatorKey, boolean)
	 */
	@Override
	public void setCalculatorKeyStateForTable(String tableName,
			CalculatorKey key, boolean state) {
		Map<CalculatorKey, Boolean> map = TeacherControlComms.get()
				.getStudentTableDeviceForName(tableName)
				.getCalculatorKeyStateMap().getValue();
		if (map == null) {
			map = new HashMap<CalculatorKey, Boolean>();
		}
		map.put(key, state);
		TeacherControlComms.get().getStudentTableDeviceForName(tableName)
				.getCalculatorKeyStateMap().setValue(map);
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.web.apps.numbernet.client.service.NumberNetService#
	 * setCalculatorVisibility(boolean)
	 */
	@Override
	public void setCalculatorVisibility(boolean visible) {
		TeacherControlComms.get().setAllCalculatorsVisible(visible);
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.web.apps.numbernet.client.service.NumberNetService#
	 * setCorrectExpressionsVisibleForAllTables(boolean)
	 */
	@Override
	public void setCorrectExpressionsVisibleForAllTables(boolean shouldBeVisible) {
		TeacherControlComms.get().setCorrectExpressionsVisibleForAllTables(
				shouldBeVisible);
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.web.apps.numbernet.client.service.NumberNetService#
	 * setGraphingModeEnabled(boolean)
	 */
	@Override
	public void setGraphingModeEnabled(boolean graphingModeOn) {
		TeacherControlComms.get().setGraphingModeEnabled(graphingModeOn);
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.web.apps.numbernet.client.service.NumberNetService#
	 * setIncorrectExpressionsVisibleForAllTables(boolean)
	 */
	@Override
	public void setIncorrectExpressionsVisibleForAllTables(
			boolean shouldBeVisible) {
		TeacherControlComms.get().setIncorrectExpressionsVisibleForAllTables(
				shouldBeVisible);
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.web.apps.numbernet.client.service.NumberNetService#
	 * setOthersCorrectExpressionsVisibleForAllTables(boolean)
	 */
	@Override
	public void setOthersCorrectExpressionsVisibleForAllTables(
			boolean shouldBeVisible) {
		TeacherControlComms
				.get()
				.setOthersCorrectExpressionsVisibleForAllTables(shouldBeVisible);
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.web.apps.numbernet.client.service.NumberNetService#
	 * setOthersIncorrectExpressionsVisibleForAllTables(boolean)
	 */
	@Override
	public void setOthersIncorrectExpressionsVisibleForAllTables(
			boolean shouldBeVisible) {
		TeacherControlComms.get()
				.setOthersIncorrectExpressionsVisibleForAllTables(
						shouldBeVisible);
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.web.apps.numbernet.client.service.NumberNetService#
	 * setProjectorDisplayMode(java.lang.String,
	 * synergynet3.web.apps.numbernet.shared.ProjectionDisplayMode)
	 */
	@Override
	public void setProjectorDisplayMode(String projector,
			ProjectionDisplayMode mode) {
		TeacherControlComms.get().setProjectorDisplayMode(projector, mode);
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.web.apps.numbernet.client.service.NumberNetService#
	 * setScoresVisibleForAllTables(boolean)
	 */
	@Override
	public void setScoresVisibleForAllTables(boolean shouldBeVisible) {
		TeacherControlComms.get().setScoresVisibleForAllTables(shouldBeVisible);
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.web.apps.numbernet.client.service.NumberNetService#
	 * setTableInputEnabled(boolean)
	 */
	@Override
	public void setTableInputEnabled(boolean enabled) {
		TeacherControlComms.get().setTableInputEnabled(enabled);

	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.web.apps.numbernet.client.service.NumberNetService#
	 * setTableTargets(java.util.List)
	 */
	@Override
	public void setTableTargets(List<TableTarget> targets) {
		TeacherControlComms.get().setTableTargets(targets);
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.web.apps.numbernet.client.service.NumberNetService#
	 * setUnifyRotationModeEnabled(java.lang.String, boolean)
	 */
	@Override
	public void setUnifyRotationModeEnabled(String projector, boolean enabled) {
		TeacherControlComms.get().setUnifyRotationModeEnabled(projector,
				enabled);
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.web.apps.numbernet.client.service.NumberNetService#
	 * updatePositionInformationFromProjector(java.lang.String)
	 */
	@Override
	public void updatePositionInformationFromProjector(String projector) {
		TeacherControlComms.get().updatePositionInformationFromProjector(
				projector);
	}

}
