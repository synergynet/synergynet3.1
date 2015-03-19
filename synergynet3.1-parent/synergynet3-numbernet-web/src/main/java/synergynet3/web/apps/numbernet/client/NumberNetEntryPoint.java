package synergynet3.web.apps.numbernet.client;

import com.google.gwt.core.client.EntryPoint;

public class NumberNetEntryPoint implements EntryPoint {

	public void onModuleLoad() {
		//doUI();
	}

	protected void doUI() {
		com.google.gwt.user.client.ui.RootPanel rootPanel = com.google.gwt.user.client.ui.RootPanel.get();		
		NumberNetUI ccui = new NumberNetUI();
		rootPanel.add(ccui);		
	}

	
}
