package synergynet3.web.apps.numbernet.comms.table;

import synergynet3.cluster.clustereddevice.ClusteredDevice;
import synergynet3.cluster.sharedmemory.DistributedProperty;
import synergynet3.web.apps.numbernet.shared.ProjectionDisplayMode;

/**
 * The Class ProjectorDevice.
 */
public class ProjectorDevice extends ClusteredDevice
{

	/** The Constant deviceType. */
	public static final String deviceType = "projector";

	/** The display mode. */
	private DistributedProperty<ProjectionDisplayMode> displayMode;

	/** The target to show. */
	private DistributedProperty<Double> targetToShow;

	/** The unify rotation control variable. */
	private DistributedProperty<Boolean> unifyRotationControlVariable;

	/** The update position kicker. */
	private DistributedProperty<Integer> updatePositionKicker;

	/**
	 * Instantiates a new projector device.
	 *
	 * @param name
	 *            the name
	 */
	public ProjectorDevice(String name)
	{
		super(name);
		targetToShow = getDistributedPropertyMap().createDistributedProperty("targetvalue");
		unifyRotationControlVariable = getDistributedPropertyMap().createDistributedProperty("unifyrotation");
		updatePositionKicker = getDistributedPropertyMap().createDistributedProperty("shouldupdatepos");
		displayMode = getDistributedPropertyMap().createDistributedProperty("displaymode");
	}

	/**
	 * Gets the projection display mode control variable.
	 *
	 * @return the projection display mode control variable
	 */
	public DistributedProperty<ProjectionDisplayMode> getProjectionDisplayModeControlVariable()
	{
		return displayMode;
	}

	/**
	 * Gets the should update position control variable.
	 *
	 * @return the should update position control variable
	 */
	public DistributedProperty<Integer> getShouldUpdatePositionControlVariable()
	{
		return updatePositionKicker;
	}

	/**
	 * Gets the target to show control variable.
	 *
	 * @return the target to show control variable
	 */
	public DistributedProperty<Double> getTargetToShowControlVariable()
	{
		return targetToShow;
	}

	/**
	 * Gets the unify rotation control variable.
	 *
	 * @return the unify rotation control variable
	 */
	public DistributedProperty<Boolean> getUnifyRotationControlVariable()
	{
		return unifyRotationControlVariable;
	}

	/**
	 * Notify should update position.
	 */
	public void notifyShouldUpdatePosition()
	{
		Integer i = updatePositionKicker.getValue();
		if (i == null)
		{
			updatePositionKicker.setValue(1);
		}
		else
		{
			i += 1;
			updatePositionKicker.setValue(i);
		}
	}
}
