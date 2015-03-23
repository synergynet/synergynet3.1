package synergynet3.mediadetection;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import synergynet3.mediadetection.mediasearch.Searcher;
import synergynet3.mediadetection.mediasearch.threads.SearchDirectoryThread;
import synergynet3.mediadetection.mediasearch.threads.SearchRemoveableMediaThread;
import synergynet3.mediadetection.mediasearch.threads.SearchThread;
import synergynet3.mediadetection.mediasearchtypes.AudioSearchType;
import synergynet3.mediadetection.mediasearchtypes.CustomSearchType;
import synergynet3.mediadetection.mediasearchtypes.DocumentSearchType;
import synergynet3.mediadetection.mediasearchtypes.ImageSearchType;
import synergynet3.mediadetection.mediasearchtypes.MediaSearchType;
import synergynet3.mediadetection.mediasearchtypes.SlideshowSearchType;
import synergynet3.mediadetection.mediasearchtypes.VideoSearchType;

/**
 * The Class MediaDetection.
 */
public class MediaDetection {

	/**
	 * The Enum Ordering.
	 */
	public enum Ordering {
		/** The alphabetical. */
		ALPHABETICAL,
		/** The date modified. */
		DATE_MODIFIED
	}

	/**
	 * The Enum SearchType.
	 */
	public enum SearchType {
		/** The audio. */
		AUDIO, /** The document. */
		DOCUMENT, /** The image. */
		IMAGE, /** The slideshow. */
		SLIDESHOW, /** The video. */
		VIDEO
	}

	/** The logger. */
	private static Logger logger;

	/** The listener number to return. */
	private int listenerNumberToReturn = -1;

	/** The listener ordering. */
	private Ordering listenerOrdering = Ordering.DATE_MODIFIED;

	/** The media search types. */
	private MediaSearchType[] mediaSearchTypes = {};

	/** The search threads. */
	private ArrayList<SearchThread> searchThreads = new ArrayList<SearchThread>();

	/**
	 * Creates a custom search type for use with initialising media discovery
	 * listeners.
	 *
	 * @param extension The extension of the file type this MediaSearchType
	 *            should be used for finding.
	 **/
	public static CustomSearchType createCustomSearchType(String extension) {
		String[] extensions = { extension };
		return createCustomSearchType(extensions);
	}

	/**
	 * Creates a custom search type for use with initialising media discovery
	 * listeners and searching directories using the static methods defined by
	 * this class.
	 *
	 * @param extensions An array of strings which represent the extensions of
	 *            the file types this MediaSearchType should be used for
	 *            finding.
	 **/
	public static CustomSearchType createCustomSearchType(String[] extensions) {
		CustomSearchType customSearchType = new CustomSearchType();
		for (String s : extensions) {
			customSearchType.addExtension(s);
		}
		return customSearchType;
	}

	/**
	 * Log media detection error.
	 *
	 * @param level the level
	 * @param logMessage the log message
	 * @param e the e
	 */
	public static void logMediaDetectionError(Level level, String logMessage,
			Exception e) {
		if (logger != null) {
			logger = Logger.getLogger(MediaDetection.class.getName());
		}
		if (e != null) {
			logger.log(level, logMessage, e);
		} else {
			logger.log(level, logMessage);
		}
	}

	/**
	 * Searches the directory at the given location for file types defined by
	 * the given MediaSearchType.
	 *
	 * @param directory The location and name of the directory to be searched
	 *            for files.
	 * @param mediaSearchTypes A MediaSearchType which defines the file types
	 *            being searched for.
	 * @param numberToReturn The maximum number of files to be returned by this
	 *            search. If value is not positive then all files discovered
	 *            will be returned.
	 * @param ordering A predefined ordering which sets the order any discovered
	 *            files will be returned in. If value is null then the
	 *            discovered files will be returned in order of data modified.
	 **/
	public static File[] searchDirectory(String directory,
			MediaSearchType mediaSearchType, int numberToReturn,
			Ordering ordering) {
		if (ordering == null) {
			ordering = Ordering.DATE_MODIFIED;
		}
		if (numberToReturn <= 0) {
			numberToReturn = -1;
		}
		MediaSearchType[] mediaSearchTypes = { mediaSearchType };
		return searchDirectory(directory, mediaSearchTypes, numberToReturn,
				ordering);
	}

	/**
	 * Searches the directory at the given location for file types defined by
	 * the given MediaSearchTypes.
	 *
	 * @param directory The location and name of the directory to be searched
	 *            for files.
	 * @param mediaSearchTypes An array of user defined MediaSearchTypes which
	 *            defines the file types being searched for.
	 * @param numberToReturn The maximum number of files to be returned by this
	 *            search. If value is not positive then all files discovered
	 *            will be returned.
	 * @param ordering A predefined ordering which sets the order any discovered
	 *            files will be returned in. If value is null then the
	 *            discovered files will be returned in order of data modified.
	 **/
	public static File[] searchDirectory(String directory,
			MediaSearchType[] mediaSearchTypes, int numberToReturn,
			Ordering ordering) {
		if (ordering == null) {
			ordering = Ordering.DATE_MODIFIED;
		}
		if (numberToReturn <= 0) {
			numberToReturn = -1;
		}
		File[] foundFiles = {};
		File directoryFile;
		try {
			directoryFile = new File(new URI(directory));
			if (directoryFile.isDirectory()) {
				foundFiles = Searcher.searchDirectoryTree(directoryFile,
						mediaSearchTypes, ordering, numberToReturn);
			} else {
				logMediaDetectionError(Level.SEVERE,
						"Location given is not a valid directory.", null);
			}
		} catch (URISyntaxException e) {
			logMediaDetectionError(Level.SEVERE,
					"Location given is not a valid address.", e);
		}
		return foundFiles;
	}

	/**
	 * Searches the directory at the given location for file types defined by
	 * the CustomSearchType created from the given extension.
	 *
	 * @param directory The location and name of the directory to be searched
	 *            for files.
	 * @param searchType A predefined SearchType which determines the files
	 *            types to be searched for.
	 * @param numberToReturn The maximum number of files to be returned by this
	 *            search. If value is not positive then all files discovered
	 *            will be returned.
	 * @param ordering A predefined ordering which sets the order any discovered
	 *            files will be returned in. If value is null then the
	 *            discovered files will be returned in order of data modified.
	 **/
	public static File[] searchDirectory(String directory,
			SearchType searchType, int numberToReturn, Ordering ordering) {
		if (ordering == null) {
			ordering = Ordering.DATE_MODIFIED;
		}
		if (numberToReturn <= 0) {
			numberToReturn = -1;
		}
		MediaSearchType mediaSearchType = getSearchTypeTarget(searchType);
		return searchDirectory(directory, mediaSearchType, numberToReturn,
				ordering);
	}

	/**
	 * Searches the directory at the given location for file types defined by
	 * the CustomSearchType created from the given extension.
	 *
	 * @param directory The location and name of the directory to be searched
	 *            for files.
	 * @param extension The extension used to define the file type being
	 *            searched for.
	 * @param numberToReturn The maximum number of files to be returned by this
	 *            search. If value is not positive then all files discovered
	 *            will be returned.
	 * @param ordering A predefined ordering which sets the order any discovered
	 *            files will be returned in. If value is null then the
	 *            discovered files will be returned in order of data modified.
	 **/
	public static File[] searchDirectory(String directory, String extension,
			int numberToReturn, Ordering ordering) {
		if (ordering == null) {
			ordering = Ordering.DATE_MODIFIED;
		}
		if (numberToReturn <= 0) {
			numberToReturn = -1;
		}
		CustomSearchType customSearchType = new CustomSearchType();
		customSearchType.addExtension(extension);
		return searchDirectory(directory, customSearchType, numberToReturn,
				ordering);
	}

	/**
	 * Searches the directory at the given location for file types defined by
	 * the CustomSearchType created from the given array of extensions.
	 *
	 * @param directory The location and name of the directory to be searched
	 *            for files.
	 * @param extensions An array of extensions used to define the file types
	 *            being searched for.
	 * @param numberToReturn The maximum number of files to be returned by this
	 *            search. If value is not positive then all files discovered
	 *            will be returned.
	 * @param ordering A predefined ordering which sets the order any discovered
	 *            files will be returned in. If value is null then the
	 *            discovered files will be returned in order of data modified.
	 **/
	public static File[] searchDirectory(String directory, String[] extensions,
			int numberToReturn, Ordering ordering) {
		if (ordering == null) {
			ordering = Ordering.DATE_MODIFIED;
		}
		if (numberToReturn <= 0) {
			numberToReturn = -1;
		}
		CustomSearchType customSearchType = new CustomSearchType();
		for (String s : extensions) {
			customSearchType.addExtension(s);
		}
		return searchDirectory(directory, customSearchType, numberToReturn,
				ordering);
	}

	/**
	 * Sets a logger for media detection to utilise.
	 *
	 * @param logger The existing logger to be used by media detection.
	 **/
	public static void setLogger(Logger logger) {
		MediaDetection.logger = logger;
	}

	/**
	 * Gets the search type target.
	 *
	 * @param searchType the search type
	 * @return the search type target
	 */
	private static MediaSearchType getSearchTypeTarget(SearchType searchType) {
		switch (searchType) {
			case IMAGE:
				return new ImageSearchType();
			case AUDIO:
				return new AudioSearchType();
			case VIDEO:
				return new VideoSearchType();
			case DOCUMENT:
				return new DocumentSearchType();
			case SLIDESHOW:
				return new SlideshowSearchType();
		}
		return new CustomSearchType();
	}

	/**
	 * Creates a custom search type using the extension. The listener affiliated
	 * with this object will search for any files which have the extension
	 * given.
	 *
	 * @param extension The extension of a file type the listener is needed to
	 *            search for.
	 **/
	public void addSearchTypeExtension(String extension) {
		CustomSearchType searchType = new CustomSearchType();
		searchType.addExtension(extension);
		addSearchTypeTarget(searchType);
	}

	/**
	 * Adds a MediaSearchType which will set the file types this object's
	 * affiliated listener will search for. This method can be called multiple
	 * times to set a range of file types to be searched for.
	 *
	 * @param mediaSearchType A user defined MediaSearchType which the listener
	 *            is needed to search for.
	 **/
	public void addSearchTypeTarget(MediaSearchType mediaSearchType) {
		MediaSearchType[] newMediaSearchTypes = new MediaSearchType[mediaSearchTypes.length + 1];
		for (int i = 0; i < mediaSearchTypes.length; i++) {
			newMediaSearchTypes[i] = mediaSearchTypes[i];
		}
		newMediaSearchTypes[mediaSearchTypes.length] = mediaSearchType;
		mediaSearchTypes = newMediaSearchTypes;
	}

	/**
	 * Adds a MediaSearchType which will set the file types this object's
	 * affiliated listener will search for. This method can be called multiple
	 * times to set a range of file types to be searched for.
	 *
	 * @param searchType A predefined SearchType which the listener is needed to
	 *            search for.
	 **/
	public void addSearchTypeTarget(SearchType searchType) {
		MediaSearchType mediaSearchType = getSearchTypeTarget(searchType);
		addSearchTypeTarget(mediaSearchType);
	}

	/**
	 * Sets the directory this object's listener should check for new files.
	 *
	 * @param directoryFile The file representing the directory to be check for
	 *            new files.
	 * @param mediaSearcher The object which any discovered files will be
	 *            forwarded to.
	 **/
	public void initialiseDirectoryListener(File directoryFile,
			IMediaSearcher mediaSearcher) {
		if (directoryFile.isDirectory()) {
			stopListener();
			SearchDirectoryThread searchDirectoryThread = new SearchDirectoryThread();
			searchDirectoryThread.setDirectory(directoryFile);
			searchDirectoryThread.initialize(mediaSearcher, mediaSearchTypes,
					listenerOrdering, listenerNumberToReturn);
			searchDirectoryThread.start();
			searchThreads.add(searchDirectoryThread);
		} else {
			logMediaDetectionError(Level.SEVERE,
					"Location given is not a valid directory.", null);
		}
	}

	/**
	 * Sets the directory this object's listener should check for new files.
	 *
	 * @param directory The absolute path (and /name) of the directory to be
	 *            check for new files.
	 * @param mediaSearcher The object which any discovered files will be
	 *            forwarded to.
	 **/
	public void initialiseDirectoryListener(String directory,
			IMediaSearcher mediaSearcher) {
		try {
			File directoryFile = new File(new URI(directory));
			if (directoryFile.isDirectory()) {
				stopListener();
				SearchDirectoryThread searchDirectoryThread = new SearchDirectoryThread();
				searchDirectoryThread.setDirectory(directory, true);
				searchDirectoryThread.initialize(mediaSearcher,
						mediaSearchTypes, listenerOrdering,
						listenerNumberToReturn);
				searchDirectoryThread.start();
				searchThreads.add(searchDirectoryThread);
			} else {
				logMediaDetectionError(Level.SEVERE,
						"Location given is not a valid directory.", null);
			}
		} catch (URISyntaxException e) {
			logMediaDetectionError(Level.SEVERE,
					"Location given is not a valid address.", e);
		}
	}

	/**
	 * Sets this object's listener to check for any new media devices. When
	 * discovered these devices will be searched for new File.
	 * 
	 * @param mediaSearcher The object which any discovered files will be
	 *            forwarded to.
	 **/
	public void initialiseRemovableMediaListener(IMediaSearcher mediaSearcher) {
		String os = System.getProperty("os.name").toLowerCase();
		if (os.indexOf("win") >= 0) {
			stopListener();
			SearchRemoveableMediaThread searchRemoveableMediaThread = new SearchRemoveableMediaThread();
			searchRemoveableMediaThread.initialize(mediaSearcher,
					mediaSearchTypes, listenerOrdering, listenerNumberToReturn);
			searchRemoveableMediaThread.start();
			searchThreads.add(searchRemoveableMediaThread);
		} else {
			String mediaDirectoryAddress = "";

			if (os.indexOf("mac") >= 0) {
				mediaDirectoryAddress = "/Volumes/";
			} else if ((os.indexOf("nix") >= 0) || (os.indexOf("nux") >= 0)) {
				mediaDirectoryAddress = "/media/";
			}

			if (mediaDirectoryAddress.length() > 0) {
				File mediaDirectory = new File(mediaDirectoryAddress);
				if (mediaDirectory.isDirectory()) {
					stopListener();
					SearchDirectoryThread searchDirectoryThread = new SearchDirectoryThread();
					searchDirectoryThread.setDirectory(mediaDirectoryAddress,
							false);
					searchDirectoryThread.setDetectingNewDrives();
					searchDirectoryThread.initialize(mediaSearcher,
							mediaSearchTypes, listenerOrdering,
							listenerNumberToReturn);
					searchDirectoryThread.start();
					searchThreads.add(searchDirectoryThread);
				} else {
					logMediaDetectionError(Level.SEVERE,
							"Cannot find media folder.", null);
				}
			} else {
				logMediaDetectionError(Level.SEVERE,
						"Media folder was not set.", null);
			}
		}
	}

	/**
	 * Sends any items already discovered by the in these locations to the
	 * tables.
	 **/
	public ArrayList<File[]> reloadDisoveredContent() {
		ArrayList<File[]> discoveredFiles = new ArrayList<File[]>();
		for (SearchThread searchThread : searchThreads) {
			discoveredFiles.add(searchThread.reloadDiscoveredContents());
		}
		return discoveredFiles;
	}

	/**
	 * Sets how the maximum number of files should be returned by this object's
	 * listener at a time.
	 *
	 * @param listenerNumberToReturn The maximum number of files to return.
	 **/
	public void setNumberToReturn(int listenerNumberToReturn) {
		this.listenerNumberToReturn = listenerNumberToReturn;
	}

	/**
	 * Sets how any files discovered by this object's listener should be sorted.
	 *
	 * @param listenerOrdering A predefined ordering which sets the order any
	 *            discovered files will be returned in.
	 **/
	public void setOrder(Ordering listenerOrdering) {
		this.listenerOrdering = listenerOrdering;
	}

	/**
	 * Stops this object listening for new files.
	 **/
	public void stopListener() {
		for (SearchThread searchThread : searchThreads) {
			if (searchThread != null) {
				searchThread.stop();
				searchThread = null;
			}
		}
	}

}
