package multiplicity3.csys.items.keyboard.behaviour;

import multiplicity3.csys.items.keyboard.model.KeyboardKey;

/**
 * The listener interface for receiving IMultiTouchKeyboard events. The class
 * that is interested in processing a IMultiTouchKeyboard event implements this
 * interface, and the object created with that class is registered with a
 * component using the component's
 * <code>addIMultiTouchKeyboardListener<code> method. When
 * the IMultiTouchKeyboard event occurs, that object's appropriate
 * method is invoked.
 *
 * @see IMultiTouchKeyboardEvent
 */
public interface IMultiTouchKeyboardListener
{

	/**
	 * Key pressed.
	 *
	 * @param k
	 *            the k
	 * @param shiftDown
	 *            the shift down
	 * @param altDown
	 *            the alt down
	 * @param ctlDown
	 *            the ctl down
	 */
	public void keyPressed(KeyboardKey k, boolean shiftDown, boolean altDown, boolean ctlDown);

	/**
	 * Key released.
	 *
	 * @param k
	 *            the k
	 * @param shiftDown
	 *            the shift down
	 * @param altDown
	 *            the alt down
	 * @param ctlDown
	 *            the ctl down
	 */
	public void keyReleased(KeyboardKey k, boolean shiftDown, boolean altDown, boolean ctlDown);
}
