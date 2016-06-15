package synergynet3.activitypack1.table.gravitysim.model;

import com.jme3.math.Vector2f;

/**
 * The Class Body.
 */
public class Body
{

	/** The global id. */
	private static int globalId = 0;

	/** The mass. */
	public MassReference mass;

	/** The velocity. */
	public Vector2f velocity = new Vector2f();

	/** The id. */
	private int id = 0;

	/** The ignore. */
	private boolean ignore = true;

	/** The name. */
	private String name;

	/** The pos. */
	private Vector2f pos = new Vector2f();

	/**
	 * Instantiates a new body.
	 *
	 * @param name
	 *            the name
	 * @param mass
	 *            the mass
	 * @param position
	 *            the position
	 * @param velocity
	 *            the velocity
	 */
	public Body(String name, MassReference mass, Vector2f position, Vector2f velocity)
	{
		id = globalId;
		globalId++;
		this.name = name;
		this.mass = mass;
		this.velocity = velocity;
		this.pos = position;
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public int getId()
	{
		return this.id;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName()
	{
		return this.name;
	}

	/**
	 * Gets the position.
	 *
	 * @return the position
	 */
	public Vector2f getPosition()
	{
		return pos;
	}

	/**
	 * Sets the active.
	 */
	public void setActive()
	{
		ignore = false;
	}

	/**
	 * Sets the ignore.
	 */
	public void setIgnore()
	{
		ignore = true;
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
		this.pos.set(x, y);
	}

	/**
	 * Sets the position.
	 *
	 * @param pos
	 *            the new position
	 */
	public void setPosition(Vector2f pos)
	{
		this.pos.set(pos);
	}

	/**
	 * Sets the velocity.
	 *
	 * @param v
	 *            the new velocity
	 */
	public void setVelocity(Vector2f v)
	{
		this.velocity.set(v);
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

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return name + "[" + mass + "]";
	}

}
