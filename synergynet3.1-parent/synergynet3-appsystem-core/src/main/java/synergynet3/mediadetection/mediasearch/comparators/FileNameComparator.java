package synergynet3.mediadetection.mediasearch.comparators;

import java.io.File;
import java.util.Comparator;

/**
 * The Class FileNameComparator.
 */
public class FileNameComparator implements Comparator<File> {

	/*
	 * (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(File a, File b) {
		if (a.isDirectory() && !a.isDirectory()) {
			return -1;
		} else if (!a.isDirectory() && a.isDirectory()) {
			return 1;
		} else {
			return a.getName().compareToIgnoreCase(b.getName());
		}
	}

}
