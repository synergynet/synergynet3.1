package synergynet3.tracking.network.shared;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * The Class CombinedUserEntity.
 */
public class CombinedUserEntity implements Serializable
{

	/** The Constant ID_TOKEN. */
	public static final String ID_TOKEN = "_-_";

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -2861616852462076282L;

	/** The is teacher. */
	private boolean isTeacher = false;

	/** The unique id. */
	private int uniqueID = 0;

	/** The user i ds. */
	private ArrayList<String> userIDs = new ArrayList<String>();

	/** The user location. */
	private UserLocation userLocation;

	/**
	 * Instantiates a new combined user entity.
	 *
	 * @param ID
	 *            the id
	 * @param userLocation
	 *            the user location
	 * @param uniqueID
	 *            the unique id
	 */
	public CombinedUserEntity(String ID, UserLocation userLocation, int uniqueID)
	{
		this.uniqueID = uniqueID;
		addUserID(ID);
		this.userLocation = userLocation;
	}

	/**
	 * Generate user id string.
	 *
	 * @param userID
	 *            the user id
	 * @param tableID
	 *            the table id
	 * @return the string
	 */
	public static String generateUserIDString(int userID, String tableID)
	{
		return userID + CombinedUserEntity.ID_TOKEN + tableID;
	}

	/**
	 * Gets the tracker source from id.
	 *
	 * @param ID
	 *            the id
	 * @return the tracker source from id
	 */
	public static String getTrackerSourceFromID(String ID)
	{
		try
		{
			return ID.split(CombinedUserEntity.ID_TOKEN)[1];
		}
		catch (Exception e)
		{
			return "";
		}
	}

	/**
	 * Gets the user id from id.
	 *
	 * @param ID
	 *            the id
	 * @return the user id from id
	 */
	public static int getUserIDFromID(String ID)
	{
		try
		{
			return Integer.parseInt(ID.split(CombinedUserEntity.ID_TOKEN)[0]);
		}
		catch (Exception e)
		{
			return -1;
		}
	}

	/**
	 * Adds the user id.
	 *
	 * @param ID
	 *            the id
	 */
	public void addUserID(String ID)
	{
		if (!userIDs.contains(ID))
		{
			userIDs.add(ID);
		}
	}

	/**
	 * @return the uniqueID
	 */
	public int getUniqueID()
	{
		return uniqueID;
	}

	/**
	 * @return the userIDs
	 */
	public ArrayList<String> getUserIDs()
	{
		return userIDs;
	}

	/**
	 * @return the userLocation
	 */
	public UserLocation getUserLocation()
	{
		return userLocation;
	}

	/**
	 * @return the isTeacher
	 */
	public boolean isTeacher()
	{
		return isTeacher;
	}

	/**
	 * Removes the user id.
	 *
	 * @param ID
	 *            the id
	 */
	public void removeUserID(String ID)
	{
		if (userIDs.contains(ID))
		{
			userIDs.remove(ID);
		}
	}

	/**
	 * @param isTeacher
	 *            the isTeacher to set
	 */
	public void setTeacher(boolean isTeacher)
	{
		this.isTeacher = isTeacher;
	}

	/**
	 * @param userLocation
	 *            the userLocation to set
	 */
	public void setUserLocation(UserLocation userLocation)
	{
		this.userLocation = userLocation;
	}

}
