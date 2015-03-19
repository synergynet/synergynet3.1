package synergynet3.cluster.fileshare.filetransfer.receiver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.filetransfer.FileTransferListener;
import org.jivesoftware.smackx.filetransfer.FileTransferRequest;
import org.jivesoftware.smackx.filetransfer.IncomingFileTransfer;

import synergynet3.cluster.fileshare.localfilecache.LocalFileCache;
import synergynet3.cluster.fileshare.localfilecache.LocalFileCacheEntry;
import synergynet3.cluster.fileshare.localfilecache.MD5Hash;


public class FileReceiver implements FileTransferListener {
	private static final Logger log = Logger.getLogger(FileReceiver.class.getName());
	private LocalFileCache cache;
	private Map<MD5Hash, String> hashToDevice;
	private FileReceiverDelegate delegate;

	public FileReceiver(FileReceiverDelegate delegate, LocalFileCache cache) {
		this.delegate = delegate;
		this.cache = cache;
		hashToDevice = new HashMap<MD5Hash, String>();
	}
	
	public void shouldReceiveFile(MD5Hash id, String device) {
		hashToDevice.put(id, device);
	}

	public void fileTransferRequest(final FileTransferRequest request) {
		log.fine("Received file transfer request " + request);
		final String uniqueFileIdentifier = request.getDescription();
		final MD5Hash hash = new MD5Hash(uniqueFileIdentifier);
		final File temporaryIncomingFile = getTemporaryFileToStoreIncomingTransfer();
		log.fine("File " + request.getFileName() + " being saved as " + temporaryIncomingFile.getAbsolutePath());
		
		final IncomingFileTransfer transfer = request.accept();		
		try {			
			transfer.recieveFile(temporaryIncomingFile);
			FileTransferWorker fileTransferWorker = new FileTransferWorker(new FileTransferWorker.FileTransferWorkerDelegate() {
				@Override
				public void transferComplete() {					
					fileTransferDidComplete(request.getFileName(), temporaryIncomingFile, hash, transfer);
				}

			}, transfer);
			fileTransferWorker.start();

		} catch (XMPPException ex) {
			log.log(Level.SEVERE, null, ex);
		}
	}

	protected void fileTransferDidComplete(String fileName, File fileLocation, MD5Hash hash, IncomingFileTransfer transfer) {
		log.fine("File " + fileLocation.getAbsolutePath() + " with id " + hash + " received successfully.");
		String device = hashToDevice.get(hash);
		try {
			LocalFileCacheEntry entry = cache.addFileToCacheWithName(fileLocation, fileName, device);
			delegate.newFileAddedToCache(entry);
		} catch (FileNotFoundException e) {
			delegate.errorReceivingFile(hash);
			log.log(Level.SEVERE, "Could not add file " + hash + " to cache", e);
		} catch (IOException e) {
			delegate.errorReceivingFile(hash);
			log.log(Level.SEVERE, "Could not add file " + hash + " to cache", e);
		}
		
	}

	private File getTemporaryFileToStoreIncomingTransfer() {
		try {
			File temporaryFile = File.createTempFile("mn_filexfer", "tmp");
			temporaryFile.deleteOnExit();
			return temporaryFile;
		} catch (IOException e) {
			log.log(Level.SEVERE, "Could not receive an incoming file transfer: couldn't create temporary file storage", e);
		}
		return null;
	}
	
	public static interface FileReceiverDelegate {
		void newFileAddedToCache(LocalFileCacheEntry entry);
		void errorReceivingFile(MD5Hash hash);
		
	}
}
