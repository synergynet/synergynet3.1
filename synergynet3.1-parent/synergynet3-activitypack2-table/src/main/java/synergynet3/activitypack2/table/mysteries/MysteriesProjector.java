package synergynet3.activitypack2.table.mysteries;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.net.SocketException;

import synergynet3.additionalUtils.AdditionalSynergyNetUtilities;
import synergynet3.projector.SynergyNetProjector;
import synergynet3.projector.network.ProjectorTransferUtilities;
import multiplicity3.appsystem.IQueueOwner;
import multiplicity3.appsystem.MultiplicityClient;
import multiplicity3.config.identity.IdentityConfigPrefsItem;
import multiplicity3.input.MultiTouchInputComponent;

/** Class to be run to produce a projection environment.*/
public class MysteriesProjector extends SynergyNetProjector {
	
	public static String mysteriesLocation;
	
	@Override
	public void shouldStart(MultiTouchInputComponent input, IQueueOwner iqo) {
		super.shouldStart(input, iqo);	

		ProjectorTransferUtilities.get().setDecelerationOnArrival(-1);
		
		new MysteriesProjectorMenu(this);
	}
	
	@Override
	public String getFriendlyAppName() {
		return "MysteriesProjector";
	}
	
	public static void main(String[] args) throws SocketException {
		if(args.length > 0) {
			IdentityConfigPrefsItem idprefs = new IdentityConfigPrefsItem();			
			idprefs.setID(args[0]);
		}		
		
		try{
			mysteriesLocation = ManagementFactory.getRuntimeMXBean().getSystemProperties().get("mysteries");
			AdditionalSynergyNetUtilities.logInfo("Using mysteries from: " + mysteriesLocation );
		}catch(Exception e){
			stop();
		}
		
		if (mysteriesLocation == null)stop();
		
		File mysteriesDir = new File(mysteriesLocation);
		if (!mysteriesDir.isDirectory())stop();
		
		MultiplicityClient client = MultiplicityClient.get();
		client.start();
		MysteriesProjector app = new MysteriesProjector();
		client.setCurrentApp(app);		
	}
	
	private static void stop(){
		AdditionalSynergyNetUtilities.logInfo( "No valid mysteries address in arguments, no mysteries will be available.");
		System.exit(0);
	}
	
}