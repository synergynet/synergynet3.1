package synergynet3.web.earlyyears.core;

import synergynet3.cluster.clustereddevice.ClusteredDevice;
import synergynet3.cluster.sharedmemory.DistributedProperty;
import synergynet3.web.earlyyears.shared.EarlyYearsActivity;
import synergynet3.web.shared.messages.PerformActionMessage;

public class EarlyYearsDeviceControl extends ClusteredDevice {

	private DistributedProperty<EarlyYearsActivity> activity;
	private DistributedProperty<Integer> railWayCornerNumControl;
	private DistributedProperty<Integer> railWayCrossNumControl;
	private DistributedProperty<Integer> railWayStraightNumControl;
	private DistributedProperty<PerformActionMessage> exploreShowTeacherConsoleControl;
	private DistributedProperty<PerformActionMessage> roadModeControl;

	public EarlyYearsDeviceControl(String deviceName) {
		super(deviceName);
		activity = getDistributedPropertyMap().createDistributedProperty("scenarioName");
		initWithDefault(activity, EarlyYearsActivity.ENVIRONMENT_EXPLORER);	
		railWayCornerNumControl = getDistributedPropertyMap().createDistributedProperty("railWayCornerNumControl");
		initWithDefault(railWayCornerNumControl, 6);
		railWayCrossNumControl = getDistributedPropertyMap().createDistributedProperty("railWayCrossNumControl");
		initWithDefault(railWayCrossNumControl, 1);		
		railWayStraightNumControl = getDistributedPropertyMap().createDistributedProperty("railWayStraightNumControl");
		initWithDefault(railWayStraightNumControl, 4);
		exploreShowTeacherConsoleControl = getDistributedPropertyMap().createDistributedProperty("exploreShowTeacherConsoleControl");
		initWithDefault(exploreShowTeacherConsoleControl, new PerformActionMessage());
		roadModeControl = getDistributedPropertyMap().createDistributedProperty("roadMode");
		initWithDefault(roadModeControl, new PerformActionMessage());
	}

	protected void initWithDefault(DistributedProperty<PerformActionMessage> control, PerformActionMessage defaultValue) {
		if(null == control.getValue()) {
			control.setValue(defaultValue);
		}		
	}
	
	protected void initWithDefault(DistributedProperty<Double> control, double defaultValue) {
		if(null == control.getValue()) {
			control.setValue(defaultValue);
		}		
	}
	
	protected void initWithDefault(DistributedProperty<EarlyYearsActivity> control, EarlyYearsActivity defaultValue) {
		if(null == control.getValue()) {
			control.setValue(defaultValue);
		}
	}
	
	protected void initWithDefault(DistributedProperty<Integer> control, int defaultValue) {
		if(null == control.getValue()) {
			control.setValue(defaultValue);
		}
	}	
	
	public DistributedProperty<EarlyYearsActivity> getActivity() {
		return activity;
	}	

	public DistributedProperty<Integer> getRailWayCornerNumControl() {
		return railWayCornerNumControl;
	}

	public DistributedProperty<Integer> getRailWayCrossNumControl() {
		return railWayCrossNumControl;
	}

	public DistributedProperty<Integer> getRailWayStraightNumControl() {
		return railWayStraightNumControl;
	}

	public DistributedProperty<PerformActionMessage> getExploreShowTeacherConsoleControl() {
		return exploreShowTeacherConsoleControl;
	}
	
	public DistributedProperty<PerformActionMessage> getRoadModeControl() {
		return roadModeControl;
	}
	
}
