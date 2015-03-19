package multiplicity3.csys.behaviours;

import java.util.logging.Logger;

import multiplicity3.csys.items.item.IItem;
import multiplicity3.csys.stage.IStage;
import multiplicity3.input.IMultiTouchEventListener;
import multiplicity3.input.events.MultiTouchCursorEvent;
import multiplicity3.input.events.MultiTouchObjectEvent;

public class MoveBetweenContainerBehaviour implements IBehaviour, IMultiTouchEventListener {
	
	private final static Logger logger = Logger.getLogger(MoveBetweenContainerBehaviour.class.getName());	
	private IItem item;
	private boolean active;

	@Override
	public void setItemActingOn(IItem item) {
	}
	

	@Override
	public void setEventSource(IItem eventSourceItem) {		
		if(eventSourceItem == null) {
			item.getMultiTouchDispatcher().remove(this);
			return;
		}
		this.item = eventSourceItem;
		item.getMultiTouchDispatcher().addListener(this);		
	}


	@Override
	public void cursorChanged(MultiTouchCursorEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cursorClicked(MultiTouchCursorEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cursorPressed(MultiTouchCursorEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cursorReleased(MultiTouchCursorEvent event) {
		
		if(!active ) return;
		logger.info("cursor released caught event: "+item.getParentItem().getClass());
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

	@Override
	public void setStage(IStage stage) {
		// TODO Auto-generated method stub		
	}

	@Override
	public void setActive(boolean active) {
		this.active = active;
	}

}
