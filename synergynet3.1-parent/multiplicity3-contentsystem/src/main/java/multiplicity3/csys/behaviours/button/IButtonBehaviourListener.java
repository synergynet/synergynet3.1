package multiplicity3.csys.behaviours.button;

import multiplicity3.csys.items.item.IItem;

/**
 * The listener interface for receiving IButtonBehaviour events. The class that
 * is interested in processing a IButtonBehaviour event implements this
 * interface, and the object created with that class is registered with a
 * component using the component's
 * <code>addIButtonBehaviourListener<code> method. When
 * the IButtonBehaviour event occurs, that object's appropriate
 * method is invoked.
 *
 * @see IButtonBehaviourEvent
 */
public interface IButtonBehaviourListener {

	/**
	 * Button clicked.
	 *
	 * @param item the item
	 */
	public void buttonClicked(IItem item);

	/**
	 * Button pressed.
	 *
	 * @param item the item
	 */
	public void buttonPressed(IItem item);

	/**
	 * Button released.
	 *
	 * @param item the item
	 */
	public void buttonReleased(IItem item);
}
