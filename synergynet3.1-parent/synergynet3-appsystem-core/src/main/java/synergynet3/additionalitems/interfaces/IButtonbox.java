package synergynet3.additionalitems.interfaces;

import synergynet3.fonts.FontColour;

import com.jme3.math.ColorRGBA;

import multiplicity3.csys.items.border.IRoundedBorder;
import multiplicity3.csys.items.image.IImage;
import multiplicity3.csys.items.item.IItem;
import multiplicity3.csys.items.mutablelabel.IMutableLabel;
import multiplicity3.csys.stage.IStage;

public interface IButtonbox extends IItem {
	
	public void setText(String text, ColorRGBA bgColour, ColorRGBA borderColour, FontColour fontColour, float width, float height, IStage stage);
	public void setImage(IImage image, ColorRGBA bgColour, ColorRGBA borderColour, float width, float height, IStage stage);
	public IImage getListener();
	public String getText();
	public IMutableLabel getTextLabel();
	public IRoundedBorder getTextBorder();
	public float getWidth();
	public float getHeight();
	
}
