package multiplicity3.csys.items.keyboard.defs.simple;

import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.util.List;

import multiplicity3.csys.items.keyboard.model.CharacterKey;
import multiplicity3.csys.items.keyboard.model.KeyModifiers;
import multiplicity3.csys.items.keyboard.model.KeyboardDefinition;
import multiplicity3.csys.items.keyboard.model.KeyboardKey;

/**
 * The Class SimpleAlphaKeyboardDefinition.
 */
public class SimpleAlphaKeyboardDefinition extends KeyboardDefinition
{

	/**
	 * Instantiates a new simple alpha keyboard definition.
	 */
	public SimpleAlphaKeyboardDefinition()
	{

		int standardKeyWidth = 64;
		int standardKeyHeight = 64;

		int gapBetweenKeys = 5;
		int gapBetweenLines = 5;
		int borderSize = 20;

		List<KeyboardKey> qwertyKeys = SimpleAlphaKeyboardKeyMaker.createSquareKeysLine("qwertyuiop", standardKeyWidth, standardKeyHeight, borderSize, borderSize, gapBetweenKeys);
		addKeys(qwertyKeys);

		KeyboardKey p = getKey("p");
		Rectangle2D rect = new Rectangle2D.Float(p.getMaxX() + gapBetweenKeys, p.getMinY(), standardKeyWidth * 2, standardKeyHeight);
		CharacterKey backspace = new CharacterKey("<---", KeyEvent.VK_BACK_SPACE);
		addKey(new KeyboardKey(backspace, rect));

		List<KeyboardKey> asdfKeys = SimpleAlphaKeyboardKeyMaker.createSquareKeysLine("asdfghjkl", standardKeyWidth, standardKeyHeight, borderSize + (standardKeyWidth / 3), standardKeyHeight + gapBetweenLines + borderSize, gapBetweenKeys);
		addKeys(asdfKeys);
		KeyboardKey l = getKey("l");
		rect = new Rectangle2D.Float(l.getMaxX() + gapBetweenKeys, l.getMinY(), standardKeyWidth * 2, standardKeyHeight);
		CharacterKey enterKey = new CharacterKey("Enter", KeyEvent.VK_ENTER);
		addKey(new KeyboardKey(enterKey, rect));

		List<KeyboardKey> zxcKeys = SimpleAlphaKeyboardKeyMaker.createSquareKeysLine("zxcvbnm", standardKeyWidth, standardKeyHeight, borderSize + (standardKeyWidth / 2), ((standardKeyHeight + gapBetweenLines) * 2) + borderSize, gapBetweenKeys);
		addKeys(zxcKeys);
		KeyboardKey m = getKey("m");
		rect = new Rectangle2D.Float(m.getMaxX() + gapBetweenKeys, m.getMinY(), standardKeyWidth * 1.5f, standardKeyHeight);
		CharacterKey shiftChar = new CharacterKey("Shift", KeyEvent.VK_SHIFT);
		KeyboardKey rightShift = new KeyboardKey(shiftChar, rect);
		rightShift.setModifiers(KeyModifiers.SHIFT);
		addKey(rightShift);

		rect = new Rectangle2D.Float(200, ((standardKeyHeight + gapBetweenLines) * 3) + borderSize, 200, standardKeyHeight);
		CharacterKey spaceKey = new CharacterKey("Space", KeyEvent.VK_SPACE);
		KeyboardKey spaceBar = new KeyboardKey(spaceKey, rect);
		addKey(spaceBar);
	}

}
