package synergynet3.activitypack1.core.gravitysim;

import synergynet3.activitypack1.web.shared.UniverseScenario;
import synergynet3.cluster.clustereddevice.ClusteredDevice;
import synergynet3.cluster.sharedmemory.DistributedProperty;

/**
 * The Class GravitySimDeviceControl.
 */
public class GravitySimDeviceControl extends ClusteredDevice {

	/** The instance. */
	private static GravitySimDeviceControl instance;

	/** The body limit control. */
	private DistributedProperty<Integer> bodyLimitControl;

	/** The clear bodies trigger. */
	private DistributedProperty<Integer> clearBodiesTrigger;

	/** The gravity control. */
	private DistributedProperty<Double> gravityControl;

	/** The scenario. */
	private DistributedProperty<UniverseScenario> scenario;

	/** The time control. */
	private DistributedProperty<Double> timeControl;

	/**
	 * Instantiates a new gravity sim device control.
	 *
	 * @param deviceName the device name
	 */
	private GravitySimDeviceControl(String deviceName) {
		super(deviceName);
		gravityControl = getDistributedPropertyMap().createDistributedProperty(
				"gravityControl");
		initWithDefault(gravityControl, 1e5);
		timeControl = getDistributedPropertyMap().createDistributedProperty(
				"timeControl");
		initWithDefault(timeControl, 0.001);
		scenario = getDistributedPropertyMap().createDistributedProperty(
				"scenarioName");
		initWithDefault(scenario, UniverseScenario.SUN_AND_MOONS);
		clearBodiesTrigger = getDistributedPropertyMap()
				.createDistributedProperty("clearBodiesTrigger");
		bodyLimitControl = getDistributedPropertyMap()
				.createDistributedProperty("bodyLimitControl");
		initWithDefault(bodyLimitControl, 4);
	}

	/**
	 * Gets the.
	 *
	 * @return the gravity sim device control
	 */
	public static GravitySimDeviceControl get() {
		if (instance == null) {
			instance = new GravitySimDeviceControl("gsim");
		}
		return instance;
	}

	/**
	 * Gets the body limit.
	 *
	 * @return the body limit
	 */
	public DistributedProperty<Integer> getBodyLimit() {
		return bodyLimitControl;
	}

	/**
	 * Gets the clear bodies trigger.
	 *
	 * @return the clear bodies trigger
	 */
	public DistributedProperty<Integer> getClearBodiesTrigger() {
		return clearBodiesTrigger;
	}

	/**
	 * Gets the gravity control.
	 *
	 * @return the gravity control
	 */
	public DistributedProperty<Double> getGravityControl() {
		return gravityControl;
	}

	/**
	 * Gets the scenario.
	 *
	 * @return the scenario
	 */
	public DistributedProperty<UniverseScenario> getScenario() {
		return scenario;
	}

	/**
	 * Gets the time control.
	 *
	 * @return the time control
	 */
	public DistributedProperty<Double> getTimeControl() {
		return timeControl;
	}

	/**
	 * Inits the with default.
	 *
	 * @param control the control
	 * @param defaultValue the default value
	 */
	protected void initWithDefault(DistributedProperty<Boolean> control,
			boolean defaultValue) {
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
	protected void initWithDefault(DistributedProperty<String> control,
			String defaultValue) {
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
			DistributedProperty<UniverseScenario> control,
			UniverseScenario defaultValue) {
		if (null == control.getValue()) {
			control.setValue(defaultValue);
		}
	}

}
