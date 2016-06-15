package synergynet3.apps.numbernet.ui.calculator;

/**
 * The listener interface for receiving ICalculatorEvent events. The class that
 * is interested in processing a ICalculatorEvent event implements this
 * interface, and the object created with that class is registered with a
 * component using the component's
 * <code>addICalculatorEventListener<code> method. When
 * the ICalculatorEvent event occurs, that object's appropriate
 * method is invoked.
 *
 * @see ICalculatorEventEvent
 */
public interface ICalculatorEventListener
{

	/**
	 * Character added.
	 *
	 * @param character
	 *            the character
	 * @param calculator
	 *            the calculator
	 * @param currentDisplay
	 *            the current display
	 */
	public void characterAdded(char character, Calculator calculator, String currentDisplay);

	/**
	 * Character removed.
	 *
	 * @param calculator
	 *            the calculator
	 * @param text
	 *            the text
	 */
	public void characterRemoved(Calculator calculator, String text);

	/**
	 * Enter key pressed.
	 *
	 * @param calculator
	 *            the calculator
	 * @param currentDisplay
	 *            the current display
	 */
	public void enterKeyPressed(Calculator calculator, String currentDisplay);
}
