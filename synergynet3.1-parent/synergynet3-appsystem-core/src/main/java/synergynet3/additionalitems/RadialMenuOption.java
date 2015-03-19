package synergynet3.additionalitems;

import multiplicity3.csys.items.item.IItem;
import multiplicity3.input.MultiTouchEventAdapter;
import multiplicity3.input.events.MultiTouchCursorEvent;

public class RadialMenuOption {
	
	private IItem item;
	
	public RadialMenuOption(IItem item) {
		item.getMultiTouchDispatcher().addListener(new MultiTouchEventAdapter() {
			@Override
			public void cursorClicked(MultiTouchCursorEvent event) {
				onOptionSelect();
			}
		});
		
		if (item.getChildItems().size() > 0){
			for (IItem child: item.getChildItems()){
				child.getMultiTouchDispatcher().addListener(new MultiTouchEventAdapter() {
					@Override
					public void cursorClicked(MultiTouchCursorEvent event) {
						onOptionSelect();
					}
				});
			}
		}
		
		this.item = item;
	}

	public IItem asItem(){
		return item;
	}
	
	protected void onOptionSelect(){}

}
