package synergynet3.activitypack1.core.lightbox.lightboxmodel.items;

import java.awt.geom.Point2D.Float;
import java.io.Serializable;

/**
 * The Class LightBoxItem.
 */
public class LightBoxItem implements Serializable
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 6773468172887101449L;

	/** The moveable. */
	private boolean moveable;

	/** The position. */
	private Float position;

	/** The rotation degrees. */
	private int rotationDegrees;

	/**
	 * Gets the position.
	 *
	 * @return the position
	 */
	public Float getPosition()
	{
		return this.position;
	}

	/**
	 * Gets the rotation degrees.
	 *
	 * @return the rotation degrees
	 */
	public int getRotationDegrees()
	{
		return rotationDegrees;
	}

	/**
	 * Checks if is moveable.
	 *
	 * @return true, if is moveable
	 */
	public boolean isMoveable()
	{
		return this.moveable;
	}

	/**
	 * Sets the moveable.
	 *
	 * @param isMoveable
	 *            the new moveable
	 */
	public void setMoveable(boolean isMoveable)
	{
		this.moveable = isMoveable;
	}

	/**
	 * Sets the position.
	 *
	 * @param position
	 *            the new position
	 */
	public void setPosition(Float position)
	{
		this.position = position;
	}

	/**
	 * Sets the rotation degrees.
	 *
	 * @param rotationDegrees
	 *            the new rotation degrees
	 */
	public void setRotationDegrees(int rotationDegrees)
	{
		this.rotationDegrees = rotationDegrees;
	}
}
