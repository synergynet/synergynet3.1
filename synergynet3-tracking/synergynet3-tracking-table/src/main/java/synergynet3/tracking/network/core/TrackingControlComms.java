package synergynet3.tracking.network.core;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import synergynet3.cluster.SynergyNetCluster;
import synergynet3.tracking.network.shared.CombinedUserEntity;
import synergynet3.tracking.network.shared.PointDirection;
import synergynet3.tracking.network.shared.UserLocations;
import synergynet3.web.shared.messages.PerformActionMessage;

import com.hazelcast.core.Hazelcast;

/**
 * The Class TrackingControlComms.
 */
public class TrackingControlComms
{

	/** The instance. */
	private static TrackingControlComms instance;

	/** The Constant log. */
	private static final Logger log = Logger.getLogger(TrackingControlComms.class.getName());

	/**
	 * Instantiates a new tracking control comms.
	 */
	private TrackingControlComms()
	{
		log.info(getClass().getName() + " with cluster time " + Hazelcast.getCluster().getClusterTime());
	}

	/**
	 * Gets the.
	 *
	 * @return the tracking control comms
	 */
	public static TrackingControlComms get()
	{
		synchronized (TrackingControlComms.class)
		{
			if (instance == null)
			{
				instance = new TrackingControlComms();
			}
			return instance;
		}
	}

	/**
	 * Announce de selection.
	 *
	 * @param trackingTableIdentity
	 *            the tracking table identity
	 */
	public void announceDeSelection(String trackingTableIdentity)
	{
		for (String device : getTrackersList())
		{
			try
			{
				TrackingDeviceControl std = getDeviceForName(device);
				std.getTableDeselectedControlVariable().setValue(trackingTableIdentity);
			}
			catch (Exception ex)
			{
			}
		}
	}

	/**
	 * Announce pointing gesture.
	 *
	 * @param pointDirection
	 *            the point direction
	 */
	public void announcePointingGesture(PointDirection pointDirection)
	{
		for (String device : getTablesList())
		{
			try
			{
				TrackingDeviceControl std = getDeviceForName(device);
				std.getPointingControlVariable().setValue(pointDirection);
			}
			catch (Exception ex)
			{
			}
		}
	}

	/**
	 * Announce selection.
	 *
	 * @param trackingTableIdentity
	 *            the tracking table identity
	 */
	public void announceSelection(String trackingTableIdentity)
	{
		for (String device : getTrackersList())
		{
			try
			{
				TrackingDeviceControl std = getDeviceForName(device);
				std.getTableSelectedControlVariable().setValue(trackingTableIdentity);
			}
			catch (Exception ex)
			{
			}
		}
	}

	/**
	 * Broadcast user locations.
	 *
	 * @param userLocations
	 *            the user locations
	 */
	public void broadcastUserLocations(UserLocations userLocations)
	{
		for (String device : getMultiplexersList())
		{
			try
			{
				TrackingDeviceControl std = getDeviceForName(device);
				std.getTrackedUserLocationsControlVariable().setValue(userLocations);
			}
			catch (Exception ex)
			{
			}
		}
	}

	/**
	 * Gets the device for name.
	 *
	 * @param device
	 *            the device
	 * @return the device for name
	 */
	public TrackingDeviceControl getDeviceForName(String device)
	{
		try
		{
			TrackingDeviceControl std = (TrackingDeviceControl) SynergyNetCluster.get().getDeviceClusterManager().getClusteredDeviceByName(device);
			if (std == null)
			{
				std = new TrackingDeviceControl(device);
			}
			return std;
		}
		catch (ClassCastException e)
		{
			return new TrackingDeviceControl(device);
		}
	}

	// -------------- Tracker Functions --------------

	/**
	 * Gets the multiplexers list.
	 *
	 * @return the multiplexers list
	 */
	public List<String> getMultiplexersList()
	{
		return SynergyNetCluster.get().getPresenceManager().getDeviceNamesOnline("multiplexers");
	}

	/**
	 * Get the number of all the devices connected to the network cluster of
	 * 'trackers' type.
	 *
	 * @return A float representing the number of devices connected to the
	 *         network cluster of 'trackers' type.
	 */
	public int getNumberOfTrackersOnline()
	{
		return SynergyNetCluster.get().getPresenceManager().getDeviceNamesOnline("trackers").size();
	}

	/**
	 * Gets the tables list.
	 *
	 * @return the tables list
	 */
	public List<String> getTablesList()
	{
		return SynergyNetCluster.get().getPresenceManager().getDeviceNamesOnline("tables");
	}

	/**
	 * Gets the trackers list.
	 *
	 * @return the trackers list
	 */
	public List<String> getTrackersList()
	{
		return SynergyNetCluster.get().getPresenceManager().getDeviceNamesOnline("trackers");
	}

	/**
	 * Send colour to specifc tracker.
	 *
	 * @param message
	 *            the message
	 * @param device
	 *            the device
	 */
	public void sendColourToSpecifcTracker(String message, String device)
	{
		try
		{
			TrackingDeviceControl std = getDeviceForName(device);
			std.getUniqueIDToTrackerControlVariable().setValue(message);
		}
		catch (Exception ex)
		{
		}
	}

	/**
	 * Send teacher status to specifc tracker.
	 *
	 * @param message
	 *            the message
	 * @param device
	 *            the device
	 */
	public void sendTeacherStatusToSpecifcTracker(String message, String device)
	{
		try
		{
			TrackingDeviceControl std = getDeviceForName(device);
			std.getTeacherStatusToTrackerControlVariable().setValue(message);
		}
		catch (Exception ex)
		{
		}
	}

	// -------------- Tracked App functions --------------

	/**
	 * Send user locations to all tables.
	 *
	 * @param users
	 *            the users
	 */
	public void sendUserLocationsToAllTables(ArrayList<CombinedUserEntity> users)
	{
		for (String device : getTablesList())
		{
			try
			{
				TrackingDeviceControl std = getDeviceForName(device);
				std.getUserLocationsControlVariable().setValue(users);
			}
			catch (Exception ex)
			{
			}
		}

	}

	/**
	 * Sets the all tables selected mode enabled.
	 *
	 * @param message
	 *            the new all tables selected mode enabled
	 */
	public void setAllTablesSelectedModeEnabled(PerformActionMessage message)
	{
		for (String device : getTablesList())
		{
			try
			{
				TrackingDeviceControl std = getDeviceForName(device);
				std.getAllTablesSelectedModeEnabledControlVariable().setValue(message);
			}
			catch (Exception ex)
			{
			}
		}
	}

	/**
	 * Sets the gesture control broadcast.
	 *
	 * @param userID
	 *            the user id
	 * @param localDevice
	 *            the local device
	 */
	public void setGestureControlBroadcast(int userID, String localDevice)
	{
		List<String> devices = getTrackersList();
		devices.add(localDevice);
		for (String device : devices)
		{
			try
			{
				TrackingDeviceControl std = getDeviceForName(device);
				std.getGestureControlVariable().setValue(userID);
			}
			catch (Exception ex)
			{
			}
		}
	}

	// -------------- Multiplexer functions --------------

	/**
	 * Sets the individual table select mode enabled.
	 *
	 * @param message
	 *            the new individual table select mode enabled
	 */
	public void setIndividualTableSelectModeEnabled(PerformActionMessage message)
	{
		for (String device : getTablesList())
		{
			try
			{
				TrackingDeviceControl std = getDeviceForName(device);
				std.getIndividualTableSelectModeEnabledControlVariable().setValue(message);
			}
			catch (Exception ex)
			{
			}
		}
	}

	/**
	 * Sets the table selectd mode disabled.
	 *
	 * @param message
	 *            the new table selectd mode disabled
	 */
	public void setTableSelectdModeDisabled(PerformActionMessage message)
	{
		for (String device : getTablesList())
		{
			try
			{
				TrackingDeviceControl std = getDeviceForName(device);
				std.getTableSelectedModeDisabledControlVariable().setValue(message);
			}
			catch (Exception ex)
			{
			}
		}
	}

	/**
	 * Update teacher status.
	 *
	 * @param message
	 *            the message
	 */
	public void updateTeacherStatus(String message)
	{
		for (String device : getMultiplexersList())
		{
			try
			{
				TrackingDeviceControl std = getDeviceForName(device);
				std.getTeacherStatusControlVariable().setValue(message);
			}
			catch (Exception ex)
			{
			}
		}
	}

}
