package synergynet3.web.shared;

/**
 * Class which manages the colours and their corresponding RGB values when
 * referred to on the web pages.
 */
public class ColourManager {

	/** Collection of colour RGB values. */
	private static float[][] colourRGBs = { { 1f, 1f, 1f }, { 1f, 0f, 0f },
			{ 0f, 0f, 1f }, { 1f, 1f, 0f }, { 0f, 1f, 0f },
			{ 1f, 69f / 255f, 0f }, { 95f / 255f, 0f, 1f }, { 1f, 0f, 1f },
			{ 0f, 1f, 1f } };

	/** Collection of colour names. */
	private static String[] colours = { "white", "red", "blue", "yellow",
			"green", "orange", "purple", "pink", "cyan" };

	/**
	 * Get the collection of colour names.
	 *
	 * @return The collection of colour names.
	 */
	public static String[] getColours() {
		return colours;
	}

	/**
	 * Finds the rgb value that corresponds to a colour's name.
	 *
	 * @param colour Name of the colour.
	 * @return RGB value of the colour in the form: {r, g, b}.
	 */
	public static float[] getRGBForColour(String colour) {
		float[] toReturn = { 0, 0, 0 };
		for (int i = 0; i < colours.length; i++) {
			if (colour.equalsIgnoreCase(colours[i])) {
				toReturn = colourRGBs[i];
				break;
			}
		}
		return toReturn;
	}

	/**
	 * Set the collection of colour names to use.
	 *
	 * @param colours The collection of colour names to use.
	 */
	public static void setColours(String[] colours) {
		ColourManager.colours = colours;
	}

}
