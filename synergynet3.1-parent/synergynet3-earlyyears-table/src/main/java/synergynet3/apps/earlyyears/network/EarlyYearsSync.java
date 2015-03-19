package synergynet3.apps.earlyyears.network;

import synergynet3.apps.earlyyears.applications.EarlyYearsApp;
import synergynet3.apps.earlyyears.applications.environmentexplorer.EnvironmentExplorerApp;
import synergynet3.apps.earlyyears.applications.traintracks.TrainTracksApp;
import synergynet3.cluster.sharedmemory.DistributedPropertyChangedAction;
import synergynet3.web.earlyyears.core.EarlyYearsDeviceControl;
import synergynet3.web.earlyyears.shared.EarlyYearsActivity;
import synergynet3.web.shared.messages.PerformActionMessage;
import synergynet3.web.shared.messages.PerformActionMessage.MESSAGESTATE;

import com.hazelcast.core.Member;

public class EarlyYearsSync {
	private EarlyYearsApp sneyNode;
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
			if (sneyNode instanceof TrainTracksApp){
				oldValue = ((TrainTracksApp)sneyNode).getNumOfCorners();
				if (oldValue < newValue){
					for (int i = 0; i < (newValue - oldValue); i++)((TrainTracksApp)sneyNode).addCorner();
				}else if ((oldValue > newValue) && (newValue >= 0)){
					for (int i = 0; i < (oldValue - newValue); i++)((TrainTracksApp)sneyNode).removeCorner();
				}
			}
		}
	};
	
	private DistributedPropertyChangedAction<Integer> changeCrossNumberAction = new DistributedPropertyChangedAction<Integer>() {			
		@Override
		public void distributedPropertyDidChange(Member member, Integer oldValue, Integer newValue) {
			if(newValue == null) return;	
			if (sneyNode instanceof TrainTracksApp){
				oldValue = ((TrainTracksApp)sneyNode).getNumOfCrosses();
				if (oldValue < newValue){
					for (int i = 0; i < (newValue - oldValue); i++)((TrainTracksApp)sneyNode).addCross();
				}else if ((oldValue > newValue) && (newValue >= 0)){
					for (int i = 0; i < (oldValue - newValue); i++)((TrainTracksApp)sneyNode).removeCross();
				}
			}
		}
	};
	
	private DistributedPropertyChangedAction<Integer> changeStraightNumberAction = new DistributedPropertyChangedAction<Integer>() {			
		@Override
		public void distributedPropertyDidChange(Member member, Integer oldValue, Integer newValue) {
			if(newValue == null) return;	
			if (sneyNode instanceof TrainTracksApp){
				oldValue = ((TrainTracksApp)sneyNode).getNumOfStraights();
				if (oldValue < newValue){
					for (int i = 0; i < (newValue - oldValue); i++)((TrainTracksApp)sneyNode).addStraight();
				}else if ((oldValue > newValue) && (newValue >= 0)){
					for (int i = 0; i < (oldValue - newValue); i++)((TrainTracksApp)sneyNode).removeStraight();
				}
			}
		}
	};
	
	private DistributedPropertyChangedAction<PerformActionMessage> changeRoadModeAction = new DistributedPropertyChangedAction<PerformActionMessage>() {			
		@Override
		public void distributedPropertyDidChange(Member m, PerformActionMessage oldValue, PerformActionMessage newValue) {
			if (!newValue.messageAlreadyReceived()){
				if (sneyNode instanceof TrainTracksApp){
					if (newValue.getMessageState() == MESSAGESTATE.ACTIVATE){
						((TrainTracksApp)sneyNode).setMode(true);
					}else{
						((TrainTracksApp)sneyNode).setMode(false);	
					}	
				}
			}
		}
	};
	
	private DistributedPropertyChangedAction<PerformActionMessage> changeShowTeacherControlAction = new DistributedPropertyChangedAction<PerformActionMessage>() {			
		@Override
		public void distributedPropertyDidChange(Member member, PerformActionMessage oldValue, PerformActionMessage newValue) {
			if (!newValue.messageAlreadyReceived()){
				if (sneyNode instanceof EnvironmentExplorerApp){
					if (newValue.getMessageState() == MESSAGESTATE.ACTIVATE){
						((EnvironmentExplorerApp) sneyNode).setTeacherControlVisibility(true);
					}else{
						((EnvironmentExplorerApp) sneyNode).setTeacherControlVisibility(false);						
					}
				}				
			}
		}
	};
	
	public EarlyYearsSync(EarlyYearsDeviceControl c, EarlyYearsApp sneyNode) {
		this.c = c;
		this.sneyNode = sneyNode;
		addSync();
	}
	
	public void reSync(EarlyYearsApp sneyNode) {
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
