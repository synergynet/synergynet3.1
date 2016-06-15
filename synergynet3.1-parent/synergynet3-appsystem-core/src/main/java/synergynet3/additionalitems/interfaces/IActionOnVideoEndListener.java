package synergynet3.additionalitems.interfaces;

/**
 * The listener interface for receiving IActionOnVideoEnd events. The class that
 * is interested in processing a IActionOnVideoEnd event implements this
 * interface, and the object created with that class is registered with a
 * component using the component's
 * <code>addIActionOnVideoEndListener<code> method. When
 * the IActionOnVideoEnd event occurs, that object's appropriate
 * method is invoked.
 *
 * @see IActionOnVideoEndEvent
 */
public interface IActionOnVideoEndListener
{

	/**
	 * On video end.
	 */
	public void onVideoEnd();
}
