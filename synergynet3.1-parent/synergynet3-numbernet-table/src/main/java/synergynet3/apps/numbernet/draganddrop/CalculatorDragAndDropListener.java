package synergynet3.apps.numbernet.draganddrop;

import java.util.logging.Logger;

import multiplicity3.csys.draganddrop.DragAndDropListener;
import multiplicity3.csys.items.item.IItem;
import synergynet3.apps.numbernet.controller.numbernettable.CalculatorEventProcessor;
import synergynet3.apps.numbernet.ui.calculator.Calculator;

/**
 * The listener interface for receiving calculatorDragAndDrop events. The class
 * that is interested in processing a calculatorDragAndDrop event implements
 * this interface, and the object created with that class is registered with a
 * component using the component's
 * <code>addCalculatorDragAndDropListener<code> method. When
 * the calculatorDragAndDrop event occurs, that object's appropriate
 * method is invoked.
 *
 * @see CalculatorDragAndDropEvent
 */
public class CalculatorDragAndDropListener implements DragAndDropListener
{

	/** The Constant log. */
	private static final Logger log = Logger.getLogger(CalculatorDragAndDropListener.class.getName());

	/** The calculator. */
	protected Calculator calculator;

	/** The event processor. */
	protected CalculatorEventProcessor eventProcessor;

	/**
	 * Instantiates a new calculator drag and drop listener.
	 *
	 * @param c
	 *            the c
	 * @param controller
	 *            the controller
	 */
	public CalculatorDragAndDropListener(Calculator c, CalculatorEventProcessor controller)
	{
		this.eventProcessor = controller;
		this.calculator = c;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.csys.draganddrop.DragAndDropListener#itemDraggedAndDropped
	 * (multiplicity3.csys.items.item.IItem,
	 * multiplicity3.csys.items.item.IItem, int)
	 */
	@Override
	public void itemDraggedAndDropped(IItem itemDropped, IItem onto, int indexOfDrop)
	{
		if (itemDropped == null)
		{
			log.warning("No actual dropping occurred!");
			return;
		}

		if (!itemDropped.isVisible())
		{
			log.fine("Item is not visible, no further action.");
			return;
		}

		log.fine("Item drag and dropped: " + itemDropped + " onto " + onto + " at index " + indexOfDrop);
		eventProcessor.calculatorDragAndDropEvent(calculator, itemDropped, onto, indexOfDrop);
	}
}
