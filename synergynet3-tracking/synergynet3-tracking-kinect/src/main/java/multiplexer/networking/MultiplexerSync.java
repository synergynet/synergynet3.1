package multiplexer.networking;

import java.util.ArrayList;
import java.util.HashMap;

import multiplexer.Multiplexer;
import multiplexer.MultiplexerUtils;
import synergynet3.cluster.sharedmemory.DistributedPropertyChangedAction;
import synergynet3.tracking.network.core.TrackingDeviceControl;
import synergynet3.tracking.network.shared.CombinedUserEntity;
import synergynet3.tracking.network.shared.UserLocation;
import synergynet3.tracking.network.shared.UserLocations;
import usertracking.TrackerUtils;

import com.hazelcast.core.Member;

/**
 * The Class MultiplexerSync.
 */
public class MultiplexerSync
{

	/** The c. */
	private TrackingDeviceControl c;

	/** The existing ids. */
	private HashMap<String, ArrayList<Integer>> existingIds = new HashMap<String, ArrayList<Integer>>();

	/** The teacher status changed. */
	private DistributedPropertyChangedAction<String> teacherStatusChanged = new DistributedPropertyChangedAction<String>()
	{
		@Override
		public void distributedPropertyDidChange(Member member, String oldValue, String newValue)
		{
			String userID = CombinedUserEntity.generateUserIDString(TrackerUtils.getUserIdFromMessage(newValue), TrackerUtils.getTableIdFromTeacherStatusMessage(newValue));

			boolean isTeacher = TrackerUtils.getTeacherStatusFromTeacherStatusMessage(newValue);
			synchronized (Multiplexer.users)
			{
				for (CombinedUserEntity user : Multiplexer.users)
				{
					if (user.getUserIDs().contains(userID))
					{
						if (user.isTeacher() != isTeacher)
						{
							if (isTeacher)
							{
								Multiplexer.writeToAnnouncementBox("Entity " + user.getUniqueID() + " has been set to teacher");
							}
							else
							{
								Multiplexer.writeToAnnouncementBox("Entity " + user.getUniqueID() + " has been set to student");
							}
							user.setTeacher(isTeacher);
							TrackerUpdater.sendTeacherStatusUpdateToTrackers(user);
						}
						break;
					}
				}
			}
		}
	};

	/** The user locations. */
	private DistributedPropertyChangedAction<UserLocations> userLocations = new DistributedPropertyChangedAction<UserLocations>()
	{
		@Override
		public void distributedPropertyDidChange(Member m, UserLocations oldValue, UserLocations newValue)
		{

			if (!Multiplexer.isRunning)
			{
				return;
			}

			synchronized (Multiplexer.users)
			{

				String source = newValue.getSource();
				ArrayList<Integer> newIDs = new ArrayList<Integer>();

				for (UserLocation userLoc : newValue.getUserLocations())
				{
					newIDs.add(userLoc.getID());
					String userID = CombinedUserEntity.generateUserIDString(userLoc.getID(), source);
					boolean newID = true;
					for (CombinedUserEntity user : Multiplexer.users)
					{
						if (user.getUserIDs().contains(userID))
						{
							user.setUserLocation(MultiplexerUtils.mergeUserLocations(userLoc, user.getUserLocation()));
							newID = false;
							break;
						}
					}
					if (newID)
					{
						CombinedUserEntity user = new CombinedUserEntity(userID, userLoc, MultiplexerUtils.getLowestAvailableUniqueCombinedUserID());
						Multiplexer.users.add(user);
						Multiplexer.writeToAnnouncementBox("Discovered Entity " + user.getUniqueID() + " (Person " + userLoc.getID() + " from " + source + ")");
						TrackerUpdater.sendUniqueIDUpdateToTrackers(user);
					}
				}

				ArrayList<String> missingIDs = new ArrayList<String>();
				if (existingIds.containsKey(source))
				{
					for (int i : existingIds.get(source))
					{
						if (!newIDs.contains(i))
						{
							String userID = CombinedUserEntity.generateUserIDString(i, source);
							missingIDs.add(userID);
						}
					}
				}

				ArrayList<CombinedUserEntity> toRemove = new ArrayList<CombinedUserEntity>();
				for (String userID : missingIDs)
				{
					for (CombinedUserEntity user : Multiplexer.users)
					{
						if (user.getUserIDs().contains(userID))
						{
							user.getUserIDs().remove(userID);
							if (user.getUserIDs().size() == 0)
							{
								toRemove.add(user);
								Multiplexer.writeToAnnouncementBox("Lost track of Entity " + user.getUniqueID());
							}
							break;
						}
					}
				}

				for (CombinedUserEntity user : toRemove)
				{
					Multiplexer.users.remove(user);
				}

				existingIds.put(source, newIDs);
			}
		}
	};

	/**
	 * Instantiates a new multiplexer sync.
	 *
	 * @param c
	 *            the c
	 */
	public MultiplexerSync(TrackingDeviceControl c)
	{
		this.c = c;
		addSync();
	}

	/**
	 * Stop.
	 */
	public void stop()
	{
		c.getTeacherStatusControlVariable().unregisterChangeListener(teacherStatusChanged);
		c.getTrackedUserLocationsControlVariable().unregisterChangeListener(userLocations);
	}

	/**
	 * Adds the sync.
	 */
	private void addSync()
	{
		c.getTeacherStatusControlVariable().registerChangeListener(teacherStatusChanged);
		c.getTrackedUserLocationsControlVariable().registerChangeListener(userLocations);
	}

}
