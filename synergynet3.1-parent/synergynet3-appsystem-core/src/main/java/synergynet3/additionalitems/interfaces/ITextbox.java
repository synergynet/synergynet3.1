package synergynet3.additionalitems.interfaces;

import synergynet3.fonts.FontColour;

import com.jme3.math.ColorRGBA;

import multiplicity3.csys.items.border.IRoundedBorder;
import multiplicity3.csys.items.item.IItem;
import multiplicity3.csys.items.mutablelabel.IMutableLabel;
import multiplicity3.csys.items.shapes.IColourRectangle;
import multiplicity3.csys.stage.IStage;

public interface ITextbox extends IItem {
	public void setText(String text,IStage stage);
	public IItem getListenBlock();
	public void setWidth(float width);
	public float getWidth();
	public void setHeight(float height);	
	public float getHeight();	
	public IColourRectangle getBackground();
	public IMutableLabel getTextLabel();
	public IRoundedBorder getTextBorder();
	public void setScaleLimits(float scaleMin, float scaleMax);
	public void setMovable(boolean movable);
	public void setColours(ColorRGBA bgColour, ColorRGBA borderColour, FontColour fontColour);
	
}
