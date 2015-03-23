package synergynet3.mediadetection.mediasearch.threads;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;

import synergynet3.mediadetection.MediaDetection;
import synergynet3.mediadetection.mediasearch.Searcher;

/**
 * The Class SearchDirectoryThread.
 */
public class SearchDirectoryThread extends SearchThread {

	/** The directory. */
	private File directory;

	/** The directory set. */
	private boolean directorySet = false;

	/** The is detecting new drives. */
	private boolean isDetectingNewDrives = false;

	/** The roots. */
	private File[] roots;

	/*
	 * (non-Javadoc)
	 * @see synergynet3.mediadetection.mediasearch.threads.SearchThread#
	 * reloadDiscoveredContents()
	 */
	@Override
	public File[] reloadDiscoveredContents() {
		return Searcher.searchDirectoryTree(directory, mediaSearchTypes, order,
				numberToReturn);
	}

	/**
	 * Sets the detecting new drives.
	 */
	public void setDetectingNewDrives() {
		isDetectingNewDrives = true;
	}

	/**
	 * Sets the directory.
	 *
	 * @param directoryFile the new directory
	 */
	public void setDirectory(File directoryFile) {
		directory = directoryFile;
		if (directory != null) {
			if (directory.isDirectory()) {
				directorySet = true;
			} else {
				MediaDetection.logMediaDetectionError(Level.SEVERE,
						"Location given is not a directory.", null);
			}
		}
	}

	/**
	 * Sets the directory.
	 *
	 * @param directoryAddress the directory address
	 * @param isURI the is uri
	 */
	public void setDirectory(String directoryAddress, boolean isURI) {
		if (isURI) {
			try {
				directory = new File(new URI(directoryAddress));
			} catch (URISyntaxException e) {
				MediaDetection.logMediaDetectionError(Level.SEVERE,
						"Location given is not a valid address.", e);
			}
		} else {
			directory = new File(directoryAddress);
		}
		if (directory != null) {
			if (directory.isDirectory()) {
				directorySet = true;
			} else {
				MediaDetection.logMediaDetectionError(Level.SEVERE,
						"Location given is not a directory.", null);
			}
		}
	}

	/**
	 * Find files.
	 *
	 * @param f the f
	 */
	private void findFiles(File f) {
		File[] foundFiles = null;
		foundFiles = Searcher.searchDirectoryTree(f, mediaSearchTypes, order,
				numberToReturn);
		if (foundFiles != null) {
			if (foundFiles.length > 0) {
				filesFound(foundFiles);
			}
		}
	}

	/**
	 * Checks if is not present in roots.
	 *
	 * @param f the f
	 * @return true, if is not present in roots
	 */
	private boolean isNotPresentInRoots(File f) {
		boolean isNotPresent = true;
		for (File r : roots) {
			if (r.equals(f)) {
				isNotPresent = false;
				break;
			}
		}
		return isNotPresent;
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.mediadetection.mediasearch.threads.SearchThread#
	 * checkForNewDirectories()
	 */
	@Override
	protected void checkForNewDirectories() {
		if (roots.length != directory.listFiles().length) {
			if (roots.length < directory.listFiles().length) {
				if (!isDetectingNewDrives) {
					findFiles(directory);
				}
				for (File f : directory.listFiles()) {
					if (isNotPresentInRoots(f)) {
						if (f.isDirectory()) {
							findFiles(f);
						}
					}
				}

			}
			roots = directory.listFiles();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.mediadetection.mediasearch.threads.SearchThread#
	 * checkInitialDirectories()
	 */
	@Override
	protected void checkInitialDirectories() {
		if (directorySet) {
			roots = directory.listFiles();
		} else {
			MediaDetection.logMediaDetectionError(Level.SEVERE,
					"Directory to search has not been set yet.", null);
		}
	}

}
