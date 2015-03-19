package multiplicity3.input.luminja.exceptions;

import multiplicity3.input.exceptions.MultiTouchInputException;

public class LuminSystemException extends MultiTouchInputException {
	private static final long serialVersionUID = 669142604850871346L;
	private Throwable cause;
	
	public LuminSystemException(Throwable t) {
		super();
		this.setCause(t);
	}

	public void setCause(Throwable cause) {
		this.cause = cause;
	}

	public Throwable getCause() {
		return cause;
	}


}
