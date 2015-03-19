package synergynet3.apps.numbernet.ui.expression.scores;

import java.util.UUID;

import com.jme3.math.Vector2f;

import multiplicity3.csys.factory.ContentTypeNotBoundException;
import multiplicity3.csys.factory.IContentFactory;
import multiplicity3.csys.items.container.IContainer;
import multiplicity3.csys.items.item.IItem;
import multiplicity3.csys.items.mutablelabel.IMutableLabel;
import multiplicity3.csys.stage.IStage;

public class ScoresUI {
	private IItem container;
	private IMutableLabel correctLine;
	private IMutableLabel incorrectLine;
	private IContentFactory contentFactory;
	
	public ScoresUI(IStage stage) {
		this.contentFactory = stage.getContentFactory();		
	}
	
	public void setScores(int correct, int incorrect) {
		correctLine.setText(getCorrectScoreLabelTextForScore(correct));
		incorrectLine.setText(getIncorrectScoreLabelTextForScore(incorrect));
	}

	public void setVisible(boolean b) {
		this.container.setVisible(b);
	}

	public IItem getContentItem() throws ContentTypeNotBoundException {
		if(container == null) {
			createUI();
		}
		return container;
	}

	private void createUI() throws ContentTypeNotBoundException {
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
	
	private String getCorrectScoreLabelTextForScore(int score) {
		return "Correct: " + score;
	}
	
	private String getIncorrectScoreLabelTextForScore(int score) {
		return "Incorrect: " + score;
	}
}
