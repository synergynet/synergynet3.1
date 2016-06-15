package multiplicity3.csys.behaviours;

import multiplicity3.csys.items.item.IItem;
import multiplicity3.csys.stage.IStage;

/**
 * The Interface IBehaviour.
 */
public interface IBehaviour
{
	/**
	 * Set whether a behaviour executes events
	 *
	 * @param active
	 */
	public void setActive(boolean active);

	/**
	 * The item whose multi-touch event dispatcher will generate the events that
	 * this behaviour will respond to.
	 *
	 * @param eventSourceItem
	 */
	public void setEventSource(final IItem eventSourceItem);

	/**
	 * The item that the behaviour should operate on. The behaviourmaker will
	 * set this to be the same as the event source, but this method gives the
	 * option to route the actions elsewhere.
	 *
	 * @param item
	 */
	public void setItemActingOn(final IItem item);

	/**
	 * Behaviours usually need to know about the stage.
	 *
	 * @param stage
	 */
	public void setStage(IStage stage);

}
