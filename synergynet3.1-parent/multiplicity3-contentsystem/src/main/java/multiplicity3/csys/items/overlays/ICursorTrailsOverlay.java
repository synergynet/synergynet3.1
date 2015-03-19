package multiplicity3.csys.items.overlays;

import java.awt.Color;

import multiplicity3.csys.items.item.IItem;
import multiplicity3.input.IMultiTouchEventListener;
import multiplicity3.input.IMultiTouchEventProducer;

public interface ICursorTrailsOverlay extends IItem, IMultiTouchEventListener {
	public void respondToItem(IItem item);
	public void respondToMultiTouchInput(IMultiTouchEventProducer multiTouchEventProducer);
	public void setSolidColour(Color c);
	public void setFadingColour(Color c);
}
