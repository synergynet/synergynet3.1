package synergynet3.web.core;

import java.util.ArrayList;

import synergynet3.behaviours.networkflick.messages.FlickMessage;
import synergynet3.cluster.clustereddevice.ClusteredDevice;
import synergynet3.cluster.sharedmemory.DistributedProperty;
import synergynet3.positioning.SynergyNetPosition;
import synergynet3.projector.network.messages.ContentTransferedMessage;
import synergynet3.web.shared.messages.PerformActionMessage;

/**
 * Manages the network cluster default entries for SynergyNetApp extending
 * application.
 */
public class AppSystemDeviceControl extends ClusteredDevice
{

	/** Network messages relating to sending content to tables. */
	private DistributedProperty<ArrayList<ContentTransferedMessage>> contentTransferToTableControl;

	/**
	 * Network messages relating to the value to be used when converting between
	 * pixels and metres.
	 */
	private DistributedProperty<Float> conversionBetweenPixelAndMetresControl;

	/** Network messages for toggling tables' freeze states. */
	private DistributedProperty<PerformActionMessage> freezeControl;

	/**
	 * Network messages announcing the arrival of an item transferred through a
	 * network flick.
	 */
	private DistributedProperty<FlickMessage> networkFlickControl;

	/** Network messages initiating tables sending screenshots to projectors. */
	private DistributedProperty<PerformActionMessage> networkFlickState;

	/**
	 * Network messages instigating the recreation of items from a removable
	 * drive.
	 */
	private DistributedProperty<PerformActionMessage> reloadRemovableDriveContentsControl;

	/**
	 * Network messages instigating the recreation of items from a device's
	 * directory in the networked shared cache.
	 */
	private DistributedProperty<PerformActionMessage> reloadServerContentsControl;

	/**
	 * Network messages instigating the removal of any items in an environment
	 * not directly created by the application.
	 */
	private DistributedProperty<PerformActionMessage> removeAdditionalMediaControl;

	/** Network messages initiating tables sending contents to projectors. */
	private DistributedProperty<String[]> sendContentsToProjectors;

	/** Network messages initiating tables sending screenshots to projectors. */
	private DistributedProperty<String[]> sendScreenshotsToProjectors;

	/** Network messages relating to students being logged in. */
	private DistributedProperty<String> studentLoginControl;

	/** Network messages relating to students being logged out. */
	private DistributedProperty<String> studentLogoutControl;

	/** Network messages relating to a class being logged out. */
	private DistributedProperty<String> studentLogoutOfClassControl;

	/**
	 * Network messages instigating bringing student icons to the top of an
	 * environment.
	 */
	private DistributedProperty<PerformActionMessage> studentsOnTopControl;

	/**
	 * Network messages announcing the location of another device's interface
	 * position.
	 */
	private DistributedProperty<SynergyNetPosition> tablePositionsControl;

	/** Network messages instigating the creation of a screenshot item. */
	private DistributedProperty<PerformActionMessage> takeScreenshotControl;

	/**
	 * Creates the default shared properties of a network cluster for a device.
	 *
	 * @param deviceName
	 *            ID of the device being initiated on the network cluster.
	 */
	public AppSystemDeviceControl(String deviceName)
	{
		super(deviceName);

		studentLoginControl = getDistributedPropertyMap().createDistributedProperty("studentLoginControl");
		initWithDefault(studentLoginControl, "");
		studentLogoutControl = getDistributedPropertyMap().createDistributedProperty("studentLogoutcontrol");
		initWithDefault(studentLogoutControl, "");
		studentLogoutOfClassControl = getDistributedPropertyMap().createDistributedProperty("studentLogoutOfClassControl");
		initWithDefault(studentLogoutOfClassControl, "");
		reloadServerContentsControl = getDistributedPropertyMap().createDistributedProperty("reloadServerContentsControl");
		initWithDefault(reloadServerContentsControl, new PerformActionMessage());
		reloadRemovableDriveContentsControl = getDistributedPropertyMap().createDistributedProperty("reloadRemovableDriveContentsControl");
		initWithDefault(reloadRemovableDriveContentsControl, new PerformActionMessage());
		studentsOnTopControl = getDistributedPropertyMap().createDistributedProperty("studentsOnTopControl");
		initWithDefault(studentsOnTopControl, new PerformActionMessage());
		takeScreenshotControl = getDistributedPropertyMap().createDistributedProperty("takeScreenshotControl");
		initWithDefault(takeScreenshotControl, new PerformActionMessage());
		removeAdditionalMediaControl = getDistributedPropertyMap().createDistributedProperty("removeAdditionalMediaControl");
		initWithDefault(removeAdditionalMediaControl, new PerformActionMessage());
		tablePositionsControl = getDistributedPropertyMap().createDistributedProperty("tableLocationsControl");
		initWithDefault(tablePositionsControl, new SynergyNetPosition());
		networkFlickControl = getDistributedPropertyMap().createDistributedProperty("networkFlickControl");
		initWithDefault(networkFlickControl, new FlickMessage());
		networkFlickState = getDistributedPropertyMap().createDistributedProperty("networkFlickState");
		initWithDefault(networkFlickState, new PerformActionMessage());
		sendScreenshotsToProjectors = getDistributedPropertyMap().createDistributedProperty("sendScreenshotsToProjectors");
		initWithDefault(sendScreenshotsToProjectors, new String[0]);
		sendContentsToProjectors = getDistributedPropertyMap().createDistributedProperty("sendContentsToProjectors");
		initWithDefault(sendContentsToProjectors, new String[0]);
		contentTransferToTableControl = getDistributedPropertyMap().createDistributedProperty("contentTransferToProjectorControl");
		initWithDefault(contentTransferToTableControl, new ArrayList<ContentTransferedMessage>());
		conversionBetweenPixelAndMetresControl = getDistributedPropertyMap().createDistributedProperty("conversionBetweenPixelAndMetresControl");
		initWithDefault(conversionBetweenPixelAndMetresControl, 0f);
		freezeControl = getDistributedPropertyMap().createDistributedProperty("freezeControl");
		initWithDefault(freezeControl, new PerformActionMessage());
	}

	/**
	 * Get the value of the network cluster property representing messages
	 * relating content arriving on a table.
	 *
	 * @return The value of the network cluster property representing messages
	 *         relating to content arriving on a table.
	 */
	public DistributedProperty<ArrayList<ContentTransferedMessage>> getContentTransferToTableControl()
	{
		return contentTransferToTableControl;
	}

	/**
	 * Get the value of the network cluster property representing the value to
	 * be used when converting between pixels and metres.
	 *
	 * @return The value of the network cluster property representing the value
	 *         to be used when converting between pixels and metres.
	 */
	public DistributedProperty<Float> getConversionBetweenPixelAndMetresControl()
	{
		return conversionBetweenPixelAndMetresControl;
	}

	/**
	 * Get the value of the network cluster property representing the table's
	 * freeze state.
	 *
	 * @return The value of the network cluster property representing the
	 *         table's freeze state.
	 */
	public DistributedProperty<PerformActionMessage> getFreezeControlVariable()
	{
		return freezeControl;
	}

	/**
	 * Get the value of the network cluster property representing messages
	 * announcing the arrival of an item transferred through a network flick.
	 *
	 * @return The value of the network cluster property representing messages
	 *         announcing the arrival of an item transferred through a network
	 *         flick.
	 */
	public DistributedProperty<FlickMessage> getNetworkFlick()
	{
		return networkFlickControl;
	}

	/**
	 * Get the value of the network cluster property representing messages
	 * announcing whether network flicks are enabled for this device.
	 *
	 * @return The value of the network cluster property representing messages
	 *         announcing whether network flicks are enabled for this device.
	 */
	public DistributedProperty<PerformActionMessage> getNetworkFlickState()
	{
		return networkFlickState;
	}

	/**
	 * Get the value of the network cluster property representing messages
	 * instigating the recreation of items from a removable drive.
	 *
	 * @return The value of the network cluster property representing messages
	 *         instigating the recreation of items from a removable drive.
	 */
	public DistributedProperty<PerformActionMessage> getReloadRemovableDriveContentsControl()
	{
		return reloadRemovableDriveContentsControl;
	}

	/**
	 * Get the value of the network cluster property representing messages
	 * instigating the recreation of items from a device's directory in the
	 * networked shared cache.
	 *
	 * @return The value of the network cluster property representing messages
	 *         instigating the recreation of items from a device's directory in
	 *         the networked shared cache.
	 */
	public DistributedProperty<PerformActionMessage> getReloadServerContentsControl()
	{
		return reloadServerContentsControl;
	}

	/**
	 * Get the value of the network cluster property representing messages
	 * instigating the removal of any items in an environment not directly
	 * created by the application.
	 *
	 * @return The value of the network cluster property representing messages
	 *         instigating the removal of any items in an environment not
	 *         directly created by the application.
	 */
	public DistributedProperty<PerformActionMessage> getRemoveAdditionalMediaControl()
	{
		return removeAdditionalMediaControl;
	}

	/**
	 * Get the value of the network cluster property representing messages
	 * initiating tables sending contents to projectors.
	 *
	 * @return The value of the network cluster property representing messages
	 *         initiating tables sending contents to projectors.
	 */
	public DistributedProperty<String[]> getSendContentsToProjectors()
	{
		return sendContentsToProjectors;
	}

	/**
	 * Get the value of the network cluster property representing messages
	 * initiating tables sending screenshots to projectors.
	 *
	 * @return The value of the network cluster property representing messages
	 *         initiating tables sending screenshots to projectors.
	 */
	public DistributedProperty<String[]> getSendScreenshotsToProjectors()
	{
		return sendScreenshotsToProjectors;
	}

	/**
	 * Get the value of the network cluster property representing messages
	 * relating to students being logged in.
	 *
	 * @return The value of the network cluster property representing messages
	 *         relating to students being logged in.
	 */
	public DistributedProperty<String> getStudentLoginControl()
	{
		return studentLoginControl;
	}

	/**
	 * Get the value of the network cluster property representing messages
	 * relating to students being logged out.
	 *
	 * @return The value of the network cluster property representing messages
	 *         relating to students being logged out.
	 */
	public DistributedProperty<String> getStudentLogoutControl()
	{
		return studentLogoutControl;
	}

	/**
	 * Get the value of the network cluster property representing messages
	 * relating to a class being logged out.
	 *
	 * @return The value of the network cluster property representing messages
	 *         relating to a class being logged out.
	 */
	public DistributedProperty<String> getStudentLogoutOfClassControl()
	{
		return studentLogoutOfClassControl;

	}

	/**
	 * Get the value of the network cluster property representing messages
	 * instigating bringing student icons to the top of an environment.
	 *
	 * @return The value of the network cluster property representing messages
	 *         instigating bringing student icons to the top of an environment
	 */
	public DistributedProperty<PerformActionMessage> getStudentsOnTopControl()
	{
		return studentsOnTopControl;
	}

	/**
	 * Get the value of the network cluster property representing messages
	 * announcing the location of another device's interface position.
	 *
	 * @return The value of the network cluster property representing messages
	 *         announcing the location of another device's interface position.
	 */
	public DistributedProperty<SynergyNetPosition> getTablePositions()
	{
		return tablePositionsControl;
	}

	/**
	 * Get the value of the network cluster property representing messages
	 * instigating the creation of a screenshot item.
	 *
	 * @return The value of the network cluster property representing messages
	 *         instigating the creation of a screenshot item.
	 */
	public DistributedProperty<PerformActionMessage> getTakeScreenshotControl()
	{
		return takeScreenshotControl;
	}

	/**
	 * Initialises the corresponding shared properties with the supplied default
	 * value.
	 *
	 * @param control
	 *            Property on the network cluster to be initialised.
	 * @param defaultValue
	 *            The default value to be used when initialising the shared
	 *            property.
	 */
	private void initWithDefault(DistributedProperty<PerformActionMessage> control, PerformActionMessage defaultValue)
	{
		if (null == control.getValue())
		{
			control.setValue(defaultValue);
		}
	}

	/**
	 * Initialises the corresponding shared properties with the supplied default
	 * value.
	 *
	 * @param control
	 *            Property on the network cluster to be initialised.
	 * @param defaultValue
	 *            The default value to be used when initialising the shared
	 *            property.
	 */
	protected void initWithDefault(DistributedProperty<ArrayList<ContentTransferedMessage>> control, ArrayList<ContentTransferedMessage> defaultValue)
	{
		if (null == control.getValue())
		{
			control.setValue(defaultValue);
		}
	}

	/**
	 * Initialises the corresponding shared properties with the supplied default
	 * value.
	 *
	 * @param control
	 *            Property on the network cluster to be initialised.
	 * @param defaultValue
	 *            The default value to be used when initialising the shared
	 *            property.
	 */
	protected void initWithDefault(DistributedProperty<FlickMessage> control, FlickMessage defaultValue)
	{
		if (null == control.getValue())
		{
			control.setValue(defaultValue);
		}
	}

	/**
	 * Initialises the corresponding shared properties with the supplied default
	 * value.
	 *
	 * @param control
	 *            Property on the network cluster to be initialised.
	 * @param defaultValue
	 *            The default value to be used when initialising the shared
	 *            property.
	 */
	protected void initWithDefault(DistributedProperty<Float> control, Float defaultValue)
	{
		if (null == control.getValue())
		{
			control.setValue(defaultValue);
		}
	}

	/**
	 * Initialises the corresponding shared properties with the supplied default
	 * value.
	 *
	 * @param control
	 *            Property on the network cluster to be initialised.
	 * @param defaultValue
	 *            The default value to be used when initialising the shared
	 *            property.
	 */
	protected void initWithDefault(DistributedProperty<String[]> control, String[] defaultValue)
	{
		if (null == control.getValue())
		{
			control.setValue(defaultValue);
		}
	}

	/**
	 * Initialises the corresponding shared properties with the supplied default
	 * value.
	 *
	 * @param control
	 *            Property on the network cluster to be initialised.
	 * @param defaultValue
	 *            The default value to be used when initialising the shared
	 *            property.
	 */
	protected void initWithDefault(DistributedProperty<String> control, String defaultValue)
	{
		if (null == control.getValue())
		{
			control.setValue(defaultValue);
		}
	}

	/**
	 * Initialises the corresponding shared properties with the supplied default
	 * value.
	 *
	 * @param control
	 *            Property on the network cluster to be initialised.
	 * @param defaultValue
	 *            The default value to be used when initialising the shared
	 *            property.
	 */
	protected void initWithDefault(DistributedProperty<SynergyNetPosition> control, SynergyNetPosition defaultValue)
	{
		if (null == control.getValue())
		{
			control.setValue(defaultValue);
		}
	}

}
