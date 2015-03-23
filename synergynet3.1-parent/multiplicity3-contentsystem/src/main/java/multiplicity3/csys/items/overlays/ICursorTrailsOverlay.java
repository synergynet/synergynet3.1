package multiplicity3.csys.items.overlays;

import java.awt.Color;

import multiplicity3.csys.items.item.IItem;
import multiplicity3.input.IMultiTouchEventListener;
import multiplicity3.input.IMultiTouchEventProducer;

/**
 * The Interface ICursorTrailsOverlay.
 */
public interface ICursorTrailsOverlay extends IItem, IMultiTouchEventListener {

	/**
	 * Respond to item.
	 *
	 * @param item the item
	 */
	public void respondToItem(IItem item);

	/**
	 * Respond to multi touch input.
	 *
	 * @param multiTouchEventProducer the multi touch event producer
	 */
	public void respondToMultiTouchInput(
			IMultiTouchEventProducer multiTouchEventProducer);

	/**
	 * Sets the fading colour.
	 *
	 * @param c the new fading colour
	 */
	public void setFadingColour(Color c);

	/**
	 * Sets the solid colour.
	 *
	 * @param c the new solid colour
	 */
	public void setSolidColour(Color c);
}
