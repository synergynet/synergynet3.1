package synergynet3.additionalitems.interfaces;

import com.jme3.math.ColorRGBA;

import multiplicity3.csys.items.image.IImage;
import multiplicity3.csys.stage.IStage;

public interface ICachableImage extends IImage {
	
	public void generateBorder(IStage stage, ColorRGBA borderColour, float borderWidth);
	public void removeBorder();
	
}
