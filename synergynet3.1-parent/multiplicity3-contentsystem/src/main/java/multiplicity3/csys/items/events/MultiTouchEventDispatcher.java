package multiplicity3.csys.items.events;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import multiplicity3.input.IMultiTouchEventListener;
import multiplicity3.input.events.MultiTouchCursorEvent;
import multiplicity3.input.events.MultiTouchObjectEvent;

public class MultiTouchEventDispatcher implements IMultiTouchEventListener {

	protected List<IMultiTouchEventListener> items = new CopyOnWriteArrayList<IMultiTouchEventListener>();
	private boolean enabled = true;

	public void setEnabled(boolean b) {		
		this.enabled = b;
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public void addListener(IMultiTouchEventListener listener) {
		if(listener != null && !items.contains(listener)) {
			items.add(listener);
		}
	}

	@Override
	public void cursorPressed(MultiTouchCursorEvent event) {
		if(!enabled) return;
		for(IMultiTouchEventListener item : items) {
			item.cursorPressed(event);
		}
	}

	@Override
	public void cursorReleased(MultiTouchCursorEvent event) {
		if(!enabled) return;
        for(IMultiTouchEventListener item : items) {
            item.cursorReleased(event);
        }
	}	

	@Override
	public void cursorClicked(MultiTouchCursorEvent event) {
		if(!enabled) return;
		for(IMultiTouchEventListener item : items) {
			item.cursorClicked(event);
		}
	}

	@Override
	public void cursorChanged(MultiTouchCursorEvent event) {
		if(!enabled) return;
		for(IMultiTouchEventListener item : items) {
			item.cursorChanged(event);
		}
	}

	@Override
	public void objectAdded(MultiTouchObjectEvent event) {
		if(!enabled) return;
		for(IMultiTouchEventListener item : items) {
			item.objectAdded(event);
		}
	}

	@Override
	public void objectRemoved(MultiTouchObjectEvent event) {
		if(!enabled) return;
		for(IMultiTouchEventListener item : items) {
			item.objectRemoved(event);
		}
	}

	@Override
	public void objectChanged(MultiTouchObjectEvent event) {
		if(!enabled) return;
		for(IMultiTouchEventListener item : items) {
			item.objectChanged(event);
		}
	}
	
	public String toString() {
		return MultiTouchEventDispatcher.class.getName() + " containing " + items;
	}

	public List<IMultiTouchEventListener> getListeners() {
		return items;
	}

	public void addListeners(List<IMultiTouchEventListener> listeners) {
		for(IMultiTouchEventListener l : listeners) {
			addListener(l);
		}		
	}

	public void remove(IMultiTouchEventListener listener) {
		items.remove(listener);		
	}
	
	public void removeListeners(List<IMultiTouchEventListener> listeners) {
		for(IMultiTouchEventListener l : listeners) {
			remove(l);
		}		
	}
}
