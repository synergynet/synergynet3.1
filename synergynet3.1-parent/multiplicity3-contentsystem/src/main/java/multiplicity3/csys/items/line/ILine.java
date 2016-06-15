package multiplicity3.csys.items.line;

import multiplicity3.csys.items.item.IItem;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;

/**
 * This line will link two items together such that when those items are
 * updated, the line will be updated too.
 *
 * @author dcs0ah1
 */

public interface ILine extends IItem
{

	/**
	 * The Enum LineMode.
	 */
	public enum LineMode
	{

		/** The linked. */
		LINKED,

		/** The unlinked. */
		UNLINKED
	}

	/**
	 * Gets the destination item.
	 *
	 * @return the destination item
	 */
	public IItem getDestinationItem();

	/**
	 * Gets the end position.
	 *
	 * @return the end position
	 */
	public Vector2f getEndPosition();

	/**
	 * Gets the length.
	 *
	 * @return the length
	 */
	public float getLength();

	/**
	 * Gets the mode.
	 *
	 * @return the mode
	 */
	public LineMode getMode();

	/**
	 * Gets the source item.
	 *
	 * @return the source item
	 */
	public IItem getSourceItem();

	/**
	 * Gets the start position.
	 *
	 * @return the start position
	 */
	public Vector2f getStartPosition();

	/**
	 * Sets the destination item.
	 *
	 * @param item
	 *            the new destination item
	 */
	public void setDestinationItem(IItem item);

	/**
	 * Sets the end position.
	 *
	 * @param v
	 *            the new end position
	 */
	public void setEndPosition(Vector2f v);

	/**
	 * Sets the line colour.
	 *
	 * @param c
	 *            the new line colour
	 */
	public void setLineColour(ColorRGBA c);

	/**
	 * Sets the line visibility changes with item visibility.
	 *
	 * @param autoVisibilityChange
	 *            the new line visibility changes with item visibility
	 */
	public void setLineVisibilityChangesWithItemVisibility(boolean autoVisibilityChange);

	/**
	 * Sets the line width.
	 *
	 * @param width
	 *            the new line width
	 */
	public void setLineWidth(float width);

	/**
	 * Sets the source item.
	 *
	 * @param item
	 *            the new source item
	 */
	public void setSourceItem(IItem item);

	/**
	 * Sets the start position.
	 *
	 * @param v
	 *            the new start position
	 */
	public void setStartPosition(Vector2f v);

}
