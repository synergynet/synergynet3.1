package synergynet3.web.apps.numbernet.client.calckeys;

import java.util.Map;

import synergynet3.web.apps.numbernet.client.calckeys.CalculatorKeyControlPanel.CalculatorKeyControlPanelDelegate;
import synergynet3.web.apps.numbernet.shared.CalculatorKey;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * The Class EditCalculatorForTablePanel.
 */
public class EditCalculatorForTablePanel extends VerticalPanel
{

	/** The key control panel. */
	private CalculatorKeyControlPanel keyControlPanel;

	/** The lbltable name. */
	private Label lbltableName;

	/**
	 * Instantiates a new edits the calculator for table panel.
	 */
	public EditCalculatorForTablePanel()
	{
		super();

		lbltableName = new Label(" ");
		add(lbltableName);
		keyControlPanel = new CalculatorKeyControlPanel();
		add(keyControlPanel);
	}

	/**
	 * Sets the calculator key control panel delegate.
	 *
	 * @param delegate
	 *            the new calculator key control panel delegate
	 */
	public void setCalculatorKeyControlPanelDelegate(CalculatorKeyControlPanelDelegate delegate)
	{
		keyControlPanel.delegate = delegate;
	}

	/**
	 * Sets the table name.
	 *
	 * @param name
	 *            the new table name
	 */
	public void setTableName(String name)
	{
		lbltableName.setText(name);
	}

	/**
	 * Sets the values for check boxes with key state info.
	 *
	 * @param keyStateInfo
	 *            the key state info
	 */
	public void setValuesForCheckBoxesWithKeyStateInfo(Map<CalculatorKey, Boolean> keyStateInfo)
	{
		keyControlPanel.setValuesForCheckBoxesWithKeyStateInfo(keyStateInfo);
	}
}
