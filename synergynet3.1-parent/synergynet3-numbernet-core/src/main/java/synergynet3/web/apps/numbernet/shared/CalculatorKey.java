package synergynet3.web.apps.numbernet.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * The Enum CalculatorKey.
 */
public enum CalculatorKey implements IsSerializable
{

	/** The divide. */
	DIVIDE("\u00F7"),

	/** The eight. */
	EIGHT("8"),

	/** The five. */
	FIVE("5"),

	/** The four. */
	FOUR("4"),

	/** The leftbracket. */
	LEFTBRACKET("("),

	/** The minus. */
	MINUS("-"),

	/** The multiply. */
	MULTIPLY("\u00D7"),

	/** The nine. */
	NINE("9"),

	/** The one. */
	ONE("1"),

	/** The plus. */
	PLUS("+"),

	/** The point. */
	POINT("."),

	/** The rightbracket. */
	RIGHTBRACKET(")"),

	/** The seven. */
	SEVEN("7"),

	/** The six. */
	SIX("6"),

	/** The three. */
	THREE("3"),

	/** The two. */
	TWO("2"),

	/** The zero. */
	ZERO("0");

	/** The string representation. */
	private String stringRepresentation;

	/**
	 * Instantiates a new calculator key.
	 *
	 * @param stringRepresentation
	 *            the string representation
	 */
	CalculatorKey(String stringRepresentation)
	{
		this.stringRepresentation = stringRepresentation;
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

}
