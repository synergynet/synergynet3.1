package synergynet3.web.core;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import synergynet3.behaviours.networkflick.messages.FlickMessage;
import synergynet3.cluster.SynergyNetCluster;
import synergynet3.positioning.SynergyNetPosition;
import synergynet3.projector.network.messages.ContentTransferedMessage;
import synergynet3.web.shared.messages.PerformActionMessage;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.monitor.LocalMapOperationStats;

/** Management of retrieving and modifying network cluster properties. */
public class AppSystemControlComms {

	/** An instance of AppSystemControlComms to be accessed statically. */
	private static AppSystemControlComms instance;

	/**
	 * Logger used to provide details on the current execution of SynergyNetApp
	 * extending applications.
	 */
	private static final Logger log = Logger
			.getLogger(AppSystemControlComms.class.getName());

	/**
	 * Initialise an instance of the class.
	 */
	private AppSystemControlComms() {
		log.info(getClass().getName() + " with cluster time "
				+ Hazelcast.getCluster().getClusterTime());
	}

	/**
	 * Retrieve, and initialise if needed, a static instance of
	 * AppSystemControlComms.
	 *
	 * @return An instance of AppSystemControlComms to be accessed statically.
	 */
	public static AppSystemControlComms get() {
		synchronized (AppSystemControlComms.class) {
			if (instance == null) {
				instance = new AppSystemControlComms();
			}
			return instance;
		}
	}

	/**
	 * Send a message announcing the location of another device's interface
	 * position to all devices.
	 *
	 * @param b Structured message identifying the position of a network
	 *            device's interface.
	 */
	public void allTablePositionUpdate(SynergyNetPosition b) {
		for (String device : getTablesList()) {
			try {
				AppSystemDeviceControl std = getSNDeviceFromName(device);
				std.getTablePositions().setValue(b);
			} catch (Exception ex) {
			}
		}
	}

	/**
	 * Send a message instigating bringing student icons to the top of an
	 * environment to all devices.
	 *
	 * @param b Indicator for whether student icons should be brought to the top
	 *            of an environment.
	 */
	public void allTablesBringStudentsToTop(PerformActionMessage message) {
		for (String device : getTablesList()) {
			try {
				AppSystemDeviceControl std = getSNDeviceFromName(device);
				std.getStudentsOnTopControl().setValue(message);
			} catch (Exception ex) {
			}
		}
	}

	/**
	 * Send a message informing all tables to toggle their freeze state
	 *
	 * @param message enables toggle.
	 */
	public void allTablesFreeze(PerformActionMessage message) {
		for (String device : getTablesList()) {
			try {
				AppSystemDeviceControl std = getSNDeviceFromName(device);
				std.getFreezeControlVariable().setValue(message);
			} catch (Exception ex) {
			}
		}
	}

	/**
	 * Send a message informing all table devices of contents to display.
	 *
	 * @param message list of the content item representations being transfered.
	 */
	public void allTablesReceiveContent(
			ArrayList<ContentTransferedMessage> messages) {
		for (String device : getTablesList()) {
			try {
				AppSystemDeviceControl std = getSNDeviceFromName(device);
				std.getContentTransferToTableControl().setValue(messages);
			} catch (Exception ex) {
			}
		}
	}

	/**
	 * Send a message instigating the recreation of items from removable drives
	 * to all devices.
	 *
	 * @param b Indicator for whether the items from removable drives should be
	 *            recreated.
	 */
	public void allTablesReloadRemovableDriveContents(
			PerformActionMessage message) {
		for (String device : getTablesList()) {
			try {
				AppSystemDeviceControl std = getSNDeviceFromName(device);
				std.getReloadRemovableDriveContentsControl().setValue(message);
			} catch (Exception ex) {
			}
		}
	}

	/**
	 * Send a message instigating the recreation of items from a device's
	 * directory in the networked shared cache to all devices.
	 *
	 * @param message Indicator for whether the items from a device's directory
	 *            in the networked shared cache should be recreated.
	 */
	public void allTablesReloadServerContents(PerformActionMessage message) {
		for (String device : getTablesList()) {
			try {
				AppSystemDeviceControl std = getSNDeviceFromName(device);
				std.getReloadServerContentsControl().setValue(message);
			} catch (Exception ex) {
			}
		}
	}

	/**
	 * Send a message instigating the removal of any items in an environment not
	 * directly created by the application to all devices.
	 *
	 * @param b Indicator for whether any items in an environment not directly
	 *            created by the application should be removed.
	 */
	public void allTablesRemoveAdditionalContent(PerformActionMessage message) {
		for (String device : getTablesList()) {
			try {
				AppSystemDeviceControl std = getSNDeviceFromName(device);
				std.getRemoveAdditionalMediaControl().setValue(message);
			} catch (Exception ex) {
			}
		}
	}

	/**
	 * Send a message informing all table devices to send their contents to a
	 * list of projectors.
	 *
	 * @param projectorsToSendTo List of the projectors the contents are to be
	 *            send to.
	 */
	public void allTablesSendContentsToProjectors(String[] projectorsToSendTo) {
		for (String device : getTablesList()) {
			try {
				AppSystemDeviceControl std = getSNDeviceFromName(device);
				std.getSendContentsToProjectors().setValue(projectorsToSendTo);
			} catch (Exception ex) {
			}
		}
	}

	/**
	 * Send a message informing all table devices to send screenshots of their
	 * contents to a list of projectors.
	 *
	 * @param projectorsToSendTo List of the projectors the screenshots are to
	 *            be send to.
	 */
	public void allTablesSendScreenshotsToProjectors(String[] projectorsToSendTo) {
		for (String device : getTablesList()) {
			try {
				AppSystemDeviceControl std = getSNDeviceFromName(device);
				std.getSendScreenshotsToProjectors().setValue(
						projectorsToSendTo);
			} catch (Exception ex) {
			}
		}
	}

	/**
	 * Send a message instigating whether network flick is enabled to all
	 * devices.
	 *
	 * @param b Indicator for whether the devices should.
	 */
	public void allTablesSetNetworkFlick(PerformActionMessage message) {
		for (String device : getTablesList()) {
			try {
				AppSystemDeviceControl std = getSNDeviceFromName(device);
				std.getNetworkFlickState().setValue(message);
			} catch (Exception ex) {
			}
		}
	}

	/**
	 * Send a message instigating the creation of a screenshot item to all
	 * devices.
	 *
	 * @param b Indicator for whether a screenshot item should be created.
	 */
	public void allTablesTakeScreenshot(PerformActionMessage message) {
		for (String device : getTablesList()) {
			try {
				AppSystemDeviceControl std = getSNDeviceFromName(device);
				std.getTakeScreenshotControl().setValue(message);
			} catch (Exception ex) {
			}
		}
	}

	/**
	 * Finds the latency of sending values to another device.
	 *
	 * @param remoteTable The name of the table the latency is to be tested for.
	 * @return A long value representing the latency time.
	 */
	public long getLatency(String remoteTable) {
		long latency = 0;
		try {
			AppSystemDeviceControl std = getSNDeviceFromName(remoteTable);
			LocalMapOperationStats stats = std.getDistributedPropertyMap()
					.getMap().getLocalMapStats().getOperationStats();
			latency = stats.getTotalPutLatency() / stats.getNumberOfPuts();
		} catch (Exception ex) {
		}
		return latency;
	}

	/**
	 * Get the number of all the devices connected to the network cluster of
	 * 'table' type.
	 *
	 * @return A float representing the number of devices connected to the
	 *         network cluster of 'table' type.
	 */
	public int getNumberOfTablesOnline() {
		return SynergyNetCluster.get().getPresenceManager()
				.getDeviceNamesOnline("tables").size();
	}

	/**
	 * Get a collection of all the devices connected to the network cluster of
	 * 'projector' type.
	 *
	 * @return A collection of all the devices connected to the network cluster
	 *         of 'projector' type.
	 */
	public List<String> getProjectorsList() {
		return SynergyNetCluster.get().getPresenceManager()
				.getDeviceNamesOnline("projectors");
	}

	/**
	 * Retrieve a representation of a device connected to the network cluster
	 * from their ID.
	 *
	 * @param device ID representing a device connected to the network cluster.
	 * @return Representation of a device connected to the network cluster.
	 */
	public AppSystemDeviceControl getSNDeviceFromName(String device) {
		try {
			AppSystemDeviceControl std = (AppSystemDeviceControl) SynergyNetCluster
					.get().getDeviceClusterManager()
					.getClusteredDeviceByName(device);
			if (std == null) {
				std = new AppSystemDeviceControl(device);
			}
			return std;
		} catch (ClassCastException e) {
			return new AppSystemDeviceControl(device);
		}
	}

	/**
	 * Get a collection of all the devices connected to the network cluster of
	 * 'table' type.
	 *
	 * @return A collection of all the devices connected to the network cluster
	 *         of 'table' type.
	 */
	public List<String> getTablesList() {
		return SynergyNetCluster.get().getPresenceManager()
				.getDeviceNamesOnline("tables");
	}

	/**
	 * Send a message announcing the arrival of an item transferred through a
	 * network flick.
	 *
	 * @param b Structured message representing the item, its target and
	 *            trajectory when transferred through a network flick.
	 * @param table ID on the network cluster of the device the message is to be
	 *            sent to.
	 */
	public void networkFlick(FlickMessage b, String table) {
		AppSystemDeviceControl std = getSNDeviceFromName(table);
		std.getNetworkFlick().setValue(b);
	}

	/**
	 * Send a message to log out a student from a specific device.
	 *
	 * @param ID ID of the student to be logged out.
	 * @param table ID on the network cluster of the device the message is to be
	 *            sent to.
	 */
	public void removeStudentFromTable(String ID, String table) {
		AppSystemDeviceControl std = getSNDeviceFromName(table);
		std.getStudentLogoutControl().setValue(ID);
	}

	/**
	 * Send a message to log a class of students to all devices.
	 *
	 * @param className Name of the class being logged out.
	 */
	public void removeStudentsOfClass(String className) {
		for (String device : getTablesList()) {
			try {
				AppSystemDeviceControl std = getSNDeviceFromName(device);
				std.getStudentLogoutOfClassControl().setValue(className);
			} catch (Exception ex) {
			}
		}
	}

	/**
	 * Send a message to log a student in to a specific device.
	 *
	 * @param ID ID of the student to be logged in.
	 * @param table ID on the network cluster of the device the message is to be
	 *            sent to.
	 */
	public void sendStudentToTable(String ID, String table) {
		AppSystemDeviceControl std = getSNDeviceFromName(table);
		std.getStudentLoginControl().setValue(ID);
	}

	/**
	 * Send a message instigating bringing student icons to the top of an
	 * environment to a specific device.
	 *
	 * @param b Indicator for whether student icons should be brought to the top
	 *            of an environment.
	 * @param table ID on the network cluster of the device the message is to be
	 *            sent to.
	 */
	public void specificTableBringStudentsToTop(PerformActionMessage message,
			String table) {
		AppSystemDeviceControl std = getSNDeviceFromName(table);
		std.getStudentsOnTopControl().setValue(message);
	}

	/**
	 * Send a message announcing the location of another device's interface
	 * position to a specific device.
	 *
	 * @param b Structured message identifying the position of a network
	 *            device's interface.
	 * @param table ID on the network cluster of the device the message is to be
	 *            sent to.
	 */
	public void specificTablePositionUpdate(SynergyNetPosition b, String table) {
		AppSystemDeviceControl std = getSNDeviceFromName(table);
		std.getTablePositions().setValue(b);
	}

	/**
	 * Send a message informing a specific table device of contents to display.
	 *
	 * @param message list of the content item representations being transfered.
	 * @param device ID on the network cluster of the device the message is to
	 *            be sent to.
	 */
	public void specificTableReceiveContent(
			ArrayList<ContentTransferedMessage> messages, String device) {
		AppSystemDeviceControl std = getSNDeviceFromName(device);
		std.getContentTransferToTableControl().setValue(messages);
	}

	/**
	 * Send a message instigating the recreation of items from removable drives
	 * to a specific device.
	 *
	 * @param b Indicator for whether the items from removable drives should be
	 *            recreated.
	 * @param table ID on the network cluster of the device the message is to be
	 *            sent to.
	 */
	public void specificTableReloadRemovableDriveContents(
			PerformActionMessage message, String table) {
		AppSystemDeviceControl std = getSNDeviceFromName(table);
		std.getReloadRemovableDriveContentsControl().setValue(message);

	}

	/**
	 * Send a message instigating the recreation of items from a device's
	 * directory in the networked shared cache to a specific device.
	 *
	 * @param b Indicator for whether the items from a device's directory in the
	 *            networked shared cache should be recreated.
	 * @param table ID on the network cluster of the device the message is to be
	 *            sent to.
	 */
	public void specificTableReloadServerContents(PerformActionMessage message,
			String table) {
		AppSystemDeviceControl std = getSNDeviceFromName(table);
		std.getReloadServerContentsControl().setValue(message);
	}

	/**
	 * Send a message instigating the removal of any items in an environment not
	 * directly created by the application to a specific device.
	 *
	 * @param b Indicator for whether any items in an environment not directly
	 *            created by the application should be removed.
	 * @param table ID on the network cluster of the device the message is to be
	 *            sent to.
	 */
	public void specificTableRemoveAdditionalContent(
			PerformActionMessage message, String table) {
		AppSystemDeviceControl std = getSNDeviceFromName(table);
		std.getRemoveAdditionalMediaControl().setValue(message);
	}

	/**
	 * Send a message instigating whether network flick is enabled to a specific
	 * devices.
	 *
	 * @param b Indicator for whether network flick is enabled.
	 * @param table ID on the network cluster of the device the message is to be
	 *            sent to.
	 */
	public void specificTableSetNetworkFlick(PerformActionMessage message,
			String table) {
		AppSystemDeviceControl std = getSNDeviceFromName(table);
		std.getNetworkFlickState().setValue(message);

	}

	/**
	 * Send a message informing all tables to toggle their freeze state
	 *
	 * @param message enables toggle.
	 * @param device ID on the network cluster of the device the message is to
	 *            be sent to.
	 */
	public void specificTablesFreeze(PerformActionMessage message, String table) {
		try {
			AppSystemDeviceControl std = getSNDeviceFromName(table);
			std.getFreezeControlVariable().setValue(message);
		} catch (Exception ex) {
		}
	}

	/**
	 * Send a message informing a specific devices to send its contents to a
	 * list of projectors.
	 *
	 * @param projectorsToSendTo List of the projectors the contents are to be
	 *            send to.
	 * @param table ID on the network cluster of the device the message is to be
	 *            sent to.
	 */
	public void specificTablesSendContentsToProjectors(
			String[] projectorsToSendTo, String device) {
		AppSystemDeviceControl std = getSNDeviceFromName(device);
		std.getSendContentsToProjectors().setValue(projectorsToSendTo);
	}

	/**
	 * Send a message informing a specific devices to send screenshots of its
	 * contents to a list of projectors.
	 *
	 * @param projectorsToSendTo List of the projectors the screenshots are to
	 *            be send to.
	 * @param table ID on the network cluster of the device the message is to be
	 *            sent to.
	 */
	public void specificTablesSendScreenshotsToProjectors(
			String[] projectorsToSendTo, String device) {
		AppSystemDeviceControl std = getSNDeviceFromName(device);
		std.getSendScreenshotsToProjectors().setValue(projectorsToSendTo);
	}

	/**
	 * Send a message instigating the creation of a screenshot item to a
	 * specific device.
	 *
	 * @param b Indicator for whether a screenshot item should be created.
	 * @param table ID on the network cluster of the device the message is to be
	 *            sent to.
	 */
	public void specificTableTakeScreenshot(PerformActionMessage message,
			String table) {
		AppSystemDeviceControl std = getSNDeviceFromName(table);
		std.getTakeScreenshotControl().setValue(message);
	}

}