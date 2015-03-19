package multiplicity3.csys.items.mutablelabel;

import com.jme3.math.Vector2f;

import multiplicity3.csys.items.item.IItem;

public interface IMutableLabel extends IItem {
	public void setText(String text);
	public void setFont(String resourcePath);
	public void setBoxSize(float width, float height);
	public boolean isBoxSizeSet();
	public void removeChar();
	public String getText();
	public void appendChar(char charAt);
	public Vector2f getTextSize();
	public void setFontScale(float f);
}
