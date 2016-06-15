package multiplicity3.csys.factory;

import multiplicity3.csys.items.item.IItem;

/**
 * The Class ContentTypeAlreadyBoundException.
 */
public class ContentTypeAlreadyBoundException extends Exception
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 7555560238063836362L;

	/** The already bound to. */
	private Class<? extends IItem> alreadyBoundTo;

	/**
	 * Instantiates a new content type already bound exception.
	 *
	 * @param alreadyBoundTo
	 *            the already bound to
	 */
	public ContentTypeAlreadyBoundException(Class<? extends IItem> alreadyBoundTo)
	{
		this.setAlreadyBoundTo(alreadyBoundTo);
	}

	/**
	 * Gets the already bound to.
	 *
	 * @return the already bound to
	 */
	public Class<? extends IItem> getAlreadyBoundTo()
	{
		return alreadyBoundTo;
	}

	/**
	 * Sets the already bound to.
	 *
	 * @param alreadyBoundTo
	 *            the new already bound to
	 */
	public void setAlreadyBoundTo(Class<? extends IItem> alreadyBoundTo)
	{
		this.alreadyBoundTo = alreadyBoundTo;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Throwable#toString()
	 */
	@Override
	public String toString()
	{
		return super.toString() + " already bound to " + alreadyBoundTo.getName();
	}
}
