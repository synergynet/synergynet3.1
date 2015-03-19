package synergynet3.cluster.clustereddevice;

import java.util.logging.Logger;

import synergynet3.cluster.SynergyNetCluster;
import synergynet3.cluster.sharedmemory.DistributedPropertyMap;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.IMap;

public abstract class ClusteredDevice {
	private static final Logger log = Logger.getLogger(ClusteredDevice.class.getName());
	
	private String name;
	private DistributedPropertyMap distributedPropertyMap;

	public ClusteredDevice(String deviceName) {
		this.name = deviceName;
		distributedPropertyMap = new DistributedPropertyMap(getControlVariableMapForDevice());
		log.fine(getClass().getName() + " with cluster time " + Hazelcast.getCluster().getClusterTime());
		SynergyNetCluster.get().getDeviceClusterManager().add(this);
	}
	
	public String getNameOfOwnerDevice() {
		return this.name;
	}
	
	public DistributedPropertyMap getDistributedPropertyMap() {
		return distributedPropertyMap;
	}

	//************************
	
	private <T> IMap<String,T> getControlVariableMapForDevice() {
		log.info("Getting map " + name + "_cvars");
		return Hazelcast.getMap(name + "_cvars");
	}
	
}
