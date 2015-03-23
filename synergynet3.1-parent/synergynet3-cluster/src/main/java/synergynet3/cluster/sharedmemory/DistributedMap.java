package synergynet3.cluster.sharedmemory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
// import java.util.logging.Logger;
import java.util.Set;
import java.util.concurrent.Callable;

import synergynet3.cluster.threading.ClusterThreadManager;

import com.hazelcast.core.EntryEvent;
import com.hazelcast.core.EntryListener;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.IMap;
import com.hazelcast.core.Member;

/**
 * The Class DistributedMap.
 *
 * @param <K> the key type
 * @param <V> the value type
 */
public class DistributedMap<K, V> {

	/**
	 * The Interface ItemAddedAction.
	 *
	 * @param <K> the key type
	 * @param <V> the value type
	 */
	public static interface ItemAddedAction<K, V> {

		/**
		 * Item added to collection.
		 *
		 * @param collection the collection
		 * @param itemKey the item key
		 * @param itemValue the item value
		 * @param member the member
		 */
		public void itemAddedToCollection(IMap<K, V> collection, K itemKey,
				V itemValue, Member member);
	}

	/**
	 * The Interface ItemRemovedAction.
	 *
	 * @param <K> the key type
	 * @param <V> the value type
	 */
	public static interface ItemRemovedAction<K, V> {

		/**
		 * Item removed from collection.
		 *
		 * @param collection the collection
		 * @param itemKey the item key
		 * @param itemValue the item value
		 * @param member the member
		 */
		public void itemRemovedFromCollection(IMap<K, V> collection, K itemKey,
				V itemValue, Member member);
	}

	/**
	 * The Interface ItemUpdatedAction.
	 *
	 * @param <K> the key type
	 * @param <V> the value type
	 */
	public static interface ItemUpdatedAction<K, V> {

		/**
		 * Item updated in collection.
		 *
		 * @param collection the collection
		 * @param itemKey the item key
		 * @param itemOldValue the item old value
		 * @param itemNewValue the item new value
		 * @param member the member
		 */
		public void itemUpdatedInCollection(IMap<K, V> collection, K itemKey,
				V itemOldValue, V itemNewValue, Member member);
	}

	/**
	 * The listener interface for receiving collectionChange events. The class
	 * that is interested in processing a collectionChange event implements this
	 * interface, and the object created with that class is registered with a
	 * component using the component's
	 * <code>addCollectionChangeListener<code> method. When
	 * the collectionChange event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @param <G> the generic type
	 * @param <H> the generic type
	 * @see CollectionChangeEvent
	 */
	private class CollectionChangeListener<G, H> implements EntryListener<G, H> {

		/** The added actions. */
		private List<ItemAddedAction<G, H>> addedActions;

		/** The collection. */
		private IMap<G, H> collection;

		/** The removed actions. */
		private List<ItemRemovedAction<G, H>> removedActions;

		/** The updated actions. */
		private List<ItemUpdatedAction<G, H>> updatedActions;

		/**
		 * Instantiates a new collection change listener.
		 *
		 * @param collection the collection
		 */
		public CollectionChangeListener(IMap<G, H> collection) {
			collection.addEntryListener(this, true);
			this.collection = collection;
			addedActions = new ArrayList<ItemAddedAction<G, H>>();
			removedActions = new ArrayList<ItemRemovedAction<G, H>>();
			updatedActions = new ArrayList<ItemUpdatedAction<G, H>>();
		}

		/*
		 * (non-Javadoc)
		 * @see
		 * com.hazelcast.core.EntryListener#entryAdded(com.hazelcast.core.EntryEvent
		 * )
		 */
		@Override
		public void entryAdded(final EntryEvent<G, H> e) {
			ClusterThreadManager.get().enqueue(new Callable<Void>() {
				@Override
				public Void call() throws Exception {
					for (ItemAddedAction<G, H> action : addedActions) {
						action.itemAddedToCollection(collection, e.getKey(),
								e.getValue(), e.getMember());
					}
					return null;
				}
			});
		}

		/*
		 * (non-Javadoc)
		 * @see
		 * com.hazelcast.core.EntryListener#entryEvicted(com.hazelcast.core.
		 * EntryEvent)
		 */
		@Override
		public void entryEvicted(EntryEvent<G, H> e) {
		}

		/*
		 * (non-Javadoc)
		 * @see
		 * com.hazelcast.core.EntryListener#entryRemoved(com.hazelcast.core.
		 * EntryEvent)
		 */
		@Override
		public void entryRemoved(final EntryEvent<G, H> e) {
			ClusterThreadManager.get().enqueue(new Callable<Void>() {
				@Override
				public Void call() throws Exception {
					for (ItemRemovedAction<G, H> action : removedActions) {
						action.itemRemovedFromCollection(collection,
								e.getKey(), e.getValue(), e.getMember());
					}
					return null;
				}
			});
		}

		/*
		 * (non-Javadoc)
		 * @see
		 * com.hazelcast.core.EntryListener#entryUpdated(com.hazelcast.core.
		 * EntryEvent)
		 */
		@Override
		public void entryUpdated(final EntryEvent<G, H> e) {
			ClusterThreadManager.get().enqueue(new Callable<Void>() {
				@Override
				public Void call() throws Exception {
					for (ItemUpdatedAction<G, H> action : updatedActions) {
						action.itemUpdatedInCollection(collection, e.getKey(),
								e.getOldValue(), e.getValue(), e.getMember());
					}
					return null;
				}
			});
		}

		/**
		 * Register item added action.
		 *
		 * @param action the action
		 */
		public void registerItemAddedAction(ItemAddedAction<G, H> action) {
			if (addedActions.contains(action)) {
				return;
			}
			addedActions.add(action);
		}

		/**
		 * Register item removed action.
		 *
		 * @param action the action
		 */
		public void registerItemRemovedAction(ItemRemovedAction<G, H> action) {
			if (removedActions.contains(action)) {
				return;
			}
			removedActions.add(action);
		}

		/**
		 * Register item updated action.
		 *
		 * @param action the action
		 */
		public void registerItemUpdatedAction(ItemUpdatedAction<G, H> action) {
			if (updatedActions.contains(action)) {
				return;
			}
			updatedActions.add(action);
		}

		/**
		 * Unregister item added action.
		 *
		 * @param action the action
		 */
		public void unregisterItemAddedAction(ItemAddedAction<K, V> action) {
			addedActions.remove(action);
		}

		/**
		 * Unregister item removed action.
		 *
		 * @param action the action
		 */
		public void unregisterItemRemovedAction(ItemRemovedAction<K, V> action) {
			removedActions.remove(action);
		}
	}

	/** The listener. */
	private CollectionChangeListener<K, V> listener;

	/** The map. */
	private IMap<K, V> map;

	/**
	 * Instantiates a new distributed map.
	 *
	 * @param map the map
	 */
	public DistributedMap(IMap<K, V> map) {
		this.map = map;
		listener = this.new CollectionChangeListener<K, V>(map);
	}

	/**
	 * Instantiates a new distributed map.
	 *
	 * @param distributedMapName the distributed map name
	 */
	@SuppressWarnings("unchecked")
	public DistributedMap(String distributedMapName) {
		this((IMap<K, V>) Hazelcast.getMap(distributedMapName));
	}

	/**
	 * Clear.
	 */
	public void clear() {
		map.clear();
	}

	/**
	 * Entry set.
	 *
	 * @return the sets the
	 */
	public Set<Entry<K, V>> entrySet() {
		return map.entrySet();
	}

	/**
	 * Gets the.
	 *
	 * @param key the key
	 * @return the v
	 */
	public V get(K key) {
		return map.get(key);
	}

	/**
	 * Key set.
	 *
	 * @return the sets the
	 */
	public Set<K> keySet() {
		return map.keySet();
	}

	/**
	 * Put.
	 *
	 * @param key the key
	 * @param value the value
	 * @return the v
	 */
	public V put(K key, V value) {
		return map.put(key, value);
	}

	/**
	 * Register item added action.
	 *
	 * @param action the action
	 */
	public void registerItemAddedAction(ItemAddedAction<K, V> action) {
		listener.registerItemAddedAction(action);
	}

	/**
	 * Register item removed action.
	 *
	 * @param action the action
	 */
	public void registerItemRemovedAction(ItemRemovedAction<K, V> action) {
		listener.registerItemRemovedAction(action);
	}

	/**
	 * Register item updated action.
	 *
	 * @param action the action
	 */
	public void registerItemUpdatedAction(ItemUpdatedAction<K, V> action) {
		listener.registerItemUpdatedAction(action);
	}

	/**
	 * Removes the.
	 *
	 * @param key the key
	 * @return the v
	 */
	public V remove(K key) {
		return map.remove(key);
	}

	/**
	 * Replace contents.
	 *
	 * @param contents the contents
	 */
	public void replaceContents(Map<K, V> contents) {
		map.clear();
		for (K key : contents.keySet()) {
			map.put(key, contents.get(key));
		}
	}

	/**
	 * Size.
	 *
	 * @return the int
	 */
	public int size() {
		return map.size();
	}

	/**
	 * Unregister item added action.
	 *
	 * @param action the action
	 */
	public void unregisterItemAddedAction(ItemAddedAction<K, V> action) {
		listener.unregisterItemAddedAction(action);
	}

	/**
	 * Unregister item removed action.
	 *
	 * @param action the action
	 */
	public void unregisterItemRemovedAction(ItemRemovedAction<K, V> action) {
		listener.unregisterItemRemovedAction(action);
	}

	/**
	 * Values.
	 *
	 * @return the collection
	 */
	public Collection<V> values() {
		return map.values();
	}

}
