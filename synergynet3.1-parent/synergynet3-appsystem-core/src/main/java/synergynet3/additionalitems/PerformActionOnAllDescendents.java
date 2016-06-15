package synergynet3.additionalitems;

import java.util.Collections;
import java.util.List;

import multiplicity3.csys.items.item.IItem;

/**
 * The Class PerformActionOnAllDescendents.
 */
public class PerformActionOnAllDescendents
{

	/** The stop. */
	protected boolean stop = false;

	/**
	 * Instantiates a new perform action on all descendents.
	 *
	 * @param item
	 *            the item
	 * @param breadth
	 *            the breadth
	 * @param reverse
	 *            the reverse
	 */
	public PerformActionOnAllDescendents(IItem item, boolean breadth, boolean reverse)
	{
		if (breadth)
		{
			if (!reverse)
			{
				action(item);
				cycleThroughDescendentsBreadth(item);
			}
			else
			{
				action(item);
				cycleThroughDescendentsBreadthReverse(item);
			}
		}
		else
		{
			if (!reverse)
			{
				cycleThroughDescendentsDepth(item);
			}
			else
			{
				cycleThroughDescendentsDepthReverse(item);
			}
		}
		onFinish();
	}

	/**
	 * Action.
	 *
	 * @param child
	 *            the child
	 */
	private void action(IItem child)
	{
		if (!stop)
		{
			actionOnDescendent(child);
		}
	}

	/**
	 * Cycle through descendents breadth.
	 *
	 * @param item
	 *            the item
	 */
	private void cycleThroughDescendentsBreadth(IItem item)
	{
		for (IItem descendent : item.getChildItems())
		{
			action(descendent);
		}
		for (IItem descendent : item.getChildItems())
		{
			cycleThroughDescendentsBreadth(descendent);
		}
	}

	/**
	 * Cycle through descendents breadth reverse.
	 *
	 * @param item
	 *            the item
	 */
	private void cycleThroughDescendentsBreadthReverse(IItem item)
	{
		List<IItem> reverseList = item.getChildItems();
		Collections.reverse(reverseList);
		for (IItem descendent : reverseList)
		{
			action(descendent);
		}
		for (IItem descendent : reverseList)
		{
			cycleThroughDescendentsBreadthReverse(descendent);
		}
	}

	/**
	 * Cycle through descendents depth.
	 *
	 * @param item
	 *            the item
	 */
	private void cycleThroughDescendentsDepth(IItem item)
	{
		for (IItem descendent : item.getChildItems())
		{
			cycleThroughDescendentsDepth(descendent);
		}
		action(item);
	}

	/**
	 * Cycle through descendents depth reverse.
	 *
	 * @param item
	 *            the item
	 */
	private void cycleThroughDescendentsDepthReverse(IItem item)
	{
		action(item);
		for (IItem descendent : item.getChildItems())
		{
			cycleThroughDescendentsDepthReverse(descendent);
		}
	}

	/**
	 * Action on descendent.
	 *
	 * @param child
	 *            the child
	 */
	protected void actionOnDescendent(IItem child)
	{
	}

	/**
	 * On finish.
	 */
	protected void onFinish()
	{
	}

}
