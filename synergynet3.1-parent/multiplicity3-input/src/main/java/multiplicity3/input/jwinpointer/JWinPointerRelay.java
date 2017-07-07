/**
 * 
 */
package multiplicity3.input.jwinpointer;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import jwinpointer.JWinPointerReader.PointerEventListener;

class JWinPointerRelay implements PointerEventListener, MouseListener, MouseMotionListener 
{

	private JWinPointerTouchInput jWinPointerTouchInput;

	public JWinPointerRelay(JWinPointerTouchInput jWinPointerTouchInput) 
	{
		this.jWinPointerTouchInput = jWinPointerTouchInput;
	}

	public void pointerXYEvent(int deviceType, int pointerID, int eventType, boolean inverted, int x, int y, int pressure) {

		if (eventType == 1 || eventType == 3 || eventType == 4) 
		{
			jWinPointerTouchInput.updatedPointer(pointerID, eventType, x, y);
		}
	}
	
	public void mouseClicked( MouseEvent e ) {}
	public void mouseEntered( MouseEvent e ) {}
	public void mouseExited( MouseEvent e ) {}
	public void mousePressed( MouseEvent e ) {}
	public void mouseReleased( MouseEvent e ) {}
	public void mouseMoved( MouseEvent e ) {}
	public void mouseDragged( MouseEvent e ) {}
	public void pointerButtonEvent(int deviceType, int pointerID, int eventType, boolean inverted, int buttonIndex) {}
	public void pointerEvent(int deviceType, int pointerID, int eventType, boolean inverted) {}
	
}
