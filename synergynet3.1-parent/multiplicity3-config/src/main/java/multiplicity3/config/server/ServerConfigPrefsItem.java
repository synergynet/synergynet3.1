package multiplicity3.config.server;

import java.util.prefs.Preferences;

import javax.swing.JPanel;

import multiplicity3.config.ConfigurationApplication;
import multiplicity3.config.PreferencesItem;

public class ServerConfigPrefsItem implements PreferencesItem {

	private static final Preferences prefs = ConfigurationApplication.getPreferences(ServerConfigPrefsItem.class);
	private static final String WEB_SERVER_PORT = "WEB_SERVER_PORT";
	private static final String WEB_SERVER_DIR = "WEB_SERVER_DIR";
	
	@Override
	public JPanel getConfigurationPanel() {
		return new ServerConfigPanel(this);
	}

	@Override
	public String getConfigurationPanelName() {
		return "Server";
	}
	
	public void setPort(int port) {
		prefs.putInt(WEB_SERVER_PORT, port);
	}
	
	public int getPort() {
		return prefs.getInt(WEB_SERVER_PORT, 8080);
	}
	
	public void setWebDirectory(String s) {
		prefs.put(WEB_SERVER_DIR, s);
	}
	
	public String getWebDirectory() {
		return prefs.get(WEB_SERVER_DIR, "/");
	}

}
