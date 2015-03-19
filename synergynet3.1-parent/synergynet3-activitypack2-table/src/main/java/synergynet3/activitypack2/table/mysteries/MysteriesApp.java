package synergynet3.activitypack2.table.mysteries;

import java.io.IOException;

import synergynet3.SynergyNetApp;
import synergynet3.feedbacksystem.defaultfeedbacktypes.AudioFeedback;
import synergynet3.feedbacksystem.defaultfeedbacktypes.SimpleTrafficLightFeedback;
import synergynet3.feedbacksystem.defaultfeedbacktypes.SmilieFeedback;
import synergynet3.feedbacksystem.defaultfeedbacktypes.YesOrNoFeedback;
import synergynet3.projector.network.ProjectorTransferUtilities;

import multiplicity3.appsystem.MultiplicityClient;
import multiplicity3.config.identity.IdentityConfigPrefsItem;
import multiplicity3.csys.factory.ContentTypeNotBoundException;

public class MysteriesApp extends SynergyNetApp {	
	@Override
	protected void loadDefaultContent() throws IOException, ContentTypeNotBoundException {		
		
		ProjectorTransferUtilities.get().setDecelerationOnArrival(-1);
		
		feedbackTypes.add(SimpleTrafficLightFeedback.class);
		feedbackTypes.add(AudioFeedback.class);
		feedbackTypes.add(SmilieFeedback.class);
		feedbackTypes.add(YesOrNoFeedback.class);		
	}
		
	public static void main(String[] args) {
		if(args.length > 0) {
			IdentityConfigPrefsItem idprefs = new IdentityConfigPrefsItem();
			idprefs.setID(args[0]);
		}
		
		MultiplicityClient client = MultiplicityClient.get();
		client.start();
		MysteriesApp app = new MysteriesApp();
		client.setCurrentApp(app);
	}

}