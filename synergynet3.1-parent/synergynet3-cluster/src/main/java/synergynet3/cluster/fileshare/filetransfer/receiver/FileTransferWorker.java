package synergynet3.cluster.fileshare.filetransfer.receiver;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.jivesoftware.smackx.filetransfer.IncomingFileTransfer;

public class FileTransferWorker implements Runnable {
	private static final Logger log = Logger.getLogger(FileTransferWorker.class.getName());
	
	private IncomingFileTransfer transfer;
	private FileTransferWorkerDelegate delegate;

	public FileTransferWorker(FileTransferWorkerDelegate delegate, IncomingFileTransfer transfer) {
		this.delegate = delegate;
		this.transfer = transfer;
	}
	
	@Override
	public void run() {
		waitUntilTransferIsComplete();
		delegate.transferComplete();
	}

	private void waitUntilTransferIsComplete() {
		while(!transfer.isDone()) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				log.log(Level.WARNING, "Thread error.", e);
			}
		}		
	}
	
	public static interface FileTransferWorkerDelegate {
		public void transferComplete();
	}

	public void start() {
		Thread workerThread = new Thread(this);
		workerThread.setDaemon(true);
		workerThread.start();
	}

}
