package synergynet3.apps.numbernet;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.logging.Logger;

import multiplicity3.appsystem.IMultiplicityApp;
import multiplicity3.appsystem.IQueueOwner;
import multiplicity3.appsystem.MultiplicityClient;
import multiplicity3.csys.MultiplicityEnvironment;
import multiplicity3.csys.stage.IStage;
import multiplicity3.input.MultiTouchInputComponent;
import synergynet3.apps.numbernet.network.projector.ProjectionModeSynchronizer;
import synergynet3.apps.numbernet.network.projector.ScoresSynchronizer;
import synergynet3.apps.numbernet.network.projector.TableCloneDisplaySynchronizer;
import synergynet3.apps.numbernet.ui.projection.expressions.ProjectExpressionsUI;
import synergynet3.apps.numbernet.ui.projection.scores.ProjectScoresUI;
import synergynet3.cluster.SynergyNetCluster;
import synergynet3.cluster.threading.ClusterThreadManager;
import synergynet3.cluster.threading.IQueueProcessor;
import synergynet3.web.apps.numbernet.comms.table.ProjectorDevice;

/**
 * The Class NumberNetProjector.
 */
public class NumberNetProjector implements IMultiplicityApp
{

	/** The Constant log. */
	private static final Logger log = Logger.getLogger(NumberNetProjector.class.getName());

	/** The project expressions ui. */
	private ProjectExpressionsUI projectExpressionsUI;

	/** The project scores ui. */
	private ProjectScoresUI projectScoresUI;

	/** The stage. */
	private IStage stage;

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args)
	{
		log.info("Starting NumberNet projector");
		MultiplicityClient client = MultiplicityClient.get();
		client.start();
		NumberNetProjector app = new NumberNetProjector();
		client.setCurrentApp(app);
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.appsystem.IMultiplicityApp#getFriendlyAppName()
	 */
	@Override
	public String getFriendlyAppName()
	{
		return "NumberNet Projector";
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.appsystem.IMultiplicityApp#onDestroy()
	 */
	@Override
	public void onDestroy()
	{
		SynergyNetCluster.get().shutdown();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.appsystem.IMultiplicityApp#shouldStart(multiplicity3.input
	 * .MultiTouchInputComponent, multiplicity3.appsystem.IQueueOwner)
	 */
	@Override
	public void shouldStart(MultiTouchInputComponent input, final IQueueOwner iqo)
	{
		stage = MultiplicityEnvironment.get().getLocalStages().get(0);
		stage.getZOrderManager().setAutoBringToTop(true);

		ClusterThreadManager.get().setQueueProcessor(new IQueueProcessor()
		{
			@Override
			public <V> Future<V> enqueue(Callable<V> callable)
			{
				return iqo.enqueue(callable);
			}
		});

		SynergyNetCluster.get().getDeviceClusterManager().join();
		final ProjectorDevice device = new ProjectorDevice(SynergyNetCluster.get().getIdentity());

		projectScoresUI = new ProjectScoresUI(stage);
		ScoresSynchronizer scoresSync = new ScoresSynchronizer(projectScoresUI);

		projectExpressionsUI = new ProjectExpressionsUI(stage);
		new TableCloneDisplaySynchronizer(projectExpressionsUI, device);

		new ProjectionModeSynchronizer(device, projectScoresUI, scoresSync, projectExpressionsUI);
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.appsystem.IMultiplicityApp#shouldStop()
	 */
	@Override
	public void shouldStop()
	{
	}

}
