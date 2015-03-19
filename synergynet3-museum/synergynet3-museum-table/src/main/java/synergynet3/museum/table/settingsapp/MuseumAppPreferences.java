package synergynet3.museum.table.settingsapp;

import java.io.File;

import synergynet3.fonts.FontColour;
import synergynet3.mediadetection.mediasearchtypes.ImageSearchType;
import synergynet3.museum.table.settingsapp.appearance.AppearanceConfigPrefsItem;
import synergynet3.museum.table.settingsapp.general.GeneralConfigPanel;
import synergynet3.museum.table.settingsapp.general.GeneralConfigPrefsItem;

import com.jme3.math.ColorRGBA;

public abstract class MuseumAppPreferences {
	
	private static final GeneralConfigPrefsItem miscConfigPrefsItem = new GeneralConfigPrefsItem();	
	
	private static final ImageSearchType IMAGE_CHECK = new ImageSearchType();
	
	//background screens
	public static ColorRGBA getBackgroundColour() {
		return SettingsUtil.getColorRGBA(AppearanceConfigPrefsItem.getValue(AppearanceConfigPrefsItem.BG_COLOUR));
	}
	
	//entity
	public static ColorRGBA getEntityBackgroundColour() {
		return SettingsUtil.getColorRGBA(AppearanceConfigPrefsItem.getValue(AppearanceConfigPrefsItem.ENTITY_BG_COLOUR));
	}
	
	public static ColorRGBA getEntityBorderColour() {
		return SettingsUtil.getColorRGBA(AppearanceConfigPrefsItem.getValue(AppearanceConfigPrefsItem.ENTITY_BORDER_COLOUR));
	}
	
	public static String getEntityBorderColourAsString() {
		return AppearanceConfigPrefsItem.getValue(AppearanceConfigPrefsItem.ENTITY_BORDER_COLOUR);
	}
	
	public static ColorRGBA getUserGeneratedContentColour() {
		return SettingsUtil.getColorRGBA(AppearanceConfigPrefsItem.getValue(AppearanceConfigPrefsItem.ENTITY_USER_GENERATED_COLOUR));
	}
	
	public static FontColour getEntityFontColour() {
		return SettingsUtil.getFontColour(AppearanceConfigPrefsItem.getValue(AppearanceConfigPrefsItem.ENTITY_FONT_COLOUR));
	}
	
	public static float getEntitySpread(){
		float toReturn = 0;
		String floatVal = AppearanceConfigPrefsItem.getValue(AppearanceConfigPrefsItem.ENTITY_SPREAD);
		try{
			toReturn = Float.parseFloat(floatVal);
		}catch(Exception e){
			toReturn = Float.parseFloat(AppearanceConfigPrefsItem.getDefaultValue(AppearanceConfigPrefsItem.ENTITY_SPREAD));
		}
		return toReturn;
	}
	
	//entity close
	public static String getEntityCloseButtonBackgroundColour() {
		return AppearanceConfigPrefsItem.getValue(AppearanceConfigPrefsItem.ENTITY_CLOSE_BG_COLOUR);
	}
	
	//entity links
	public static String getEntityLinkButtonBackgroundColour() {
		return AppearanceConfigPrefsItem.getValue(AppearanceConfigPrefsItem.ENTITY_LINK_BG_COLOUR);
	}
	

	public static String getEntityLinkText() {
		return AppearanceConfigPrefsItem.getValue(AppearanceConfigPrefsItem.ENTITY_LINK_TEXT);
	}
	
	//entity recording
	public static String getEntityRecordingButtonBackgroundColour() {
		return AppearanceConfigPrefsItem.getValue(AppearanceConfigPrefsItem.ENTITY_RECORDER_BG_COLOUR);
	}
	
	public static String[] getPrompts() {
		String prompts = AppearanceConfigPrefsItem.getValue(AppearanceConfigPrefsItem.RECORDING_PROMPTS);
		return prompts.split(AppearanceConfigPrefsItem.PROMPT_TOKEN);
	}	
	
	public static boolean isSinglePrompt(){
		if (AppearanceConfigPrefsItem.getValue(AppearanceConfigPrefsItem.RECORDING_SINGLE_PROMPT).equals(AppearanceConfigPrefsItem.YES)){
			return true;
		}else{
			return false;
		}
	}
	
	
	//poi	
	public static float getPoiWidth(){
		float toReturn = 0;
		String floatVal = AppearanceConfigPrefsItem.getValue(AppearanceConfigPrefsItem.POI_WIDTH);
		try{
			toReturn = Float.parseFloat(floatVal);
		}catch(Exception e){
			toReturn = Float.parseFloat(AppearanceConfigPrefsItem.getDefaultValue(AppearanceConfigPrefsItem.POI_WIDTH));
		}
		return toReturn;
	}

	public static String getPOIColourAsString() {
		return AppearanceConfigPrefsItem.getValue(AppearanceConfigPrefsItem.POI_COLOUR);
	}
	
	public static String getPOIBorderColour() {
		return AppearanceConfigPrefsItem.getValue(AppearanceConfigPrefsItem.POI_BORDER_COLOUR);
	}
	
	public static ColorRGBA getPOIColourAsColorRGBA() {
		return SettingsUtil.getColorRGBA(AppearanceConfigPrefsItem.getValue(AppearanceConfigPrefsItem.POI_COLOUR));
	}
	
	//lens
	public static String getLensCloseColour() {
		return AppearanceConfigPrefsItem.getValue(AppearanceConfigPrefsItem.LENS_CLOSE_COLOUR);
	}
	
	public static String getLensBackgroundColour() {
		return AppearanceConfigPrefsItem.getValue(AppearanceConfigPrefsItem.LENS_BG_COLOUR);
	}

	public static ColorRGBA getLensBorderColour() {
		return SettingsUtil.getColorRGBA(AppearanceConfigPrefsItem.getValue(AppearanceConfigPrefsItem.LENS_BORDER_COLOUR));
	}

	public static String getLensArrowColour() {
		return AppearanceConfigPrefsItem.getValue(AppearanceConfigPrefsItem.LENS_ARROW_COLOUR);
	}
	
	public static FontColour getLensFontColour() {
		return SettingsUtil.getFontColour(AppearanceConfigPrefsItem.getValue(AppearanceConfigPrefsItem.LENS_FONT_COLOUR));
	}
	
	//lens button
	public static ColorRGBA getLensButtonBackgroundColour() {
		return SettingsUtil.getColorRGBA(AppearanceConfigPrefsItem.getValue(AppearanceConfigPrefsItem.LENS_BUTTON_BG_COLOUR));
	}

	public static ColorRGBA getLensButtonBorderColour() {
		return SettingsUtil.getColorRGBA(AppearanceConfigPrefsItem.getValue(AppearanceConfigPrefsItem.LENS_BUTTON_BORDER_COLOUR));
	}
	
	public static FontColour getLensButtonFontColour() {
		return SettingsUtil.getFontColour(AppearanceConfigPrefsItem.getValue(AppearanceConfigPrefsItem.LENS_BUTTON_FONT_COLOUR));
	}
	
	public static String getLensButtonText(){
		return AppearanceConfigPrefsItem.getValue(AppearanceConfigPrefsItem.LENS_BUTTON_TEXT);
	}
		
    //recorder
	public static ColorRGBA getRecorderBackgroundColour() {
		return SettingsUtil.getColorRGBA(AppearanceConfigPrefsItem.getValue(AppearanceConfigPrefsItem.RECORDER_BG_COLOUR));
	}
	
	public static FontColour getRecorderFontColour() {
		return SettingsUtil.getFontColour(AppearanceConfigPrefsItem.getValue(AppearanceConfigPrefsItem.RECORDER_FONT_COLOUR));
	}
	
	public static ColorRGBA getRecorderActiveButtonBorderColour() {
		return SettingsUtil.getColorRGBA(AppearanceConfigPrefsItem.getValue(AppearanceConfigPrefsItem.RECORDER_ACTIVE_BUTTON_BORDER_COLOUR));
	}
	
	public static ColorRGBA getRecorderInactiveButtonBorderColour() {
		return SettingsUtil.getColorRGBA(AppearanceConfigPrefsItem.getValue(AppearanceConfigPrefsItem.RECORDER_INACTIVE_BUTTON_BORDER_COLOUR));
	}
	
	public static FontColour getRecorderActiveButtonFontColour() {
		return SettingsUtil.getFontColour(AppearanceConfigPrefsItem.getValue(AppearanceConfigPrefsItem.RECORDER_ACTIVE_BUTTON_FONT_COLOUR));
	}
	
	public static FontColour getRecorderInactiveButtonFontColour() {
		return SettingsUtil.getFontColour(AppearanceConfigPrefsItem.getValue(AppearanceConfigPrefsItem.RECORDER_INACTIVE_BUTTON_FONT_COLOUR));
	}
	
	public static String getRecorderText() {
		return AppearanceConfigPrefsItem.getValue(AppearanceConfigPrefsItem.RECORDER_TEXT);
	}
	
	
    //metrics GUI
	public static ColorRGBA getMetricGUIBackgroundColour() {
		return SettingsUtil.getColorRGBA(AppearanceConfigPrefsItem.getValue(AppearanceConfigPrefsItem.METRIC_BG_COLOUR));
	}
	
	public static ColorRGBA getMetricGUIBorderColour() {
		return SettingsUtil.getColorRGBA(AppearanceConfigPrefsItem.getValue(AppearanceConfigPrefsItem.METRIC_BORDER_COLOUR));
	}
	
	public static ColorRGBA getMetricGUIButtonColour() {
		return SettingsUtil.getColorRGBA(AppearanceConfigPrefsItem.getValue(AppearanceConfigPrefsItem.METRIC_BUTTON_COLOUR));
	}
	
	public static String getMetricGUIButtonColourAsString() {
		return AppearanceConfigPrefsItem.getValue(AppearanceConfigPrefsItem.METRIC_BUTTON_COLOUR);
	}
	
	public static FontColour getMetricGUIFontColour() {
		return SettingsUtil.getFontColour(AppearanceConfigPrefsItem.getValue(AppearanceConfigPrefsItem.METRIC_FONT_COLOUR));
	}
	
	public static String getMetricGUIText() {
		return AppearanceConfigPrefsItem.getValue(AppearanceConfigPrefsItem.METRIC_TEXT);
	}
	
	
	//text input GUI	
	public static ColorRGBA getTextInputBackgroundColour() {
		return SettingsUtil.getColorRGBA(AppearanceConfigPrefsItem.getValue(AppearanceConfigPrefsItem.TEXT_INPUT_BG_COLOUR));
	}
	
	public static ColorRGBA getTextInputKeyboardButtonBackgroundColour() {
		return SettingsUtil.getColorRGBA(AppearanceConfigPrefsItem.getValue(AppearanceConfigPrefsItem.TEXT_INPUT_KEYBOARD_BUTTON_BG_COLOUR));
	}
	
	public static ColorRGBA getTextInputKeyboardButtonBorderColour() {
		return SettingsUtil.getColorRGBA(AppearanceConfigPrefsItem.getValue(AppearanceConfigPrefsItem.TEXT_INPUT_KEYBOARD_BUTTON_BORDER_COLOUR));
	}
	
	public static FontColour getTextInputKeyboardButtonFontColour() {
		return SettingsUtil.getFontColour(AppearanceConfigPrefsItem.getValue(AppearanceConfigPrefsItem.TEXT_INPUT_KEYBOARD_BUTTON_FONT_COLOUR));
	}
	
	public static FontColour getTextInputScrollboxFontColour() {
		return SettingsUtil.getFontColour(AppearanceConfigPrefsItem.getValue(AppearanceConfigPrefsItem.TEXT_INPUT_SCROLLBOX_FONT_COLOUR));
	}
	
	public static FontColour getTextInputFontColour() {
		return SettingsUtil.getFontColour(AppearanceConfigPrefsItem.getValue(AppearanceConfigPrefsItem.TEXT_INPUT_FONT_COLOUR));
	}
	
	public static String getTextInputInstructions() {
		return AppearanceConfigPrefsItem.getValue(AppearanceConfigPrefsItem.TEXT_INPUT_TEXT);
	}
	
	//close button	
	public static String getCloseButtonColour() {
		return AppearanceConfigPrefsItem.getValue(AppearanceConfigPrefsItem.CLOSE_BUTTON_COLOUR);
	}
	
    //error message
	public static ColorRGBA getErrorColourAsRGBA() {
		return SettingsUtil.getColorRGBA(AppearanceConfigPrefsItem.getValue(AppearanceConfigPrefsItem.ERROR_MESSAGE_COLOUR));
	}
	
	public static FontColour getErrorColourAsFontColour() {
		return SettingsUtil.getFontColour(AppearanceConfigPrefsItem.getValue(AppearanceConfigPrefsItem.ERROR_MESSAGE_COLOUR));
	}
    
    //shutdown screen	
	public static ColorRGBA getShutdownBackgroundColour() {
		ColorRGBA colour = SettingsUtil.getColorRGBA(AppearanceConfigPrefsItem.getValue(AppearanceConfigPrefsItem.SHUTDOWN_BG_COLOUR));
		colour.set(colour.r, colour.g, colour.b, 0.5f);
		return colour;
	}
	
	public static ColorRGBA getShutdownControlsBackgroundColour() {
		return SettingsUtil.getColorRGBA(AppearanceConfigPrefsItem.getValue(AppearanceConfigPrefsItem.SHUTDOWN_CONTROLS_BG_COLOUR));
	}
	
	public static ColorRGBA getShutdownControlsBorderColour() {
		return SettingsUtil.getColorRGBA(AppearanceConfigPrefsItem.getValue(AppearanceConfigPrefsItem.SHUTDOWN_CONTROLS_BORDER_COLOUR));
	}
	
	public static FontColour getShutdownControlsFontColour() {
		return SettingsUtil.getFontColour(AppearanceConfigPrefsItem.getValue(AppearanceConfigPrefsItem.SHUTDOWN_CONTROLS_FONT_COLOUR));
	}
	public static String getShutdownInstructionsText() {
		return AppearanceConfigPrefsItem.getValue(AppearanceConfigPrefsItem.SHUTDOWN_INSTRUCTIONS_TEXT);
	}
	
	//general
	public static String getAdminPIN(){
		return miscConfigPrefsItem.getAdminPIN();
	}
	
	public static String getContentFolder(){
		return miscConfigPrefsItem.getContentFolder();
	}
	
	public static boolean areLocationsEnabled(){
		return miscConfigPrefsItem.getLocationsEnabled();
	}
	
	public static boolean areUserRecordingsEnabled(){
		return miscConfigPrefsItem.getUserRecordingsEnabled();
	}
	
	public static int getMaxRecordingTime(){
		return miscConfigPrefsItem.getMaxRecordingTime() * 1000;
	}

	public static boolean areMetricsEnabled(){
		if (miscConfigPrefsItem.getMetricsEnabled()){
			if (new File(getMetricsFolder()).isDirectory()){
				return true;
			}
		}
		return false;
	}
	
	public static String getMetricsFolder(){
		return miscConfigPrefsItem.getMetricsFolder();
	}
	
	public static File getBackgroundImage(){
		String loc = getContentFolder();
		if (loc.equals("")){
			return null;
		}
		loc += File.separator + GeneralConfigPanel.BACKGROUNDLOC;
		File folder = new File(loc);
		if (!folder.exists()){
			folder.mkdir();
		}
		File toReturn = null;
		for (File file: folder.listFiles()){
			if (IMAGE_CHECK.isFileOfSearchType(file)){
				toReturn = file;
			}
		}		
		return toReturn;
	}

}
