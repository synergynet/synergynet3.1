package synergynet3.additionalitems;

import multiplicity3.csys.items.item.IItem;
import multiplicity3.input.MultiTouchEventAdapter;
import multiplicity3.input.events.MultiTouchCursorEvent;

/**
 * The Class RadialMenuOption.
 */
public class RadialMenuOption {

	/** The item. */
	private IItem item;

	/**
	 * Instantiates a new radial menu option.
	 *
	 * @param item the item
	 */
	public RadialMenuOption(IItem item) {
		item.getMultiTouchDispatcher().addListener(
				new MultiTouchEventAdapter() {
					@Override
					public void cursorClicked(MultiTouchCursorEvent event) {
						onOptionSelect();
					}
				});

		if (item.getChildItems().size() > 0) {
			for (IItem child : item.getChildItems()) {
				child.getMultiTouchDispatcher().addListener(
						new MultiTouchEventAdapter() {
							@Override
							public void cursorClicked(
									MultiTouchCursorEvent event) {
								onOptionSelect();
							}
						});
			}
		}

		this.item = item;
	}

	/**
	 * As item.
	 *
	 * @return the i item
	 */
	public IItem asItem() {
		return item;
	}

	/**
	 * On option select.
	 */
	protected void onOptionSelect() {
	}

}
