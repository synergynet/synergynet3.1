package synergynet3.web.appsystem.client.ui;

import synergynet3.web.commons.client.ui.FixedSizeScrollableListBox;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * The Class ApplicationsAvailableWidget.
 */
public class ApplicationsAvailableWidget extends VerticalPanel {

	/** The buttons panel. */
	private HorizontalPanel buttonsPanel;

	/** The list box. */
	private FixedSizeScrollableListBox listBox;

	/**
	 * Instantiates a new applications available widget.
	 */
	public ApplicationsAvailableWidget() {
		super();
		listBox = new FixedSizeScrollableListBox();
		listBox.setAllowsSelection(true);
		listBox.setMultipleSelect(false);
		add(listBox);
		buttonsPanel = new HorizontalPanel();
		add(buttonsPanel);
	}

	/**
	 * Adds the application name.
	 *
	 * @param appName the app name
	 */
	public void addApplicationName(String appName) {
		listBox.addItem(appName);
	}

	/**
	 * Gets the buttons panel.
	 *
	 * @return the buttons panel
	 */
	public HorizontalPanel getButtonsPanel() {
		return buttonsPanel;
	}

	/**
	 * Gets the selected application.
	 *
	 * @return the selected application
	 */
	public String getSelectedApplication() {
		return listBox.getSelectedItem();
	}

	/**
	 * Removes the all applications.
	 */
	public void removeAllApplications() {
		listBox.removeAllItems();
	}

	/**
	 * Sets the should allow selection.
	 *
	 * @param b the new should allow selection
	 */
	public void setShouldAllowSelection(boolean b) {
		listBox.setAllowsSelection(b);
	}

	/*
	 * (non-Javadoc)
	 * @see com.google.gwt.user.client.ui.UIObject#setSize(java.lang.String,
	 * java.lang.String)
	 */
	public void setSize(String width, String height) {
		super.setSize(width, height);
		listBox.setSize(width, height);
	}

}
