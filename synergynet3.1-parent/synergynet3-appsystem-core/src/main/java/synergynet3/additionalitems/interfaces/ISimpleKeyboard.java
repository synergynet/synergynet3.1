package synergynet3.additionalitems.interfaces;

import com.jme3.math.ColorRGBA;

import synergynet3.fonts.FontColour;
import synergynet3.keyboard.KeyboardOutput;
import multiplicity3.csys.items.image.IImage;
import multiplicity3.csys.items.item.IItem;
import multiplicity3.csys.stage.IStage;

public interface ISimpleKeyboard extends IItem {
	public void setColours(ColorRGBA bgColour, ColorRGBA keyColour, ColorRGBA keyBorderColour, ColorRGBA boardBorderColour, FontColour fontColour);
	public void setMovable(boolean movable);
	public void generateKeys(IStage stage, KeyboardOutput keyboardOutput);
	public void setScaleLimits(float minScale, float maxScale);
	public float getWidth();	
	public float getHeight();
	public IImage getListener();
}
