package synergynet3.web.commons.client.devicesonline;

import java.util.ArrayList;
import java.util.List;

import synergynet3.web.commons.client.service.SynergyNetWebCommonsService;
import synergynet3.web.commons.client.ui.FixedSizeScrollableListBox;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ClickEvent;

public class DevicesOnlineWidget extends VerticalPanel {

	public static interface DevicesOnlineListBoxDelegate {
		public void devicesOnlineListBoxDidReload();
		public void devicesOnlineListBoxReloadFailed(Throwable caught);
		public void noDevicesSelected();
		public void devicesSelected(List<String> device);
	}

	private List<String> tablesPresent = new ArrayList<String>();
	
	private DevicesOnlineListBoxDelegate delegate;
	private String deviceType = "tables";
	
	private HorizontalPanel buttonsPanel;
	private Button btnRefresh;

	private FixedSizeScrollableListBox listBox;
	
	private Label bufferLabel;
	private CheckBox allTables;
	
	private String allTablesOffset = "50px";
	
	public DevicesOnlineWidget() {
		super();
		
		listBox = new FixedSizeScrollableListBox();
		listBox.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				List<String> itemsSelected = listBox.getSelectedItems();
				if(delegate != null) delegate.devicesSelected(itemsSelected);
			}			
		});
		
		add(listBox);

		buttonsPanel = new HorizontalPanel();
		
		btnRefresh = new Button("Refresh");
		btnRefresh.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				loadOnlineTableList();
			}
		});
		buttonsPanel.add(btnRefresh);		
		
		bufferLabel = new Label();
		bufferLabel.setWidth("0px");
		buttonsPanel.add(bufferLabel);
		
		allTables = new CheckBox("Select All");
		allTables.setVisible(false);
		allTables.setEnabled(false);
		buttonsPanel.add(allTables);
		
		add(buttonsPanel);
	}
	
	public void setAllTablesCheckOptionEnabled(boolean allTablesSelect){
		allTables.setVisible(allTablesSelect);
		allTables.setEnabled(allTablesSelect);
		if (allTablesSelect){
			bufferLabel.setWidth(allTablesOffset);
		}else{
			bufferLabel.setWidth("0px");
		}
	}
	
	public void setAllTablesCheckBoxOffset(String allTablesOffset){
		this.allTablesOffset = allTablesOffset;
	}
	
	public boolean getAllDevicesOptionCheck(){
		return allTables.getValue();
	}
	
	public void setMultipleSelectionAllowed(boolean multipleSelect) {
		listBox.setMultipleSelect(multipleSelect);
	}
	
	public void setSize(String width, String height) {
		super.setSize(width, height);
		listBox.setSize(width, height);
	}
	
	public void setListBoxSize(String width, String height) {
		listBox.setSize(width, height);
	}
	
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public void setDelegate(DevicesOnlineListBoxDelegate delegate) {
		this.delegate = delegate;
	}

	public void setRefreshButtonVisibility(boolean visible) {
		this.btnRefresh.setVisible(visible);
	}
	
	public Button getRefreshButton(){
		return btnRefresh;
	}
	
	public HorizontalPanel getButtonsPanel() {
		return this.buttonsPanel;
	}
	
	public List<String> getDevicesSelected() {
		return listBox.getSelectedItems();
	}
	
	public String getDeviceSelected() {
		List<String> devices = getDevicesSelected();
		if(devices.size() < 1) return null;
		return devices.get(0);
	}

	public void updateList() {
		loadOnlineTableList();
	}

	protected void loadOnlineTableList() {
		SynergyNetWebCommonsService.Util.getInstance().getDevicesCurrentlyOnline(deviceType, new AsyncCallback<List<String>>() {			
			@Override
			public void onSuccess(List<String> result) {
				listBox.removeAllItems();
				tablesPresent.clear();
				if(result.size() > 0) {
					for(int row = 0; row < result.size(); row++) {
						listBox.addItem(result.get(row));
						tablesPresent.add(result.get(row));
					}
				}

				if(delegate != null) {
					delegate.devicesOnlineListBoxDidReload();
					delegate.noDevicesSelected();
				}
				
				
				onRefresh();
			}

			@Override
			public void onFailure(Throwable caught) {
				if(delegate != null) delegate.devicesOnlineListBoxReloadFailed(caught);				
			}
		});		
	}
	
	protected void onRefresh() {} 

	public List<String> getDevicesPresent() {
		return tablesPresent;
	}
}
