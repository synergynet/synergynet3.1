package synergynet3.web.appsystem.client.ui;

import synergynet3.web.commons.client.ui.FixedSizeScrollableListBox;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ApplicationsAvailableWidget extends VerticalPanel {

	private FixedSizeScrollableListBox listBox;
	private HorizontalPanel buttonsPanel;
	
	public ApplicationsAvailableWidget() {
		super();
		listBox = new FixedSizeScrollableListBox();
		listBox.setAllowsSelection(true);
		listBox.setMultipleSelect(false);		
		add(listBox);
		buttonsPanel = new HorizontalPanel();
		add(buttonsPanel);
	}
	
	public void setSize(String width, String height) {
		super.setSize(width, height);
		listBox.setSize(width, height);
	}

	public void setShouldAllowSelection(boolean b) {
		listBox.setAllowsSelection(b);
	}

	public void removeAllApplications() {
		listBox.removeAllItems();		
	}

	public void addApplicationName(String appName) {
		listBox.addItem(appName);
	}

	public String getSelectedApplication() {
		return listBox.getSelectedItem();
	}
	
	public HorizontalPanel getButtonsPanel() {
		return buttonsPanel;
	}

}
