package multiplicity3.appsystem;

import multiplicity3.input.MultiTouchInputComponent;

/**
 * The Interface IMultiplicityApp.
 */
public interface IMultiplicityApp
{

	/**
	 * Gets the friendly app name.
	 *
	 * @return the friendly app name
	 */
	String getFriendlyAppName();

	/**
	 * On destroy.
	 */
	void onDestroy();

	/**
	 * Should start.
	 *
	 * @param input
	 *            the input
	 * @param iqo
	 *            the iqo
	 */
	void shouldStart(MultiTouchInputComponent input, IQueueOwner iqo);

	/**
	 * Should stop.
	 */
	void shouldStop();
}
