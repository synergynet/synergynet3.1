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


public class MessagingManager implements PacketListener {
	private static final String PROPERTY_KEY_PAYLOAD = "multiplicity";
	
	private XMPPConnection xmpp;	
	private List<ListenerWithInterestedType> listeners;

	public MessagingManager() {
		listeners = new ArrayList<ListenerWithInterestedType>();
	}
	
	public void setConnection(XMPPConnection xmpp) {
		this.xmpp = xmpp;
		this.xmpp.addPacketListener(this, new PacketFilter() {
			@Override
			public boolean accept(Packet p) {
				return true;
			}			
		});
	}

	public void registerMessageListener(IMessageListener listener, Class<?> objectsOfType) {
		listeners.add(new ListenerWithInterestedType(listener, objectsOfType));
	}

	@Override
	public void processPacket(Packet inboundXMPPPacket) {
		Object payloadValue = inboundXMPPPacket.getProperty(PROPERTY_KEY_PAYLOAD);
		if(!isValidMultiplicityMessage(payloadValue)) 
			return;

		notifyInterestedListenersOfMultiplicityMessage((synergynet3.cluster.xmpp.messaging.Message) payloadValue);
	}

	private void notifyInterestedListenersOfMultiplicityMessage(final synergynet3.cluster.xmpp.messaging.Message validMultiplicityMessage) {
		for(final ListenerWithInterestedType listener : listeners) {
			if(listener.isInterestedInMessagesOfType(validMultiplicityMessage.getClass())) {
				ClusterThreadManager.get().enqueue(new Callable<Void>() {
					@Override
					public Void call() throws Exception {
						listener.getListener().messageReceived(validMultiplicityMessage);
						return null;
					}					
				});				
			}
		}
	}

	private boolean isValidMultiplicityMessage(Object obj) {
		return obj != null && obj instanceof synergynet3.cluster.xmpp.messaging.Message;
	}

	public void sendMessageToDevices(synergynet3.cluster.xmpp.messaging.Message message, String... devices) {
		String[] jids = namesToJIDs(devices);
		sendMessageToJIDs(message, jids);
	}

	private void sendMessageToJIDs(synergynet3.cluster.xmpp.messaging.Message message, String... recipients) {
		for(String recipient : recipients) {
			Message xmppMessage = getMessageForRecipientWithPayload(recipient, message);
			xmpp.sendPacket(xmppMessage);
		}
	}
	
	public String deviceNameToJID(String device) {
		String[] devices = { device };
		return namesToJIDs(devices)[0];
	}

	public String[] namesToJIDs(String... devices) {		
		String[] jids = new String[devices.length];
		for(RosterEntry re : xmpp.getRoster().getEntries()) {
			for(int i = 0; i < devices.length; i++) {
				if(re.getName().equals(devices[i])) {
					jids[i] = re.getUser();
				}
			}
		}
		return jids;
	}

	private Message getMessageForRecipientWithPayload(String recipient, synergynet3.cluster.xmpp.messaging.Message messagePayload) {
		Message message = new Message(recipient);
		message.setBody("test");
		message.setProperty(PROPERTY_KEY_PAYLOAD, messagePayload);
		return message;
	}

	private static class ListenerWithInterestedType {
		private IMessageListener listener;
		private Class<?> objectsOfType;

		public ListenerWithInterestedType(IMessageListener listener,
				Class<?> objectsOfType) {
			this.listener = listener;
			this.objectsOfType = objectsOfType;
		}

		public IMessageListener getListener() {
			return listener;
		}

		public boolean isInterestedInMessagesOfType(Class<?> type) {
			return objectsOfType.isAssignableFrom(type);
		}
	}
}
