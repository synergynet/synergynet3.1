package synergynet3.web.apps.numbernet.client;

import com.google.gwt.core.client.EntryPoint;

/**
 * The Class NumberNetEntryPoint.
 */
public class NumberNetEntryPoint implements EntryPoint
{

	/*
	 * (non-Javadoc)
	 * @see com.google.gwt.core.client.EntryPoint#onModuleLoad()
	 */
	@Override
	public void onModuleLoad()
	{
		// doUI();
	}

	/**
	 * Do ui.
	 */
	protected void doUI()
	{
		com.google.gwt.user.client.ui.RootPanel rootPanel = com.google.gwt.user.client.ui.RootPanel.get();
		NumberNetUI ccui = new NumberNetUI();
		rootPanel.add(ccui);
	}

}
