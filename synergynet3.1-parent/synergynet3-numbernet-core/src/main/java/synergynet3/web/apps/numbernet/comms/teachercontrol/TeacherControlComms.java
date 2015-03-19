package synergynet3.web.apps.numbernet.comms.teachercontrol;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.hazelcast.core.Hazelcast;

import synergynet3.cluster.SynergyNetCluster;
import synergynet3.web.apps.numbernet.comms.table.NumberNetStudentTableClusteredData;
import synergynet3.web.apps.numbernet.comms.table.ProjectorDevice;
import synergynet3.web.apps.numbernet.comms.table.TargetMaps;
import synergynet3.web.apps.numbernet.shared.CalculatorKey;
import synergynet3.web.apps.numbernet.shared.Expression;
import synergynet3.web.apps.numbernet.shared.Participant;
import synergynet3.web.apps.numbernet.shared.ProjectionDisplayMode;
import synergynet3.web.apps.numbernet.shared.TableTarget;

public class TeacherControlComms {
	private static final Logger log = Logger.getLogger(TeacherControlComms.class.getName());


	private static TeacherControlComms instance;

	public static TeacherControlComms get() {
		synchronized(TeacherControlComms.class) {
			if(instance == null) instance = new TeacherControlComms();			
			return instance;
		}
	}

	private TeacherControlComms() {
		log.info(getClass().getName() + " with cluster time " + Hazelcast.getCluster().getClusterTime());
	}

	public void setAllCalculatorsVisible(boolean visible) {
		//for(String device : SynergyNetCluster.get().getClusteredDataManager().getNamesOfDevicesInCluster(NumberNetStudentTableClusteredData.deviceType)) {
		for(String device : getExpectedTablesList()) {
			try {
			NumberNetStudentTableClusteredData std = getStudentTableDeviceForName(device);
			std.getCalculatorVisibleControlVariable().setValue(visible);
			}catch(Exception ex) {}
		}
	}

	public void setTableInputEnabled(boolean enabled) {
		//for(String device : SynergyNetCluster.get().getClusteredDataManager().getNamesOfDevicesInCluster(NumberNetStudentTableClusteredData.deviceType)) {
		for(String device : getExpectedTablesList()) {
			try {
			NumberNetStudentTableClusteredData std = getStudentTableDeviceForName(device);
			std.getTouchEnabledControlVariable().setValue(enabled);
			}catch(Exception ex) {}
		}		
	}

	public NumberNetStudentTableClusteredData getStudentTableDeviceForName(String device) {
		try{
			NumberNetStudentTableClusteredData std = (NumberNetStudentTableClusteredData) SynergyNetCluster.get().getDeviceClusterManager().getClusteredDeviceByName(device);
			if(std == null) {
				std = new NumberNetStudentTableClusteredData(device);
			}
			return std;
		}catch (ClassCastException e){
			return new NumberNetStudentTableClusteredData(device);
		}
	}

	public ProjectorDevice getProjectorDeviceForName(String device) {
		try{
			ProjectorDevice pd = (ProjectorDevice) SynergyNetCluster.get().getDeviceClusterManager().getClusteredDeviceByName(device);
			if(pd == null) {
				pd = new ProjectorDevice(device);
			}
			return pd;
		}catch (ClassCastException e){
			return new ProjectorDevice(device);
		}
	}

	public void addExpectedTable(String name) {
		getStudentTableDeviceForName(name); // this will auto-add a device for the table		
	}

	public List<String> getExpectedTablesList() {		
		List<String> list = new ArrayList<String>();
		list.addAll(SynergyNetCluster.get().getDeviceClusterManager().getNames());		
		list = removeDevicesFromGroup(list, "projectors");
		list = removeDevicesFromGroup(list, "trackers");
		list = removeDevicesFromGroup(list, "multiplexers");
		list = removeDevicesFromGroup(list, "teacherconsoles");
		List<String> online = SynergyNetCluster.get().getPresenceManager().getDeviceNamesOnline("tables");
		for(String o : online) {
			if(!list.contains(o)) {
				list.add(o);
			}
		}
		return list;		
	}

	private List<String> removeDevicesFromGroup(List<String> currentDeviceList, String groupToRemove){
		List<String> online = SynergyNetCluster.get().getPresenceManager().getDeviceNamesOnline(groupToRemove);
		for(String o : online) {
			if(currentDeviceList.contains(o)) {
				currentDeviceList.remove(o);
			}
		}
		return currentDeviceList;
	}
	
	public void assignParticipantToTable(String table, String participant) {
		log.info("Adding " + participant + " to " + table);
		List<Participant> list = getStudentTableDeviceForName(table).getParticipantListControlVariable().getValue();
		log.info("Current list: " + list);
		if(list == null) list = new ArrayList<Participant>();
		list.add(new Participant(participant));
		log.info("New list: " + list);
		getStudentTableDeviceForName(table).getParticipantListControlVariable().setValue(list);
	}

	public List<Participant> getParticipantListForTable(String table) {
		log.info("Getting participant list."); 
		List<Participant> list = getStudentTableDeviceForName(table).getParticipantListControlVariable().getValue();
		if(list == null) list = new ArrayList<Participant>();
		log.info("Returning: " + list);
		return list;
	}

	public List<Participant> getAllParticipants() {
		List<Participant> participants = new ArrayList<Participant>();

		for(String table : getExpectedTablesList()) {
			participants.addAll(getParticipantListForTable(table));
		}

		return participants;
	}

	public void removeParticipantFromTable(String table, String name) {
		log.info("Removing " + name + " from " + table);
		List<Participant> currentList = getStudentTableDeviceForName(table).getParticipantListControlVariable().getValue();
		if(currentList == null) currentList = new ArrayList<Participant>();
		ArrayList<Participant> newList = new ArrayList<Participant>();
		for(Participant p : currentList) {
			if(!p.getName().equals(name)) {
				newList.add(p);
			}
		}
		getStudentTableDeviceForName(table).getParticipantListControlVariable().setValue(newList);
	}

	//********** table targets **********

	public List<TableTarget> getTableTargets() {		
		List<TableTarget> list = new ArrayList<TableTarget>();
		for(String table : getExpectedTablesList()) {
			Double value = getStudentTableDeviceForName(table).getTargetValueControlVariable().getValue();
			TableTarget tt = new TableTarget(table, value);
			list.add(tt);
		}
		log.info("There are " + list.size() + " items in the table to target mapping list.");
		return list;
	}

	public void setTableTargets(List<TableTarget> targets) {
		log.info("Setting table targets with list " + targets);
		for(TableTarget tt : targets) {
			getStudentTableDeviceForName(tt.getTable()).getTargetValueControlVariable().setValue(tt.getTarget());
		}
	}

	public List<Expression> getExpressionsForTable(String table) {
		List<Expression> list = new ArrayList<Expression>();
		Double value = getStudentTableDeviceForName(table).getTargetValueControlVariable().getValue();
		if(value != null) {
			list.addAll(TargetMaps.get().getDistributedMapForTarget(value).values());
		}
		return list;
	}

	public List<Expression> getExpressionsForTarget(double target) {
		List<Expression> list = new ArrayList<Expression>();
		list.addAll(TargetMaps.get().getDistributedMapForTarget(target).values());		
		return list;
	}

	public List<Expression> getExpressionsForPerson(String person) {
		List<Expression> list = new ArrayList<Expression>();

		List<TableTarget> tablesAndTargets = getTableTargets();
		for(TableTarget tt : tablesAndTargets) {
			Double target = tt.getTarget();
			if(target != null) {
				Collection<Expression> expressions = TargetMaps.get().getDistributedMapForTarget(target).values();
				for(Expression e : expressions) {
					if(e.getCreatedBy().equals(person)) {
						list.add(e);
					}
				}
			}
		}

		return list;
	}

	public void projectTable(String table, String projector) {
		log.info("Projecting " + table + " onto " + projector);
		Double value = getStudentTableDeviceForName(table).getTargetValueControlVariable().getValue();
		if(value != null) {
			getProjectorDeviceForName(projector).getTargetToShowControlVariable().setValue(value);
			setProjectorDisplayMode(projector, ProjectionDisplayMode.TABLE_CLONE);
		}		
	}

	public void setProjectorDisplayMode(String projector, ProjectionDisplayMode mode) {
		getProjectorDeviceForName(projector).getProjectionDisplayModeControlVariable().setValue(mode);
	}
	
	public void setUnifyRotationModeEnabled(String projector, boolean enabled) {
		getProjectorDeviceForName(projector).getUnifyRotationControlVariable().setValue(enabled);
	}



	public void rotateTableContentAndTargets(List<TableTarget> to) {

		Map<String, Double> currentTargets = new HashMap<String,Double>();
		for(String table : getExpectedTablesList()) {
			currentTargets.put(table, getStudentTableDeviceForName(table).getTargetValueControlVariable().getValue());
		}

		Map<String, Double> assignTargets = new HashMap<String,Double>();
		for(TableTarget tableTarget : to) {
			assignTargets.put(tableTarget.getTable(), tableTarget.getTarget());
		}

		for(String table : getExpectedTablesList()) {
			try{
				getStudentTableDeviceForName(table).getTargetValueControlVariable().setValue(assignTargets.get(table));
			}catch (NullPointerException e){
				//Stop trying to get table for projector
			}
		}

	}

	public Map<CalculatorKey,Boolean> getCalculatorAllKeyStatesForTable(String tableName) {
		Map<CalculatorKey,Boolean> map = TeacherControlComms.get().getStudentTableDeviceForName(tableName).getCalculatorKeyStateMap().getValue();
		if (map == null)map = new HashMap<CalculatorKey,Boolean>();
		return map;
	}

	public void setCalculatorKeyStateForAllTables(CalculatorKey key, boolean state) {
		for(String device : getExpectedTablesList()) {
			Map<CalculatorKey,Boolean> map = TeacherControlComms.get().getStudentTableDeviceForName(device).getCalculatorKeyStateMap().getValue();
			if (map == null)map = new HashMap<CalculatorKey,Boolean>();
			map.put(key, state);
			TeacherControlComms.get().getStudentTableDeviceForName(device).getCalculatorKeyStateMap().setValue(map);
		}
	}

	public void setScoresVisibleForAllTables(boolean shouldBeVisible) {
		for(String device : getExpectedTablesList()) {
			getStudentTableDeviceForName(device).getScoresVisibleControlVariable().setValue(shouldBeVisible);
		}
	}	

	public void setGraphingModeEnabled(boolean graphingModeOn) {
		for(String device : getExpectedTablesList()) {
			getStudentTableDeviceForName(device).getGraphingModeControl().setValue(graphingModeOn);
		}
	}

	public void updatePositionInformationFromProjector(String projector) {
		getProjectorDeviceForName(projector).notifyShouldUpdatePosition();		
	}
	
	public void setCorrectExpressionsVisibleForAllTables(boolean shouldBeVisible) {
		for(String device : getExpectedTablesList()) {
			getStudentTableDeviceForName(device).getCorrectExpressionsVisibleControlVariable().setValue(shouldBeVisible);
		}
	}
	
	public void setIncorrectExpressionsVisibleForAllTables(boolean shouldBeVisible) {
		for(String device : getExpectedTablesList()) {
			getStudentTableDeviceForName(device).getIncorrectExpressionsVisibleControlVariable().setValue(shouldBeVisible);
		}
	}

	public void setOthersIncorrectExpressionsVisibleForAllTables(boolean shouldBeVisible) {
		for(String device : getExpectedTablesList()) {
			getStudentTableDeviceForName(device).getOthersIncorrectExpressionsVisibleControlVariable().setValue(shouldBeVisible);
		}
	}

	public void setOthersCorrectExpressionsVisibleForAllTables(boolean shouldBeVisible) {
		for(String device : getExpectedTablesList()) {
			getStudentTableDeviceForName(device).getOthersCorrectExpressionsVisibleControlVariable().setValue(shouldBeVisible);
		}		
	}
	
	
	
	

	public List<Double> getAllTargetsThatHaveOneOrMoreExpressions() {
		List<Double> targetsList = TargetMaps.get().getTargetsList();
		return targetsList;
	}







}
