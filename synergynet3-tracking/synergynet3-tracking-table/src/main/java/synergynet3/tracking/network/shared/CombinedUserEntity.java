package synergynet3.tracking.network.shared;

import java.io.Serializable;
import java.util.ArrayList;

public class CombinedUserEntity implements Serializable {
	
	private static final long serialVersionUID = -2861616852462076282L;
	public static final String ID_TOKEN = "_-_";
	
	private ArrayList<String> userIDs = new ArrayList<String>();
	private UserLocation userLocation;
	private boolean isTeacher = false;
	private int uniqueID = 0;
	
	public CombinedUserEntity(String ID, UserLocation userLocation, int uniqueID){
		this.uniqueID = uniqueID;
		addUserID(ID);
		this.userLocation = userLocation;
	}
	
	public void addUserID(String ID){
		if (!userIDs.contains(ID))userIDs.add(ID);
	}
	
	public void removeUserID(String ID){
		if (userIDs.contains(ID))userIDs.remove(ID);
	}

	/**
	 * @return the userLocation
	 */
	public UserLocation getUserLocation() {
		return userLocation;
	}

	/**
	 * @param userLocation the userLocation to set
	 */
	public void setUserLocation(UserLocation userLocation) {
		this.userLocation = userLocation;
	}

	/**
	 * @return the userIDs
	 */
	public ArrayList<String> getUserIDs() {
		return userIDs;
	}

	/**
	 * @return the isTeacher
	 */
	public boolean isTeacher() {
		return isTeacher;
	}

	/**
	 * @param isTeacher the isTeacher to set
	 */
	public void setTeacher(boolean isTeacher) {
		this.isTeacher = isTeacher;
	}
	
	public static String getTrackerSourceFromID(String ID){
		try{
			return ID.split(CombinedUserEntity.ID_TOKEN)[1];
		}catch (Exception e){
			return "";
		}
	}
	
	public static int getUserIDFromID(String ID){
		try{
			return Integer.parseInt(ID.split(CombinedUserEntity.ID_TOKEN)[0]);
		}catch (Exception e){
			return -1;
		}
	}
	
	public static String generateUserIDString(int userID, String tableID){
		return userID + CombinedUserEntity.ID_TOKEN + tableID;
	}

	/**
	 * @return the uniqueID
	 */
	public int getUniqueID() {
		return uniqueID;
	}
	
}