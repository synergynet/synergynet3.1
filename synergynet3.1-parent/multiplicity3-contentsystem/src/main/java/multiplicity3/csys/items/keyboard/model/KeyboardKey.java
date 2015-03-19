package multiplicity3.csys.items.keyboard.model;

import java.awt.Color;
import java.awt.Shape;


public class KeyboardKey {
	private Shape keyShape;
	private Color backgroundColour = Color.white;
	private Color keyTextColour = Color.darkGray;
	private CharacterKey defaultCharacter;
	private KeyModifiers modifiers = KeyModifiers.NONE;
	private boolean enabled = true;
	
	public KeyboardKey(CharacterKey defaultCharacter, Shape keyShape) {
		this.defaultCharacter = defaultCharacter;
		this.setKeyShape(keyShape);
	}

	public String getKeyStringRepresentation() {
		return defaultCharacter.getStringRepresentation();
	}
	
	public int getKeyCode() {
		return defaultCharacter.getKeyCode();
	}

	public void setKeyShape(Shape keyShape) {
		this.keyShape = keyShape;
	}

	public Shape getKeyShape() {
		return keyShape;
	}
	
	public int getMinX() {
		return (int) keyShape.getBounds2D().getMinX();	
	}
	
	public int getMinY() {
		return (int) keyShape.getBounds2D().getMinY();	
	}
	
	public int getMaxX() {
		return (int) keyShape.getBounds2D().getMaxX();
	}
	
	public int getMaxY() {
		return (int) keyShape.getBounds2D().getMaxY();
	}

	public void setModifiers(KeyModifiers modifiers) {
		this.modifiers = modifiers;
	}

	public KeyModifiers getModifiers() {
		return modifiers;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setBackgroundColour(Color backgroundColour) {
		this.backgroundColour = backgroundColour;
	}

	public Color getBackgroundColour() {
		return backgroundColour;
	}

	public void setKeyTextColour(Color keyTextColour) {
		this.keyTextColour = keyTextColour;
	}

	public Color getKeyTextColour() {
		return keyTextColour;
	}

}
