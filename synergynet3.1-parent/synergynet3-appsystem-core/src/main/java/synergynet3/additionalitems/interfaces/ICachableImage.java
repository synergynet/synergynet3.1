package synergynet3.additionalitems.interfaces;

import multiplicity3.csys.items.image.IImage;
import multiplicity3.csys.stage.IStage;

import com.jme3.math.ColorRGBA;

/**
 * The Interface ICachableImage.
 */
public interface ICachableImage extends IImage
{

	/**
	 * Generate border.
	 *
	 * @param stage
	 *            the stage
	 * @param borderColour
	 *            the border colour
	 * @param borderWidth
	 *            the border width
	 */
	public void generateBorder(IStage stage, ColorRGBA borderColour, float borderWidth);

	/**
	 * Removes the border.
	 */
	public void removeBorder();

}
