package synergynet3.table.appcontrol;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import synergynet3.cluster.xmpp.messaging.IMessageListener;
import synergynet3.cluster.xmpp.messaging.Message;
import synergynet3.cluster.xmpp.messaging.appcontrol.AppControlMessage;
import synergynet3.table.appcontrol.handlers.AppControlMessageHandler;

/**
 * The Class AppController.
 */
public class AppController implements IMessageListener {

	/** The Constant log. */
	private static final Logger log = Logger.getLogger(AppController.class
			.getName());

	/** The handlers. */
	private Map<String, AppControlMessageHandler> handlers = new HashMap<String, AppControlMessageHandler>();

	/**
	 * Adds the handler.
	 *
	 * @param messageType the message type
	 * @param appControlMessageHandler the app control message handler
	 */
	public void addHandler(Class<? extends AppControlMessage> messageType,
			AppControlMessageHandler appControlMessageHandler) {
		handlers.put(messageType.getName(), appControlMessageHandler);
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.cluster.xmpp.messaging.IMessageListener#messageReceived(
	 * synergynet3.cluster.xmpp.messaging.Message)
	 */
	@Override
	public void messageReceived(Message msg) {
		if (msg == null) {
			return;
		}
		if (!(msg instanceof AppControlMessage)) {
			return;
		}

		AppControlMessage appControlMessage = (AppControlMessage) msg;
		Class<? extends AppControlMessage> messageType = appControlMessage
				.getClass();

		String className = messageType.getName();
		log.info("message received: " + className);

		AppControlMessageHandler handler = handlers.get(className);
		if (handler != null) {
			log.info("dispatching message to " + handler.getClass().getName());
			handler.handleMessage(appControlMessage);
		}
	}
}
