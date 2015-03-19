package synergynet3.cluster.sharedmemory;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

import synergynet3.cluster.threading.ClusterThreadManager;


import com.hazelcast.core.EntryEvent;
import com.hazelcast.core.EntryListener;
import com.hazelcast.core.IMap;

public class DistributedPropertyChangedListener implements EntryListener<String,Object> {
	private static final Logger log = Logger.getLogger(DistributedPropertyChangedListener.class.getName());
	
	private Map<String, List<DistributedPropertyChangedAction<?>>> propertyChangedActions = new HashMap<String,List<DistributedPropertyChangedAction<?>>>();
	protected IMap<String,Object> map;

	public DistributedPropertyChangedListener(IMap<String,Object> map) {
		this.map = map;
		this.map.addEntryListener(this, true);
	}

	public void registerPropertyChangedAction(String key, DistributedPropertyChangedAction<?> pca) {
		List<DistributedPropertyChangedAction<?>> actions = propertyChangedActions.get(key);
		if(actions == null) {
			actions = new ArrayList<DistributedPropertyChangedAction<?>>();
			propertyChangedActions.put(key, actions);
		}

		if(actions.contains(pca)) return; // we don't want duplicates

		actions.add(pca);
	}

	public void unregisterPropertyChangeAction(DistributedPropertyChangedAction<?> pca) {
		String key = getKeyForPropertyChangeAction(pca);
		if(key != null) {
			propertyChangedActions.get(key).remove(pca);
		}
	}

	private String getKeyForPropertyChangeAction(DistributedPropertyChangedAction<?> pca) {
		for(String key : propertyChangedActions.keySet()) {
			List<DistributedPropertyChangedAction<?>> list = propertyChangedActions.get(key);
			for(DistributedPropertyChangedAction<?> p : list) {
				if(p == pca) {
					return key;
				}
			}
		}
		return null;
	}

	@SuppressWarnings({"rawtypes" })
	@Override
	public void entryUpdated(final EntryEvent<String, Object> ee) {
		log.info("entry updated for " + ee.getKey());
		final List<DistributedPropertyChangedAction<?>> actions = propertyChangedActions.get(ee.getKey());
		if(actions == null) return;
		ClusterThreadManager.get().enqueue(new Callable<Void>() {
			@SuppressWarnings({ "unchecked" })
			@Override
			public Void call() throws Exception {
				try{
					for(DistributedPropertyChangedAction pca : actions) {
						pca.distributedPropertyDidChange(ee.getMember(), ee.getOldValue(), ee.getValue());
					}
				}catch(ConcurrentModificationException e){}
				return null;
			}
		});
	}











	// *********** 
	@Override
	public void entryAdded(final EntryEvent<String, Object> ee) {
		log.info("Entry added for " + ee.getKey());
		final List<DistributedPropertyChangedAction<?>> actions = propertyChangedActions.get(ee.getKey());
		if(actions == null) return;
		ClusterThreadManager.get().enqueue(new Callable<Void>() {
			@SuppressWarnings({ "unchecked" })
			@Override
			public Void call() throws Exception {
				for(@SuppressWarnings("rawtypes") DistributedPropertyChangedAction pca : actions) {
					pca.distributedPropertyDidChange(ee.getMember(), null, ee.getValue());
				}
				return null;
			}
		});
	}

	@Override
	public void entryEvicted(EntryEvent<String, Object> ee) {
		log.info("Entry evicted for " + ee.getKey());
	}

	@Override
	public void entryRemoved(EntryEvent<String, Object> ee) {
		log.info("Entry removed for " + ee.getKey());
	}

}
