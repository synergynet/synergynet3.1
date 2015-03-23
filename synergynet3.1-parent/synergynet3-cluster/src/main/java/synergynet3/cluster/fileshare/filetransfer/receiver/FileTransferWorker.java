package synergynet3.cluster.fileshare.filetransfer.receiver;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.jivesoftware.smackx.filetransfer.IncomingFileTransfer;

/**
 * The Class FileTransferWorker.
 */
public class FileTransferWorker implements Runnable {

	/**
	 * The Interface FileTransferWorkerDelegate.
	 */
	public static interface FileTransferWorkerDelegate {

		/**
		 * Transfer complete.
		 */
		public void transferComplete();
	}

	/** The Constant log. */
	private static final Logger log = Logger.getLogger(FileTransferWorker.class
			.getName());

	/** The delegate. */
	private FileTransferWorkerDelegate delegate;

	/** The transfer. */
	private IncomingFileTransfer transfer;

	/**
	 * Instantiates a new file transfer worker.
	 *
	 * @param delegate the delegate
	 * @param transfer the transfer
	 */
	public FileTransferWorker(FileTransferWorkerDelegate delegate,
			IncomingFileTransfer transfer) {
		this.delegate = delegate;
		this.transfer = transfer;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		waitUntilTransferIsComplete();
		delegate.transferComplete();
	}

	/**
	 * Start.
	 */
	public void start() {
		Thread workerThread = new Thread(this);
		workerThread.setDaemon(true);
		workerThread.start();
	}

	/**
	 * Wait until transfer is complete.
	 */
	private void waitUntilTransferIsComplete() {
		while (!transfer.isDone()) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				log.log(Level.WARNING, "Thread error.", e);
			}
		}
	}

}
