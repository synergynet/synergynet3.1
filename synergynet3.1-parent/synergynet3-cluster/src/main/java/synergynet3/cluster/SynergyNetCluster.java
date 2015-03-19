package synergynet3.cluster;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.util.logging.Logger;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import synergynet3.cluster.clustereddevice.ClusteredDeviceManager;
import synergynet3.cluster.fileshare.FileShareSystem;
import synergynet3.cluster.xmpp.messaging.MessagingManager;
import synergynet3.cluster.xmpp.presence.PresenceManager;
import synergynet3.config.web.WebConfigPrefsItem;

import com.hazelcast.core.Hazelcast;

public class SynergyNetCluster {
	private static final Logger log = Logger.getLogger(SynergyNetCluster.class.getName());

	private static SynergyNetCluster instance;
	private static WebConfigPrefsItem serverConfigPrefsItem = new WebConfigPrefsItem();

	public static SynergyNetCluster get() {
		synchronized(SynergyNetCluster.class) {
			String identity = getIdentityFromArguments();			
			if(instance == null) {
				instance = new SynergyNetCluster(identity);
				instance.getPresenceManager(); // forces cluster to go online
			}
			return instance;
		}
	}

	private ClusteredDeviceManager deviceManager;
	private PresenceManager presenceManager;
	private MessagingManager messagingManager;
	private FileShareSystem fileShareUtility;
	private String identity;

	private XMPPConnection xmpp;

	public SynergyNetCluster(String identity) {
		log.info("SynergyNetCluster() for " + identity);
		this.identity = identity;
	}

	public String getIdentity() {
		return this.identity;
	}

	public ClusteredDeviceManager getDeviceClusterManager() {
		if(deviceManager == null) {
			Hazelcast.getDefaultInstance();
			this.deviceManager = new ClusteredDeviceManager(this.identity);
		}
		return deviceManager;
	}

	public PresenceManager getPresenceManager() {
		if(presenceManager == null) {
			presenceManager = new PresenceManager();
			presenceManager.setConnection(getXMPPConnection());
		}
		return presenceManager;

	}

	public MessagingManager getMessagingManager() {
		if(messagingManager == null) {
			messagingManager = new MessagingManager();
			messagingManager.setConnection(getXMPPConnection());
		}
		return messagingManager;
	}

	public FileShareSystem getFileShareUtility() {
		if(fileShareUtility == null) {
			fileShareUtility = new FileShareSystem(this.identity, getLocalFileCacheDirectory());
		}
		return fileShareUtility;
	}

	public XMPPConnection getXMPPConnection() {
		if(this.xmpp == null) {
			ConnectionConfiguration config = new ConnectionConfiguration(getHostFromArguments(), getPortFromArguments());
			config.setSASLAuthenticationEnabled(false);
			config.setDebuggerEnabled(false);
			this.xmpp = new XMPPConnection(config);		
			try {
				xmpp.connect();
				xmpp.login(identity, getPasswordFromArguments());
			} catch (XMPPException e) {
				e.printStackTrace();
			}
		}
		return xmpp;
	}

	private File getLocalFileCacheDirectory() {
		File userHomeDirectory = new File(System.getProperty("user.home"));
		File synergynet3Directory = new File(userHomeDirectory, ".synergynet3");
		File identityDirectory = new File(synergynet3Directory, this.identity);
		File localFileCacheDirectory = new File(identityDirectory, "filecache");
		return localFileCacheDirectory;
	}

	private static String getHostFromArguments() {		
		String argument = ManagementFactory.getRuntimeMXBean().getSystemProperties().get("synergynet3.host");
		if (argument != null){
			return argument;
		}		
		return serverConfigPrefsItem.getClusterHost();
	}

	private static int getPortFromArguments() {
		String argument = ManagementFactory.getRuntimeMXBean().getSystemProperties().get("synergynet3.port");
		if (argument != null){
			return Integer.parseInt(argument);
		}		
		return serverConfigPrefsItem.getPort();
	}

	private static String getIdentityFromArguments() {
		String argument = ManagementFactory.getRuntimeMXBean().getSystemProperties().get("synergynet3.user");
		if (argument != null){
			return argument;
		}		
		return serverConfigPrefsItem.getClusterUserName();
	}
	
	private static String getPasswordFromArguments() {		
		String argument = ManagementFactory.getRuntimeMXBean().getSystemProperties().get("synergynet3.password");
		if (argument != null){
			return argument;
		}		
		return serverConfigPrefsItem.getClusterPassword();
	}

	public void shutdown() {
		xmpp.disconnect();
		Hazelcast.shutdownAll();
	}
}
