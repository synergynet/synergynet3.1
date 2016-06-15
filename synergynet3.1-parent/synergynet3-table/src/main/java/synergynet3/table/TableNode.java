package synergynet3.table;

import multiplicity3.appsystem.MultiplicityClient;
import synergynet3.cluster.SynergyNetCluster;
import synergynet3.cluster.xmpp.messaging.appcontrol.AppControlMessage;
import synergynet3.cluster.xmpp.messaging.appcontrol.SwitchToApplication;
import synergynet3.table.appcontrol.AppController;
import synergynet3.table.appcontrol.handlers.SwitchToApplicationMessageHandler;

/**
 * The Class TableNode.
 */
public class TableNode
{

	/**
	 * Instantiates a new table node.
	 */
	public TableNode()
	{
		MultiplicityClient client = MultiplicityClient.get();
		client.start();
		initNetwork();
	}

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args)
	{
		new TableNode();
	}

	/**
	 * Gets the configured app controller.
	 *
	 * @return the configured app controller
	 */
	private AppController getConfiguredAppController()
	{
		AppController appController = new AppController();
		appController.addHandler(SwitchToApplication.class, new SwitchToApplicationMessageHandler());
		return appController;
	}

	/**
	 * Inits the network.
	 */
	private void initNetwork()
	{
		AppController appController = getConfiguredAppController();
		SynergyNetCluster.get().getMessagingManager().registerMessageListener(appController, AppControlMessage.class);

	}
}
