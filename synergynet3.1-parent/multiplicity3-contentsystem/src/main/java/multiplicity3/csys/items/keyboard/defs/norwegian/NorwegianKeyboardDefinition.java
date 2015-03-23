package multiplicity3.csys.items.keyboard.defs.norwegian;

import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.util.List;

import multiplicity3.csys.items.keyboard.defs.simple.SimpleAlphaKeyboardKeyMaker;
import multiplicity3.csys.items.keyboard.model.CharacterKey;
import multiplicity3.csys.items.keyboard.model.KeyModifiers;
import multiplicity3.csys.items.keyboard.model.KeyboardDefinition;
import multiplicity3.csys.items.keyboard.model.KeyboardKey;

/**
 * The Class NorwegianKeyboardDefinition.
 */
public class NorwegianKeyboardDefinition extends KeyboardDefinition {

	/**
	 * Instantiates a new norwegian keyboard definition.
	 */
	public NorwegianKeyboardDefinition() {

		int standardKeyWidth = 64;
		int standardKeyHeight = 64;

		int gapBetweenKeys = 5;
		int gapBetweenLines = 5;
		int borderSize = 20;

		List<KeyboardKey> qwertyKeys = SimpleAlphaKeyboardKeyMaker
				.createSquareKeysLine("qwertyuiop", standardKeyWidth,
						standardKeyHeight, borderSize, borderSize,
						gapBetweenKeys);
		addKeys(qwertyKeys);

		KeyboardKey p = getKey("p");
		Rectangle2D rect = new Rectangle2D.Float(p.getMaxX() + gapBetweenKeys,
				p.getMinY(), standardKeyWidth, standardKeyHeight);
		CharacterKey oo = new CharacterKey("�", 91);
		addKey(new KeyboardKey(oo, rect));

		KeyboardKey p1 = getKey("�");
		rect = new Rectangle2D.Float(p1.getMaxX() + gapBetweenKeys,
				p1.getMinY(), standardKeyWidth * 2, standardKeyHeight);
		CharacterKey backspace = new CharacterKey("<---",
				KeyEvent.VK_BACK_SPACE);
		addKey(new KeyboardKey(backspace, rect));

		List<KeyboardKey> asdfKeys = SimpleAlphaKeyboardKeyMaker
				.createSquareKeysLine("asdfghjkl", standardKeyWidth,
						standardKeyHeight, borderSize + (standardKeyWidth / 3),
						standardKeyHeight + gapBetweenLines + borderSize,
						gapBetweenKeys);
		addKeys(asdfKeys);
		KeyboardKey l = getKey("l");
		rect = new Rectangle2D.Float(l.getMaxX() + gapBetweenKeys, l.getMinY(),
				standardKeyWidth, standardKeyHeight);
		CharacterKey xo = new CharacterKey("�", 59);
		addKey(new KeyboardKey(xo, rect));

		KeyboardKey xo1 = getKey("�");
		rect = new Rectangle2D.Float(xo1.getMaxX() + gapBetweenKeys,
				xo1.getMinY(), standardKeyWidth, standardKeyHeight);
		CharacterKey ae = new CharacterKey("�", 222);
		addKey(new KeyboardKey(ae, rect));

		KeyboardKey ae1 = getKey("�");
		rect = new Rectangle2D.Float(ae1.getMaxX() + gapBetweenKeys,
				ae1.getMinY(), standardKeyWidth * 1.5f, standardKeyHeight);
		CharacterKey enterKey = new CharacterKey("Enter", KeyEvent.VK_ENTER);
		addKey(new KeyboardKey(enterKey, rect));

		List<KeyboardKey> zxcKeys = SimpleAlphaKeyboardKeyMaker
				.createSquareKeysLine("zxcvbnm", standardKeyWidth,
						standardKeyHeight, borderSize + (standardKeyWidth / 2),
						((standardKeyHeight + gapBetweenLines) * 2)
								+ borderSize, gapBetweenKeys);
		addKeys(zxcKeys);
		KeyboardKey m = getKey("m");
		rect = new Rectangle2D.Float(m.getMaxX() + gapBetweenKeys, m.getMinY(),
				standardKeyWidth * 1.5f, standardKeyHeight);
		CharacterKey shiftChar = new CharacterKey("Shift", KeyEvent.VK_SHIFT);
		KeyboardKey rightShift = new KeyboardKey(shiftChar, rect);
		rightShift.setModifiers(KeyModifiers.SHIFT);
		addKey(rightShift);

		rect = new Rectangle2D.Float(200,
				((standardKeyHeight + gapBetweenLines) * 3) + borderSize, 200,
				standardKeyHeight);
		CharacterKey spaceKey = new CharacterKey("Space", KeyEvent.VK_SPACE);
		KeyboardKey spaceBar = new KeyboardKey(spaceKey, rect);
		addKey(spaceBar);

		KeyboardKey ooo = getKey("�");
		KeyboardKey sp = getKey("Space");
		rect = new Rectangle2D.Float(ooo.getMaxX() + gapBetweenKeys,
				sp.getMinY(), standardKeyWidth * 1.5f, standardKeyHeight);
		CharacterKey closeKey = new CharacterKey("CLOSE", KeyEvent.VK_CANCEL);
		KeyboardKey closeBar = new KeyboardKey(closeKey, rect);
		addKey(closeBar);
	}
}
