package synergynet3.additionalitems.interfaces;

import java.util.logging.Logger;

import synergynet3.additionalitems.RadialMenuOption;

import com.jme3.math.ColorRGBA;

import multiplicity3.csys.items.item.IItem;
import multiplicity3.csys.stage.IStage;

public interface IRadialMenu extends IItem {

	public void setRootItem(IItem studentIcon, IStage stage, Logger log, ColorRGBA studentColour);
	public void setRadius(int i);
	public void toggleOptionVisibility();
	public void setOptionVisibility(boolean b);
	public void removeOption(int optionIndex);
	public int addOption(RadialMenuOption option);

}
