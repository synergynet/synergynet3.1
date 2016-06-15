package multiplicity3.csys.behaviours.gesture;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import multiplicity3.csys.behaviours.IBehaviour;
import multiplicity3.csys.items.item.IItem;
import multiplicity3.csys.stage.IStage;
import multiplicity3.input.IMultiTouchEventListener;
import multiplicity3.input.events.MultiTouchCursorEvent;
import multiplicity3.input.events.MultiTouchObjectEvent;

/**
 * The Class GestureDetectionBehaviour.
 */
public class GestureDetectionBehaviour implements IBehaviour, IMultiTouchEventListener
{

	/** The active. */
	private boolean active = true;

	/** The current gestures. */
	private Map<Long, Gesture> currentGestures = new HashMap<Long, Gesture>();

	/** The item. */
	private IItem item;

	/** The listeners. */
	private List<IGestureListener> listeners = new ArrayList<IGestureListener>();

	// @Override
	/**
	 * Adds the listener.
	 *
	 * @param l
	 *            the l
	 */
	public void addListener(IGestureListener l)
	{
		listeners.add(l);
		item.getMultiTouchDispatcher().addListener(this);
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
		Gesture g = currentGestures.get(event.getCursorID());
		if (g != null)
		{
			g.addPoint(event.getPosition());
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
		// TODO Auto-generated method stub

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
		Gesture g = new Gesture("c" + event.getCursorID());
		g.addPoint(event.getPosition());
		currentGestures.put(event.getCursorID(), g);
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
		Gesture g = currentGestures.get(event.getCursorID());
		if (g != null)
		{
			g.addPoint(event.getPosition());
		}
		detectGesture(g, event);
		currentGestures.remove(event.getCursorID());
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
		// TODO Auto-generated method stub

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
		// TODO Auto-generated method stub

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
		// TODO Auto-generated method stub

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
	public void setItemActingOn(IItem item)
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

	/**
	 * Detect gesture.
	 *
	 * @param g
	 *            the g
	 * @param event
	 *            the event
	 */
	private void detectGesture(Gesture g, MultiTouchCursorEvent event)
	{
		GestureMatch match = GestureLibrary.getInstance().findGestureMatch(g, 0.1f);
		if (match != null)
		{
			for (IGestureListener l : listeners)
			{
				l.gestureDetected(match, item);
			}
		}
	}

}
