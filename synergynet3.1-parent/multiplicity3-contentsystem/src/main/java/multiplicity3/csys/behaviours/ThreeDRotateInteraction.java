package multiplicity3.csys.behaviours;

import java.util.logging.Logger;

import multiplicity3.csys.items.item.IItem;
import multiplicity3.csys.stage.IStage;
import multiplicity3.input.IMultiTouchEventListener;
import multiplicity3.input.events.MultiTouchCursorEvent;
import multiplicity3.input.events.MultiTouchObjectEvent;

import com.jme3.bounding.BoundingBox;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;

/**
 * The Class ThreeDRotateInteraction.
 */
public class ThreeDRotateInteraction implements IBehaviour, IMultiTouchEventListener
{

	/** The Constant log. */
	private static final Logger log = Logger.getLogger(ThreeDRotateInteraction.class.getName());

	/** The active. */
	private boolean active = true;

	/** The affected item. */
	private IItem affectedItem;

	/** The current cursor id. */
	private long currentCursorID;

	/** The cursor pressed position. */
	private Vector2f cursorPressedPosition;

	/** The event source item. */
	private IItem eventSourceItem;

	/** The parent item. */
	private IItem parentItem;

	/** The stage. */
	private IStage stage;

	/** The max dim. */
	private float width, height, depth, maxDim = 0;

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.input.IMultiTouchEventListener#cursorChanged(multiplicity3
	 * .input.events.MultiTouchCursorEvent)
	 */
	@Override
	public void cursorChanged(MultiTouchCursorEvent event)
	{
		if (!active)
		{
			return;
		}
		if (currentCursorID == event.getCursorID())
		{
			Vector2f newPos = event.getPosition();
			newPos = stage.tableToWorld(newPos);
			newPos = newPos.subtract(parentItem.getWorldLocation());
			newPos.rotateAroundOrigin(-parentItem.getRelativeRotation(), false);
			if (newPos.length() < (maxDim * parentItem.getRelativeScale()))
			{
				Vector2f change = newPos.subtract(cursorPressedPosition);
				float xChange = (((-change.y) / (height * parentItem.getRelativeScale())) / (360 * FastMath.DEG_TO_RAD));
				float yChange = (((change.x) / (width * parentItem.getRelativeScale())) / (360 * FastMath.DEG_TO_RAD));

				float[] angles =
				{ xChange, yChange, 0 };
				Quaternion q = new Quaternion();
				q.fromAngles(angles);
				q.multLocal(affectedItem.getManipulableSpatial().getLocalRotation());
				affectedItem.getManipulableSpatial().setLocalRotation(q);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.input.IMultiTouchEventListener#cursorClicked(multiplicity3
	 * .input.events.MultiTouchCursorEvent)
	 */
	@Override
	public void cursorClicked(MultiTouchCursorEvent event)
	{
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.input.IMultiTouchEventListener#cursorPressed(multiplicity3
	 * .input.events.MultiTouchCursorEvent)
	 */
	@Override
	public void cursorPressed(MultiTouchCursorEvent event)
	{
		if (!active)
		{
			return;
		}
		this.currentCursorID = event.getCursorID();
		this.cursorPressedPosition = event.getPosition();
		cursorPressedPosition = stage.tableToWorld(cursorPressedPosition);
		cursorPressedPosition = cursorPressedPosition.subtract(parentItem.getWorldLocation());
		cursorPressedPosition.rotateAroundOrigin(-parentItem.getRelativeRotation(), false);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.input.IMultiTouchEventListener#cursorReleased(multiplicity3
	 * .input.events.MultiTouchCursorEvent)
	 */
	@Override
	public void cursorReleased(MultiTouchCursorEvent event)
	{
		this.currentCursorID = -1;
	}

	/**
	 * Gets the parent item.
	 *
	 * @return the parent item
	 */
	public IItem getParentItem()
	{
		return parentItem;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.input.IMultiTouchEventListener#objectAdded(multiplicity3
	 * .input.events.MultiTouchObjectEvent)
	 */
	@Override
	public void objectAdded(MultiTouchObjectEvent event)
	{
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.input.IMultiTouchEventListener#objectChanged(multiplicity3
	 * .input.events.MultiTouchObjectEvent)
	 */
	@Override
	public void objectChanged(MultiTouchObjectEvent event)
	{
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.input.IMultiTouchEventListener#objectRemoved(multiplicity3
	 * .input.events.MultiTouchObjectEvent)
	 */
	@Override
	public void objectRemoved(MultiTouchObjectEvent event)
	{
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.csys.behaviours.IBehaviour#setActive(boolean)
	 */
	@Override
	public void setActive(boolean active)
	{
		this.active = active;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.csys.behaviours.IBehaviour#setEventSource(multiplicity3
	 * .csys.items.item.IItem)
	 */
	@Override
	public void setEventSource(IItem newSourceItem)
	{
		if (newSourceItem == eventSourceItem)
		{
			// no change
			return;
		}

		// already have an event source, so unregister it
		if (this.eventSourceItem != null)
		{
			this.eventSourceItem.getMultiTouchDispatcher().remove(this);
		}

		eventSourceItem = newSourceItem;
		eventSourceItem.getMultiTouchDispatcher().addListener(this);

		width = ((BoundingBox) eventSourceItem.getManipulableSpatial().getWorldBound()).getXExtent();
		height = ((BoundingBox) eventSourceItem.getManipulableSpatial().getWorldBound()).getYExtent();
		depth = ((BoundingBox) eventSourceItem.getManipulableSpatial().getWorldBound()).getZExtent();

		maxDim = width;
		if (maxDim < height)
		{
			maxDim = height;
		}
		if (maxDim < depth)
		{
			maxDim = depth;
		}
		maxDim /= 2;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.csys.behaviours.IBehaviour#setItemActingOn(multiplicity3
	 * .csys.items.item.IItem)
	 */
	@Override
	public void setItemActingOn(IItem item)
	{
		log.fine("Adding 3D rotation behaviour to " + item);
		this.affectedItem = item;
	}

	/**
	 * Sets the parent item.
	 *
	 * @param parentItem
	 *            the new parent item
	 */
	public void setParentItem(IItem parentItem)
	{
		this.parentItem = parentItem;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.csys.behaviours.IBehaviour#setStage(multiplicity3.csys.
	 * stage.IStage)
	 */
	@Override
	public void setStage(IStage stage)
	{
		this.stage = stage;
	}

}
