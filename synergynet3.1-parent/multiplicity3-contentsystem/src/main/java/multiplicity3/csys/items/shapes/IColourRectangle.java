package multiplicity3.csys.items.shapes;

import com.jme3.math.ColorRGBA;

import multiplicity3.csys.gfx.Gradient;

public interface IColourRectangle extends IRectangularItem {
	public void setGradientBackground(Gradient g);
	public void setSolidBackgroundColour(ColorRGBA colorRGBA);
	public void enableTransparency();
}
