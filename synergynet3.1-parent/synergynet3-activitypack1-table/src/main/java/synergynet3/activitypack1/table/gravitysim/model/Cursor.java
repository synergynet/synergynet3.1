package synergynet3.activitypack1.table.gravitysim.model;

import com.jme3.math.Vector2f;

/**
 * The Class Cursor.
 */
public class Cursor
{

	/**
	 * The Enum Mode.
	 */
	public enum Mode
	{

		/** The dragging. */
		DRAGGING
	}

	/** The current. */
	public Vector2f current;

	/** The endpos. */
	public Vector2f endpos;

	/** The id. */
	public long id;

	/** The mode. */
	public Mode mode;

	/** The startpos. */
	public Vector2f startpos = new Vector2f();

	/**
	 * Instantiates a new cursor.
	 *
	 * @param cursorID
	 *            the cursor id
	 * @param position
	 *            the position
	 */
	public Cursor(long cursorID, Vector2f position)
	{
		this.id = cursorID;
		this.startpos = position.clone();
		this.mode = Mode.DRAGGING;
	}

	/**
	 * Sets the current position.
	 *
	 * @param pos
	 *            the new current position
	 */
	public void setCurrentPosition(Vector2f pos)
	{
		this.current = pos.clone();
	}

	/**
	 * Sets the end position.
	 *
	 * @param pos
	 *            the new end position
	 */
	public void setEndPosition(Vector2f pos)
	{
		this.endpos = pos.clone();
	}
}
