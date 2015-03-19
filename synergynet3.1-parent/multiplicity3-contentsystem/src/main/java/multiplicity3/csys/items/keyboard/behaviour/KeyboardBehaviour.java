package multiplicity3.csys.items.keyboard.behaviour;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.jme3.math.Vector2f;

import multiplicity3.csys.behaviours.IBehaviour;
import multiplicity3.csys.items.item.IItem;
import multiplicity3.csys.items.keyboard.IKeyboard;
import multiplicity3.csys.items.keyboard.model.KeyModifiers;
import multiplicity3.csys.items.keyboard.model.KeyboardKey;
import multiplicity3.csys.stage.IStage;
import multiplicity3.input.IMultiTouchEventListener;
import multiplicity3.input.events.MultiTouchCursorEvent;
import multiplicity3.input.events.MultiTouchEvent;
import multiplicity3.input.events.MultiTouchObjectEvent;

public class KeyboardBehaviour implements IBehaviour, IMultiTouchEventListener {
	
	private IKeyboard item;
	private List<IMultiTouchKeyboardListener> listeners = new ArrayList<IMultiTouchKeyboardListener>();
	private Map<Long,KeyboardKey> trackedKeyPresses = new HashMap<Long,KeyboardKey>();
	private Map<String,Long> keyPressTimes = new HashMap<String,Long>();
	private boolean shiftDown = false;
	private boolean ctlDown = false;
	private boolean altDown = false;
	private long minTimeBetweenKeyPresses = 10;
	private IStage stage; 
	private boolean active = true;

	
	public void setMinimumTimeBetweenKeyPressesMS(long milliseconds) {
		this.minTimeBetweenKeyPresses = milliseconds;
	}

	@Override
	public void setItemActingOn(IItem item) {
		// unneeded
	}
	
	@Override
	public void setEventSource(IItem eventSourceItem) {
		if(eventSourceItem == null && this.item != null) {
			item.getMultiTouchDispatcher().remove(this);
		}
		if(eventSourceItem instanceof IKeyboard) {
			this.item = (IKeyboard) eventSourceItem;
			item.getMultiTouchDispatcher().addListener(this);
		}else{
			//TODO: log severe
		}
	}

	public void addListener(IMultiTouchKeyboardListener l) {
		if(!listeners.contains(l)) {
			listeners.add(l);			
		}
	}

	@Override
	public void cursorChanged(MultiTouchCursorEvent event) {			
		if(!active ) return;

		KeyboardKey kk = trackedKeyPresses.get(event.getCursorID());
		if(kk != null) {
			KeyboardKey newKey = getKeyUnderEvent(event);
			if(newKey == null || !(newKey.getKeyCode() == kk.getKeyCode())) {

				if(kk.getModifiers() == KeyModifiers.SHIFT) {
					shiftDown = false;
				}

				for(IMultiTouchKeyboardListener kl : listeners) {
					kl.keyReleased(kk, shiftDown, altDown, ctlDown);
				}
			}
		}

	}

	@Override
	public void cursorClicked(MultiTouchCursorEvent event) {

	}

	@Override
	public void cursorPressed(MultiTouchCursorEvent event) {
		if(!active ) return;
		KeyboardKey kk = getKeyUnderEvent(event);
		
		if(kk != null && kk.isEnabled()) {			
			if(kk.getModifiers() == KeyModifiers.SHIFT) {
				shiftDown = true;
				trackedKeyPresses.put(event.getCursorID(), kk);
			}

			for(IMultiTouchKeyboardListener kl : listeners) {
				kl.keyPressed(kk, shiftDown, altDown, ctlDown);
			}

		}		
	}

	@Override
	public void cursorReleased(MultiTouchCursorEvent event) {
		if(!active ) return;
		KeyboardKey kk = getKeyUnderEvent(event);
		if(kk != null && kk.isEnabled()) {
			if(kk.getModifiers() == KeyModifiers.SHIFT) {
				shiftDown = false;
			}
			
			if(keyPressTimes.containsKey(kk.getKeyStringRepresentation())) {
				long lastTime = keyPressTimes.get(kk.getKeyStringRepresentation());
				if(lastTime > 0) {
					long duration = System.currentTimeMillis() - lastTime;
					if(duration < minTimeBetweenKeyPresses) {
						return;
					}
				}
			}
			keyPressTimes.put(kk.getKeyStringRepresentation(), System.currentTimeMillis());
			
			for(IMultiTouchKeyboardListener kl : listeners) {
				kl.keyReleased(kk, shiftDown, altDown, ctlDown);
			}
		}

	}

	private KeyboardKey getKeyUnderEvent(MultiTouchEvent event) {
		Vector2f screenPosition = new Vector2f();
		stage.tableToScreen(event.getPosition(), screenPosition);		
		Vector2f localPosition = item.getRelativeLocationOfWorldLocation(new Vector2f(screenPosition.x, screenPosition.y));		
		localPosition.x = localPosition.x += (float) item.getKeyboardDefinition().getBounds().getWidth() / 2.0f;
		localPosition.y = localPosition.y += (float) item.getKeyboardDefinition().getBounds().getHeight() / 2.0f;
		Point2D p = new Point2D.Float(localPosition.x, (float)item.getKeyboardDefinition().getBounds().getMaxY() - localPosition.y);
		return item.getKeyboardDefinition().getKeyAt(p);
	}

	@Override
	public void objectAdded(MultiTouchObjectEvent event) {}

	@Override
	public void objectChanged(MultiTouchObjectEvent event) {}

	@Override
	public void objectRemoved(MultiTouchObjectEvent event) {}

	@Override
	public void setStage(IStage stage) {
		this.stage = stage;		
	}

	@Override
	public void setActive(boolean active) {
		this.active = active;		
	}
}
