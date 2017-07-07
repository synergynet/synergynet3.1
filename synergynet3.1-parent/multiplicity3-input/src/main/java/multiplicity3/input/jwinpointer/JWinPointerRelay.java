/**
 * 
 */
package multiplicity3.input.jwinpointer;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import jwinpointer.JWinPointerReader.PointerEventListener;

/**
 * Listener for input events from the JWinPointer library.
 */
class JWinPointerRelay implements PointerEventListener, MouseListener, MouseMotionListener 
{

	/**
	 * Input handler to make calls back to.
	 */
	private JWinPointerTouchInput jWinPointerTouchInput;

	/**
	 * Initialise listener.
	 * 
	 * @param jWinPointerTouchInput Input handler to make calls back to.
	 */
	public JWinPointerRelay(JWinPointerTouchInput jWinPointerTouchInput) 
	{
		this.jWinPointerTouchInput = jWinPointerTouchInput;
	}

	/**
	 * Method called by the JWinPointer library when a pointer event occurs.
	 * Calls back to the input handler with the relevant touch event info.
	 * 
	 * @param deviceType The type of the device.
	 * @param pointerID The unique ID of the pointer.
	 * @param eventType The type of the event (1 drag, 2 hover, 3 down, 4 up).
	 * @param inverted Flag indicating whether the positional information has been inverted.
	 * @param x The x location of the event.
	 * @param y The y location of the event.
	 * @param pressure The pressure of the event.
	 */
	public void pointerXYEvent(int deviceType, int pointerID, int eventType, boolean inverted, int x, int y, int pressure) 
	{
		if (eventType == 1 || eventType == 3 || eventType == 4) 
		{
			jWinPointerTouchInput.updatedPointer(pointerID, eventType, x, y);
		}
	}
	
	
	// Unused inherited methods.
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
