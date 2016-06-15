package multiplicity3.csys.gfx;

import java.awt.Color;
import java.lang.reflect.Field;

import com.jme3.math.ColorRGBA;

/**
 * The Class ColourUtils.
 */
public class ColourUtils
{

	/**
	 * Color from string.
	 *
	 * @param str
	 *            the str
	 * @return the color
	 */
	public static Color colorFromString(String str)
	{
		Color color;
		try
		{
			Field field = Class.forName("java.awt.Color").getField(str);
			color = (Color) field.get(null);
		}
		catch (Exception e)
		{
			color = null;
		}
		return color;
	}

	/**
	 * Colour convert.
	 *
	 * @param c
	 *            the c
	 * @return the color rgba
	 */
	public static ColorRGBA colourConvert(Color c)
	{
		return new ColorRGBA(c.getRed() / 255f, c.getGreen() / 255f, c.getBlue() / 255f, c.getAlpha() / 255f);
	}

	/**
	 * Gets the color rgba from color.
	 *
	 * @param color
	 *            the color
	 * @return the color rgba from color
	 */
	public static ColorRGBA getColorRGBAFromColor(Color color)
	{
		ColorRGBA crgba = new ColorRGBA();
		float r = color.getRed() / 255f;
		float g = color.getGreen() / 255f;
		float b = color.getBlue() / 255f;
		float a = color.getAlpha() / 255f;
		crgba.set(r, g, b, a);
		return crgba;
	}
}
