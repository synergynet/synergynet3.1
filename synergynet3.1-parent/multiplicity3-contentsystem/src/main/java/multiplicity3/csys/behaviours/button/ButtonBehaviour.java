package multiplicity3.csys.behaviours.button;

import multiplicity3.csys.behaviours.IBehaviour;
import multiplicity3.csys.items.item.IItem;
import multiplicity3.csys.stage.IStage;
import multiplicity3.input.MultiTouchEventAdapter;
import multiplicity3.input.events.MultiTouchCursorEvent;

public class ButtonBehaviour implements IBehaviour {

	private IItem item;
	private boolean active = true;

	@Override
	public void setEventSource(IItem eventSourceItem) {
		this.item = eventSourceItem;		
	}
	
	public void setItemActingOn(final IItem item) {
		
	}

	public void addListener(final IButtonBehaviourListener l) {
		if(l != null) {
			final IButtonBehaviourListener bb = (IButtonBehaviourListener) l;
			item.getMultiTouchDispatcher().addListener(new MultiTouchEventAdapter() {
				@Override
				public void cursorReleased(MultiTouchCursorEvent event) {			
					if(!active ) return;
					bb.buttonReleased(item);				
				}

				@Override
				public void cursorPressed(MultiTouchCursorEvent event) {			
					if(!active ) return;
					bb.buttonPressed(item);
				}

				@Override
				public void cursorClicked(MultiTouchCursorEvent event) {			
					if(!active ) return;
					bb.buttonClicked(item);				
				}
			});
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
