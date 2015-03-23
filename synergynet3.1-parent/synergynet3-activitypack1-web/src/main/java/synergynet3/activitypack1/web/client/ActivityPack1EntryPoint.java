package synergynet3.activitypack1.web.client;

import com.google.gwt.core.client.EntryPoint;

/**
 * The Class ActivityPack1EntryPoint.
 */
public class ActivityPack1EntryPoint implements EntryPoint {

	/*
	 * (non-Javadoc)
	 * @see com.google.gwt.core.client.EntryPoint#onModuleLoad()
	 */
	public void onModuleLoad() {
		// doUI();
	}

	/**
	 * Do ui.
	 */
	protected void doUI() {
		com.google.gwt.user.client.ui.RootPanel rootPanel = com.google.gwt.user.client.ui.RootPanel
				.get();
		rootPanel.add(new ActivityPack1UI("gravity"));
	}

}
