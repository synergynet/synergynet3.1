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

/**
 * The Class ExpressionDisplay.
 */
public class ExpressionDisplay implements IExpressionSessionChangeListener
{

	/**
	 * The Class ExpressionAndExpressionVisual.
	 */
	private static class ExpressionAndExpressionVisual
	{

		/** The expression. */
		private Expression expression;

		/** The expression visual. */
		private IContainer expressionVisual;

		/**
		 * Instantiates a new expression and expression visual.
		 *
		 * @param expression
		 *            the expression
		 * @param expressionVisual
		 *            the expression visual
		 */
		public ExpressionAndExpressionVisual(Expression expression, IContainer expressionVisual)
		{
			this.setExpression(expression);
			this.setExpressionVisual(expressionVisual);
		}

		/**
		 * Gets the expression.
		 *
		 * @return the expression
		 */
		public Expression getExpression()
		{
			return expression;
		}

		/**
		 * Gets the expression visual.
		 *
		 * @return the expression visual
		 */
		public IContainer getExpressionVisual()
		{
			return expressionVisual;
		}

		/**
		 * Sets the expression.
		 *
		 * @param expression
		 *            the new expression
		 */
		public void setExpression(Expression expression)
		{
			this.expression = expression;
		}

		/**
		 * Sets the expression visual.
		 *
		 * @param expressionVisual
		 *            the new expression visual
		 */
		public void setExpressionVisual(IContainer expressionVisual)
		{
			this.expressionVisual = expressionVisual;
		}
	}

	/** The Constant log. */
	private static final Logger log = Logger.getLogger(ExpressionDisplay.class.getName());

	/** The Constant STRING_TARGET_NOT_SET. */
	private static final String STRING_TARGET_NOT_SET = "(please wait)";

	/** The calculator collection manager. */
	private CalculatorCollectionManager calculatorCollectionManager;

	/** The expression text to expression and visual. */
	private ConcurrentHashMap<String, ExpressionAndExpressionVisual> expressionTextToExpressionAndVisual;

	/** The expression visual factory. */
	private ExpressionVisualRepresentationFactory expressionVisualFactory;

	/** The scores ui. */
	private ScoresUI scoresUI;

	/** The session. */
	private ExpressionSession session;

	/** The stage. */
	private IStage stage;

	/** The table id. */
	private String tableID;

	/** The title label. */
	private ITextbox titleLabel;

	/**
	 * Instantiates a new expression display.
	 *
	 * @param tableID
	 *            the table id
	 * @param stage
	 *            the stage
	 * @param container
	 *            the container
	 * @param calculatorCollectionManager
	 *            the calculator collection manager
	 * @throws ContentTypeNotBoundException
	 *             the content type not bound exception
	 */
	public ExpressionDisplay(String tableID, IStage stage, IContainer container, CalculatorCollectionManager calculatorCollectionManager) throws ContentTypeNotBoundException
	{
		this.tableID = tableID;
		this.stage = stage;
		this.calculatorCollectionManager = calculatorCollectionManager;
		this.expressionVisualFactory = new ExpressionVisualRepresentationFactory(container, stage);
		this.expressionTextToExpressionAndVisual = new ConcurrentHashMap<String, ExpressionAndExpressionVisual>();
		addTitleLabel();
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.apps.numbernet.model.IExpressionSessionChangeListener#
	 * allExpressionsRemoved()
	 */
	@Override
	public void allExpressionsRemoved()
	{
		for (ExpressionAndExpressionVisual ev : expressionTextToExpressionAndVisual.values())
		{
			ProjectorTransferUtilities.get().removeFromTransferableContents(ev.getExpressionVisual());
			stage.removeItem(ev.getExpressionVisual());
		}
		expressionTextToExpressionAndVisual.clear();
		fetchAndUpdateScores();
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.apps.numbernet.model.IExpressionSessionChangeListener#
	 * expressionAddedFromCalculator
	 * (synergynet3.web.apps.numbernet.shared.Expression)
	 */
	@Override
	public void expressionAddedFromCalculator(Expression e)
	{
		log.fine("Adding expression " + e + " from calculator");
		Calculator calculator = getCalculatorForExpression(e);
		try
		{
			IContainer expressionVisual = expressionVisualFactory.createExpressionVisualRepresentation(e, calculator.getRotation(), calculator.getDisplayItem().getWorldLocation());

			Vector2f from = calculator.getDisplayItem().getWorldLocation(calculator.getDisplayItem().getRelativeLocation());
			Vector2f to = calculator.getDisplayItem().getWorldLocation(calculator.getDisplayItem().getRelativeLocation().add(new Vector2f(0f, 65f)));

			stage.getAnimationSystem().add(new MoveItem(from, to, 1, expressionVisual, true));
			addExpressionVisualForExpression(expressionVisual, e);

		}
		catch (ContentTypeNotBoundException e1)
		{
			log.log(Level.WARNING, "Fault in adding expression.", e1);
		}
		fetchAndUpdateScores();
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.apps.numbernet.model.IExpressionSessionChangeListener#
	 * expressionAddedFromNetwork
	 * (synergynet3.web.apps.numbernet.shared.Expression)
	 */
	@Override
	public void expressionAddedFromNetwork(Expression e)
	{
		log.fine("Adding expression from network " + e);
		IContainer expressionVisual;
		try
		{
			expressionVisual = expressionVisualFactory.createExpressionVisualRepresentation(e, 0, new Vector2f(0, 0));
			updateExpressionVisualWithDistributedVisualProperties(e, expressionVisual);
			addExpressionVisualForExpression(expressionVisual, e);
		}
		catch (ContentTypeNotBoundException e1)
		{
			log.log(Level.WARNING, "Fault in adding expression.", e1);
		}
		fetchAndUpdateScores();
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.apps.numbernet.model.IExpressionSessionChangeListener#
	 * expressionRemoved(synergynet3.web.apps.numbernet.shared.Expression)
	 */
	@Override
	public void expressionRemoved(Expression expression)
	{
		IContainer container = getExpressionVisualForExpression(expression);
		if (container == null)
		{
			return;
		}
		log.fine("Attempt to remove item from stage on thread " + Thread.currentThread().getName());
		ProjectorTransferUtilities.get().removeFromTransferableContents(container);
		stage.removeItem(container);
		expressionTextToExpressionAndVisual.remove(expression.getExpression());
		fetchAndUpdateScores();
	}

	/**
	 * Fetch and update scores.
	 */
	public void fetchAndUpdateScores()
	{
		ExpressionSessionScores scores = session.getScoresForTable(tableID);
		updateScores(scores.getCorrect(), scores.getIncorrect());
	}

	/**
	 * Gets the all items.
	 *
	 * @return the all items
	 */
	public Collection<IContainer> getAllItems()
	{
		List<IContainer> list = new ArrayList<IContainer>();
		for (ExpressionAndExpressionVisual ev : expressionTextToExpressionAndVisual.values())
		{
			list.add(ev.expressionVisual);
		}
		return list;
	}

	/**
	 * Gets the current id to expression visible map.
	 *
	 * @return the current id to expression visible map
	 */
	public Map<String, ExpressionVisibleProperties> getCurrentIDToExpressionVisibleMap()
	{
		Map<String, ExpressionVisibleProperties> map = new HashMap<String, ExpressionVisibleProperties>();
		for (ExpressionAndExpressionVisual ev : expressionTextToExpressionAndVisual.values())
		{
			ExpressionVisibleProperties properties = new ExpressionVisibleProperties();
			properties.id = ev.expression.getId();
			properties.x = ev.expressionVisual.getWorldLocation().x;
			properties.y = ev.expressionVisual.getWorldLocation().y;
			properties.rot = ev.expressionVisual.getRelativeRotation();
			map.put(properties.id, properties);
		}
		return map;
	}

	/**
	 * Gets the current target.
	 *
	 * @return the current target
	 */
	public Double getCurrentTarget()
	{
		if (session == null)
		{
			return null;
		}
		return session.getTargetValue();
	}

	/**
	 * Gets the expression container for expression.
	 *
	 * @param ex
	 *            the ex
	 * @return the expression container for expression
	 */
	public IContainer getExpressionContainerForExpression(Expression ex)
	{
		ExpressionAndExpressionVisual eev = expressionTextToExpressionAndVisual.get(ex.getExpression());
		if (eev != null)
		{
			return eev.getExpressionVisual();
		}
		return null;
	}

	/**
	 * Gets the label text.
	 *
	 * @return the label text
	 */
	public String getLabelText()
	{
		if (session.getCurrentTargetValueAsString() == null)
		{
			return STRING_TARGET_NOT_SET;
		}
		else
		{
			return "Calculate " + session.getCurrentTargetValueAsString();
		}
	}

	/**
	 * Sets the expression session.
	 *
	 * @param session
	 *            the new expression session
	 */
	public void setExpressionSession(ExpressionSession session)
	{
		if (session == null)
		{
			return;
		}
		if (this.session == session)
		{
			return;
		}

		if (this.session != null)
		{
			this.session.removeChangeListener(this);
		}

		// clear current expressions
		this.session = session;
		this.session.addChangeListener(this);
		// add new expressions
		try
		{
			updateTitle(getLabelText());
		}
		catch (ContentTypeNotBoundException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Sets the expression visibility correct.
	 *
	 * @param showCorrect
	 *            the new expression visibility correct
	 */
	public void setExpressionVisibilityCorrect(boolean showCorrect)
	{
		for (ExpressionAndExpressionVisual ev : expressionTextToExpressionAndVisual.values())
		{
			boolean expressionIsCorrect = ev.getExpression().isCorrect();
			if (ev.getExpression().getCreatedOnTable().equals(tableID) && expressionIsCorrect)
			{
				ev.getExpressionVisual().setVisible(showCorrect);
			}
		}
	}

	/**
	 * Sets the expression visibility incorrect.
	 *
	 * @param showIncorrect
	 *            the new expression visibility incorrect
	 */
	public void setExpressionVisibilityIncorrect(boolean showIncorrect)
	{
		for (ExpressionAndExpressionVisual ev : expressionTextToExpressionAndVisual.values())
		{
			boolean expressionIsCorrect = ev.getExpression().isCorrect();
			if (ev.getExpression().getCreatedOnTable().equals(tableID) && !expressionIsCorrect)
			{
				ev.getExpressionVisual().setVisible(showIncorrect);
			}
		}
	}

	/**
	 * Sets the others expression visibility correct.
	 *
	 * @param showCorrect
	 *            the new others expression visibility correct
	 */
	public void setOthersExpressionVisibilityCorrect(boolean showCorrect)
	{
		for (ExpressionAndExpressionVisual ev : expressionTextToExpressionAndVisual.values())
		{
			boolean expressionIsCorrect = ev.getExpression().isCorrect();
			if (!ev.getExpression().getCreatedOnTable().equals(tableID) && expressionIsCorrect)
			{
				ev.getExpressionVisual().setVisible(showCorrect);
			}
		}
	}

	/**
	 * Sets the others expression visibility incorrect.
	 *
	 * @param showIncorrect
	 *            the new others expression visibility incorrect
	 */
	public void setOthersExpressionVisibilityIncorrect(boolean showIncorrect)
	{
		for (ExpressionAndExpressionVisual ev : expressionTextToExpressionAndVisual.values())
		{
			boolean expressionIsCorrect = ev.getExpression().isCorrect();
			if (!ev.getExpression().getCreatedOnTable().equals(tableID) && !expressionIsCorrect)
			{
				ev.getExpressionVisual().setVisible(showIncorrect);
			}
		}
	}

	/**
	 * Sets the scores display visibility.
	 *
	 * @param shouldBeVisible
	 *            the new scores display visibility
	 * @throws ContentTypeNotBoundException
	 *             the content type not bound exception
	 */
	public void setScoresDisplayVisibility(boolean shouldBeVisible) throws ContentTypeNotBoundException
	{
		if (shouldBeVisible)
		{
			getScoresUI().setVisible(true);
			fetchAndUpdateScores();
		}
		else
		{
			if (scoresUI != null)
			{
				getScoresUI().setVisible(false);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.apps.numbernet.model.IExpressionSessionChangeListener#
	 * targetChanged(java.lang.Double)
	 */
	@Override
	public void targetChanged(Double newValue)
	{
		try
		{
			updateTitle(getLabelText());
		}
		catch (ContentTypeNotBoundException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Update expressions visual properties.
	 *
	 * @param visualProperties
	 *            the visual properties
	 */
	public void updateExpressionsVisualProperties(Map<String, ExpressionVisibleProperties> visualProperties)
	{

		for (String key : visualProperties.keySet())
		{
			ExpressionVisibleProperties evp = visualProperties.get(key);
			IContainer expressionContainer = getExpressionVisualForExpressionID(evp.id);
			updateExpressionContainerWithExpressionVisibleProperties(expressionContainer, evp);
		}
	}

	/**
	 * Update scores.
	 *
	 * @param correct
	 *            the correct
	 * @param incorrect
	 *            the incorrect
	 */
	public void updateScores(int correct, int incorrect)
	{
		if (scoresUI != null)
		{
			scoresUI.setScores(correct, incorrect);
		}
	}

	// *********************************
	// ******** private ****************
	// *********************************

	/**
	 * Adds the expression visual for expression.
	 *
	 * @param expressionVisual
	 *            the expression visual
	 * @param e
	 *            the e
	 */
	private void addExpressionVisualForExpression(IContainer expressionVisual, Expression e)
	{
		ExpressionAndExpressionVisual expressionAndVisual = new ExpressionAndExpressionVisual(e, expressionVisual);
		expressionTextToExpressionAndVisual.put(e.getExpression(), expressionAndVisual);
	}

	/**
	 * Adds the title label.
	 *
	 * @throws ContentTypeNotBoundException
	 *             the content type not bound exception
	 */
	private void addTitleLabel() throws ContentTypeNotBoundException
	{
		updateTitle(STRING_TARGET_NOT_SET);
	}

	/**
	 * Gets the calculator for expression.
	 *
	 * @param e
	 *            the e
	 * @return the calculator for expression
	 */
	private Calculator getCalculatorForExpression(Expression e)
	{
		return calculatorCollectionManager.getCalculatorForUser(e.getCreatedBy());
	}

	/**
	 * Gets the expression visual for expression.
	 *
	 * @param expression
	 *            the expression
	 * @return the expression visual for expression
	 */
	private IContainer getExpressionVisualForExpression(Expression expression)
	{
		for (ExpressionAndExpressionVisual ev : expressionTextToExpressionAndVisual.values())
		{
			log.info("comparing " + ev.getExpression().getFullString() + " with " + expression.getFullString());
			if (ev.getExpression().equals(expression))
			{
				return ev.getExpressionVisual();
			}
		}
		log.info("Did not find an expression visual for " + expression);
		return null;
	}

	/**
	 * Gets the expression visual for expression id.
	 *
	 * @param id
	 *            the id
	 * @return the expression visual for expression id
	 */
	private IContainer getExpressionVisualForExpressionID(String id)
	{
		for (ExpressionAndExpressionVisual ev : expressionTextToExpressionAndVisual.values())
		{
			if (ev.getExpression().getId().equals(id))
			{
				return ev.getExpressionVisual();
			}
		}
		return null;
	}

	/**
	 * Gets the scores ui.
	 *
	 * @return the scores ui
	 * @throws ContentTypeNotBoundException
	 *             the content type not bound exception
	 */
	private ScoresUI getScoresUI() throws ContentTypeNotBoundException
	{
		if (scoresUI == null)
		{
			scoresUI = new ScoresUI(stage);
			IItem container = scoresUI.getContentItem();
			container.setRelativeLocation(new Vector2f(-460, 350));
			stage.addItem(container);
		}
		return scoresUI;
	}

	/**
	 * Update expression container with expression visible properties.
	 *
	 * @param expressionContainer
	 *            the expression container
	 * @param evp
	 *            the evp
	 */
	private void updateExpressionContainerWithExpressionVisibleProperties(IContainer expressionContainer, ExpressionVisibleProperties evp)
	{

		RotateTranslateScaleBehaviour rts = expressionContainer.getBehaviours(RotateTranslateScaleBehaviour.class).get(0);
		if ((rts != null) && !rts.isActive())
		{
			expressionContainer.setWorldLocation(new Vector2f(evp.x, evp.y));
			expressionContainer.setRelativeRotation(evp.rot);
		}
	}

	/**
	 * Update expression visual with distributed visual properties.
	 *
	 * @param e
	 *            the e
	 * @param expressionVisual
	 *            the expression visual
	 */
	private void updateExpressionVisualWithDistributedVisualProperties(Expression e, IContainer expressionVisual)
	{
		DistributedMap<String, ExpressionVisibleProperties> evpmap = TargetMaps.get().getExpressionVisiblePropertiesMapForTarget(this.getCurrentTarget());
		ExpressionVisibleProperties evp = evpmap.get(e.getId());
		if (evp != null)
		{
			expressionVisual.setWorldLocation(new Vector2f(evp.x, evp.y));
			expressionVisual.setRelativeRotation(evp.rot);
		}
	}

	/**
	 * Update title.
	 *
	 * @param text
	 *            the text
	 * @throws ContentTypeNotBoundException
	 *             the content type not bound exception
	 */
	private void updateTitle(String text) throws ContentTypeNotBoundException
	{
		if (titleLabel != null)
		{
			stage.removeItem(titleLabel);
			ProjectorTransferUtilities.get().removeFromTransferableContents(titleLabel);
		}
		titleLabel = stage.getContentFactory().create(ITextbox.class, text, UUID.randomUUID());
		titleLabel.setMovable(false);
		ColorRGBA invis = new ColorRGBA(0, 0, 0, 0);
		titleLabel.setColours(invis, invis, FontColour.White);
		titleLabel.setText(text, stage);
		titleLabel.setRelativeLocation(new Vector2f(0, (stage.getDisplayHeight() / 2) - 60));

		ProjectorTransferUtilities.get().addToTransferableContents(titleLabel, stage.getDisplayWidth(), 60f, tableID + "-title");

		stage.addItem(titleLabel);
	}

}
