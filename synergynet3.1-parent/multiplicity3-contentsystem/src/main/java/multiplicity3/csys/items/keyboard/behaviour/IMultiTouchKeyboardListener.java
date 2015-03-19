package multiplicity3.csys.items.keyboard.behaviour;

import multiplicity3.csys.items.keyboard.model.KeyboardKey;

public interface IMultiTouchKeyboardListener {
	public void keyPressed(KeyboardKey k, boolean shiftDown, boolean altDown, boolean ctlDown);
	public void keyReleased(KeyboardKey k, boolean shiftDown, boolean altDown, boolean ctlDown);
}
