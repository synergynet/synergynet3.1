package synergynet3.cluster.sharedmemory;

/**
 * The Class DistributedProperty.
 *
 * @param <T>
 *            the generic type
 */
public class DistributedProperty<T>
{

	/** The key. */
	private String key;

	/** The map. */
	private DistributedPropertyMap map;

	/**
	 * Instantiates a new distributed property.
	 *
	 * @param map
	 *            the map
	 * @param key
	 *            the key
	 */
	public DistributedProperty(DistributedPropertyMap map, String key)
	{
		this.map = map;
		this.key = key;
	}

	/**
	 * Gets the key.
	 *
	 * @return the key
	 */
	public String getKey()
	{
		return key;
	}

	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	@SuppressWarnings("unchecked")
	public T getValue()
	{
		return (T) map.getMap().get(key);
	}

	/**
	 * Register change listener.
	 *
	 * @param pca
	 *            the pca
	 */
	public void registerChangeListener(DistributedPropertyChangedAction<T> pca)
	{
		map.getDistributedPropertyChangedListener().registerPropertyChangedAction(getKey(), pca);
	}

	/**
	 * Sets the value.
	 *
	 * @param value
	 *            the new value
	 */
	@SuppressWarnings("unchecked")
	public void setValue(T value)
	{
		map.getMap().put(key, value);
	}

	/**
	 * Unregister change listener.
	 *
	 * @param pca
	 *            the pca
	 */
	public void unregisterChangeListener(DistributedPropertyChangedAction<T> pca)
	{
		map.getDistributedPropertyChangedListener().unregisterPropertyChangeAction(pca);
	}
}
