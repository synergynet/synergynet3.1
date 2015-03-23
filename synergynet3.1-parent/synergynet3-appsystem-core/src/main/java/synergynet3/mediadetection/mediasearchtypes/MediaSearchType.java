package synergynet3.mediadetection.mediasearchtypes;

import java.io.File;

/**
 * The Class MediaSearchType.
 */
public abstract class MediaSearchType {

	/** The extensions. */
	protected String[] extensions;

	/**
	 * Checks if is file of search type.
	 *
	 * @param file the file
	 * @return true, if is file of search type
	 */
	public boolean isFileOfSearchType(File file) {
		String fileExtension = getFileExtension(file);
		for (String extention : extensions) {
			if (extention.toLowerCase().equals(fileExtension.toLowerCase())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Gets the file extension.
	 *
	 * @param file the file
	 * @return the file extension
	 */
	private String getFileExtension(File file) {
		String fileName = file.getName();
		String[] dividedByDots = fileName.split("\\.");
		return dividedByDots[dividedByDots.length - 1];
	}

}
