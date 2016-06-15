package synergynet3.activitypack2.web.shared.flickgame;

import java.io.Serializable;

import synergynet3.web.shared.messages.PerformActionMessage;

/** Structured message representing the score for a flick game. */
public class FlickGameScore extends PerformActionMessage implements Serializable
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 4194520051144654645L;

	/** The scores of the two tables. */
	private int scoreBlue, scoreRed;

	/**
	 * Empty Constructor used to initialise value in the network cluster.
	 */
	public FlickGameScore()
	{
		super();
	}

	/**
	 * Create a structured device position message for transmission through the
	 * cluster.
	 *
	 * @param remove
	 */
	public FlickGameScore(boolean remove)
	{
		super(MESSAGESTATE.DEACTIVATE);
	}

	/**
	 * Create a structured score message for transmission through the cluster.
	 *
	 * @param scoreBlue
	 *            Score of device 1.
	 * @param scoreRed
	 *            Score of device 2.
	 */
	public FlickGameScore(int scoreBlue, int scoreRed)
	{
		super(MESSAGESTATE.ACTIVATE);
		this.scoreBlue = scoreBlue;
		this.scoreRed = scoreRed;
	}

	/**
	 * Get the score for device 1.
	 *
	 * @return String representing the score for device 1.
	 */
	public int getScoreBlue()
	{
		return scoreBlue;
	}

	/**
	 * Get the score for device 2.
	 *
	 * @return String representing the score for device 2.
	 */
	public int getScoreRed()
	{
		return scoreRed;
	}

}
