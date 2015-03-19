package multiplicity3.csys.items.keyboard;

import java.awt.Graphics2D;

import multiplicity3.csys.items.keyboard.behaviour.IMultiTouchKeyboardListener;

public interface IKeyboardGraphicsRenderer extends IMultiTouchKeyboardListener {
	public void drawKeyboard(Graphics2D g2d, boolean shiftDown, boolean altDown, boolean ctlDown);
}
