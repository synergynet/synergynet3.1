package multiplicity3.csys.items.keyboard;

import multiplicity3.csys.items.keyboard.model.KeyboardDefinition;
import multiplicity3.csys.items.shapes.IRectangularItem;

/**
 * The Interface IKeyboard.
 */
public interface IKeyboard extends IRectangularItem {

	/**
	 * Gets the keyboard definition.
	 *
	 * @return the keyboard definition
	 */
	public KeyboardDefinition getKeyboardDefinition();

	/**
	 * Re draw.
	 */
	public void reDraw();

	/**
	 * Re draw keyboard.
	 *
	 * @param shiftDown the shift down
	 * @param altDown the alt down
	 * @param ctlDown the ctl down
	 */
	public void reDrawKeyboard(boolean shiftDown, boolean altDown,
			boolean ctlDown);

	/**
	 * Sets the keyboard definition.
	 *
	 * @param kd the new keyboard definition
	 */
	public void setKeyboardDefinition(KeyboardDefinition kd);

	/**
	 * Sets the keyboard renderer.
	 *
	 * @param simpleAlphaKeyboardRenderer the new keyboard renderer
	 */
	public void setKeyboardRenderer(
			IKeyboardGraphicsRenderer simpleAlphaKeyboardRenderer);
}
