package synergynet3.apps.numbernet.ui.expression.scores;

import java.util.UUID;

import multiplicity3.csys.factory.ContentTypeNotBoundException;
import multiplicity3.csys.factory.IContentFactory;
import multiplicity3.csys.items.container.IContainer;
import multiplicity3.csys.items.item.IItem;
import multiplicity3.csys.items.mutablelabel.IMutableLabel;
import multiplicity3.csys.stage.IStage;

import com.jme3.math.Vector2f;

/**
 * The Class ScoresUI.
 */
public class ScoresUI
{

	/** The container. */
	private IItem container;

	/** The content factory. */
	private IContentFactory contentFactory;

	/** The correct line. */
	private IMutableLabel correctLine;

	/** The incorrect line. */
	private IMutableLabel incorrectLine;

	/**
	 * Instantiates a new scores ui.
	 *
	 * @param stage
	 *            the stage
	 */
	public ScoresUI(IStage stage)
	{
		this.contentFactory = stage.getContentFactory();
	}

	/**
	 * Gets the content item.
	 *
	 * @return the content item
	 * @throws ContentTypeNotBoundException
	 *             the content type not bound exception
	 */
	public IItem getContentItem() throws ContentTypeNotBoundException
	{
		if (container == null)
		{
			createUI();
		}
		return container;
	}

	/**
	 * Sets the scores.
	 *
	 * @param correct
	 *            the correct
	 * @param incorrect
	 *            the incorrect
	 */
	public void setScores(int correct, int incorrect)
	{
		correctLine.setText(getCorrectScoreLabelTextForScore(correct));
		incorrectLine.setText(getIncorrectScoreLabelTextForScore(incorrect));
	}

	/**
	 * Sets the visible.
	 *
	 * @param b
	 *            the new visible
	 */
	public void setVisible(boolean b)
	{
		this.container.setVisible(b);
	}

	/**
	 * Creates the ui.
	 *
	 * @throws ContentTypeNotBoundException
	 *             the content type not bound exception
	 */
	private void createUI() throws ContentTypeNotBoundException
	{
		container = contentFactory.create(IContainer.class, "scores", UUID.randomUUID());

		correctLine = contentFactory.create(IMutableLabel.class, "correctscore", UUID.randomUUID());
		correctLine.setText(getCorrectScoreLabelTextForScore(0));

		incorrectLine = contentFactory.create(IMutableLabel.class, "correctscore", UUID.randomUUID());
		incorrectLine.setText(getIncorrectScoreLabelTextForScore(0));
		incorrectLine.setRelativeLocation(new Vector2f(0, -30f));

		container.addItem(correctLine);
		container.addItem(incorrectLine);

		container.setRelativeScale(0.5f);
	}

	/**
	 * Gets the correct score label text for score.
	 *
	 * @param score
	 *            the score
	 * @return the correct score label text for score
	 */
	private String getCorrectScoreLabelTextForScore(int score)
	{
		return "Correct: " + score;
	}

	/**
	 * Gets the incorrect score label text for score.
	 *
	 * @param score
	 *            the score
	 * @return the incorrect score label text for score
	 */
	private String getIncorrectScoreLabelTextForScore(int score)
	{
		return "Incorrect: " + score;
	}
}
