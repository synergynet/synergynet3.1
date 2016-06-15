package multiplicity3.input.data;

import java.io.Serializable;

import com.jme3.math.Vector2f;

/**
 * Stores history instance of a cursor position in screen coordinates with time
 * in millis
 *
 * @author dcs0ah1
 */
public class CursorPositionRecord implements Serializable
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -4462161324401346938L;

	/** The position. */
	private Vector2f position;

	/** The time millis. */
	private long timeMillis;

	/**
	 * Instantiates a new cursor position record.
	 *
	 * @param position
	 *            the position
	 * @param timeMillis
	 *            the time millis
	 */
	public CursorPositionRecord(Vector2f position, long timeMillis)
	{
		this.setPosition(position);
		this.setTimeMillis(timeMillis);
	}

	/**
	 * Gets the position.
	 *
	 * @return the position
	 */
	public Vector2f getPosition()
	{
		return position;
	}

	/**
	 * Gets the time millis.
	 *
	 * @return the time millis
	 */
	public long getTimeMillis()
	{
		return timeMillis;
	}

	/**
	 * Sets the position.
	 *
	 * @param position
	 *            the new position
	 */
	public void setPosition(Vector2f position)
	{
		this.position = position.clone();
	}

	/**
	 * Sets the time millis.
	 *
	 * @param timeMillis
	 *            the new time millis
	 */
	public void setTimeMillis(long timeMillis)
	{
		this.timeMillis = timeMillis;
	}
}
