package synergynet3.mediadetection.mediasearch.comparators;

import java.io.File;
import java.util.Comparator;

public class DateModifiedComparator implements Comparator<File> {
	
    public int compare(File a, File b) {
        if (a.isDirectory() && !a.isDirectory()) {
            return -1;

        } else if (!a.isDirectory() && a.isDirectory()) {
            return 1;

        } else {
        	long aLastModified = a.lastModified();
        	long bLastModified = b.lastModified();
        	if (aLastModified < bLastModified){
        		return -1;
        	}else if(aLastModified > bLastModified){
        		return 1;
        	}else{
        		return 0;
        	}

        }
    }

}
