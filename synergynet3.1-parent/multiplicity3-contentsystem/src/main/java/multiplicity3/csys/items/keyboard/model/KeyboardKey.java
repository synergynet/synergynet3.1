package multiplicity3.csys.items.keyboard.model;

import java.awt.Color;
import java.awt.Shape;

/**
 * The Class KeyboardKey.
 */
public class KeyboardKey
{

	/** The background colour. */
	private Color backgroundColour = Color.white;

	/** The default character. */
	private CharacterKey defaultCharacter;

	/** The enabled. */
	private boolean enabled = true;

	/** The key shape. */
	private Shape keyShape;

	/** The key text colour. */
	private Color keyTextColour = Color.darkGray;

	/** The modifiers. */
	private KeyModifiers modifiers = KeyModifiers.NONE;

	/**
	 * Instantiates a new keyboard key.
	 *
	 * @param defaultCharacter
	 *            the default character
	 * @param keyShape
	 *            the key shape
	 */
	public KeyboardKey(CharacterKey defaultCharacter, Shape keyShape)
	{
		this.defaultCharacter = defaultCharacter;
		this.setKeyShape(keyShape);
	}

	/**
	 * Gets the background colour.
	 *
	 * @return the background colour
	 */
	public Color getBackgroundColour()
	{
		return backgroundColour;
	}

	/**
	 * Gets the key code.
	 *
	 * @return the key code
	 */
	public int getKeyCode()
	{
		return defaultCharacter.getKeyCode();
	}

	/**
	 * Gets the key shape.
	 *
	 * @return the key shape
	 */
	public Shape getKeyShape()
	{
		return keyShape;
	}

	/**
	 * Gets the key string representation.
	 *
	 * @return the key string representation
	 */
	public String getKeyStringRepresentation()
	{
		return defaultCharacter.getStringRepresentation();
	}

	/**
	 * Gets the key text colour.
	 *
	 * @return the key text colour
	 */
	public Color getKeyTextColour()
	{
		return keyTextColour;
	}

	/**
	 * Gets the max x.
	 *
	 * @return the max x
	 */
	public int getMaxX()
	{
		return (int) keyShape.getBounds2D().getMaxX();
	}

	/**
	 * Gets the max y.
	 *
	 * @return the max y
	 */
	public int getMaxY()
	{
		return (int) keyShape.getBounds2D().getMaxY();
	}

	/**
	 * Gets the min x.
	 *
	 * @return the min x
	 */
	public int getMinX()
	{
		return (int) keyShape.getBounds2D().getMinX();
	}

	/**
	 * Gets the min y.
	 *
	 * @return the min y
	 */
	public int getMinY()
	{
		return (int) keyShape.getBounds2D().getMinY();
	}

	/**
	 * Gets the modifiers.
	 *
	 * @return the modifiers
	 */
	public KeyModifiers getModifiers()
	{
		return modifiers;
	}

	/**
	 * Checks if is enabled.
	 *
	 * @return true, if is enabled
	 */
	public boolean isEnabled()
	{
		return enabled;
	}

	/**
	 * Sets the background colour.
	 *
	 * @param backgroundColour
	 *            the new background colour
	 */
	public void setBackgroundColour(Color backgroundColour)
	{
		this.backgroundColour = backgroundColour;
	}

	/**
	 * Sets the enabled.
	 *
	 * @param enabled
	 *            the new enabled
	 */
	public void setEnabled(boolean enabled)
	{
		this.enabled = enabled;
	}

	/**
	 * Sets the key shape.
	 *
	 * @param keyShape
	 *            the new key shape
	 */
	public void setKeyShape(Shape keyShape)
	{
		this.keyShape = keyShape;
	}

	/**
	 * Sets the key text colour.
	 *
	 * @param keyTextColour
	 *            the new key text colour
	 */
	public void setKeyTextColour(Color keyTextColour)
	{
		this.keyTextColour = keyTextColour;
	}

	/**
	 * Sets the modifiers.
	 *
	 * @param modifiers
	 *            the new modifiers
	 */
	public void setModifiers(KeyModifiers modifiers)
	{
		this.modifiers = modifiers;
	}

}
