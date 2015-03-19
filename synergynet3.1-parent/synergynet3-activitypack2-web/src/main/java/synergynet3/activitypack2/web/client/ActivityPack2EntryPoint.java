package synergynet3.activitypack2.web.client;


import com.google.gwt.core.client.EntryPoint;

public class ActivityPack2EntryPoint implements EntryPoint {

	public void onModuleLoad() {
		//doUI();
	}

	protected void doUI() {
		com.google.gwt.user.client.ui.RootPanel rootPanel = com.google.gwt.user.client.ui.RootPanel.get();				
		rootPanel.add(new ActivityPack2UI("...."));		
	}

	
}
