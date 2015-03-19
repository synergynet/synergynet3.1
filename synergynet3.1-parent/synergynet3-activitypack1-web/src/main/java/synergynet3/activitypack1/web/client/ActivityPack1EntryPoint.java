package synergynet3.activitypack1.web.client;


import com.google.gwt.core.client.EntryPoint;

public class ActivityPack1EntryPoint implements EntryPoint {

	public void onModuleLoad() {
		//doUI();
	}

	protected void doUI() {
		com.google.gwt.user.client.ui.RootPanel rootPanel = com.google.gwt.user.client.ui.RootPanel.get();				
		rootPanel.add(new ActivityPack1UI("gravity"));		
	}

	
}
