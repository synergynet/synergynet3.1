package synergynet3.cluster.sharedmemory;


import com.hazelcast.core.IMap;

public class DistributedPropertyMap {
	private IMap<String,Object> map;
	private DistributedPropertyChangedListener distributedPropertyChangedListener;
	
	public DistributedPropertyMap(IMap<String,Object> map) {
		this.map = map;
		this.distributedPropertyChangedListener = new DistributedPropertyChangedListener(map);
	}
	
	@SuppressWarnings("rawtypes")
	public IMap getMap() {
		return this.map;
	}

	public DistributedPropertyChangedListener getDistributedPropertyChangedListener() {
		return distributedPropertyChangedListener;
	}
	
	public <T> DistributedProperty<T> createDistributedProperty(String key) {
		return new DistributedProperty<T>(this, key);
	}
}
