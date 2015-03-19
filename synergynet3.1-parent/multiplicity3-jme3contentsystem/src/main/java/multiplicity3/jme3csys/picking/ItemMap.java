package multiplicity3.jme3csys.picking;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.jme3.scene.Spatial;

import multiplicity3.csys.items.item.IItem;
import multiplicity3.jme3csys.items.item.JMEItem;
public class ItemMap {
	private static Map<UUID, List<IItem>> itemMap;

	public static List<IItem> getItem(UUID uuid) {
		return itemMap.get(uuid);
	}
	
	static {
		itemMap = new HashMap<UUID, List<IItem>>();
	}
	
	/**
	 * Associates a spatial item to a content item. This is used
	 * by the <code>PickedItemDispatcher</code> to find out where
	 * to dispatch events to when a spatial is pressed on.
	 * @param spatial
	 * @param item
	 */
	public static void register(Spatial spatial, IItem item) {
		spatial.setUserData(JMEItem.KEY_JMEITEMDATA, item.getUUID().toString());
		List<IItem> list = itemMap.get(item.getUUID());
		if(list == null) list = new ArrayList<IItem>();
		list.add(item);
		itemMap.put(item.getUUID(), list);
	}
	
	public static void unregister(Spatial spatial, IItem item) {
		itemMap.remove(item.getUUID());		
	}
	
	public static String view() {
		String s = "ItemMap";
		for(UUID id : itemMap.keySet()) {
			List<IItem> list = itemMap.get(id);
			s += "\n  " + id + " -> " + list;	
		}
		return s;
	}

}
