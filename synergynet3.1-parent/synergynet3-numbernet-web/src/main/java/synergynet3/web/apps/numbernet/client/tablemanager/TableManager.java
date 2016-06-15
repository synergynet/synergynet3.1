package synergynet3.web.apps.numbernet.client.tablemanager;

import java.util.List;
import java.util.Map;

import synergynet3.web.apps.numbernet.client.NumberNetUI;
import synergynet3.web.apps.numbernet.client.calckeys.EditCalculatorForTablePanel;
import synergynet3.web.apps.numbernet.client.logic.CalculatorSync;
import synergynet3.web.apps.numbernet.client.participantmanager.ParticipantManager;
import synergynet3.web.apps.numbernet.client.service.NumberNetService;
import synergynet3.web.apps.numbernet.shared.CalculatorKey;
import synergynet3.web.commons.client.devicesonline.DevicesOnlineWidget;
import synergynet3.web.commons.client.dialogs.MessageDialogBox;
import synergynet3.web.commons.client.ui.FixedSizeScrollableListBox;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * The Class TableManager.
 */
public class TableManager extends VerticalPanel
{

	/** The list box expected tables. */
	private FixedSizeScrollableListBox listBoxExpectedTables;

	/** The tables online. */
	private DevicesOnlineWidget tablesOnline;

	/** The txt table name. */
	private TextBox txtTableName;

	/**
	 * Instantiates a new table manager.
	 */
	public TableManager()
	{
		super();
		setSpacing(10);
		setSize("492px", "437px");

		DisclosurePanel disclosurePanelTablesExpected = new DisclosurePanel("Tables Expected");
		disclosurePanelTablesExpected.setOpen(true);
		add(disclosurePanelTablesExpected);

		VerticalPanel expectedTablesPanel = new VerticalPanel();
		expectedTablesPanel.setSpacing(5);
		disclosurePanelTablesExpected.setContent(expectedTablesPanel);
		expectedTablesPanel.setSize("437px", "4cm");

		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.setSpacing(5);
		expectedTablesPanel.add(horizontalPanel);
		horizontalPanel.setWidth("355px");

		Label label_2 = new Label("Table Name:");
		horizontalPanel.add(label_2);
		horizontalPanel.setCellHorizontalAlignment(label_2, HasHorizontalAlignment.ALIGN_RIGHT);

		txtTableName = new TextBox();
		horizontalPanel.add(txtTableName);
		txtTableName.setWidth("172px");

		Button btnAddTableName = new Button("Add");
		btnAddTableName.addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(ClickEvent event)
			{
				addExpectedTable();
			}
		});
		horizontalPanel.add(btnAddTableName);

		HorizontalPanel horizontalPanel_1 = new HorizontalPanel();
		horizontalPanel_1.setSpacing(10);
		expectedTablesPanel.add(horizontalPanel_1);
		horizontalPanel_1.setWidth("355px");

		listBoxExpectedTables = new FixedSizeScrollableListBox();
		horizontalPanel_1.add(listBoxExpectedTables);
		horizontalPanel_1.setCellHorizontalAlignment(listBoxExpectedTables, HasHorizontalAlignment.ALIGN_RIGHT);
		listBoxExpectedTables.setSize("160px", "100px");

		VerticalPanel verticalPanel_1 = new VerticalPanel();
		verticalPanel_1.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
		verticalPanel_1.setSpacing(5);
		horizontalPanel_1.add(verticalPanel_1);
		verticalPanel_1.setWidth("220px");

		Button btnExpectedTableView = new Button("Open Participant Manager");
		btnExpectedTableView.addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(ClickEvent event)
			{
				String table = listBoxExpectedTables.getSelectedItem();
				if (table == null)
				{
					return;
				}
				openTableTab(table);
			}
		});
		verticalPanel_1.add(btnExpectedTableView);

		Button btnRemove = new Button("Remove Table");
		btnRemove.addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(ClickEvent event)
			{
				removeExpectedTable();
			}
		});
		verticalPanel_1.add(btnRemove);
		horizontalPanel_1.setCellVerticalAlignment(btnRemove, HasVerticalAlignment.ALIGN_BOTTOM);

		Button btnRefreshExpected = new Button("Refresh");
		btnRefreshExpected.addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(ClickEvent event)
			{
				loadExpectedTablesList();
			}
		});
		verticalPanel_1.add(btnRefreshExpected);

		DisclosurePanel disclosurePanelTablesOnline = new DisclosurePanel("Tables Online");
		disclosurePanelTablesOnline.setOpen(true);
		add(disclosurePanelTablesOnline);

		VerticalPanel verticalPanelTablesOnline = new VerticalPanel();
		verticalPanelTablesOnline.setSpacing(5);
		disclosurePanelTablesOnline.setContent(verticalPanelTablesOnline);
		verticalPanelTablesOnline.setSize("417px", "4cm");

		tablesOnline = new DevicesOnlineWidget();
		tablesOnline.setDeviceType("tables");
		tablesOnline.setSize("381px", "100px");
		tablesOnline.getButtonsPanel().setSpacing(5);
		verticalPanelTablesOnline.add(tablesOnline);

		Button btnOnlineTableView = new Button("View");
		btnOnlineTableView.addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(ClickEvent event)
			{
			}
		});
		tablesOnline.getButtonsPanel().add(btnOnlineTableView);

		Button btnEditCalculators = new Button("Edit Calculators");
		btnEditCalculators.addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(ClickEvent event)
			{
				editCalculatorForSpecificTableButtonPressed();
			}
		});
		tablesOnline.getButtonsPanel().add(btnEditCalculators);

		// loadExpectedTablesList();
		// loadOnlineTableList();
	}

	/*
	 * (non-Javadoc)
	 * @see com.google.gwt.user.client.ui.UIObject#getTitle()
	 */
	@Override
	public String getTitle()
	{
		return "Table Manager";
	}

	/**
	 * Adds the expected table.
	 */
	protected void addExpectedTable()
	{
		System.out.println("adding expected table...");
		NumberNetService.Util.getInstance().addExpectedTable(txtTableName.getText(), new AsyncCallback<Void>()
		{
			@Override
			public void onFailure(Throwable caught)
			{
				new MessageDialogBox(caught.getMessage()).show();
			}

			@Override
			public void onSuccess(Void result)
			{
				loadExpectedTablesList();
			}
		});
	}

	/**
	 * Edits the calculator for specific table button pressed.
	 */
	protected void editCalculatorForSpecificTableButtonPressed()
	{
		final String tableSelected = tablesOnline.getDeviceSelected();
		if (tableSelected == null)
		{
			return;
		}

		NumberNetService.Util.getInstance().getCalculatorAllKeyStatesForTable(tableSelected, new AsyncCallback<Map<CalculatorKey, Boolean>>()
		{
			@Override
			public void onFailure(Throwable caught)
			{
				new MessageDialogBox(caught.getMessage()).show();
			}

			@Override
			public void onSuccess(Map<CalculatorKey, Boolean> result)
			{
				final EditCalculatorForTablePanel editor = new EditCalculatorForTablePanel();
				editor.setTableName(tableSelected);
				CalculatorSync syncer = new CalculatorSync(tableSelected);
				editor.setCalculatorKeyControlPanelDelegate(syncer);
				NumberNetUI.get().addToTabPanel(editor, "Edit calculators (" + tableSelected + ")");
				editor.setValuesForCheckBoxesWithKeyStateInfo(result);
			}
		});

	}

	/**
	 * Load expected tables list.
	 */
	protected void loadExpectedTablesList()
	{
		NumberNetService.Util.getInstance().getExpectedTables(new AsyncCallback<List<String>>()
		{
			@Override
			public void onFailure(Throwable caught)
			{
				new MessageDialogBox(caught.getMessage()).show();
			}

			@Override
			public void onSuccess(List<String> result)
			{
				listBoxExpectedTables.removeAllItems();
				for (String name : result)
				{
					listBoxExpectedTables.addItem(name);
				}
			}
		});

	}

	/**
	 * Open table tab.
	 *
	 * @param table
	 *            the table
	 */
	protected void openTableTab(String table)
	{
		ParticipantManager pm = new ParticipantManager();
		pm.setTable(table);
		NumberNetUI.get().addToTabPanel(pm, table + " participants");
	}

	/**
	 * Removes the expected table.
	 */
	protected void removeExpectedTable()
	{
		// TODO: unimplemented as yet
	}
}
