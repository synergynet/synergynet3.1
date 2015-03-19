package multiplicity3.csys.items;

import multiplicity3.csys.items.item.IItem;

public interface IPalet extends IItem {

	public void lockPalet(boolean locked);
	
	public int tap();
	
	public void resetTaps();

}
