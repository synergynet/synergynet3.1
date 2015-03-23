package multiplicity3.csys.draganddrop;

import multiplicity3.csys.items.item.IItem;

/**
 * The listener interface for receiving dragAndDrop events. The class that is
 * interested in processing a dragAndDrop event implements this interface, and
 * the object created with that class is registered with a component using the
 * component's <code>addDragAndDropListener<code> method. When
 * the dragAndDrop event occurs, that object's appropriate
 * method is invoked.
 *
 * @see DragAndDropEvent
 */
public interface DragAndDropListener {
	/**
	 * @param itemDropped Item that was dropped onto drag destination.
	 * @param onto Drag destination dropped onto.
	 * @param indexOfDrop If the item was on top, this will be zero, otherwise,
	 *            how far underneath the top item.
	 */
	public void itemDraggedAndDropped(IItem itemDropped, IItem onto,
			int indexOfDrop);
}
