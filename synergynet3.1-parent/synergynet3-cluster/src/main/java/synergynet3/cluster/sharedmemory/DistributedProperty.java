package synergynet3.cluster.sharedmemory;


public class DistributedProperty <T> {
	private String key;
	private DistributedPropertyMap map;

	public DistributedProperty(DistributedPropertyMap map, String key) {
		this.map = map;
		this.key = key;
	}
	
	@SuppressWarnings("unchecked")
	public void setValue(T value) {
		map.getMap().put(key, value);
	}
	
	@SuppressWarnings("unchecked")
	public T getValue() {
		return (T) map.getMap().get(key);
	}

	public String getKey() {
		return key;
	}
	
	public void registerChangeListener(DistributedPropertyChangedAction<T> pca) {
		map.getDistributedPropertyChangedListener().registerPropertyChangedAction(getKey(), pca);
	}
	
	public void unregisterChangeListener(DistributedPropertyChangedAction<T> pca) {
		map.getDistributedPropertyChangedListener().unregisterPropertyChangeAction(pca);
	}
}