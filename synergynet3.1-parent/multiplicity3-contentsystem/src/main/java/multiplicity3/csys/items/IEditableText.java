package multiplicity3.csys.items;

public interface IEditableText extends ILabel {
	public void setCursorAt(int index);
	public void setCursorDisplay(boolean onOrOff);
	public void insertChar(char c);
	public void removeChar();
	public void appendChar(char c);
	public void appendString(String charSet);
	public void setCursorAtEnd();
}
