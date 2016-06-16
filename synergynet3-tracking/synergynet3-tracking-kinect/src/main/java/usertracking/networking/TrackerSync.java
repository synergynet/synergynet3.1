package usertracking.networking;

import synergynet3.cluster.sharedmemory.DistributedPropertyChangedAction;
import synergynet3.tracking.network.core.TrackingDeviceControl;
import usertracking.TrackerUtils;
import usertracking.UserTracker;

import com.hazelcast.core.Member;

/**
 * The Class TrackerSync.
 */
public class TrackerSync
{

	/** The c. */
	private TrackingDeviceControl c;

	/** The colour changed action. */
	private DistributedPropertyChangedAction<String> colourChangedAction = new DistributedPropertyChangedAction<String>()
	{
		@Override
		public void distributedPropertyDidChange(Member m, String oldValue, String newValue)
		{
			int userID = TrackerUtils.getUserIdFromMessage(newValue);
			if (userID > -1)
			{
				int uniqueID = TrackerUtils.getUniqueIDFromMessage(newValue);
				TrackerNetworking.uniqueIDs[userID] = uniqueID;
				TeacherControlPanel.getInstance().fillTable();
			}
		}
	};

	// TODO: Not receiving
	/** The gesture control action. */
	private DistributedPropertyChangedAction<Integer> gestureControlAction = new DistributedPropertyChangedAction<Integer>()
	{
		@Override
		public void distributedPropertyDidChange(Member m, Integer oldValue, Integer newValue)
		{
			TrackerNetworking.gestureControlReceive(newValue);
		}
	};

	/** The table deselected action. */
	private DistributedPropertyChangedAction<String> tableDeselectedAction = new DistributedPropertyChangedAction<String>()
	{
		@Override
		public void distributedPropertyDidChange(Member m, String oldValue, String newValue)
		{
			if (UserTracker.SELECTED_TABLES.contains(newValue))
			{
				UserTracker.SELECTED_TABLES.remove(newValue);
			}
			TrackerNetworking.cancelPosesAndClearGestureSequences();
		}
	};

	/** The table selected action. */
	private DistributedPropertyChangedAction<String> tableSelectedAction = new DistributedPropertyChangedAction<String>()
	{
		@Override
		public void distributedPropertyDidChange(Member m, String oldValue, String newValue)
		{
			if (UserTracker.SELECTED_TABLES.contains(UserTracker.ALL_TABLES))
			{
				TrackerNetworking.switchToIndividualTableSelectMode();
			}
			if (!UserTracker.SELECTED_TABLES.contains(newValue))
			{
				UserTracker.SELECTED_TABLES.add(newValue);
			}
			TrackerNetworking.cancelPosesAndClearGestureSequences();
		}
	};

	/** The teacher status changed action. */
	private DistributedPropertyChangedAction<String> teacherStatusChangedAction = new DistributedPropertyChangedAction<String>()
	{
		@Override
		public void distributedPropertyDidChange(Member m, String oldValue, String newValue)
		{
			int userID = TrackerUtils.getUserIdFromMessage(newValue);
			if (userID > -1)
			{
				boolean isTeacher = TrackerUtils.getTeacherStatusFromTeacherStatusMessage(newValue);
				TrackerNetworking.teacherStatuses[userID] = isTeacher;
				TeacherControlPanel.getInstance().fillTable();
			}
		}
	};

	/**
	 * Instantiates a new tracker sync.
	 *
	 * @param c
	 *            the c
	 */
	public TrackerSync(TrackingDeviceControl c)
	{
		this.c = c;
		addSync();
	}

	/**
	 * Stop.
	 */
	public void stop()
	{
		c.getTableSelectedControlVariable().unregisterChangeListener(tableSelectedAction);
		c.getTableDeselectedControlVariable().unregisterChangeListener(tableDeselectedAction);
		c.getGestureControlVariable().unregisterChangeListener(gestureControlAction);
		c.getTeacherStatusToTrackerControlVariable().unregisterChangeListener(teacherStatusChangedAction);
		c.getUniqueIDToTrackerControlVariable().unregisterChangeListener(colourChangedAction);
	}

	/**
	 * Adds the sync.
	 */
	private void addSync()
	{
		c.getTableSelectedControlVariable().registerChangeListener(tableSelectedAction);
		c.getTableDeselectedControlVariable().registerChangeListener(tableDeselectedAction);
		c.getGestureControlVariable().registerChangeListener(gestureControlAction);
		c.getTeacherStatusToTrackerControlVariable().registerChangeListener(teacherStatusChangedAction);
		c.getUniqueIDToTrackerControlVariable().registerChangeListener(colourChangedAction);
	}

}
