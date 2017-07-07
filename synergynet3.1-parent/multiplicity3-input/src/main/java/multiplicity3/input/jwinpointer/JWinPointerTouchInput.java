/*
 * mt4j Copyright (c) 2008 - 2010 Christopher Ruff, Fraunhofer-Gesellschaft All
 * rights reserved. This program is free software: you can redistribute it
 * and/or modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version. This program is distributed in the
 * hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details. You should have received a
 * copy of the GNU General Public License along with this program. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package multiplicity3.input.jwinpointer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import com.jme3.math.Vector2f;

import multiplicity3.input.IMultiTouchEventListener;
import multiplicity3.input.IMultiTouchInputSource;
import multiplicity3.input.events.MultiTouchCursorEvent;
import multiplicity3.input.exceptions.MultiTouchInputException;
import multiplicity3.input.utils.ClickDetector;

import jwinpointer.JWinPointerReader;

/**
 * Input handler for JWinPointer messages for single/multi-touch.
 */
public class JWinPointerTouchInput implements IMultiTouchInputSource
{

	/** 
	 * The calling list. 
	 */
	protected List<Callable<Object>> callingList = new ArrayList<Callable<Object>>();

	/** 
	 * The click detector. 
	 */
	protected ClickDetector clickDetector = new ClickDetector(500, 2f);

	/**
	 * Listener for input events from the JWinPointer library.
	 */
	private JWinPointerReader jWinPointerReader;

	/** 
	 * The listeners. 
	 */
	protected List<IMultiTouchEventListener> listeners = new ArrayList<IMultiTouchEventListener>();	
	
	/** 
	 * The height. 
	 */
	private float height = 768f;

	/** 
	 * The width. 
	 */
	private float width = 1024f;	


	/**
	 * Instantiates a new JWinPointer touch source.
	 * 
	 * @param width The width of the window.
	 * @param height The height of the window
	 */
	public JWinPointerTouchInput(float width, float height)
	{
		
		// Establish dimensions.
		this.width = width;
		this.height = height;
		
		// Create the listener.
		JWinPointerRelay relay = new JWinPointerRelay(this);
		
		// Start the JWinPointer library and pass it the listener.
		jWinPointerReader = new JWinPointerReader("Multiplicity v3.0");
		jWinPointerReader.addPointerEventListener(relay);
		
	}

	/* (non-Javadoc)
	 * @see multiplicity3.input.IMultiTouchInputSource#endListening()
	 */
	@Override
	public void endListening(){ }

	/* (non-Javadoc)
	 * @see multiplicity3.input.IMultiTouchInputSource#registerMultiTouchEventListener(multiplicity3.input.IMultiTouchEventListener)
	 */
	@Override
	public void registerMultiTouchEventListener(IMultiTouchEventListener listener)
	{
		if (!listeners.contains(listener))
		{
			listeners.add(listener);
		}
	}

	/* (non-Javadoc)
	 * @see multiplicity3.input.IMultiTouchInputSource#registerMultiTouchEventListener(multiplicity3.input.IMultiTouchEventListener, int)
	 */
	@Override
	public void registerMultiTouchEventListener(IMultiTouchEventListener listener, int index)
	{
		if (!listeners.contains(listener))
		{
			listeners.add(index, listener);
		}
	}

	/* (non-Javadoc)
	 * @see multiplicity3.input.IMultiTouchInputSource#requiresMouseDisplay()
	 */
	@Override
	public boolean requiresMouseDisplay()
	{
		return false;
	}

	/* (non-Javadoc)
	 * @see multiplicity3.input.IMultiTouchInputSource#setClickSensitivity(long, float)
	 */
	@Override
	public void setClickSensitivity(long time, float distance)
	{
		this.clickDetector = new ClickDetector(time, distance);
	}

	/* (non-Javadoc)
	 * @see multiplicity3.input.IMultiTouchInputSource#unregisterMultiTouchEventListener(multiplicity3.input.IMultiTouchEventListener)
	 */
	@Override
	public void unregisterMultiTouchEventListener(IMultiTouchEventListener listener)
	{
		listeners.remove(listener);		
	}

	/* (non-Javadoc)
	 * @see multiplicity3.input.IMultiTouchInputSource#update(float)
	 */
	@Override
	public void update(float tpf) throws MultiTouchInputException
	{
		// Synchronous block to perform queued events triggered by the listener in.
		synchronized (callingList)
		{
			for (Callable<Object> c : callingList)
			{
				try
				{
					c.call();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
			callingList.clear();
		}
	}
	
	/**
	 * Method called back by the listener.
	 * 
	 * @param pointerID The unique ID of the pointer.
	 * @param eventType The type of the event (1 drag, 2 hover, 3 down, 4 up).
	 * @param x The x location of the event.
	 * @param y The y location of the event.
	 */
	public void updatedPointer(final int pointerID, final int eventType, final int x, final int y) 
	{
		
		// Create a call-able object to call later in a synchronous block.
		Callable<Object> c = new Callable<Object>()
		{
			
			@Override
			public Object call() throws Exception
			{
				
				// Map JWinPointer co-ordinates to SynergyNet ones.
				Vector2f p = new Vector2f(x/width, 1 - (y/height));		
				MultiTouchCursorEvent event = new MultiTouchCursorEvent(pointerID, p);
				 
				// Loop through listeners.
				for (IMultiTouchEventListener listener : listeners)
				{
					
					// Adapt to event type.
					switch (eventType)
					{
						case 1:
							// Drag event.
							listener.cursorChanged(event);
							break;
						case 3:
							// Pressed down - start checking for click event too.
							clickDetector.newCursorPressed(pointerID, p);
							listener.cursorPressed(event);
							break;
						case 4:
							// Released press - check if it was a click then release.
							int clickCount = clickDetector.cursorReleasedGetClickCount(pointerID, p);
							if (clickCount > 0)
							{
								event.setClickCount(clickCount);
								listener.cursorClicked(event);
							}
							listener.cursorReleased(event);
							break;
					}
				}
				return null;		
			}
		};
		
		// Synchronously add the call-able object to the list to be looped through later.
		synchronized (callingList)
		{
			callingList.add(c);
		}
		
	}
	
}
