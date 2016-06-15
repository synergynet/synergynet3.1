package multiplicity3.csys.zorder;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import multiplicity3.csys.items.item.IItem;
import multiplicity3.input.events.MultiTouchCursorEvent;

/**
 * The Class ZOrderManager.
 */
public class ZOrderManager implements IZOrderManager
{

	/** The Constant log. */
	private static final Logger log = Logger.getLogger(ZOrderManager.class.getName());

	/** The auto bring to top. */
	private boolean autoBringToTop = true;

	/** The bring to top propagates up. */
	private boolean bringToTopPropagatesUp = true;

	/** The capacity. */
	private int capacity = 1;

	/** The item being managed. */
	private IItem itemBeingManaged;

	/** The registered items. */
	protected List<IItem> registeredItems = new ArrayList<IItem>();

	/** The start z order. */
	protected int startZOrder = 1000;

	/** The used z space. */
	protected int usedZSpace = 1;

	/**
	 * Instantiates a new z order manager.
	 *
	 * @param itemBeingManaged
	 *            the item being managed
	 * @param initialCapacity
	 *            the initial capacity
	 */
	public ZOrderManager(IItem itemBeingManaged, int initialCapacity)
	{
		capacity = initialCapacity;
		this.itemBeingManaged = itemBeingManaged;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.csys.zorder.IZOrderManager#bringToTop(multiplicity3.csys
	 * .items.item.IItem)
	 */
	@Override
	public void bringToTop(IItem item)
	{
		registeredItems.remove(item);
		registeredItems.add(0, item);
		updateOrder();
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.csys.zorder.IZOrderManager#getItemZOrder()
	 */
	@Override
	public int getItemZOrder()
	{
		return this.startZOrder;
	}

	/**
	 * Gets the parent z order manager.
	 *
	 * @return the parent z order manager
	 */
	public IZOrderManager getParentZOrderManager()
	{
		if ((itemBeingManaged != null) && (itemBeingManaged.getParentItem() != null))
		{
			return itemBeingManaged.getParentItem().getZOrderManager();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.csys.zorder.IZOrderManager#getZCapacity()
	 */
	@Override
	public int getZCapacity()
	{
		return capacity;
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.csys.zorder.IZOrderManager#ignoreItemClickedBehaviour(
	 * multiplicity3.csys.items.item.IItem)
	 */
	@Override
	public void ignoreItemClickedBehaviour(IItem item)
	{
		item.removeItemListener(this);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.csys.items.events.IItemListener#itemCursorChanged(multiplicity3
	 * .csys.items.item.IItem, multiplicity3.input.events.MultiTouchCursorEvent)
	 */
	@Override
	public void itemCursorChanged(IItem item, MultiTouchCursorEvent event)
	{
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.csys.items.events.IItemListener#itemCursorClicked(multiplicity3
	 * .csys.items.item.IItem, multiplicity3.input.events.MultiTouchCursorEvent)
	 */
	@Override
	public void itemCursorClicked(IItem item, MultiTouchCursorEvent event)
	{
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.csys.items.events.IItemListener#itemCursorPressed(multiplicity3
	 * .csys.items.item.IItem, multiplicity3.input.events.MultiTouchCursorEvent)
	 */
	@Override
	public void itemCursorPressed(IItem item, MultiTouchCursorEvent event)
	{
		if (autoBringToTop)
		{
			log.fine("Bringing " + item + " to the top");
			bringToTop(item);
		}
		if (bringToTopPropagatesUp && (getParentZOrderManager() != null))
		{
			getParentZOrderManager().bringToTop(item.getParentItem());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.csys.items.events.IItemListener#itemCursorReleased(
	 * multiplicity3.csys.items.item.IItem,
	 * multiplicity3.input.events.MultiTouchCursorEvent)
	 */
	@Override
	public void itemCursorReleased(IItem item, MultiTouchCursorEvent event)
	{
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.csys.items.events.IItemListener#itemMoved(multiplicity3
	 * .csys.items.item.IItem)
	 */
	@Override
	public void itemMoved(IItem item)
	{
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.csys.items.events.IItemListener#itemRotated(multiplicity3
	 * .csys.items.item.IItem)
	 */
	@Override
	public void itemRotated(IItem item)
	{
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.csys.items.events.IItemListener#itemScaled(multiplicity3
	 * .csys.items.item.IItem)
	 */
	@Override
	public void itemScaled(IItem item)
	{
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.csys.items.events.IItemListener#itemVisibilityChanged(
	 * multiplicity3.csys.items.item.IItem, boolean)
	 */
	@Override
	public void itemVisibilityChanged(IItem item, boolean isVisible)
	{
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.csys.items.events.IItemListener#itemZOrderChanged(multiplicity3
	 * .csys.items.item.IItem)
	 */
	@Override
	public void itemZOrderChanged(IItem item)
	{
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.csys.zorder.IZOrderManager#notifyChildZCapacityChanged(
	 * multiplicity3.csys.items.item.IItem,
	 * multiplicity3.csys.zorder.IZOrderManager)
	 */
	@Override
	public void notifyChildZCapacityChanged(IItem item, IZOrderManager manager)
	{
		log.fine(this.itemBeingManaged + " has item that changed capacity: " + item);
		int zReq = capacity;
		for (IItem i : registeredItems)
		{
			zReq += i.getZOrderManager().getZCapacity();
		}
		this.usedZSpace = zReq;
		if (usedZSpace > capacity)
		{
			doubleZSpaceCapacity();
		}
		updateOrder();
	}

	// ****** unused ********

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.csys.zorder.IZOrderManager#registerForZOrdering(multiplicity3
	 * .csys.items.item.IItem)
	 */
	@Override
	public void registerForZOrdering(IItem item)
	{
		if (!registeredItems.contains(item))
		{
			registeredItems.add(0, item);
			item.getZOrderManager().setItemZOrder(usedZSpace);
			usedZSpace += item.getZOrderManager().getZCapacity();
			if (usedZSpace > capacity)
			{
				doubleZSpaceCapacity();
			}
			item.addItemListener(this);
		}
		updateOrder();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.csys.zorder.IZOrderManager#sendToBottom(multiplicity3.csys
	 * .items.item.IItem)
	 */
	@Override
	public void sendToBottom(IItem item)
	{
		registeredItems.remove(item);
		registeredItems.add(item);
		updateOrder();
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.csys.zorder.IZOrderManager#setAutoBringToTop(boolean)
	 */
	@Override
	public void setAutoBringToTop(boolean enabled)
	{
		this.autoBringToTop = enabled;
		updateOrder();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.csys.zorder.IZOrderManager#setBringToTopPropagatesUp(boolean
	 * )
	 */
	@Override
	public void setBringToTopPropagatesUp(boolean should)
	{
		this.bringToTopPropagatesUp = should;
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.csys.zorder.IZOrderManager#setItemZOrder(int)
	 */
	@Override
	public void setItemZOrder(int zValue)
	{
		this.startZOrder = zValue;
		updateOrder();
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.csys.zorder.IZOrderManager#setZCapacity(int)
	 */
	@Override
	public void setZCapacity(int c)
	{
		log.fine(this.itemBeingManaged + " setCapacity " + c);
		if (c > capacity)
		{
			this.capacity = c;
			log.fine(this.itemBeingManaged + " now has z capacity of " + this.capacity);
			informParentThatCapacityChanged();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.csys.zorder.IZOrderManager#unregisterForZOrdering(multiplicity3
	 * .csys.items.item.IItem)
	 */
	@Override
	public void unregisterForZOrdering(IItem item)
	{
		if (registeredItems.contains(item))
		{
			registeredItems.remove(item);
			usedZSpace -= item.getZOrderManager().getZCapacity();
			item.removeItemListener(this);
		}
		updateOrder();
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.csys.zorder.IZOrderManager#updateOrder()
	 */
	@Override
	public void updateOrder()
	{
		if (itemBeingManaged != null)
		{
			itemBeingManaged.setZOrder(startZOrder);
		}
		int z = startZOrder;
		for (IItem i : registeredItems)
		{
			i.getZOrderManager().setItemZOrder(z);
			z -= i.getZOrderManager().getZCapacity();
		}
	}

	// ****** private methods *****

	/**
	 * Double z space capacity.
	 */
	private void doubleZSpaceCapacity()
	{
		setZCapacity(getZCapacity() * 2);
	}

	/**
	 * Inform parent that capacity changed.
	 */
	private void informParentThatCapacityChanged()
	{
		if (getParentZOrderManager() != null)
		{
			getParentZOrderManager().notifyChildZCapacityChanged(itemBeingManaged, this);
		}
	}

}
