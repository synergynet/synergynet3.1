package synergynet3.behaviours.networkflick;

import multiplicity3.csys.animation.AnimationSystem;
import multiplicity3.csys.behaviours.IBehaviour;
import multiplicity3.csys.behaviours.inertia.ItemPositionHistory;
import multiplicity3.csys.items.item.IItem;
import multiplicity3.csys.stage.IStage;
import multiplicity3.input.IMultiTouchEventListener;
import multiplicity3.input.events.MultiTouchCursorEvent;
import multiplicity3.input.events.MultiTouchObjectEvent;
import synergynet3.behaviours.BehaviourUtilities.RelativePosition;

import com.jme3.math.Vector2f;

/**
 * Behaviour for items which are intended to be capable of being flicked across
 * the network. Note that with this behaviour an item does not need
 * inertiaBehaviour, inertia is supplied by this class.
 */
public class NetworkFlickBehaviour implements IBehaviour, IMultiTouchEventListener
{

	/**
	 * Determines if the item will respond to user touches.
	 */
	private boolean active = true;

	/**
	 * The number of current cursors on the instigating item.
	 */
	private int cursorCount = 0;

	/**
	 * Represents the dimensions of the local display in pixels.
	 */
	private Vector2f displayDimensions = null;

	/**
	 * Item instigating the events this listener responds to.
	 */
	private IItem eventSource;

	/**
	 * Repositions an item when flick using the JME animation system.
	 */
	private NetworkFlickAnimationElement iae;

	/**
	 * Collection of positions at which the item has currently been positioned
	 * when moved by a user.
	 */
	private ItemPositionHistory positionHistory;

	/**
	 * The environment the instigating item belongs to.
	 */
	protected IStage stage;

	/**
	 * Causes item to bounce.
	 *
	 * @param vX
	 *            X component of the velocity the item that the managed item is
	 *            bouncing off.
	 * @param vY
	 *            Y component of the velocity the item that the managed item is
	 *            bouncing off.
	 * @param rp
	 */
	public void bounce(float vX, float vY, float tpf, RelativePosition bouncePosition)
	{
		iae.bounce(vX, vY, tpf, bouncePosition);
	}

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
		if (displayDimensions == null)
		{
			displayDimensions = new Vector2f(stage.getDisplayWidth(), stage.getDisplayHeight());
		}
		float x = this.eventSource.getWorldLocation().x / displayDimensions.x;
		float y = this.eventSource.getWorldLocation().y / displayDimensions.y;
		positionHistory.add(new Vector2f(x, y), System.currentTimeMillis());
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
		if (!active)
		{
			return;
		}
		iae.reset();
		positionHistory.clear();

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
		cursorCount++;
		iae.reset();
		positionHistory.clear();
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
		if (!active)
		{
			return;
		}
		if (cursorCount == 1)
		{
			positionHistory.add(event.getPosition(), System.currentTimeMillis());
			iae.moveWithVelocity(positionHistory.getVelocity());
			AnimationSystem.getInstance().add(iae);
			positionHistory.clear();
		}
		cursorCount--;
	}

	/**
	 * Flicks the item without the need for initiation from a user gesture.
	 *
	 * @param newVelocity
	 *            Direction (and force) the item should be flicked in.
	 */
	public void flick(Vector2f newVelocity)
	{
		iae.moveWithVelocity(newVelocity);
		AnimationSystem.getInstance().add(iae);
		positionHistory.clear();
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

	/**
	 * Halts the item's current movement if travelling under its own momentum.
	 */
	public void reset()
	{
		iae.reset();
		positionHistory.clear();
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

	/**
	 * Sets the deceleration at which the managed item's momentum is reduced.
	 *
	 * @param drag
	 *            The deceleration at which the managed item's momentum is
	 *            reduced.
	 */
	public void setDeceleration(float deceleration)
	{
		iae.setDeceleration(deceleration);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.csys.behaviours.IBehaviour#setEventSource(multiplicity3
	 * .csys.items.item.IItem)
	 */
	@Override
	public void setEventSource(IItem eventSourceItem)
	{
		if ((eventSourceItem == null) && (eventSource != null))
		{
			eventSource.getMultiTouchDispatcher().remove(this);
		}

		eventSource = eventSourceItem;
		eventSource.getMultiTouchDispatcher().addListener(this);
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
		iae = new NetworkFlickAnimationElement(item, stage);
		positionHistory = new ItemPositionHistory(item);
		positionHistory.setStage(stage);
	}

	/**
	 * Sets the predetermined max dimension of the managed item.
	 *
	 * @param maxDim
	 *            The predetermined max dimension of the managed item
	 */
	public void setMaxDimension(float maxDimension)
	{
		iae.setMaxDimension(maxDimension);
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
		if (positionHistory != null)
		{
			positionHistory.setStage(stage);
		}
	}

}
