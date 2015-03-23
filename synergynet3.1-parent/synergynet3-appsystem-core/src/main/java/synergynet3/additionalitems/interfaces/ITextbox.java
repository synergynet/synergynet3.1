package synergynet3.additionalitems.interfaces;

import multiplicity3.csys.items.border.IRoundedBorder;
import multiplicity3.csys.items.item.IItem;
import multiplicity3.csys.items.mutablelabel.IMutableLabel;
import multiplicity3.csys.items.shapes.IColourRectangle;
import multiplicity3.csys.stage.IStage;
import synergynet3.fonts.FontColour;

import com.jme3.math.ColorRGBA;

/**
 * The Interface ITextbox.
 */
public interface ITextbox extends IItem {

	/**
	 * Gets the background.
	 *
	 * @return the background
	 */
	public IColourRectangle getBackground();

	/**
	 * Gets the height.
	 *
	 * @return the height
	 */
	public float getHeight();

	/**
	 * Gets the listen block.
	 *
	 * @return the listen block
	 */
	public IItem getListenBlock();

	/**
	 * Gets the text border.
	 *
	 * @return the text border
	 */
	public IRoundedBorder getTextBorder();

	/**
	 * Gets the text label.
	 *
	 * @return the text label
	 */
	public IMutableLabel getTextLabel();

	/**
	 * Gets the width.
	 *
	 * @return the width
	 */
	public float getWidth();

	/**
	 * Sets the colours.
	 *
	 * @param bgColour the bg colour
	 * @param borderColour the border colour
	 * @param fontColour the font colour
	 */
	public void setColours(ColorRGBA bgColour, ColorRGBA borderColour,
			FontColour fontColour);

	/**
	 * Sets the height.
	 *
	 * @param height the new height
	 */
	public void setHeight(float height);

	/**
	 * Sets the movable.
	 *
	 * @param movable the new movable
	 */
	public void setMovable(boolean movable);

	/**
	 * Sets the scale limits.
	 *
	 * @param scaleMin the scale min
	 * @param scaleMax the scale max
	 */
	public void setScaleLimits(float scaleMin, float scaleMax);

	/**
	 * Sets the text.
	 *
	 * @param text the text
	 * @param stage the stage
	 */
	public void setText(String text, IStage stage);

	/**
	 * Sets the width.
	 *
	 * @param width the new width
	 */
	public void setWidth(float width);

}
