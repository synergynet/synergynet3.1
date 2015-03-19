package synergynet3.mediadetection.mediasearch.comparators;

import java.io.File;
import java.util.Comparator;

public class FileNameComparator implements Comparator<File> {
	
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
