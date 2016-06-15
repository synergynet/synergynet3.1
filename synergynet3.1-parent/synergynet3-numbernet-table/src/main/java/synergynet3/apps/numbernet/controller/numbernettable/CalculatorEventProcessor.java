package synergynet3.apps.numbernet.controller.numbernettable;

import java.util.logging.Logger;

import multiplicity3.csys.items.container.IContainer;
import multiplicity3.csys.items.item.IItem;
import multiplicity3.csys.items.mutablelabel.IMutableLabel;
import synergynet3.apps.numbernet.model.ExpressionFactory;
import synergynet3.apps.numbernet.model.ExpressionSession;
import synergynet3.apps.numbernet.ui.calculator.Calculator;
import synergynet3.apps.numbernet.ui.calculator.ICalculatorEventListener;
import synergynet3.apps.numbernet.validation.IValidationChecker;
import synergynet3.apps.numbernet.validation.IValidationChecker.ValidationResult;
import synergynet3.web.apps.numbernet.shared.Expression;

/**
 * The Class CalculatorEventProcessor.
 */
public class CalculatorEventProcessor implements ICalculatorEventListener
{

	/** The Constant log. */
	private static final Logger log = Logger.getLogger(CalculatorEventProcessor.class.getName());

	/** The expression session. */
	private ExpressionSession expressionSession;

	/** The table id. */
	private String tableID;

	/** The validation checker. */
	private IValidationChecker validationChecker;

	/** The allow editing of others policy. */
	boolean allowEditingOfOthersPolicy = false;

	/**
	 * Instantiates a new calculator event processor.
	 *
	 * @param tableID
	 *            the table id
	 * @param validationChecker
	 *            the validation checker
	 */
	public CalculatorEventProcessor(String tableID, IValidationChecker validationChecker)
	{
		this.tableID = tableID;
		this.validationChecker = validationChecker;
	}

	/**
	 * Calculator drag and drop event.
	 *
	 * @param calculator
	 *            the calculator
	 * @param itemDropped
	 *            the item dropped
	 * @param onto
	 *            the onto
	 * @param indexOfDrop
	 *            the index of drop
	 */
	public void calculatorDragAndDropEvent(Calculator calculator, IItem itemDropped, IItem onto, int indexOfDrop)
	{
		log.fine("Calculator drag and drop occurred.");
		if (!calculator.isVisible())
		{
			log.fine("attempt to drop on an invisible calculator ignored.");
			return;
		}

		log.fine("Item dropped: " + itemDropped.getClass().getName());
		IContainer container = getContainerForDroppedItem(itemDropped);
		String expressionText = getExpressionTextForExpressionContainer(container);

		if (expressionText == null)
		{
			log.fine("Could not find the text for the expression.");
			return;
		}

		Expression expressionObject = expressionSession.getExpressionForExpressionString(expressionText);
		if (!expressionObject.getCreatedOnTable().equals(tableID) && !allowEditingOfOthersPolicy)
		{
			log.fine("Attempt to edit other table's expression ignored.");
			return;
		}

		log.fine("Setting calculator display to be: " + expressionText);
		calculator.setTextForCalculatorDisplay(expressionText);
		expressionSession.removeExpressionWithText(expressionText);
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.apps.numbernet.ui.calculator.ICalculatorEventListener#
	 * characterAdded(char, synergynet3.apps.numbernet.ui.calculator.Calculator,
	 * java.lang.String)
	 */
	@Override
	public void characterAdded(char character, Calculator calculator, String currentDisplay)
	{
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.apps.numbernet.ui.calculator.ICalculatorEventListener#
	 * characterRemoved(synergynet3.apps.numbernet.ui.calculator.Calculator,
	 * java.lang.String)
	 */
	@Override
	public void characterRemoved(Calculator calculator, String text)
	{
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.apps.numbernet.ui.calculator.ICalculatorEventListener#
	 * enterKeyPressed(synergynet3.apps.numbernet.ui.calculator.Calculator,
	 * java.lang.String)
	 */
	@Override
	public void enterKeyPressed(Calculator calculator, String currentDisplay)
	{
		if (expressionSession == null)
		{
			log.warning("Calculator event processor is not aware of the current expression session");
			return;
		}
		if (expressionSession.getTargetValue() != null)
		{
			currentDisplay = currentDisplay.trim();
			log.fine("Checking " + currentDisplay + " validitiy.");
			ValidationResult validationResult = validationChecker.isValid(currentDisplay);
			calculator.setDisplayStyle(validationResult);
			log.fine("Result: " + validationResult);

			if ((validationResult == ValidationResult.VALID) || (validationResult == ValidationResult.INVALID))
			{
				log.fine("Should add to expressions");
				double currentTarget = expressionSession.getTargetValue();
				Expression expression = ExpressionFactory.createExpressionFromStringAndCalculator(currentDisplay, calculator, tableID, currentTarget, false);
				expressionSession.addExpressionCreatedByCalculator(expression);
				calculator.setTextForCalculatorDisplay("");
			}
		}
		else
		{
			log.fine("No target value set.");
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
		this.expressionSession = session;
	}

	/**
	 * Gets the container for dropped item.
	 *
	 * @param itemDropped
	 *            the item dropped
	 * @return the container for dropped item
	 */
	private IContainer getContainerForDroppedItem(IItem itemDropped)
	{
		return (IContainer) itemDropped.getParentItem();
	}

	/**
	 * Gets the expression text for expression container.
	 *
	 * @param container
	 *            the container
	 * @return the expression text for expression container
	 */
	private String getExpressionTextForExpressionContainer(IContainer container)
	{
		for (int i = 0; i < container.getChildItems().size(); i++)
		{
			IItem child = container.getChildItems().get(i);
			if (child instanceof IMutableLabel)
			{
				IMutableLabel lbl = (IMutableLabel) child;
				return lbl.getText();
			}
		}
		return null;
	}
}
