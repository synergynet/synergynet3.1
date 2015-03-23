package synergynet3.config.web;

import java.io.File;
import java.util.regex.Pattern;

/**
 * The Class CacheOrganisation.
 */
public class CacheOrganisation {

	/** The Constant AUDIO_DIR. */
	public static final String AUDIO_DIR = "Audio";

	/** The Constant CAPTURE_DIR. */
	public static final String CAPTURE_DIR = "Capture";

	/** The Constant ICON_DIR. */
	public static final String ICON_DIR = "Icon";

	/** The Constant NETWORK_FLICK_DIR. */
	public static final String NETWORK_FLICK_DIR = "Network_Flick";

	/** The Constant PROJECTOR_TRANSFER_DIR. */
	public static final String PROJECTOR_TRANSFER_DIR = "Projector_Transfer";

	/** The Constant SCREENSHOT_DIR. */
	public static final String SCREENSHOT_DIR = "Screenshots";

	/** The Constant STUDENT_DIR. */
	public static final String STUDENT_DIR = "Students";

	/** The Constant TABLE_DIR. */
	public static final String TABLE_DIR = "Table_Media";

	/** The Constant TRANSFER_DIR. */
	public static final String TRANSFER_DIR = "Transfers";

	/**
	 * Clear capture cache.
	 */
	public static void clearCaptureCache() {
		File screenshotCacheDir = new File(getScreenshotDir());
		if (screenshotCacheDir.isDirectory()) {
			File[] subFolders = screenshotCacheDir.listFiles();
			for (File subFolder : subFolders) {
				if (subFolder.isFile()) {
					if (!subFolder.delete()) {
						subFolder.deleteOnExit();
					}
				} else if (subFolder.isDirectory()) {
					File[] files = subFolder.listFiles();
					for (File file : files) {
						if (!file.delete()) {
							file.deleteOnExit();
						}
					}
				}
			}
		}

		File audioCacheDir = new File(getAudioDir());
		if (audioCacheDir.isDirectory()) {
			File[] subFolders = audioCacheDir.listFiles();
			for (File subFolder : subFolders) {
				if (subFolder.isFile()) {
					if (!subFolder.delete()) {
						subFolder.deleteOnExit();
					}
				} else if (subFolder.isDirectory()) {
					File[] files = subFolder.listFiles();
					for (File file : files) {
						if (!file.delete()) {
							file.deleteOnExit();
						}
					}
				}
			}
		}
	}

	/**
	 * Clear transfer caches.
	 */
	public static void clearTransferCaches() {
		File networkFlickCacheDir = new File(getNetworkFlickDir());
		if (networkFlickCacheDir.isDirectory()) {
			File[] subFolders = networkFlickCacheDir.listFiles();
			for (File subFolder : subFolders) {
				if (subFolder.isFile()) {
					if (!subFolder.delete()) {
						subFolder.deleteOnExit();
					}
				} else if (subFolder.isDirectory()) {
					File[] files = subFolder.listFiles();
					for (File file : files) {
						if (!file.delete()) {
							file.deleteOnExit();
						}
					}
				}
			}
		}

		File projectorTransferCacheDir = new File(getProjectorTransferDir());
		if (projectorTransferCacheDir.isDirectory()) {
			File[] subFolders = projectorTransferCacheDir.listFiles();
			for (File subFolder : subFolders) {
				if (subFolder.isFile()) {
					if (!subFolder.delete()) {
						subFolder.deleteOnExit();
					}
				} else if (subFolder.isDirectory()) {
					File[] files = subFolder.listFiles();
					for (File file : files) {
						if (!file.delete()) {
							file.deleteOnExit();
						}
					}
				}
			}
		}
	}

	/**
	 * Gets the audio dir.
	 *
	 * @return the audio dir
	 */
	public static String getAudioDir() {
		String captureCacheLoc = getCaptureDir();
		String audioCacheLoc = captureCacheLoc + File.separator + AUDIO_DIR;
		File audioCacheDir = new File(audioCacheLoc);
		if (!audioCacheDir.isDirectory()) {
			audioCacheDir.mkdir();
		}
		return audioCacheLoc;
	}

	/**
	 * Gets the cache location.
	 *
	 * @return the cache location
	 */
	public static String getCacheLocation() {
		WebConfigPrefsItem prefs = new WebConfigPrefsItem();
		String cacheLoc = prefs.getSharedLocation();
		File cacheDir = new File(cacheLoc);
		if (!cacheDir.isDirectory()) {
			cacheDir.mkdir();
		}
		return cacheLoc;
	}

	/**
	 * Gets the capture dir.
	 *
	 * @return the capture dir
	 */
	public static String getCaptureDir() {
		String cacheLoc = getCacheLocation();
		String captureCacheLoc = cacheLoc + File.separator + CAPTURE_DIR;
		File captureCacheDir = new File(captureCacheLoc);
		if (!captureCacheDir.isDirectory()) {
			captureCacheDir.mkdir();
		}
		return captureCacheLoc;
	}

	/**
	 * Gets the network flick dir.
	 *
	 * @return the network flick dir
	 */
	public static String getNetworkFlickDir() {
		String transferCacheLoc = getTransferDir();
		String networkFlickCacheLoc = transferCacheLoc + File.separator
				+ NETWORK_FLICK_DIR;
		File networkFlickCacheDir = new File(networkFlickCacheLoc);
		if (!networkFlickCacheDir.isDirectory()) {
			networkFlickCacheDir.mkdir();
		}
		return networkFlickCacheLoc;
	}

	/**
	 * Gets the projector transfer dir.
	 *
	 * @return the projector transfer dir
	 */
	public static String getProjectorTransferDir() {
		String transferCacheLoc = getTransferDir();
		String projectorTransferCacheLoc = transferCacheLoc + File.separator
				+ PROJECTOR_TRANSFER_DIR;
		File projectorTransferCacheDir = new File(projectorTransferCacheLoc);
		if (!projectorTransferCacheDir.isDirectory()) {
			projectorTransferCacheDir.mkdir();
		}
		return projectorTransferCacheLoc;
	}

	/**
	 * Gets the screenshot dir.
	 *
	 * @return the screenshot dir
	 */
	public static String getScreenshotDir() {
		String captureCacheLoc = getCaptureDir();
		String screenshotCacheLoc = captureCacheLoc + File.separator
				+ SCREENSHOT_DIR;
		File screenshotCacheDir = new File(screenshotCacheLoc);
		if (!screenshotCacheDir.isDirectory()) {
			screenshotCacheDir.mkdir();
		}
		return screenshotCacheLoc;
	}

	/**
	 * Gets the specific dir.
	 *
	 * @param loc the loc
	 * @return the specific dir
	 */
	public static String getSpecificDir(String loc) {
		String cacheLoc = getCacheLocation();
		String[] path = loc.split(Pattern.quote(File.separator));
		for (String s : path) {
			cacheLoc += File.separator + s;
			File specificDir = new File(cacheLoc);
			if (!specificDir.isDirectory()) {
				specificDir.mkdir();
			}
		}
		return cacheLoc;
	}

	/**
	 * Gets the specific student dir.
	 *
	 * @param studentID the student id
	 * @return the specific student dir
	 */
	public static String getSpecificStudentDir(String studentID) {
		String studentCacheLoc = getStudentsDir();
		String specifcStudentCacheLoc = studentCacheLoc + File.separator
				+ studentID;
		File specificStudentCacheDir = new File(specifcStudentCacheLoc);
		if (!specificStudentCacheDir.isDirectory()) {
			specificStudentCacheDir.mkdir();
		}
		return specifcStudentCacheLoc;
	}

	/**
	 * Gets the specific student icon dir.
	 *
	 * @param studentID the student id
	 * @return the specific student icon dir
	 */
	public static String getSpecificStudentIconDir(String studentID) {
		String studentSpecificCache = getSpecificStudentDir(studentID);
		String specifcStudentIconCacheLoc = studentSpecificCache
				+ File.separator + ICON_DIR;
		File specifcStudentIconCacheDir = new File(specifcStudentIconCacheLoc);
		if (!specifcStudentIconCacheDir.isDirectory()) {
			specifcStudentIconCacheDir.mkdir();
		}
		return specifcStudentIconCacheLoc;
	}

	/**
	 * Gets the specific table dir.
	 *
	 * @param tableID the table id
	 * @return the specific table dir
	 */
	public static String getSpecificTableDir(String tableID) {
		String tableCacheLoc = getTablesDir();
		String specificTableCacheLoc = tableCacheLoc + File.separator + tableID;
		File specificTableCacheDir = new File(specificTableCacheLoc);
		if (!specificTableCacheDir.isDirectory()) {
			specificTableCacheDir.mkdir();
		}
		return specificTableCacheLoc;
	}

	/**
	 * Gets the students dir.
	 *
	 * @return the students dir
	 */
	public static String getStudentsDir() {
		String cacheLoc = getCacheLocation();
		String studentCacheLoc = cacheLoc + File.separator + STUDENT_DIR;
		File studentCacheDir = new File(studentCacheLoc);
		if (!studentCacheDir.isDirectory()) {
			studentCacheDir.mkdir();
		}
		return studentCacheLoc;
	}

	/**
	 * Gets the tables dir.
	 *
	 * @return the tables dir
	 */
	public static String getTablesDir() {
		String cacheLoc = getCacheLocation();
		String tableCacheLoc = cacheLoc + File.separator + TABLE_DIR;
		File tableCacheDir = new File(tableCacheLoc);
		if (!tableCacheDir.isDirectory()) {
			tableCacheDir.mkdir();
		}
		return tableCacheLoc;
	}

	/**
	 * Gets the transfer dir.
	 *
	 * @return the transfer dir
	 */
	public static String getTransferDir() {
		String cacheLoc = getCacheLocation();
		String transferCacheLoc = cacheLoc + File.separator + TRANSFER_DIR;
		File transferCacheDir = new File(transferCacheLoc);
		if (!transferCacheDir.isDirectory()) {
			transferCacheDir.mkdir();
		}
		return transferCacheLoc;
	}

}
