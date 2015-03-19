package synergynet3.additionalitems;

import java.util.Collections;
import java.util.List;

import multiplicity3.csys.items.item.IItem;

public class PerformActionOnAllDescendents {
	
	protected boolean stop = false;
	
	public PerformActionOnAllDescendents(IItem item, boolean breadth, boolean reverse){		
		if (breadth){
			if (!reverse){
				action(item);
				cycleThroughDescendentsBreadth(item);
			}else{
				action(item);
				cycleThroughDescendentsBreadthReverse(item);
			}			
		}else{
			if (!reverse){
				cycleThroughDescendentsDepth(item);
			}else{
				cycleThroughDescendentsDepthReverse(item);
			}	
		}	
		onFinish();
	}
	
	private void cycleThroughDescendentsBreadth(IItem item){
		for (IItem descendent: item.getChildItems()){	
			action(descendent);
		}		
		for (IItem descendent: item.getChildItems()){	
			cycleThroughDescendentsBreadth(descendent);
		}
	}
	
	private void cycleThroughDescendentsBreadthReverse(IItem item) {
		List<IItem> reverseList = item.getChildItems();
		Collections.reverse(reverseList);
		for (IItem descendent: reverseList){	
			action(descendent);
		}	
		for (IItem descendent: reverseList){	
			cycleThroughDescendentsBreadthReverse(descendent);
		}
	}

	private void cycleThroughDescendentsDepth(IItem item) {		
		for (IItem descendent: item.getChildItems()){	
			cycleThroughDescendentsDepth(descendent);
		}
		action(item);
	}

	private void cycleThroughDescendentsDepthReverse(IItem item) {
		action(item);
		for (IItem descendent: item.getChildItems()){	
			cycleThroughDescendentsDepthReverse(descendent);
		}
	}
	
	private void action(IItem child){	
		if (!stop){
			actionOnDescendent(child);
		}
	}
	
	protected void actionOnDescendent(IItem child) {}
	protected void onFinish() {}

}
