package multiplicity3.csys.items.keyboard;

import java.awt.Graphics2D;

import multiplicity3.csys.items.keyboard.behaviour.IMultiTouchKeyboardListener;

/**
 * The Interface IKeyboardGraphicsRenderer.
 */
public interface IKeyboardGraphicsRenderer extends IMultiTouchKeyboardListener
{

	/**
	 * Draw keyboard.
	 *
	 * @param g2d
	 *            the g2d
	 * @param shiftDown
	 *            the shift down
	 * @param altDown
	 *            the alt down
	 * @param ctlDown
	 *            the ctl down
	 */
	public void drawKeyboard(Graphics2D g2d, boolean shiftDown, boolean altDown, boolean ctlDown);
}
