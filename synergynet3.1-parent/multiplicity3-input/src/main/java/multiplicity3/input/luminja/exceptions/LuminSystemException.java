package multiplicity3.input.luminja.exceptions;

import multiplicity3.input.exceptions.MultiTouchInputException;

/**
 * The Class LuminSystemException.
 */
public class LuminSystemException extends MultiTouchInputException
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 669142604850871346L;

	/** The cause. */
	private Throwable cause;

	/**
	 * Instantiates a new lumin system exception.
	 *
	 * @param t
	 *            the t
	 */
	public LuminSystemException(Throwable t)
	{
		super();
		this.setCause(t);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Throwable#getCause()
	 */
	@Override
	public Throwable getCause()
	{
		return cause;
	}

	/**
	 * Sets the cause.
	 *
	 * @param cause
	 *            the new cause
	 */
	public void setCause(Throwable cause)
	{
		this.cause = cause;
	}

}
