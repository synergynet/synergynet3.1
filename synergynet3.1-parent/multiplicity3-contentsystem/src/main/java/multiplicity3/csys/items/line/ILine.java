package multiplicity3.csys.items.line;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;

import multiplicity3.csys.items.item.IItem;


/**
 * This line will link two items together such that when those items
 * are updated, the line will be updated too.
 * @author dcs0ah1
 *
 */

public interface ILine extends IItem {
	
	public enum LineMode {
		LINKED,
		UNLINKED
	}
	
	public LineMode getMode();	
	
	public void setStartPosition(Vector2f v);
	public Vector2f getStartPosition();
	public void setEndPosition(Vector2f v);
	public Vector2f getEndPosition();
	public void setLineColour(ColorRGBA c);
	public void setLineWidth(float width);
	public float getLength();

	public void setSourceItem(IItem item);
	public IItem getSourceItem();
	public void setDestinationItem(IItem item);
	public IItem getDestinationItem();
	
	public void setLineVisibilityChangesWithItemVisibility(boolean autoVisibilityChange);

}
