package multiplicity3.csys.items.keyboard.defs.norwegian;

import java.awt.event.KeyEvent;

import multiplicity3.csys.items.IEditableText;
import multiplicity3.csys.items.IFrame;
import multiplicity3.csys.items.keyboard.IKeyboard;
import multiplicity3.csys.items.keyboard.behaviour.IMultiTouchKeyboardListener;
import multiplicity3.csys.items.keyboard.model.KeyModifiers;
import multiplicity3.csys.items.keyboard.model.KeyboardKey;

public class NorwegianKeyboardListener implements IMultiTouchKeyboardListener {

    
    private IEditableText editItem;
    private IKeyboard keyboard;
    
    public NorwegianKeyboardListener(IEditableText editItem, IKeyboard keyboard) {
        this.editItem = editItem;
        this.keyboard = keyboard;
    }

    @Override
    public void keyPressed(KeyboardKey k, boolean shiftDown, boolean altDown,
            boolean ctlDown) {
        if(k.getKeyCode() == 222) {
//            if(!shiftDown)
//                editItem.appendString("æ");
//            else
                editItem.appendString("Æ");
        } else if(k.getKeyCode() == 59) {
//            if(!shiftDown)
//                editItem.appendString("ø");
//            else
                editItem.appendString("Ø");
        } else if(k.getKeyCode() == 91) {
//             if(!shiftDown)
//                 editItem.appendString("å");
//             else
                 editItem.appendString("Å");
             
         } else if(k.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
            editItem.removeChar();
       
        }else if(k.getKeyCode() == KeyEvent.VK_ENTER) {
            // ignore
        }else if(k.getKeyCode() !=  KeyEvent.VK_CANCEL && k.getModifiers() == KeyModifiers.NONE  ) {               
            if(shiftDown) {
                String txt = KeyEvent.getKeyText(k.getKeyCode()).toUpperCase();
                editItem.appendChar(Character.toUpperCase(txt.charAt(0)));
            }else{
                String txt = KeyEvent.getKeyText(k.getKeyCode()).toLowerCase();
                editItem.appendChar(Character.toUpperCase(txt.charAt(0)));
            }
        }
        
        if( editItem.getParentItem() instanceof IFrame ) {
            IFrame frame = (IFrame) editItem.getParentItem();
            frame.setSize(editItem.getWidth(), frame.getSize().y);
        }
        
        keyboard.reDrawKeyboard(shiftDown, altDown, ctlDown);
    }

    @Override
    public void keyReleased(KeyboardKey k, boolean shiftDown, boolean altDown,
            boolean ctlDown) {
        // TODO Auto-generated method stub
        
    }

}
