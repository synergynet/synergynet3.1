package synergynet3.museum.table.settingsapp.general;

import java.util.prefs.Preferences;

import javax.swing.JPanel;


import multiplicity3.config.PreferencesItem;

public class GeneralConfigPrefsItem implements PreferencesItem {	

	private static final Preferences prefs = Preferences.userNodeForPackage(GeneralConfigPrefsItem.class);
	private static final String ADMIN_PIN = "ADMIN_PIN";
	private static final String CONTENT_FOLDER = "CONTENT_FOLDER";
	private static final String LOCATIONS = "LOCATIONS";
	private static final String USER_RECORDINGS = "USER_RECORDINGS";
	private static final String METRICS = "METRICS";
	private static final String METRICS_LOC = "METRICS_LOC";
	private static final String MAX_RECORDING_TIME = "MAX_RECORDING_TIME";
	
	public void setAdminPIN(String s) {
		prefs.put(ADMIN_PIN, s);
	}
	
	public String getAdminPIN() {
		return prefs.get(ADMIN_PIN, "1234");
	}
	
	public void setContentFolder(String s) {
		prefs.put(CONTENT_FOLDER, s);
	}
	
	public String getContentFolder() {
		return prefs.get(CONTENT_FOLDER, "");
	}
	
	public void setLocationsEnabled(boolean b) {
		prefs.putBoolean(LOCATIONS, b);
	}
	
	public boolean getLocationsEnabled() {
		return prefs.getBoolean(LOCATIONS, true);
	}
	
	public void setUserRecordingsEnabled(boolean b) {
		prefs.putBoolean(USER_RECORDINGS, b);
	}
	
	public boolean getUserRecordingsEnabled() {
		return prefs.getBoolean(USER_RECORDINGS, true);
	}
	
	public void setMetricsEnabled(boolean b) {
		prefs.putBoolean(METRICS, b);
	}
	
	public boolean getMetricsEnabled() {
		return prefs.getBoolean(METRICS, false);
	}
	
	public void setMetricsFolder(String s) {
		prefs.put(METRICS_LOC, s);
	}
	
	public String getMetricsFolder() {
		return prefs.get(METRICS_LOC, "");
	}
	
	public void setMaxRecordingTime(int i) {
		prefs.putInt(MAX_RECORDING_TIME, i);
	}
	
	public int getMaxRecordingTime() {
		return prefs.getInt(MAX_RECORDING_TIME, 300);
	}
	

	@Override
	public JPanel getConfigurationPanel() {
		return new GeneralConfigPanel(this);
	}

	@Override
	public String getConfigurationPanelName() {
		return "General";
	}

}
