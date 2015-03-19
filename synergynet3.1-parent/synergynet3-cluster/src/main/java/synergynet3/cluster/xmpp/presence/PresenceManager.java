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

public class PresenceManager implements RosterListener {
	public static final String MULTIPLICITY_DEVICES_GROUP = "multiplicitydevices";

	private static final Logger log = Logger.getLogger(PresenceManager.class.getName());


	private Set<String> currentDevicesPresent;
	private List<IPresenceListener> listeners;
	private XMPPConnection connection;

	private RosterGroup multiplicityGroup;
	private Roster roster;


	public PresenceManager() {
		log.log(Level.FINE, "Creating XMPP Presence Manager");
		currentDevicesPresent = new HashSet<String>();		
		listeners = new ArrayList<IPresenceListener>();				
	}

	public void setConnection(XMPPConnection connection) {
		this.connection = connection;
		roster = connection.getRoster();
		roster.addRosterListener(this);
		multiplicityGroup = roster.getGroup(MULTIPLICITY_DEVICES_GROUP);
		notifyCurrentPresence(connection);
	}

	public XMPPConnection getConnection() {
		return connection;
	}


	private void notifyCurrentPresence(XMPPConnection connection) {
		if(multiplicityGroup == null) {
			log.log(Level.WARNING, "Could not find the multiplicity roster group.");
			return;
		}
		log.log(Level.FINE, "Loading roster entries.");
		for(RosterEntry re : multiplicityGroup.getEntries()) {
			log.finer("Roster entry check availability on " + re.getUser());
			Presence presence = roster.getPresence(re.getUser());
			if(presence.isAvailable()) {
				log.finer("  is available!");
				notifyDeviceAvailable(re.getName());	
			}
		}
	}

	public Collection<String> getCurrentDevicesPresent() {
		return Collections.unmodifiableCollection(currentDevicesPresent);
	}

	public List<String> getDeviceNamesOnline(String deviceType) {
		List<String> list = new ArrayList<String>();
		if(connection == null) return list;
		
		Roster roster = connection.getRoster();
		if(roster == null) return list;
		
		
		try{
			RosterGroup rg = roster.getGroup(deviceType);
			for(RosterEntry re : rg.getEntries()) {
				Presence p = connection.getRoster().getPresence(re.getUser());
				if(p.isAvailable()) {
					list.add(re.getName());
				}
			}
		}catch(NullPointerException e){
			
		}
		return list;
	}

	public void registerPresenceListener(IPresenceListener listener) {
		if(listeners.contains(listener)) return;
		listeners.add(listener);
	}

	@Override
	public void presenceChanged(Presence presence) {
		log.fine("Presence change from " + presence.getFrom());
		String name = StringUtils.parseName(presence.getFrom());
		if(!isMultiplicityDevice(name))  return;
		
		if(presence.isAvailable()) {
			notifyDeviceAvailable(name);
		}else {
			notifyDeviceUnavailable(name);
		}		
	}

	private boolean isMultiplicityDevice(String from) {
		for(RosterEntry re : multiplicityGroup.getEntries()) {
			if(re.getName().equals(from)) return true;
		}
		return false;
	}

	// *** business logic ***

	private void notifyDeviceUnavailable(final String id) {
		synchronized(currentDevicesPresent) {
			if(currentDevicesPresent.remove(id)) {
				for(IPresenceListener l : listeners) {			
					l.deviceUnavailable(id);	
				}			
			}
		}
	}

	private void notifyDeviceAvailable(final String id) {
		synchronized(currentDevicesPresent) {
			if(currentDevicesPresent.add(id)) {
				log.log(Level.FINER, "Added " + id + " to currentDevicesPresent list. Notifying listeners.");
				for(IPresenceListener l : listeners) {	
					l.deviceAvailable(id);				
				}
			}else{
				log.log(Level.FINER, "Already had " + id + " in the currentDevicesPresent list.");
			}		
		}
	}

	//*** unneeded roster listener methods ***

	@Override
	public void entriesAdded(Collection<String> addresses) {} // unneeded, concerned with CRUD on Roster

	@Override
	public void entriesUpdated(Collection<String> addresses) {} // unneeded, concerned with CRUD on Roster

	@Override
	public void entriesDeleted(Collection<String> addresses) {} // unneeded, concerned with CRUD on Roster

}
