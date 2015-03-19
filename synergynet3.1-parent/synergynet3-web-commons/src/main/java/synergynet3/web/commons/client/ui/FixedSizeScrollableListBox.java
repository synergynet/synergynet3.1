package synergynet3.web.commons.client.ui;

import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.ScrollPanel;

public class FixedSizeScrollableListBox extends ScrollPanel {
	SimpleListBox listBox;

	public FixedSizeScrollableListBox() {
		listBox = new SimpleListBox();
		setStylePrimaryName("fixedSizeScrollableListBox");
		listBox.setStylePrimaryName("simpleListBoxPlain");
		add(listBox);
	}
	
	public void removeAllItems() {
		listBox.removeAllItems();
	}

	public void addItem(String item) {
		listBox.addItem(item);
	}

	public int getItemCount() {
		return listBox.getWidgetCount();
	}
	
	public String getItemAtIndex(int index) {
		return listBox.getItemAtIndex(index);
	}
	
	// *** item selection ***
	
	public void setAllowsSelection(boolean allowsSelection) {
		listBox.setAllowsSelection(allowsSelection);
	}
	
	public void setMultipleSelect(boolean isMultipleSelect) {
		listBox.setMultipleSelect(isMultipleSelect);
	}
	
	public boolean isMultipleSelect() {
		return listBox.isMultipleSelect();
	}
	
	public int getSelectedIndex() {
		return listBox.getSelectedIndex();
	}
	
	public String getSelectedItem() {
		return listBox.getSelectedItem();
	}
	
	public List<String> getSelectedItems() {
		return listBox.getSelectedItems();
	}

	
	public boolean isSelected(int index) {
		return listBox.isSelected(index);
	}
	

	public void selectAll() {
		listBox.selectAll();
	}

	// *** handlers ***
	
	public HandlerRegistration addClickHandler(ClickHandler handler) {		
		return addDomHandler(handler, ClickEvent.getType());
	}

}
