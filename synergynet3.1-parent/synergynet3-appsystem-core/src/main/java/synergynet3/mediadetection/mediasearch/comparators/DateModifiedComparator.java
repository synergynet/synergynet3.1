package synergynet3.mediadetection.mediasearch.comparators;

import java.io.File;
import java.util.Comparator;

/**
 * The Class DateModifiedComparator.
 */
public class DateModifiedComparator implements Comparator<File> {

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
			long aLastModified = a.lastModified();
			long bLastModified = b.lastModified();
			if (aLastModified < bLastModified) {
				return -1;
			} else if (aLastModified > bLastModified) {
				return 1;
			} else {
				return 0;
			}

		}
	}

}
