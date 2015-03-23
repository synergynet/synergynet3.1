package synergynet3.mediadetection.mediasearch.threads;

import java.io.File;
import java.util.ArrayList;

import synergynet3.mediadetection.mediasearch.Searcher;

/**
 * The Class SearchRemoveableMediaThread.
 */
public class SearchRemoveableMediaThread extends SearchThread {

	/** The discovered roots. */
	private ArrayList<File> discoveredRoots = new ArrayList<File>();

	/** The roots. */
	private File[] roots;

	/*
	 * (non-Javadoc)
	 * @see synergynet3.mediadetection.mediasearch.threads.SearchThread#
	 * reloadDiscoveredContents()
	 */
	@Override
	public File[] reloadDiscoveredContents() {

		ArrayList<File[]> allFilesOnDiscoveredRoots = new ArrayList<File[]>();
		for (File r : discoveredRoots) {
			allFilesOnDiscoveredRoots.add(Searcher.searchDirectoryTree(r,
					mediaSearchTypes, order, numberToReturn));
		}
		int count = 0;
		for (File[] discovered : allFilesOnDiscoveredRoots) {
			count += discovered.length;
		}
		File[] toReturn = new File[count];
		int i = 0;
		for (File[] discovered : allFilesOnDiscoveredRoots) {
			for (File f : discovered) {
				toReturn[i] = f;
				i++;
			}
		}
		return toReturn;
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
		if (roots.length != File.listRoots().length) {
			if (roots.length < File.listRoots().length) {
				for (File f : File.listRoots()) {
					if (isNotPresentInRoots(f)) {
						discoveredRoots.add(f);
						findFiles(f);
					}
				}

			}
			roots = File.listRoots();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.mediadetection.mediasearch.threads.SearchThread#
	 * checkInitialDirectories()
	 */
	@Override
	protected void checkInitialDirectories() {
		roots = File.listRoots();
	}

}
