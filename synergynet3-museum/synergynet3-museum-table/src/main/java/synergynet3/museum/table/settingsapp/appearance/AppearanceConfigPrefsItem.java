package synergynet3.museum.table.settingsapp.appearance;

import java.util.HashMap;

import javax.swing.JPanel;

import synergynet3.museum.table.settingsapp.MuseumAppPreferences;

import multiplicity3.config.PreferencesItem;

public class AppearanceConfigPrefsItem implements PreferencesItem {

	public static AppearanceXmlManager appearanceXMLManager = new AppearanceXmlManager(MuseumAppPreferences.getContentFolder());
	
	public static final String PROMPT_TOKEN = "\n";
	public static final String YES = "YES";
	public static final String NO = "NO";
	
	private static AppearanceConfigPanel panel;	
	
	private static final HashMap<String, String> defaultAppearanceValues = getDefaults();
	public static final String APPEARANCE_NODE = "Appearance";	
	
	//background
	public static final String BG_COLOUR = "BG_COLOUR";
	
	//entity
	public static final String ENTITY_BG_COLOUR = "ENTITY_BG_COLOUR";
	public static final String ENTITY_BORDER_COLOUR = "ENTITY_BORDER_COLOUR";
	public static final String ENTITY_USER_GENERATED_COLOUR = "ENTITY_USER_GENERATED_COLOUR";
	public static final String ENTITY_FONT_COLOUR = "ENTITY_FONT_COLOUR";
	public static final String ENTITY_SPREAD = "ENTITY_SPREAD";
	
	//entity close
	public static final String ENTITY_CLOSE_BG_COLOUR = "ENTITY_CLOSE_BG_COLOUR";
	
	//entity links
	public static final String ENTITY_LINK_BG_COLOUR = "ENTITY_LINK_BG_COLOUR";
	public static final String ENTITY_LINK_TEXT = "ENTITY_LINK_TEXT";
	
	//entity recording
	public static final String ENTITY_RECORDER_BG_COLOUR = "ENTITY_RECORDER_BG_COLOUR";
	
	//poi
	public static final String POI_WIDTH = "POI_WIDTH";	
	public static final String POI_COLOUR = "POI_COLOUR";
	public static final String POI_BORDER_COLOUR = "POI_BORDER_COLOUR";
	
	//recorder
	public static final String RECORDER_BG_COLOUR = "RECORDER_BG_COLOUR";
	public static final String RECORDER_ACTIVE_BUTTON_BORDER_COLOUR = "RECORDER_ACTIVE_BORDER_COLOUR";
	public static final String RECORDER_INACTIVE_BUTTON_BORDER_COLOUR = "RECORDER_BUTTON_BORDER_COLOUR";
	public static final String RECORDER_ACTIVE_BUTTON_FONT_COLOUR = "RECORDER_ACTIVE_BUTTON_FONT_COLOUR";
	public static final String RECORDER_INACTIVE_BUTTON_FONT_COLOUR = "RECORDER_INACTIVE_BUTTON_FONT_COLOUR";
	public static final String RECORDER_FONT_COLOUR = "RECORDER_FONT_COLOUR";
	public static final String RECORDER_TEXT = "RECORDER_TEXT";
	
	//text input
	public static final String TEXT_INPUT_BG_COLOUR = "TEXT_INPUT_BG_COLOUR";
	public static final String TEXT_INPUT_KEYBOARD_BUTTON_BG_COLOUR = "TEXT_INPUT_KEYBOARD_BUTTON_BG_COLOUR";
	public static final String TEXT_INPUT_KEYBOARD_BUTTON_BORDER_COLOUR = "TEXT_INPUT_KEYBOARD_BUTTON_BORDER_COLOUR";
	public static final String TEXT_INPUT_KEYBOARD_BUTTON_FONT_COLOUR = "TEXT_INPUT_KEYBOARD_BUTTON_FONT_COLOUR";
	public static final String TEXT_INPUT_SCROLLBOX_FONT_COLOUR = "TEXT_INPUT_SCROLLBOX_FONT_COLOUR";
	public static final String TEXT_INPUT_FONT_COLOUR = "TEXT_INPUT_FONT_COLOUR";
	public static final String TEXT_INPUT_TEXT = "TEXT_INPUT_TEXT";
	
	//metric collection GUI
	public static final String METRIC_BG_COLOUR = "METRIC_BG_COLOUR";
	public static final String METRIC_BORDER_COLOUR = "METRIC_BORDER_COLOUR";
	public static final String METRIC_BUTTON_COLOUR = "METRIC_BUTTON_BORDER_COLOUR";
	public static final String METRIC_FONT_COLOUR = "METRIC_FONT_COLOUR";
	public static final String METRIC_TEXT = "METRIC_TEXT";
	
	//lens
	public static final String LENS_CLOSE_COLOUR = "LENS_CLOSE_COLOUR";
	public static final String LENS_BORDER_COLOUR = "LENS_BORDER_COLOUR";
	public static final String LENS_BG_COLOUR = "LENS_BG_COLOUR";
	public static final String LENS_ARROW_COLOUR = "LENS_ARROW_COLOUR";
	public static final String LENS_FONT_COLOUR = "LENS_FONT_COLOUR";
	
	//lens button
	public static final String LENS_BUTTON_TEXT = "LENS_BUTTON_TEXT";
	public static final String LENS_BUTTON_BG_COLOUR = "LENS_BUTTON_BG_COLOUR";
	public static final String LENS_BUTTON_BORDER_COLOUR = "LENS_BUTTON_BORDER_COLOUR";
	public static final String LENS_BUTTON_FONT_COLOUR = "LENS_BUTTON_FONT_COLOUR";
	
	//close button
	public static final String CLOSE_BUTTON_COLOUR = "CLOSE_BUTTON_BG_COLOUR";
    
    //shutdown screen	
	public static final String SHUTDOWN_BG_COLOUR = "SHUTDOWN_BG_COLOUR";	
	public static final String SHUTDOWN_CONTROLS_BG_COLOUR = "KEYPAD_BG_COLOUR";		
	public static final String SHUTDOWN_CONTROLS_BORDER_COLOUR = "KEYPAD_BORDER_COLOUR";
	public static final String SHUTDOWN_CONTROLS_FONT_COLOUR = "KEYPAD_FONT_COLOUR";	
	public static final String SHUTDOWN_INSTRUCTIONS_TEXT = "SHUTDOWN_INSTRUCTIONS_TEXT";
	public static final String ERROR_MESSAGE_COLOUR = "ERROR_MESSAGE_FONT_COLOUR";	
	
	//prompts
	public static final String RECORDING_PROMPTS = "RECORDING_PROMPTS";
	public static final String RECORDING_SINGLE_PROMPT = "RECORDING_SINGLE_PROMPT";
	
	@Override
	public JPanel getConfigurationPanel() {
		panel = new AppearanceConfigPanel(this);
		loadFromXML();
		return panel;
	}
	
	public static HashMap<String, String> getDefaults(){
		HashMap<String, String> map = new HashMap<String, String>();		
		map.put(AppearanceConfigPrefsItem.BG_COLOUR, "Black");
		map.put(AppearanceConfigPrefsItem.ENTITY_BG_COLOUR, "Black");
		map.put(AppearanceConfigPrefsItem.ENTITY_BORDER_COLOUR, "Black");
		map.put(AppearanceConfigPrefsItem.ENTITY_USER_GENERATED_COLOUR, "Dark Grey");	
		map.put(AppearanceConfigPrefsItem.ENTITY_FONT_COLOUR, "Orange");	
		map.put(AppearanceConfigPrefsItem.ENTITY_SPREAD, "" + 200);	
		map.put(AppearanceConfigPrefsItem.ENTITY_LINK_BG_COLOUR, "Blue");	
		map.put(AppearanceConfigPrefsItem.ENTITY_LINK_TEXT, "Connected to:");		
		map.put(AppearanceConfigPrefsItem.ENTITY_RECORDER_BG_COLOUR, "Green");
		map.put(AppearanceConfigPrefsItem.ENTITY_CLOSE_BG_COLOUR, "Red");
		map.put(AppearanceConfigPrefsItem.POI_WIDTH, "" + 22.5);	
		map.put(AppearanceConfigPrefsItem.POI_COLOUR, "Red");
		map.put(AppearanceConfigPrefsItem.POI_BORDER_COLOUR, "White");
		map.put(AppearanceConfigPrefsItem.RECORDER_BG_COLOUR, "Black");
		map.put(AppearanceConfigPrefsItem.RECORDER_ACTIVE_BUTTON_BORDER_COLOUR, "Orange");
		map.put(AppearanceConfigPrefsItem.RECORDER_INACTIVE_BUTTON_BORDER_COLOUR, "Dark Grey");
		map.put(AppearanceConfigPrefsItem.RECORDER_ACTIVE_BUTTON_FONT_COLOUR, "Orange");	
		map.put(AppearanceConfigPrefsItem.RECORDER_INACTIVE_BUTTON_FONT_COLOUR, "Dark Grey");
		map.put(AppearanceConfigPrefsItem.RECORDER_FONT_COLOUR, "Orange");
		map.put(AppearanceConfigPrefsItem.RECORDER_TEXT, "Record your thoughts or stories here.  What you record may be used in future exhibits.");
		map.put(AppearanceConfigPrefsItem.TEXT_INPUT_BG_COLOUR, "Black");
		map.put(AppearanceConfigPrefsItem.TEXT_INPUT_KEYBOARD_BUTTON_BG_COLOUR, "Black");
		map.put(AppearanceConfigPrefsItem.TEXT_INPUT_KEYBOARD_BUTTON_BORDER_COLOUR, "Orange");
		map.put(AppearanceConfigPrefsItem.TEXT_INPUT_KEYBOARD_BUTTON_FONT_COLOUR, "Orange");
		map.put(AppearanceConfigPrefsItem.TEXT_INPUT_SCROLLBOX_FONT_COLOUR, "White");	
		map.put(AppearanceConfigPrefsItem.TEXT_INPUT_FONT_COLOUR, "Orange");
		map.put(AppearanceConfigPrefsItem.TEXT_INPUT_TEXT, "If you'd like to help us out by providing more information for future exhibits please leave some contact details:");
		map.put(AppearanceConfigPrefsItem.METRIC_BG_COLOUR, "Black");
		map.put(AppearanceConfigPrefsItem.METRIC_BORDER_COLOUR, "Black");
		map.put(AppearanceConfigPrefsItem.METRIC_BUTTON_COLOUR, "Orange");
		map.put(AppearanceConfigPrefsItem.METRIC_FONT_COLOUR, "Orange");
		map.put(AppearanceConfigPrefsItem.METRIC_TEXT, "Providing the following additional information about this recording will help us improve this exhibit.");
		map.put(AppearanceConfigPrefsItem.LENS_CLOSE_COLOUR, "Red");
		map.put(AppearanceConfigPrefsItem.LENS_BORDER_COLOUR, "Black");
		map.put(AppearanceConfigPrefsItem.LENS_BG_COLOUR, "Black");
		map.put(AppearanceConfigPrefsItem.LENS_ARROW_COLOUR, "Orange");	
		map.put(AppearanceConfigPrefsItem.LENS_FONT_COLOUR, "Orange");		
		map.put(AppearanceConfigPrefsItem.LENS_BUTTON_TEXT, "Add Lens");
		map.put(AppearanceConfigPrefsItem.LENS_BUTTON_BG_COLOUR, "Blue");
		map.put(AppearanceConfigPrefsItem.LENS_BUTTON_BORDER_COLOUR, "White");
		map.put(AppearanceConfigPrefsItem.LENS_BUTTON_FONT_COLOUR, "White");
		map.put(AppearanceConfigPrefsItem.CLOSE_BUTTON_COLOUR, "Red");	
		map.put(AppearanceConfigPrefsItem.SHUTDOWN_BG_COLOUR, "Dark Grey");
		map.put(AppearanceConfigPrefsItem.SHUTDOWN_CONTROLS_BG_COLOUR, "Black");			
		map.put(AppearanceConfigPrefsItem.SHUTDOWN_CONTROLS_BORDER_COLOUR, "White");
		map.put(AppearanceConfigPrefsItem.SHUTDOWN_CONTROLS_FONT_COLOUR, "White");
		map.put(AppearanceConfigPrefsItem.SHUTDOWN_INSTRUCTIONS_TEXT, "Enter the admin PIN for shutdown:");
		map.put(AppearanceConfigPrefsItem.ERROR_MESSAGE_COLOUR, "Red");	
		map.put(AppearanceConfigPrefsItem.RECORDING_PROMPTS, "Record your thoughts" + PROMPT_TOKEN + "What do you like about this?");	
		map.put(AppearanceConfigPrefsItem.RECORDING_SINGLE_PROMPT, NO);
		return map;
	}
	
	public static String getValue(String key){
		if (appearanceXMLManager.appearanceValues.containsKey(key)){
			return appearanceXMLManager.appearanceValues.get(key);
		}else{
			return defaultAppearanceValues.get(key);
		}
	}
	
	public static String getDefaultValue(String key){
		return defaultAppearanceValues.get(key);
	}
	
	public static void setValue(String key, String value){
		appearanceXMLManager.appearanceValues.put(key, value);
	}
	
	public static void loadFromXML() {
		appearanceXMLManager = new AppearanceXmlManager(MuseumAppPreferences.getContentFolder());
		panel.updateValues();
	}

	public static void savetoXML() {
		appearanceXMLManager.saveXML();
	}

	@Override
	public String getConfigurationPanelName() {
		return "Appearance";
	}

}
