package multiplicity3.csys.zorder;

import multiplicity3.csys.items.events.IItemListener;
import multiplicity3.csys.items.item.IItem;

/**
 * The Interface IZOrderManager.
 */
public interface IZOrderManager extends IItemListener
{

	/**
	 * Bring to top.
	 *
	 * @param item
	 *            the item
	 */
	void bringToTop(IItem item);

	/**
	 * Gets the item z order.
	 *
	 * @return the item z order
	 */
	int getItemZOrder();

	/**
	 * Gets the z capacity.
	 *
	 * @return the z capacity
	 */
	int getZCapacity();

	/**
	 * Ignore item clicked behaviour.
	 *
	 * @param item
	 *            the item
	 */
	void ignoreItemClickedBehaviour(IItem item);

	/**
	 * Notify child z capacity changed.
	 *
	 * @param itemBeingManaged
	 *            the item being managed
	 * @param defaultZOrderManager
	 *            the default z order manager
	 */
	void notifyChildZCapacityChanged(IItem itemBeingManaged, IZOrderManager defaultZOrderManager);

	/**
	 * Register for z ordering.
	 *
	 * @param item
	 *            the item
	 */
	void registerForZOrdering(IItem item);

	/**
	 * Send to bottom.
	 *
	 * @param item
	 *            the item
	 */
	void sendToBottom(IItem item);

	/**
	 * Sets the auto bring to top.
	 *
	 * @param enabled
	 *            the new auto bring to top
	 */
	void setAutoBringToTop(boolean enabled);

	/**
	 * Sets the bring to top propagates up.
	 *
	 * @param should
	 *            the new bring to top propagates up
	 */
	void setBringToTopPropagatesUp(boolean should);

	/**
	 * Sets the item z order.
	 *
	 * @param zValue
	 *            the new item z order
	 */
	void setItemZOrder(int zValue);

	/**
	 * Sets the z capacity.
	 *
	 * @param c
	 *            the new z capacity
	 */
	void setZCapacity(int c);

	/**
	 * Unregister for z ordering.
	 *
	 * @param i
	 *            the i
	 */
	void unregisterForZOrdering(IItem i);

	/**
	 * Update order.
	 */
	void updateOrder();
}
