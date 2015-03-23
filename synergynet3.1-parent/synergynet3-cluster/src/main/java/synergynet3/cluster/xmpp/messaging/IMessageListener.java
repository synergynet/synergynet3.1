package synergynet3.cluster.xmpp.messaging;

/**
 * The listener interface for receiving IMessage events. The class that is
 * interested in processing a IMessage event implements this interface, and the
 * object created with that class is registered with a component using the
 * component's <code>addIMessageListener<code> method. When
 * the IMessage event occurs, that object's appropriate
 * method is invoked.
 *
 * @see IMessageEvent
 */
public interface IMessageListener {

	/**
	 * Message received.
	 *
	 * @param message the message
	 */
	public void messageReceived(Message message);
}
