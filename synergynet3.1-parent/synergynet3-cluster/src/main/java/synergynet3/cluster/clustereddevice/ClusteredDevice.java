package synergynet3.cluster.clustereddevice;

import java.util.logging.Logger;

import synergynet3.cluster.SynergyNetCluster;
import synergynet3.cluster.sharedmemory.DistributedPropertyMap;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.IMap;

/**
 * The Class ClusteredDevice.
 */
public abstract class ClusteredDevice {

	/** The Constant log. */
	private static final Logger log = Logger.getLogger(ClusteredDevice.class
			.getName());

	/** The distributed property map. */
	private DistributedPropertyMap distributedPropertyMap;

	/** The name. */
	private String name;

	/**
	 * Instantiates a new clustered device.
	 *
	 * @param deviceName the device name
	 */
	public ClusteredDevice(String deviceName) {
		this.name = deviceName;
		distributedPropertyMap = new DistributedPropertyMap(
				getControlVariableMapForDevice());
		log.fine(getClass().getName() + " with cluster time "
				+ Hazelcast.getCluster().getClusterTime());
		SynergyNetCluster.get().getDeviceClusterManager().add(this);
	}

	/**
	 * Gets the distributed property map.
	 *
	 * @return the distributed property map
	 */
	public DistributedPropertyMap getDistributedPropertyMap() {
		return distributedPropertyMap;
	}

	/**
	 * Gets the name of owner device.
	 *
	 * @return the name of owner device
	 */
	public String getNameOfOwnerDevice() {
		return this.name;
	}

	// ************************

	/**
	 * Gets the control variable map for device.
	 *
	 * @param <T> the generic type
	 * @return the control variable map for device
	 */
	private <T> IMap<String, T> getControlVariableMapForDevice() {
		log.info("Getting map " + name + "_cvars");
		return Hazelcast.getMap(name + "_cvars");
	}

}
