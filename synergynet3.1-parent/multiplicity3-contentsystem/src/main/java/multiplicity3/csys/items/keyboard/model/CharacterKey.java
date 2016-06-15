package multiplicity3.csys.items.keyboard.model;

/**
 * The Class CharacterKey.
 */
public class CharacterKey
{

	/** The key code. */
	private int keyCode;

	/** The string representation. */
	private String stringRepresentation;

	/**
	 * Instantiates a new character key.
	 *
	 * @param stringRepresentation
	 *            the string representation
	 * @param keyCode
	 *            the key code
	 */
	public CharacterKey(String stringRepresentation, int keyCode)
	{
		this.setStringRepresentation(stringRepresentation);
		this.setKeyCode(keyCode);
	}

	/**
	 * Gets the key code.
	 *
	 * @return the key code
	 */
	public int getKeyCode()
	{
		return keyCode;
	}

	/**
	 * Gets the string representation.
	 *
	 * @return the string representation
	 */
	public String getStringRepresentation()
	{
		return stringRepresentation;
	}

	/**
	 * Sets the key code.
	 *
	 * @param keyCode
	 *            the new key code
	 */
	public void setKeyCode(int keyCode)
	{
		this.keyCode = keyCode;
	}

	/**
	 * Sets the string representation.
	 *
	 * @param stringRepresentation
	 *            the new string representation
	 */
	public void setStringRepresentation(String stringRepresentation)
	{
		this.stringRepresentation = stringRepresentation;
	}
}
