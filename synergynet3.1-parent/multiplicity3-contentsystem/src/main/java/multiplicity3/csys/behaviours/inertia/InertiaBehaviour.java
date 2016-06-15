package multiplicity3.csys.behaviours.inertia;

import multiplicity3.csys.animation.AnimationSystem;
import multiplicity3.csys.animation.elements.behaviourelements.InertiaAnimationElement;
import multiplicity3.csys.behaviours.IBehaviour;
import multiplicity3.csys.items.item.IItem;
import multiplicity3.csys.stage.IStage;
import multiplicity3.input.IMultiTouchEventListener;
import multiplicity3.input.events.MultiTouchCursorEvent;
import multiplicity3.input.events.MultiTouchObjectEvent;

/**
 * The Class InertiaBehaviour.
 */
public class InertiaBehaviour implements IBehaviour, IMultiTouchEventListener
{

	/** The active. */
	private boolean active = true;

	/** The event source. */
	private IItem eventSource;

	/** The stage. */
	protected IStage stage;

	/** The cursor count. */
	int cursorCount = 0;

	/** The iae. */
	InertiaAnimationElement iae;

	/** The position history. */
	ItemPositionHistory positionHistory;

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
		if (cursorCount == 1)
		{
			positionHistory.add(event.getPosition(), System.currentTimeMillis());
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

	/**
	 * Sets the deceleration.
	 *
	 * @param deceleration
	 *            the new deceleration
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
		iae = new InertiaAnimationElement(item, stage);
		positionHistory = new ItemPositionHistory(item);
		positionHistory.setStage(stage);
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
