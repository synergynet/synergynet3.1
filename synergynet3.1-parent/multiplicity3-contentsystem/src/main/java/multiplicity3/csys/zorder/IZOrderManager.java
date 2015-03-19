package multiplicity3.csys.zorder;

import multiplicity3.csys.items.events.IItemListener;
import multiplicity3.csys.items.item.IItem;

public interface IZOrderManager extends IItemListener {
	void registerForZOrdering(IItem item);	
	void unregisterForZOrdering(IItem i);
	void updateOrder();
	
	void ignoreItemClickedBehaviour(IItem item);
	
	void setItemZOrder(int zValue);
	int getItemZOrder();
	
	void notifyChildZCapacityChanged(IItem itemBeingManaged, IZOrderManager defaultZOrderManager);
	int getZCapacity();
	void setZCapacity(int c);

	
	
	void setAutoBringToTop(boolean enabled);
	void setBringToTopPropagatesUp(boolean should);
	
	void bringToTop(IItem item);
	void sendToBottom(IItem item);	
}
