package synergynet3.museum.table.settingsapp.general;

import java.util.prefs.Preferences;

import javax.swing.JPanel;

import multiplicity3.config.PreferencesItem;

/**
 * The Class GeneralConfigPrefsItem.
 */
public class GeneralConfigPrefsItem implements PreferencesItem
{

	/** The Constant ADMIN_PIN. */
	private static final String ADMIN_PIN = "ADMIN_PIN";

	/** The Constant CONTENT_FOLDER. */
	private static final String CONTENT_FOLDER = "CONTENT_FOLDER";

	/** The Constant LOCATIONS. */
	private static final String LOCATIONS = "LOCATIONS";

	/** The Constant MAX_RECORDING_TIME. */
	private static final String MAX_RECORDING_TIME = "MAX_RECORDING_TIME";

	/** The Constant METRICS. */
	private static final String METRICS = "METRICS";

	/** The Constant METRICS_LOC. */
	private static final String METRICS_LOC = "METRICS_LOC";

	/** The Constant prefs. */
	private static final Preferences prefs = Preferences.userNodeForPackage(GeneralConfigPrefsItem.class);

	/** The Constant USER_RECORDINGS. */
	private static final String USER_RECORDINGS = "USER_RECORDINGS";

	/**
	 * Gets the admin pin.
	 *
	 * @return the admin pin
	 */
	public String getAdminPIN()
	{
		return prefs.get(ADMIN_PIN, "1234");
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.config.PreferencesItem#getConfigurationPanel()
	 */
	@Override
	public JPanel getConfigurationPanel()
	{
		return new GeneralConfigPanel(this);
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.config.PreferencesItem#getConfigurationPanelName()
	 */
	@Override
	public String getConfigurationPanelName()
	{
		return "General";
	}

	/**
	 * Gets the content folder.
	 *
	 * @return the content folder
	 */
	public String getContentFolder()
	{
		return prefs.get(CONTENT_FOLDER, "");
	}

	/**
	 * Gets the locations enabled.
	 *
	 * @return the locations enabled
	 */
	public boolean getLocationsEnabled()
	{
		return prefs.getBoolean(LOCATIONS, true);
	}

	/**
	 * Gets the max recording time.
	 *
	 * @return the max recording time
	 */
	public int getMaxRecordingTime()
	{
		return prefs.getInt(MAX_RECORDING_TIME, 300);
	}

	/**
	 * Gets the metrics enabled.
	 *
	 * @return the metrics enabled
	 */
	public boolean getMetricsEnabled()
	{
		return prefs.getBoolean(METRICS, false);
	}

	/**
	 * Gets the metrics folder.
	 *
	 * @return the metrics folder
	 */
	public String getMetricsFolder()
	{
		return prefs.get(METRICS_LOC, "");
	}

	/**
	 * Gets the user recordings enabled.
	 *
	 * @return the user recordings enabled
	 */
	public boolean getUserRecordingsEnabled()
	{
		return prefs.getBoolean(USER_RECORDINGS, true);
	}

	/**
	 * Sets the admin pin.
	 *
	 * @param s
	 *            the new admin pin
	 */
	public void setAdminPIN(String s)
	{
		prefs.put(ADMIN_PIN, s);
	}

	/**
	 * Sets the content folder.
	 *
	 * @param s
	 *            the new content folder
	 */
	public void setContentFolder(String s)
	{
		prefs.put(CONTENT_FOLDER, s);
	}

	/**
	 * Sets the locations enabled.
	 *
	 * @param b
	 *            the new locations enabled
	 */
	public void setLocationsEnabled(boolean b)
	{
		prefs.putBoolean(LOCATIONS, b);
	}

	/**
	 * Sets the max recording time.
	 *
	 * @param i
	 *            the new max recording time
	 */
	public void setMaxRecordingTime(int i)
	{
		prefs.putInt(MAX_RECORDING_TIME, i);
	}

	/**
	 * Sets the metrics enabled.
	 *
	 * @param b
	 *            the new metrics enabled
	 */
	public void setMetricsEnabled(boolean b)
	{
		prefs.putBoolean(METRICS, b);
	}

	/**
	 * Sets the metrics folder.
	 *
	 * @param s
	 *            the new metrics folder
	 */
	public void setMetricsFolder(String s)
	{
		prefs.put(METRICS_LOC, s);
	}

	/**
	 * Sets the user recordings enabled.
	 *
	 * @param b
	 *            the new user recordings enabled
	 */
	public void setUserRecordingsEnabled(boolean b)
	{
		prefs.putBoolean(USER_RECORDINGS, b);
	}

}
