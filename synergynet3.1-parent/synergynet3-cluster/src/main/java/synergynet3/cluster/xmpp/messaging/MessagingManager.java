package synergynet3.cluster.xmpp.messaging;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;

import synergynet3.cluster.threading.ClusterThreadManager;

/**
 * The Class MessagingManager.
 */
public class MessagingManager implements PacketListener {

	/**
	 * The Class ListenerWithInterestedType.
	 */
	private static class ListenerWithInterestedType {

		/** The listener. */
		private IMessageListener listener;

		/** The objects of type. */
		private Class<?> objectsOfType;

		/**
		 * Instantiates a new listener with interested type.
		 *
		 * @param listener the listener
		 * @param objectsOfType the objects of type
		 */
		public ListenerWithInterestedType(IMessageListener listener,
				Class<?> objectsOfType) {
			this.listener = listener;
			this.objectsOfType = objectsOfType;
		}

		/**
		 * Gets the listener.
		 *
		 * @return the listener
		 */
		public IMessageListener getListener() {
			return listener;
		}

		/**
		 * Checks if is interested in messages of type.
		 *
		 * @param type the type
		 * @return true, if is interested in messages of type
		 */
		public boolean isInterestedInMessagesOfType(Class<?> type) {
			return objectsOfType.isAssignableFrom(type);
		}
	}

	/** The Constant PROPERTY_KEY_PAYLOAD. */
	private static final String PROPERTY_KEY_PAYLOAD = "multiplicity";

	/** The listeners. */
	private List<ListenerWithInterestedType> listeners;

	/** The xmpp. */
	private XMPPConnection xmpp;

	/**
	 * Instantiates a new messaging manager.
	 */
	public MessagingManager() {
		listeners = new ArrayList<ListenerWithInterestedType>();
	}

	/**
	 * Device name to jid.
	 *
	 * @param device the device
	 * @return the string
	 */
	public String deviceNameToJID(String device) {
		String[] devices = { device };
		return namesToJIDs(devices)[0];
	}

	/**
	 * Names to ji ds.
	 *
	 * @param devices the devices
	 * @return the string[]
	 */
	public String[] namesToJIDs(String... devices) {
		String[] jids = new String[devices.length];
		for (RosterEntry re : xmpp.getRoster().getEntries()) {
			for (int i = 0; i < devices.length; i++) {
				if (re.getName().equals(devices[i])) {
					jids[i] = re.getUser();
				}
			}
		}
		return jids;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.jivesoftware.smack.PacketListener#processPacket(org.jivesoftware.
	 * smack.packet.Packet)
	 */
	@Override
	public void processPacket(Packet inboundXMPPPacket) {
		Object payloadValue = inboundXMPPPacket
				.getProperty(PROPERTY_KEY_PAYLOAD);
		if (!isValidMultiplicityMessage(payloadValue)) {
			return;
		}

		notifyInterestedListenersOfMultiplicityMessage((synergynet3.cluster.xmpp.messaging.Message) payloadValue);
	}

	/**
	 * Register message listener.
	 *
	 * @param listener the listener
	 * @param objectsOfType the objects of type
	 */
	public void registerMessageListener(IMessageListener listener,
			Class<?> objectsOfType) {
		listeners.add(new ListenerWithInterestedType(listener, objectsOfType));
	}

	/**
	 * Send message to devices.
	 *
	 * @param message the message
	 * @param devices the devices
	 */
	public void sendMessageToDevices(
			synergynet3.cluster.xmpp.messaging.Message message,
			String... devices) {
		String[] jids = namesToJIDs(devices);
		sendMessageToJIDs(message, jids);
	}

	/**
	 * Sets the connection.
	 *
	 * @param xmpp the new connection
	 */
	public void setConnection(XMPPConnection xmpp) {
		this.xmpp = xmpp;
		this.xmpp.addPacketListener(this, new PacketFilter() {
			@Override
			public boolean accept(Packet p) {
				return true;
			}
		});
	}

	/**
	 * Gets the message for recipient with payload.
	 *
	 * @param recipient the recipient
	 * @param messagePayload the message payload
	 * @return the message for recipient with payload
	 */
	private Message getMessageForRecipientWithPayload(String recipient,
			synergynet3.cluster.xmpp.messaging.Message messagePayload) {
		Message message = new Message(recipient);
		message.setBody("test");
		message.setProperty(PROPERTY_KEY_PAYLOAD, messagePayload);
		return message;
	}

	/**
	 * Checks if is valid multiplicity message.
	 *
	 * @param obj the obj
	 * @return true, if is valid multiplicity message
	 */
	private boolean isValidMultiplicityMessage(Object obj) {
		return (obj != null)
				&& (obj instanceof synergynet3.cluster.xmpp.messaging.Message);
	}

	/**
	 * Notify interested listeners of multiplicity message.
	 *
	 * @param validMultiplicityMessage the valid multiplicity message
	 */
	private void notifyInterestedListenersOfMultiplicityMessage(
			final synergynet3.cluster.xmpp.messaging.Message validMultiplicityMessage) {
		for (final ListenerWithInterestedType listener : listeners) {
			if (listener.isInterestedInMessagesOfType(validMultiplicityMessage
					.getClass())) {
				ClusterThreadManager.get().enqueue(new Callable<Void>() {
					@Override
					public Void call() throws Exception {
						listener.getListener().messageReceived(
								validMultiplicityMessage);
						return null;
					}
				});
			}
		}
	}

	/**
	 * Send message to ji ds.
	 *
	 * @param message the message
	 * @param recipients the recipients
	 */
	private void sendMessageToJIDs(
			synergynet3.cluster.xmpp.messaging.Message message,
			String... recipients) {
		for (String recipient : recipients) {
			Message xmppMessage = getMessageForRecipientWithPayload(recipient,
					message);
			xmpp.sendPacket(xmppMessage);
		}
	}
}
