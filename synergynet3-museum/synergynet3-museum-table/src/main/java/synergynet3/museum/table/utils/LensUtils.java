package synergynet3.museum.table.utils;

import java.io.File;
import java.util.ArrayList;

import synergynet3.mediadetection.mediasearchtypes.XMLSearchType;
import synergynet3.museum.table.settingsapp.MuseumAppPreferences;
import synergynet3.museum.table.settingsapp.lensmanager.LensXmlManager;

public class LensUtils {
	
	public static final String LENSES_FOLDER = "Lenses";
	
	private static final XMLSearchType XML_CHECK = new XMLSearchType();

	public static String[] getLenses(){
		ArrayList<String> lenses = new ArrayList<String>();
		File lensesFolder = new File(MuseumAppPreferences.getContentFolder() + File.separator + LENSES_FOLDER);
		if (!lensesFolder.isDirectory()){
			lensesFolder.mkdir();
		}
		
		for (File file: lensesFolder.listFiles()){
			if (XML_CHECK.isFileOfSearchType(file)){
				LensXmlManager lensXmlManager = new LensXmlManager(file);
				lensXmlManager.regenerate();
				lenses.add(lensXmlManager.getName());
			}else{
				if (!file.delete()){
					file.deleteOnExit();
				}
			}
		}
		String[] toReturn = new String[lenses.size()];
		int i = 0;
		for (String s : lenses){
			toReturn[i] = s;
			i++;
	    }
		return toReturn;
	}
	
	public static String getLensColour(String lens){
		File lensesFolder = new File(MuseumAppPreferences.getContentFolder() + File.separator + LENSES_FOLDER);
		if (!lensesFolder.isDirectory()){
			lensesFolder.mkdir();
		}
		
		for (File file: lensesFolder.listFiles()){
			if (XML_CHECK.isFileOfSearchType(file)){
				LensXmlManager lensXmlManager = new LensXmlManager(file);
				lensXmlManager.regenerate();
				if (lensXmlManager.getName().equals(lens)){
					return lensXmlManager.getColour();
				}
			}else{
				if (!file.delete()){
					file.deleteOnExit();
				}
			}
		}
		return "Red";
	}
	
}
