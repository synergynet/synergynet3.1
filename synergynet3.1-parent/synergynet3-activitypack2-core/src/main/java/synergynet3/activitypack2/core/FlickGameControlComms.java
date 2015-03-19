package synergynet3.activitypack2.core;

import java.util.List;
import java.util.logging.Logger;

import com.hazelcast.core.Hazelcast;

import synergynet3.activitypack2.web.shared.flickgame.FlickGameScore;
import synergynet3.cluster.SynergyNetCluster;

public class FlickGameControlComms {
	private static final Logger log = Logger.getLogger(FlickGameControlComms.class.getName());

	private static FlickGameControlComms instance;

	public static FlickGameControlComms get() {
		synchronized(FlickGameControlComms.class) {
			if(instance == null) instance = new FlickGameControlComms();			
			return instance;
		}
	}

	private FlickGameControlComms() {
		log.info(getClass().getName() + " with cluster time " + Hazelcast.getCluster().getClusterTime());
	}

	public FlickGameDeviceControl getFlickGameDeviceFromName(String device) {
		try{
			FlickGameDeviceControl std = (FlickGameDeviceControl) SynergyNetCluster.get().getDeviceClusterManager().getClusteredDeviceByName(device);
			if(std == null) {
				std = new FlickGameDeviceControl(device);
			}
			return std;
		}catch (ClassCastException e){
			return new FlickGameDeviceControl(device);
		}
	}
	
	public List<String> getTablesList() {		
		return SynergyNetCluster.get().getPresenceManager().getDeviceNamesOnline("tables");		
	}
	
	public void setAllTablesScore(FlickGameScore score) {
		for(String device : getTablesList()) {
			try {
				FlickGameDeviceControl std = getFlickGameDeviceFromName(device);
				std.getScore().setValue(score);
			}catch(Exception ex) {}
		}
	}

}
