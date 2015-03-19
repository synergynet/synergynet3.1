package synergynet3.activitypack2.table.flickgame.network;

import synergynet3.activitypack2.core.FlickGameDeviceControl;
import synergynet3.activitypack2.table.flickgame.FlickGameApp;
import synergynet3.activitypack2.web.shared.flickgame.FlickGameScore;
import synergynet3.cluster.sharedmemory.DistributedPropertyChangedAction;

import com.hazelcast.core.Member;

public class FlickGameSync {
	private FlickGameApp flickGame;
	private FlickGameDeviceControl c;
	
	private DistributedPropertyChangedAction<FlickGameScore> scoreChangedAction = new DistributedPropertyChangedAction<FlickGameScore>() {
		@Override
		public void distributedPropertyDidChange(Member member,	FlickGameScore oldValue, FlickGameScore newValue) {
			flickGame.updateScore(newValue);				
		}
	};
	
	public FlickGameSync(FlickGameDeviceControl c, FlickGameApp flickGame) {
		this.c = c;
		this.flickGame = flickGame;
		addSync();
	}
	
	public void stop(){
		c.getScore().unregisterChangeListener(scoreChangedAction);		
	}

	private void addSync() {
		c.getScore().registerChangeListener(scoreChangedAction);	
	}

}