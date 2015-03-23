package synergynet3.web.commons.client.ui;

import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.ScrollPanel;

/**
 * The Class FixedSizeScrollableListBox.
 */
public class FixedSizeScrollableListBox extends ScrollPanel {

	/** The list box. */
	SimpleListBox listBox;

	/**
	 * Instantiates a new fixed size scrollable list box.
	 */
	public FixedSizeScrollableListBox() {
		listBox = new SimpleListBox();
		setStylePrimaryName("fixedSizeScrollableListBox");
		listBox.setStylePrimaryName("simpleListBoxPlain");
		add(listBox);
	}

	/**
	 * Adds the click handler.
	 *
	 * @param handler the handler
	 * @return the handler registration
	 */
	public HandlerRegistration addClickHandler(ClickHandler handler) {
		return addDomHandler(handler, ClickEvent.getType());
	}

	/**
	 * Adds the item.
	 *
	 * @param item the item
	 */
	public void addItem(String item) {
		listBox.addItem(item);
	}

	/**
	 * Gets the item at index.
	 *
	 * @param index the index
	 * @return the item at index
	 */
	public String getItemAtIndex(int index) {
		return listBox.getItemAtIndex(index);
	}

	/**
	 * Gets the item count.
	 *
	 * @return the item count
	 */
	public int getItemCount() {
		return listBox.getWidgetCount();
	}

	// *** item selection ***

	/**
	 * Gets the selected index.
	 *
	 * @return the selected index
	 */
	public int getSelectedIndex() {
		return listBox.getSelectedIndex();
	}

	/**
	 * Gets the selected item.
	 *
	 * @return the selected item
	 */
	public String getSelectedItem() {
		return listBox.getSelectedItem();
	}

	/**
	 * Gets the selected items.
	 *
	 * @return the selected items
	 */
	public List<String> getSelectedItems() {
		return listBox.getSelectedItems();
	}

	/**
	 * Checks if is multiple select.
	 *
	 * @return true, if is multiple select
	 */
	public boolean isMultipleSelect() {
		return listBox.isMultipleSelect();
	}

	/**
	 * Checks if is selected.
	 *
	 * @param index the index
	 * @return true, if is selected
	 */
	public boolean isSelected(int index) {
		return listBox.isSelected(index);
	}

	/**
	 * Removes the all items.
	 */
	public void removeAllItems() {
		listBox.removeAllItems();
	}

	/**
	 * Select all.
	 */
	public void selectAll() {
		listBox.selectAll();
	}

	/**
	 * Sets the allows selection.
	 *
	 * @param allowsSelection the new allows selection
	 */
	public void setAllowsSelection(boolean allowsSelection) {
		listBox.setAllowsSelection(allowsSelection);
	}

	// *** handlers ***

	/**
	 * Sets the multiple select.
	 *
	 * @param isMultipleSelect the new multiple select
	 */
	public void setMultipleSelect(boolean isMultipleSelect) {
		listBox.setMultipleSelect(isMultipleSelect);
	}

}
