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

public class CalculatorEventProcessor implements ICalculatorEventListener {
	private static final Logger log = Logger.getLogger(CalculatorEventProcessor.class.getName());
	
	private IValidationChecker validationChecker;
	private ExpressionSession expressionSession;

	private String tableID;
	boolean allowEditingOfOthersPolicy = false;

	public CalculatorEventProcessor(String tableID, IValidationChecker validationChecker) {
		this.tableID = tableID;
		this.validationChecker = validationChecker;
	}
	
	public void setExpressionSession(ExpressionSession session) {
		this.expressionSession = session;
	}

	@Override
	public void enterKeyPressed(Calculator calculator, String currentDisplay) {
		if(expressionSession == null) {
			log.warning("Calculator event processor is not aware of the current expression session");
			return;
		}
		if(expressionSession.getTargetValue() != null){
			currentDisplay = currentDisplay.trim();
			log.fine("Checking " + currentDisplay + " validitiy.");
			ValidationResult validationResult = validationChecker.isValid(currentDisplay);
			calculator.setDisplayStyle(validationResult);
			log.fine("Result: " + validationResult);
			
			if(validationResult == ValidationResult.VALID || validationResult == ValidationResult.INVALID) {
				log.fine("Should add to expressions");
				double currentTarget = expressionSession.getTargetValue();
				Expression expression = ExpressionFactory.createExpressionFromStringAndCalculator(currentDisplay, calculator, tableID, currentTarget, false);			
				expressionSession.addExpressionCreatedByCalculator(expression);
				calculator.setTextForCalculatorDisplay("");
			}
		}else{
			log.fine("No target value set.");
		}
	}



	public void calculatorDragAndDropEvent(Calculator calculator, IItem itemDropped, IItem onto,
			int indexOfDrop) {
		log.fine("Calculator drag and drop occurred.");
		if(!calculator.isVisible()) {
			log.fine("attempt to drop on an invisible calculator ignored.");
			return;
		}
		
		log.fine("Item dropped: " + itemDropped.getClass().getName());		
		IContainer container = getContainerForDroppedItem(itemDropped);
		String expressionText = getExpressionTextForExpressionContainer(container);
		
		if(expressionText == null) {
			log.fine("Could not find the text for the expression.");
			return;
		}
		
		Expression expressionObject = expressionSession.getExpressionForExpressionString(expressionText);
		if(!expressionObject.getCreatedOnTable().equals(tableID) && !allowEditingOfOthersPolicy) {
			log.fine("Attempt to edit other table's expression ignored.");
			return;
		}
		

		log.fine("Setting calculator display to be: " + expressionText);
		calculator.setTextForCalculatorDisplay(expressionText);
		expressionSession.removeExpressionWithText(expressionText);
	}
	
	private String getExpressionTextForExpressionContainer(IContainer container) {
		for(int i = 0; i < container.getChildItems().size(); i++) {
			IItem child = container.getChildItems().get(i);
			if(child instanceof IMutableLabel) {
				IMutableLabel lbl = (IMutableLabel) child;
				return lbl.getText();
			}
		}
		return null;
	}

	private IContainer getContainerForDroppedItem(IItem itemDropped) {
		return (IContainer) itemDropped.getParentItem();
	}

	@Override
	public void characterAdded(char character, Calculator calculator, String currentDisplay) {}

	@Override
	public void characterRemoved(Calculator calculator, String text) {}
}
