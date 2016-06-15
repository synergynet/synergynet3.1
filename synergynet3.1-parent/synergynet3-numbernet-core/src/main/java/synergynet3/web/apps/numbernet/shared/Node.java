package synergynet3.web.apps.numbernet.shared;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * The Class Node.
 */
public class Node implements Serializable, IsSerializable
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 3955541802925698325L;

	/** The id. */
	private String id;

	/** The ignore. */
	private boolean ignore;

	/** The x. */
	private float x;

	/** The y. */
	private float y;

	/**
	 * Instantiates a new node.
	 *
	 * @param id
	 *            the id
	 */
	public Node(String id)
	{
		this.id = id;
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public String getID()
	{
		return id;
	}

	/**
	 * Gets the x.
	 *
	 * @return the x
	 */
	public float getX()
	{
		return this.x;
	}

	/**
	 * Gets the y.
	 *
	 * @return the y
	 */
	public float getY()
	{
		return this.y;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		return id.hashCode();
	}

	/**
	 * Sets the ignore.
	 *
	 * @param b
	 *            the new ignore
	 */
	public void setIgnore(boolean b)
	{
		ignore = b;
	}

	/**
	 * Sets the position.
	 *
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 */
	public void setPosition(float x, float y)
	{
		this.x = x;
		this.y = y;
	}

	/**
	 * Should ignore.
	 *
	 * @return true, if successful
	 */
	public boolean shouldIgnore()
	{
		return ignore;
	}
}
