package synergynet3.table;

import synergynet3.cluster.SynergyNetCluster;
import synergynet3.cluster.xmpp.messaging.appcontrol.AppControlMessage;
import synergynet3.cluster.xmpp.messaging.appcontrol.SwitchToApplication;
import synergynet3.table.appcontrol.AppController;
import synergynet3.table.appcontrol.handlers.SwitchToApplicationMessageHandler;
import multiplicity3.appsystem.MultiplicityClient;

public class TableNode {
	public TableNode() {
		MultiplicityClient client = MultiplicityClient.get();
		client.start();
		initNetwork();
	}
	
	private void initNetwork() {
		AppController appController = getConfiguredAppController();
		SynergyNetCluster.get().getMessagingManager().registerMessageListener(appController, AppControlMessage.class);
		
	}

	private AppController getConfiguredAppController() {
		AppController appController = new AppController();
		appController.addHandler(SwitchToApplication.class, new SwitchToApplicationMessageHandler());
		return appController;
	}

	public static void main(String[] args) {
		new TableNode();
	}
}
