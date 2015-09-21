package synergynet3.config.web;

import java.lang.management.ManagementFactory;
import java.util.prefs.Preferences;

import javax.swing.JPanel;

import multiplicity3.config.ConfigurationApplication;
import multiplicity3.config.PreferencesItem;

/**
 * The Class WebConfigPrefsItem.
 */
public class WebConfigPrefsItem implements PreferencesItem {

	/** The Constant CLUSTER_HOST. */
	private static final String CLUSTER_HOST = "CLUSTER_HOST";

	/** The Constant CLUSTER_PORT. */
	private static final String CLUSTER_PORT = "CLUSTER_PORT";
	
	/** The Constant CLUSTER_INTERFACE. */
	private static final String CLUSTER_INTERFACE = "CLUSTER_INTERFACE";

	/** The Constant PASSWORD. */
	private static final String PASSWORD = "PASSWORD";

	/** The Constant prefs. */
	private static final Preferences prefs = ConfigurationApplication
			.getPreferences(WebConfigPrefsItem.class);

	/** The Constant SHARED_LOCATION. */
	private static final String SHARED_LOCATION = "SHARED_LOCATION";

	/** The Constant USERNAME. */
	private static final String USERNAME = "USER_NAME";

	/**
	 * Gets the cluster host.
	 *
	 * @return the cluster host
	 */
	public String getClusterHost() {
		String argument = ManagementFactory.getRuntimeMXBean()
				.getSystemProperties().get("synergynet3.host");
		if (argument != null) {
			return argument;
		}
		return prefs.get(CLUSTER_HOST, "localhost");
	}

	/**
	 * Gets the cluster password.
	 *
	 * @return the cluster password
	 */
	public String getClusterPassword() {
		String argument = ManagementFactory.getRuntimeMXBean()
				.getSystemProperties().get("synergynet3.password");
		if (argument != null) {
			return argument;
		}
		return prefs.get(PASSWORD, "");
	}

	/**
	 * Gets the cluster user name.
	 *
	 * @return the cluster user name
	 */
	public String getClusterUserName() {
		String argument = ManagementFactory.getRuntimeMXBean()
				.getSystemProperties().get("synergynet3.user");
		if (argument != null) {
			return argument;
		}
		return prefs.get(USERNAME, "");
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.config.PreferencesItem#getConfigurationPanel()
	 */
	@Override
	public JPanel getConfigurationPanel() {
		return new WebConfigPanel(this);
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.config.PreferencesItem#getConfigurationPanelName()
	 */
	@Override
	public String getConfigurationPanelName() {
		return "Web";
	}
	
	/**
	 * Gets the Interface.
	 *
	 * @return the interface
	 */
	public String getClusterInterface() {
		String argument = ManagementFactory.getRuntimeMXBean()
				.getSystemProperties().get("synergynet3.interface");
		if (argument != null) {
			return argument;
		}
		return prefs.get(CLUSTER_INTERFACE, "");
	}

	/**
	 * Gets the port.
	 *
	 * @return the port
	 */
	public int getPort() {
		String argument = ManagementFactory.getRuntimeMXBean()
				.getSystemProperties().get("synergynet3.port");
		if (argument != null) {
			return Integer.parseInt(argument);
		}
		return prefs.getInt(CLUSTER_PORT, 5222);
	}
	

	/**
	 * Gets the shared location.
	 *
	 * @return the shared location
	 */
	public String getSharedLocation() {
		return prefs.get(SHARED_LOCATION, "");
	}

	/**
	 * Sets the cluster host.
	 *
	 * @param s the new cluster host
	 */
	public void setClusterHost(String s) {
		prefs.put(CLUSTER_HOST, s);
	}

	/**
	 * Sets the cluster password.
	 *
	 * @param s the new cluster password
	 */
	public void setClusterPassword(String s) {
		prefs.put(PASSWORD, s);
	}

	/**
	 * Sets the cluster user name.
	 *
	 * @param s the new cluster user name
	 */
	public void setClusterUserName(String s) {
		prefs.put(USERNAME, s);
	}
	
	/**
	 * Sets the interface.
	 *
	 * @param port the new interface
	 */
	public void setClusterInterface(String clusterInterface) {
		prefs.put(CLUSTER_INTERFACE, clusterInterface);
	}	

	/**
	 * Sets the port.
	 *
	 * @param port the new port
	 */
	public void setPort(int port) {
		prefs.putInt(CLUSTER_PORT, port);
	}
	
	/**
	 * Sets the shared location.
	 *
	 * @param s the new shared location
	 */
	public void setSharedLocation(String s) {
		prefs.put(SHARED_LOCATION, s);
	}

}
