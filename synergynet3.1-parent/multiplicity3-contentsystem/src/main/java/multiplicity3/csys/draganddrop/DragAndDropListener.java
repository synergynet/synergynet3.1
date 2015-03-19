package multiplicity3.csys.draganddrop;

import multiplicity3.csys.items.item.IItem;

public interface DragAndDropListener {
	/**
	 * 
	 * @param itemDropped Item that was dropped onto drag destination.
	 * @param onto Drag destination dropped onto.
	 * @param indexOfDrop If the item was on top, this will be zero, otherwise, how far underneath the top item.
	 */
	public void itemDraggedAndDropped(IItem itemDropped, IItem onto, int indexOfDrop);
}
