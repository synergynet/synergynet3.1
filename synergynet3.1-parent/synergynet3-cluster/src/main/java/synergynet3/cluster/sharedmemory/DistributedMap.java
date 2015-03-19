package synergynet3.cluster.sharedmemory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
//import java.util.logging.Logger;
import java.util.Set;

import synergynet3.cluster.threading.ClusterThreadManager;

import com.hazelcast.core.EntryEvent;
import com.hazelcast.core.EntryListener;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.IMap;
import com.hazelcast.core.Member;

public class DistributedMap<K,V> {
	private IMap<K, V> map;
	private CollectionChangeListener<K, V> listener;
	
	@SuppressWarnings("unchecked")
	public DistributedMap(String distributedMapName) {
		this((IMap<K,V>)Hazelcast.getMap(distributedMapName));		
	}

	public DistributedMap(IMap<K,V> map) {
		this.map = map;
		listener = this.new CollectionChangeListener<K,V>(map);
	}
	
	public void registerItemAddedAction(ItemAddedAction<K,V> action) {
		listener.registerItemAddedAction(action);
	}
	
	public void unregisterItemAddedAction(ItemAddedAction<K,V> action) {
		listener.unregisterItemAddedAction(action);
	}

	
	public void registerItemRemovedAction(ItemRemovedAction<K,V> action) {
		listener.registerItemRemovedAction(action);
	}
	
	public void unregisterItemRemovedAction(ItemRemovedAction<K,V> action) {
		listener.unregisterItemRemovedAction(action);
	}
	
	public void registerItemUpdatedAction(ItemUpdatedAction<K,V> action) {
		listener.registerItemUpdatedAction(action);
	}
	
	public V put(K key, V value) {
		return map.put(key, value);
	}
	
	public V get(K key) {
		return map.get(key);
	}
	
	public Set<Entry<K, V>> entrySet() {
		return map.entrySet();
	}
	
	public Collection<V> values() {
		return map.values();
	}
	
	public void clear() {
		map.clear();
	}
	
	public Set<K> keySet() {
		return map.keySet();
	}

	public void replaceContents(Map<K, V> contents) {
		map.clear();
		for(K key : contents.keySet()) {
			map.put(key, contents.get(key));
		}
	}
	
	public V remove(K key) {
		return map.remove(key);
	}
	
	
	private class CollectionChangeListener<G,H> implements EntryListener<G,H> {
		private List<ItemAddedAction<G,H>> addedActions;
		private List<ItemRemovedAction<G,H>> removedActions;
		private List<ItemUpdatedAction<G,H>> updatedActions;
		private IMap<G, H> collection;

		public CollectionChangeListener(IMap<G,H> collection) {
			collection.addEntryListener(this, true);
			this.collection = collection;
			addedActions = new ArrayList<ItemAddedAction<G,H>>();
			removedActions = new ArrayList<ItemRemovedAction<G,H>>();
			updatedActions = new ArrayList<ItemUpdatedAction<G,H>>();
		}

		public void registerItemAddedAction(ItemAddedAction<G,H> action) {
			if(addedActions.contains(action)) return;
			addedActions.add(action);
		}
		
		public void unregisterItemAddedAction(ItemAddedAction<K, V> action) {
			addedActions.remove(action);
		}
		
		public void registerItemRemovedAction(ItemRemovedAction<G,H> action) {
			if(removedActions.contains(action)) return;
			removedActions.add(action);
		}
		
		public void unregisterItemRemovedAction(ItemRemovedAction<K, V> action) {
			removedActions.remove(action);
		}
		
		public void registerItemUpdatedAction(ItemUpdatedAction<G,H> action) {
			if(updatedActions.contains(action)) return;
			updatedActions.add(action);
		}

		@Override
		public void entryAdded(final EntryEvent<G,H> e) {
			ClusterThreadManager.get().enqueue(new Callable<Void>() {
				@Override
				public Void call() throws Exception {
					for(ItemAddedAction<G,H> action : addedActions) {
						action.itemAddedToCollection(collection, e.getKey(), e.getValue(), e.getMember());
					}
					return null;
				}
			});
		}

		@Override
		public void entryRemoved(final EntryEvent<G,H> e) {
			ClusterThreadManager.get().enqueue(new Callable<Void>() {
				@Override
				public Void call() throws Exception {
					for(ItemRemovedAction<G,H> action : removedActions) {
						action.itemRemovedFromCollection(collection, e.getKey(), e.getValue(), e.getMember());
					}
					return null;
				}
			});
		}
		
		@Override
		public void entryEvicted(EntryEvent<G,H> e) {}

		@Override
		public void entryUpdated(final EntryEvent<G,H> e) {
			ClusterThreadManager.get().enqueue(new Callable<Void>() {
				@Override
				public Void call() throws Exception {
					for(ItemUpdatedAction<G,H> action : updatedActions) {
						action.itemUpdatedInCollection(collection, e.getKey(), e.getOldValue(), e.getValue(), e.getMember());
					}
					return null;
				}
			});
		}
	}
	
	public static interface ItemAddedAction <K,V> {
		public void itemAddedToCollection(IMap<K,V> collection, K itemKey, V itemValue, Member member);
	}
	
	public static interface ItemRemovedAction <K,V> {
		public void itemRemovedFromCollection(IMap<K,V> collection, K itemKey, V itemValue, Member member);
	}
	
	public static interface ItemUpdatedAction <K,V> {
		public void itemUpdatedInCollection(IMap<K,V> collection, K itemKey, V itemOldValue, V itemNewValue, Member member);
	}

	public int size() {
		return map.size();
	}



}
