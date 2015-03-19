package synergynet3.cluster.fileshare;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jivesoftware.smackx.filetransfer.FileTransferManager;
import org.jivesoftware.smackx.filetransfer.FileTransferNegotiator;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.IMap;

import synergynet3.cluster.SynergyNetCluster;
import synergynet3.cluster.fileshare.filetransfer.receiver.FileReceiver;
import synergynet3.cluster.fileshare.filetransfer.receiver.FileReceiver.FileReceiverDelegate;
import synergynet3.cluster.fileshare.filetransfer.sender.FileSenderWorker;
import synergynet3.cluster.fileshare.localfilecache.LocalFileCache;
import synergynet3.cluster.fileshare.localfilecache.LocalFileCacheEntry;
import synergynet3.cluster.fileshare.localfilecache.MD5Hash;
import synergynet3.cluster.fileshare.messages.FileDistributionMessage;
import synergynet3.cluster.fileshare.messages.RequestFileTransfer;
import synergynet3.cluster.threading.ClusterThreadManager;
import synergynet3.cluster.xmpp.messaging.IMessageListener;
import synergynet3.cluster.xmpp.messaging.Message;
import synergynet3.cluster.xmpp.messaging.MessagingManager;

public class FileShareSystem implements IMessageListener, FileReceiverDelegate {
	private static final String FILESHAREMAPNAME = "multiplicityfilesharemap";
	private static final Logger log = Logger.getLogger(FileShareSystem.class.getName());
	
	private LocalFileCache localFileCache;
	private FileReceiver fileReceiver;
	private String deviceID;
	private FileTransferManager fileTransferManager;
	private MessagingManager messagingManager;
	private Map<MD5Hash, FileShareSystemContentRegistrationDelegate> fileShareDelegatesByContentID;

	public FileShareSystem(String deviceID, File localCacheDirectory) {
		this.deviceID = deviceID;
		fileShareDelegatesByContentID = new HashMap<MD5Hash, FileShareSystemContentRegistrationDelegate>();
		this.localFileCache = new LocalFileCache(localCacheDirectory);
		FileTransferNegotiator.IBB_ONLY = true;
		fileReceiver = new FileReceiver(this, this.localFileCache);
		fileTransferManager = new FileTransferManager(SynergyNetCluster.get().getXMPPConnection());
		log.fine("Registering fileReceiver with file transfer manager");
		fileTransferManager.addFileTransferListener(fileReceiver);
		messagingManager = SynergyNetCluster.get().getMessagingManager();
		messagingManager.registerMessageListener(this, FileDistributionMessage.class);
	}	
	
	public MD5Hash registerContent(File localFile) throws FileNotFoundException, IOException {
		log.fine("Attempting to register " + localFile);
		LocalFileCacheEntry entry = localFileCache.addFileToCache(localFile, deviceID);
		MD5Hash id = entry.getHash();
		boolean didRegisterWithFileShareMap = registerWithFileShareMap(id, deviceID);
		if(!didRegisterWithFileShareMap) {
			log.log(Level.WARNING, "Was not able to register " + id + " to " + deviceID + " in shared map");
			return null;		
		}else{
			log.fine("Successfully registered local content.");
			return id;
		}
	}
	
	public void retrieveContent(final MD5Hash contentID, final FileShareSystemContentRegistrationDelegate delegate, long timeoutMillis) throws IOException {
		log.fine("Request for " + contentID);
		if(localFileCache.cacheContainsFileWithHashIdentity(contentID)) {
			log.fine("Found " + contentID + "in local cache.");
			notifyDelegateThatFileExistsLocally(contentID, delegate);
		}else{
			log.fine("Need to fetch " + contentID);
			fileShareDelegatesByContentID.put(contentID, delegate);
			String device = getDeviceThatHasFileDescribedByHash(contentID);
			
			if(device != null) {
				log.fine("Found that " + device + " has " + contentID);
				askDeviceForFile(device, contentID, delegate);
			}else{
				log.warning("Could not find a device owner for " + contentID);
				ClusterThreadManager.get().enqueue(new Callable<Void>() {
					@Override
					public Void call() throws Exception {
						delegate.contentNotReceived(contentID);
						return null;
					}
				});
			}
		}
	}
	
	private void askDeviceForFile(String device, MD5Hash contentID, FileShareSystemContentRegistrationDelegate delegate) {
		log.fine("Setup file receiver for " + contentID);
		fileReceiver.shouldReceiveFile(contentID, device);
		RequestFileTransfer rft = new RequestFileTransfer(this.deviceID, contentID, device);
		log.fine("Send message to " + device + " to send us the file.");
		SynergyNetCluster.get().getMessagingManager().sendMessageToDevices(rft, device);
	}

	private String getDeviceThatHasFileDescribedByHash(MD5Hash contentID) {
		String device = getFileShareLocationMap().get(contentID);
		return device;
	}

	private void notifyDelegateThatFileExistsLocally(final MD5Hash contentID,
			final FileShareSystemContentRegistrationDelegate delegate) throws IOException {
		final File localFile = localFileCache.getCachedFileForHashIdentity(contentID);		
		ClusterThreadManager.get().enqueue(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				delegate.contentReceived(contentID, localFile.toURI().toURL(), true);
				return null;
			}			
		});				
	}

	private boolean registerWithFileShareMap(MD5Hash id, String device) {
		log.fine("Associating " + id + " with " + device);
		if(getFileShareLocationMap().keySet().contains(id)) return false;
		getFileShareLocationMap().put(id, device);
		return true;
	}

	IMap<MD5Hash, String> getFileShareLocationMap() {
		return Hazelcast.getMap(FILESHAREMAPNAME);
	}

	@Override
	public void messageReceived(Message message) {
		if(message instanceof RequestFileTransfer) {
			handleFileTransferRequest((RequestFileTransfer) message);
		}
	}

	private void handleFileTransferRequest(RequestFileTransfer message) {		
		String deviceToSendToJID = messagingManager.deviceNameToJID(message.getDeviceAsking());		
		MD5Hash identityOfFileToSend = message.getIDOfFile();
		log.fine("Received a file transfer request for " + identityOfFileToSend + " to be sent to " + deviceToSendToJID);
		FileSenderWorker sendFileWorker = new FileSenderWorker(localFileCache, fileTransferManager, deviceToSendToJID, identityOfFileToSend);
		sendFileWorker.start();
	}

	@Override
	public void newFileAddedToCache(LocalFileCacheEntry entry) {
		final MD5Hash identityHash = entry.getHash();
		log.fine("File added to local cache: " + entry.getHash() + " @" + entry.getFile());
		final FileShareSystemContentRegistrationDelegate delegate = fileShareDelegatesByContentID.get(identityHash);
		if(delegate != null) {
			log.fine("Notifying delegate that content was received for " + identityHash + " @" + entry.getFile());
			try {
				final URL url = entry.getFile().toURI().toURL();
				ClusterThreadManager.get().enqueue(new Callable<Void>() {
					@Override
					public Void call() throws Exception {
						delegate.contentReceived(identityHash, url, false);
						return null;		
					}
				});				
				fileShareDelegatesByContentID.remove(identityHash);
			} catch (MalformedURLException e) {}			
		}
	}

	@Override
	public void errorReceivingFile(final MD5Hash hash) {
		final FileShareSystemContentRegistrationDelegate delegate = fileShareDelegatesByContentID.get(hash);
		if(delegate != null) {
			ClusterThreadManager.get().enqueue(new Callable<Void>() {
				@Override
				public Void call() throws Exception {
					delegate.contentNotReceived(hash);
					return null;
				}
			});
		}
		fileShareDelegatesByContentID.remove(hash);
	}

}
