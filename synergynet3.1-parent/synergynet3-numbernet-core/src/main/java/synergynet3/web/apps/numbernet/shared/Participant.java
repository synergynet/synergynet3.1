package synergynet3.web.apps.numbernet.shared;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Represents an individual person.
 *
 * @author dcs0ah1
 */
public class Participant implements Serializable, IsSerializable
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 2647062936457560005L;

	/** The name. */
	private String name;

	/**
	 * Instantiates a new participant.
	 */
	public Participant()
	{
		this.name = "<none>";
	}

	/**
	 * Instantiates a new participant.
	 *
	 * @param name
	 *            the name
	 */
	public Participant(String name)
	{
		this.name = name;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return getName();
	}

}
