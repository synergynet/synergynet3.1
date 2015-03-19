package synergynet3.apps.numbernet;

import java.net.SocketException;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.logging.Logger;

import synergynet3.SynergyNetApp;
import synergynet3.apps.numbernet.controller.numbernettable.NumberNetController;
import synergynet3.cluster.SynergyNetCluster;
import synergynet3.cluster.threading.ClusterThreadManager;
import synergynet3.cluster.threading.IQueueProcessor;
import synergynet3.feedbacksystem.defaultfeedbacktypes.AudioFeedback;
import synergynet3.feedbacksystem.defaultfeedbacktypes.SmilieFeedback;

import multiplicity3.appsystem.IQueueOwner;
import multiplicity3.appsystem.MultiplicityClient;
import multiplicity3.config.identity.IdentityConfigPrefsItem;
import multiplicity3.csys.factory.ContentTypeNotBoundException;
import multiplicity3.input.MultiTouchInputComponent;

public class NumberNet extends SynergyNetApp {	

	private static final Logger log = Logger.getLogger(NumberNet.class.getName());

	public static void main(String[] args) throws SocketException {
		if(args.length > 0) {
			IdentityConfigPrefsItem idprefs = new IdentityConfigPrefsItem();
			idprefs.setID(args[0]);
		}

		MultiplicityClient client = MultiplicityClient.get();
		client.start();
		NumberNet app = new NumberNet();
		client.setCurrentApp(app);
	}

	private NumberNetController numbernetController;

	@Override
	public void shouldStart(final MultiTouchInputComponent input, final IQueueOwner iqo)
	{
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

	private void joinDataCluster() {
		SynergyNetCluster.get().getDeviceClusterManager().join();
	}

	private void setupNetworkThreading(final IQueueOwner iqo) {
		ClusterThreadManager.get().setQueueProcessor(new IQueueProcessor() {
			@Override
			public <V> Future<V> enqueue(Callable<V> callable) {
				return iqo.enqueue(callable);
			}			
		});
	}

	@Override
	public void shouldStop() {
		numbernetController.shutdown();
	}

	@Override
	public String getFriendlyAppName() {
		return "NumberNet";
	}

}
