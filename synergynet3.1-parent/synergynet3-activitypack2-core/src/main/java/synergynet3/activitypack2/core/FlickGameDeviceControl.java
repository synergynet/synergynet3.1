package synergynet3.activitypack2.core;

import synergynet3.activitypack2.web.shared.flickgame.FlickGameScore;
import synergynet3.cluster.clustereddevice.ClusteredDevice;
import synergynet3.cluster.sharedmemory.DistributedProperty;

/**
 * The Class FlickGameDeviceControl.
 */
public class FlickGameDeviceControl extends ClusteredDevice {

	/** The score. */
	private DistributedProperty<FlickGameScore> score;

	/**
	 * Instantiates a new flick game device control.
	 *
	 * @param deviceName the device name
	 */
	public FlickGameDeviceControl(String deviceName) {
		super(deviceName);
		score = getDistributedPropertyMap().createDistributedProperty(
				"flickGameScore");
		initWithDefault(score, new FlickGameScore(0, 0));
	}

	/**
	 * Gets the score.
	 *
	 * @return the score
	 */
	public DistributedProperty<FlickGameScore> getScore() {
		return score;
	}

	/**
	 * Inits the with default.
	 *
	 * @param control the control
	 * @param defaultValue the default value
	 */
	protected void initWithDefault(DistributedProperty<FlickGameScore> control,
			FlickGameScore defaultValue) {
		if (null == control.getValue()) {
			control.setValue(defaultValue);
		}
	}

}