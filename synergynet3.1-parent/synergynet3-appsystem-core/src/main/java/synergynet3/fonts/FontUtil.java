package synergynet3.fonts;

/**
 * The Class FontUtil.
 */
public class FontUtil
{

	/** The Constant RESOURCES_DIR. */
	private static final String RESOURCES_DIR = "synergynet3/fonts/";

	/**
	 * Gets the font.
	 *
	 * @param colour
	 *            the colour
	 * @return the font
	 */
	public static String getFont(FontColour colour)
	{
		String font = RESOURCES_DIR;
		switch (colour)
		{
			case Blue:
				font += "arial64_blue.fnt";
				break;
			case Cyan:
				font += "arial64_cyan.fnt";
				break;
			case Green:
				font += "arial64_green.fnt";
				break;
			case Magenta:
				font += "arial64_magenta.fnt";
				break;
			case Orange:
				font += "arial64_orange.fnt";
				break;
			case Red:
				font += "arial64_red.fnt";
				break;
			case White:
				font += "arial64_white.fnt";
				break;
			case Yellow:
				font += "arial64_yellow.fnt";
				break;
			case Grey:
				font += "arial64_grey.fnt";
				break;
			case Dark_Grey:
				font += "arial64_darkgrey.fnt";
				break;
			default:
				font += "arial64_black.fnt";
				break;
		}
		return font;
	}

	/**
	 * Gets the font colour from string.
	 *
	 * @param fontColourString
	 *            the font colour string
	 * @return the font colour from string
	 */
	public static FontColour getFontColourFromString(String fontColourString)
	{
		for (FontColour fontColour : FontColour.values())
		{
			if (fontColourString.equalsIgnoreCase(fontColour.toString()))
			{
				return fontColour;
			}
		}
		return FontColour.Black;
	}

}
