package synergynet3.museum.table.utils;

import synergynet3.museum.table.settingsapp.SettingsUtil;

public class ImageUtils {
	
	public static final String RESOURCE_DIR = "synergynet3/museum/table/";
	
	public static String getImage(String colour, String imageLoc, String suffix){
		if (colour.equals(SettingsUtil.COLOUR_CHOICE[0])){
			imageLoc+=  "white" + suffix;
		}else if (colour.equals(SettingsUtil.COLOUR_CHOICE[1])){
			imageLoc+=  "black" + suffix;
		}else if (colour.equals(SettingsUtil.COLOUR_CHOICE[2])){
			imageLoc+=  "red" + suffix;
		}else if (colour.equals(SettingsUtil.COLOUR_CHOICE[3])){
			imageLoc+=  "blue" + suffix;
		}else if (colour.equals(SettingsUtil.COLOUR_CHOICE[4])){
			imageLoc+=  "yellow" + suffix;
		}else if (colour.equals(SettingsUtil.COLOUR_CHOICE[5])){
			imageLoc+=  "green" + suffix;
		}else if (colour.equals(SettingsUtil.COLOUR_CHOICE[6])){
			imageLoc+=  "orange" + suffix;
		}else if (colour.equals(SettingsUtil.COLOUR_CHOICE[7])){
			imageLoc+=  "pink" + suffix;
		}else if (colour.equals(SettingsUtil.COLOUR_CHOICE[8])){
			imageLoc+=  "cyan" + suffix;
		}else if (colour.equals(SettingsUtil.COLOUR_CHOICE[9])){
			imageLoc+=  "grey" + suffix;
		}else if (colour.equals(SettingsUtil.COLOUR_CHOICE[10])){
			imageLoc+=  "darkgrey" + suffix;
		}else if (colour.equals(SettingsUtil.COLOUR_CHOICE[11])){
			imageLoc = RESOURCE_DIR + "/blank.png";
		}
		return imageLoc;
	}
}
