package synergynet3.cluster.xmpp.presence;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterGroup;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.util.StringUtils;

/**
 * The Class PresenceManager.
 */
public class PresenceManager implements RosterListener
{

	/** The Constant MULTIPLICITY_DEVICES_GROUP. */
	public static final String MULTIPLICITY_DEVICES_GROUP = "multiplicitydevices";

	/** The Constant log. */
	private static final Logger log = Logger.getLogger(PresenceManager.class.getName());

	/** The connection. */
	private XMPPConnection connection;

	/** The current devices present. */
	private Set<String> currentDevicesPresent;

	/** The listeners. */
	private List<IPresenceListener> listeners;

	/** The multiplicity group. */
	private RosterGroup multiplicityGroup;

	/** The roster. */
	private Roster roster;

	/**
	 * Instantiates a new presence manager.
	 */
	public PresenceManager()
	{
		log.log(Level.FINE, "Creating XMPP Presence Manager");
		currentDevicesPresent = new HashSet<String>();
		listeners = new ArrayList<IPresenceListener>();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.jivesoftware.smack.RosterListener#entriesAdded(java.util.Collection)
	 */
	@Override
	public void entriesAdded(Collection<String> addresses)
	{
	} // unneeded, concerned with CRUD on Roster

	/*
	 * (non-Javadoc)
	 * @see
	 * org.jivesoftware.smack.RosterListener#entriesDeleted(java.util.Collection
	 * )
	 */
	@Override
	public void entriesDeleted(Collection<String> addresses)
	{
	} // unneeded, concerned with CRUD on Roster

	/*
	 * (non-Javadoc)
	 * @see
	 * org.jivesoftware.smack.RosterListener#entriesUpdated(java.util.Collection
	 * )
	 */
	@Override
	public void entriesUpdated(Collection<String> addresses)
	{
	} // unneeded, concerned with CRUD on Roster

	/**
	 * Gets the connection.
	 *
	 * @return the connection
	 */
	public XMPPConnection getConnection()
	{
		return connection;
	}

	/**
	 * Gets the current devices present.
	 *
	 * @return the current devices present
	 */
	public Collection<String> getCurrentDevicesPresent()
	{
		return Collections.unmodifiableCollection(currentDevicesPresent);
	}

	/**
	 * Gets the device names online.
	 *
	 * @param deviceType
	 *            the device type
	 * @return the device names online
	 */
	public List<String> getDeviceNamesOnline(String deviceType)
	{
		List<String> list = new ArrayList<String>();
		if (connection == null)
		{
			return list;
		}

		Roster roster = connection.getRoster();
		if (roster == null)
		{
			return list;
		}

		try
		{
			RosterGroup rg = roster.getGroup(deviceType);
			for (RosterEntry re : rg.getEntries())
			{
				Presence p = connection.getRoster().getPresence(re.getUser());
				if (p.isAvailable())
				{
					list.add(re.getName());
				}
			}
		}
		catch (NullPointerException e)
		{

		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.jivesoftware.smack.RosterListener#presenceChanged(org.jivesoftware
	 * .smack.packet.Presence)
	 */
	@Override
	public void presenceChanged(Presence presence)
	{
		log.fine("Presence change from " + presence.getFrom());
		String name = StringUtils.parseName(presence.getFrom());
		if (!isMultiplicityDevice(name))
		{
			return;
		}

		if (presence.isAvailable())
		{
			notifyDeviceAvailable(name);
		}
		else
		{
			notifyDeviceUnavailable(name);
		}
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

	// *** business logic ***

	/**
	 * Sets the connection.
	 *
	 * @param connection
	 *            the new connection
	 */
	public void setConnection(XMPPConnection connection)
	{
		this.connection = connection;
		roster = connection.getRoster();
		roster.addRosterListener(this);
		multiplicityGroup = roster.getGroup(MULTIPLICITY_DEVICES_GROUP);
		notifyCurrentPresence(connection);
	}

	/**
	 * Checks if is multiplicity device.
	 *
	 * @param from
	 *            the from
	 * @return true, if is multiplicity device
	 */
	private boolean isMultiplicityDevice(String from)
	{
		for (RosterEntry re : multiplicityGroup.getEntries())
		{
			if (re.getName().equals(from))
			{
				return true;
			}
		}
		return false;
	}

	// *** unneeded roster listener methods ***

	/**
	 * Notify current presence.
	 *
	 * @param connection
	 *            the connection
	 */
	private void notifyCurrentPresence(XMPPConnection connection)
	{
		if (multiplicityGroup == null)
		{
			log.log(Level.WARNING, "Could not find the multiplicity roster group.");
			return;
		}
		log.log(Level.FINE, "Loading roster entries.");
		for (RosterEntry re : multiplicityGroup.getEntries())
		{
			log.finer("Roster entry check availability on " + re.getUser());
			Presence presence = roster.getPresence(re.getUser());
			if (presence.isAvailable())
			{
				log.finer("  is available!");
				notifyDeviceAvailable(re.getName());
			}
		}
	}

	/**
	 * Notify device available.
	 *
	 * @param id
	 *            the id
	 */
	private void notifyDeviceAvailable(final String id)
	{
		synchronized (currentDevicesPresent)
		{
			if (currentDevicesPresent.add(id))
			{
				log.log(Level.FINER, "Added " + id + " to currentDevicesPresent list. Notifying listeners.");
				for (IPresenceListener l : listeners)
				{
					l.deviceAvailable(id);
				}
			}
			else
			{
				log.log(Level.FINER, "Already had " + id + " in the currentDevicesPresent list.");
			}
		}
	}

	/**
	 * Notify device unavailable.
	 *
	 * @param id
	 *            the id
	 */
	private void notifyDeviceUnavailable(final String id)
	{
		synchronized (currentDevicesPresent)
		{
			if (currentDevicesPresent.remove(id))
			{
				for (IPresenceListener l : listeners)
				{
					l.deviceUnavailable(id);
				}
			}
		}
	}

}
