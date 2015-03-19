package multiplicity3.csys.items.border;

import multiplicity3.csys.items.item.IItem;

import com.jme3.math.Vector2f;
import com.jme3.math.ColorRGBA;

public interface IRoundedBorder extends IItem {
	public Vector2f getSize();
	public void setSize(Vector2f size);
	public void setSize(float width, float height);
	public void setBorderWidth(float borderSize);
	public void setColor(ColorRGBA color);
	public float getBorderWidth();
}
