package multiplexer.networking;

import synergynet3.tracking.network.core.TrackingControlComms;
import synergynet3.tracking.network.shared.CombinedUserEntity;
import usertracking.TrackerUtils;

public class TrackerUpdater {
	
	public static void sendTeacherStatusUpdateToTrackers(CombinedUserEntity user){
		for (String userID : user.getUserIDs()){
			int id = CombinedUserEntity.getUserIDFromID(userID);
			String target = CombinedUserEntity.getTrackerSourceFromID(userID);			
			String message = TrackerUtils.createTeacherStatusMessage(id, "", user.isTeacher());
			TrackingControlComms.get().sendTeacherStatusToSpecifcTracker(message, target);
		}
	}
	
	public static void sendUniqueIDUpdateToTrackers(CombinedUserEntity user){
		for (String userID : user.getUserIDs()){
			int id = CombinedUserEntity.getUserIDFromID(userID);
			String target = CombinedUserEntity.getTrackerSourceFromID(userID);			
			String message = TrackerUtils.createUniqueIDToTrackersMessage(id, user.getUniqueID());			
			TrackingControlComms.get().sendColourToSpecifcTracker(message, target);
		}
	}

}