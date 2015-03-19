package multiplicity3.jme3csys.items.mutablelabel;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.jme3.asset.AssetManager;
import com.jme3.font.BitmapFont;

public class FontCache {
	private static final Logger log = Logger.getLogger(FontCache.class.getName());
	
	private static FontCache instance;

	public static FontCache get() {
		synchronized(FontCache.class) { 
			if(instance == null) instance = new FontCache();
			return instance;
		}
	}
	
	private Map<String,BitmapFont> cacheMap;
	
	private FontCache() {
		cacheMap = new HashMap<String, BitmapFont>();
	}
	
	public BitmapFont getFont(String resource, AssetManager assetManager) {
		BitmapFont font = cacheMap.get(resource);
		if(font == null) {
			font = assetManager.loadFont(resource);
			if(font == null) {
				log.severe("Looks like you need to do a clean on the workspace:");
				log.severe("  Couldn't find font resource " + resource);
				log.severe("  Either this doesn't exist OR ");
				log.severe("  Eclipse didn't copy it properly, and a clean will fix it.");
			}
			cacheMap.put(resource, font);
		}
		return font;
	}
}
