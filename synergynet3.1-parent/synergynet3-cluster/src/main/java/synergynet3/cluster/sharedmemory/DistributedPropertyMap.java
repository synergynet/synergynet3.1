package synergynet3.cluster.sharedmemory;

import com.hazelcast.core.IMap;

/**
 * The Class DistributedPropertyMap.
 */
public class DistributedPropertyMap {

	/** The distributed property changed listener. */
	private DistributedPropertyChangedListener distributedPropertyChangedListener;

	/** The map. */
	private IMap<String, Object> map;

	/**
	 * Instantiates a new distributed property map.
	 *
	 * @param map the map
	 */
	public DistributedPropertyMap(IMap<String, Object> map) {
		this.map = map;
		this.distributedPropertyChangedListener = new DistributedPropertyChangedListener(
				map);
	}

	/**
	 * Creates the distributed property.
	 *
	 * @param <T> the generic type
	 * @param key the key
	 * @return the distributed property
	 */
	public <T> DistributedProperty<T> createDistributedProperty(String key) {
		return new DistributedProperty<T>(this, key);
	}

	/**
	 * Gets the distributed property changed listener.
	 *
	 * @return the distributed property changed listener
	 */
	public DistributedPropertyChangedListener getDistributedPropertyChangedListener() {
		return distributedPropertyChangedListener;
	}

	/**
	 * Gets the map.
	 *
	 * @return the map
	 */
	@SuppressWarnings("rawtypes")
	public IMap getMap() {
		return this.map;
	}
}
