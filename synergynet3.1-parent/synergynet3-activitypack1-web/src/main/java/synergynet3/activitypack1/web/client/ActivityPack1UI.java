package synergynet3.activitypack1.web.client;

import synergynet3.activitypack1.web.client.gravitysim.GravitySimUI;

import com.google.gwt.user.client.ui.VerticalPanel;

public class ActivityPack1UI extends VerticalPanel {
	public ActivityPack1UI(String app) {
		super();
		if("gravity".equals(app)) {
			this.add(new GravitySimUI());
		}
	}
}
