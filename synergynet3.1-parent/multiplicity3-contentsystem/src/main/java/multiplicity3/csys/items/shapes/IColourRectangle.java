package multiplicity3.csys.items.shapes;

import multiplicity3.csys.gfx.Gradient;

import com.jme3.math.ColorRGBA;

/**
 * The Interface IColourRectangle.
 */
public interface IColourRectangle extends IRectangularItem {

	/**
	 * Enable transparency.
	 */
	public void enableTransparency();

	/**
	 * Sets the gradient background.
	 *
	 * @param g the new gradient background
	 */
	public void setGradientBackground(Gradient g);

	/**
	 * Sets the solid background colour.
	 *
	 * @param colorRGBA the new solid background colour
	 */
	public void setSolidBackgroundColour(ColorRGBA colorRGBA);
}
