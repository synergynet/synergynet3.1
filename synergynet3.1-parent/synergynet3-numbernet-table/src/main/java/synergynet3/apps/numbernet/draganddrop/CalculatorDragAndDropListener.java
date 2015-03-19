package synergynet3.apps.numbernet.draganddrop;

import java.util.logging.Logger;

import synergynet3.apps.numbernet.controller.numbernettable.CalculatorEventProcessor;
import synergynet3.apps.numbernet.ui.calculator.Calculator;
import multiplicity3.csys.draganddrop.DragAndDropListener;
import multiplicity3.csys.items.item.IItem;

public class CalculatorDragAndDropListener implements DragAndDropListener {
	private static final Logger log = Logger.getLogger(CalculatorDragAndDropListener.class.getName());
	
	protected Calculator calculator;
	protected CalculatorEventProcessor eventProcessor;

	public CalculatorDragAndDropListener(Calculator c, CalculatorEventProcessor controller) {
		this.eventProcessor = controller;
		this.calculator = c;		
	}
	
	@Override
	public void itemDraggedAndDropped(IItem itemDropped, IItem onto, int indexOfDrop) {
		if(itemDropped == null) {
			log.warning("No actual dropping occurred!");
			return;
		}
		
		if(!itemDropped.isVisible()) {
			log.fine("Item is not visible, no further action.");
			return;
		}
		
		log.fine("Item drag and dropped: " + itemDropped + " onto " + onto + " at index " + indexOfDrop);
		eventProcessor.calculatorDragAndDropEvent(calculator, itemDropped, onto, indexOfDrop);		
	}	
}
