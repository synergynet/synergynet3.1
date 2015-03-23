package synergynet3.web.appsystem.client.ui.dbcontrol;

import synergynet3.web.appsystem.client.SynergyNetAppSystemUI.SynergyNetAppServiceUIDelegate;

import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * The Class DatabaseControlUI.
 */
public class DatabaseControlUI extends VerticalPanel {

	/** The class selection panel. */
	private static ClassSelectionPanel classSelectionPanel;

	/** The delegate. */
	private static SynergyNetAppServiceUIDelegate delegate;

	/** The shared instance. */
	private static DatabaseControlUI sharedInstance;

	/** The student admin panel. */
	private static StudentAdministationPanel studentAdminPanel;

	/** The tab panel. */
	private static TabPanel tabPanel;

	/**
	 * Instantiates a new database control ui.
	 */
	public DatabaseControlUI() {
		super();
		sharedInstance = this;
		tabPanel = new TabPanel();

		tabPanel.setWidth("270px");

		studentAdminPanel = new StudentAdministationPanel();
		tabPanel.add(studentAdminPanel, studentAdminPanel.getTitle(), false);

		classSelectionPanel = new ClassSelectionPanel();
		tabPanel.add(classSelectionPanel, classSelectionPanel.getTitle(), false);

		tabPanel.selectTab(0);
		add(tabPanel);
	}

	/**
	 * Gets the.
	 *
	 * @return the database control ui
	 */
	public static DatabaseControlUI get() {
		return sharedInstance;
	}

	/**
	 * @return the databaseSelectionPanel
	 */
	public static ClassSelectionPanel getClassSelectionPanel() {
		return classSelectionPanel;
	}

	/**
	 * @return the studentAdminPanel
	 */
	public static StudentAdministationPanel getStudentAdminPanel() {
		return studentAdminPanel;
	}

	/**
	 * Removes the database tabs.
	 */
	public static void removeDatabaseTabs() {
		delegate.shouldHideDBControls();
	}

	/**
	 * Sets the delegate.
	 *
	 * @param delegate the new delegate
	 */
	public void setDelegate(SynergyNetAppServiceUIDelegate delegate) {
		DatabaseControlUI.delegate = delegate;
	}

}
