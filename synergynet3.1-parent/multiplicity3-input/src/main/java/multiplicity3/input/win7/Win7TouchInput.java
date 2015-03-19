/*
 * mt4j Copyright (c) 2008 - 2010 Christopher Ruff, Fraunhofer-Gesellschaft All rights reserved.
 *  
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
*/
package multiplicity3.input.win7;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import multiplicity3.input.IMultiTouchEventListener;
import multiplicity3.input.IMultiTouchInputSource;
import multiplicity3.input.events.MultiTouchCursorEvent;
import multiplicity3.input.exceptions.MultiTouchInputException;
import multiplicity3.input.utils.ClickDetector;

import org.mt4j.input.inputSources.Windows7TouchEvent;
import org.mt4j.input.inputSources.Win7NativeTouchSource;

import com.jme3.math.Vector2f;


/**
 * Input source for native Windows 7 WM_TOUCH messages for single/multi-touch.
 */
public class Win7TouchInput implements IMultiTouchInputSource {
	
	protected Map<Long,Win7Cursor> fingerCursors = new HashMap<Long,Win7Cursor>();

	protected List<IMultiTouchEventListener> listeners = new ArrayList<IMultiTouchEventListener>();
	protected ClickDetector clickDetector = new ClickDetector(500, 2f);
	
	protected List<Callable<Object>> callingList = new ArrayList<Callable<Object>>();
		
	private float width = 1024f;	
	private float height = 768f;
	
	private boolean is64bitJava = false;
	
	private Win7NativeTouchSource win7NativeTouchSource;
	
	/**
	 * Instantiates a new win7 native touch source.
	 */
	public Win7TouchInput(float width, float height, boolean is64bitJava) {
		this.width = width;
		this.height = height;
		this.is64bitJava = is64bitJava;
		start();
	}
		
	public void start() {
		synchronized(this) {			
			win7NativeTouchSource = new Win7NativeTouchSource(this);			
		}
	}
	

	public void addTouchCursor(final Windows7TouchEvent wmTouchEvent){

		final long touchID = wmTouchEvent.id;
		Win7Cursor fingerCursor = fingerCursors.get(touchID);
		if(fingerCursor == null) {
			fingerCursor = new Win7Cursor();			
			fingerCursors.put(touchID, fingerCursor);
						
			Callable<Object> c = new Callable<Object>() {

				float xpos = wmTouchEvent.x/width;
				float ypos = wmTouchEvent.y/height;							
				float xContactSize = wmTouchEvent.contactSizeX;
				float yContactSize = wmTouchEvent.contactSizeY;
				
				@Override
				public Object call() throws Exception {
					
					final Win7Cursor fingerCursor = fingerCursors.get(touchID);
					if(fingerCursor != null) {	
						fingerCursor.setPosition(new Vector2f(xpos, 1-ypos));
						fingerCursor.setContactSize(new Vector2f(xContactSize, yContactSize));
						for(IMultiTouchEventListener listener : listeners) {
							clickDetector.newCursorPressed(fingerCursor.getId(), fingerCursor.getPosition());
							MultiTouchCursorEvent evt = 
									new MultiTouchCursorEvent(fingerCursor.getId(), fingerCursor.getPosition());
							listener.cursorPressed(evt);
						}
					}
					return null;
				}
			};
			synchronized(callingList) {
				callingList.add(c);
			}
		}
	}
					
	public void updateTouchCursor(final Windows7TouchEvent wmTouchEvent){
			
		Callable<Object> c = new Callable<Object>() {
			
			float xpos = wmTouchEvent.x/width; 
			float ypos = wmTouchEvent.y/height;							
			float xContactSize = wmTouchEvent.contactSizeX;
			float yContactSize = wmTouchEvent.contactSizeY;
			
			long touchID = wmTouchEvent.id;		
			
			@Override
			public Object call() throws Exception {
				final Win7Cursor fingerCursor = fingerCursors.get(touchID);
				if(fingerCursor != null) {
					fingerCursor.setPosition(new Vector2f(xpos, 1-ypos));
					fingerCursor.setContactSize(new Vector2f(xContactSize, yContactSize));
					for(IMultiTouchEventListener listener : listeners) {
						MultiTouchCursorEvent evt =
								new MultiTouchCursorEvent(fingerCursor.getId(), fingerCursor.getPosition());
						listener.cursorChanged(evt);
					}
				}
				return null;
			}
		};
		
		synchronized(callingList) {
			callingList.add(c);
		}
	}
	
	public void removeTouchCursor(final Windows7TouchEvent wmTouchEvent){
		Callable<Object> c = new Callable<Object>() {
			
			long touchID = wmTouchEvent.id;

			@Override
			public Object call() throws Exception {
				final Win7Cursor fingerCursor = fingerCursors.get(touchID);

				if(fingerCursor != null) {

					for(IMultiTouchEventListener l : listeners) {
						MultiTouchCursorEvent event = 
								new MultiTouchCursorEvent(fingerCursor.getId(), fingerCursor.getPosition());
						int clickCount = clickDetector.cursorReleasedGetClickCount(
								fingerCursor.getId(), fingerCursor.getPosition());
						if(clickCount > 0) {
							event.setClickCount(clickCount);
							l.cursorClicked(event);
						}
						l.cursorReleased(event);
						
					}
					fingerCursors.remove(touchID);

				}
				return null;
			}
		};
		synchronized(callingList) {
			callingList.add(c);
		}
	}
	
	public boolean isIs64bitJava() {
		return is64bitJava;
	}

	public void setIs64bitJava(boolean is64bitJava) {
		this.is64bitJava = is64bitJava;
	}
	
	@Override
	public void registerMultiTouchEventListener(IMultiTouchEventListener listener) {
		if(!listeners.contains(listener)) listeners.add(listener);			
	}

	@Override
	public void registerMultiTouchEventListener(IMultiTouchEventListener listener, int index) {
		if(!listeners.contains(listener)) listeners.add(index, listener);
	}

	@Override
	public void unregisterMultiTouchEventListener(IMultiTouchEventListener listener) {
		listeners.remove(listener);		
	}

	@Override
	public void setClickSensitivity(long time, float distance) {
		this.clickDetector = new ClickDetector(time, distance);
	}

	@Override
	public void update(float tpf) throws MultiTouchInputException {
		win7NativeTouchSource.pollEvents();
		synchronized(callingList) {
			for(Callable<Object> c : callingList) {
				try {
					c.call();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			callingList.clear();
		}
	}

	@Override
	public boolean requiresMouseDisplay() {
		return false;
	}

	@Override
	public void endListening() {}

}
