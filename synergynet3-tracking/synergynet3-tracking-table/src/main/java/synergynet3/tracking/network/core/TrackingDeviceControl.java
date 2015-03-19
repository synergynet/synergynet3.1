package synergynet3.tracking.network.core;

import java.util.ArrayList;

import synergynet3.cluster.clustereddevice.StudentTableClusteredDevice;
import synergynet3.cluster.sharedmemory.DistributedProperty;
import synergynet3.tracking.network.shared.CombinedUserEntity;
import synergynet3.tracking.network.shared.PointDirection;
import synergynet3.tracking.network.shared.UserLocations;
import synergynet3.web.shared.messages.PerformActionMessage;

public class TrackingDeviceControl extends StudentTableClusteredDevice {
	
	private DistributedProperty<ArrayList<CombinedUserEntity>> userLocationsControl;
	private DistributedProperty<UserLocations> trackedUserLocationsControl;
	private DistributedProperty<String> tableSelectedControl;
	private DistributedProperty<String> tableDeselectedControl;
	private DistributedProperty<PerformActionMessage> allTablesSelectedModeEnabledControlVariable;
	private DistributedProperty<PerformActionMessage> individualTableSelectModeEnabledControlVariable;
	private DistributedProperty<PerformActionMessage> tableSelectdModeDisabledControlVariable;
	private DistributedProperty<Integer> gestureModeControlVariable;
	private DistributedProperty<String> teacherStatusControlVariable;
	private DistributedProperty<String> teacherStatusToTrackerControlVariable;
	private DistributedProperty<String> uniqueIDToTrackerControlVariable;
	private DistributedProperty<PointDirection> pointingControlVariable;
	
	public TrackingDeviceControl(String deviceName) {
		super(deviceName);
		trackedUserLocationsControl = getDistributedPropertyMap().createDistributedProperty("trackedIserLocationsControlVariable");
		initWithDefault(trackedUserLocationsControl, new UserLocations(""));
		tableSelectedControl = getDistributedPropertyMap().createDistributedProperty("tableSelectedControl");
		initWithDefault(tableSelectedControl, "");
		tableDeselectedControl = getDistributedPropertyMap().createDistributedProperty("tableDeselectedControl");
		initWithDefault(tableDeselectedControl, "");
		allTablesSelectedModeEnabledControlVariable = getDistributedPropertyMap().createDistributedProperty("allTablesSelectedModeEnabledControlVariable");
		initWithDefault(allTablesSelectedModeEnabledControlVariable, new PerformActionMessage());
		individualTableSelectModeEnabledControlVariable = getDistributedPropertyMap().createDistributedProperty("individualTableSelectModeEnabledControlVariable");
		initWithDefault(individualTableSelectModeEnabledControlVariable, new PerformActionMessage());
		tableSelectdModeDisabledControlVariable = getDistributedPropertyMap().createDistributedProperty("tableSelectdModeDisabledControlVariable");
		initWithDefault(tableSelectdModeDisabledControlVariable, new PerformActionMessage());
		gestureModeControlVariable = getDistributedPropertyMap().createDistributedProperty("gestureModeControlVariable");
		initWithDefault(gestureModeControlVariable, -1);
		userLocationsControl = getDistributedPropertyMap().createDistributedProperty("userLocationsControlVariable");
		initWithDefault(userLocationsControl, new ArrayList<CombinedUserEntity>());
		teacherStatusControlVariable = getDistributedPropertyMap().createDistributedProperty("teacherStatusControlVariable");
		initWithDefault(teacherStatusControlVariable, "");
		teacherStatusToTrackerControlVariable = getDistributedPropertyMap().createDistributedProperty("teacherStatusToTrackerControlVariable");
		initWithDefault(teacherStatusToTrackerControlVariable, "");
		uniqueIDToTrackerControlVariable = getDistributedPropertyMap().createDistributedProperty("uniqueIDToTrackerControlVariable");
		initWithDefault(uniqueIDToTrackerControlVariable, "");
		pointingControlVariable = getDistributedPropertyMap().createDistributedProperty("pointingControlVariable");
		initWithDefault(pointingControlVariable, new PointDirection());
	}

	public DistributedProperty<UserLocations> getTrackedUserLocationsControlVariable() {
		return trackedUserLocationsControl;
	}
	
	public DistributedProperty<String> getTableSelectedControlVariable() {
		return tableSelectedControl;
	}

	public DistributedProperty<String> getTableDeselectedControlVariable() {
		return tableDeselectedControl;
	}
	
	public DistributedProperty<PerformActionMessage> getAllTablesSelectedModeEnabledControlVariable() {
		return allTablesSelectedModeEnabledControlVariable;
	}
	
	public DistributedProperty<PerformActionMessage> getIndividualTableSelectModeEnabledControlVariable() {
		return individualTableSelectModeEnabledControlVariable;
	}
	
	public DistributedProperty<PerformActionMessage> getTableSelectedModeDisabledControlVariable() {
		return tableSelectdModeDisabledControlVariable;
	}
	
	public DistributedProperty<Integer> getGestureControlVariable() {
		return gestureModeControlVariable;
	}
	
	public DistributedProperty<ArrayList<CombinedUserEntity>> getUserLocationsControlVariable() {
		return userLocationsControl;
	}
	
	public DistributedProperty<String> getTeacherStatusControlVariable() {
		return teacherStatusControlVariable;
	}
	
	public DistributedProperty<String> getTeacherStatusToTrackerControlVariable() {
		return teacherStatusToTrackerControlVariable;
	}
	
	public DistributedProperty<String> getUniqueIDToTrackerControlVariable() {
		return uniqueIDToTrackerControlVariable;
	}
	
	public DistributedProperty<PointDirection> getPointingControlVariable() {
		return pointingControlVariable;
	}
	
	protected void initWithDefault(DistributedProperty<UserLocations> control, UserLocations defaultValue) {
		if(null == control.getValue()) {
			control.setValue(defaultValue);
		}		
	}
	
	protected void initWithDefault(DistributedProperty<PerformActionMessage> control, PerformActionMessage defaultValue) {
		if(null == control.getValue()) {
			control.setValue(defaultValue);
		}		
	}
	
	protected void initWithDefault(DistributedProperty<String> control, String defaultValue) {
		if(null == control.getValue()) {
			control.setValue(defaultValue);
		}		
	}
	
	protected void initWithDefault(DistributedProperty<Integer> control, Integer defaultValue) {
		if(null == control.getValue()) {
			control.setValue(defaultValue);
		}		
	}
	
	protected void initWithDefault(DistributedProperty<ArrayList<CombinedUserEntity>> control, ArrayList<CombinedUserEntity> defaultValue) {
		if(null == control.getValue()) {
			control.setValue(defaultValue);
		}		
	}
	
	protected void initWithDefaultStringArray(DistributedProperty<ArrayList<String>> control, ArrayList<String> defaultValue) {
		if(null == control.getValue()) {
			control.setValue(defaultValue);
		}		
	}
	
	protected void initWithDefault(DistributedProperty<PointDirection> control, PointDirection defaultValue) {
		if(null == control.getValue()) {
			control.setValue(defaultValue);
		}		
	}
	
}
