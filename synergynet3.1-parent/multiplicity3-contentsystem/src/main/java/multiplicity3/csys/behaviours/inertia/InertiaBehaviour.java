package multiplicity3.csys.behaviours.inertia;

import multiplicity3.csys.animation.AnimationSystem;
import multiplicity3.csys.animation.elements.behaviourelements.InertiaAnimationElement;
import multiplicity3.csys.behaviours.IBehaviour;
import multiplicity3.csys.items.item.IItem;
import multiplicity3.csys.stage.IStage;
import multiplicity3.input.IMultiTouchEventListener;
import multiplicity3.input.events.MultiTouchCursorEvent;
import multiplicity3.input.events.MultiTouchObjectEvent;

public class InertiaBehaviour implements IBehaviour, IMultiTouchEventListener {
	
	ItemPositionHistory positionHistory;
	InertiaAnimationElement iae;
	
	int cursorCount = 0;
	private IItem eventSource;
	protected IStage stage;
	private boolean active = true;
	
	@Override
	public void setEventSource(IItem eventSourceItem) {
		if(eventSourceItem == null && eventSource != null) {
			eventSource.getMultiTouchDispatcher().remove(this);
		}
		
		eventSource = eventSourceItem;
		eventSource.getMultiTouchDispatcher().addListener(this);
	}

	@Override
	public void setItemActingOn(IItem item) {
		iae = new InertiaAnimationElement(item, stage);
		positionHistory = new ItemPositionHistory(item);	
		positionHistory.setStage(stage);
	}

	@Override
	public void cursorChanged(MultiTouchCursorEvent event) {				
		if(!active ) return;	
		if(cursorCount == 1) {
			positionHistory.add(event.getPosition(), System.currentTimeMillis());
		}
	}

	@Override
	public void cursorClicked(MultiTouchCursorEvent event) {			
		if(!active ) return;
		iae.reset();
		positionHistory.clear();
		
	}

	@Override
	public void cursorPressed(MultiTouchCursorEvent event) {			
		if(!active ) return;
		cursorCount++;		
		iae.reset();
		positionHistory.clear();
	}

	@Override
	public void cursorReleased(MultiTouchCursorEvent event) {			
		if(!active ) return;
		if(cursorCount == 1) {
			positionHistory.add(event.getPosition(), System.currentTimeMillis());			
			iae.moveWithVelocity(positionHistory.getVelocity());
			AnimationSystem.getInstance().add(iae);
			positionHistory.clear();
		}
		cursorCount--;
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
		if (positionHistory != null) positionHistory.setStage(stage);
	}


	public void setDeceleration(float deceleration){
		iae.setDeceleration(deceleration);
	}

	@Override
	public void setActive(boolean active) {
		this.active = active;
	}

}
