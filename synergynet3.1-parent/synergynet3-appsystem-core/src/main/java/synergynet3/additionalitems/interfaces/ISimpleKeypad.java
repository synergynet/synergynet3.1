package synergynet3.additionalitems.interfaces;

import multiplicity3.csys.items.item.IItem;
import multiplicity3.csys.stage.IStage;
import synergynet3.fonts.FontColour;
import synergynet3.keyboard.KeyboardOutput;

import com.jme3.math.ColorRGBA;

/**
 * The Interface ISimpleKeypad.
 */
public interface ISimpleKeypad extends IItem
{

	/**
	 * Generate keys.
	 *
	 * @param stage
	 *            the stage
	 * @param keyboardOutput
	 *            the keyboard output
	 */
	public void generateKeys(IStage stage, KeyboardOutput keyboardOutput);

	/**
	 * Gets the height.
	 *
	 * @return the height
	 */
	public float getHeight();

	/**
	 * Gets the width.
	 *
	 * @return the width
	 */
	public float getWidth();

	/**
	 * Sets the button size and spacing.
	 *
	 * @param buttonSize
	 *            the button size
	 * @param buttonSpacing
	 *            the button spacing
	 */
	public void setButtonSizeAndSpacing(float buttonSize, float buttonSpacing);

	/**
	 * Sets the colours.
	 *
	 * @param bgColour
	 *            the bg colour
	 * @param keyColour
	 *            the key colour
	 * @param keyBorderColour
	 *            the key border colour
	 * @param boardBorderColour
	 *            the board border colour
	 * @param fontColour
	 *            the font colour
	 */
	public void setColours(ColorRGBA bgColour, ColorRGBA keyColour, ColorRGBA keyBorderColour, ColorRGBA boardBorderColour, FontColour fontColour);

	/**
	 * Sets the movable.
	 *
	 * @param movable
	 *            the new movable
	 */
	public void setMovable(boolean movable);

	/**
	 * Sets the scale limits.
	 *
	 * @param minScale
	 *            the min scale
	 * @param maxScale
	 *            the max scale
	 */
	public void setScaleLimits(float minScale, float maxScale);
}
