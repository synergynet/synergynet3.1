package synergynet3.mediadetection.mediasearch.threads;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;

import synergynet3.mediadetection.MediaDetection;
import synergynet3.mediadetection.mediasearch.Searcher;


public class SearchDirectoryThread extends SearchThread {
	
	private File directory;	
	private File[] roots;
	private boolean directorySet = false;
	private boolean isDetectingNewDrives = false;
	
	public void setDetectingNewDrives(){
		isDetectingNewDrives = true;
	}
	
	public void setDirectory(File directoryFile){
		directory = directoryFile;
		if (directory != null){
			if (directory.isDirectory()){
				directorySet = true;
			}else{
				MediaDetection.logMediaDetectionError(Level.SEVERE, "Location given is not a directory.", null);
			}
		}
	}

	public void setDirectory(String directoryAddress, boolean isURI){
		if (isURI){
			try {
				directory = new File(new URI(directoryAddress));
			} catch (URISyntaxException e) {
				MediaDetection.logMediaDetectionError(Level.SEVERE, "Location given is not a valid address.", e);
			}
		}else{
			directory = new File(directoryAddress);
		}
		if (directory != null){
			if (directory.isDirectory()){
				directorySet = true;
			}else{
				MediaDetection.logMediaDetectionError(Level.SEVERE, "Location given is not a directory.", null);
			}
		}
	}
	
	@Override
	protected void checkInitialDirectories() {
		if (directorySet){
			roots = directory.listFiles();
		}else{
			MediaDetection.logMediaDetectionError(Level.SEVERE, "Directory to search has not been set yet.", null);
		}
	}
	
	@Override
	protected void checkForNewDirectories(){
		if (roots.length != directory.listFiles().length){
			if (roots.length < directory.listFiles().length){
				if (!isDetectingNewDrives){
					findFiles(directory);
				}
				for (File f : directory.listFiles()){					
					if (isNotPresentInRoots(f)){
						if (f.isDirectory()){
							findFiles(f);
						}
					}
				}

			}
			roots = directory.listFiles();
		}
	}

	private void findFiles(File f){
		File[] foundFiles = null;
		foundFiles = Searcher.searchDirectoryTree(f, mediaSearchTypes, order, numberToReturn);
		if (foundFiles != null){
			if (foundFiles.length > 0){
				filesFound(foundFiles);
			}
		}
	}

	private boolean isNotPresentInRoots(File f) {
		boolean isNotPresent = true;
		for (File r : roots){
			if (r.equals(f)){
				isNotPresent = false;
				break;
			}
		}
		return isNotPresent;
	}
	
	@Override
	public File[] reloadDiscoveredContents(){
		return Searcher.searchDirectoryTree(directory, mediaSearchTypes, order, numberToReturn);
	}

}
