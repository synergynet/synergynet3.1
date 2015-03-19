package synergynet3.apps.numbernet.ui.expression;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import multiplicity3.csys.behaviours.RotateTranslateScaleBehaviour;
import multiplicity3.csys.factory.ContentTypeNotBoundException;
import multiplicity3.csys.items.container.IContainer;
import multiplicity3.csys.items.item.IItem;
import multiplicity3.csys.stage.IStage;
import synergynet3.additionalitems.interfaces.ITextbox;
import synergynet3.apps.numbernet.controller.numbernettable.CalculatorCollectionManager;
import synergynet3.apps.numbernet.model.ExpressionSession;
import synergynet3.apps.numbernet.model.ExpressionSessionScores;
import synergynet3.apps.numbernet.model.IExpressionSessionChangeListener;
import synergynet3.apps.numbernet.ui.animation.MoveItem;
import synergynet3.apps.numbernet.ui.calculator.Calculator;
import synergynet3.apps.numbernet.ui.expression.scores.ScoresUI;
import synergynet3.cluster.sharedmemory.DistributedMap;
import synergynet3.fonts.FontColour;
import synergynet3.projector.network.ProjectorTransferUtilities;
import synergynet3.web.apps.numbernet.comms.table.TargetMaps;
import synergynet3.web.apps.numbernet.shared.Expression;
import synergynet3.web.apps.numbernet.shared.ExpressionVisibleProperties;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;

public class ExpressionDisplay implements IExpressionSessionChangeListener {
	private static final String STRING_TARGET_NOT_SET = "(please wait)";

	private static final Logger log = Logger.getLogger(ExpressionDisplay.class.getName());

	private IStage stage;

	private ExpressionSession session;
	private ITextbox titleLabel;
	private ExpressionVisualRepresentationFactory expressionVisualFactory;
	private CalculatorCollectionManager calculatorCollectionManager;
	private ConcurrentHashMap<String,ExpressionAndExpressionVisual> expressionTextToExpressionAndVisual;
	private ScoresUI scoresUI;
	private String tableID;

	public ExpressionDisplay(String tableID, IStage stage, IContainer container, CalculatorCollectionManager calculatorCollectionManager) throws ContentTypeNotBoundException {
		this.tableID = tableID;
		this.stage = stage;
		this.calculatorCollectionManager = calculatorCollectionManager;
		this.expressionVisualFactory = new ExpressionVisualRepresentationFactory(container, stage);
		this.expressionTextToExpressionAndVisual = new ConcurrentHashMap<String,ExpressionAndExpressionVisual>();
		addTitleLabel();
	}

	public Double getCurrentTarget() {
		if(session == null) return null;
		return session.getTargetValue();
	}

	public void setScoresDisplayVisibility(boolean shouldBeVisible) throws ContentTypeNotBoundException {
		if(shouldBeVisible) {
			getScoresUI().setVisible(true);		
			fetchAndUpdateScores();
		}else{
			if(scoresUI != null) {
				getScoresUI().setVisible(false);
			}
		}
	}

	public void fetchAndUpdateScores() {
		ExpressionSessionScores scores = session.getScoresForTable(tableID);
		updateScores(scores.getCorrect(), scores.getIncorrect());
	}

	public void updateScores(int correct, int incorrect) {
		if(scoresUI != null) {
			scoresUI.setScores(correct, incorrect);
		}
	}

	public void setExpressionSession(ExpressionSession session) {
		if(session == null) return;
		if(this.session == session) return;

		if(this.session != null) this.session.removeChangeListener(this);

		// clear current expressions
		this.session = session;
		this.session.addChangeListener(this);
		// add new expressions
		try {
			updateTitle(getLabelText());
		} catch (ContentTypeNotBoundException e) {
			e.printStackTrace();
		}
	}

	public String getLabelText() {
		if(session.getCurrentTargetValueAsString() == null) {
			return STRING_TARGET_NOT_SET;
		}else{
			return "Calculate " + session.getCurrentTargetValueAsString();
		}
	}

	@Override
	public void expressionAddedFromCalculator(Expression e) {
		log.fine("Adding expression " + e + " from calculator");
		Calculator calculator = getCalculatorForExpression(e);
		try {
			IContainer expressionVisual = expressionVisualFactory.createExpressionVisualRepresentation(e, 
					calculator.getRotation(), calculator.getDisplayItem().getWorldLocation());

			Vector2f from = calculator.getDisplayItem().getWorldLocation(calculator.getDisplayItem().getRelativeLocation());
			Vector2f to = calculator.getDisplayItem().getWorldLocation(calculator.getDisplayItem().getRelativeLocation().add(new Vector2f(0f, 65f)));

			stage.getAnimationSystem().add(new MoveItem(from, to, 1, expressionVisual, true));
			addExpressionVisualForExpression(expressionVisual, e);

		} catch (ContentTypeNotBoundException e1) {
			log.log(Level.WARNING, "Fault in adding expression.", e1);
		}
		fetchAndUpdateScores();
	}

	@Override
	public void expressionAddedFromNetwork(Expression e) {
		log.fine("Adding expression from network " + e);
		IContainer expressionVisual;
		try {
			expressionVisual = expressionVisualFactory.createExpressionVisualRepresentation(e, 0, new Vector2f(0,0));
			updateExpressionVisualWithDistributedVisualProperties(e, expressionVisual);
			addExpressionVisualForExpression(expressionVisual, e);
		} catch (ContentTypeNotBoundException e1) {			
			log.log(Level.WARNING, "Fault in adding expression.", e1);
		}
		fetchAndUpdateScores();
	}



	private void updateExpressionVisualWithDistributedVisualProperties(Expression e,
			IContainer expressionVisual) {
		DistributedMap<String, ExpressionVisibleProperties> evpmap = TargetMaps.get().getExpressionVisiblePropertiesMapForTarget(this.getCurrentTarget());
		ExpressionVisibleProperties evp = evpmap.get(e.getId());
		if(evp != null) {
			expressionVisual.setWorldLocation(new Vector2f(evp.x, evp.y));
			expressionVisual.setRelativeRotation(evp.rot);
		}
	}

	@Override
	public void allExpressionsRemoved() {
		for(ExpressionAndExpressionVisual ev : expressionTextToExpressionAndVisual.values()) {
			ProjectorTransferUtilities.get().removeFromTransferableContents(ev.getExpressionVisual());
			stage.removeItem(ev.getExpressionVisual());
		}
		expressionTextToExpressionAndVisual.clear();
		fetchAndUpdateScores();
	}

	@Override
	public void expressionRemoved(Expression expression) {
		IContainer container = getExpressionVisualForExpression(expression);
		if(container == null) return;
		log.fine("Attempt to remove item from stage on thread " + Thread.currentThread().getName());
		ProjectorTransferUtilities.get().removeFromTransferableContents(container);
		stage.removeItem(container);
		expressionTextToExpressionAndVisual.remove(expression.getExpression());
		fetchAndUpdateScores();
	}

	@Override
	public void targetChanged(Double newValue) {
		try {
			updateTitle(getLabelText());
		} catch (ContentTypeNotBoundException e) {
			e.printStackTrace();
		}
	}

	public Map<String, ExpressionVisibleProperties> getCurrentIDToExpressionVisibleMap() {
		Map<String,ExpressionVisibleProperties> map = new HashMap<String,ExpressionVisibleProperties>();
		for(ExpressionAndExpressionVisual ev : expressionTextToExpressionAndVisual.values()) {
			ExpressionVisibleProperties properties = new ExpressionVisibleProperties();
			properties.id = ev.expression.getId();
			properties.x = ev.expressionVisual.getWorldLocation().x;
			properties.y = ev.expressionVisual.getWorldLocation().y;
			properties.rot = ev.expressionVisual.getRelativeRotation();
			map.put(properties.id, properties);
		}
		return map;
	}

	public void updateExpressionsVisualProperties(
			Map<String, ExpressionVisibleProperties> visualProperties) {

		for(String key : visualProperties.keySet()) {
			ExpressionVisibleProperties evp = visualProperties.get(key);
			IContainer expressionContainer = getExpressionVisualForExpressionID(evp.id);
			updateExpressionContainerWithExpressionVisibleProperties(expressionContainer, evp);
		}
	}

	public void setExpressionVisibilityCorrect(boolean showCorrect) {
		for(ExpressionAndExpressionVisual ev : expressionTextToExpressionAndVisual.values()) {
			boolean expressionIsCorrect = ev.getExpression().isCorrect();
			if(ev.getExpression().getCreatedOnTable().equals(tableID) && expressionIsCorrect) {
				ev.getExpressionVisual().setVisible(showCorrect);
			}
		}
	}

	public void setExpressionVisibilityIncorrect(boolean showIncorrect) {
		for(ExpressionAndExpressionVisual ev : expressionTextToExpressionAndVisual.values()) {
			boolean expressionIsCorrect = ev.getExpression().isCorrect();
			if(ev.getExpression().getCreatedOnTable().equals(tableID) && !expressionIsCorrect) {
				ev.getExpressionVisual().setVisible(showIncorrect);
			}
		}
	}

	public void setOthersExpressionVisibilityCorrect(boolean showCorrect) {
		for(ExpressionAndExpressionVisual ev : expressionTextToExpressionAndVisual.values()) {
			boolean expressionIsCorrect = ev.getExpression().isCorrect();
			if(!ev.getExpression().getCreatedOnTable().equals(tableID) && expressionIsCorrect) {
				ev.getExpressionVisual().setVisible(showCorrect);
			}
		}
	}

	public void setOthersExpressionVisibilityIncorrect(boolean showIncorrect) {
		for(ExpressionAndExpressionVisual ev : expressionTextToExpressionAndVisual.values()) {
			boolean expressionIsCorrect = ev.getExpression().isCorrect();
			if(!ev.getExpression().getCreatedOnTable().equals(tableID) && !expressionIsCorrect) {
				ev.getExpressionVisual().setVisible(showIncorrect);
			}
		}
	}

	public Collection<IContainer> getAllItems() {
		List<IContainer> list = new ArrayList<IContainer>();
		for(ExpressionAndExpressionVisual ev : expressionTextToExpressionAndVisual.values()) {
			list.add(ev.expressionVisual);
		}
		return list;
	}

	public IContainer getExpressionContainerForExpression(Expression ex) {
		ExpressionAndExpressionVisual eev = expressionTextToExpressionAndVisual.get(ex.getExpression());
		if(eev != null) {
			return eev.getExpressionVisual();
		}
		return null;
	}

	// *********************************
	// ******** private ****************
	// *********************************	

	private void addTitleLabel() throws ContentTypeNotBoundException {
		updateTitle(STRING_TARGET_NOT_SET);
	}
	
	private void updateTitle(String text) throws ContentTypeNotBoundException{
		if (titleLabel != null){
			stage.removeItem(titleLabel);
			ProjectorTransferUtilities.get().removeFromTransferableContents(titleLabel);
		}
		titleLabel = stage.getContentFactory().create(ITextbox.class, text, UUID.randomUUID());
		titleLabel.setMovable(false);
		ColorRGBA invis = new ColorRGBA(0,0,0,0);
		titleLabel.setColours(invis, invis, FontColour.White);
		titleLabel.setText(text, stage);			
		titleLabel.setRelativeLocation(new Vector2f(0, (stage.getDisplayHeight()/2) - 60));
		
		ProjectorTransferUtilities.get().addToTransferableContents(titleLabel, stage.getDisplayWidth(), 60f, tableID + "-title");

		stage.addItem(titleLabel);
	}

	private void addExpressionVisualForExpression(IContainer expressionVisual,
			Expression e) {	
		ExpressionAndExpressionVisual expressionAndVisual = new ExpressionAndExpressionVisual(e, expressionVisual);
		expressionTextToExpressionAndVisual.put(e.getExpression(), expressionAndVisual);
	}

	private Calculator getCalculatorForExpression(Expression e) {
		return calculatorCollectionManager.getCalculatorForUser(e.getCreatedBy());
	}

	private IContainer getExpressionVisualForExpression(Expression expression) {
		for(ExpressionAndExpressionVisual ev : expressionTextToExpressionAndVisual.values()) {
			log.info("comparing " + ev.getExpression().getFullString() + " with " + expression.getFullString());
			if(ev.getExpression().equals(expression)) {
				return ev.getExpressionVisual();
			}
		}
		log.info("Did not find an expression visual for " + expression);
		return null;
	}

	private IContainer getExpressionVisualForExpressionID(String id) {
		for(ExpressionAndExpressionVisual ev : expressionTextToExpressionAndVisual.values()) {
			if(ev.getExpression().getId().equals(id)) {
				return ev.getExpressionVisual();
			}
		}
		return null;
	}


	private ScoresUI getScoresUI() throws ContentTypeNotBoundException {
		if(scoresUI == null) {
			scoresUI = new ScoresUI(stage);
			IItem container = scoresUI.getContentItem();
			container.setRelativeLocation(new Vector2f(-460, 350));
			stage.addItem(container);
		}
		return scoresUI;
	}		

	private static class ExpressionAndExpressionVisual
	{
		private Expression expression;
		private IContainer expressionVisual;

		public ExpressionAndExpressionVisual(Expression expression, IContainer expressionVisual) {
			this.setExpression(expression);
			this.setExpressionVisual(expressionVisual);
		}

		public void setExpression(Expression expression) {
			this.expression = expression;
		}

		public Expression getExpression() {
			return expression;
		}

		public void setExpressionVisual(IContainer expressionVisual) {
			this.expressionVisual = expressionVisual;
		}

		public IContainer getExpressionVisual() {
			return expressionVisual;
		}
	}

	private void updateExpressionContainerWithExpressionVisibleProperties(
			IContainer expressionContainer, ExpressionVisibleProperties evp) {

		RotateTranslateScaleBehaviour rts = expressionContainer.getBehaviours(RotateTranslateScaleBehaviour.class).get(0);
		if(rts != null && !rts.isActive()) {		
			expressionContainer.setWorldLocation(new Vector2f(evp.x, evp.y));
			expressionContainer.setRelativeRotation(evp.rot);
		}
	}





}
