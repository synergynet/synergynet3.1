package usertracking;

import java.applet.Applet;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.jme3.math.Vector3f;

import synergynet3.projector.web.ProjectorControlComms;
import synergynet3.tracking.network.core.TrackingControlComms;
import synergynet3.tracking.network.shared.PointDirection;
import synergynet3.web.core.AppSystemControlComms;
import synergynet3.web.shared.messages.PerformActionMessage;
import synergynet3.web.shared.messages.PerformActionMessage.MESSAGESTATE;

import usertracking.networking.TrackerNetworking;
import usertracking.networking.TrackerPositioning;

public class GestureActions {

	private static final float POINT_MOVEMENT_THRESHOLD = 0.1f;
	
	private static final int TIME_OUT = 30000;
	
	private static final int ATTENTION_GET_TIME = 1500;
	private static final int ATTENTION_DISMISS_TIME = 4000;
	private static final int POINT_TIME = 1500;
	private static final int POSE_TIME = 2000;
	private static final int SLEEP_TIME = 100;	
	
	private static boolean kinectAttention = false;

	private static Date timeOutCountStartTime = null;
	
	private static HashMap<Integer, Date> usersLeftHandsUpActive = new HashMap<Integer, Date>();
	private static HashMap<Integer, Date> usersRightHandsUpActive = new HashMap<Integer, Date>();
	
	private static Date oneHandUpActive = null;	
	private static Date twoHandsUpActive = null;	
	private static Date handsTogetherActive = null;
	private static Date rightHandPointingActive = null;
	private static Date leftHandPointingActive = null;
	
	private static PointDirection rightHandPointingCurrent = null;
	private static PointDirection leftHandPointingCurrent  = null;
	
  	public static Thread poseThread = new Thread(new Runnable() {	  
		public void run() {
		    try{			
		    	while(TrackerPanel.isRunning){
		    		Thread.sleep(SLEEP_TIME);	
		    		if (kinectAttention){
		    			if (checkIfTimerHasExpired(timeOutCountStartTime, TIME_OUT + POSE_TIME)){
		    				UserTracker.writeToAnnouncementBox("Entity " + UserTracker.GESTURING_USER + " took too long to perform a gesture.\n\t  They are no longer in control.");	
							disableGestureControl();
			    		}else if (checkIfTimerHasExpired(oneHandUpActive, ATTENTION_DISMISS_TIME)){
			    			UserTracker.writeToAnnouncementBox("Entity " + UserTracker.GESTURING_USER + " held one arm in the air.\n\t  They are no longer in control.");
			    			disableGestureControl();
			    			resetPoseDetection();
			    		}else if (checkIfTimerHasExpired(twoHandsUpActive, POSE_TIME)){
			    			freezeTables();
			    			resetPoseDetection();
			    		}else if (checkIfTimerHasExpired(handsTogetherActive, POSE_TIME)){
			    			screenshotTables();
			    			resetPoseDetection();
			    		}else if (checkIfTimerHasExpired(rightHandPointingActive, POINT_TIME)){
			    			sendPointInfoToTables(Poses.rightHandPoint);
			    			timeOutCountStartTime = new Date();
			    			UserTracker.clearSequences();
			    			resetPoseDetection();
			    		}else if (checkIfTimerHasExpired(leftHandPointingActive, POINT_TIME)){
			    			sendPointInfoToTables(Poses.leftHandPoint);
			    			UserTracker.clearSequences();
			    			resetPoseDetection();
			    		}		
		    		}else{
		    			int holdingOneHandUp = checkIfOneHandUpTimersHaveExpired();
			    		if (holdingOneHandUp > -1){
			    			UserTracker.writeToAnnouncementBox("Entity " + TrackerNetworking.uniqueIDs[holdingOneHandUp] + " held an arm in the air.\n\t  They are now in control.");
			    			resetPoseDetection();
			    		}
		    		}
		    	}		    	
			} catch (InterruptedException ie) {}		
		}      		
  	});	
  		
  	public static boolean checkIfTimerHasExpired(Date poseStartTime, int timeOut){
  		if (poseStartTime == null)return false;
  		if (new Date().getTime() - poseStartTime.getTime() > timeOut)return true;
  		return false;
  	}
  	
  	public static int checkIfOneHandUpTimersHaveExpired(){
		for (Entry<Integer, Date> timer: usersRightHandsUpActive.entrySet()){
	  		if (new Date().getTime() - timer.getValue().getTime() > ATTENTION_GET_TIME){
	  			enableGestureControl(timer.getKey());
	  			return timer.getKey();
	  		}
		}
		for (Entry<Integer, Date> timer: usersLeftHandsUpActive.entrySet()){
	  		if (new Date().getTime() - timer.getValue().getTime() > ATTENTION_GET_TIME){
	  			enableGestureControl(timer.getKey());
	  			return timer.getKey();
	  		}
		}
  		return -1;
  	}
	
  	public static void resetPoseDetection(){
  		usersRightHandsUpActive.clear();
  		usersLeftHandsUpActive.clear();
  		oneHandUpActive = null;
  		twoHandsUpActive = null;
  		handsTogetherActive = null;
  		rightHandPointingActive = null;
  		leftHandPointingActive = null;
  		rightHandPointingCurrent = null;
  		leftHandPointingCurrent  = null;
  	}
  	
	public static void performGesture(int userID, PoseName gest, boolean isActivated){
		
		if (UserTracker.GESTURING_USER == TrackerNetworking.uniqueIDs[userID]){
			if (kinectAttention){
				
				switch (gest){
//				
//					case HORIZ_WAVE_RIGHT: {
//
//						break;
//					}
//					
//					case VERT_WAVE_RIGHT: {
//						
//						break;
//					}
//					
//					case HORIZ_WAVE_LEFT: {
//
//						break;
//					}
//					
//					case VERT_WAVE_LEFT: {
//						
//						break;
//					}
//					
					case PUSH_RIGHT: {
		    			sendTableContentsToProjectors();
						break;
					}
					
					case PUSH_LEFT: {						
		    			sendTableContentsToProjectors();		    			
						break;
					}
	
					case PULL_RIGHT: {
						sendProjectedContentsToTables();
						break;
					}
					
					case PULL_LEFT: {						
						sendProjectedContentsToTables();
						break;
					}
					
					case SWEEP_OUT: {					
						tidyTables();
						break;
					}
					                     
					case HANDS_NEAR: {
						if (isActivated){					  	
							handsTogetherActive = new Date();		
						}else{
							handsTogetherActive = null;
						}					
						break;
					}
//					
//					case HANDS_NEAR_TORSO: {
//				
//						break;
//					}
//					
//					case BOTH_HANDS_OUT: {
//	
//						break;
//					}
//					
//					case LEAN_LEFT: {
//						
//						break;
//					}
//					
//					case LEAN_RIGHT: {
//						
//						break;
//					}
//					
//					case LEAN_FWD: {
//						
//						break;
//					}
//					
//					case LEAN_BACK: {
//						
//						break;
//					}
//					
//					case TURN_RIGHT: {
//						
//						break;
//					}
//					
//					case TURN_LEFT: {
//						
//						break;
//					}
//					
					case POINT_RIGHT: {
						pointing(PoseName.POINT_RIGHT, userID, isActivated);
						break;
					}
					
					case POINT_LEFT: {
						pointing(PoseName.POINT_LEFT, userID, isActivated);
						break;
					}
//					
//					case LH_LHIP: {
//						
//						break;
//					}
//					
//					case RH_RHIP: {
//						
//						break;
//					}
//					
					case RH_UP: {
						handUpDuringActivation(PoseName.LH_UP, userID, isActivated);
						break;
					}
//					
//					case RH_OUT_H: {
//	
//	
//						break;
//					}
//					
//					case RH_IN_H: {
//						
//						break;
//					}
//					
//					case RH_DOWN: {
//		
//						break;
//					}
//					
					case LH_UP: {
						handUpDuringActivation(PoseName.RH_UP, userID, isActivated);
						break;
					}
				default:
					break;
									
//					case LH_OUT_H: {
//						
//						break;
//					}
//					
//					case LH_IN_H: {
//						
//						break;
//					}
//					
//					case LH_DOWN: {
//		
//						break;
//					}
//					
//					case RE_UP: {
//	
//						break;
//					}
//					
//					case LE_UP: {
//	
//						break;
//					}					
				}
			}
		}else if (UserTracker.GESTURING_USER == -1){				
			switch (gest){			
				case RH_UP: {
					rightHandUpPreActivation(userID, isActivated);
					break;
				}
					
				case LH_UP: {
					leftHandUpPreActivation(userID, isActivated);
					break;
				}
			default:
				break;
			}
		}
	}
	
	private static void rightHandUpPreActivation(int userID, boolean isActivated){
		if (isActivated){
			if (!usersRightHandsUpActive.containsKey(userID))usersRightHandsUpActive.put(userID, new Date());
		}else{
			if (usersRightHandsUpActive.containsKey(userID))usersRightHandsUpActive.remove(userID);
		}
	}

	private static void leftHandUpPreActivation(int userID, boolean isActivated){
		if (isActivated){
			if (!usersLeftHandsUpActive.containsKey(userID))usersLeftHandsUpActive.put(userID, new Date());
		}else{
			if (usersLeftHandsUpActive.containsKey(userID))usersLeftHandsUpActive.remove(userID);
		}
	}

	private static void handUpDuringActivation(PoseName poseToCheck, int userID, boolean isActivated){
		if (isActivated){
			if (isPoseActive(poseToCheck, userID)){
				if (oneHandUpActive != null)oneHandUpActive = null;
				if (twoHandsUpActive == null)twoHandsUpActive = new Date(); 
			}else{
				if (oneHandUpActive == null)oneHandUpActive = new Date();
			}
		}else{
			if (twoHandsUpActive != null)twoHandsUpActive = null;
			if (isPoseActive(poseToCheck, userID)){
				if (oneHandUpActive == null)oneHandUpActive = new Date();
			}else{
				if (oneHandUpActive != null)oneHandUpActive = null;
			}
		}
	}
	
	private static void pointing(PoseName poseToCheck, int userID, boolean isActivated) {		
		if (isActivated){
			if (poseToCheck == PoseName.POINT_RIGHT){				
				if (rightHandPointingCurrent != null){							
					boolean movedOrDeviated = false;					
					Vector3f currentStartPoint = new Vector3f(Poses.rightHandPoint.getStartPoint()[0], Poses.rightHandPoint.getStartPoint()[1], Poses.rightHandPoint.getStartPoint()[2]);
					Vector3f originalStartPoint = new Vector3f(rightHandPointingCurrent.getStartPoint()[0], rightHandPointingCurrent.getStartPoint()[1], rightHandPointingCurrent.getStartPoint()[2]);					
					float startPointMovement = currentStartPoint.distance(originalStartPoint);					
					if (startPointMovement > POINT_MOVEMENT_THRESHOLD * 1000){
						movedOrDeviated = true;
					}else{
						Vector3f currentEndPoint = new Vector3f(Poses.rightHandPoint.getEndPoint()[0], Poses.rightHandPoint.getEndPoint()[1], Poses.rightHandPoint.getEndPoint()[2]);
						Vector3f originalEndPoint = new Vector3f(rightHandPointingCurrent.getEndPoint()[0], rightHandPointingCurrent.getEndPoint()[1], rightHandPointingCurrent.getEndPoint()[2]);					
						float endPointMovement = currentEndPoint.distance(originalEndPoint);					
						if (endPointMovement > POINT_MOVEMENT_THRESHOLD * 1000){
							movedOrDeviated = true;
						}
					}
					
					if (movedOrDeviated){
						rightHandPointingCurrent = Poses.rightHandPoint;
						rightHandPointingActive = new Date();
					}
					
				}else{
					rightHandPointingCurrent = Poses.rightHandPoint;
					rightHandPointingActive = new Date();
				}				
			}else{
				if (leftHandPointingCurrent != null){							
					boolean movedOrDeviated = false;					
					Vector3f currentStartPoint = new Vector3f(Poses.leftHandPoint.getStartPoint()[0], Poses.leftHandPoint.getStartPoint()[1], Poses.leftHandPoint.getStartPoint()[2]);
					Vector3f originalStartPoint = new Vector3f(leftHandPointingCurrent.getStartPoint()[0], leftHandPointingCurrent.getStartPoint()[1], leftHandPointingCurrent.getStartPoint()[2]);					
					float startPointMovement = currentStartPoint.distance(originalStartPoint);					
					if (startPointMovement > POINT_MOVEMENT_THRESHOLD * 1000){
						movedOrDeviated = true;
					}else{
						Vector3f currentEndPoint = new Vector3f(Poses.leftHandPoint.getEndPoint()[0], Poses.leftHandPoint.getEndPoint()[1], Poses.leftHandPoint.getEndPoint()[2]);
						Vector3f originalEndPoint = new Vector3f(leftHandPointingCurrent.getEndPoint()[0], leftHandPointingCurrent.getEndPoint()[1], leftHandPointingCurrent.getEndPoint()[2]);					
						float endPointMovement = currentEndPoint.distance(originalEndPoint);					
						if (endPointMovement > POINT_MOVEMENT_THRESHOLD * 1000){
							movedOrDeviated = true;
						}
					}
					
					if (movedOrDeviated){
						leftHandPointingCurrent = Poses.leftHandPoint;
						leftHandPointingActive = new Date();
					}
					
				}else{
					leftHandPointingCurrent = Poses.leftHandPoint;
					leftHandPointingActive = new Date();
				}	
			}
		}else{
			if (poseToCheck == PoseName.POINT_RIGHT){	
				rightHandPointingCurrent = null;
				rightHandPointingActive = null;	
			}else{
				leftHandPointingCurrent = null;
				leftHandPointingActive = null;
			}
		}		
	}
	
	private static boolean isPoseActive(PoseName pose, int userID){
		if (!Poses.poseActivity.get(pose).containsKey(userID)){
			Poses.poseActivity.get(pose).put(userID, false);
			return false;
		}
		return Poses.poseActivity.get(pose).get(userID);
	}
	
	private static boolean noTableSelected(){
		if (UserTracker.SELECTED_TABLES.size() == 0){
			disableGestureControl();
			return true;
		}
		return false;
	}
	
	public static void enableGestureControl(int userID){	
		if (UserTracker.GESTURING_USER > 0)return;
		int uniqueID = TrackerNetworking.uniqueIDs[userID];
		if (uniqueID == 0)return;
		new Thread(){public void run(){Applet.newAudioClip(GestureActions.class.getResource("selected.wav")).play();}}.start();	
		timeOutCountStartTime = new Date();
		TrackerNetworking.gestureControlBroadcast(uniqueID);	
		TrackerNetworking.enableAllTablesSelectedMode();	
	}
	
	public static void disableGestureControl(){
		timeOutCountStartTime = null;
		TrackerNetworking.disableTableSelectedMode();
		TrackerNetworking.gestureControlBroadcast(-1);
	}
	
	public static void gestureControlUpdate(int userID){
		kinectAttention = false;
		if (userID == -1){
			UserTracker.GESTURING_USER = -1;
			UserTracker.SELECTED_TABLES.clear();	
	  		usersRightHandsUpActive.clear();
	  		usersLeftHandsUpActive.clear();
	  		oneHandUpActive = null;
			twoHandsUpActive = null;
			handsTogetherActive = null;
			TrackerNetworking.cancelPosesAndClearGestureSequences();
		}else{
			UserTracker.GESTURING_USER = userID;
			UserTracker.SELECTED_TABLES.clear();
			UserTracker.SELECTED_TABLES.add(UserTracker.ALL_TABLES);
			TrackerNetworking.cancelPosesAndClearGestureSequences();

			int localUserIdTemp = -1;
			
			for (int i = 0; i < TrackerNetworking.uniqueIDs.length; i++){
				if (TrackerNetworking.uniqueIDs[i] == userID){
					localUserIdTemp = i;
					break;
				}
			}
			
			final int localUserId = localUserIdTemp;
			
	      	new Thread(new Runnable() {	  
				public void run() {				
					try {
						Thread.sleep(POSE_TIME/2);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}				

					TrackerNetworking.cancelPosesAndClearGestureSequences();
					kinectAttention = true;		
					if (localUserId > -1){
						if (isPoseActive(PoseName.LH_UP, localUserId) && isPoseActive(PoseName.RH_UP, localUserId)){
							twoHandsUpActive = new Date();
						}else if (isPoseActive(PoseName.LH_UP, localUserId) || isPoseActive(PoseName.RH_UP, localUserId)){
							oneHandUpActive = new Date();
						}
					}
				}		      		
	      	}).start();   
		}
	}
	
	private static void sendProjectedContentsToTables() {
		
		UserTracker.writeToAnnouncementBox("Entity " + UserTracker.GESTURING_USER + " performed a pull gesture.\n\t  Sending projector contents to tables.");
		
		if (!noTableSelected()){
			if (UserTracker.SELECTED_TABLES.contains(UserTracker.ALL_TABLES)){
				ProjectorControlComms.get().allProjectorsSendContentsToTables(toStringArray(AppSystemControlComms.get().getTablesList()));
			}else{
				ProjectorControlComms.get().allProjectorsSendContentsToTables(toStringArray(UserTracker.SELECTED_TABLES));
			}
			disableGestureControl();
		}		
		
	}
	
	private static void screenshotTables() {
		
		UserTracker.writeToAnnouncementBox("Entity " + UserTracker.GESTURING_USER + " held both hands together.\n\t  Sending screenshots to projectors.");
		
		if (!noTableSelected()){
			if (UserTracker.SELECTED_TABLES.contains(UserTracker.ALL_TABLES)){
				AppSystemControlComms.get().allTablesSendScreenshotsToProjectors(toStringArray(AppSystemControlComms.get().getProjectorsList()));
			}else{
				for (String table: UserTracker.SELECTED_TABLES){
					AppSystemControlComms.get().specificTablesSendScreenshotsToProjectors(toStringArray(AppSystemControlComms.get().getProjectorsList()), table);
				}
			}
			disableGestureControl();
		}			
	}
	
	private static void freezeTables() {
		
		UserTracker.writeToAnnouncementBox("Entity " + UserTracker.GESTURING_USER + " held both hands in the air.\n\t  Freezing tables.");
		
		if (!noTableSelected()){
			if (UserTracker.SELECTED_TABLES.contains(UserTracker.ALL_TABLES)){
				AppSystemControlComms.get().allTablesFreeze(new PerformActionMessage(MESSAGESTATE.ACTIVATE));
			}else{
				for (String table: UserTracker.SELECTED_TABLES){
					AppSystemControlComms.get().specificTablesFreeze(new PerformActionMessage(MESSAGESTATE.ACTIVATE), table);
				}
			}
			disableGestureControl();
		}			
	}
	
	private static void sendTableContentsToProjectors() {
				
		UserTracker.writeToAnnouncementBox("Entity " + UserTracker.GESTURING_USER + " performed a push gesture.\n\t  Sending table contents to projectors.");
		
		if (!noTableSelected()){
			if (UserTracker.SELECTED_TABLES.contains(UserTracker.ALL_TABLES)){
				AppSystemControlComms.get().allTablesSendContentsToProjectors(toStringArray(AppSystemControlComms.get().getProjectorsList()));
			}else{
				for (String table: UserTracker.SELECTED_TABLES){
					AppSystemControlComms.get().specificTablesSendContentsToProjectors(toStringArray(AppSystemControlComms.get().getProjectorsList()), table);
				}
			}
			disableGestureControl();
					
		}
	}
	
	private static void tidyTables() {
		
		UserTracker.writeToAnnouncementBox("Entity " + UserTracker.GESTURING_USER + " performed a sweep gesture.\n\t  Tidying tables.");
		
		if (!noTableSelected()){
			if (UserTracker.SELECTED_TABLES.contains(UserTracker.ALL_TABLES)){
				AppSystemControlComms.get().allTablesRemoveAdditionalContent(new PerformActionMessage(MESSAGESTATE.ACTIVATE));
			}else{
				for (String table: UserTracker.SELECTED_TABLES){
					AppSystemControlComms.get().specificTableRemoveAdditionalContent(new PerformActionMessage(MESSAGESTATE.ACTIVATE), table);
				}
			}
			disableGestureControl();
		}			
	}
	
	private static void sendPointInfoToTables(PointDirection pointDirection) {
		if (pointDirection != null){
			
			UserTracker.writeToAnnouncementBox("Entity " + UserTracker.GESTURING_USER + " seen pointing.\n\t  Sending selection info to tables.");
			
			Vector3f startPoint = TrackerPositioning.toRealWorldVectorInM(pointDirection.getStartPoint()[0], pointDirection.getStartPoint()[1], pointDirection.getStartPoint()[2]);
			Vector3f endPoint = TrackerPositioning.toRealWorldVectorInM(pointDirection.getEndPoint()[0], pointDirection.getEndPoint()[1], pointDirection.getEndPoint()[2]);
			
			PointDirection worldPointDirection = new PointDirection();
			worldPointDirection.setStartPoint(startPoint.x, startPoint.y, startPoint.z);
			worldPointDirection.setEndPoint(endPoint.x, endPoint.y, endPoint.z);
			
			TrackingControlComms.get().announcePointingGesture(worldPointDirection);
		}
		
	}		
	
	private static String[] toStringArray(List<String> tablesList) {
		String[] result = new String[tablesList.size()];
		for (int i = 0 ; i < tablesList.size(); i++){
			result[i] = tablesList.get(i);
		}		
		return result;
	}

}