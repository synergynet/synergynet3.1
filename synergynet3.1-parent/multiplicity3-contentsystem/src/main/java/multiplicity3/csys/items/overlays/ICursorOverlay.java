package multiplicity3.csys.items.overlays;

import multiplicity3.csys.items.item.IItem;
import multiplicity3.input.IMultiTouchEventListener;
import multiplicity3.input.IMultiTouchEventProducer;

public interface ICursorOverlay extends IItem, IMultiTouchEventListener {
	void respondToItem(IItem item);
	void respondToMultiTouchInput(IMultiTouchEventProducer multiTouchEventProducer);
}
