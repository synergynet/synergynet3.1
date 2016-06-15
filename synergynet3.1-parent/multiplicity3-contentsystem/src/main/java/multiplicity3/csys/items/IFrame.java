package multiplicity3.csys.items;

import java.awt.Color;

import multiplicity3.csys.gfx.Gradient;
import multiplicity3.csys.items.border.IRoundedBorder;
import multiplicity3.csys.items.events.IItemListener;
import multiplicity3.csys.items.item.IItem;

import com.jme3.math.Vector2f;

/**
 * The Interface IFrame.
 */
public interface IFrame extends IItem
{

	/**
	 * Gets the border.
	 *
	 * @return the border
	 */
	public IRoundedBorder getBorder();

	/**
	 * Gets the size.
	 *
	 * @return the size
	 */
	public Vector2f getSize();

	/**
	 * Checks for border.
	 *
	 * @return true, if successful
	 */
	public boolean hasBorder();

	/**
	 * Maintain border size during scale.
	 *
	 * @return the i item listener
	 */
	public IItemListener maintainBorderSizeDuringScale();

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.csys.items.INestable#removeItem(multiplicity3.csys.items
	 * .item.IItem)
	 */
	@Override
	public void removeItem(IItem item);

	/**
	 * Sets the border.
	 *
	 * @param b
	 *            the new border
	 */
	public void setBorder(IRoundedBorder b);

	/**
	 * Sets the gradient background.
	 *
	 * @param g
	 *            the new gradient background
	 */
	public void setGradientBackground(Gradient g);

	/**
	 * Sets the size.
	 *
	 * @param width
	 *            the width
	 * @param height
	 *            the height
	 */
	public void setSize(float width, float height);

	/**
	 * Sets the solid background colour.
	 *
	 * @param c
	 *            the new solid background colour
	 */
	public void setSolidBackgroundColour(Color c);
}
