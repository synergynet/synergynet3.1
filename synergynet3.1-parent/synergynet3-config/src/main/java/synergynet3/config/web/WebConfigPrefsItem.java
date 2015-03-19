package synergynet3.config.web;

import java.util.prefs.Preferences;

import javax.swing.JPanel;

import multiplicity3.config.ConfigurationApplication;
import multiplicity3.config.PreferencesItem;

public class WebConfigPrefsItem implements PreferencesItem {

	private static final Preferences prefs = ConfigurationApplication.getPreferences(WebConfigPrefsItem.class);
	private static final String CLUSTER_PORT = "CLUSTER_PORT";
	private static final String CLUSTER_HOST = "CLUSTER_HOST";
	private static final String USERNAME = "USER_NAME";
	private static final String PASSWORD = "PASSWORD";
	private static final String SHARED_LOCATION = "SHARED_LOCATION";
	
	@Override
	public JPanel getConfigurationPanel() {
		return new WebConfigPanel(this);
	}

	@Override
	public String getConfigurationPanelName() {
		return "Web";
	}
	
	public void setPort(int port) {
		prefs.putInt(CLUSTER_PORT, port);
	}
	
	public int getPort() {
		return prefs.getInt(CLUSTER_PORT, 5222);
	}
	
	public void setClusterHost(String s) {
		prefs.put(CLUSTER_HOST, s);
	}
	
	public String getClusterHost() {
		return prefs.get(CLUSTER_HOST, "localhost");
	}
	
	public void setClusterUserName(String s) {
		prefs.put(USERNAME, s);
	}
	
	public String getClusterUserName() {
		return prefs.get(USERNAME, "");
	}
	
	public void setClusterPassword(String s) {
		prefs.put(PASSWORD, s);
	}
	
	public String getClusterPassword() {
		return prefs.get(PASSWORD, "");
	}

	public void setSharedLocation(String s) {
		prefs.put(SHARED_LOCATION, s);
	}
	
	public String getSharedLocation() {
		return prefs.get(SHARED_LOCATION, "");
	}
	
}
