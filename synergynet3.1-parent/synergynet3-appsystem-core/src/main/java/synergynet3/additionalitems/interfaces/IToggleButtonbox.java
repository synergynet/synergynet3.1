package synergynet3.additionalitems.interfaces;

import multiplicity3.csys.items.border.IRoundedBorder;
import multiplicity3.csys.items.image.IImage;
import multiplicity3.csys.items.item.IItem;
import multiplicity3.csys.items.mutablelabel.IMutableLabel;
import multiplicity3.csys.stage.IStage;
import synergynet3.fonts.FontColour;

import com.jme3.math.ColorRGBA;

/**
 * The Interface IToggleButtonbox.
 */
public interface IToggleButtonbox extends IItem {

	/**
	 * Gets the image off.
	 *
	 * @return the image off
	 */
	public IImage getImageOff();

	/**
	 * Gets the image on.
	 *
	 * @return the image on
	 */
	public IImage getImageOn();

	/**
	 * Gets the listener.
	 *
	 * @return the listener
	 */
	public IImage getListener();

	/**
	 * Gets the text border off.
	 *
	 * @return the text border off
	 */
	public IRoundedBorder getTextBorderOff();

	/**
	 * Gets the text border on.
	 *
	 * @return the text border on
	 */
	public IRoundedBorder getTextBorderOn();

	/**
	 * Gets the text label off.
	 *
	 * @return the text label off
	 */
	public IMutableLabel getTextLabelOff();

	/**
	 * Gets the text label on.
	 *
	 * @return the text label on
	 */
	public IMutableLabel getTextLabelOn();

	/**
	 * Gets the toggle status.
	 *
	 * @return the toggle status
	 */
	public boolean getToggleStatus();

	/**
	 * Sets the image.
	 *
	 * @param imageOff the image off
	 * @param bgColourOff the bg colour off
	 * @param borderColourOff the border colour off
	 * @param imageOn the image on
	 * @param bgColourOn the bg colour on
	 * @param borderColourOn the border colour on
	 * @param width the width
	 * @param height the height
	 * @param stage the stage
	 */
	public void setImage(IImage imageOff, ColorRGBA bgColourOff,
			ColorRGBA borderColourOff, IImage imageOn, ColorRGBA bgColourOn,
			ColorRGBA borderColourOn, float width, float height, IStage stage);

	/**
	 * Sets the text.
	 *
	 * @param textOff the text off
	 * @param bgColourOff the bg colour off
	 * @param borderColourOff the border colour off
	 * @param fontColourOff the font colour off
	 * @param textOn the text on
	 * @param bgColourOn the bg colour on
	 * @param borderColourOn the border colour on
	 * @param fontColourOn the font colour on
	 * @param width the width
	 * @param height the height
	 * @param stage the stage
	 */
	public void setText(String textOff, ColorRGBA bgColourOff,
			ColorRGBA borderColourOff, FontColour fontColourOff, String textOn,
			ColorRGBA bgColourOn, ColorRGBA borderColourOn,
			FontColour fontColourOn, float width, float height, IStage stage);

	/**
	 * Toggle.
	 */
	public void toggle();

}
