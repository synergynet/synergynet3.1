package multiplicity3.csys.draganddrop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

import multiplicity3.csys.items.item.IItem;
import multiplicity3.csys.picksystem.IPickSystem;
import multiplicity3.csys.stage.IStage;
import multiplicity3.input.events.MultiTouchCursorEvent;

public class DragAndDropSystem {
	private static final Logger log = Logger.getLogger(DragAndDropSystem.class.getName());
	
	private IPickSystem pickSystem;
	private Map<UUID,ItemDragAndDropWorker> workers = new HashMap<UUID,ItemDragAndDropWorker>();
	private Map<UUID,List<DragAndDropListener>> destinationListeners = new HashMap<UUID,List<DragAndDropListener>>();
	private IStage stage;
	
	public DragAndDropSystem(IStage stage) {
		this.stage = stage;
	}
	
	public void registerDragSource(IItem dragSource) {
		log.info("Registered " + dragSource + " as a source for drag and drop system");
		log.info("UUID is " + dragSource.getUUID());
		workers.put(dragSource.getUUID(), new ItemDragAndDropWorker(stage, dragSource));
	}
	
	public void unregisterDragSource(IItem dragSource) {
		ItemDragAndDropWorker worker = workers.get(dragSource.getUUID());
		if(worker != null) {
			worker.stopWorking();
			workers.remove(dragSource.getUUID());
		}
	}
	
	public void registerDragDestinationListener(IItem dragDestination, DragAndDropListener listener) {
		log.info("Registered " + dragDestination + " as a destination for drag and drop system");
		List<DragAndDropListener> listeners = destinationListeners.get(dragDestination.getUUID());
		if(listeners == null) {
			listeners = new ArrayList<DragAndDropListener>();
			destinationListeners.put(dragDestination.getUUID(), listeners);
		}
		listeners.add(listener);
	}

	public void dropOccurred(MultiTouchCursorEvent event, IItem itemDropped) {
		log.info("Drop occurred: " + event);
		List<IItem> items = pickSystem.findItemsOnTableAtPosition(event.getPosition());
		int index = 0;
		for(IItem x : items) {
			List<DragAndDropListener> listeners = destinationListeners.get(x.getUUID());
			if(listeners != null) {				
				for(DragAndDropListener listener : listeners) {
					listener.itemDraggedAndDropped(itemDropped, x, index);
				}
			}
			index++;
		}	
	}

	public void setPickSystemForApp(IPickSystem pickSystem) {
		this.pickSystem = pickSystem;		
	}


}
