package synergynet3.apps.numbernet.network;

import java.util.logging.Logger;

import multiplicity3.input.MultiTouchInputComponent;
import synergynet3.cluster.sharedmemory.DistributedPropertyChangedAction;
import synergynet3.web.apps.numbernet.comms.table.NumberNetStudentTableClusteredData;

import com.hazelcast.core.Member;

public class MultiTouchEnabledSync {
	private static final Logger log = Logger.getLogger(MultiTouchEnabledSync.class.getName());
	
	private MultiTouchInputComponent input;
	private NumberNetStudentTableClusteredData tableClusterData;

	public MultiTouchEnabledSync(NumberNetStudentTableClusteredData tableClusterData, MultiTouchInputComponent mtInput) {
		this.tableClusterData = tableClusterData;
		this.input = mtInput;
	}
	
	public void start() {
		setupTableDataClusterListeners();
	}
	
	public void stop() {
		//nothing to do
	}

	
	
	
	
	
	
	
	private void setupTableDataClusterListeners() {
		tableClusterData.getTouchEnabledControlVariable().registerChangeListener(new DistributedPropertyChangedAction<Boolean>() {			
			@Override
			public void distributedPropertyDidChange(Member m, Boolean oldValue, Boolean newValue) {
				log.info("Setting multi-touch enabled property to " + newValue);
				input.setMultiTouchInputEnabled(newValue);
			}
		});
	}


	
}
