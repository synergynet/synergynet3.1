package multiplicity3.csys.items.overlays;

import multiplicity3.csys.items.item.IItem;
import multiplicity3.input.IMultiTouchEventListener;
import multiplicity3.input.IMultiTouchEventProducer;

/**
 * The Interface ICursorOverlay.
 */
public interface ICursorOverlay extends IItem, IMultiTouchEventListener
{

	/**
	 * Respond to item.
	 *
	 * @param item
	 *            the item
	 */
	void respondToItem(IItem item);

	/**
	 * Respond to multi touch input.
	 *
	 * @param multiTouchEventProducer
	 *            the multi touch event producer
	 */
	void respondToMultiTouchInput(IMultiTouchEventProducer multiTouchEventProducer);
}
