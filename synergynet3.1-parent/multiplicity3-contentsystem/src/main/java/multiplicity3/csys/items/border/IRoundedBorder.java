package multiplicity3.csys.items.border;

import multiplicity3.csys.items.item.IItem;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;

/**
 * The Interface IRoundedBorder.
 */
public interface IRoundedBorder extends IItem {

	/**
	 * Gets the border width.
	 *
	 * @return the border width
	 */
	public float getBorderWidth();

	/**
	 * Gets the size.
	 *
	 * @return the size
	 */
	public Vector2f getSize();

	/**
	 * Sets the border width.
	 *
	 * @param borderSize the new border width
	 */
	public void setBorderWidth(float borderSize);

	/**
	 * Sets the color.
	 *
	 * @param color the new color
	 */
	public void setColor(ColorRGBA color);

	/**
	 * Sets the size.
	 *
	 * @param width the width
	 * @param height the height
	 */
	public void setSize(float width, float height);

	/**
	 * Sets the size.
	 *
	 * @param size the new size
	 */
	public void setSize(Vector2f size);
}
