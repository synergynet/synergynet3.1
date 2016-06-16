package usertracking.networking;

import synergynet3.cluster.SynergyNetCluster;
import synergynet3.tracking.network.core.TrackingControlComms;
import synergynet3.tracking.network.shared.UserLocation;
import synergynet3.tracking.network.shared.UserLocations;
import synergynet3.web.shared.messages.PerformActionMessage;
import synergynet3.web.shared.messages.PerformActionMessage.MESSAGESTATE;
import usertracking.GestureActions;
import usertracking.TrackerUtils;
import usertracking.UserTracker;

import com.jme3.math.Vector3f;

/**
 * The Class TrackerNetworking.
 */
public class TrackerNetworking
{

	/** The teacher statuses. */
	public static boolean[] teacherStatuses = new boolean[50];

	/** The unique i ds. */
	public static int[] uniqueIDs = new int[50];

	/** The user locations. */
	public static UserLocations userLocations = new UserLocations(getIdentity());

	/** The identity. */
	private static String identity = null;

	/**
	 * Broadcast clear user locations.
	 */
	public static void broadcastClearUserLocations()
	{
		synchronized (userLocations)
		{
			userLocations.clear();
			TrackingControlComms.get().broadcastUserLocations(userLocations);
		}
	}

	/**
	 * Broadcast user locations.
	 */
	public static void broadcastUserLocations()
	{
		synchronized (userLocations)
		{
			TrackingControlComms.get().broadcastUserLocations(userLocations);
		}
	}

	/**
	 * Cancel poses and clear gesture sequences.
	 */
	public static void cancelPosesAndClearGestureSequences()
	{
		GestureActions.resetPoseDetection();
		UserTracker.clearSequences();
	}

	/**
	 * Disable table selected mode.
	 */
	public static void disableTableSelectedMode()
	{
		TrackingControlComms.get().setTableSelectdModeDisabled(new PerformActionMessage(MESSAGESTATE.ACTIVATE));
	}

	/**
	 * Enable all tables selected mode.
	 */
	public static void enableAllTablesSelectedMode()
	{
		TrackingControlComms.get().setAllTablesSelectedModeEnabled(new PerformActionMessage(MESSAGESTATE.ACTIVATE));
	}

	/**
	 * Gesture control broadcast.
	 *
	 * @param userID
	 *            the user id
	 */
	public static void gestureControlBroadcast(int userID)
	{
		TrackingControlComms.get().setGestureControlBroadcast(userID, getIdentity());
	}

	/**
	 * Gesture control receive.
	 *
	 * @param newValue
	 *            the new value
	 */
	public static void gestureControlReceive(Integer newValue)
	{
		GestureActions.gestureControlUpdate(newValue);
	}

	/**
	 * Gets the identity.
	 *
	 * @return the identity
	 */
	public static String getIdentity()
	{
		if (identity == null)
		{
			identity = SynergyNetCluster.get().getIdentity();
		}
		return identity;
	}

	/**
	 * Gets the user location.
	 *
	 * @param userID
	 *            the user id
	 * @return the user location
	 */
	public static UserLocation getUserLocation(int userID)
	{
		synchronized (TrackerNetworking.userLocations)
		{
			for (UserLocation userLoc : userLocations.getUserLocations())
			{
				if (userID == userLoc.getID())
				{
					return userLoc;
				}
			}
			UserLocation userLoc = new UserLocation(userID, getIdentity());
			userLocations.getUserLocations().add(userLoc);
			TeacherControlPanel.getInstance().fillTable();
			return userLoc;
		}
	}

	/**
	 * Removes the user location.
	 *
	 * @param userID
	 *            the user id
	 */
	public static void removeUserLocation(int userID)
	{
		synchronized (TrackerNetworking.userLocations)
		{
			UserLocation contained = null;
			for (UserLocation userLoc : userLocations.getUserLocations())
			{
				if (userID == userLoc.getID())
				{
					contained = userLoc;
					break;
				}
			}
			if (contained != null)
			{
				userLocations.remove(contained);
			}
		}
	}

	/**
	 * Send teacher status update message.
	 *
	 * @param userID
	 *            the user id
	 * @param isTeacher
	 *            the is teacher
	 */
	public static void sendTeacherStatusUpdateMessage(int userID, boolean isTeacher)
	{
		String message = TrackerUtils.createTeacherStatusMessage(userID, getIdentity(), isTeacher);
		TrackingControlComms.get().updateTeacherStatus(message);
	}

	/**
	 * Sets the both user hand locations.
	 *
	 * @param userID
	 *            the user id
	 * @param xOne
	 *            the x one
	 * @param yOne
	 *            the y one
	 * @param zOne
	 *            the z one
	 * @param xTwo
	 *            the x two
	 * @param yTwo
	 *            the y two
	 * @param zTwo
	 *            the z two
	 */
	public static void setBothUserHandLocations(int userID, float xOne, float yOne, float zOne, float xTwo, float yTwo, float zTwo)
	{
		UserLocation userLocation = getUserLocation(userID);
		Vector3f handVecOne = TrackerPositioning.toRealWorldVectorInM(xOne, yOne, zOne);
		Vector3f handVecTwo = TrackerPositioning.toRealWorldVectorInM(xTwo, yTwo, zTwo);
		userLocation.setBothUserHandLocations(handVecOne.x, handVecOne.y, handVecOne.z, handVecTwo.x, handVecTwo.y, handVecTwo.z);
		setUserLocation(userLocation);
	}

	/**
	 * Sets the single user hand location.
	 *
	 * @param userID
	 *            the user id
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @param z
	 *            the z
	 */
	public static void setSingleUserHandLocation(int userID, float x, float y, float z)
	{
		UserLocation userLocation = getUserLocation(userID);
		Vector3f handVec = TrackerPositioning.toRealWorldVectorInM(x, y, z);
		userLocation.setSingleUserHandLocation(handVec.x, handVec.y, handVec.z);
		setUserLocation(userLocation);
	}

	/**
	 * Sets the user body location.
	 *
	 * @param userID
	 *            the user id
	 * @param x
	 *            the x
	 * @param z
	 *            the z
	 */
	public static void setUserBodyLocation(int userID, float x, float z)
	{
		UserLocation userLocation = getUserLocation(userID);
		Vector3f bodyVec = TrackerPositioning.toRealWorldVectorInM(x, 0, z);
		userLocation.setUserBodyLocation(bodyVec.x, bodyVec.y);
		setUserLocation(userLocation);
	}

	/**
	 * Sets the user location.
	 *
	 * @param userLocIn
	 *            the new user location
	 */
	public static void setUserLocation(UserLocation userLocIn)
	{
		synchronized (TrackerNetworking.userLocations)
		{
			removeUserLocation(userLocIn.getID());
			userLocations.add(userLocIn);
		}
	}

	/**
	 * Switch to individual table select mode.
	 */
	public static void switchToIndividualTableSelectMode()
	{
		UserTracker.SELECTED_TABLES.remove(UserTracker.ALL_TABLES);
		TrackingControlComms.get().setIndividualTableSelectModeEnabled(new PerformActionMessage(MESSAGESTATE.ACTIVATE));
	}
}
