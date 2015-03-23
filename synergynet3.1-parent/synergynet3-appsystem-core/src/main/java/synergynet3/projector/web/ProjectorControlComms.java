package synergynet3.projector.web;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import synergynet3.cluster.SynergyNetCluster;
import synergynet3.projector.network.messages.ContentTransferedMessage;
import synergynet3.web.shared.messages.PerformActionMessage;

import com.hazelcast.core.Hazelcast;

/** Management of retrieving and modifying network cluster properties. */
public class ProjectorControlComms {

	/** An instance of AppSystemControlComms to be accessed statically. */
	private static ProjectorControlComms instance;

	/**
	 * Logger used to provide details on the current execution of SynergyNetApp
	 * extending applications.
	 */
	private static final Logger log = Logger
			.getLogger(ProjectorControlComms.class.getName());

	/**
	 * Initialise an instance of the class.
	 */
	private ProjectorControlComms() {
		log.info(getClass().getName() + " with cluster time "
				+ Hazelcast.getCluster().getClusterTime());
	}

	/**
	 * Retrieve, and initialise if needed, a static instance of
	 * AppSystemControlComms.
	 *
	 * @return An instance of AppSystemControlComms to be accessed statically.
	 */
	public static ProjectorControlComms get() {
		synchronized (ProjectorControlComms.class) {
			if (instance == null) {
				instance = new ProjectorControlComms();
			}
			return instance;
		}
	}

	/**
	 * Send a message instigating the alignment of content on all projectors.
	 *
	 * @param message Indicator for whether content on all projectors should be
	 *            aligned.
	 */
	public void allProjectorsAlign(PerformActionMessage message) {
		for (String device : getProjectorsList()) {
			try {
				ProjectorDeviceControl std = getSNDeviceFromName(device);
				std.getProjectorAlignControl().setValue(message);
			} catch (Exception ex) {
			}
		}
	}

	/**
	 * Send a message instigating the clearing of content on all projectors.
	 *
	 * @param message Indicator for whether content on all projectors should be
	 *            cleared.
	 */
	public void allProjectorsClear(PerformActionMessage message) {
		for (String device : getProjectorsList()) {
			try {
				ProjectorDeviceControl std = getSNDeviceFromName(device);
				std.getProjectorClearControl().setValue(message);
			} catch (Exception ex) {
			}
		}
	}

	/**
	 * Send a message informing all projector devices of contents to display.
	 *
	 * @param message list of the content item representations being transfered.
	 */
	public void allProjectorsReceiveContent(
			ArrayList<ContentTransferedMessage> messages) {
		for (String device : getProjectorsList()) {
			try {
				ProjectorDeviceControl std = getSNDeviceFromName(device);
				std.getContentTransferToProjectorControl().setValue(messages);
			} catch (Exception ex) {
			}
		}
	}

	/**
	 * Send a message informing all projector devices to send their contents to
	 * a list of tables.
	 *
	 * @param tablesToSendTo List of the tables the contents are to be send to.
	 */
	public void allProjectorsSendContentsToTables(String[] tablesToSendTo) {
		for (String device : getProjectorsList()) {
			try {
				ProjectorDeviceControl std = getSNDeviceFromName(device);
				std.getSendContentsToTablesControl().setValue(tablesToSendTo);
			} catch (Exception ex) {
			}
		}
	}

	/**
	 * Get the number of all the devices connected to the network cluster of
	 * 'projector' type.
	 *
	 * @return A float representing the number of devices connected to the
	 *         network cluster of 'projector' type.
	 */
	public int getNumberOfProjectorsOnline() {
		return SynergyNetCluster.get().getPresenceManager()
				.getDeviceNamesOnline("projectors").size();
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
	public ProjectorDeviceControl getSNDeviceFromName(String device) {
		try {
			ProjectorDeviceControl std = (ProjectorDeviceControl) SynergyNetCluster
					.get().getDeviceClusterManager()
					.getClusteredDeviceByName(device);
			if (std == null) {
				std = new ProjectorDeviceControl(device);
			}
			return std;
		} catch (ClassCastException e) {
			return new ProjectorDeviceControl(device);
		}
	}

	/**
	 * Send a message instigating the alignment of content on specific
	 * projector.
	 *
	 * @param message Indicator for whether content on specific projector should
	 *            be aligned.
	 * @param device ID on the network cluster of the device the message is to
	 *            be sent to.
	 */
	public void specificProjectorAlign(PerformActionMessage message,
			String device) {
		ProjectorDeviceControl std = getSNDeviceFromName(device);
		std.getProjectorAlignControl().setValue(message);
	}

	/**
	 * Send a message instigating the clearing of content on specific projector.
	 *
	 * @param message Indicator for whether content on specific projector should
	 *            be cleared.
	 * @param device ID on the network cluster of the device the message is to
	 *            be sent to.
	 */
	public void specificProjectorClear(PerformActionMessage message,
			String device) {
		ProjectorDeviceControl std = getSNDeviceFromName(device);
		std.getProjectorClearControl().setValue(message);
	}

	/**
	 * Send a message informing a specific projector devices of contents to
	 * display.
	 *
	 * @param message list of the content item representations being transfered.
	 * @param device ID on the network cluster of the device the message is to
	 *            be sent to.
	 */
	public void specificProjectorsReceiveContent(
			ArrayList<ContentTransferedMessage> messages, String device) {
		ProjectorDeviceControl std = getSNDeviceFromName(device);
		std.getContentTransferToProjectorControl().setValue(messages);
	}

	/**
	 * Send a message informing a specific devices to send its contents to a
	 * list of tables.
	 *
	 * @param tablesToSendTo List of the tables the contents are to be send to.
	 * @param device ID on the network cluster of the device the message is to
	 *            be sent to.
	 */
	public void specificProjectorsSendContentsToTables(String[] tablesToSendTo,
			String device) {
		ProjectorDeviceControl std = getSNDeviceFromName(device);
		std.getSendContentsToTablesControl().setValue(tablesToSendTo);
	}

}