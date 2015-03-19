package synergynet3.web.commons.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class SimpleListBox extends VerticalPanel {	
	
	private boolean multipleSelect = false;
	private boolean allowsSelection = true;

	public SimpleListBox() {
		super();
		setStylePrimaryName("simpleListBox");
	}
	
	// *** managing items ***
	
	public void removeAllItems() {
		removeAllWidgets();
	}

	public void addItem(String item) {
		addLabel(item);
	}

	public int getItemCount() {
		return getWidgetCount();
	}
	
	public String getItemAtIndex(int index) {
		Label labelForIndex = getLabelAtIndex(index);
		return labelForIndex.getText();
	}
	
	// *** item selection ***
	
	public void setAllowsSelection(boolean allowsSelection) {
		this.allowsSelection = allowsSelection;
		if(!allowsSelection) {
			makeAllLabelsUnselected();
		}
	}
	
	public void setMultipleSelect(boolean isMultipleSelect) {
		this.multipleSelect = isMultipleSelect;
		makeAllLabelsUnselected();
	}
	
	public boolean isMultipleSelect() {
		return this.multipleSelect;
	}
	
	public int getSelectedIndex() {
		for(int i = 0; i < getItemCount(); i++) {
			if(isItemSelectedAtIndex(i))
				return i;
		}
		return -1;
	}
	
	public String getSelectedItem() {
		int selectedIndex = getSelectedIndex();
		if(selectedIndex == -1) return null;		
		return getItemAtIndex(selectedIndex);
	}
	
	public List<String> getSelectedItems() {
		List<String> list = new ArrayList<String>();
		for(int i = 0; i < getItemCount(); i++) {
			if(isItemSelectedAtIndex(i))
				list.add(getItemAtIndex(i));
		}
		return list;
	}

	
	public boolean isSelected(int index) {
		return isItemSelectedAtIndex(index);
	}
	
	public void selectAll() {
		if(!allowsSelection) return;
		if(!multipleSelect) return;
		makeAllLabelsSelected();
	}

	// *** handlers ***
	
	public HandlerRegistration addClickHandler(final ClickHandler handler) {		
		return addDomHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if(allowsSelection) {
					handler.onClick(event);
				}				
			}
			
		}, ClickEvent.getType());
	}
	
	// *** private methods ***
	
	private void removeAllWidgets() {
		this.clear();
	}
	
	private Label getLabelAtIndex(int index) {
		return (Label) this.getWidget(index);
	}
	
	private void addLabel(String item) {
		final Label label = new Label(item);
		label.setStylePrimaryName("simpleListBoxItem");
		label.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				itemClicked(label);
			}
		});
		this.add(label);
	}


	
	// *** private event handling ***


	protected void itemClicked(Label label) {
		if(!this.allowsSelection) return;
		
		if(!labelIsSelected(label)) {
			makeLabelSelected(label);
		}else{
			makeLabelUnselected(label);
		}
		if(!multipleSelect) {
			makeLabelsUnselectedExceptFor(label);
		}
	}

	private boolean isItemSelectedAtIndex(int i) {
		Label labelAtIndex = getLabelAtIndex(i);
		return labelIsSelected(labelAtIndex);
	}

	private void makeLabelUnselected(Label label) {
		label.removeStyleDependentName("selected");
	}

	private void makeLabelSelected(Label label) {
		label.setStyleDependentName("selected", true);
	}

	private boolean labelIsSelected(Label label) {
		return label.getStyleName().contains("selected");
	}
	
	private void makeAllLabelsUnselected() {
		for(int i = 0; i < getItemCount(); i++) {
			Label labelAtIndex = getLabelAtIndex(i);
			makeLabelUnselected(labelAtIndex);
		}
	}
	
	private void makeAllLabelsSelected() {
		for(int i = 0; i < getItemCount(); i++) {
			Label labelAtIndex = getLabelAtIndex(i);
			makeLabelSelected(labelAtIndex);
		}
	}
	
	private void makeLabelsUnselectedExceptFor(Label label) {
		for(int i = 0; i < getItemCount(); i++) {
			Label labelAtIndex = getLabelAtIndex(i);
			if(labelAtIndex != label) {
				makeLabelUnselected(labelAtIndex);
			}
		}
	}







}
