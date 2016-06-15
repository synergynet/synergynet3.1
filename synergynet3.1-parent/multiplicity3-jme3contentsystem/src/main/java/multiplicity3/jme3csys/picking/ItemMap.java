package multiplicity3.jme3csys.picking;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import multiplicity3.csys.items.item.IItem;
import multiplicity3.jme3csys.items.item.JMEItem;

import com.jme3.scene.Spatial;

/**
 * The Class ItemMap.
 */
public class ItemMap
{

	/** The item map. */
	private static Map<UUID, List<IItem>> itemMap;

	static
	{
		itemMap = new HashMap<UUID, List<IItem>>();
	}

	/**
	 * Gets the item.
	 *
	 * @param uuid
	 *            the uuid
	 * @return the item
	 */
	public static List<IItem> getItem(UUID uuid)
	{
		return itemMap.get(uuid);
	}

	/**
	 * Associates a spatial item to a content item. This is used by the
	 * <code>PickedItemDispatcher</code> to find out where to dispatch events to
	 * when a spatial is pressed on.
	 *
	 * @param spatial
	 * @param item
	 */
	public static void register(Spatial spatial, IItem item)
	{
		spatial.setUserData(JMEItem.KEY_JMEITEMDATA, item.getUUID().toString());
		List<IItem> list = itemMap.get(item.getUUID());
		if (list == null)
		{
			list = new ArrayList<IItem>();
		}
		list.add(item);
		itemMap.put(item.getUUID(), list);
	}

	/**
	 * Unregister.
	 *
	 * @param spatial
	 *            the spatial
	 * @param item
	 *            the item
	 */
	public static void unregister(Spatial spatial, IItem item)
	{
		itemMap.remove(item.getUUID());
	}

	/**
	 * View.
	 *
	 * @return the string
	 */
	public static String view()
	{
		String s = "ItemMap";
		for (UUID id : itemMap.keySet())
		{
			List<IItem> list = itemMap.get(id);
			s += "\n  " + id + " -> " + list;
		}
		return s;
	}

}
