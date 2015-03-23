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

/**
 * The Class SynergyNetCluster.
 */
public class SynergyNetCluster {

	/** The instance. */
	private static SynergyNetCluster instance;

	/** The Constant log. */
	private static final Logger log = Logger.getLogger(SynergyNetCluster.class
			.getName());

	/** The server config prefs item. */
	private static WebConfigPrefsItem serverConfigPrefsItem = new WebConfigPrefsItem();

	/** The device manager. */
	private ClusteredDeviceManager deviceManager;

	/** The file share utility. */
	private FileShareSystem fileShareUtility;

	/** The identity. */
	private String identity;

	/** The messaging manager. */
	private MessagingManager messagingManager;

	/** The presence manager. */
	private PresenceManager presenceManager;

	/** The xmpp. */
	private XMPPConnection xmpp;

	/**
	 * Instantiates a new synergy net cluster.
	 *
	 * @param identity the identity
	 */
	public SynergyNetCluster(String identity) {
		log.info("SynergyNetCluster() for " + identity);
		this.identity = identity;
	}

	/**
	 * Gets the.
	 *
	 * @return the synergy net cluster
	 */
	public static SynergyNetCluster get() {
		synchronized (SynergyNetCluster.class) {
			String identity = getIdentityFromArguments();
			if (instance == null) {
				instance = new SynergyNetCluster(identity);
				instance.getPresenceManager(); // forces cluster to go online
			}
			return instance;
		}
	}

	/**
	 * Gets the host from arguments.
	 *
	 * @return the host from arguments
	 */
	private static String getHostFromArguments() {
		String argument = ManagementFactory.getRuntimeMXBean()
				.getSystemProperties().get("synergynet3.host");
		if (argument != null) {
			return argument;
		}
		return serverConfigPrefsItem.getClusterHost();
	}

	/**
	 * Gets the identity from arguments.
	 *
	 * @return the identity from arguments
	 */
	private static String getIdentityFromArguments() {
		String argument = ManagementFactory.getRuntimeMXBean()
				.getSystemProperties().get("synergynet3.user");
		if (argument != null) {
			return argument;
		}
		return serverConfigPrefsItem.getClusterUserName();
	}

	/**
	 * Gets the password from arguments.
	 *
	 * @return the password from arguments
	 */
	private static String getPasswordFromArguments() {
		String argument = ManagementFactory.getRuntimeMXBean()
				.getSystemProperties().get("synergynet3.password");
		if (argument != null) {
			return argument;
		}
		return serverConfigPrefsItem.getClusterPassword();
	}

	/**
	 * Gets the port from arguments.
	 *
	 * @return the port from arguments
	 */
	private static int getPortFromArguments() {
		String argument = ManagementFactory.getRuntimeMXBean()
				.getSystemProperties().get("synergynet3.port");
		if (argument != null) {
			return Integer.parseInt(argument);
		}
		return serverConfigPrefsItem.getPort();
	}

	/**
	 * Gets the device cluster manager.
	 *
	 * @return the device cluster manager
	 */
	public ClusteredDeviceManager getDeviceClusterManager() {
		if (deviceManager == null) {
			Hazelcast.getDefaultInstance();
			this.deviceManager = new ClusteredDeviceManager(this.identity);
		}
		return deviceManager;
	}

	/**
	 * Gets the file share utility.
	 *
	 * @return the file share utility
	 */
	public FileShareSystem getFileShareUtility() {
		if (fileShareUtility == null) {
			fileShareUtility = new FileShareSystem(this.identity,
					getLocalFileCacheDirectory());
		}
		return fileShareUtility;
	}

	/**
	 * Gets the identity.
	 *
	 * @return the identity
	 */
	public String getIdentity() {
		return this.identity;
	}

	/**
	 * Gets the messaging manager.
	 *
	 * @return the messaging manager
	 */
	public MessagingManager getMessagingManager() {
		if (messagingManager == null) {
			messagingManager = new MessagingManager();
			messagingManager.setConnection(getXMPPConnection());
		}
		return messagingManager;
	}

	/**
	 * Gets the presence manager.
	 *
	 * @return the presence manager
	 */
	public PresenceManager getPresenceManager() {
		if (presenceManager == null) {
			presenceManager = new PresenceManager();
			presenceManager.setConnection(getXMPPConnection());
		}
		return presenceManager;

	}

	/**
	 * Gets the XMPP connection.
	 *
	 * @return the XMPP connection
	 */
	public XMPPConnection getXMPPConnection() {
		if (this.xmpp == null) {
			ConnectionConfiguration config = new ConnectionConfiguration(
					getHostFromArguments(), getPortFromArguments());
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

	/**
	 * Shutdown.
	 */
	public void shutdown() {
		xmpp.disconnect();
		Hazelcast.shutdownAll();
	}

	/**
	 * Gets the local file cache directory.
	 *
	 * @return the local file cache directory
	 */
	private File getLocalFileCacheDirectory() {
		File userHomeDirectory = new File(System.getProperty("user.home"));
		File synergynet3Directory = new File(userHomeDirectory, ".synergynet3");
		File identityDirectory = new File(synergynet3Directory, this.identity);
		File localFileCacheDirectory = new File(identityDirectory, "filecache");
		return localFileCacheDirectory;
	}
}
