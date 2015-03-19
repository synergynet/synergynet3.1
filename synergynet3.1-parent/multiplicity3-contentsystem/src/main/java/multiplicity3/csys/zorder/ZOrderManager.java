package multiplicity3.csys.zorder;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import multiplicity3.csys.items.item.IItem;
import multiplicity3.input.events.MultiTouchCursorEvent;

public class ZOrderManager implements IZOrderManager {
	private static final Logger log = Logger.getLogger(ZOrderManager.class.getName());

	protected List<IItem> registeredItems = new ArrayList<IItem>();
	private int capacity = 1;
	protected int startZOrder = 1000;
	protected int usedZSpace = 1;
	private IItem itemBeingManaged;
	private boolean autoBringToTop = true;
	private boolean bringToTopPropagatesUp = true;

	public ZOrderManager(IItem itemBeingManaged, int initialCapacity) {
		capacity = initialCapacity;
		this.itemBeingManaged = itemBeingManaged;
	}

	public IZOrderManager getParentZOrderManager() {
		if(itemBeingManaged != null && itemBeingManaged.getParentItem() != null) {
			return itemBeingManaged.getParentItem().getZOrderManager();
		}
		return null;
	}


	@Override
	public void itemCursorPressed(IItem item, MultiTouchCursorEvent event) {
		if(autoBringToTop) {
			log.fine("Bringing " + item + " to the top");
			bringToTop(item);
		}
		if(bringToTopPropagatesUp && getParentZOrderManager() != null) {
			getParentZOrderManager().bringToTop(item.getParentItem());
		}
	}


	@Override
	public int getItemZOrder() {
		return this.startZOrder;
	}

	
	@Override
	public void setItemZOrder(int zValue) {
		this.startZOrder = zValue;
		updateOrder();
	}

	
	@Override
	public void notifyChildZCapacityChanged(IItem item, IZOrderManager manager) {
		log.fine(this.itemBeingManaged + " has item that changed capacity: " + item);
		int zReq = capacity;
		for(IItem i : registeredItems) {
			zReq += i.getZOrderManager().getZCapacity();
		}
		this.usedZSpace = zReq;
		if(usedZSpace > capacity) {
			doubleZSpaceCapacity();	
		}
		updateOrder();
	}


	@Override
	public int getZCapacity() {
		return capacity;
	}

	@Override
	public void setZCapacity(int c) {
		log.fine(this.itemBeingManaged + " setCapacity " + c);
		if(c > capacity) {
			this.capacity = c;
			log.fine(this.itemBeingManaged + " now has z capacity of " + this.capacity);
			informParentThatCapacityChanged();
		}
	}



	@Override
	public void updateOrder() {		
		if(itemBeingManaged != null) {
			itemBeingManaged.setZOrder(startZOrder);
		}
		int z = startZOrder;
		for(IItem i : registeredItems) {
			i.getZOrderManager().setItemZOrder(z);
			z -= i.getZOrderManager().getZCapacity();
		}		
	}

	@Override
	public void registerForZOrdering(IItem item) {		
		if(!registeredItems.contains(item)) {
			registeredItems.add(0, item);
			item.getZOrderManager().setItemZOrder(usedZSpace);
			usedZSpace += item.getZOrderManager().getZCapacity();
			if(usedZSpace > capacity) {
				doubleZSpaceCapacity();	
			}
			item.addItemListener(this);			
		}
		updateOrder();
	}




	@Override
	public void unregisterForZOrdering(IItem item) {
		if(registeredItems.contains(item)) {
			registeredItems.remove(item);
			usedZSpace -= item.getZOrderManager().getZCapacity();
			item.removeItemListener(this);
		}
		updateOrder();		
	}	

	@Override
	public void bringToTop(IItem item) {
		registeredItems.remove(item);
		registeredItems.add(0, item);
		updateOrder();
	}

	@Override
	public void sendToBottom(IItem item) {
		registeredItems.remove(item);
		registeredItems.add(item);
		updateOrder();
	}

	@Override
	public void setAutoBringToTop(boolean enabled) {
		this.autoBringToTop  = enabled;
		updateOrder();
	}

	@Override
	public void setBringToTopPropagatesUp(boolean should) {
		this.bringToTopPropagatesUp  = should;		
	}

	@Override
	public void ignoreItemClickedBehaviour(IItem item) {
		item.removeItemListener(this);
	}


	// ****** unused ********

	@Override
	public void itemMoved(IItem item) {}

	@Override
	public void itemRotated(IItem item) {}

	@Override
	public void itemScaled(IItem item) {}

	@Override
	public void itemCursorReleased(IItem item, MultiTouchCursorEvent event) {}

	@Override
	public void itemCursorChanged(IItem item, MultiTouchCursorEvent event) {}

	@Override
	public void itemCursorClicked(IItem item, MultiTouchCursorEvent event) {}

	@Override
	public void itemZOrderChanged(IItem item) {}
	
	@Override
	public void itemVisibilityChanged(IItem item, boolean isVisible) {}
	
	
	// ****** private methods *****
	
	private void doubleZSpaceCapacity() {
		setZCapacity(getZCapacity() * 2);
	}
	
	private void informParentThatCapacityChanged() {
		if(getParentZOrderManager() != null) {
			getParentZOrderManager().notifyChildZCapacityChanged(itemBeingManaged, this);
		}
	}


}
