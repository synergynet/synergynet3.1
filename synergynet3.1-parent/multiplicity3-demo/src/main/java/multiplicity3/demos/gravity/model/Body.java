package multiplicity3.demos.gravity.model;

import multiplicity3.csys.items.item.IItem;

import com.jme3.math.Vector2f;

/**
 * The Class Body.
 */
public class Body
{

	/** The mass. */
	public MassReference mass;

	/** The velocity. */
	public Vector2f velocity;

	/** The name. */
	private String name;

	/** The representation. */
	private IItem representation;

	/**
	 * Instantiates a new body.
	 *
	 * @param name
	 *            the name
	 * @param representation
	 *            the representation
	 * @param mass
	 *            the mass
	 * @param position
	 *            the position
	 * @param velocity
	 *            the velocity
	 */
	public Body(String name, IItem representation, MassReference mass, Vector2f position, Vector2f velocity)
	{
		this.name = name;
		this.representation = representation;
		this.mass = mass;
		this.velocity = velocity;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName()
	{
		return representation.getName();
	}

	/**
	 * Gets the position.
	 *
	 * @return the position
	 */
	public Vector2f getPosition()
	{
		return representation.getRelativeLocation();
	}

	/**
	 * Gets the representation.
	 *
	 * @return the representation
	 */
	public IItem getRepresentation()
	{
		return representation;
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
		representation.setRelativeLocation(new Vector2f(x, y));
	}

	/**
	 * Sets the position.
	 *
	 * @param pos
	 *            the new position
	 */
	public void setPosition(Vector2f pos)
	{
		representation.setRelativeLocation(pos);
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
