package multiplicity3.config.network;

import java.util.prefs.Preferences;

import javax.swing.JPanel;

import multiplicity3.config.ConfigurationApplication;
import multiplicity3.config.PreferencesItem;

/**
 * The Class NetworkConfigPrefsItem.
 */
public class NetworkConfigPrefsItem implements PreferencesItem
{

	/** The Constant HTTP_PROXY_ENABLED. */
	private static final String HTTP_PROXY_ENABLED = "HTTP_PROXY_ENABLED";

	/** The Constant HTTP_PROXY_HOST. */
	private static final String HTTP_PROXY_HOST = "HTTP_PROXY_HOST";

	/** The Constant HTTP_PROXY_PORT. */
	private static final String HTTP_PROXY_PORT = "HTTP_PROXY_PORT";

	/** The Constant prefs. */
	private static final Preferences prefs = ConfigurationApplication.getPreferences(NetworkConfigPrefsItem.class);

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.config.PreferencesItem#getConfigurationPanel()
	 */
	@Override
	public JPanel getConfigurationPanel()
	{
		return new NetworkPreferencesPanel(this);
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.config.PreferencesItem#getConfigurationPanelName()
	 */
	@Override
	public String getConfigurationPanelName()
	{
		return "Network";
	}

	/**
	 * Gets the proxy enabled.
	 *
	 * @return the proxy enabled
	 */
	public boolean getProxyEnabled()
	{
		return prefs.getBoolean(HTTP_PROXY_ENABLED, false);
	}

	/**
	 * Gets the proxy host.
	 *
	 * @return the proxy host
	 */
	public String getProxyHost()
	{
		return prefs.get(HTTP_PROXY_HOST, "");
	}

	/**
	 * Gets the proxy port.
	 *
	 * @return the proxy port
	 */
	public int getProxyPort()
	{
		return prefs.getInt(HTTP_PROXY_PORT, 8080);
	}

	/**
	 * Sets the proxy enabled.
	 *
	 * @param b
	 *            the new proxy enabled
	 */
	public void setProxyEnabled(boolean b)
	{
		prefs.putBoolean(HTTP_PROXY_ENABLED, b);
	}

	/**
	 * Sets the proxy host.
	 *
	 * @param host
	 *            the new proxy host
	 */
	public void setProxyHost(String host)
	{
		prefs.put(HTTP_PROXY_HOST, host);
	}

	/**
	 * Sets the proxy port.
	 *
	 * @param port
	 *            the new proxy port
	 */
	public void setProxyPort(int port)
	{
		prefs.putInt(HTTP_PROXY_PORT, port);
	}

}
