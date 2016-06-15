package synergynet3.table.appcontrol.handlers;

import synergynet3.cluster.xmpp.messaging.appcontrol.AppControlMessage;

/**
 * The Interface AppControlMessageHandler.
 */
public interface AppControlMessageHandler
{

	/**
	 * Handle message.
	 *
	 * @param msg
	 *            the msg
	 */
	public void handleMessage(AppControlMessage msg);
}
