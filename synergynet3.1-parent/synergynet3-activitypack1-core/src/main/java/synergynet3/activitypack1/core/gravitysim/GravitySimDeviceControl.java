package synergynet3.activitypack1.core.gravitysim;

import synergynet3.activitypack1.web.shared.UniverseScenario;
import synergynet3.cluster.clustereddevice.ClusteredDevice;
import synergynet3.cluster.sharedmemory.DistributedProperty;

public class GravitySimDeviceControl extends ClusteredDevice {

	private DistributedProperty<Double> gravityControl;
	private DistributedProperty<Double> timeControl;
	private DistributedProperty<UniverseScenario> scenario;
	private DistributedProperty<Integer> clearBodiesTrigger;
	private DistributedProperty<Integer> bodyLimitControl;




	private GravitySimDeviceControl(String deviceName) {
		super(deviceName);
		gravityControl = getDistributedPropertyMap().createDistributedProperty("gravityControl");
		initWithDefault(gravityControl, 1e5);
		timeControl = getDistributedPropertyMap().createDistributedProperty("timeControl");
		initWithDefault(timeControl, 0.001);
		scenario = getDistributedPropertyMap().createDistributedProperty("scenarioName");
		initWithDefault(scenario, UniverseScenario.SUN_AND_MOONS);	
		clearBodiesTrigger = getDistributedPropertyMap().createDistributedProperty("clearBodiesTrigger");
		bodyLimitControl = getDistributedPropertyMap().createDistributedProperty("bodyLimitControl");
		initWithDefault(bodyLimitControl, 4);
	}


	public DistributedProperty<Double> getGravityControl() {
		return gravityControl;
	}

	public DistributedProperty<Double> getTimeControl() {
		return timeControl;
	}

	public DistributedProperty<UniverseScenario> getScenario() {
		return scenario;
	}
	
	public DistributedProperty<Integer> getClearBodiesTrigger() {
		return clearBodiesTrigger;
	}
	
	protected void initWithDefault(DistributedProperty<Boolean> control, boolean defaultValue) {
		if(null == control.getValue()) {
			control.setValue(defaultValue);
		}		
	}
	
	protected void initWithDefault(DistributedProperty<Double> control, double defaultValue) {
		if(null == control.getValue()) {
			control.setValue(defaultValue);
		}		
	}

	protected void initWithDefault(DistributedProperty<String> control, String defaultValue) {
		if(null == control.getValue()) {
			control.setValue(defaultValue);
		}
	}
	
	protected void initWithDefault(DistributedProperty<UniverseScenario> control, UniverseScenario defaultValue) {
		if(null == control.getValue()) {
			control.setValue(defaultValue);
		}
	}
	
	protected void initWithDefault(
			DistributedProperty<Integer> control, int defaultValue) {
		if(null == control.getValue()) {
			control.setValue(defaultValue);
		}
	}


	private static GravitySimDeviceControl instance;
	
	public static GravitySimDeviceControl get() {
		if(instance == null) {
			instance = new GravitySimDeviceControl("gsim");
		}
		return instance;
	}

	public DistributedProperty<Integer> getBodyLimit() {
		return bodyLimitControl;
	}

}
