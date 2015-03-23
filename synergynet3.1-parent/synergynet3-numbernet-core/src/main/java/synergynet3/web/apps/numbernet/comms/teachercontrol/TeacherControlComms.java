package synergynet3.web.apps.numbernet.comms.teachercontrol;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import synergynet3.cluster.SynergyNetCluster;
import synergynet3.web.apps.numbernet.comms.table.NumberNetStudentTableClusteredData;
import synergynet3.web.apps.numbernet.comms.table.ProjectorDevice;
import synergynet3.web.apps.numbernet.comms.table.TargetMaps;
import synergynet3.web.apps.numbernet.shared.CalculatorKey;
import synergynet3.web.apps.numbernet.shared.Expression;
import synergynet3.web.apps.numbernet.shared.Participant;
import synergynet3.web.apps.numbernet.shared.ProjectionDisplayMode;
import synergynet3.web.apps.numbernet.shared.TableTarget;

import com.hazelcast.core.Hazelcast;

/**
 * The Class TeacherControlComms.
 */
public class TeacherControlComms {

	/** The instance. */
	private static TeacherControlComms instance;

	/** The Constant log. */
	private static final Logger log = Logger
			.getLogger(TeacherControlComms.class.getName());

	/**
	 * Instantiates a new teacher control comms.
	 */
	private TeacherControlComms() {
		log.info(getClass().getName() + " with cluster time "
				+ Hazelcast.getCluster().getClusterTime());
	}

	/**
	 * Gets the.
	 *
	 * @return the teacher control comms
	 */
	public static TeacherControlComms get() {
		synchronized (TeacherControlComms.class) {
			if (instance == null) {
				instance = new TeacherControlComms();
			}
			return instance;
		}
	}

	/**
	 * Adds the expected table.
	 *
	 * @param name the name
	 */
	public void addExpectedTable(String name) {
		getStudentTableDeviceForName(name); // this will auto-add a device for
											// the table
	}

	/**
	 * Assign participant to table.
	 *
	 * @param table the table
	 * @param participant the participant
	 */
	public void assignParticipantToTable(String table, String participant) {
		log.info("Adding " + participant + " to " + table);
		List<Participant> list = getStudentTableDeviceForName(table)
				.getParticipantListControlVariable().getValue();
		log.info("Current list: " + list);
		if (list == null) {
			list = new ArrayList<Participant>();
		}
		list.add(new Participant(participant));
		log.info("New list: " + list);
		getStudentTableDeviceForName(table).getParticipantListControlVariable()
				.setValue(list);
	}

	/**
	 * Gets the all participants.
	 *
	 * @return the all participants
	 */
	public List<Participant> getAllParticipants() {
		List<Participant> participants = new ArrayList<Participant>();

		for (String table : getExpectedTablesList()) {
			participants.addAll(getParticipantListForTable(table));
		}

		return participants;
	}

	/**
	 * Gets the all targets that have one or more expressions.
	 *
	 * @return the all targets that have one or more expressions
	 */
	public List<Double> getAllTargetsThatHaveOneOrMoreExpressions() {
		List<Double> targetsList = TargetMaps.get().getTargetsList();
		return targetsList;
	}

	/**
	 * Gets the calculator all key states for table.
	 *
	 * @param tableName the table name
	 * @return the calculator all key states for table
	 */
	public Map<CalculatorKey, Boolean> getCalculatorAllKeyStatesForTable(
			String tableName) {
		Map<CalculatorKey, Boolean> map = TeacherControlComms.get()
				.getStudentTableDeviceForName(tableName)
				.getCalculatorKeyStateMap().getValue();
		if (map == null) {
			map = new HashMap<CalculatorKey, Boolean>();
		}
		return map;
	}

	/**
	 * Gets the expected tables list.
	 *
	 * @return the expected tables list
	 */
	public List<String> getExpectedTablesList() {
		List<String> list = new ArrayList<String>();
		list.addAll(SynergyNetCluster.get().getDeviceClusterManager()
				.getNames());
		list = removeDevicesFromGroup(list, "projectors");
		list = removeDevicesFromGroup(list, "trackers");
		list = removeDevicesFromGroup(list, "multiplexers");
		list = removeDevicesFromGroup(list, "teacherconsoles");
		List<String> online = SynergyNetCluster.get().getPresenceManager()
				.getDeviceNamesOnline("tables");
		for (String o : online) {
			if (!list.contains(o)) {
				list.add(o);
			}
		}
		return list;
	}

	/**
	 * Gets the expressions for person.
	 *
	 * @param person the person
	 * @return the expressions for person
	 */
	public List<Expression> getExpressionsForPerson(String person) {
		List<Expression> list = new ArrayList<Expression>();

		List<TableTarget> tablesAndTargets = getTableTargets();
		for (TableTarget tt : tablesAndTargets) {
			Double target = tt.getTarget();
			if (target != null) {
				Collection<Expression> expressions = TargetMaps.get()
						.getDistributedMapForTarget(target).values();
				for (Expression e : expressions) {
					if (e.getCreatedBy().equals(person)) {
						list.add(e);
					}
				}
			}
		}

		return list;
	}

	/**
	 * Gets the expressions for table.
	 *
	 * @param table the table
	 * @return the expressions for table
	 */
	public List<Expression> getExpressionsForTable(String table) {
		List<Expression> list = new ArrayList<Expression>();
		Double value = getStudentTableDeviceForName(table)
				.getTargetValueControlVariable().getValue();
		if (value != null) {
			list.addAll(TargetMaps.get().getDistributedMapForTarget(value)
					.values());
		}
		return list;
	}

	/**
	 * Gets the expressions for target.
	 *
	 * @param target the target
	 * @return the expressions for target
	 */
	public List<Expression> getExpressionsForTarget(double target) {
		List<Expression> list = new ArrayList<Expression>();
		list.addAll(TargetMaps.get().getDistributedMapForTarget(target)
				.values());
		return list;
	}

	/**
	 * Gets the participant list for table.
	 *
	 * @param table the table
	 * @return the participant list for table
	 */
	public List<Participant> getParticipantListForTable(String table) {
		log.info("Getting participant list.");
		List<Participant> list = getStudentTableDeviceForName(table)
				.getParticipantListControlVariable().getValue();
		if (list == null) {
			list = new ArrayList<Participant>();
		}
		log.info("Returning: " + list);
		return list;
	}

	/**
	 * Gets the projector device for name.
	 *
	 * @param device the device
	 * @return the projector device for name
	 */
	public ProjectorDevice getProjectorDeviceForName(String device) {
		try {
			ProjectorDevice pd = (ProjectorDevice) SynergyNetCluster.get()
					.getDeviceClusterManager().getClusteredDeviceByName(device);
			if (pd == null) {
				pd = new ProjectorDevice(device);
			}
			return pd;
		} catch (ClassCastException e) {
			return new ProjectorDevice(device);
		}
	}

	// ********** table targets **********

	/**
	 * Gets the student table device for name.
	 *
	 * @param device the device
	 * @return the student table device for name
	 */
	public NumberNetStudentTableClusteredData getStudentTableDeviceForName(
			String device) {
		try {
			NumberNetStudentTableClusteredData std = (NumberNetStudentTableClusteredData) SynergyNetCluster
					.get().getDeviceClusterManager()
					.getClusteredDeviceByName(device);
			if (std == null) {
				std = new NumberNetStudentTableClusteredData(device);
			}
			return std;
		} catch (ClassCastException e) {
			return new NumberNetStudentTableClusteredData(device);
		}
	}

	/**
	 * Gets the table targets.
	 *
	 * @return the table targets
	 */
	public List<TableTarget> getTableTargets() {
		List<TableTarget> list = new ArrayList<TableTarget>();
		for (String table : getExpectedTablesList()) {
			Double value = getStudentTableDeviceForName(table)
					.getTargetValueControlVariable().getValue();
			TableTarget tt = new TableTarget(table, value);
			list.add(tt);
		}
		log.info("There are " + list.size()
				+ " items in the table to target mapping list.");
		return list;
	}

	/**
	 * Project table.
	 *
	 * @param table the table
	 * @param projector the projector
	 */
	public void projectTable(String table, String projector) {
		log.info("Projecting " + table + " onto " + projector);
		Double value = getStudentTableDeviceForName(table)
				.getTargetValueControlVariable().getValue();
		if (value != null) {
			getProjectorDeviceForName(projector)
					.getTargetToShowControlVariable().setValue(value);
			setProjectorDisplayMode(projector,
					ProjectionDisplayMode.TABLE_CLONE);
		}
	}

	/**
	 * Removes the participant from table.
	 *
	 * @param table the table
	 * @param name the name
	 */
	public void removeParticipantFromTable(String table, String name) {
		log.info("Removing " + name + " from " + table);
		List<Participant> currentList = getStudentTableDeviceForName(table)
				.getParticipantListControlVariable().getValue();
		if (currentList == null) {
			currentList = new ArrayList<Participant>();
		}
		ArrayList<Participant> newList = new ArrayList<Participant>();
		for (Participant p : currentList) {
			if (!p.getName().equals(name)) {
				newList.add(p);
			}
		}
		getStudentTableDeviceForName(table).getParticipantListControlVariable()
				.setValue(newList);
	}

	/**
	 * Rotate table content and targets.
	 *
	 * @param to the to
	 */
	public void rotateTableContentAndTargets(List<TableTarget> to) {

		Map<String, Double> currentTargets = new HashMap<String, Double>();
		for (String table : getExpectedTablesList()) {
			currentTargets.put(table, getStudentTableDeviceForName(table)
					.getTargetValueControlVariable().getValue());
		}

		Map<String, Double> assignTargets = new HashMap<String, Double>();
		for (TableTarget tableTarget : to) {
			assignTargets.put(tableTarget.getTable(), tableTarget.getTarget());
		}

		for (String table : getExpectedTablesList()) {
			try {
				getStudentTableDeviceForName(table)
						.getTargetValueControlVariable().setValue(
								assignTargets.get(table));
			} catch (NullPointerException e) {
				// Stop trying to get table for projector
			}
		}

	}

	/**
	 * Sets the all calculators visible.
	 *
	 * @param visible the new all calculators visible
	 */
	public void setAllCalculatorsVisible(boolean visible) {
		// for(String device :
		// SynergyNetCluster.get().getClusteredDataManager().getNamesOfDevicesInCluster(NumberNetStudentTableClusteredData.deviceType))
		// {
		for (String device : getExpectedTablesList()) {
			try {
				NumberNetStudentTableClusteredData std = getStudentTableDeviceForName(device);
				std.getCalculatorVisibleControlVariable().setValue(visible);
			} catch (Exception ex) {
			}
		}
	}

	/**
	 * Sets the calculator key state for all tables.
	 *
	 * @param key the key
	 * @param state the state
	 */
	public void setCalculatorKeyStateForAllTables(CalculatorKey key,
			boolean state) {
		for (String device : getExpectedTablesList()) {
			Map<CalculatorKey, Boolean> map = TeacherControlComms.get()
					.getStudentTableDeviceForName(device)
					.getCalculatorKeyStateMap().getValue();
			if (map == null) {
				map = new HashMap<CalculatorKey, Boolean>();
			}
			map.put(key, state);
			TeacherControlComms.get().getStudentTableDeviceForName(device)
					.getCalculatorKeyStateMap().setValue(map);
		}
	}

	/**
	 * Sets the correct expressions visible for all tables.
	 *
	 * @param shouldBeVisible the new correct expressions visible for all tables
	 */
	public void setCorrectExpressionsVisibleForAllTables(boolean shouldBeVisible) {
		for (String device : getExpectedTablesList()) {
			getStudentTableDeviceForName(device)
					.getCorrectExpressionsVisibleControlVariable().setValue(
							shouldBeVisible);
		}
	}

	/**
	 * Sets the graphing mode enabled.
	 *
	 * @param graphingModeOn the new graphing mode enabled
	 */
	public void setGraphingModeEnabled(boolean graphingModeOn) {
		for (String device : getExpectedTablesList()) {
			getStudentTableDeviceForName(device).getGraphingModeControl()
					.setValue(graphingModeOn);
		}
	}

	/**
	 * Sets the incorrect expressions visible for all tables.
	 *
	 * @param shouldBeVisible the new incorrect expressions visible for all
	 *            tables
	 */
	public void setIncorrectExpressionsVisibleForAllTables(
			boolean shouldBeVisible) {
		for (String device : getExpectedTablesList()) {
			getStudentTableDeviceForName(device)
					.getIncorrectExpressionsVisibleControlVariable().setValue(
							shouldBeVisible);
		}
	}

	/**
	 * Sets the others correct expressions visible for all tables.
	 *
	 * @param shouldBeVisible the new others correct expressions visible for all
	 *            tables
	 */
	public void setOthersCorrectExpressionsVisibleForAllTables(
			boolean shouldBeVisible) {
		for (String device : getExpectedTablesList()) {
			getStudentTableDeviceForName(device)
					.getOthersCorrectExpressionsVisibleControlVariable()
					.setValue(shouldBeVisible);
		}
	}

	/**
	 * Sets the others incorrect expressions visible for all tables.
	 *
	 * @param shouldBeVisible the new others incorrect expressions visible for
	 *            all tables
	 */
	public void setOthersIncorrectExpressionsVisibleForAllTables(
			boolean shouldBeVisible) {
		for (String device : getExpectedTablesList()) {
			getStudentTableDeviceForName(device)
					.getOthersIncorrectExpressionsVisibleControlVariable()
					.setValue(shouldBeVisible);
		}
	}

	/**
	 * Sets the projector display mode.
	 *
	 * @param projector the projector
	 * @param mode the mode
	 */
	public void setProjectorDisplayMode(String projector,
			ProjectionDisplayMode mode) {
		getProjectorDeviceForName(projector)
				.getProjectionDisplayModeControlVariable().setValue(mode);
	}

	/**
	 * Sets the scores visible for all tables.
	 *
	 * @param shouldBeVisible the new scores visible for all tables
	 */
	public void setScoresVisibleForAllTables(boolean shouldBeVisible) {
		for (String device : getExpectedTablesList()) {
			getStudentTableDeviceForName(device)
					.getScoresVisibleControlVariable()
					.setValue(shouldBeVisible);
		}
	}

	/**
	 * Sets the table input enabled.
	 *
	 * @param enabled the new table input enabled
	 */
	public void setTableInputEnabled(boolean enabled) {
		// for(String device :
		// SynergyNetCluster.get().getClusteredDataManager().getNamesOfDevicesInCluster(NumberNetStudentTableClusteredData.deviceType))
		// {
		for (String device : getExpectedTablesList()) {
			try {
				NumberNetStudentTableClusteredData std = getStudentTableDeviceForName(device);
				std.getTouchEnabledControlVariable().setValue(enabled);
			} catch (Exception ex) {
			}
		}
	}

	/**
	 * Sets the table targets.
	 *
	 * @param targets the new table targets
	 */
	public void setTableTargets(List<TableTarget> targets) {
		log.info("Setting table targets with list " + targets);
		for (TableTarget tt : targets) {
			getStudentTableDeviceForName(tt.getTable())
					.getTargetValueControlVariable().setValue(tt.getTarget());
		}
	}

	/**
	 * Sets the unify rotation mode enabled.
	 *
	 * @param projector the projector
	 * @param enabled the enabled
	 */
	public void setUnifyRotationModeEnabled(String projector, boolean enabled) {
		getProjectorDeviceForName(projector).getUnifyRotationControlVariable()
				.setValue(enabled);
	}

	/**
	 * Update position information from projector.
	 *
	 * @param projector the projector
	 */
	public void updatePositionInformationFromProjector(String projector) {
		getProjectorDeviceForName(projector).notifyShouldUpdatePosition();
	}

	/**
	 * Removes the devices from group.
	 *
	 * @param currentDeviceList the current device list
	 * @param groupToRemove the group to remove
	 * @return the list
	 */
	private List<String> removeDevicesFromGroup(List<String> currentDeviceList,
			String groupToRemove) {
		List<String> online = SynergyNetCluster.get().getPresenceManager()
				.getDeviceNamesOnline(groupToRemove);
		for (String o : online) {
			if (currentDeviceList.contains(o)) {
				currentDeviceList.remove(o);
			}
		}
		return currentDeviceList;
	}

}
