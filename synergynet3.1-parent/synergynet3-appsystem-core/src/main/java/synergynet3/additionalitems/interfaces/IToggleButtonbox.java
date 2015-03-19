package synergynet3.additionalitems.interfaces;

import synergynet3.fonts.FontColour;

import com.jme3.math.ColorRGBA;

import multiplicity3.csys.items.border.IRoundedBorder;
import multiplicity3.csys.items.image.IImage;
import multiplicity3.csys.items.item.IItem;
import multiplicity3.csys.items.mutablelabel.IMutableLabel;
import multiplicity3.csys.stage.IStage;

public interface IToggleButtonbox extends IItem {
	
	public void setText(String textOff, ColorRGBA bgColourOff, ColorRGBA borderColourOff, FontColour fontColourOff,
			String textOn, ColorRGBA bgColourOn, ColorRGBA borderColourOn, FontColour fontColourOn,
			float width, float height, IStage stage);
	public void setImage(IImage imageOff, ColorRGBA bgColourOff, ColorRGBA borderColourOff,
			IImage imageOn, ColorRGBA bgColourOn, ColorRGBA borderColourOn,
			float width, float height, IStage stage);
	public void toggle();
	public IImage getListener();
	public boolean getToggleStatus();
	public IMutableLabel getTextLabelOn();
	public IRoundedBorder getTextBorderOn();	
	public IImage getImageOn();	
	public IMutableLabel getTextLabelOff();
	public IRoundedBorder getTextBorderOff();	
	public IImage getImageOff();
	
}
