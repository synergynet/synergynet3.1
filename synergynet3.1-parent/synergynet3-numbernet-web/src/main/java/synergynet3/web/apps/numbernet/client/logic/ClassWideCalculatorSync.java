package synergynet3.web.apps.numbernet.client.logic;

import com.google.gwt.user.client.rpc.AsyncCallback;

import synergynet3.web.apps.numbernet.client.calckeys.CalculatorKeyControlPanel.CalculatorKeyControlPanelDelegate;
import synergynet3.web.apps.numbernet.client.service.NumberNetService;
import synergynet3.web.apps.numbernet.shared.CalculatorKey;
import synergynet3.web.commons.client.dialogs.MessageDialogBox;

public class ClassWideCalculatorSync implements CalculatorKeyControlPanelDelegate {

	@Override
	public void keyStateChanged(CalculatorKey key, boolean state) {
		NumberNetService.Util.getInstance().setCalculatorKeyStateForAllTables(key, state, new AsyncCallback<Void>() {			
			@Override
			public void onSuccess(Void result) {}
			
			@Override
			public void onFailure(Throwable caught) {
				new MessageDialogBox(caught.getMessage()).show();
			}
		});
	}
}
