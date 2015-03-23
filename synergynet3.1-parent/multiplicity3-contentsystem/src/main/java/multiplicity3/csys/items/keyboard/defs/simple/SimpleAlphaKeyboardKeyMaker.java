package multiplicity3.csys.items.keyboard.defs.simple;

import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import multiplicity3.csys.items.keyboard.model.CharacterKey;
import multiplicity3.csys.items.keyboard.model.KeyboardKey;

/**
 * The Class SimpleAlphaKeyboardKeyMaker.
 */
public class SimpleAlphaKeyboardKeyMaker {

	/**
	 * Creates the square keys line.
	 *
	 * @param keysOnePerChar the keys one per char
	 * @param width the width
	 * @param height the height
	 * @param leftIndent the left indent
	 * @param topIndent the top indent
	 * @param gapBetweenKeys the gap between keys
	 * @return the list
	 */
	public static List<KeyboardKey> createSquareKeysLine(String keysOnePerChar,
			int width, int height, int leftIndent, int topIndent,
			int gapBetweenKeys) {
		List<KeyboardKey> keys = new ArrayList<KeyboardKey>();

		for (int i = 0; i < keysOnePerChar.length(); i++) {
			char keyChar = keysOnePerChar.charAt(i);
			int code;
			try {
				code = getKeyCode(keyChar);
				int x = leftIndent + (i * (width + gapBetweenKeys));
				int y = topIndent;
				Rectangle2D rect = new Rectangle2D.Float(x, y, width, height);
				KeyboardKey k = new KeyboardKey(new CharacterKey(keyChar + "",
						code), rect);
				// add for upper case/shift
				keys.add(k);
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}

		}
		return keys;
	}

	/**
	 * Gets the key code.
	 *
	 * @param c the c
	 * @return the key code
	 * @throws SecurityException the security exception
	 * @throws NoSuchFieldException the no such field exception
	 * @throws IllegalArgumentException the illegal argument exception
	 * @throws IllegalAccessException the illegal access exception
	 */
	public static int getKeyCode(char c) throws SecurityException,
			NoSuchFieldException, IllegalArgumentException,
			IllegalAccessException {
		Field f = KeyEvent.class.getField("VK_" + Character.toUpperCase(c));
		f.setAccessible(true);
		return (Integer) f.get(null);
	}

}
