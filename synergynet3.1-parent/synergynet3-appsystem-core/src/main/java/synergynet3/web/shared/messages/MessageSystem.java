package synergynet3.web.shared.messages;

import java.util.ArrayList;
import java.util.Date;

/**
 * The Class MessageSystem.
 */
public class MessageSystem
{

	/** The generated i ds. */
	private static long generatedIDs = 0;

	/** The registered i ds. */
	private static ArrayList<String> registeredIDs = new ArrayList<String>();

	/** The table id. */
	private static String tableID = "";

	/**
	 * Generate message id.
	 *
	 * @return the string
	 */
	public static String generateMessageID()
	{
		if (tableID.equals(""))
		{
			tableID = "" + new Date().getTime();
		}
		String messageID = tableID + generatedIDs;
		incrementGeneratedMessageIDs();
		return messageID;
	}

	/**
	 * Message already received.
	 *
	 * @param message
	 *            the message
	 * @return true, if successful
	 */
	public static boolean messageAlreadyReceived(PerformActionMessage message)
	{
		if (registeredIDs.contains(message.getMessageID()))
		{
			return true;
		}
		else
		{
			registeredIDs.add(message.getMessageID());
			return false;
		}
	}

	/**
	 * Increment generated message i ds.
	 */
	private static void incrementGeneratedMessageIDs()
	{
		generatedIDs++;
		if (generatedIDs == Long.MAX_VALUE)
		{
			generatedIDs = 0;
		}
	}
}
