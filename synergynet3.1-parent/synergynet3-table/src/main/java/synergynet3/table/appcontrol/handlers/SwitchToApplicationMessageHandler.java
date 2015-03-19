package synergynet3.table.appcontrol.handlers;

import java.util.logging.Level;
import java.util.logging.Logger;

import multiplicity3.appsystem.IMultiplicityApp;
import multiplicity3.appsystem.MultiplicityClient;
import synergynet3.cluster.xmpp.messaging.appcontrol.AppControlMessage;
import synergynet3.cluster.xmpp.messaging.appcontrol.SwitchToApplication;
import synergynet3.table.appcontrol.AppFactory;

public class SwitchToApplicationMessageHandler implements AppControlMessageHandler {
	
	private static final Logger log = Logger.getLogger(SwitchToApplicationMessageHandler.class.getName());

	@Override
	public void handleMessage(AppControlMessage msg) {
		SwitchToApplication switchMessage = (SwitchToApplication) msg;
		String appClassName = switchMessage.getClassname();
		IMultiplicityApp app;
		try {
			app = AppFactory.instantiateAppForClassName(appClassName);
			MultiplicityClient.get().setCurrentApp(app);
		} catch (ClassNotFoundException e) {
			log.log(Level.SEVERE, "Error switching to app " + appClassName, e);
		} catch (InstantiationException e) {
			log.log(Level.SEVERE, "Error switching to app " + appClassName, e);
		} catch (IllegalAccessException e) {
			log.log(Level.SEVERE, "Error switching to app " + appClassName, e);
		}
	}
}
