package synergynet3.mediadetection.mediasearch;

import java.io.File;
import java.util.Arrays;
import java.util.logging.Level;

import synergynet3.mediadetection.MediaDetection;
import synergynet3.mediadetection.MediaDetection.Ordering;
import synergynet3.mediadetection.mediasearch.comparators.DateModifiedComparator;
import synergynet3.mediadetection.mediasearch.comparators.FileNameComparator;
import synergynet3.mediadetection.mediasearchtypes.MediaSearchType;


public abstract class Searcher {
	
	public static File[] searchDirectoryTree(File file, MediaSearchType[] mediaSearchTypes, Ordering order, int numberToReturn){			
		File[] filesFound = {};
		filesFound = recursiveFileSearch(file, mediaSearchTypes, filesFound);		
		filesFound = organiseFilesFound(filesFound, order, numberToReturn);		
		return filesFound;		
	}
	
	private static File[] recursiveFileSearch(File file, MediaSearchType[] mediaSearchTypes, File[] filesFound) {
		if (file.isDirectory()) {
	        for (String s: file.list()) {
	        	filesFound = recursiveFileSearch(new File(file, s), mediaSearchTypes, filesFound);
	        }
	    }else if(file.isFile()){
	    	if (fileIsOfMediaTypes(file, mediaSearchTypes)){
	    		filesFound = addToFilesFound(file, filesFound);
	    	}
	    }else{
			MediaDetection.logMediaDetectionError(Level.INFO, "File is not a directory or a file?", null);
	    }
		return filesFound;
	}

	private static File[] addToFilesFound(File file, File[] filesFound) {
		File[] newFilesFound = new File[filesFound.length + 1];
		for (int i = 0; i < filesFound.length; i++){
			newFilesFound[i] = filesFound[i];
		}
		newFilesFound[filesFound.length] = file;
		return newFilesFound;
	}

	private static boolean fileIsOfMediaTypes(File f, MediaSearchType[] mediaSearchTypes){
		for (MediaSearchType searchType: mediaSearchTypes){
			if (searchType.isFileOfSearchType(f)){
				return true;
			}
		}
		return false;		
	}	
	
	private static File[] organiseFilesFound(File[] filesFound, Ordering order,	int numberToReturn) {
		if (filesFound.length > 1){
			filesFound = sortFilesFound(filesFound, order);
			if (numberToReturn > 0){
				if (numberToReturn < filesFound.length){
					filesFound = reduceNumberOfFilesFound(filesFound, numberToReturn);
				}
			}
		}
		return filesFound;
	}
	
	private static File[] reduceNumberOfFilesFound(File[] filesFound, int numberToReturn) {
		File[] newFilesFound = new File[numberToReturn];
		for (int i = 0; i < numberToReturn; i++){
			newFilesFound[i] = filesFound[i];
		}
		return newFilesFound;
	}

	private static File[] sortFilesFound(File[] filesFound, Ordering order) {
		
		if (order.equals(Ordering.DATE_MODIFIED)){
			DateModifiedComparator comparator = new DateModifiedComparator();
			Arrays.sort(filesFound, comparator);
		}else if (order.equals(Ordering.ALPHABETICAL)){
			FileNameComparator comparator = new FileNameComparator();
			Arrays.sort(filesFound, comparator);
		}else{
			MediaDetection.logMediaDetectionError(Level.INFO, "Undefined ordering type given.", null);
		}

		return filesFound;
	}

}
