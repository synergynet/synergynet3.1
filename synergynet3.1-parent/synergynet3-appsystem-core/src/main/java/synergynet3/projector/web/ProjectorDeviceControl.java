package synergynet3.projector.web;

import java.util.ArrayList;

import synergynet3.cluster.clustereddevice.ClusteredDevice;
import synergynet3.cluster.sharedmemory.DistributedProperty;
import synergynet3.projector.network.messages.ContentTransferedMessage;
import synergynet3.web.shared.messages.PerformActionMessage;

/**
 * Manages the network cluster default entries for SynergyNetApp extending
 * application.
 */
public class ProjectorDeviceControl extends ClusteredDevice
{

	/** Network messages relating to sending content to projectors. */
	private DistributedProperty<ArrayList<ContentTransferedMessage>> contentTransferToProjectorControl;

	/** Network messages relating to aligning contents on a projector. */
	private DistributedProperty<PerformActionMessage> projectorAlignControl;

	/** Network messages relating to clearing a projector. */
	private DistributedProperty<PerformActionMessage> projectorClearControl;

	/** Network messages relating to sending projected contents to tables. */
	private DistributedProperty<String[]> sendProjectedContentsToTablesControl;

	/**
	 * Creates the default shared properties of a network cluster for a device.
	 *
	 * @param deviceName
	 *            ID of the device being initiated on the network cluster.
	 */
	public ProjectorDeviceControl(String deviceName)
	{
		super(deviceName);

		projectorClearControl = getDistributedPropertyMap().createDistributedProperty("projectorClearControl");
		initWithDefault(projectorClearControl, new PerformActionMessage());

		projectorAlignControl = getDistributedPropertyMap().createDistributedProperty("projectorAlignControl");
		initWithDefault(projectorAlignControl, new PerformActionMessage());

		sendProjectedContentsToTablesControl = getDistributedPropertyMap().createDistributedProperty("sendProjectedContentsToTablesControl");
		initWithDefault(sendProjectedContentsToTablesControl, new String[0]);

		contentTransferToProjectorControl = getDistributedPropertyMap().createDistributedProperty("contentTransferToProjectorControl");
		initWithDefault(contentTransferToProjectorControl, new ArrayList<ContentTransferedMessage>());

	}

	/**
	 * Get the value of the network cluster property representing messages
	 * relating content arriving on a projector.
	 *
	 * @return The value of the network cluster property representing messages
	 *         relating to content arriving on a projector.
	 */
	public DistributedProperty<ArrayList<ContentTransferedMessage>> getContentTransferToProjectorControl()
	{
		return contentTransferToProjectorControl;
	}

	/**
	 * Get the value of the network cluster property representing messages
	 * relating to aligning contents on a projector.
	 *
	 * @return The value of the network cluster property representing messages
	 *         relating to aligning contents a projector.
	 */
	public DistributedProperty<PerformActionMessage> getProjectorAlignControl()
	{
		return projectorAlignControl;
	}

	/**
	 * Get the value of the network cluster property representing messages
	 * relating to clearing a projector.
	 *
	 * @return The value of the network cluster property representing messages
	 *         relating to clearing a projector.
	 */
	public DistributedProperty<PerformActionMessage> getProjectorClearControl()
	{
		return projectorClearControl;
	}

	/**
	 * Get the value of the network cluster property representing messages
	 * relating to sending projected contents to tables.
	 *
	 * @return The value of the network cluster property representing messages
	 *         relating to aligning contents a projector.
	 */
	public DistributedProperty<String[]> getSendContentsToTablesControl()
	{
		return sendProjectedContentsToTablesControl;
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
	protected void initWithDefault(DistributedProperty<PerformActionMessage> control, PerformActionMessage defaultValue)
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

}
