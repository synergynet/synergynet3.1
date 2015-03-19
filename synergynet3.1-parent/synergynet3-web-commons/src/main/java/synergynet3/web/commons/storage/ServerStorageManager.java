package synergynet3.web.commons.storage;

import java.io.File;

public class ServerStorageManager {
	public static File getStorageDirForAppWithName(String name) {
		File applicationDirectory = new File(getServerStorageDir(), name);
		applicationDirectory.mkdirs();
		return applicationDirectory;
	}

	private static File getServerStorageDir() {
		return new File(System.getProperty("user.home"), ".synergynet3-server");
	}
}
