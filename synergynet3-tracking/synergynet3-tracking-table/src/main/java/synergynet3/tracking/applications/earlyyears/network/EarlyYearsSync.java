package synergynet3.tracking.applications.earlyyears.network;

import synergynet3.cluster.sharedmemory.DistributedPropertyChangedAction;
import synergynet3.tracking.applications.earlyyears.EarlyYearsTrackedApp;
import synergynet3.tracking.applications.earlyyears.environmentexplorer.EnvironmentExplorerTrackedApp;
import synergynet3.tracking.applications.earlyyears.traintracks.TrainTracksTrackedApp;
import synergynet3.web.earlyyears.core.EarlyYearsDeviceControl;
import synergynet3.web.earlyyears.shared.EarlyYearsActivity;
import synergynet3.web.shared.messages.PerformActionMessage;
import synergynet3.web.shared.messages.PerformActionMessage.MESSAGESTATE;

import com.hazelcast.core.Member;

public class EarlyYearsSync {
	private EarlyYearsTrackedApp sneyNode;
	private EarlyYearsDeviceControl c;
	
	private DistributedPropertyChangedAction<EarlyYearsActivity> changeActivtyAction = new DistributedPropertyChangedAction<EarlyYearsActivity>() {
		@Override
		public void distributedPropertyDidChange(Member member,	EarlyYearsActivity oldValue, EarlyYearsActivity newValue) {
			sneyNode.setActivity(newValue);				
		}
	};
	
	private DistributedPropertyChangedAction<Integer> changeCornerNumberAction = new DistributedPropertyChangedAction<Integer>() {			
		@Override
		public void distributedPropertyDidChange(Member member, Integer oldValue, Integer newValue) {
			if(newValue == null) return;	
			if (sneyNode instanceof TrainTracksTrackedApp){
				oldValue = ((TrainTracksTrackedApp)sneyNode).getNumOfCorners();
				if (oldValue < newValue){
					for (int i = 0; i < (newValue - oldValue); i++)((TrainTracksTrackedApp)sneyNode).addCorner();
				}else if ((oldValue > newValue) && (newValue >= 0)){
					for (int i = 0; i < (oldValue - newValue); i++)((TrainTracksTrackedApp)sneyNode).removeCorner();
				}
			}
		}
	};
	
	private DistributedPropertyChangedAction<Integer> changeCrossNumberAction = new DistributedPropertyChangedAction<Integer>() {			
		@Override
		public void distributedPropertyDidChange(Member member, Integer oldValue, Integer newValue) {
			if(newValue == null) return;	
			if (sneyNode instanceof TrainTracksTrackedApp){
				oldValue = ((TrainTracksTrackedApp)sneyNode).getNumOfCrosses();
				if (oldValue < newValue){
					for (int i = 0; i < (newValue - oldValue); i++)((TrainTracksTrackedApp)sneyNode).addCross();
				}else if ((oldValue > newValue) && (newValue >= 0)){
					for (int i = 0; i < (oldValue - newValue); i++)((TrainTracksTrackedApp)sneyNode).removeCross();
				}
			}
		}
	};
	
	private DistributedPropertyChangedAction<Integer> changeStraightNumberAction = new DistributedPropertyChangedAction<Integer>() {			
		@Override
		public void distributedPropertyDidChange(Member member, Integer oldValue, Integer newValue) {
			if(newValue == null) return;	
			if (sneyNode instanceof TrainTracksTrackedApp){
				oldValue = ((TrainTracksTrackedApp)sneyNode).getNumOfStraights();
				if (oldValue < newValue){
					for (int i = 0; i < (newValue - oldValue); i++)((TrainTracksTrackedApp)sneyNode).addStraight();
				}else if ((oldValue > newValue) && (newValue >= 0)){
					for (int i = 0; i < (oldValue - newValue); i++)((TrainTracksTrackedApp)sneyNode).removeStraight();
				}
			}
		}
	};
	
	private DistributedPropertyChangedAction<PerformActionMessage> changeRoadModeAction = new DistributedPropertyChangedAction<PerformActionMessage>() {			
		@Override
		public void distributedPropertyDidChange(Member m, PerformActionMessage oldValue, PerformActionMessage newValue) {
			if (!newValue.messageAlreadyReceived()){
				if (sneyNode instanceof TrainTracksTrackedApp){
					if (newValue.getMessageState() == MESSAGESTATE.ACTIVATE){
						((TrainTracksTrackedApp)sneyNode).setMode(true);
					}else{
						((TrainTracksTrackedApp)sneyNode).setMode(false);					
					}
				}
			}
		}
	};
	
	private DistributedPropertyChangedAction<PerformActionMessage> changeShowTeacherControlAction = new DistributedPropertyChangedAction<PerformActionMessage>() {			
		@Override
		public void distributedPropertyDidChange(Member member, PerformActionMessage oldValue, PerformActionMessage newValue) {
			if (!newValue.messageAlreadyReceived()){
				if (sneyNode instanceof EnvironmentExplorerTrackedApp){
					if (newValue.getMessageState() == MESSAGESTATE.ACTIVATE){
						((EnvironmentExplorerTrackedApp) sneyNode).setTeacherControlVisibility(true);
					}else{
						((EnvironmentExplorerTrackedApp) sneyNode).setTeacherControlVisibility(false);						
					}
				}				
			}
		}
	};
	
	public EarlyYearsSync(EarlyYearsDeviceControl c, EarlyYearsTrackedApp earlyYearsApp) {
		this.c = c;
		this.sneyNode = earlyYearsApp;
		addSync();
	}
	
	public void reSync(EarlyYearsTrackedApp sneyNode) {
		this.sneyNode = sneyNode;
	}
	
	public void stop(){
		c.getActivity().unregisterChangeListener(changeActivtyAction);
		c.getRailWayCornerNumControl().unregisterChangeListener(changeCornerNumberAction);
		c.getRailWayCrossNumControl().unregisterChangeListener(changeCrossNumberAction);
		c.getRailWayStraightNumControl().unregisterChangeListener(changeStraightNumberAction);
		c.getExploreShowTeacherConsoleControl().unregisterChangeListener(changeShowTeacherControlAction);
		c.getRoadModeControl().unregisterChangeListener(changeRoadModeAction);
	}

	private void addSync() {
		c.getActivity().registerChangeListener(changeActivtyAction);		
		c.getRailWayCornerNumControl().registerChangeListener(changeCornerNumberAction);		
		c.getRailWayCrossNumControl().registerChangeListener(changeCrossNumberAction);		
		c.getRailWayStraightNumControl().registerChangeListener(changeStraightNumberAction);
		c.getExploreShowTeacherConsoleControl().registerChangeListener(changeShowTeacherControlAction);
		c.getRoadModeControl().registerChangeListener(changeRoadModeAction);
	}

}
