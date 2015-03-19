package synergynet3.tracking.network;

import java.util.ArrayList;

import synergynet3.cluster.sharedmemory.DistributedPropertyChangedAction;
import synergynet3.tracking.applications.TrackedApp;
import synergynet3.tracking.network.core.TrackingDeviceControl;
import synergynet3.tracking.network.shared.CombinedUserEntity;
import synergynet3.tracking.network.shared.PointDirection;
import synergynet3.web.shared.messages.PerformActionMessage;
import synergynet3.web.shared.messages.PerformActionMessage.MESSAGESTATE;

import com.hazelcast.core.Member;

public class TrackedAppSync {
	private TrackedApp trackingNode;
	private TrackingDeviceControl trackingDeviceControl;
	
	private DistributedPropertyChangedAction<PointDirection> pointingAction = new DistributedPropertyChangedAction<PointDirection>() {			
		@Override
		public void distributedPropertyDidChange(Member m, PointDirection oldValue, PointDirection newValue) {
			trackingNode.userPointing(newValue);
		}
	};
	
	private DistributedPropertyChangedAction<ArrayList<CombinedUserEntity>> updateUserLocationsAction = new DistributedPropertyChangedAction<ArrayList<CombinedUserEntity>>() {			
		@Override
		public void distributedPropertyDidChange(Member m, ArrayList<CombinedUserEntity> oldValue, ArrayList<CombinedUserEntity> newValue) {
			trackingNode.setUserLocations(newValue);
		}
	};
	
	private DistributedPropertyChangedAction<PerformActionMessage> allTablesSelectedModeEnabledAction = new DistributedPropertyChangedAction<PerformActionMessage>() {			
		@Override
		public void distributedPropertyDidChange(Member m, PerformActionMessage oldValue, PerformActionMessage newValue) {
			if (!newValue.messageAlreadyReceived()){
				if (newValue.getMessageState() == MESSAGESTATE.ACTIVATE){
					trackingNode.gestureaAllModeEnabled();
				}
			}
		}
	};
	
	private DistributedPropertyChangedAction<PerformActionMessage> individualTableSelectModeEnabledAction = new DistributedPropertyChangedAction<PerformActionMessage>() {			
		@Override
		public void distributedPropertyDidChange(Member m, PerformActionMessage oldValue, PerformActionMessage newValue) {
			if (!newValue.messageAlreadyReceived()){
				if (newValue.getMessageState() == MESSAGESTATE.ACTIVATE){
					trackingNode.gestureIndividualModeEnabled();
				}
			}
		}
	};
	
	private DistributedPropertyChangedAction<PerformActionMessage> tableSelectdModeDisabledAction = new DistributedPropertyChangedAction<PerformActionMessage>() {			
		@Override
		public void distributedPropertyDidChange(Member m, PerformActionMessage oldValue, PerformActionMessage newValue) {
			if (!newValue.messageAlreadyReceived()){
				if (newValue.getMessageState() == MESSAGESTATE.ACTIVATE){
					trackingNode.gestureModeDisabled();
				}
			}
		}
	};
	
	public TrackedAppSync(TrackingDeviceControl c, TrackedApp trackingNode) {
		this.trackingDeviceControl = c;
		this.trackingNode = trackingNode;
		addSync();
	}
	
	public void stop(){
		trackingDeviceControl.getUserLocationsControlVariable().unregisterChangeListener(updateUserLocationsAction);
		trackingDeviceControl.getAllTablesSelectedModeEnabledControlVariable().unregisterChangeListener(allTablesSelectedModeEnabledAction);
		trackingDeviceControl.getIndividualTableSelectModeEnabledControlVariable().unregisterChangeListener(individualTableSelectModeEnabledAction);
		trackingDeviceControl.getTableSelectedModeDisabledControlVariable().unregisterChangeListener(tableSelectdModeDisabledAction);
		trackingDeviceControl.getPointingControlVariable().unregisterChangeListener(pointingAction);		
	}

	private void addSync() {
		trackingDeviceControl.getUserLocationsControlVariable().registerChangeListener(updateUserLocationsAction);
		trackingDeviceControl.getAllTablesSelectedModeEnabledControlVariable().registerChangeListener(allTablesSelectedModeEnabledAction);
		trackingDeviceControl.getIndividualTableSelectModeEnabledControlVariable().registerChangeListener(individualTableSelectModeEnabledAction);
		trackingDeviceControl.getTableSelectedModeDisabledControlVariable().registerChangeListener(tableSelectdModeDisabledAction);
		trackingDeviceControl.getPointingControlVariable().registerChangeListener(pointingAction);		
	}

}
