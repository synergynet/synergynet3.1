package synergynet3.apps.numbernet.ui.projection.scores;

import java.util.UUID;

import synergynet3.fonts.FontColour;
import synergynet3.fonts.FontUtil;

import multiplicity3.csys.factory.ContentTypeNotBoundException;
import multiplicity3.csys.factory.IContentFactory;
import multiplicity3.csys.gfx.ColourUtils;
import multiplicity3.csys.gfx.Gradient;
import multiplicity3.csys.gfx.Gradient.GradientDirection;
import multiplicity3.csys.items.container.IContainer;
import multiplicity3.csys.items.mutablelabel.IMutableLabel;
import multiplicity3.csys.items.shapes.IColourRectangle;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;

public class TableScoresUI {	
	
//	private IContainer container;
	private String table;
	private IMutableLabel correctExpressionLabel;
	private IMutableLabel incorrectExpressionLabel;
	private IMutableLabel bracketUseLabel;
	private IMutableLabel usedAllOperators;
	private IMutableLabel moreThanOneOperator;

	public TableScoresUI(String table) {
		this.table = table;
	}

	public void buildUI(IContainer root, IContentFactory contentFactory) throws ContentTypeNotBoundException {
//		container = contentFactory.create(IContainer.class, "tablescores_" + table, UUID.randomUUID());
		root.getZOrderManager().setAutoBringToTop(false);
//		root.addItem(container);
		
		IColourRectangle backgroundRectangle = contentFactory.create(IColourRectangle.class, "bg_" + table, UUID.randomUUID());				
		root.addItem(backgroundRectangle);	
		ColorRGBA baseColour = ColourUtils.colourConvert(ColourUtils.colorFromString(table));
		
		float topColourFactor = 0.3f;
		float bottomColourFactor = 0.1f;
		float fontScale = 0.7f;
		float currentY = 120f;
		float deltaY = 30f;
		
		ColorRGBA gradientTop = new ColorRGBA(baseColour.r * topColourFactor, baseColour.g * topColourFactor, baseColour.b * topColourFactor, 1f);
		ColorRGBA gradientBottom = new ColorRGBA(baseColour.r * bottomColourFactor, baseColour.g * bottomColourFactor, baseColour.b * bottomColourFactor, 1f);
		Gradient backgroundGradient = new Gradient(gradientTop, gradientBottom, GradientDirection.VERTICAL);
		backgroundRectangle.setGradientBackground(backgroundGradient);
		backgroundRectangle.setSize(450, 350);
		
		correctExpressionLabel = contentFactory.create(IMutableLabel.class, "correct", UUID.randomUUID());
		root.addItem(correctExpressionLabel);
		incorrectExpressionLabel = contentFactory.create(IMutableLabel.class, "incorrect", UUID.randomUUID());
		root.addItem(incorrectExpressionLabel);
		bracketUseLabel = contentFactory.create(IMutableLabel.class, "bracketuse", UUID.randomUUID());
		root.addItem(bracketUseLabel);
		usedAllOperators = contentFactory.create(IMutableLabel.class, "usedAllOperators", UUID.randomUUID());
		root.addItem(usedAllOperators);
		moreThanOneOperator = contentFactory.create(IMutableLabel.class, "usedAllOperators", UUID.randomUUID());
		root.addItem(moreThanOneOperator);

		correctExpressionLabel.setFont(FontUtil.getFont(FontColour.White));
		correctExpressionLabel.setText("Correct:");
		correctExpressionLabel.setRelativeScale(fontScale);		
		correctExpressionLabel.setRelativeLocation(new Vector2f(0, currentY));
		currentY -= deltaY;
		
		incorrectExpressionLabel.setFont(FontUtil.getFont(FontColour.White));
		incorrectExpressionLabel.setText("Inorrect:");
		incorrectExpressionLabel.setRelativeScale(fontScale);
		incorrectExpressionLabel.setRelativeLocation(new Vector2f(0, currentY));
		currentY -= deltaY;
		
		bracketUseLabel.setFont(FontUtil.getFont(FontColour.White));
		bracketUseLabel.setText("Correct bracket use:");
		bracketUseLabel.setRelativeScale(fontScale);
		bracketUseLabel.setRelativeLocation(new Vector2f(0, currentY));		
		currentY -= deltaY;

		usedAllOperators.setFont(FontUtil.getFont(FontColour.White));
		usedAllOperators.setText("Used all operators:");
		usedAllOperators.setRelativeScale(fontScale);
		usedAllOperators.setRelativeLocation(new Vector2f(0, currentY));
		currentY -= deltaY;

		moreThanOneOperator.setFont(FontUtil.getFont(FontColour.White));
		moreThanOneOperator.setText("More than one operator:");
		moreThanOneOperator.setRelativeScale(fontScale);
		moreThanOneOperator.setRelativeLocation(new Vector2f(0, currentY));
		
		root.getZOrderManager().sendToBottom(backgroundRectangle);
//		root.getZOrderManager().bringToTop(bracketUseLabel);
//		root.getZOrderManager().bringToTop(correctExpressionLabel);
//		root.getZOrderManager().bringToTop(incorrectExpressionLabel);

	}

	public void setCorrectExpressionCount(int correctExpressionCount) {
		correctExpressionLabel.setText("Correct: " + correctExpressionCount);
	}

	public void setIncorrectExpressionCount(int incorrectExpressionCount) {
		incorrectExpressionLabel.setText("Incorrect: " + incorrectExpressionCount);
	}

	public void setBracketUseCount(int count) {
		bracketUseLabel.setText("Correct bracket use: " + count);
	}
	
	public void setUsedAllOperators(boolean hasUsedAllOperators) {
		String str = "Used all operators?: ";
		if(hasUsedAllOperators) str += " yes";
		if(!hasUsedAllOperators) str += " no";
		usedAllOperators.setText(str);
	}

	public void setMoreThanOneOperatorCount(int moreThanOneOperatorCount) {
		moreThanOneOperator.setText("More than one operator: " + moreThanOneOperatorCount);
	}
}
