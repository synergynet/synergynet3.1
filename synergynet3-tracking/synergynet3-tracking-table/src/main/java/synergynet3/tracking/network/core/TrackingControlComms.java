package synergynet3.tracking.network.core;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.hazelcast.core.Hazelcast;

import synergynet3.cluster.SynergyNetCluster;
import synergynet3.tracking.network.shared.CombinedUserEntity;
import synergynet3.tracking.network.shared.PointDirection;
import synergynet3.tracking.network.shared.UserLocations;
import synergynet3.web.shared.messages.PerformActionMessage;

public class TrackingControlComms {	
	private static final Logger log = Logger.getLogger(TrackingControlComms.class.getName());

	private static TrackingControlComms instance;

	public static TrackingControlComms get() {
		synchronized(TrackingControlComms.class) {
			if(instance == null) instance = new TrackingControlComms();			
			return instance;
		}
	}

	private TrackingControlComms() {
		log.info(getClass().getName() + " with cluster time " + Hazelcast.getCluster().getClusterTime());
	}

	public TrackingDeviceControl getDeviceForName(String device) {
		try{
			TrackingDeviceControl std = (TrackingDeviceControl) SynergyNetCluster.get().getDeviceClusterManager().getClusteredDeviceByName(device);
			if(std == null) {
				std = new TrackingDeviceControl(device);
			}
			return std;
		}catch (ClassCastException e){
			return new TrackingDeviceControl(device);
		}
	}
	
	/**
	 * Get the number of all the devices connected to the network cluster of 'trackers' type.
	 * 
	 * @return A float representing the number of devices connected to the network cluster of 'trackers' type.
	 */
	public int getNumberOfTrackersOnline() {		
		return SynergyNetCluster.get().getPresenceManager().getDeviceNamesOnline("trackers").size();		
	}
	
	public List<String> getTrackersList() {		
		return SynergyNetCluster.get().getPresenceManager().getDeviceNamesOnline("trackers");		
	}
	
	public List<String> getTablesList() {		
		return SynergyNetCluster.get().getPresenceManager().getDeviceNamesOnline("tables");		
	}
	
	public List<String> getMultiplexersList() {		
		return SynergyNetCluster.get().getPresenceManager().getDeviceNamesOnline("multiplexers");		
	}

	
	// -------------- Tracker Functions -------------- 
		
	public void broadcastUserLocations(UserLocations userLocations) {
		for(String device : getMultiplexersList()) {
			try {
				TrackingDeviceControl std = getDeviceForName(device);
				std.getTrackedUserLocationsControlVariable().setValue(userLocations);
			}catch(Exception ex) {}
		}
	}
	
	public void updateTeacherStatus(String message) {
		for(String device : getMultiplexersList()) {
			try {
				TrackingDeviceControl std = getDeviceForName(device);
				std.getTeacherStatusControlVariable().setValue(message);
			}catch(Exception ex) {}
		}
	}
	
	public void setAllTablesSelectedModeEnabled(PerformActionMessage message) {
		for(String device : getTablesList()) {
			try {
				TrackingDeviceControl std = getDeviceForName(device);
				std.getAllTablesSelectedModeEnabledControlVariable().setValue(message);
			}catch(Exception ex) {}
		}
	}

	public void setIndividualTableSelectModeEnabled(PerformActionMessage message) {
		for(String device : getTablesList()) {
			try {
				TrackingDeviceControl std = getDeviceForName(device);
				std.getIndividualTableSelectModeEnabledControlVariable().setValue(message);
			}catch(Exception ex) {}
		}
	}

	public void setTableSelectdModeDisabled(PerformActionMessage message) {
		for(String device : getTablesList()) {
			try {
				TrackingDeviceControl std = getDeviceForName(device);
				std.getTableSelectedModeDisabledControlVariable().setValue(message);
			}catch(Exception ex) {}
		}
	}	
	
	public void announcePointingGesture(PointDirection pointDirection) {
		for(String device : getTablesList()) {
			try {
				TrackingDeviceControl std = getDeviceForName(device);
				std.getPointingControlVariable().setValue(pointDirection);
			}catch(Exception ex) {}
		}
	}	
	
	
	// -------------- Tracked App functions -------------- 

	public void announceSelection(String trackingTableIdentity) {
		for(String device : getTrackersList()) {
			try {
				TrackingDeviceControl std = getDeviceForName(device);
				std.getTableSelectedControlVariable().setValue(trackingTableIdentity);				
			}catch(Exception ex) {}
		}		
	}

	public void announceDeSelection(String trackingTableIdentity) {
		for(String device : getTrackersList()) {
			try {
				TrackingDeviceControl std = getDeviceForName(device);
				std.getTableDeselectedControlVariable().setValue(trackingTableIdentity);				
			}catch(Exception ex) {}
		}	
	}

	public void setGestureControlBroadcast(int userID, String localDevice) {
		List<String> devices = getTrackersList();
		devices.add(localDevice);
		for(String device : devices) {
			try {
				TrackingDeviceControl std = getDeviceForName(device);
				std.getGestureControlVariable().setValue(userID);				
			}catch(Exception ex) {}
		}	
	}
	
	// -------------- Multiplexer functions -------------- 

	public void sendUserLocationsToAllTables(ArrayList<CombinedUserEntity> users) {
		for(String device : getTablesList()) {
			try {
				TrackingDeviceControl std = getDeviceForName(device);
				std.getUserLocationsControlVariable().setValue(users);
			}catch(Exception ex) {}
		}
	
	}

	public void sendTeacherStatusToSpecifcTracker(String message, String device) {
		try {
			TrackingDeviceControl std = getDeviceForName(device);
			std.getTeacherStatusToTrackerControlVariable().setValue(message);
		}catch(Exception ex) {}		
	}
	
	public void sendColourToSpecifcTracker(String message, String device) {
		try {
			TrackingDeviceControl std = getDeviceForName(device);
			std.getUniqueIDToTrackerControlVariable().setValue(message);
		}catch(Exception ex) {}		
	}

}
