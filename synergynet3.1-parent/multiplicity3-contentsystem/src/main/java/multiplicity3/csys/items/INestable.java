package multiplicity3.csys.items;

import java.util.List;

import multiplicity3.csys.items.item.IItem;
import multiplicity3.csys.zorder.IZOrderManager;

/**
 * The Interface INestable.
 */
public interface INestable {
	/**
	 * The listener interface for receiving IChildrenChanged events. The class
	 * that is interested in processing a IChildrenChanged event implements this
	 * interface, and the object created with that class is registered with a
	 * component using the component's
	 * <code>addIChildrenChangedListener<code> method. When
	 * the IChildrenChanged event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see IChildrenChangedEvent
	 */
	public static interface IChildrenChangedListener {

		/**
		 * Children changed.
		 *
		 * @param node the node
		 * @param list the list
		 */
		public void childrenChanged(INestable node, List<IItem> list);
	}

	// TODO: change to return true/false
	/**
	 * Adds the item.
	 *
	 * @param item the item
	 */
	public void addItem(IItem item);

	// TODO: change to return true/false
	/**
	 * De register children changed listener.
	 *
	 * @param listener the listener
	 */
	public void deRegisterChildrenChangedListener(
			IChildrenChangedListener listener);

	/**
	 * Gets the child items.
	 *
	 * @return the child items
	 */
	public List<IItem> getChildItems();

	/**
	 * Gets the children count.
	 *
	 * @return the children count
	 */
	public int getChildrenCount();

	/**
	 * Gets the z order manager.
	 *
	 * @return the z order manager
	 */
	public IZOrderManager getZOrderManager();

	/**
	 * Checks for children.
	 *
	 * @return true, if successful
	 */
	public boolean hasChildren();

	// TODO: change to return true/false
	/**
	 * Register children changed listener.
	 *
	 * @param listener the listener
	 */
	public void registerChildrenChangedListener(
			IChildrenChangedListener listener);

	/**
	 * Removes the all items.
	 *
	 * @param recursive the recursive
	 */
	public void removeAllItems(boolean recursive);

	// TODO: change to return true/false
	/**
	 * Removes the item.
	 *
	 * @param item the item
	 */
	public void removeItem(IItem item);

}
