package synergynet3.web.earlyyears.client;

import com.google.gwt.core.client.EntryPoint;

/**
 * The Class EarlyYearsEntryPoint.
 */
public class EarlyYearsEntryPoint implements EntryPoint {

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
		EarlyYearsUI eyui = new EarlyYearsUI();
		rootPanel.add(eyui);
	}

}
