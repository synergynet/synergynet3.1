package multiplicity3.csys.items.keyboard.model;

public class CharacterKey {
	private String stringRepresentation;
	private int keyCode;

	public CharacterKey(String stringRepresentation, int keyCode) {
		this.setStringRepresentation(stringRepresentation);
		this.setKeyCode(keyCode);
	}

	public void setStringRepresentation(String stringRepresentation) {
		this.stringRepresentation = stringRepresentation;
	}

	public String getStringRepresentation() {
		return stringRepresentation;
	}

	public void setKeyCode(int keyCode) {
		this.keyCode = keyCode;
	}

	public int getKeyCode() {
		return keyCode;
	}
}
