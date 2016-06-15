package multiplicity3.csys.items.events;

import multiplicity3.csys.items.item.IItem;
import multiplicity3.input.events.MultiTouchCursorEvent;

/**
 * The listener interface for receiving IItem events. The class that is
 * interested in processing a IItem event implements this interface, and the
 * object created with that class is registered with a component using the
 * component's <code>addIItemListener<code> method. When
 * the IItem event occurs, that object's appropriate
 * method is invoked.
 *
 * @see IItemEvent
 */
public interface IItemListener
{

	/**
	 * Item cursor changed.
	 *
	 * @param item
	 *            the item
	 * @param event
	 *            the event
	 */
	public void itemCursorChanged(IItem item, MultiTouchCursorEvent event);

	/**
	 * Item cursor clicked.
	 *
	 * @param item
	 *            the item
	 * @param event
	 *            the event
	 */
	public void itemCursorClicked(IItem item, MultiTouchCursorEvent event);

	/**
	 * Item cursor pressed.
	 *
	 * @param item
	 *            the item
	 * @param event
	 *            the event
	 */
	public void itemCursorPressed(IItem item, MultiTouchCursorEvent event);

	/**
	 * Item cursor released.
	 *
	 * @param item
	 *            the item
	 * @param event
	 *            the event
	 */
	public void itemCursorReleased(IItem item, MultiTouchCursorEvent event);

	/**
	 * Item moved.
	 *
	 * @param item
	 *            the item
	 */
	public void itemMoved(IItem item);

	/**
	 * Item rotated.
	 *
	 * @param item
	 *            the item
	 */
	public void itemRotated(IItem item);

	/**
	 * Item scaled.
	 *
	 * @param item
	 *            the item
	 */
	public void itemScaled(IItem item);

	/**
	 * Item visibility changed.
	 *
	 * @param item
	 *            the item
	 * @param isVisible
	 *            the is visible
	 */
	public void itemVisibilityChanged(IItem item, boolean isVisible);

	/**
	 * Item z order changed.
	 *
	 * @param item
	 *            the item
	 */
	public void itemZOrderChanged(IItem item);
}
