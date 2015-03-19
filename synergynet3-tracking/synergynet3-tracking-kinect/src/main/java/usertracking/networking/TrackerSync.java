package usertracking.networking;

import synergynet3.cluster.sharedmemory.DistributedPropertyChangedAction;
import synergynet3.tracking.network.core.TrackingDeviceControl;
import usertracking.TrackerUtils;
import usertracking.UserTracker;

import com.hazelcast.core.Member;

public class TrackerSync {
	private TrackingDeviceControl c;
	
	private DistributedPropertyChangedAction<String> tableSelectedAction = new DistributedPropertyChangedAction<String>() {			
		@Override
		public void distributedPropertyDidChange(Member m, String oldValue, String newValue) {
			if (UserTracker.SELECTED_TABLES.contains(UserTracker.ALL_TABLES))TrackerNetworking.switchToIndividualTableSelectMode();
			if (!UserTracker.SELECTED_TABLES.contains(newValue))UserTracker.SELECTED_TABLES.add(newValue);
			TrackerNetworking.cancelPosesAndClearGestureSequences();
		}
	};	
	
	private DistributedPropertyChangedAction<String> tableDeselectedAction = new DistributedPropertyChangedAction<String>() {			
		@Override
		public void distributedPropertyDidChange(Member m, String oldValue, String newValue) {
			if (UserTracker.SELECTED_TABLES.contains(newValue))UserTracker.SELECTED_TABLES.remove(newValue);
			TrackerNetworking.cancelPosesAndClearGestureSequences();
		}
	};	

	//TODO:  Not receiving
	private DistributedPropertyChangedAction<Integer> gestureControlAction = new DistributedPropertyChangedAction<Integer>() {				
		@Override
		public void distributedPropertyDidChange(Member m, Integer oldValue, Integer newValue) {		
			TrackerNetworking.gestureControlReceive(newValue);  
		}
	};	
	
	private DistributedPropertyChangedAction<String> teacherStatusChangedAction = new DistributedPropertyChangedAction<String>() {			
		@Override
		public void distributedPropertyDidChange(Member m, String oldValue, String newValue) {
			int userID = TrackerUtils.getUserIdFromMessage(newValue);
			if (userID > -1){
				boolean isTeacher = TrackerUtils.getTeacherStatusFromTeacherStatusMessage(newValue);
				TrackerNetworking.teacherStatuses[userID] = isTeacher;
				TeacherControlPanel.getInstance().fillTable();			
			}
		}
	};	
	
	private DistributedPropertyChangedAction<String> colourChangedAction = new DistributedPropertyChangedAction<String>() {			
		@Override
		public void distributedPropertyDidChange(Member m, String oldValue, String newValue) {
			int userID = TrackerUtils.getUserIdFromMessage(newValue);
			if (userID > -1){
				int uniqueID = TrackerUtils.getUniqueIDFromMessage(newValue);
				TrackerNetworking.uniqueIDs[userID] = uniqueID;
				TeacherControlPanel.getInstance().fillTable();	
			}
		}
	};	
	
	public TrackerSync(TrackingDeviceControl c) {
		this.c = c;
		addSync();
	}
	
	public void stop(){
		c.getTableSelectedControlVariable().unregisterChangeListener(tableSelectedAction);
		c.getTableDeselectedControlVariable().unregisterChangeListener(tableDeselectedAction);
		c.getGestureControlVariable().unregisterChangeListener(gestureControlAction);
		c.getTeacherStatusToTrackerControlVariable().unregisterChangeListener(teacherStatusChangedAction);
		c.getUniqueIDToTrackerControlVariable().unregisterChangeListener(colourChangedAction);
	}

	private void addSync() {
		c.getTableSelectedControlVariable().registerChangeListener(tableSelectedAction);
		c.getTableDeselectedControlVariable().registerChangeListener(tableDeselectedAction);
		c.getGestureControlVariable().registerChangeListener(gestureControlAction);
		c.getTeacherStatusToTrackerControlVariable().registerChangeListener(teacherStatusChangedAction);
		c.getUniqueIDToTrackerControlVariable().registerChangeListener(colourChangedAction);
	}

}