package synergynet3.activitypack2.core;

import synergynet3.activitypack2.web.shared.flickgame.FlickGameScore;
import synergynet3.cluster.clustereddevice.ClusteredDevice;
import synergynet3.cluster.sharedmemory.DistributedProperty;

public class FlickGameDeviceControl extends ClusteredDevice {

	private DistributedProperty<FlickGameScore> score;

	public FlickGameDeviceControl(String deviceName) {
		super(deviceName);
		score = getDistributedPropertyMap().createDistributedProperty("flickGameScore");
		initWithDefault(score, new FlickGameScore(0, 0));	
	}

	protected void initWithDefault(DistributedProperty<FlickGameScore> control, FlickGameScore defaultValue) {
		if(null == control.getValue()) {
			control.setValue(defaultValue);
		}		
	}
	
	public DistributedProperty<FlickGameScore> getScore() {
		return score;
	}	
	
}