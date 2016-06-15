package synergynet3.activitypack2.core;

import java.util.List;
import java.util.logging.Logger;

import synergynet3.activitypack2.web.shared.flickgame.FlickGameScore;
import synergynet3.cluster.SynergyNetCluster;

import com.hazelcast.core.Hazelcast;

/**
 * The Class FlickGameControlComms.
 */
public class FlickGameControlComms
{

	/** The instance. */
	private static FlickGameControlComms instance;

	/** The Constant log. */
	private static final Logger log = Logger.getLogger(FlickGameControlComms.class.getName());

	/**
	 * Instantiates a new flick game control comms.
	 */
	private FlickGameControlComms()
	{
		log.info(getClass().getName() + " with cluster time " + Hazelcast.getCluster().getClusterTime());
	}

	/**
	 * Gets the.
	 *
	 * @return the flick game control comms
	 */
	public static FlickGameControlComms get()
	{
		synchronized (FlickGameControlComms.class)
		{
			if (instance == null)
			{
				instance = new FlickGameControlComms();
			}
			return instance;
		}
	}

	/**
	 * Gets the flick game device from name.
	 *
	 * @param device
	 *            the device
	 * @return the flick game device from name
	 */
	public FlickGameDeviceControl getFlickGameDeviceFromName(String device)
	{
		try
		{
			FlickGameDeviceControl std = (FlickGameDeviceControl) SynergyNetCluster.get().getDeviceClusterManager().getClusteredDeviceByName(device);
			if (std == null)
			{
				std = new FlickGameDeviceControl(device);
			}
			return std;
		}
		catch (ClassCastException e)
		{
			return new FlickGameDeviceControl(device);
		}
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
	 * Sets the all tables score.
	 *
	 * @param score
	 *            the new all tables score
	 */
	public void setAllTablesScore(FlickGameScore score)
	{
		for (String device : getTablesList())
		{
			try
			{
				FlickGameDeviceControl std = getFlickGameDeviceFromName(device);
				std.getScore().setValue(score);
			}
			catch (Exception ex)
			{
			}
		}
	}

}
