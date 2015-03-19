package synergynet3.web.earlyyears.client;


import com.google.gwt.core.client.EntryPoint;

public class EarlyYearsEntryPoint implements EntryPoint {

	public void onModuleLoad() {
		//doUI();
	}

	protected void doUI() {
		com.google.gwt.user.client.ui.RootPanel rootPanel = com.google.gwt.user.client.ui.RootPanel.get();		
		EarlyYearsUI eyui = new EarlyYearsUI();
		rootPanel.add(eyui);	
	}

	
}
