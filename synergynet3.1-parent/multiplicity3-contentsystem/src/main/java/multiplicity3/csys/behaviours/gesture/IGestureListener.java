package multiplicity3.csys.behaviours.gesture;

import multiplicity3.csys.items.item.IItem;

/**
 * The listener interface for receiving IGesture events. The class that is
 * interested in processing a IGesture event implements this interface, and the
 * object created with that class is registered with a component using the
 * component's <code>addIGestureListener<code> method. When
 * the IGesture event occurs, that object's appropriate
 * method is invoked.
 *
 * @see IGestureEvent
 */
public interface IGestureListener
{

	/**
	 * Gesture detected.
	 *
	 * @param match
	 *            the match
	 * @param item
	 *            the item
	 */
	public void gestureDetected(GestureMatch match, IItem item);
}
