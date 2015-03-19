package multiplicity3.commons.file;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DirectoryTools {
	public static List<String> getDirectoryNamesInsideDirectory(File dir) {
		if(!dir.exists()) return null;
		
		List<String> directoryNames = new ArrayList<String>();
		for(File f : dir.listFiles()) {
			if(f.isDirectory()) {
				directoryNames.add(f.getName());
			}
		}
		return directoryNames;
	}
}
