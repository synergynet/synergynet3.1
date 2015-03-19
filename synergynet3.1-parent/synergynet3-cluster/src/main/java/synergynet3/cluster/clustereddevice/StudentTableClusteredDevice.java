package synergynet3.cluster.clustereddevice;

import synergynet3.cluster.sharedmemory.DistributedProperty;

public class StudentTableClusteredDevice extends ClusteredDevice {
	
	private DistributedProperty<Boolean> touchEnabledControl;

	public StudentTableClusteredDevice(String deviceName) {
		super(deviceName);
		touchEnabledControl = getDistributedPropertyMap().createDistributedProperty("touchenabled");
	}

	public DistributedProperty<Boolean> getTouchEnabledControlVariable() {
		return touchEnabledControl;
	}

}
