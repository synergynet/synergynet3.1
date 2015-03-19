package usertracking.networking;

import com.jme3.math.Vector3f;

import synergynet3.cluster.SynergyNetCluster;
import synergynet3.tracking.network.core.TrackingControlComms;
import synergynet3.tracking.network.shared.UserLocation;
import synergynet3.tracking.network.shared.UserLocations;
import synergynet3.web.shared.messages.PerformActionMessage;
import synergynet3.web.shared.messages.PerformActionMessage.MESSAGESTATE;
import usertracking.GestureActions;
import usertracking.TrackerUtils;
import usertracking.UserTracker;

public class TrackerNetworking {
	
	public static UserLocations userLocations = new UserLocations(getIdentity());
	
	public static boolean[] teacherStatuses = new boolean[50];
	public static int[] uniqueIDs = new int[50];
	
	private static String identity = null;
		
	public static String getIdentity() {
		if (identity == null)identity = SynergyNetCluster.get().getIdentity();
		return identity;
	}
	
	public static UserLocation getUserLocation(int userID){
		synchronized(TrackerNetworking.userLocations){
			for (UserLocation userLoc: userLocations.getUserLocations()){
				if (userID == userLoc.getID()){
					return userLoc;
				}
			}
			UserLocation userLoc = new UserLocation(userID, getIdentity());
			userLocations.getUserLocations().add(userLoc);	
			TeacherControlPanel.getInstance().fillTable();
			return userLoc;
		}
	}	
	
	public static void setUserLocation(UserLocation userLocIn){
		synchronized(TrackerNetworking.userLocations){
			removeUserLocation(userLocIn.getID());
			userLocations.add(userLocIn);
		}
	}	
	
	public static void removeUserLocation(int userID){	
		synchronized(TrackerNetworking.userLocations){
			UserLocation contained = null;
			for (UserLocation userLoc: userLocations.getUserLocations()){
				if (userID == userLoc.getID()){
					contained = userLoc;
					break;
				}
			}
			if (contained != null)userLocations.remove(contained);
		}
	}	

	public static void setUserBodyLocation(int userID, float x, float z){		
		UserLocation userLocation = getUserLocation(userID);		
		Vector3f bodyVec = TrackerPositioning.toRealWorldVectorInM(x, 0, z);
		userLocation.setUserBodyLocation(bodyVec.x, bodyVec.y);
		setUserLocation(userLocation);
	}

	public static void setSingleUserHandLocation(int userID, float x, float y, float z){
		UserLocation userLocation = getUserLocation(userID);	
		Vector3f handVec = TrackerPositioning.toRealWorldVectorInM(x, y, z);	
		userLocation.setSingleUserHandLocation(handVec.x, handVec.y, handVec.z);
		setUserLocation(userLocation);
	}
	
	public static void setBothUserHandLocations(int userID, float xOne, float yOne, float zOne, float xTwo, float yTwo, float zTwo){
		UserLocation userLocation = getUserLocation(userID);
		Vector3f handVecOne = TrackerPositioning.toRealWorldVectorInM(xOne, yOne, zOne);	
		Vector3f handVecTwo = TrackerPositioning.toRealWorldVectorInM(xTwo, yTwo, zTwo);
		userLocation.setBothUserHandLocations(handVecOne.x, handVecOne.y, handVecOne.z, handVecTwo.x, handVecTwo.y, handVecTwo.z);
		setUserLocation(userLocation);
	}
	
	public static void broadcastUserLocations(){
		synchronized(userLocations){
			TrackingControlComms.get().broadcastUserLocations(userLocations);
		}
	}
	
	public static void broadcastClearUserLocations(){
		synchronized(userLocations){
			userLocations.clear();
			TrackingControlComms.get().broadcastUserLocations(userLocations);
		}
	}
	
	public static void enableAllTablesSelectedMode(){
		TrackingControlComms.get().setAllTablesSelectedModeEnabled(new PerformActionMessage(MESSAGESTATE.ACTIVATE));
	}	

	public static void switchToIndividualTableSelectMode(){
		UserTracker.SELECTED_TABLES.remove(UserTracker.ALL_TABLES);
		TrackingControlComms.get().setIndividualTableSelectModeEnabled(new PerformActionMessage(MESSAGESTATE.ACTIVATE));
	}
	
	public static void disableTableSelectedMode(){
		TrackingControlComms.get().setTableSelectdModeDisabled(new PerformActionMessage(MESSAGESTATE.ACTIVATE));
	}
	
	public static void gestureControlBroadcast(int userID) {
		TrackingControlComms.get().setGestureControlBroadcast(userID, getIdentity());		
	}	
	
	public static void gestureControlReceive(Integer newValue) {
		GestureActions.gestureControlUpdate(newValue);		
	}
	
	public static void cancelPosesAndClearGestureSequences(){
		GestureActions.resetPoseDetection();
		UserTracker.clearSequences();
	}

	public static void sendTeacherStatusUpdateMessage(int userID, boolean isTeacher) {
		String message = TrackerUtils.createTeacherStatusMessage(userID, getIdentity(), isTeacher);
		TrackingControlComms.get().updateTeacherStatus(message);		
	}
}