package multiplicity3.input.tuio;

import java.util.prefs.Preferences;

public class TUIOPrefsItem {
	
	private static Preferences prefs = Preferences.userNodeForPackage(TUIOPrefsItem.class);
	private static final String TUIO_PORT = "TUIO_PORT";
	
	public TUIOPrefsItem() {}
	
	public void setTuioPort(int port) {
		prefs.putInt(TUIO_PORT, port);
	}

	public int getTuioPort() {			
		return prefs.getInt(TUIO_PORT, 3333);
	}
	
}
