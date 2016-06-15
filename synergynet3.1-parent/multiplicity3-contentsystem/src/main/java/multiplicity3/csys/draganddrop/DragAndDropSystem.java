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

/**
 * The Class DragAndDropSystem.
 */
public class DragAndDropSystem
{

	/** The Constant log. */
	private static final Logger log = Logger.getLogger(DragAndDropSystem.class.getName());

	/** The destination listeners. */
	private Map<UUID, List<DragAndDropListener>> destinationListeners = new HashMap<UUID, List<DragAndDropListener>>();

	/** The pick system. */
	private IPickSystem pickSystem;

	/** The stage. */
	private IStage stage;

	/** The workers. */
	private Map<UUID, ItemDragAndDropWorker> workers = new HashMap<UUID, ItemDragAndDropWorker>();

	/**
	 * Instantiates a new drag and drop system.
	 *
	 * @param stage
	 *            the stage
	 */
	public DragAndDropSystem(IStage stage)
	{
		this.stage = stage;
	}

	/**
	 * Drop occurred.
	 *
	 * @param event
	 *            the event
	 * @param itemDropped
	 *            the item dropped
	 */
	public void dropOccurred(MultiTouchCursorEvent event, IItem itemDropped)
	{
		log.info("Drop occurred: " + event);
		List<IItem> items = pickSystem.findItemsOnTableAtPosition(event.getPosition());
		int index = 0;
		for (IItem x : items)
		{
			List<DragAndDropListener> listeners = destinationListeners.get(x.getUUID());
			if (listeners != null)
			{
				for (DragAndDropListener listener : listeners)
				{
					listener.itemDraggedAndDropped(itemDropped, x, index);
				}
			}
			index++;
		}
	}

	/**
	 * Register drag destination listener.
	 *
	 * @param dragDestination
	 *            the drag destination
	 * @param listener
	 *            the listener
	 */
	public void registerDragDestinationListener(IItem dragDestination, DragAndDropListener listener)
	{
		log.info("Registered " + dragDestination + " as a destination for drag and drop system");
		List<DragAndDropListener> listeners = destinationListeners.get(dragDestination.getUUID());
		if (listeners == null)
		{
			listeners = new ArrayList<DragAndDropListener>();
			destinationListeners.put(dragDestination.getUUID(), listeners);
		}
		listeners.add(listener);
	}

	/**
	 * Register drag source.
	 *
	 * @param dragSource
	 *            the drag source
	 */
	public void registerDragSource(IItem dragSource)
	{
		log.info("Registered " + dragSource + " as a source for drag and drop system");
		log.info("UUID is " + dragSource.getUUID());
		workers.put(dragSource.getUUID(), new ItemDragAndDropWorker(stage, dragSource));
	}

	/**
	 * Sets the pick system for app.
	 *
	 * @param pickSystem
	 *            the new pick system for app
	 */
	public void setPickSystemForApp(IPickSystem pickSystem)
	{
		this.pickSystem = pickSystem;
	}

	/**
	 * Unregister drag source.
	 *
	 * @param dragSource
	 *            the drag source
	 */
	public void unregisterDragSource(IItem dragSource)
	{
		ItemDragAndDropWorker worker = workers.get(dragSource.getUUID());
		if (worker != null)
		{
			worker.stopWorking();
			workers.remove(dragSource.getUUID());
		}
	}

}
