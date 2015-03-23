package multiplicity3.csys.items.keyboard.defs.norwegian;

import java.awt.event.KeyEvent;

import multiplicity3.csys.items.IEditableText;
import multiplicity3.csys.items.IFrame;
import multiplicity3.csys.items.keyboard.IKeyboard;
import multiplicity3.csys.items.keyboard.behaviour.IMultiTouchKeyboardListener;
import multiplicity3.csys.items.keyboard.model.KeyModifiers;
import multiplicity3.csys.items.keyboard.model.KeyboardKey;

/**
 * The listener interface for receiving norwegianKeyboard events. The class that
 * is interested in processing a norwegianKeyboard event implements this
 * interface, and the object created with that class is registered with a
 * component using the component's
 * <code>addNorwegianKeyboardListener<code> method. When
 * the norwegianKeyboard event occurs, that object's appropriate
 * method is invoked.
 *
 * @see NorwegianKeyboardEvent
 */
public class NorwegianKeyboardListener implements IMultiTouchKeyboardListener {

	/** The edit item. */
	private IEditableText editItem;

	/** The keyboard. */
	private IKeyboard keyboard;

	/**
	 * Instantiates a new norwegian keyboard listener.
	 *
	 * @param editItem the edit item
	 * @param keyboard the keyboard
	 */
	public NorwegianKeyboardListener(IEditableText editItem, IKeyboard keyboard) {
		this.editItem = editItem;
		this.keyboard = keyboard;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.csys.items.keyboard.behaviour.IMultiTouchKeyboardListener
	 * #keyPressed(multiplicity3.csys.items.keyboard.model.KeyboardKey, boolean,
	 * boolean, boolean)
	 */
	@Override
	public void keyPressed(KeyboardKey k, boolean shiftDown, boolean altDown,
			boolean ctlDown) {
		if (k.getKeyCode() == 222) {
			// if(!shiftDown)
			// editItem.appendString("�");
			// else
			editItem.appendString("�");
		} else if (k.getKeyCode() == 59) {
			// if(!shiftDown)
			// editItem.appendString("�");
			// else
			editItem.appendString("�");
		} else if (k.getKeyCode() == 91) {
			// if(!shiftDown)
			// editItem.appendString("�");
			// else
			editItem.appendString("�");

		} else if (k.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
			editItem.removeChar();

		} else if (k.getKeyCode() == KeyEvent.VK_ENTER) {
			// ignore
		} else if ((k.getKeyCode() != KeyEvent.VK_CANCEL)
				&& (k.getModifiers() == KeyModifiers.NONE)) {
			if (shiftDown) {
				String txt = KeyEvent.getKeyText(k.getKeyCode()).toUpperCase();
				editItem.appendChar(Character.toUpperCase(txt.charAt(0)));
			} else {
				String txt = KeyEvent.getKeyText(k.getKeyCode()).toLowerCase();
				editItem.appendChar(Character.toUpperCase(txt.charAt(0)));
			}
		}

		if (editItem.getParentItem() instanceof IFrame) {
			IFrame frame = (IFrame) editItem.getParentItem();
			frame.setSize(editItem.getWidth(), frame.getSize().y);
		}

		keyboard.reDrawKeyboard(shiftDown, altDown, ctlDown);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.csys.items.keyboard.behaviour.IMultiTouchKeyboardListener
	 * #keyReleased(multiplicity3.csys.items.keyboard.model.KeyboardKey,
	 * boolean, boolean, boolean)
	 */
	@Override
	public void keyReleased(KeyboardKey k, boolean shiftDown, boolean altDown,
			boolean ctlDown) {
		// TODO Auto-generated method stub

	}

}
