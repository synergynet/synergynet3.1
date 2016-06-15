package synergynet3.mediadetection.mediasearch.threads;

import java.io.File;
import java.util.logging.Level;

import synergynet3.mediadetection.IMediaSearcher;
import synergynet3.mediadetection.MediaDetection;
import synergynet3.mediadetection.MediaDetection.Ordering;
import synergynet3.mediadetection.mediasearchtypes.MediaSearchType;

/**
 * The Class SearchThread.
 */
public abstract class SearchThread implements Runnable
{

	/** The Constant WAIT_TIME. */
	private static final int WAIT_TIME = 5000;

	/** The initialised. */
	private boolean initialised = false;

	/** The media searcher. */
	private IMediaSearcher mediaSearcher;

	/** The run. */
	private boolean run = false;

	/** The media search types. */
	protected MediaSearchType[] mediaSearchTypes;

	/** The number to return. */
	protected int numberToReturn;

	/** The order. */
	protected Ordering order;

	/**
	 * Initialize.
	 *
	 * @param mediaSearcher
	 *            the media searcher
	 * @param mediaSearchTypes
	 *            the media search types
	 * @param order
	 *            the order
	 * @param numberToReturn
	 *            the number to return
	 */
	public void initialize(IMediaSearcher mediaSearcher, MediaSearchType[] mediaSearchTypes, Ordering order, int numberToReturn)
	{
		this.mediaSearcher = mediaSearcher;
		this.mediaSearchTypes = mediaSearchTypes;
		this.order = order;
		this.numberToReturn = numberToReturn;
		checkInitialDirectories();
		initialised = true;
	}

	/**
	 * Checks if is initialised.
	 *
	 * @return true, if is initialised
	 */
	public boolean isInitialised()
	{
		return initialised;
	}

	/**
	 * Reload discovered contents.
	 *
	 * @return the file[]
	 */
	public File[] reloadDiscoveredContents()
	{
		return new File[0];
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run()
	{
		if (initialised)
		{
			run = true;
			while (run)
			{
				try
				{
					Thread.sleep(WAIT_TIME);
					checkForNewDirectories();
				}
				catch (InterruptedException e)
				{
					MediaDetection.logMediaDetectionError(Level.SEVERE, "Interruption Exception.", e);
				}
			}
		}
		else
		{
			MediaDetection.logMediaDetectionError(Level.SEVERE, "Search thread has not been initialised yet.", null);
		}
	}

	/**
	 * Start.
	 */
	public void start()
	{
		Thread workerThread = new Thread(this);
		workerThread.setDaemon(true);
		workerThread.start();
	}

	/**
	 * Stop.
	 */
	public void stop()
	{
		run = false;
	}

	/**
	 * Check for new directories.
	 */
	protected void checkForNewDirectories()
	{
	}

	/**
	 * Check initial directories.
	 */
	protected void checkInitialDirectories()
	{
	}

	/**
	 * Files found.
	 *
	 * @param files
	 *            the files
	 */
	protected void filesFound(File[] files)
	{
		mediaSearcher.onFind(files);
	}

}
