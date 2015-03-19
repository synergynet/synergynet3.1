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

public class NumberNetServiceImpl extends RemoteServiceServlet implements NumberNetService {
	private static final long serialVersionUID = -8692351104428199812L;
	
	static {
		new DataWriter();
	}

	@Override
	public void setCalculatorVisibility(boolean visible) {
		TeacherControlComms.get().setAllCalculatorsVisible(visible);
	}

	@Override
	public void addExpectedTable(String name) {
		TeacherControlComms.get().addExpectedTable(name);		
	}

	@Override
	public List<String> getExpectedTables() {
		return TeacherControlComms.get().getExpectedTablesList();
	}

	@Override
	public void addNameToTable(String name, String table) {
		TeacherControlComms.get().assignParticipantToTable(table, name);		
	}

	@Override
	public List<Participant> getNamesForTable(String table) {
		return TeacherControlComms.get().getParticipantListForTable(table);
	}

	@Override
	public void removeFromTable(String name, String table) {
		TeacherControlComms.get().removeParticipantFromTable(table, name);		
	}

	@Override
	public List<TableTarget> getTableTargets() {
		return TeacherControlComms.get().getTableTargets();
	}

	@Override
	public void setTableTargets(List<TableTarget> targets) {
		TeacherControlComms.get().setTableTargets(targets);
	}

	@Override
	public List<Expression> getExpressionsForTable(String table) {
		return TeacherControlComms.get().getExpressionsForTable(table);
	}
	
	@Override
	public List<Expression> getExpressionsForTarget(double target) {
		return TeacherControlComms.get().getExpressionsForTarget(target);
	}

	@Override
	public List<Expression> getExpressionsForPerson(String person) {
		return TeacherControlComms.get().getExpressionsForPerson(person);
	}

	@Override
	public void setTableInputEnabled(boolean enabled) {
		TeacherControlComms.get().setTableInputEnabled(enabled);
		
	}

	@Override
	public void projectTable(String table, String projector) {
		TeacherControlComms.get().projectTable(table, projector);
	}

	@Override
	public void rotateTableContentAndTargets(List<TableTarget> to) {
		TeacherControlComms.get().rotateTableContentAndTargets(to);
	}

	@Override
	public void setCalculatorKeyStateForAllTables(CalculatorKey key, boolean state) {
		TeacherControlComms.get().setCalculatorKeyStateForAllTables(key, state);
	}

	@Override
	public void setCalculatorKeyStateForTable(String tableName, CalculatorKey key, boolean state) {
		Map<CalculatorKey, Boolean> map = TeacherControlComms.get().getStudentTableDeviceForName(tableName).getCalculatorKeyStateMap().getValue();
		if (map == null)map = new HashMap<CalculatorKey,Boolean>();
		map.put(key,  state);
		TeacherControlComms.get().getStudentTableDeviceForName(tableName).getCalculatorKeyStateMap().setValue(map);
	}

	@Override
	public boolean getCalculatorKeyStateForTable(String tableName, CalculatorKey key) {
		Map<CalculatorKey, Boolean> map = TeacherControlComms.get().getStudentTableDeviceForName(tableName).getCalculatorKeyStateMap().getValue();
		if (map == null)map = new HashMap<CalculatorKey,Boolean>();
		if (map.containsKey(key))return map.get(key);
		return false;
	}

	@Override
	public Map<CalculatorKey, Boolean> getCalculatorAllKeyStatesForTable(String tableName) {
		return TeacherControlComms.get().getCalculatorAllKeyStatesForTable(tableName);
	}

	@Override
	public void setIncorrectExpressionsVisibleForAllTables(
			boolean shouldBeVisible) {
		TeacherControlComms.get().setIncorrectExpressionsVisibleForAllTables(shouldBeVisible);
	}

	@Override
	public void setScoresVisibleForAllTables(boolean shouldBeVisible) {
		TeacherControlComms.get().setScoresVisibleForAllTables(shouldBeVisible);
	}

	@Override
	public void setCorrectExpressionsVisibleForAllTables(boolean shouldBeVisible) {
		TeacherControlComms.get().setCorrectExpressionsVisibleForAllTables(shouldBeVisible);
	}

	@Override
	public void setGraphingModeEnabled(boolean graphingModeOn) {
		TeacherControlComms.get().setGraphingModeEnabled(graphingModeOn);
	}

	@Override
	public void updatePositionInformationFromProjector(String projector) {
		TeacherControlComms.get().updatePositionInformationFromProjector(projector);
	}

	@Override
	public void setOthersIncorrectExpressionsVisibleForAllTables(
			boolean shouldBeVisible) {
		TeacherControlComms.get().setOthersIncorrectExpressionsVisibleForAllTables(shouldBeVisible);
	}

	@Override
	public void setOthersCorrectExpressionsVisibleForAllTables(
			boolean shouldBeVisible) {
		TeacherControlComms.get().setOthersCorrectExpressionsVisibleForAllTables(shouldBeVisible);
	}

	@Override
	public List<Double> getAllTargetsThatHaveOneOrMoreExpressions() {
		return TeacherControlComms.get().getAllTargetsThatHaveOneOrMoreExpressions();
	}

	@Override
	public List<Participant> getAllParticipants() {
		return TeacherControlComms.get().getAllParticipants();
	}

	@Override
	public void setUnifyRotationModeEnabled(String projector, boolean enabled) {
		TeacherControlComms.get().setUnifyRotationModeEnabled(projector, enabled);
	}

	@Override
	public void setProjectorDisplayMode(String projector,
			ProjectionDisplayMode mode) {
		TeacherControlComms.get().setProjectorDisplayMode(projector, mode);
	}

}
