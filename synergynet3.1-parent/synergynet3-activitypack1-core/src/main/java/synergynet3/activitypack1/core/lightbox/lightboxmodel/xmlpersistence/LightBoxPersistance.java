package synergynet3.activitypack1.core.lightbox.lightboxmodel.xmlpersistence;

import java.io.InputStream;
import java.io.OutputStream;

import synergynet3.activitypack1.core.lightbox.lightboxmodel.LightBox;
import synergynet3.activitypack1.core.lightbox.lightboxmodel.items.ImageItem;
import synergynet3.activitypack1.core.lightbox.lightboxmodel.items.TextItem;

import com.thoughtworks.xstream.XStream;

/**
 * The Class LightBoxPersistance.
 */
public class LightBoxPersistance
{

	/**
	 * Read light box from input stream.
	 *
	 * @param inputStream
	 *            the input stream
	 * @return the light box
	 */
	public static LightBox readLightBoxFromInputStream(InputStream inputStream)
	{
		XStream xstreamSerializer = getXStreamForLightBoxPersistence();
		Object obj = xstreamSerializer.fromXML(inputStream);
		if (!(obj instanceof LightBox))
		{
			return null;
		}
		return (LightBox) obj;
	}

	/**
	 * Save light box.
	 *
	 * @param lightBox
	 *            the light box
	 * @param outputStream
	 *            the output stream
	 */
	public static void saveLightBox(LightBox lightBox, OutputStream outputStream)
	{
		XStream xstreamSerializer = getXStreamForLightBoxPersistence();
		xstreamSerializer.toXML(lightBox, outputStream);
	}

	/**
	 * Gets the x stream for light box persistence.
	 *
	 * @return the x stream for light box persistence
	 */
	private static XStream getXStreamForLightBoxPersistence()
	{
		XStream xs = new XStream();
		setupShortNameAliases(xs);
		return xs;
	}

	/**
	 * Make simple alias.
	 *
	 * @param xs
	 *            the xs
	 * @param clazz
	 *            the clazz
	 */
	private static void makeSimpleAlias(XStream xs, Class<?> clazz)
	{
		String fullClassName = clazz.getName();
		String nameWithoutPackage = fullClassName.substring(fullClassName.lastIndexOf('.') + 1);
		xs.alias(nameWithoutPackage, clazz);
	}

	/**
	 * Sets the up short name aliases.
	 *
	 * @param xs
	 *            the new up short name aliases
	 */
	private static void setupShortNameAliases(XStream xs)
	{
		makeSimpleAlias(xs, LightBox.class);
		makeSimpleAlias(xs, TextItem.class);
		makeSimpleAlias(xs, ImageItem.class);
	}
}
