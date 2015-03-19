package multiplicity3.csys.behaviours.gesture;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import multiplicity3.csys.behaviours.IBehaviour;
import multiplicity3.csys.items.item.IItem;
import multiplicity3.csys.stage.IStage;
import multiplicity3.input.IMultiTouchEventListener;
import multiplicity3.input.events.MultiTouchCursorEvent;
import multiplicity3.input.events.MultiTouchObjectEvent;

public class GestureDetectionBehaviour implements IBehaviour, IMultiTouchEventListener {

	private IItem item;
	private List<IGestureListener> listeners = new ArrayList<IGestureListener>();
	private Map<Long, Gesture> currentGestures = new HashMap<Long,Gesture>();
	private boolean active = true;

	@Override
	public void setItemActingOn(IItem item) {
			
	}
	
	@Override
	public void setEventSource(IItem eventSourceItem) {
		this.item = eventSourceItem;		
	}
	
//	@Override
	public void addListener(IGestureListener l) {
		listeners.add((IGestureListener) l);
		item.getMultiTouchDispatcher().addListener(this);
	}

	@Override
	public void cursorChanged(MultiTouchCursorEvent event) {			
		if(!active ) return;
		Gesture g = currentGestures.get(event.getCursorID());
		if(g != null) {
			g.addPoint(event.getPosition());
		}
	}

	@Override
	public void cursorClicked(MultiTouchCursorEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cursorPressed(MultiTouchCursorEvent event) {			
		if(!active ) return;
		Gesture g = new Gesture("c" + event.getCursorID());
		g.addPoint(event.getPosition());
		currentGestures.put(event.getCursorID(), g);
	}

	@Override
	public void cursorReleased(MultiTouchCursorEvent event) {			
		if(!active ) return;
		Gesture g = currentGestures.get(event.getCursorID());
		if(g != null) {
			g.addPoint(event.getPosition());
		}
		detectGesture(g, event);		
		currentGestures.remove(event.getCursorID());
	}

	@Override
	public void objectAdded(MultiTouchObjectEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void objectChanged(MultiTouchObjectEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void objectRemoved(MultiTouchObjectEvent event) {
		// TODO Auto-generated method stub
		
	}
	
	private void detectGesture(Gesture g, MultiTouchCursorEvent event) {
		GestureMatch match = GestureLibrary.getInstance().findGestureMatch(g, 0.1f);
		if(match != null) {
			for(IGestureListener l : listeners) {
				l.gestureDetected(match, item);
			}
		}
	}
	
	@Override
	public void setStage(IStage stage) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setActive(boolean active) {
		this.active = active;		
	}



}
