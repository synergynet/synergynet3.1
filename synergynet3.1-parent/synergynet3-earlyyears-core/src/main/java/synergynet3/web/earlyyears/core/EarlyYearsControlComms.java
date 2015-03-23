package synergynet3.web.earlyyears.core;

import java.util.List;
import java.util.logging.Logger;

import synergynet3.cluster.SynergyNetCluster;
import synergynet3.web.earlyyears.shared.EarlyYearsActivity;
import synergynet3.web.shared.messages.PerformActionMessage;

import com.hazelcast.core.Hazelcast;

/**
 * The Class EarlyYearsControlComms.
 */
public class EarlyYearsControlComms {

	/** The instance. */
	private static EarlyYearsControlComms instance;

	/** The Constant log. */
	private static final Logger log = Logger
			.getLogger(EarlyYearsControlComms.class.getName());

	/**
	 * Instantiates a new early years control comms.
	 */
	private EarlyYearsControlComms() {
		log.info(getClass().getName() + " with cluster time "
				+ Hazelcast.getCluster().getClusterTime());
	}

	/**
	 * Gets the.
	 *
	 * @return the early years control comms
	 */
	public static EarlyYearsControlComms get() {
		synchronized (EarlyYearsControlComms.class) {
			if (instance == null) {
				instance = new EarlyYearsControlComms();
			}
			return instance;
		}
	}

	/**
	 * Gets the early years device for name.
	 *
	 * @param device the device
	 * @return the early years device for name
	 */
	public EarlyYearsDeviceControl getEarlyYearsDeviceForName(String device) {
		try {
			EarlyYearsDeviceControl std = (EarlyYearsDeviceControl) SynergyNetCluster
					.get().getDeviceClusterManager()
					.getClusteredDeviceByName(device);
			if (std == null) {
				std = new EarlyYearsDeviceControl(device);
			}
			return std;
		} catch (ClassCastException e) {
			return new EarlyYearsDeviceControl(device);
		}
	}

	/**
	 * Gets the tables list.
	 *
	 * @return the tables list
	 */
	public List<String> getTablesList() {
		return SynergyNetCluster.get().getPresenceManager()
				.getDeviceNamesOnline("tables");
	}

	/**
	 * Sets the all road mode.
	 *
	 * @param roadMode the new all road mode
	 */
	public void setAllRoadMode(PerformActionMessage roadMode) {
		for (String device : getTablesList()) {
			try {
				EarlyYearsDeviceControl std = getEarlyYearsDeviceForName(device);
				std.getRoadModeControl().setValue(roadMode);
			} catch (Exception ex) {
			}
		}
	}

	/**
	 * Sets the all tables explorer show teacher control.
	 *
	 * @param show the new all tables explorer show teacher control
	 */
	public void setAllTablesExplorerShowTeacherControl(PerformActionMessage show) {
		for (String device : getTablesList()) {
			try {
				EarlyYearsDeviceControl std = getEarlyYearsDeviceForName(device);
				std.getExploreShowTeacherConsoleControl().setValue(show);
			} catch (Exception ex) {
			}
		}
	}

	/**
	 * Sets the all tables railway corners.
	 *
	 * @param newNum the new all tables railway corners
	 */
	public void setAllTablesRailwayCorners(int newNum) {
		for (String device : getTablesList()) {
			try {
				EarlyYearsDeviceControl std = getEarlyYearsDeviceForName(device);
				std.getRailWayCornerNumControl().setValue(newNum);
			} catch (Exception ex) {
			}
		}
	}

	/**
	 * Sets the all tables railway crosses.
	 *
	 * @param newNum the new all tables railway crosses
	 */
	public void setAllTablesRailwayCrosses(int newNum) {
		for (String device : getTablesList()) {
			try {
				EarlyYearsDeviceControl std = getEarlyYearsDeviceForName(device);
				std.getRailWayCrossNumControl().setValue(newNum);
			} catch (Exception ex) {
			}
		}
	}

	/**
	 * Sets the all tables railway straights.
	 *
	 * @param newNum the new all tables railway straights
	 */
	public void setAllTablesRailwayStraights(int newNum) {
		for (String device : getTablesList()) {
			try {
				EarlyYearsDeviceControl std = getEarlyYearsDeviceForName(device);
				std.getRailWayStraightNumControl().setValue(newNum);
			} catch (Exception ex) {
			}
		}
	}

	/**
	 * Sets the all tables scenario.
	 *
	 * @param scenario the new all tables scenario
	 */
	public void setAllTablesScenario(EarlyYearsActivity scenario) {
		for (String device : getTablesList()) {
			try {
				EarlyYearsDeviceControl std = getEarlyYearsDeviceForName(device);
				std.getActivity().setValue(scenario);
			} catch (Exception ex) {
			}
		}
	}

	/**
	 * Sets the specific road mode.
	 *
	 * @param roadMode the road mode
	 * @param table the table
	 */
	public void setSpecificRoadMode(PerformActionMessage roadMode, String table) {
		try {
			EarlyYearsDeviceControl std = getEarlyYearsDeviceForName(table);
			std.getRoadModeControl().setValue(roadMode);
		} catch (Exception ex) {
		}
	}

	/**
	 * Sets the specific tables explorer show teacher control.
	 *
	 * @param show the show
	 * @param table the table
	 */
	public void setSpecificTablesExplorerShowTeacherControl(
			PerformActionMessage show, String table) {
		try {
			EarlyYearsDeviceControl std = getEarlyYearsDeviceForName(table);
			std.getExploreShowTeacherConsoleControl().setValue(show);
		} catch (Exception ex) {
		}
	}

	/**
	 * Sets the specific tables railway corners.
	 *
	 * @param newNum the new num
	 * @param table the table
	 */
	public void setSpecificTablesRailwayCorners(int newNum, String table) {
		EarlyYearsDeviceControl std = getEarlyYearsDeviceForName(table);
		std.getRailWayCornerNumControl().setValue(newNum);
	}

	/**
	 * Sets the specific tables railway crosses.
	 *
	 * @param newNum the new num
	 * @param table the table
	 */
	public void setSpecificTablesRailwayCrosses(int newNum, String table) {
		EarlyYearsDeviceControl std = getEarlyYearsDeviceForName(table);
		std.getRailWayCrossNumControl().setValue(newNum);
	}

	/**
	 * Sets the specific tables railway straights.
	 *
	 * @param newNum the new num
	 * @param table the table
	 */
	public void setSpecificTablesRailwayStraights(int newNum, String table) {
		EarlyYearsDeviceControl std = getEarlyYearsDeviceForName(table);
		std.getRailWayStraightNumControl().setValue(newNum);
	}

	/**
	 * Sets the specific tables scenario.
	 *
	 * @param scenario the scenario
	 * @param table the table
	 */
	public void setSpecificTablesScenario(EarlyYearsActivity scenario,
			String table) {
		EarlyYearsDeviceControl std = getEarlyYearsDeviceForName(table);
		std.getActivity().setValue(scenario);
	}

}
