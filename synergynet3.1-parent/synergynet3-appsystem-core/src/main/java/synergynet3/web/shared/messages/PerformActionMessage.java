package synergynet3.web.shared.messages;

import java.io.Serializable;

/**
 * The Class PerformActionMessage.
 */
public class PerformActionMessage implements Serializable
{

	/**
	 * The Enum MESSAGESTATE.
	 */
	public enum MESSAGESTATE
	{
		/** The activate. */
		ACTIVATE, /** The deactivate. */
		DEACTIVATE, /** The do nothing. */
		DO_NOTHING
	}

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 8614541915205145862L;

	/** The message id. */
	private String messageID = "";

	/** The state. */
	private MESSAGESTATE state = MESSAGESTATE.DO_NOTHING;

	/**
	 * Instantiates a new perform action message.
	 */
	public PerformActionMessage()
	{
		messageID = MessageSystem.generateMessageID();
	}

	/**
	 * Instantiates a new perform action message.
	 *
	 * @param state
	 *            the state
	 */
	public PerformActionMessage(MESSAGESTATE state)
	{
		this.state = state;
		messageID = MessageSystem.generateMessageID();
	}

	/**
	 * Gets the message id.
	 *
	 * @return the message id
	 */
	public String getMessageID()
	{
		return messageID;
	}

	/**
	 * Gets the message state.
	 *
	 * @return the message state
	 */
	public MESSAGESTATE getMessageState()
	{
		return state;
	}

	/**
	 * Message already received.
	 *
	 * @return true, if successful
	 */
	public boolean messageAlreadyReceived()
	{
		return MessageSystem.messageAlreadyReceived(this);
	}

	/**
	 * Regenerate id.
	 */
	public void regenerateID()
	{
		messageID = MessageSystem.generateMessageID();
	}
}
