package synergynet3.activitypack1.core.lightbox.lightboxmodel.xmlpersistence;

import java.io.InputStream;
import java.io.OutputStream;

import synergynet3.activitypack1.core.lightbox.lightboxmodel.LightBox; 
import synergynet3.activitypack1.core.lightbox.lightboxmodel.items.ImageItem;
import synergynet3.activitypack1.core.lightbox.lightboxmodel.items.TextItem;


import com.thoughtworks.xstream.XStream;

public class LightBoxPersistance {
	public static void saveLightBox(LightBox lightBox, OutputStream outputStream) {
		XStream xstreamSerializer = getXStreamForLightBoxPersistence();
		xstreamSerializer.toXML(lightBox, outputStream);
	}

	public static LightBox readLightBoxFromInputStream(InputStream inputStream) {
		XStream xstreamSerializer = getXStreamForLightBoxPersistence();
		Object obj = xstreamSerializer.fromXML(inputStream);
		if(!(obj instanceof LightBox)) {
			return null;
		}		
		return (LightBox)obj;
	}
	
	private static XStream getXStreamForLightBoxPersistence() {
		XStream xs = new XStream();
		setupShortNameAliases(xs);
		return xs;
	}

	private static void setupShortNameAliases(XStream xs) {
		makeSimpleAlias(xs, LightBox.class);
		makeSimpleAlias(xs, TextItem.class);
		makeSimpleAlias(xs, ImageItem.class);		
	}

	private static void makeSimpleAlias(XStream xs, Class<?> clazz) {
		String fullClassName = clazz.getName();
		String nameWithoutPackage = fullClassName.substring(fullClassName.lastIndexOf('.')+1);
		xs.alias(nameWithoutPackage, clazz);		
	}
}
