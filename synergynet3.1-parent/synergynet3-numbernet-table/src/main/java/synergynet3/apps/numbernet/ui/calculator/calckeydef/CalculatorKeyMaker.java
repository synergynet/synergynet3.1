package synergynet3.apps.numbernet.ui.calculator.calckeydef;

import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import multiplicity3.csys.items.keyboard.model.CharacterKey;
import multiplicity3.csys.items.keyboard.model.KeyboardKey;

public class CalculatorKeyMaker {
	public static List<KeyboardKey> createSquareKeysLine(String keysOnePerChar, int width, int height, int leftIndent, int topIndent, int gapBetweenKeys) {
		List<KeyboardKey> keys = new ArrayList<KeyboardKey>();
		
		for(int i = 0; i < keysOnePerChar.length(); i++) {
			char keyChar = keysOnePerChar.charAt(i);
			int code;
			try {
				code = getKeyCode(keyChar);
				int x = leftIndent + (i * (width + gapBetweenKeys));
				int y = topIndent;
				Rectangle2D rect = new Rectangle2D.Float(x, y, width, height);				
				KeyboardKey k = new KeyboardKey(new CharacterKey(keyChar + "", code), rect);
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
	
	public static int getKeyCode(char c) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
	    Field f = KeyEvent.class.getField("VK_" + Character.toUpperCase(c));
	    f.setAccessible(true);
	    return (Integer) f.get(null);
	}

}
