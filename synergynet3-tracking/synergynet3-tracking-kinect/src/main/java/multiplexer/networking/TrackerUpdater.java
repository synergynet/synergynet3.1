package multiplexer.networking;

import synergynet3.tracking.network.core.TrackingControlComms;
import synergynet3.tracking.network.shared.CombinedUserEntity;
import usertracking.TrackerUtils;

/**
 * The Class TrackerUpdater.
 */
public class TrackerUpdater
{

	/**
	 * Send teacher status update to trackers.
	 *
	 * @param user
	 *            the user
	 */
	public static void sendTeacherStatusUpdateToTrackers(CombinedUserEntity user)
	{
		for (String userID : user.getUserIDs())
		{
			int id = CombinedUserEntity.getUserIDFromID(userID);
			String target = CombinedUserEntity.getTrackerSourceFromID(userID);
			String message = TrackerUtils.createTeacherStatusMessage(id, "", user.isTeacher());
			TrackingControlComms.get().sendTeacherStatusToSpecifcTracker(message, target);
		}
	}

	/**
	 * Send unique id update to trackers.
	 *
	 * @param user
	 *            the user
	 */
	public static void sendUniqueIDUpdateToTrackers(CombinedUserEntity user)
	{
		for (String userID : user.getUserIDs())
		{
			int id = CombinedUserEntity.getUserIDFromID(userID);
			String target = CombinedUserEntity.getTrackerSourceFromID(userID);
			String message = TrackerUtils.createUniqueIDToTrackersMessage(id, user.getUniqueID());
			TrackingControlComms.get().sendColourToSpecifcTracker(message, target);
		}
	}

}
