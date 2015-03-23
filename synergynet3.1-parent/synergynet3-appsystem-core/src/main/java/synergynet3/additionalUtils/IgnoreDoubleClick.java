package synergynet3.additionalUtils;

import multiplicity3.input.events.MultiTouchCursorEvent;

/**
 * The Class IgnoreDoubleClick.
 */
public class IgnoreDoubleClick {

	/** The time out. */
	private int timeOut = 1000;

	/** The clicked. */
	boolean clicked = false;

	/**
	 * Instantiates a new ignore double click.
	 */
	public IgnoreDoubleClick() {
	}

	/**
	 * Instantiates a new ignore double click.
	 *
	 * @param timeOut the time out
	 */
	public IgnoreDoubleClick(int timeOut) {
		this.timeOut = timeOut;
	}

	/**
	 * Click.
	 *
	 * @param event the event
	 */
	public void click(MultiTouchCursorEvent event) {
		if (!clicked) {
			onAction(event);
			clicked = true;
			Thread timeoutThread = new Thread(new Runnable() {
				public void run() {
					try {
						Thread.sleep(timeOut);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					clicked = false;
				}
			});
			timeoutThread.start();
		}
	}

	/**
	 * On action.
	 *
	 * @param event the event
	 */
	public void onAction(MultiTouchCursorEvent event) {
	}

}
