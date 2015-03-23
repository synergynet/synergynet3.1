package multiplicity3.csys.items;

import java.awt.Color;
import java.awt.Font;

import multiplicity3.csys.items.item.IItem;

import com.jme3.math.Vector2f;

/**
 * The Interface ILine.
 */
public interface ILine {

	/**
	 * Sets the annotation enabled.
	 *
	 * @param isEnabled the new annotation enabled
	 */
	public void setAnnotationEnabled(boolean isEnabled);

	/**
	 * Sets the arrow mode.
	 *
	 * @param arrowMode the new arrow mode
	 */
	public void setArrowMode(int arrowMode);

	/**
	 * Sets the arrows enabled.
	 *
	 * @param isEnabled the new arrows enabled
	 */
	public void setArrowsEnabled(boolean isEnabled);

	/**
	 * Sets the line colour.
	 *
	 * @param lineColour the new line colour
	 */
	public void setLineColour(Color lineColour);

	/**
	 * Sets the line mode.
	 *
	 * @param lineMode the new line mode
	 */
	public void setLineMode(int lineMode);

	/**
	 * Sets the source item.
	 *
	 * @param sourceItem the new source item
	 */
	public void setSourceItem(IItem sourceItem);

	/**
	 * Sets the source location.
	 *
	 * @param sourceLocation the new source location
	 */
	public void setSourceLocation(Vector2f sourceLocation);

	/**
	 * Sets the target item.
	 *
	 * @param targetItem the new target item
	 */
	public void setTargetItem(IItem targetItem);

	/**
	 * Sets the target location.
	 *
	 * @param targetPoint the new target location
	 */
	public void setTargetLocation(Vector2f targetPoint);

	/**
	 * Sets the text.
	 *
	 * @param text the new text
	 */
	public void setText(String text);

	/**
	 * Sets the text colour.
	 *
	 * @param textColour the new text colour
	 */
	public void setTextColour(Color textColour);

	/**
	 * Sets the text font.
	 *
	 * @param textFont the new text font
	 */
	public void setTextFont(Font textFont);

	/**
	 * Sets the width.
	 *
	 * @param lineWidth the new width
	 */
	public void setWidth(float lineWidth);

}
