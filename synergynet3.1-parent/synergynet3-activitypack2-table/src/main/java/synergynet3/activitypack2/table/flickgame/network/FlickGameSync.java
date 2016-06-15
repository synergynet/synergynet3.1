package synergynet3.activitypack2.table.flickgame.network;

import synergynet3.activitypack2.core.FlickGameDeviceControl;
import synergynet3.activitypack2.table.flickgame.FlickGameApp;
import synergynet3.activitypack2.web.shared.flickgame.FlickGameScore;
import synergynet3.cluster.sharedmemory.DistributedPropertyChangedAction;

import com.hazelcast.core.Member;

/**
 * The Class FlickGameSync.
 */
public class FlickGameSync
{

	/** The c. */
	private FlickGameDeviceControl c;

	/** The flick game. */
	private FlickGameApp flickGame;

	/** The score changed action. */
	private DistributedPropertyChangedAction<FlickGameScore> scoreChangedAction = new DistributedPropertyChangedAction<FlickGameScore>()
	{
		@Override
		public void distributedPropertyDidChange(Member member, FlickGameScore oldValue, FlickGameScore newValue)
		{
			flickGame.updateScore(newValue);
		}
	};

	/**
	 * Instantiates a new flick game sync.
	 *
	 * @param c
	 *            the c
	 * @param flickGame
	 *            the flick game
	 */
	public FlickGameSync(FlickGameDeviceControl c, FlickGameApp flickGame)
	{
		this.c = c;
		this.flickGame = flickGame;
		addSync();
	}

	/**
	 * Stop.
	 */
	public void stop()
	{
		c.getScore().unregisterChangeListener(scoreChangedAction);
	}

	/**
	 * Adds the sync.
	 */
	private void addSync()
	{
		c.getScore().registerChangeListener(scoreChangedAction);
	}

}
