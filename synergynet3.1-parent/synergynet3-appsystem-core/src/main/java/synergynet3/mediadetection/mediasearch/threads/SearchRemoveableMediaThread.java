package synergynet3.mediadetection.mediasearch.threads;

import java.io.File;
import java.util.ArrayList;

import synergynet3.mediadetection.mediasearch.Searcher;


public class SearchRemoveableMediaThread extends SearchThread {
	
	private File[] roots;
	private ArrayList<File> discoveredRoots = new ArrayList<File>();

	@Override
	protected void checkInitialDirectories() {
		roots = File.listRoots();
	}
	
	@Override
	protected void checkForNewDirectories(){
		if (roots.length != File.listRoots().length){
			if (roots.length < File.listRoots().length){
				for (File f : File.listRoots()){
					if (isNotPresentInRoots(f)){
						discoveredRoots.add(f);
						findFiles(f);
					}
				}

			}
			roots = File.listRoots();			
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

		ArrayList<File[]> allFilesOnDiscoveredRoots = new ArrayList<File[]>();
		for (File r: discoveredRoots){
			allFilesOnDiscoveredRoots.add(Searcher.searchDirectoryTree(r, mediaSearchTypes, order, numberToReturn));
		}
		int count = 0;
		for (File[] discovered: allFilesOnDiscoveredRoots){
			count += discovered.length;
		}
		File[] toReturn = new File[count];
		int i = 0;
		for (File[] discovered: allFilesOnDiscoveredRoots){
			for (File f: discovered){
				toReturn[i] = f;
				i++;
			}
		}
		return toReturn;
	}	
	
}
