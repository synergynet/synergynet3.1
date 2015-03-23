package synergynet3.additionalitems.interfaces;

import multiplicity3.csys.items.border.IRoundedBorder;
import multiplicity3.csys.items.image.IImage;
import multiplicity3.csys.items.item.IItem;
import multiplicity3.csys.items.mutablelabel.IMutableLabel;
import multiplicity3.csys.stage.IStage;
import synergynet3.fonts.FontColour;

import com.jme3.math.ColorRGBA;

/**
 * The Interface IButtonbox.
 */
public interface IButtonbox extends IItem {

	/**
	 * Gets the height.
	 *
	 * @return the height
	 */
	public float getHeight();

	/**
	 * Gets the listener.
	 *
	 * @return the listener
	 */
	public IImage getListener();

	/**
	 * Gets the text.
	 *
	 * @return the text
	 */
	public String getText();

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
	 * Sets the image.
	 *
	 * @param image the image
	 * @param bgColour the bg colour
	 * @param borderColour the border colour
	 * @param width the width
	 * @param height the height
	 * @param stage the stage
	 */
	public void setImage(IImage image, ColorRGBA bgColour,
			ColorRGBA borderColour, float width, float height, IStage stage);

	/**
	 * Sets the text.
	 *
	 * @param text the text
	 * @param bgColour the bg colour
	 * @param borderColour the border colour
	 * @param fontColour the font colour
	 * @param width the width
	 * @param height the height
	 * @param stage the stage
	 */
	public void setText(String text, ColorRGBA bgColour,
			ColorRGBA borderColour, FontColour fontColour, float width,
			float height, IStage stage);

}
