package synergynet3.web.apps.numbernet.client.projection;

import java.util.ArrayList;
import java.util.List;

import synergynet3.web.commons.client.dialogs.MessageDialogBox;
import synergynet3.web.commons.client.service.SynergyNetWebCommonsService;


import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ClickEvent;

public class ProjectionControl extends VerticalPanel {
	
	private VerticalPanel controlPanels;
	
	public ProjectionControl() {
		controlPanels = new VerticalPanel();
		setTitle("Projection Control");
		 
		Button btnDiscover = new Button("Discover");
		btnDiscover.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				loadProjectionControlPanels();
			}
		});
		add(btnDiscover);
		add(controlPanels);
	}

	protected void loadProjectionControlPanels() {
		SynergyNetWebCommonsService.Util.getInstance().getDevicesCurrentlyOnline("projectors", new AsyncCallback<List<String>>() {
			
			@Override
			public void onSuccess(List<String> result) {
				System.out.println(result);
				List<String> currentlyKnownProjectors = getCurrentlyKnownProjectors();
				List<String> projectorsGone = getCurrentlyKnownProjectors();
				
				for(String op : result) {
					projectorsGone.remove(op);
				}
				
				List<String> newProjectors = new ArrayList<String>();
				for(String n : result) {
					if(!currentlyKnownProjectors.contains(n)) {
						newProjectors.add(n);
					}
				}
				
				removeControlPanelsForNames(projectorsGone);
				addControlPanelsForNames(newProjectors);
				
			}
			
			@Override
			public void onFailure(Throwable caught) {
				new MessageDialogBox(caught.getMessage()).show();				
			}
		});
		
	}
	
	protected void addControlPanelsForNames(List<String> newProjectors) {
		for(String np : newProjectors) {
			ProjectionControlPanel pcp = new ProjectionControlPanel();
			pcp.setProjectorName(np);
			controlPanels.add(pcp);
		}
		
	}

	protected void removeControlPanelsForNames(List<String> projectorsGone) {
		for(int i = 0; i < controlPanels.getWidgetCount(); i++) {
			ProjectionControlPanel pcp = (ProjectionControlPanel) controlPanels.getWidget(i);
			if(projectorsGone.contains(pcp.getProjectorName())) {
				controlPanels.remove(pcp);
			}
		}		
	}

	private List<String> getCurrentlyKnownProjectors() {
		List<String> currentlyKnownProjectors = new ArrayList<String>();
		for(int i = 0; i < controlPanels.getWidgetCount(); i++) {
			ProjectionControlPanel pcp = (ProjectionControlPanel) controlPanels.getWidget(i);
			currentlyKnownProjectors.add(pcp.getProjectorName());
		}
		return currentlyKnownProjectors;
	}

}
