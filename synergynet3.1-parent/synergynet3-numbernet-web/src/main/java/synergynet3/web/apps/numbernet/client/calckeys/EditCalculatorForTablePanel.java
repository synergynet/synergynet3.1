package synergynet3.web.apps.numbernet.client.calckeys;


import java.util.Map;

import synergynet3.web.apps.numbernet.client.calckeys.CalculatorKeyControlPanel.CalculatorKeyControlPanelDelegate;
import synergynet3.web.apps.numbernet.shared.CalculatorKey;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Label;

public class EditCalculatorForTablePanel extends VerticalPanel {
	
	private CalculatorKeyControlPanel keyControlPanel;
	private Label lbltableName;
	
	public EditCalculatorForTablePanel() {
		super();
		
		lbltableName = new Label(" ");
		add(lbltableName);
		keyControlPanel = new CalculatorKeyControlPanel();
		add(keyControlPanel);
	}
	

	public void setTableName(String name) {
		lbltableName.setText(name);
	}
	
	public void setCalculatorKeyControlPanelDelegate(CalculatorKeyControlPanelDelegate delegate) {
		keyControlPanel.delegate = delegate;
	}
	
	public void setValuesForCheckBoxesWithKeyStateInfo(Map<CalculatorKey,Boolean> keyStateInfo) {
		keyControlPanel.setValuesForCheckBoxesWithKeyStateInfo(keyStateInfo);
	}
}
