package multiplicity3.csys.items;

import java.awt.Color;
import java.awt.Font;

import multiplicity3.csys.items.shapes.IRectangularItem;

/**
 * The Interface ILabel.
 */
public interface ILabel extends IRectangularItem {

	/**
	 * Gets the text.
	 *
	 * @return the text
	 */
	public String getText();

	/**
	 * Sets the alpha.
	 *
	 * @param alpha the new alpha
	 */
	public void setAlpha(float alpha);

	/**
	 * Sets the font.
	 *
	 * @param f the new font
	 */
	public void setFont(Font f);

	/**
	 * Sets the text.
	 *
	 * @param text the new text
	 */
	public void setText(String text);

	/**
	 * Sets the text colour.
	 *
	 * @param c the new text colour
	 */
	public void setTextColour(Color c);

	/**
	 * Sets the underline chars.
	 *
	 * @param underlineChars the new underline chars
	 */
	public void setUnderlineChars(char... underlineChars);
}
