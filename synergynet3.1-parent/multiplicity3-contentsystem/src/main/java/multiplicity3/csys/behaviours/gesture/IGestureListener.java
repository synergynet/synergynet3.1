package multiplicity3.csys.behaviours.gesture;

import multiplicity3.csys.items.item.IItem;

public interface IGestureListener {
	public void gestureDetected(GestureMatch match, IItem item);
}
