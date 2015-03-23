package multiplicity3.jme3csys.picking;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import multiplicity3.csys.items.item.IItem;
import multiplicity3.csys.stage.IStage;
import multiplicity3.input.IMultiTouchEventListener;
import multiplicity3.input.events.MultiTouchCursorEvent;
import multiplicity3.input.events.MultiTouchObjectEvent;

import com.jme3.math.Vector2f;
import com.jme3.scene.Node;

/**
 * The Class PickedItemDispatcher.
 */
public class PickedItemDispatcher implements IMultiTouchEventListener {

	/** The Constant log. */
	private static final Logger log = Logger
			.getLogger(PickedItemDispatcher.class.getName());

	// TODO: enable 3d picking too
	// private Node threeDeePickRoot;
	/** The cursor item association. */
	private Map<Long, IItem> cursorItemAssociation = new HashMap<Long, IItem>();

	/** The stage. */
	private IStage stage;

	/** The loc store. */
	Vector2f locStore = new Vector2f();

	/**
	 * Instantiates a new picked item dispatcher.
	 *
	 * @param threeDeePickRoot the three dee pick root
	 * @param stage the stage
	 */
	public PickedItemDispatcher(Node threeDeePickRoot, IStage stage) {
		this.stage = stage;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.input.IMultiTouchEventListener#cursorChanged(multiplicity3
	 * .input.events.MultiTouchCursorEvent)
	 */
	@Override
	public void cursorChanged(MultiTouchCursorEvent event) {
		// 2D
		IItem item = getAssociatedItem(event.getCursorID());
		if (item != null) {
			item.getMultiTouchDispatcher().cursorChanged(event);
		}

	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.input.IMultiTouchEventListener#cursorClicked(multiplicity3
	 * .input.events.MultiTouchCursorEvent)
	 */
	@Override
	public void cursorClicked(MultiTouchCursorEvent event) {
		// 2D
		IItem item = getAssociatedItem(event.getCursorID());
		if (item != null) {
			item.getMultiTouchDispatcher().cursorClicked(event);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.input.IMultiTouchEventListener#cursorPressed(multiplicity3
	 * .input.events.MultiTouchCursorEvent)
	 */
	@Override
	public void cursorPressed(MultiTouchCursorEvent event) {
		log.fine("Attempting to pick from cursor event: " + event);

		// 2D
		List<IItem> items = getPickedItemListener(event.getPosition().x,
				event.getPosition().y);
		if ((items != null) && (items.size() > 0)) {
			// for(IItem item : items) {
			IItem item = items.get(0);
			associate(event.getCursorID(), item);
			item.getMultiTouchDispatcher().cursorPressed(event);
			// }
		}

	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.input.IMultiTouchEventListener#cursorReleased(multiplicity3
	 * .input.events.MultiTouchCursorEvent)
	 */
	@Override
	public void cursorReleased(MultiTouchCursorEvent event) {
		// 2D
		IItem item = getAssociatedItem(event.getCursorID());
		if (item != null) {
			item.getMultiTouchDispatcher().cursorReleased(event);
			disassociate(event.getCursorID());
		}

	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.input.IMultiTouchEventListener#objectAdded(multiplicity3
	 * .input.events.MultiTouchObjectEvent)
	 */
	@Override
	public void objectAdded(MultiTouchObjectEvent event) {
		IItem item = getAssociatedItem(event.getCursorID());
		if (item != null) {
			item.getMultiTouchDispatcher().objectAdded(event);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.input.IMultiTouchEventListener#objectChanged(multiplicity3
	 * .input.events.MultiTouchObjectEvent)
	 */
	@Override
	public void objectChanged(MultiTouchObjectEvent event) {
		IItem item = getAssociatedItem(event.getCursorID());
		if (item != null) {
			item.getMultiTouchDispatcher().objectChanged(event);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.input.IMultiTouchEventListener#objectRemoved(multiplicity3
	 * .input.events.MultiTouchObjectEvent)
	 */
	@Override
	public void objectRemoved(MultiTouchObjectEvent event) {
		IItem item = getAssociatedItem(event.getCursorID());
		if (item != null) {
			item.getMultiTouchDispatcher().objectRemoved(event);
		}
	}

	/**
	 * Associate.
	 *
	 * @param cursorID the cursor id
	 * @param item the item
	 */
	private void associate(long cursorID, IItem item) {
		cursorItemAssociation.put(cursorID, item);
	}

	/**
	 * Disassociate.
	 *
	 * @param cursorID the cursor id
	 */
	private void disassociate(long cursorID) {
		cursorItemAssociation.remove(cursorID);
	}

	/**
	 * Gets the associated item.
	 *
	 * @param cursorID the cursor id
	 * @return the associated item
	 */
	private IItem getAssociatedItem(long cursorID) {
		return cursorItemAssociation.get(cursorID);
	}

	/**
	 * Gets the picked item listener.
	 *
	 * @param x the x
	 * @param y the y
	 * @return the picked item listener
	 */
	protected List<IItem> getPickedItemListener(float x, float y) {
		return stage.getPickSystem().findItemsOnTableAtPosition(
				new Vector2f(x, y));
	}

}
