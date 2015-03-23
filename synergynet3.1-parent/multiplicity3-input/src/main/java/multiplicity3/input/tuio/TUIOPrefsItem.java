package multiplicity3.input.tuio;

import java.util.prefs.Preferences;

/**
 * The Class TUIOPrefsItem.
 */
public class TUIOPrefsItem {

	/** The prefs. */
	private static Preferences prefs = Preferences
			.userNodeForPackage(TUIOPrefsItem.class);

	/** The Constant TUIO_PORT. */
	private static final String TUIO_PORT = "TUIO_PORT";

	/**
	 * Instantiates a new TUIO prefs item.
	 */
	public TUIOPrefsItem() {
	}

	/**
	 * Gets the tuio port.
	 *
	 * @return the tuio port
	 */
	public int getTuioPort() {
		return prefs.getInt(TUIO_PORT, 3333);
	}

	/**
	 * Sets the tuio port.
	 *
	 * @param port the new tuio port
	 */
	public void setTuioPort(int port) {
		prefs.putInt(TUIO_PORT, port);
	}

}
