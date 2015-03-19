package synergynet3.web.appsystem.client.ui.dbcontrol;

import synergynet3.web.appsystem.client.SynergyNetAppSystemUI.SynergyNetAppServiceUIDelegate;

import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class DatabaseControlUI extends VerticalPanel {
	
	private static TabPanel tabPanel;
	private static DatabaseControlUI sharedInstance;
	private static SynergyNetAppServiceUIDelegate delegate;
	
	private static StudentAdministationPanel studentAdminPanel;
	private static ClassSelectionPanel classSelectionPanel;
	
	public static DatabaseControlUI get() {
		return sharedInstance;
	}

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
	
	public void setDelegate(SynergyNetAppServiceUIDelegate delegate) {
		DatabaseControlUI.delegate = delegate;
	}
	
	public static void removeDatabaseTabs(){
		delegate.shouldHideDBControls();
	}

	/**
	 * @return the studentAdminPanel
	 */
	public static StudentAdministationPanel getStudentAdminPanel() {
		return studentAdminPanel;
	}

	/**
	 * @return the databaseSelectionPanel
	 */
	public static ClassSelectionPanel getClassSelectionPanel() {
		return classSelectionPanel;
	}
	
}
