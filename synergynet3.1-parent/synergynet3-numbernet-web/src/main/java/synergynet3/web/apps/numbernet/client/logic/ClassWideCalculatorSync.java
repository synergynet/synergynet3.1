package synergynet3.web.apps.numbernet.client.logic;

import synergynet3.web.apps.numbernet.client.calckeys.CalculatorKeyControlPanel.CalculatorKeyControlPanelDelegate;
import synergynet3.web.apps.numbernet.client.service.NumberNetService;
import synergynet3.web.apps.numbernet.shared.CalculatorKey;
import synergynet3.web.commons.client.dialogs.MessageDialogBox;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The Class ClassWideCalculatorSync.
 */
public class ClassWideCalculatorSync implements CalculatorKeyControlPanelDelegate
{

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynet3.web.apps.numbernet.client.calckeys.CalculatorKeyControlPanel
	 * .CalculatorKeyControlPanelDelegate
	 * #keyStateChanged(synergynet3.web.apps.numbernet.shared.CalculatorKey,
	 * boolean)
	 */
	@Override
	public void keyStateChanged(CalculatorKey key, boolean state)
	{
		NumberNetService.Util.getInstance().setCalculatorKeyStateForAllTables(key, state, new AsyncCallback<Void>()
		{
			@Override
			public void onFailure(Throwable caught)
			{
				new MessageDialogBox(caught.getMessage()).show();
			}

			@Override
			public void onSuccess(Void result)
			{
			}
		});
	}
}
