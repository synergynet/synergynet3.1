package synergynet3.tracking.applications.numberNet;

import java.net.SocketException;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.logging.Logger;

import multiplicity3.appsystem.IQueueOwner;
import multiplicity3.appsystem.MultiplicityClient;
import multiplicity3.config.identity.IdentityConfigPrefsItem;
import multiplicity3.csys.factory.ContentTypeNotBoundException;
import multiplicity3.input.MultiTouchInputComponent;
import synergynet3.apps.numbernet.controller.numbernettable.NumberNetController;
import synergynet3.cluster.SynergyNetCluster;
import synergynet3.cluster.threading.ClusterThreadManager;
import synergynet3.cluster.threading.IQueueProcessor;
import synergynet3.feedbacksystem.defaultfeedbacktypes.AudioFeedback;
import synergynet3.feedbacksystem.defaultfeedbacktypes.SmilieFeedback;
import synergynet3.tracking.applications.TrackedApp;

/**
 * The Class NumberNetTracked.
 */
public class NumberNetTracked extends TrackedApp {

	/** The Constant log. */
	private static final Logger log = Logger.getLogger(NumberNetTracked.class
			.getName());

	/** The numbernet controller. */
	private NumberNetController numbernetController;

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 * @throws SocketException the socket exception
	 */
	public static void main(String[] args) throws SocketException {
		if (args.length > 0) {
			IdentityConfigPrefsItem idprefs = new IdentityConfigPrefsItem();
			idprefs.setID(args[0]);
		}

		TrackedApp.initialiseTrackingAppArgs(args);

		MultiplicityClient client = MultiplicityClient.get();
		client.start();
		NumberNetTracked app = new NumberNetTracked();
		client.setCurrentApp(app);
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.SynergyNetApp#getFriendlyAppName()
	 */
	@Override
	public String getFriendlyAppName() {
		return "NumberNet";
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynet3.tracking.applications.TrackedApp#shouldStart(multiplicity3
	 * .input.MultiTouchInputComponent, multiplicity3.appsystem.IQueueOwner)
	 */
	@Override
	public void shouldStart(final MultiTouchInputComponent input,
			final IQueueOwner iqo) {
		super.shouldStart(input, iqo);
		log.fine("Starting NumberNet");

		feedbackTypes.add(AudioFeedback.class);
		feedbackTypes.add(SmilieFeedback.class);

		try {
			setupNetworkThreading(iqo);
			joinDataCluster();

			numbernetController = new NumberNetController(input);

		} catch (ContentTypeNotBoundException e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.SynergyNetApp#shouldStop()
	 */
	@Override
	public void shouldStop() {
		numbernetController.shutdown();
	}

	/**
	 * Join data cluster.
	 */
	private void joinDataCluster() {
		SynergyNetCluster.get().getDeviceClusterManager().join();
	}

	/**
	 * Sets the up network threading.
	 *
	 * @param iqo the new up network threading
	 */
	private void setupNetworkThreading(final IQueueOwner iqo) {
		ClusterThreadManager.get().setQueueProcessor(new IQueueProcessor() {
			@Override
			public <V> Future<V> enqueue(Callable<V> callable) {
				return iqo.enqueue(callable);
			}
		});
	}

}
