package synergynet3.web.commons.storage;

import java.io.File;

/**
 * The Class ServerStorageManager.
 */
public class ServerStorageManager {

	/**
	 * Gets the storage dir for app with name.
	 *
	 * @param name the name
	 * @return the storage dir for app with name
	 */
	public static File getStorageDirForAppWithName(String name) {
		File applicationDirectory = new File(getServerStorageDir(), name);
		applicationDirectory.mkdirs();
		return applicationDirectory;
	}

	/**
	 * Gets the server storage dir.
	 *
	 * @return the server storage dir
	 */
	private static File getServerStorageDir() {
		return new File(System.getProperty("user.home"), ".synergynet3-server");
	}
}
