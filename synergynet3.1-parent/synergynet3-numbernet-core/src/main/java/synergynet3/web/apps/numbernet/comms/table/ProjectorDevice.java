package synergynet3.web.apps.numbernet.comms.table;

import synergynet3.cluster.clustereddevice.ClusteredDevice;
import synergynet3.cluster.sharedmemory.DistributedProperty;
import synergynet3.web.apps.numbernet.shared.ProjectionDisplayMode;

public class ProjectorDevice extends ClusteredDevice {
	public static final String deviceType = "projector";
	
	private DistributedProperty<Boolean> unifyRotationControlVariable;
	private DistributedProperty<Double> targetToShow;
	private DistributedProperty<Integer> updatePositionKicker;
	private DistributedProperty<ProjectionDisplayMode> displayMode;

	public ProjectorDevice(String name) {
		super(name);
		targetToShow 					= getDistributedPropertyMap().createDistributedProperty("targetvalue");
		unifyRotationControlVariable 	= getDistributedPropertyMap().createDistributedProperty("unifyrotation");
		updatePositionKicker 			= getDistributedPropertyMap().createDistributedProperty("shouldupdatepos");
		displayMode			 			= getDistributedPropertyMap().createDistributedProperty("displaymode");
	}
	
	public DistributedProperty<Double> getTargetToShowControlVariable() {
		return targetToShow;
	}
		
	public DistributedProperty<Boolean> getUnifyRotationControlVariable() {
		return unifyRotationControlVariable;
	}
	
	public DistributedProperty<ProjectionDisplayMode> getProjectionDisplayModeControlVariable() {
		return displayMode;
	}
	
	public void notifyShouldUpdatePosition() {
		Integer i = updatePositionKicker.getValue();
		if(i == null) {
			updatePositionKicker.setValue(1);
		}else{
			i += 1;
			updatePositionKicker.setValue(i);
		}
	}
	
	public DistributedProperty<Integer> getShouldUpdatePositionControlVariable() {
		return updatePositionKicker;
	}
}
