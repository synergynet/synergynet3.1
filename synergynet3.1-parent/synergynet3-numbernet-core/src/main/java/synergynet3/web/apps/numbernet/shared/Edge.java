package synergynet3.web.apps.numbernet.shared;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * The Class Edge.
 */
public class Edge implements Serializable, IsSerializable
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -738061162769704771L;

	/** The a. */
	public Node a;

	/** The b. */
	public Node b;

	/**
	 * Instantiates a new edge.
	 *
	 * @param a
	 *            the a
	 * @param b
	 *            the b
	 */
	public Edge(Node a, Node b)
	{
		this.a = a;
		this.b = b;
	}

	/**
	 * Gets the key.
	 *
	 * @return the key
	 */
	public String getKey()
	{
		return a.getID() + "-" + b.getID();
	}

	/**
	 * Gets the length.
	 *
	 * @return the length
	 */
	public float getLength()
	{
		float diffX = b.getX() - a.getX();
		float diffY = b.getY() - a.getY();
		return (float) Math.sqrt((diffX * diffX) + (diffY * diffY));
	}
}
