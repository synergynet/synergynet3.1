package synergynet3.apps.numbernet.model;

/**
 * The Class ExpressionSessionScores.
 */
public class ExpressionSessionScores
{

	/** The correct. */
	private int correct = 0;

	/** The incorrect. */
	private int incorrect = 0;

	/**
	 * Instantiates a new expression session scores.
	 *
	 * @param correct
	 *            the correct
	 * @param incorrect
	 *            the incorrect
	 */
	public ExpressionSessionScores(int correct, int incorrect)
	{
		this.correct = correct;
		this.incorrect = incorrect;
	}

	/**
	 * Gets the correct.
	 *
	 * @return the correct
	 */
	public int getCorrect()
	{
		return correct;
	}

	/**
	 * Gets the incorrect.
	 *
	 * @return the incorrect
	 */
	public int getIncorrect()
	{
		return incorrect;
	}
}
