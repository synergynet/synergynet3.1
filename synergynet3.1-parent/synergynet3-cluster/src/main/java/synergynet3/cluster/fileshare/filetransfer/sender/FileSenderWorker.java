package synergynet3.cluster.fileshare.filetransfer.sender;

import java.io.File;
import java.io.IOException;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.filetransfer.FileTransferManager;
import org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer;

import synergynet3.cluster.fileshare.localfilecache.LocalFileCache;
import synergynet3.cluster.fileshare.localfilecache.MD5Hash;

/**
 * The Class FileSenderWorker.
 */
public class FileSenderWorker implements Runnable {

	/** The destination device. */
	private String destinationDevice;

	/** The file transfer manager. */
	private FileTransferManager fileTransferManager;

	/** The identify of file. */
	private MD5Hash identifyOfFile;

	/** The local file cache. */
	private LocalFileCache localFileCache;

	/**
	 * Instantiates a new file sender worker.
	 *
	 * @param localFileCache the local file cache
	 * @param fileTransferManager the file transfer manager
	 * @param deviceToSendToJID the device to send to jid
	 * @param identityOfFileToSend the identity of file to send
	 */
	public FileSenderWorker(LocalFileCache localFileCache,
			FileTransferManager fileTransferManager, String deviceToSendToJID,
			MD5Hash identityOfFileToSend) {
		this.localFileCache = localFileCache;
		this.fileTransferManager = fileTransferManager;
		this.destinationDevice = deviceToSendToJID;
		this.identifyOfFile = identityOfFileToSend;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		final OutgoingFileTransfer outgoingTransfer = fileTransferManager
				.createOutgoingFileTransfer(destinationDevice + "/Smack");
		try {
			File localFileToSend = localFileCache
					.getCachedFileForHashIdentity(identifyOfFile);
			outgoingTransfer.sendFile(localFileToSend,
					identifyOfFile.toString());
			Thread fileSendingThread = new Thread(new Runnable() {
				@Override
				public void run() {
					while (!outgoingTransfer.isDone()) {
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
						}
					}
				}
			}, "FileSender");
			fileSendingThread.setDaemon(true);
			fileSendingThread.start();
			fileSendingThread.join(); // TODO confirm that
										// handleFileTransferRequest will be in
										// a thread of its own
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XMPPException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Start.
	 */
	public void start() {
		Thread t = new Thread(this);
		t.setDaemon(true);
		t.start();
	}

}
