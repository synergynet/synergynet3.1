package synergynet3.mediadetection.mediasearchtypes;

import java.io.File;

public abstract class MediaSearchType {

	protected String[] extensions;
	
	public boolean isFileOfSearchType(File file){
		String fileExtension = getFileExtension(file);
		for (String extention: extensions){
			if (extention.toLowerCase().equals(fileExtension.toLowerCase())){
				return true;
			}
		}
		return false;
	}

	private String getFileExtension(File file) {
		String fileName = file.getName();
		String[] dividedByDots = fileName.split("\\.");
		return dividedByDots[dividedByDots.length-1];
	}

}
