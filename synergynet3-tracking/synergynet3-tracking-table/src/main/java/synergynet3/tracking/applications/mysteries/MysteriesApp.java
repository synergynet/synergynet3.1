package synergynet3.tracking.applications.mysteries;

import java.io.IOException;

import multiplicity3.appsystem.IQueueOwner;
import multiplicity3.appsystem.MultiplicityClient;
import multiplicity3.config.identity.IdentityConfigPrefsItem;
import multiplicity3.csys.factory.ContentTypeNotBoundException;
import multiplicity3.input.MultiTouchInputComponent;
import synergynet3.feedbacksystem.defaultfeedbacktypes.AudioFeedback;
import synergynet3.feedbacksystem.defaultfeedbacktypes.SimpleTrafficLightFeedback;
import synergynet3.feedbacksystem.defaultfeedbacktypes.SmilieFeedback;
import synergynet3.feedbacksystem.defaultfeedbacktypes.YesOrNoFeedback;
import synergynet3.projector.network.ProjectorTransferUtilities;
import synergynet3.tracking.applications.TrackedApp;

/**
 * The Class MysteriesApp.
 */
public class MysteriesApp extends TrackedApp {

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		if (args.length > 0) {
			IdentityConfigPrefsItem idprefs = new IdentityConfigPrefsItem();
			idprefs.setID(args[0]);
		}

		TrackedApp.initialiseTrackingAppArgs(args);

		MultiplicityClient client = MultiplicityClient.get();
		client.start();
		MysteriesApp app = new MysteriesApp();
		client.setCurrentApp(app);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynet3.tracking.applications.TrackedApp#shouldStart(multiplicity3
	 * .input.MultiTouchInputComponent, multiplicity3.appsystem.IQueueOwner)
	 */
	@Override
	public void shouldStart(MultiTouchInputComponent input, IQueueOwner iqo) {
		input.registerMultiTouchEventListener(this);
		super.shouldStart(input, iqo);
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.SynergyNetApp#loadDefaultContent()
	 */
	@Override
	protected void loadDefaultContent() throws IOException,
			ContentTypeNotBoundException {

		ProjectorTransferUtilities.get().setDecelerationOnArrival(-1);

		feedbackTypes.add(SimpleTrafficLightFeedback.class);
		feedbackTypes.add(AudioFeedback.class);
		feedbackTypes.add(SmilieFeedback.class);
		feedbackTypes.add(YesOrNoFeedback.class);
	}

}