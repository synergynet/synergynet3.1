package synergynet3.web.earlyyears.core;

import synergynet3.cluster.clustereddevice.ClusteredDevice;
import synergynet3.cluster.sharedmemory.DistributedProperty;
import synergynet3.web.earlyyears.shared.EarlyYearsActivity;
import synergynet3.web.shared.messages.PerformActionMessage;

/**
 * The Class EarlyYearsDeviceControl.
 */
public class EarlyYearsDeviceControl extends ClusteredDevice {

	/** The activity. */
	private DistributedProperty<EarlyYearsActivity> activity;

	/** The explore show teacher console control. */
	private DistributedProperty<PerformActionMessage> exploreShowTeacherConsoleControl;

	/** The rail way corner num control. */
	private DistributedProperty<Integer> railWayCornerNumControl;

	/** The rail way cross num control. */
	private DistributedProperty<Integer> railWayCrossNumControl;

	/** The rail way straight num control. */
	private DistributedProperty<Integer> railWayStraightNumControl;

	/** The road mode control. */
	private DistributedProperty<PerformActionMessage> roadModeControl;

	/**
	 * Instantiates a new early years device control.
	 *
	 * @param deviceName the device name
	 */
	public EarlyYearsDeviceControl(String deviceName) {
		super(deviceName);
		activity = getDistributedPropertyMap().createDistributedProperty(
				"scenarioName");
		initWithDefault(activity, EarlyYearsActivity.ENVIRONMENT_EXPLORER);
		railWayCornerNumControl = getDistributedPropertyMap()
				.createDistributedProperty("railWayCornerNumControl");
		initWithDefault(railWayCornerNumControl, 6);
		railWayCrossNumControl = getDistributedPropertyMap()
				.createDistributedProperty("railWayCrossNumControl");
		initWithDefault(railWayCrossNumControl, 1);
		railWayStraightNumControl = getDistributedPropertyMap()
				.createDistributedProperty("railWayStraightNumControl");
		initWithDefault(railWayStraightNumControl, 4);
		exploreShowTeacherConsoleControl = getDistributedPropertyMap()
				.createDistributedProperty("exploreShowTeacherConsoleControl");
		initWithDefault(exploreShowTeacherConsoleControl,
				new PerformActionMessage());
		roadModeControl = getDistributedPropertyMap()
				.createDistributedProperty("roadMode");
		initWithDefault(roadModeControl, new PerformActionMessage());
	}

	/**
	 * Gets the activity.
	 *
	 * @return the activity
	 */
	public DistributedProperty<EarlyYearsActivity> getActivity() {
		return activity;
	}

	/**
	 * Gets the explore show teacher console control.
	 *
	 * @return the explore show teacher console control
	 */
	public DistributedProperty<PerformActionMessage> getExploreShowTeacherConsoleControl() {
		return exploreShowTeacherConsoleControl;
	}

	/**
	 * Gets the rail way corner num control.
	 *
	 * @return the rail way corner num control
	 */
	public DistributedProperty<Integer> getRailWayCornerNumControl() {
		return railWayCornerNumControl;
	}

	/**
	 * Gets the rail way cross num control.
	 *
	 * @return the rail way cross num control
	 */
	public DistributedProperty<Integer> getRailWayCrossNumControl() {
		return railWayCrossNumControl;
	}

	/**
	 * Gets the rail way straight num control.
	 *
	 * @return the rail way straight num control
	 */
	public DistributedProperty<Integer> getRailWayStraightNumControl() {
		return railWayStraightNumControl;
	}

	/**
	 * Gets the road mode control.
	 *
	 * @return the road mode control
	 */
	public DistributedProperty<PerformActionMessage> getRoadModeControl() {
		return roadModeControl;
	}

	/**
	 * Inits the with default.
	 *
	 * @param control the control
	 * @param defaultValue the default value
	 */
	protected void initWithDefault(DistributedProperty<Double> control,
			double defaultValue) {
		if (null == control.getValue()) {
			control.setValue(defaultValue);
		}
	}

	/**
	 * Inits the with default.
	 *
	 * @param control the control
	 * @param defaultValue the default value
	 */
	protected void initWithDefault(
			DistributedProperty<EarlyYearsActivity> control,
			EarlyYearsActivity defaultValue) {
		if (null == control.getValue()) {
			control.setValue(defaultValue);
		}
	}

	/**
	 * Inits the with default.
	 *
	 * @param control the control
	 * @param defaultValue the default value
	 */
	protected void initWithDefault(DistributedProperty<Integer> control,
			int defaultValue) {
		if (null == control.getValue()) {
			control.setValue(defaultValue);
		}
	}

	/**
	 * Inits the with default.
	 *
	 * @param control the control
	 * @param defaultValue the default value
	 */
	protected void initWithDefault(
			DistributedProperty<PerformActionMessage> control,
			PerformActionMessage defaultValue) {
		if (null == control.getValue()) {
			control.setValue(defaultValue);
		}
	}

}
