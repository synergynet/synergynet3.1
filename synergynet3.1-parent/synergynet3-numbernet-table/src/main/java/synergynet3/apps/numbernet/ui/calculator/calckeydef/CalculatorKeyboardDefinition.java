package synergynet3.apps.numbernet.ui.calculator.calckeydef;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.util.List;

import multiplicity3.csys.items.keyboard.model.CharacterKey;
import multiplicity3.csys.items.keyboard.model.KeyboardDefinition;
import multiplicity3.csys.items.keyboard.model.KeyboardKey;

/**
 * The Class CalculatorKeyboardDefinition.
 */
public class CalculatorKeyboardDefinition extends KeyboardDefinition
{

	/** The Constant KEY_STRING_REPRESENTATION_CLEAR. */
	public static final String KEY_STRING_REPRESENTATION_CLEAR = "C";

	/** The Constant KEY_STRING_REPRESENTATION_DIVIDE. */
	public static final String KEY_STRING_REPRESENTATION_DIVIDE = "\u00F7";

	/** The Constant KEY_STRING_REPRESENTATION_LEFT_PARENTHESIS. */
	public static final String KEY_STRING_REPRESENTATION_LEFT_PARENTHESIS = "(";

	/** The Constant KEY_STRING_REPRESENTATION_MINUS. */
	public static final String KEY_STRING_REPRESENTATION_MINUS = "-";

	/** The Constant KEY_STRING_REPRESENTATION_MULTIPLY. */
	public static final String KEY_STRING_REPRESENTATION_MULTIPLY = "\u00D7";

	/** The Constant KEY_STRING_REPRESENTATION_PLUS. */
	public static final String KEY_STRING_REPRESENTATION_PLUS = "+";

	/** The Constant KEY_STRING_REPRESENTATION_POINT. */
	public static final String KEY_STRING_REPRESENTATION_POINT = ".";

	/** The Constant KEY_STRING_REPRESENTATION_RIGHT_PARENTHESIS. */
	public static final String KEY_STRING_REPRESENTATION_RIGHT_PARENTHESIS = ")";

	/** The border size. */
	private int borderSize = 10;

	/** The delete colour. */
	private Color deleteColour = new Color(239, 167, 167);

	/** The operations colour. */
	private Color operationsColour = new Color(191, 157, 143);

	/** The send colour. */
	private Color sendColour = new Color(185, 239, 153);

	/**
	 * Instantiates a new calculator keyboard definition.
	 */
	public CalculatorKeyboardDefinition()
	{

		int standardKeyWidth = 50;
		int standardKeyHeight = 50;

		int gapBetweenKeys = 16;
		int gapBetweenLines = 16;

		CharacterKey plus = new CharacterKey(KEY_STRING_REPRESENTATION_PLUS, KeyEvent.VK_PLUS);
		KeyboardKey plusKey = new KeyboardKey(plus, new Rectangle2D.Float(borderSize, borderSize, standardKeyWidth, standardKeyHeight));
		addKey(plusKey);

		CharacterKey minus = new CharacterKey(KEY_STRING_REPRESENTATION_MINUS, KeyEvent.VK_MINUS);
		KeyboardKey minusKey = new KeyboardKey(minus, rectangleForKeyToTheRight(plusKey, standardKeyWidth, standardKeyHeight, gapBetweenKeys));
		addKey(minusKey);

		CharacterKey multiply = new CharacterKey(KEY_STRING_REPRESENTATION_MULTIPLY, KeyEvent.VK_MULTIPLY);
		KeyboardKey multiplyKey = new KeyboardKey(multiply, rectangleForKeyToTheRight(minusKey, standardKeyWidth, standardKeyHeight, gapBetweenKeys));
		addKey(multiplyKey);

		CharacterKey divide = new CharacterKey(KEY_STRING_REPRESENTATION_DIVIDE, KeyEvent.VK_DIVIDE);
		KeyboardKey divideKey = new KeyboardKey(divide, rectangleForKeyToTheRight(multiplyKey, standardKeyWidth, standardKeyHeight, gapBetweenKeys));
		addKey(divideKey);

		setBackgroundColourForKeys(operationsColour, plusKey, minusKey, multiplyKey, divideKey);

		// row 1
		int row = 1;
		List<KeyboardKey> keys789 = CalculatorKeyMaker.createSquareKeysLine("789", standardKeyWidth, standardKeyHeight, borderSize, borderSize + ((standardKeyHeight + gapBetweenLines) * row), gapBetweenKeys);
		addKeys(keys789);

		KeyboardKey nineKey = getKey("9");
		CharacterKey c = new CharacterKey(KEY_STRING_REPRESENTATION_CLEAR, KeyEvent.VK_C);
		KeyboardKey cKey = new KeyboardKey(c, rectangleForKeyToTheRight(nineKey, standardKeyWidth, standardKeyHeight, gapBetweenKeys));
		addKey(cKey);

		setBackgroundColourForKeys(deleteColour, cKey);

		row = 2;
		List<KeyboardKey> keys456 = CalculatorKeyMaker.createSquareKeysLine("456", standardKeyWidth, standardKeyHeight, borderSize, borderSize + ((standardKeyHeight + gapBetweenLines) * row), gapBetweenKeys);
		addKeys(keys456);

		KeyboardKey sixKey = getKey("6");
		CharacterKey leftBracket = new CharacterKey(KEY_STRING_REPRESENTATION_LEFT_PARENTHESIS, KeyEvent.VK_LEFT_PARENTHESIS);
		KeyboardKey leftBracketKey = new KeyboardKey(leftBracket, rectangleForKeyToTheRight(sixKey, standardKeyWidth, standardKeyHeight, gapBetweenKeys));
		addKey(leftBracketKey);

		row = 3;
		List<KeyboardKey> keys123 = CalculatorKeyMaker.createSquareKeysLine("123", standardKeyWidth, standardKeyHeight, borderSize, borderSize + ((standardKeyHeight + gapBetweenLines) * row), gapBetweenKeys);
		addKeys(keys123);

		KeyboardKey threeKey = getKey("3");
		CharacterKey rightBracket = new CharacterKey(KEY_STRING_REPRESENTATION_RIGHT_PARENTHESIS, KeyEvent.VK_RIGHT_PARENTHESIS);
		KeyboardKey rightBracketKey = new KeyboardKey(rightBracket, rectangleForKeyToTheRight(threeKey, standardKeyWidth, standardKeyHeight, gapBetweenKeys));
		addKey(rightBracketKey);

		setBackgroundColourForKeys(operationsColour, leftBracketKey, rightBracketKey);

		row = 4;
		List<KeyboardKey> key0 = CalculatorKeyMaker.createSquareKeysLine("0", standardKeyWidth, standardKeyHeight, borderSize, borderSize + ((standardKeyHeight + gapBetweenLines) * row), gapBetweenKeys);
		addKeys(key0);

		KeyboardKey zero = getKey("0");

		CharacterKey period = new CharacterKey(KEY_STRING_REPRESENTATION_POINT, KeyEvent.VK_PERIOD);
		KeyboardKey periodKey = new KeyboardKey(period, rectangleForKeyToTheRight(zero, standardKeyWidth, standardKeyHeight, gapBetweenKeys));
		addKey(periodKey);

		// picked random keyevent to assign this to
		CharacterKey send = new CharacterKey("Send", KeyEvent.VK_BACK_QUOTE);
		KeyboardKey sendKey = new KeyboardKey(send, rectangleForKeyToTheRight(periodKey, (standardKeyWidth * 2) + gapBetweenKeys, standardKeyHeight, gapBetweenKeys));
		addKey(sendKey);

		setBackgroundColourForKeys(sendColour, sendKey);

	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.csys.items.keyboard.model.KeyboardDefinition#getBounds()
	 */
	@Override
	public Rectangle2D getBounds()
	{
		Rectangle2D preBorder = super.getBounds();
		preBorder.setFrame(0, 0, preBorder.getWidth() + borderSize, preBorder.getHeight() + borderSize);
		return preBorder;
	}

	/**
	 * Rectangle for key to the right.
	 *
	 * @param k
	 *            the k
	 * @param keyWidth
	 *            the key width
	 * @param keyHeight
	 *            the key height
	 * @param gapBetweenKeys
	 *            the gap between keys
	 * @return the rectangle2 d
	 */
	private Rectangle2D rectangleForKeyToTheRight(KeyboardKey k, int keyWidth, int keyHeight, int gapBetweenKeys)
	{
		return new Rectangle2D.Float(k.getMaxX() + gapBetweenKeys, k.getMinY(), keyWidth, keyHeight);
	}

	/**
	 * Sets the background colour for keys.
	 *
	 * @param c
	 *            the c
	 * @param keys
	 *            the keys
	 */
	private void setBackgroundColourForKeys(Color c, KeyboardKey... keys)
	{
		for (KeyboardKey k : keys)
		{
			k.setBackgroundColour(c);
		}

	}

}
