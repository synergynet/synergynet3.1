package synergynet3.museum.table.settingsapp;

import java.io.File;

import synergynet3.fonts.FontColour;
import synergynet3.mediadetection.mediasearchtypes.ImageSearchType;
import synergynet3.museum.table.settingsapp.appearance.AppearanceConfigPrefsItem;
import synergynet3.museum.table.settingsapp.general.GeneralConfigPanel;
import synergynet3.museum.table.settingsapp.general.GeneralConfigPrefsItem;

import com.jme3.math.ColorRGBA;

/**
 * The Class MuseumAppPreferences.
 */
public abstract class MuseumAppPreferences {

	/** The Constant IMAGE_CHECK. */
	private static final ImageSearchType IMAGE_CHECK = new ImageSearchType();

	/** The Constant miscConfigPrefsItem. */
	private static final GeneralConfigPrefsItem miscConfigPrefsItem = new GeneralConfigPrefsItem();

	/**
	 * Are locations enabled.
	 *
	 * @return true, if successful
	 */
	public static boolean areLocationsEnabled() {
		return miscConfigPrefsItem.getLocationsEnabled();
	}

	/**
	 * Are metrics enabled.
	 *
	 * @return true, if successful
	 */
	public static boolean areMetricsEnabled() {
		if (miscConfigPrefsItem.getMetricsEnabled()) {
			if (new File(getMetricsFolder()).isDirectory()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Are user recordings enabled.
	 *
	 * @return true, if successful
	 */
	public static boolean areUserRecordingsEnabled() {
		return miscConfigPrefsItem.getUserRecordingsEnabled();
	}

	// general
	/**
	 * Gets the admin pin.
	 *
	 * @return the admin pin
	 */
	public static String getAdminPIN() {
		return miscConfigPrefsItem.getAdminPIN();
	}

	// background screens
	/**
	 * Gets the background colour.
	 *
	 * @return the background colour
	 */
	public static ColorRGBA getBackgroundColour() {
		return SettingsUtil.getColorRGBA(AppearanceConfigPrefsItem
				.getValue(AppearanceConfigPrefsItem.BG_COLOUR));
	}

	/**
	 * Gets the background image.
	 *
	 * @return the background image
	 */
	public static File getBackgroundImage() {
		String loc = getContentFolder();
		if (loc.equals("")) {
			return null;
		}
		loc += File.separator + GeneralConfigPanel.BACKGROUNDLOC;
		File folder = new File(loc);
		if (!folder.exists()) {
			folder.mkdir();
		}
		File toReturn = null;
		for (File file : folder.listFiles()) {
			if (IMAGE_CHECK.isFileOfSearchType(file)) {
				toReturn = file;
			}
		}
		return toReturn;
	}

	// close button
	/**
	 * Gets the close button colour.
	 *
	 * @return the close button colour
	 */
	public static String getCloseButtonColour() {
		return AppearanceConfigPrefsItem
				.getValue(AppearanceConfigPrefsItem.CLOSE_BUTTON_COLOUR);
	}

	/**
	 * Gets the content folder.
	 *
	 * @return the content folder
	 */
	public static String getContentFolder() {
		return miscConfigPrefsItem.getContentFolder();
	}

	// entity
	/**
	 * Gets the entity background colour.
	 *
	 * @return the entity background colour
	 */
	public static ColorRGBA getEntityBackgroundColour() {
		return SettingsUtil.getColorRGBA(AppearanceConfigPrefsItem
				.getValue(AppearanceConfigPrefsItem.ENTITY_BG_COLOUR));
	}

	/**
	 * Gets the entity border colour.
	 *
	 * @return the entity border colour
	 */
	public static ColorRGBA getEntityBorderColour() {
		return SettingsUtil.getColorRGBA(AppearanceConfigPrefsItem
				.getValue(AppearanceConfigPrefsItem.ENTITY_BORDER_COLOUR));
	}

	/**
	 * Gets the entity border colour as string.
	 *
	 * @return the entity border colour as string
	 */
	public static String getEntityBorderColourAsString() {
		return AppearanceConfigPrefsItem
				.getValue(AppearanceConfigPrefsItem.ENTITY_BORDER_COLOUR);
	}

	// entity close
	/**
	 * Gets the entity close button background colour.
	 *
	 * @return the entity close button background colour
	 */
	public static String getEntityCloseButtonBackgroundColour() {
		return AppearanceConfigPrefsItem
				.getValue(AppearanceConfigPrefsItem.ENTITY_CLOSE_BG_COLOUR);
	}

	/**
	 * Gets the entity font colour.
	 *
	 * @return the entity font colour
	 */
	public static FontColour getEntityFontColour() {
		return SettingsUtil.getFontColour(AppearanceConfigPrefsItem
				.getValue(AppearanceConfigPrefsItem.ENTITY_FONT_COLOUR));
	}

	// entity links
	/**
	 * Gets the entity link button background colour.
	 *
	 * @return the entity link button background colour
	 */
	public static String getEntityLinkButtonBackgroundColour() {
		return AppearanceConfigPrefsItem
				.getValue(AppearanceConfigPrefsItem.ENTITY_LINK_BG_COLOUR);
	}

	/**
	 * Gets the entity link text.
	 *
	 * @return the entity link text
	 */
	public static String getEntityLinkText() {
		return AppearanceConfigPrefsItem
				.getValue(AppearanceConfigPrefsItem.ENTITY_LINK_TEXT);
	}

	// entity recording
	/**
	 * Gets the entity recording button background colour.
	 *
	 * @return the entity recording button background colour
	 */
	public static String getEntityRecordingButtonBackgroundColour() {
		return AppearanceConfigPrefsItem
				.getValue(AppearanceConfigPrefsItem.ENTITY_RECORDER_BG_COLOUR);
	}

	/**
	 * Gets the entity spread.
	 *
	 * @return the entity spread
	 */
	public static float getEntitySpread() {
		float toReturn = 0;
		String floatVal = AppearanceConfigPrefsItem
				.getValue(AppearanceConfigPrefsItem.ENTITY_SPREAD);
		try {
			toReturn = Float.parseFloat(floatVal);
		} catch (Exception e) {
			toReturn = Float.parseFloat(AppearanceConfigPrefsItem
					.getDefaultValue(AppearanceConfigPrefsItem.ENTITY_SPREAD));
		}
		return toReturn;
	}

	/**
	 * Gets the error colour as font colour.
	 *
	 * @return the error colour as font colour
	 */
	public static FontColour getErrorColourAsFontColour() {
		return SettingsUtil.getFontColour(AppearanceConfigPrefsItem
				.getValue(AppearanceConfigPrefsItem.ERROR_MESSAGE_COLOUR));
	}

	// error message
	/**
	 * Gets the error colour as rgba.
	 *
	 * @return the error colour as rgba
	 */
	public static ColorRGBA getErrorColourAsRGBA() {
		return SettingsUtil.getColorRGBA(AppearanceConfigPrefsItem
				.getValue(AppearanceConfigPrefsItem.ERROR_MESSAGE_COLOUR));
	}

	/**
	 * Gets the lens arrow colour.
	 *
	 * @return the lens arrow colour
	 */
	public static String getLensArrowColour() {
		return AppearanceConfigPrefsItem
				.getValue(AppearanceConfigPrefsItem.LENS_ARROW_COLOUR);
	}

	/**
	 * Gets the lens background colour.
	 *
	 * @return the lens background colour
	 */
	public static String getLensBackgroundColour() {
		return AppearanceConfigPrefsItem
				.getValue(AppearanceConfigPrefsItem.LENS_BG_COLOUR);
	}

	/**
	 * Gets the lens border colour.
	 *
	 * @return the lens border colour
	 */
	public static ColorRGBA getLensBorderColour() {
		return SettingsUtil.getColorRGBA(AppearanceConfigPrefsItem
				.getValue(AppearanceConfigPrefsItem.LENS_BORDER_COLOUR));
	}

	// lens button
	/**
	 * Gets the lens button background colour.
	 *
	 * @return the lens button background colour
	 */
	public static ColorRGBA getLensButtonBackgroundColour() {
		return SettingsUtil.getColorRGBA(AppearanceConfigPrefsItem
				.getValue(AppearanceConfigPrefsItem.LENS_BUTTON_BG_COLOUR));
	}

	/**
	 * Gets the lens button border colour.
	 *
	 * @return the lens button border colour
	 */
	public static ColorRGBA getLensButtonBorderColour() {
		return SettingsUtil.getColorRGBA(AppearanceConfigPrefsItem
				.getValue(AppearanceConfigPrefsItem.LENS_BUTTON_BORDER_COLOUR));
	}

	/**
	 * Gets the lens button font colour.
	 *
	 * @return the lens button font colour
	 */
	public static FontColour getLensButtonFontColour() {
		return SettingsUtil.getFontColour(AppearanceConfigPrefsItem
				.getValue(AppearanceConfigPrefsItem.LENS_BUTTON_FONT_COLOUR));
	}

	/**
	 * Gets the lens button text.
	 *
	 * @return the lens button text
	 */
	public static String getLensButtonText() {
		return AppearanceConfigPrefsItem
				.getValue(AppearanceConfigPrefsItem.LENS_BUTTON_TEXT);
	}

	// lens
	/**
	 * Gets the lens close colour.
	 *
	 * @return the lens close colour
	 */
	public static String getLensCloseColour() {
		return AppearanceConfigPrefsItem
				.getValue(AppearanceConfigPrefsItem.LENS_CLOSE_COLOUR);
	}

	/**
	 * Gets the lens font colour.
	 *
	 * @return the lens font colour
	 */
	public static FontColour getLensFontColour() {
		return SettingsUtil.getFontColour(AppearanceConfigPrefsItem
				.getValue(AppearanceConfigPrefsItem.LENS_FONT_COLOUR));
	}

	/**
	 * Gets the max recording time.
	 *
	 * @return the max recording time
	 */
	public static int getMaxRecordingTime() {
		return miscConfigPrefsItem.getMaxRecordingTime() * 1000;
	}

	// metrics GUI
	/**
	 * Gets the metric gui background colour.
	 *
	 * @return the metric gui background colour
	 */
	public static ColorRGBA getMetricGUIBackgroundColour() {
		return SettingsUtil.getColorRGBA(AppearanceConfigPrefsItem
				.getValue(AppearanceConfigPrefsItem.METRIC_BG_COLOUR));
	}

	/**
	 * Gets the metric gui border colour.
	 *
	 * @return the metric gui border colour
	 */
	public static ColorRGBA getMetricGUIBorderColour() {
		return SettingsUtil.getColorRGBA(AppearanceConfigPrefsItem
				.getValue(AppearanceConfigPrefsItem.METRIC_BORDER_COLOUR));
	}

	/**
	 * Gets the metric gui button colour.
	 *
	 * @return the metric gui button colour
	 */
	public static ColorRGBA getMetricGUIButtonColour() {
		return SettingsUtil.getColorRGBA(AppearanceConfigPrefsItem
				.getValue(AppearanceConfigPrefsItem.METRIC_BUTTON_COLOUR));
	}

	/**
	 * Gets the metric gui button colour as string.
	 *
	 * @return the metric gui button colour as string
	 */
	public static String getMetricGUIButtonColourAsString() {
		return AppearanceConfigPrefsItem
				.getValue(AppearanceConfigPrefsItem.METRIC_BUTTON_COLOUR);
	}

	/**
	 * Gets the metric gui font colour.
	 *
	 * @return the metric gui font colour
	 */
	public static FontColour getMetricGUIFontColour() {
		return SettingsUtil.getFontColour(AppearanceConfigPrefsItem
				.getValue(AppearanceConfigPrefsItem.METRIC_FONT_COLOUR));
	}

	/**
	 * Gets the metric gui text.
	 *
	 * @return the metric gui text
	 */
	public static String getMetricGUIText() {
		return AppearanceConfigPrefsItem
				.getValue(AppearanceConfigPrefsItem.METRIC_TEXT);
	}

	/**
	 * Gets the metrics folder.
	 *
	 * @return the metrics folder
	 */
	public static String getMetricsFolder() {
		return miscConfigPrefsItem.getMetricsFolder();
	}

	/**
	 * Gets the POI border colour.
	 *
	 * @return the POI border colour
	 */
	public static String getPOIBorderColour() {
		return AppearanceConfigPrefsItem
				.getValue(AppearanceConfigPrefsItem.POI_BORDER_COLOUR);
	}

	/**
	 * Gets the POI colour as color rgba.
	 *
	 * @return the POI colour as color rgba
	 */
	public static ColorRGBA getPOIColourAsColorRGBA() {
		return SettingsUtil.getColorRGBA(AppearanceConfigPrefsItem
				.getValue(AppearanceConfigPrefsItem.POI_COLOUR));
	}

	/**
	 * Gets the POI colour as string.
	 *
	 * @return the POI colour as string
	 */
	public static String getPOIColourAsString() {
		return AppearanceConfigPrefsItem
				.getValue(AppearanceConfigPrefsItem.POI_COLOUR);
	}

	// poi
	/**
	 * Gets the poi width.
	 *
	 * @return the poi width
	 */
	public static float getPoiWidth() {
		float toReturn = 0;
		String floatVal = AppearanceConfigPrefsItem
				.getValue(AppearanceConfigPrefsItem.POI_WIDTH);
		try {
			toReturn = Float.parseFloat(floatVal);
		} catch (Exception e) {
			toReturn = Float.parseFloat(AppearanceConfigPrefsItem
					.getDefaultValue(AppearanceConfigPrefsItem.POI_WIDTH));
		}
		return toReturn;
	}

	/**
	 * Gets the prompts.
	 *
	 * @return the prompts
	 */
	public static String[] getPrompts() {
		String prompts = AppearanceConfigPrefsItem
				.getValue(AppearanceConfigPrefsItem.RECORDING_PROMPTS);
		return prompts.split(AppearanceConfigPrefsItem.PROMPT_TOKEN);
	}

	/**
	 * Gets the recorder active button border colour.
	 *
	 * @return the recorder active button border colour
	 */
	public static ColorRGBA getRecorderActiveButtonBorderColour() {
		return SettingsUtil
				.getColorRGBA(AppearanceConfigPrefsItem
						.getValue(AppearanceConfigPrefsItem.RECORDER_ACTIVE_BUTTON_BORDER_COLOUR));
	}

	/**
	 * Gets the recorder active button font colour.
	 *
	 * @return the recorder active button font colour
	 */
	public static FontColour getRecorderActiveButtonFontColour() {
		return SettingsUtil
				.getFontColour(AppearanceConfigPrefsItem
						.getValue(AppearanceConfigPrefsItem.RECORDER_ACTIVE_BUTTON_FONT_COLOUR));
	}

	// recorder
	/**
	 * Gets the recorder background colour.
	 *
	 * @return the recorder background colour
	 */
	public static ColorRGBA getRecorderBackgroundColour() {
		return SettingsUtil.getColorRGBA(AppearanceConfigPrefsItem
				.getValue(AppearanceConfigPrefsItem.RECORDER_BG_COLOUR));
	}

	/**
	 * Gets the recorder font colour.
	 *
	 * @return the recorder font colour
	 */
	public static FontColour getRecorderFontColour() {
		return SettingsUtil.getFontColour(AppearanceConfigPrefsItem
				.getValue(AppearanceConfigPrefsItem.RECORDER_FONT_COLOUR));
	}

	/**
	 * Gets the recorder inactive button border colour.
	 *
	 * @return the recorder inactive button border colour
	 */
	public static ColorRGBA getRecorderInactiveButtonBorderColour() {
		return SettingsUtil
				.getColorRGBA(AppearanceConfigPrefsItem
						.getValue(AppearanceConfigPrefsItem.RECORDER_INACTIVE_BUTTON_BORDER_COLOUR));
	}

	/**
	 * Gets the recorder inactive button font colour.
	 *
	 * @return the recorder inactive button font colour
	 */
	public static FontColour getRecorderInactiveButtonFontColour() {
		return SettingsUtil
				.getFontColour(AppearanceConfigPrefsItem
						.getValue(AppearanceConfigPrefsItem.RECORDER_INACTIVE_BUTTON_FONT_COLOUR));
	}

	/**
	 * Gets the recorder text.
	 *
	 * @return the recorder text
	 */
	public static String getRecorderText() {
		return AppearanceConfigPrefsItem
				.getValue(AppearanceConfigPrefsItem.RECORDER_TEXT);
	}

	// shutdown screen
	/**
	 * Gets the shutdown background colour.
	 *
	 * @return the shutdown background colour
	 */
	public static ColorRGBA getShutdownBackgroundColour() {
		ColorRGBA colour = SettingsUtil.getColorRGBA(AppearanceConfigPrefsItem
				.getValue(AppearanceConfigPrefsItem.SHUTDOWN_BG_COLOUR));
		colour.set(colour.r, colour.g, colour.b, 0.5f);
		return colour;
	}

	/**
	 * Gets the shutdown controls background colour.
	 *
	 * @return the shutdown controls background colour
	 */
	public static ColorRGBA getShutdownControlsBackgroundColour() {
		return SettingsUtil
				.getColorRGBA(AppearanceConfigPrefsItem
						.getValue(AppearanceConfigPrefsItem.SHUTDOWN_CONTROLS_BG_COLOUR));
	}

	/**
	 * Gets the shutdown controls border colour.
	 *
	 * @return the shutdown controls border colour
	 */
	public static ColorRGBA getShutdownControlsBorderColour() {
		return SettingsUtil
				.getColorRGBA(AppearanceConfigPrefsItem
						.getValue(AppearanceConfigPrefsItem.SHUTDOWN_CONTROLS_BORDER_COLOUR));
	}

	/**
	 * Gets the shutdown controls font colour.
	 *
	 * @return the shutdown controls font colour
	 */
	public static FontColour getShutdownControlsFontColour() {
		return SettingsUtil
				.getFontColour(AppearanceConfigPrefsItem
						.getValue(AppearanceConfigPrefsItem.SHUTDOWN_CONTROLS_FONT_COLOUR));
	}

	/**
	 * Gets the shutdown instructions text.
	 *
	 * @return the shutdown instructions text
	 */
	public static String getShutdownInstructionsText() {
		return AppearanceConfigPrefsItem
				.getValue(AppearanceConfigPrefsItem.SHUTDOWN_INSTRUCTIONS_TEXT);
	}

	// text input GUI
	/**
	 * Gets the text input background colour.
	 *
	 * @return the text input background colour
	 */
	public static ColorRGBA getTextInputBackgroundColour() {
		return SettingsUtil.getColorRGBA(AppearanceConfigPrefsItem
				.getValue(AppearanceConfigPrefsItem.TEXT_INPUT_BG_COLOUR));
	}

	/**
	 * Gets the text input font colour.
	 *
	 * @return the text input font colour
	 */
	public static FontColour getTextInputFontColour() {
		return SettingsUtil.getFontColour(AppearanceConfigPrefsItem
				.getValue(AppearanceConfigPrefsItem.TEXT_INPUT_FONT_COLOUR));
	}

	/**
	 * Gets the text input instructions.
	 *
	 * @return the text input instructions
	 */
	public static String getTextInputInstructions() {
		return AppearanceConfigPrefsItem
				.getValue(AppearanceConfigPrefsItem.TEXT_INPUT_TEXT);
	}

	/**
	 * Gets the text input keyboard button background colour.
	 *
	 * @return the text input keyboard button background colour
	 */
	public static ColorRGBA getTextInputKeyboardButtonBackgroundColour() {
		return SettingsUtil
				.getColorRGBA(AppearanceConfigPrefsItem
						.getValue(AppearanceConfigPrefsItem.TEXT_INPUT_KEYBOARD_BUTTON_BG_COLOUR));
	}

	/**
	 * Gets the text input keyboard button border colour.
	 *
	 * @return the text input keyboard button border colour
	 */
	public static ColorRGBA getTextInputKeyboardButtonBorderColour() {
		return SettingsUtil
				.getColorRGBA(AppearanceConfigPrefsItem
						.getValue(AppearanceConfigPrefsItem.TEXT_INPUT_KEYBOARD_BUTTON_BORDER_COLOUR));
	}

	/**
	 * Gets the text input keyboard button font colour.
	 *
	 * @return the text input keyboard button font colour
	 */
	public static FontColour getTextInputKeyboardButtonFontColour() {
		return SettingsUtil
				.getFontColour(AppearanceConfigPrefsItem
						.getValue(AppearanceConfigPrefsItem.TEXT_INPUT_KEYBOARD_BUTTON_FONT_COLOUR));
	}

	/**
	 * Gets the text input scrollbox font colour.
	 *
	 * @return the text input scrollbox font colour
	 */
	public static FontColour getTextInputScrollboxFontColour() {
		return SettingsUtil
				.getFontColour(AppearanceConfigPrefsItem
						.getValue(AppearanceConfigPrefsItem.TEXT_INPUT_SCROLLBOX_FONT_COLOUR));
	}

	/**
	 * Gets the user generated content colour.
	 *
	 * @return the user generated content colour
	 */
	public static ColorRGBA getUserGeneratedContentColour() {
		return SettingsUtil
				.getColorRGBA(AppearanceConfigPrefsItem
						.getValue(AppearanceConfigPrefsItem.ENTITY_USER_GENERATED_COLOUR));
	}

	/**
	 * Checks if is single prompt.
	 *
	 * @return true, if is single prompt
	 */
	public static boolean isSinglePrompt() {
		if (AppearanceConfigPrefsItem.getValue(
				AppearanceConfigPrefsItem.RECORDING_SINGLE_PROMPT).equals(
				AppearanceConfigPrefsItem.YES)) {
			return true;
		} else {
			return false;
		}
	}

}
