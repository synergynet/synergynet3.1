package multiplicity3.csys.items.events;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import multiplicity3.input.IMultiTouchEventListener;
import multiplicity3.input.events.MultiTouchCursorEvent;
import multiplicity3.input.events.MultiTouchObjectEvent;

/**
 * The Class MultiTouchEventDispatcher.
 */
public class MultiTouchEventDispatcher implements IMultiTouchEventListener
{

	/** The enabled. */
	private boolean enabled = true;

	/** The items. */
	protected List<IMultiTouchEventListener> items = new CopyOnWriteArrayList<IMultiTouchEventListener>();

	/**
	 * Adds the listener.
	 *
	 * @param listener
	 *            the listener
	 */
	public void addListener(IMultiTouchEventListener listener)
	{
		if ((listener != null) && !items.contains(listener))
		{
			items.add(listener);
		}
	}

	/**
	 * Adds the listeners.
	 *
	 * @param listeners
	 *            the listeners
	 */
	public void addListeners(List<IMultiTouchEventListener> listeners)
	{
		for (IMultiTouchEventListener l : listeners)
		{
			addListener(l);
		}
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
		if (!enabled)
		{
			return;
		}
		for (IMultiTouchEventListener item : items)
		{
			item.cursorChanged(event);
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
		if (!enabled)
		{
			return;
		}
		for (IMultiTouchEventListener item : items)
		{
			item.cursorClicked(event);
		}
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
		if (!enabled)
		{
			return;
		}
		for (IMultiTouchEventListener item : items)
		{
			item.cursorPressed(event);
		}
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
		if (!enabled)
		{
			return;
		}
		for (IMultiTouchEventListener item : items)
		{
			item.cursorReleased(event);
		}
	}

	/**
	 * Gets the listeners.
	 *
	 * @return the listeners
	 */
	public List<IMultiTouchEventListener> getListeners()
	{
		return items;
	}

	/**
	 * Checks if is enabled.
	 *
	 * @return true, if is enabled
	 */
	public boolean isEnabled()
	{
		return enabled;
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
		if (!enabled)
		{
			return;
		}
		for (IMultiTouchEventListener item : items)
		{
			item.objectAdded(event);
		}
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
		if (!enabled)
		{
			return;
		}
		for (IMultiTouchEventListener item : items)
		{
			item.objectChanged(event);
		}
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
		if (!enabled)
		{
			return;
		}
		for (IMultiTouchEventListener item : items)
		{
			item.objectRemoved(event);
		}
	}

	/**
	 * Removes the.
	 *
	 * @param listener
	 *            the listener
	 */
	public void remove(IMultiTouchEventListener listener)
	{
		items.remove(listener);
	}

	/**
	 * Removes the listeners.
	 *
	 * @param listeners
	 *            the listeners
	 */
	public void removeListeners(List<IMultiTouchEventListener> listeners)
	{
		for (IMultiTouchEventListener l : listeners)
		{
			remove(l);
		}
	}

	/**
	 * Sets the enabled.
	 *
	 * @param b
	 *            the new enabled
	 */
	public void setEnabled(boolean b)
	{
		this.enabled = b;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return MultiTouchEventDispatcher.class.getName() + " containing " + items;
	}
}
