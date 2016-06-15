package synergynet3.cluster.xmpp.presence;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The Class PresenceEventInjector.
 */
public class PresenceEventInjector implements IPresenceListener
{

	/** The Constant log. */
	private static final Logger log = Logger.getLogger(PresenceEventInjector.class.getName());

	/** The listeners. */
	private List<IPresenceListener> listeners;

	/** The update list. */
	private ConcurrentLinkedQueue<Callable<Void>> updateList;

	/**
	 * Instantiates a new presence event injector.
	 */
	public PresenceEventInjector()
	{
		listeners = new ArrayList<IPresenceListener>();
		updateList = new ConcurrentLinkedQueue<Callable<Void>>();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynet3.cluster.xmpp.presence.IPresenceListener#deviceAvailable(java
	 * .lang.String)
	 */
	@Override
	public void deviceAvailable(final String id)
	{
		log.log(Level.FINER, "Device " + id + " is now available.");
		updateList.add(new Callable<Void>()
		{
			@Override
			public Void call() throws Exception
			{
				synchronized (listeners)
				{
					for (IPresenceListener listener : listeners)
					{
						listener.deviceAvailable(id);
					}
				}
				return null;
			}
		});

	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynet3.cluster.xmpp.presence.IPresenceListener#deviceUnavailable
	 * (java.lang.String)
	 */
	@Override
	public void deviceUnavailable(final String id)
	{
		updateList.add(new Callable<Void>()
		{
			@Override
			public Void call() throws Exception
			{
				synchronized (listeners)
				{
					for (IPresenceListener listener : listeners)
					{
						listener.deviceUnavailable(id);
					}
				}
				return null;
			}
		});
	}

	/**
	 * Register presence listener.
	 *
	 * @param listener
	 *            the listener
	 */
	public void registerPresenceListener(IPresenceListener listener)
	{
		if (listeners.contains(listener))
		{
			return;
		}
		listeners.add(listener);
	}

	/**
	 * Update.
	 */
	public void update()
	{
		if (updateList.size() < 1)
		{
			return;
		}
		log.log(Level.FINER, "Calling update on " + updateList.size() + " callable items.");
		for (Callable<Void> c : updateList)
		{
			try
			{
				log.log(Level.FINER, "calling.");
				c.call();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			log.log(Level.FINER, "removing.");
			updateList.remove(c);
		}
		log.log(Level.FINER, "update complete.");
	}

}
