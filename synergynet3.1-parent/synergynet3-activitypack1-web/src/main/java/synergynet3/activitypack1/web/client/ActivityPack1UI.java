package synergynet3.activitypack1.web.client;

import synergynet3.activitypack1.web.client.gravitysim.GravitySimUI;

import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * The Class ActivityPack1UI.
 */
public class ActivityPack1UI extends VerticalPanel {

	/**
	 * Instantiates a new activity pack1 ui.
	 *
	 * @param app the app
	 */
	public ActivityPack1UI(String app) {
		super();
		if ("gravity".equals(app)) {
			this.add(new GravitySimUI());
		}
	}
}
