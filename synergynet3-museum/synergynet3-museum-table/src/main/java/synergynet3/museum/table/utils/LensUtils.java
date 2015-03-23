package synergynet3.museum.table.utils;

import java.io.File;
import java.util.ArrayList;

import synergynet3.mediadetection.mediasearchtypes.XMLSearchType;
import synergynet3.museum.table.settingsapp.MuseumAppPreferences;
import synergynet3.museum.table.settingsapp.lensmanager.LensXmlManager;

/**
 * The Class LensUtils.
 */
public class LensUtils {

	/** The Constant LENSES_FOLDER. */
	public static final String LENSES_FOLDER = "Lenses";

	/** The Constant XML_CHECK. */
	private static final XMLSearchType XML_CHECK = new XMLSearchType();

	/**
	 * Gets the lens colour.
	 *
	 * @param lens the lens
	 * @return the lens colour
	 */
	public static String getLensColour(String lens) {
		File lensesFolder = new File(MuseumAppPreferences.getContentFolder()
				+ File.separator + LENSES_FOLDER);
		if (!lensesFolder.isDirectory()) {
			lensesFolder.mkdir();
		}

		for (File file : lensesFolder.listFiles()) {
			if (XML_CHECK.isFileOfSearchType(file)) {
				LensXmlManager lensXmlManager = new LensXmlManager(file);
				lensXmlManager.regenerate();
				if (lensXmlManager.getName().equals(lens)) {
					return lensXmlManager.getColour();
				}
			} else {
				if (!file.delete()) {
					file.deleteOnExit();
				}
			}
		}
		return "Red";
	}

	/**
	 * Gets the lenses.
	 *
	 * @return the lenses
	 */
	public static String[] getLenses() {
		ArrayList<String> lenses = new ArrayList<String>();
		File lensesFolder = new File(MuseumAppPreferences.getContentFolder()
				+ File.separator + LENSES_FOLDER);
		if (!lensesFolder.isDirectory()) {
			lensesFolder.mkdir();
		}

		for (File file : lensesFolder.listFiles()) {
			if (XML_CHECK.isFileOfSearchType(file)) {
				LensXmlManager lensXmlManager = new LensXmlManager(file);
				lensXmlManager.regenerate();
				lenses.add(lensXmlManager.getName());
			} else {
				if (!file.delete()) {
					file.deleteOnExit();
				}
			}
		}
		String[] toReturn = new String[lenses.size()];
		int i = 0;
		for (String s : lenses) {
			toReturn[i] = s;
			i++;
		}
		return toReturn;
	}

}
