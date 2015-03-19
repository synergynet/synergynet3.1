package multiplicity3.config.network;

import java.util.prefs.Preferences;

import javax.swing.JPanel;

import multiplicity3.config.ConfigurationApplication;
import multiplicity3.config.PreferencesItem;

public class NetworkConfigPrefsItem implements PreferencesItem {
	
	private static final Preferences prefs = ConfigurationApplication.getPreferences(NetworkConfigPrefsItem.class);
	
	private static final String HTTP_PROXY_HOST = "HTTP_PROXY_HOST";
	private static final String HTTP_PROXY_PORT = "HTTP_PROXY_PORT";
	private static final String HTTP_PROXY_ENABLED = "HTTP_PROXY_ENABLED";

	@Override
	public JPanel getConfigurationPanel() {
		return new NetworkPreferencesPanel(this);
	}

	@Override
	public String getConfigurationPanelName() {
		return "Network";
	}

	public String getProxyHost() {
		return prefs.get(HTTP_PROXY_HOST, "");
	}
	
	public void setProxyHost(String host) {
		prefs.put(HTTP_PROXY_HOST, host);
	}
	
	public int getProxyPort() {
		return prefs.getInt(HTTP_PROXY_PORT, 8080);
	}

	public void setProxyPort(int port) {
		prefs.putInt(HTTP_PROXY_PORT, port);
	}
	
	public void setProxyEnabled(boolean b) {
		prefs.putBoolean(HTTP_PROXY_ENABLED, b);
	}
	
	public boolean getProxyEnabled() {
		return prefs.getBoolean(HTTP_PROXY_ENABLED, false);
	}    
	
}
