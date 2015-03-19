package synergynet3.activitypack1.table.gravitysim.network;

import java.util.logging.Logger;

import synergynet3.activitypack1.core.gravitysim.GravitySimDeviceControl;
import synergynet3.activitypack1.table.gravitysim.model.Universe;
import synergynet3.activitypack1.web.shared.UniverseScenario;
import synergynet3.cluster.sharedmemory.DistributedPropertyChangedAction;

import com.hazelcast.core.Member;

public class UniverseSync {
	private static Logger log = Logger.getLogger(UniverseSync.class.getName());
	private Universe universe;
	
	private DistributedPropertyChangedAction<Integer> bodyChangeAction = new DistributedPropertyChangedAction<Integer>() {			
		@Override
		public void distributedPropertyDidChange(Member member, Integer oldValue,
				Integer newValue) {
			if(newValue == null) return;
			log.info("Updating max bodies: " + newValue);
			universe.setMaxBodies(newValue);
		}
	};
	
	private DistributedPropertyChangedAction<Integer> removeAllBodiesChangeAction = new DistributedPropertyChangedAction<Integer>() {
		@Override
		public void distributedPropertyDidChange(Member member,	Integer oldValue, Integer newValue) {
			log.info("Removing bodies.");
			universe.removeAllBodies();
		}
	};
	
	private DistributedPropertyChangedAction<Double> gravitationalConstantChangeAction = new DistributedPropertyChangedAction<Double>() {
		@Override
		public void distributedPropertyDidChange(Member member,	Double oldValue, Double newValue) {
			if(newValue == null) return;
			log.info("Updating gravitational constant: " + newValue);
			universe.setGravitationalConstant(newValue);
		}
	};
	
	private DistributedPropertyChangedAction<Double> timeMultiplierChangeAction = new DistributedPropertyChangedAction<Double>() {
		@Override
		public void distributedPropertyDidChange(Member member,	Double oldValue, Double newValue) {
			if(newValue == null) return;
			log.info("Updating time multiplier: " + newValue);
			universe.setTimeMultiplier(newValue);
		}
	};
	
	private DistributedPropertyChangedAction<UniverseScenario> universeScenarioChangeAction = new DistributedPropertyChangedAction<UniverseScenario>() {

		@Override
		public void distributedPropertyDidChange(Member member,	UniverseScenario oldValue, UniverseScenario newValue) {
			universe.setScenario(newValue);				
		}
	};

	public UniverseSync(Universe universe) {
		this.universe = universe;
		addSync();
	}

	private void addSync() {
		GravitySimDeviceControl c = GravitySimDeviceControl.get();
		c.getBodyLimit().registerChangeListener(bodyChangeAction);		
		c.getClearBodiesTrigger().registerChangeListener(removeAllBodiesChangeAction);		
		c.getGravityControl().registerChangeListener(gravitationalConstantChangeAction);		
		c.getTimeControl().registerChangeListener(timeMultiplierChangeAction);		
		c.getScenario().registerChangeListener(universeScenarioChangeAction);
	}
	
	public void stop() {
		GravitySimDeviceControl c = GravitySimDeviceControl.get();
		c.getBodyLimit().unregisterChangeListener(bodyChangeAction);		
		c.getClearBodiesTrigger().unregisterChangeListener(removeAllBodiesChangeAction);		
		c.getGravityControl().unregisterChangeListener(gravitationalConstantChangeAction);		
		c.getTimeControl().unregisterChangeListener(timeMultiplierChangeAction);		
		c.getScenario().unregisterChangeListener(universeScenarioChangeAction);
	}

}
