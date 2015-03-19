package synergynet3.web.appsystem.client;

import java.util.List;
import java.util.Map;

import synergynet3.web.appsystem.client.service.SynergyNetAppSystemService;
import synergynet3.web.appsystem.client.ui.ApplicationsAvailableWidget;
import synergynet3.web.commons.client.devicesonline.DevicesOnlineWidget;
import synergynet3.web.commons.client.devicesonline.DevicesOnlineWidget.DevicesOnlineListBoxDelegate;
import synergynet3.web.commons.client.dialogs.MessageDialogBox;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ClickEvent;

public class SynergyNetAppSystemUI extends VerticalPanel {

	private ApplicationsAvailableWidget availableApplicationsList;
	private DevicesOnlineWidget dow;
	private Map<String, String> availableAppNamesToClassName;
	private Button btnOpenManagementUi;
	private SynergyNetAppServiceUIDelegate delegate;
	
	public static interface SynergyNetAppServiceUIDelegate {
		void shouldOpenManagementUIForClassName(String appClass);	
		void shouldHideDBControls();
	}
	

	public SynergyNetAppSystemUI() {
		super();
		
		this.setSpacing(5);
		
		dow = new DevicesOnlineWidget();
		dow.setMultipleSelectionAllowed(true);
		dow.setSpacing(5);
		dow.setSize("272px", "100px");
		dow.setDeviceType("tables");
		dow.setDelegate(new DevicesOnlineListBoxDelegate() {			
			@Override
			public void noDevicesSelected() {}
			
			@Override
			public void devicesSelected(List<String> device) {}
			
			@Override
			public void devicesOnlineListBoxReloadFailed(Throwable caught) {
				new MessageDialogBox("Communication error listing online devices: " + caught.getMessage()).show();	
			}
			
			@Override
			public void devicesOnlineListBoxDidReload() {}
		});
		dow.setAllTablesCheckBoxOffset("120px");
		dow.setAllTablesCheckOptionEnabled(true);
		dow.updateList();
		add(dow);
		
		availableApplicationsList = new ApplicationsAvailableWidget();
		availableApplicationsList.setSpacing(5);
		availableApplicationsList.setSize("270px", "100px");
		availableApplicationsList.setShouldAllowSelection(true);
		add(availableApplicationsList);
		
		Button btnSetActiveApplication = new Button("Set Active Application");
		btnSetActiveApplication.setWidth("272px");
		btnSetActiveApplication.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				setActiveApplicationButtonClicked(event);
			}
		});
		availableApplicationsList.getButtonsPanel().add(btnSetActiveApplication);
		
		btnOpenManagementUi = new Button("Open Management UI");
		btnOpenManagementUi.setWidth("272px");
		btnOpenManagementUi.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				openManagementUIButtonClicked();
			}
		});
		availableApplicationsList.add(btnOpenManagementUi);
	}
	
	public void setDelegate(SynergyNetAppServiceUIDelegate delegate) {
		this.delegate = delegate;
	}

	public SynergyNetAppServiceUIDelegate getDelegate() {
		return delegate;
	}

	protected void setActiveApplicationButtonClicked(ClickEvent event) {
		String appClass = getSelectedApplicationClassName();
		if (appClass == null) {
			new MessageDialogBox("Please select at least one application").show();
			return;
		}
		
		if (dow.getDevicesSelected().size() < 1 && !dow.getAllDevicesOptionCheck()) {
			new MessageDialogBox("Please select at least one device").show();
			return;
		}
		List <String> devicesSelected;
		if (dow.getAllDevicesOptionCheck()){
			devicesSelected = dow.getDevicesPresent();
		}else{
			devicesSelected = dow.getDevicesSelected();
		}		
		SynergyNetAppSystemService.Util.get().devicesShouldOpenApplication(appClass, devicesSelected, new AsyncCallback<Void>() {
			@Override
			public void onFailure(Throwable caught) {
				new MessageDialogBox("Communication error setting app: " + caught.getMessage()).show();
			}

			@Override
			public void onSuccess(Void result) {}
		});
	}

	protected void openManagementUIButtonClicked() {
		String appClass = getSelectedApplicationClassName();
		delegate.shouldOpenManagementUIForClassName(appClass);
	}


	public void setKnownApplicationNamesWithClasses(Map<String, String> availableAppNamesToClassName) {
		this.availableAppNamesToClassName = availableAppNamesToClassName;
		availableApplicationsList.removeAllApplications();
		for(String key : availableAppNamesToClassName.keySet()) {
			availableApplicationsList.addApplicationName(key);		
		}	
	}
		
	// *** private methods ***
	
	private String getSelectedApplicationClassName() {
		String appNameSelected = availableApplicationsList.getSelectedApplication();
		if(appNameSelected == null) return null;		
		return availableAppNamesToClassName.get(appNameSelected);
	}



	
}
