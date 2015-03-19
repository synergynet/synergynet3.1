package synergynet3.mediadetection.mediasearch.threads;

import java.io.File;
import java.util.logging.Level;

import synergynet3.mediadetection.IMediaSearcher;
import synergynet3.mediadetection.MediaDetection;
import synergynet3.mediadetection.MediaDetection.Ordering;
import synergynet3.mediadetection.mediasearchtypes.MediaSearchType;


public abstract class SearchThread implements Runnable{
	
	private boolean run = false;
	
	private static final int WAIT_TIME = 5000;
	
	private IMediaSearcher mediaSearcher;
	protected MediaSearchType[] mediaSearchTypes;
	protected Ordering order;
	protected int numberToReturn;
	private boolean initialised = false;
	
	public void initialize(IMediaSearcher mediaSearcher, MediaSearchType[] mediaSearchTypes, Ordering order, int numberToReturn){
		this.mediaSearcher = mediaSearcher;
		this.mediaSearchTypes= mediaSearchTypes;
		this.order = order;
		this.numberToReturn = numberToReturn;
		checkInitialDirectories();
		initialised = true;
	}
	
	public boolean isInitialised(){
		return initialised;
	}
	
	@Override
	public void run() {
		if (initialised){
			run = true;
			while (run){
				try {					
					Thread.sleep(WAIT_TIME);
					checkForNewDirectories();
				} catch (InterruptedException e) {
					MediaDetection.logMediaDetectionError(Level.SEVERE, "Interruption Exception.", e);
				}
			}
		}else{
			MediaDetection.logMediaDetectionError(Level.SEVERE, "Search thread has not been initialised yet.", null);
		}
	}
		
	public void stop(){
		run = false;
	}
	
	protected void filesFound(File[] files){
		mediaSearcher.onFind(files);
	}
	
	public void start() {
		Thread workerThread = new Thread(this);
		workerThread.setDaemon(true);
		workerThread.start();
	}

	public File[] reloadDiscoveredContents(){return new File[0];}
	protected void checkForNewDirectories(){}	
	protected void checkInitialDirectories(){}

}
