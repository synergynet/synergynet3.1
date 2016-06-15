package multiplicity3.csys.items.shapes;

import multiplicity3.csys.items.item.IItem;

import com.jme3.math.Vector2f;

/**
 * The Interface IRectangularItem.
 */
public interface IRectangularItem extends IItem
{

	/**
	 * Gets the height.
	 *
	 * @return the height
	 */
	public float getHeight();

	/**
	 * Gets the size.
	 *
	 * @return the size
	 */
	public Vector2f getSize();

	/**
	 * Gets the width.
	 *
	 * @return the width
	 */
	public float getWidth();

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
	 * Sets the size.
	 *
	 * @param size
	 *            the new size
	 */
	public void setSize(Vector2f size);
}
