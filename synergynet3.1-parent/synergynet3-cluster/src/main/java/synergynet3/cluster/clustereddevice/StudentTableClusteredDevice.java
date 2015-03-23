package synergynet3.cluster.clustereddevice;

import synergynet3.cluster.sharedmemory.DistributedProperty;

/**
 * The Class StudentTableClusteredDevice.
 */
public class StudentTableClusteredDevice extends ClusteredDevice {

	/** The touch enabled control. */
	private DistributedProperty<Boolean> touchEnabledControl;

	/**
	 * Instantiates a new student table clustered device.
	 *
	 * @param deviceName the device name
	 */
	public StudentTableClusteredDevice(String deviceName) {
		super(deviceName);
		touchEnabledControl = getDistributedPropertyMap()
				.createDistributedProperty("touchenabled");
	}

	/**
	 * Gets the touch enabled control variable.
	 *
	 * @return the touch enabled control variable
	 */
	public DistributedProperty<Boolean> getTouchEnabledControlVariable() {
		return touchEnabledControl;
	}

}
