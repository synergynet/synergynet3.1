package multiplicity3.ioutils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {

	public static String getExtension(File file) {
		return getExtension(file.getName());
	}

	public static String getExtension(String fileName) {
		int lastdot = fileName.lastIndexOf('.');
		if(lastdot < 0) return null;
		return fileName.substring(lastdot+1, fileName.length());
	}

	public static List<String> readAsStringList(InputStream is, boolean ignoreEmptyLines) throws IOException {
		List<String> contents = new ArrayList<String>();
		
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String line;
		
		while((line = br.readLine()) != null) {
			if(ignoreEmptyLines && line.length() <= 0) {
				// ignore
			}else{
				contents.add(line);
			}
		}
		br.close();
		
		return contents;
	}
	
}
