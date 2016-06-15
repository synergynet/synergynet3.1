package multiplicity3.csys.items;

/**
 * The Interface IEditableText.
 */
public interface IEditableText extends ILabel
{

	/**
	 * Append char.
	 *
	 * @param c
	 *            the c
	 */
	public void appendChar(char c);

	/**
	 * Append string.
	 *
	 * @param charSet
	 *            the char set
	 */
	public void appendString(String charSet);

	/**
	 * Insert char.
	 *
	 * @param c
	 *            the c
	 */
	public void insertChar(char c);

	/**
	 * Removes the char.
	 */
	public void removeChar();

	/**
	 * Sets the cursor at.
	 *
	 * @param index
	 *            the new cursor at
	 */
	public void setCursorAt(int index);

	/**
	 * Sets the cursor at end.
	 */
	public void setCursorAtEnd();

	/**
	 * Sets the cursor display.
	 *
	 * @param onOrOff
	 *            the new cursor display
	 */
	public void setCursorDisplay(boolean onOrOff);
}
