package synergynet3.table.appcontrol.handlers;

import synergynet3.cluster.xmpp.messaging.appcontrol.AppControlMessage;

public interface AppControlMessageHandler {
	public void handleMessage(AppControlMessage msg);
}
