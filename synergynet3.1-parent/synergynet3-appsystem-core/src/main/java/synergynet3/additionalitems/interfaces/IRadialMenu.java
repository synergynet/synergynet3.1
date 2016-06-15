package synergynet3.additionalitems.interfaces;

import java.util.logging.Logger;

import multiplicity3.csys.items.item.IItem;
import multiplicity3.csys.stage.IStage;
import synergynet3.additionalitems.RadialMenuOption;

import com.jme3.math.ColorRGBA;

/**
 * The Interface IRadialMenu.
 */
public interface IRadialMenu extends IItem
{

	/**
	 * Adds the option.
	 *
	 * @param option
	 *            the option
	 * @return the int
	 */
	public int addOption(RadialMenuOption option);

	/**
	 * Removes the option.
	 *
	 * @param optionIndex
	 *            the option index
	 */
	public void removeOption(int optionIndex);

	/**
	 * Sets the option visibility.
	 *
	 * @param b
	 *            the new option visibility
	 */
	public void setOptionVisibility(boolean b);

	/**
	 * Sets the radius.
	 *
	 * @param i
	 *            the new radius
	 */
	public void setRadius(int i);

	/**
	 * Sets the root item.
	 *
	 * @param studentIcon
	 *            the student icon
	 * @param stage
	 *            the stage
	 * @param log
	 *            the log
	 * @param studentColour
	 *            the student colour
	 */
	public void setRootItem(IItem studentIcon, IStage stage, Logger log, ColorRGBA studentColour);

	/**
	 * Toggle option visibility.
	 */
	public void toggleOptionVisibility();

}
