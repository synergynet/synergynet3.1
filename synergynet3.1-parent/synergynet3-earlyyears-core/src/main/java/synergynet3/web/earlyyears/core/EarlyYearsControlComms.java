package synergynet3.web.earlyyears.core;

import java.util.List;
import java.util.logging.Logger;

import com.hazelcast.core.Hazelcast;

import synergynet3.cluster.SynergyNetCluster;
import synergynet3.web.earlyyears.shared.EarlyYearsActivity;
import synergynet3.web.shared.messages.PerformActionMessage;

public class EarlyYearsControlComms {
	private static final Logger log = Logger.getLogger(EarlyYearsControlComms.class.getName());

	private static EarlyYearsControlComms instance;

	public static EarlyYearsControlComms get() {
		synchronized(EarlyYearsControlComms.class) {
			if(instance == null) instance = new EarlyYearsControlComms();			
			return instance;
		}
	}

	private EarlyYearsControlComms() {
		log.info(getClass().getName() + " with cluster time " + Hazelcast.getCluster().getClusterTime());
	}

	public EarlyYearsDeviceControl getEarlyYearsDeviceForName(String device) {
		try{
			EarlyYearsDeviceControl std = (EarlyYearsDeviceControl) SynergyNetCluster.get().getDeviceClusterManager().getClusteredDeviceByName(device);
			if(std == null) {
				std = new EarlyYearsDeviceControl(device);
			}
			return std;
		}catch (ClassCastException e){
			return new EarlyYearsDeviceControl(device);
		}
	}
	
	public List<String> getTablesList() {		
		return SynergyNetCluster.get().getPresenceManager().getDeviceNamesOnline("tables");		
	}
	
	public void setAllTablesScenario(EarlyYearsActivity scenario) {
		for(String device : getTablesList()) {
			try {
				EarlyYearsDeviceControl std = getEarlyYearsDeviceForName(device);
				std.getActivity().setValue(scenario);
			}catch(Exception ex) {}
		}
	}

	public void setSpecificTablesScenario(EarlyYearsActivity scenario, String table) {
		EarlyYearsDeviceControl std = getEarlyYearsDeviceForName(table);
		std.getActivity().setValue(scenario);		
	}

	public void setAllTablesRailwayCorners(int newNum) {
		for(String device : getTablesList()) {
			try {
				EarlyYearsDeviceControl std = getEarlyYearsDeviceForName(device);
				std.getRailWayCornerNumControl().setValue(newNum);
			}catch(Exception ex) {}
		}
	}

	public void setSpecificTablesRailwayCorners(int newNum, String table) {
		EarlyYearsDeviceControl std = getEarlyYearsDeviceForName(table);
		std.getRailWayCornerNumControl().setValue(newNum);
	}

	public void setAllTablesRailwayCrosses(int newNum) {
		for(String device : getTablesList()) {
			try {
				EarlyYearsDeviceControl std = getEarlyYearsDeviceForName(device);
				std.getRailWayCrossNumControl().setValue(newNum);
			}catch(Exception ex) {}
		}
	}

	public void setSpecificTablesRailwayCrosses(int newNum, String table) {
		EarlyYearsDeviceControl std = getEarlyYearsDeviceForName(table);
		std.getRailWayCrossNumControl().setValue(newNum);		
	}

	public void setAllTablesRailwayStraights(int newNum) {
		for(String device : getTablesList()) {
			try {
				EarlyYearsDeviceControl std = getEarlyYearsDeviceForName(device);
				std.getRailWayStraightNumControl().setValue(newNum);
			}catch(Exception ex) {}
		}
	}

	public void setSpecificTablesRailwayStraights(int newNum, String table) {
		EarlyYearsDeviceControl std = getEarlyYearsDeviceForName(table);
		std.getRailWayStraightNumControl().setValue(newNum);
	}
	
	public void setAllRoadMode(PerformActionMessage roadMode) {
		for(String device : getTablesList()) {
			try {
				EarlyYearsDeviceControl std = getEarlyYearsDeviceForName(device);
				std.getRoadModeControl().setValue(roadMode);
			}catch(Exception ex) {}
		}
	}
	
	public void setSpecificRoadMode(PerformActionMessage roadMode, String table) {
		try {
			EarlyYearsDeviceControl std = getEarlyYearsDeviceForName(table);
			std.getRoadModeControl().setValue(roadMode);
		}catch(Exception ex) {}		
	}
	
	public void setAllTablesExplorerShowTeacherControl(PerformActionMessage show) {
		for(String device : getTablesList()) {
			try {
				EarlyYearsDeviceControl std = getEarlyYearsDeviceForName(device);
				std.getExploreShowTeacherConsoleControl().setValue(show);
			}catch(Exception ex) {}
		}
	}
	
	public void setSpecificTablesExplorerShowTeacherControl(PerformActionMessage show, String table) {
		try {
			EarlyYearsDeviceControl std = getEarlyYearsDeviceForName(table);
			std.getExploreShowTeacherConsoleControl().setValue(show);
		}catch(Exception ex) {}
	}


}
