package multiplicity3.csys.items.mutablelabel;

import multiplicity3.csys.items.item.IItem;

import com.jme3.math.Vector2f;

/**
 * The Interface IMutableLabel.
 */
public interface IMutableLabel extends IItem
{

	/**
	 * Append char.
	 *
	 * @param charAt
	 *            the char at
	 */
	public void appendChar(char charAt);

	/**
	 * Gets the text.
	 *
	 * @return the text
	 */
	public String getText();

	/**
	 * Gets the text size.
	 *
	 * @return the text size
	 */
	public Vector2f getTextSize();

	/**
	 * Checks if is box size set.
	 *
	 * @return true, if is box size set
	 */
	public boolean isBoxSizeSet();

	/**
	 * Removes the char.
	 */
	public void removeChar();

	/**
	 * Sets the box size.
	 *
	 * @param width
	 *            the width
	 * @param height
	 *            the height
	 */
	public void setBoxSize(float width, float height);

	/**
	 * Sets the font.
	 *
	 * @param resourcePath
	 *            the new font
	 */
	public void setFont(String resourcePath);

	/**
	 * Sets the font scale.
	 *
	 * @param f
	 *            the new font scale
	 */
	public void setFontScale(float f);

	/**
	 * Sets the text.
	 *
	 * @param text
	 *            the new text
	 */
	public void setText(String text);
}
