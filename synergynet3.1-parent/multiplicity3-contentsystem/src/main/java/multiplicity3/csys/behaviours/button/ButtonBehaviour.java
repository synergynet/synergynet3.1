package multiplicity3.csys.behaviours.button;

import multiplicity3.csys.behaviours.IBehaviour;
import multiplicity3.csys.items.item.IItem;
import multiplicity3.csys.stage.IStage;
import multiplicity3.input.MultiTouchEventAdapter;
import multiplicity3.input.events.MultiTouchCursorEvent;

/**
 * The Class ButtonBehaviour.
 */
public class ButtonBehaviour implements IBehaviour
{

	/** The active. */
	private boolean active = true;

	/** The item. */
	private IItem item;

	/**
	 * Adds the listener.
	 *
	 * @param l
	 *            the l
	 */
	public void addListener(final IButtonBehaviourListener l)
	{
		if (l != null)
		{
			final IButtonBehaviourListener bb = l;
			item.getMultiTouchDispatcher().addListener(new MultiTouchEventAdapter()
			{
				@Override
				public void cursorClicked(MultiTouchCursorEvent event)
				{
					if (!active)
					{
						return;
					}
					bb.buttonClicked(item);
				}

				@Override
				public void cursorPressed(MultiTouchCursorEvent event)
				{
					if (!active)
					{
						return;
					}
					bb.buttonPressed(item);
				}

				@Override
				public void cursorReleased(MultiTouchCursorEvent event)
				{
					if (!active)
					{
						return;
					}
					bb.buttonReleased(item);
				}
			});
		}
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
	public void setEventSource(IItem eventSourceItem)
	{
		this.item = eventSourceItem;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.csys.behaviours.IBehaviour#setItemActingOn(multiplicity3
	 * .csys.items.item.IItem)
	 */
	@Override
	public void setItemActingOn(final IItem item)
	{

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
		// TODO Auto-generated method stub

	}

}
