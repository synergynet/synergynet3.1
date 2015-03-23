package synergynet3.activitypack2.web.client;

import com.google.gwt.core.client.EntryPoint;

/**
 * The Class ActivityPack2EntryPoint.
 */
public class ActivityPack2EntryPoint implements EntryPoint {

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
		rootPanel.add(new ActivityPack2UI("...."));
	}

}
