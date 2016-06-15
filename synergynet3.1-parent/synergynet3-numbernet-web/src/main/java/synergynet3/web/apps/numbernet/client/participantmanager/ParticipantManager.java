package synergynet3.web.apps.numbernet.client.participantmanager;

import java.util.List;

import synergynet3.web.apps.numbernet.client.service.NumberNetService;
import synergynet3.web.apps.numbernet.shared.Participant;
import synergynet3.web.commons.client.dialogs.MessageDialogBox;
import synergynet3.web.commons.client.ui.FixedSizeScrollableListBox;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * The Class ParticipantManager.
 */
public class ParticipantManager extends VerticalPanel
{

	/** The list box. */
	private FixedSizeScrollableListBox listBox;

	/** The table. */
	private String table;

	/** The txt unique name. */
	private TextBox txtUniqueName;

	/**
	 * Instantiates a new participant manager.
	 */
	public ParticipantManager()
	{
		setSpacing(5);

		Label lblParticipantManager = new Label("Participant Manager");
		add(lblParticipantManager);

		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.setSpacing(10);
		add(horizontalPanel);

		Label lblUniqueName = new Label("Unique name:");
		horizontalPanel.add(lblUniqueName);

		txtUniqueName = new TextBox();
		horizontalPanel.add(txtUniqueName);

		Button btnAddName = new Button("Add Name");
		btnAddName.addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(ClickEvent event)
			{
				addName();
			}
		});
		horizontalPanel.add(btnAddName);

		HorizontalPanel horizontalPanel_1 = new HorizontalPanel();
		horizontalPanel_1.setSpacing(5);
		add(horizontalPanel_1);
		horizontalPanel_1.setWidth("333px");

		listBox = new FixedSizeScrollableListBox();
		horizontalPanel_1.add(listBox);
		horizontalPanel_1.setCellHorizontalAlignment(listBox, HasHorizontalAlignment.ALIGN_RIGHT);
		listBox.setSize("216px", "100px");

		Button btnRemoveName = new Button("Remove");
		btnRemoveName.addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(ClickEvent event)
			{
				removeSelectedName();
			}
		});
		horizontalPanel_1.add(btnRemoveName);
		horizontalPanel_1.setCellVerticalAlignment(btnRemoveName, HasVerticalAlignment.ALIGN_BOTTOM);

	}

	/**
	 * Sets the table.
	 *
	 * @param table
	 *            the new table
	 */
	public void setTable(String table)
	{
		this.table = table;
		loadParticipantList();
	}

	/**
	 * Adds the name.
	 */
	protected void addName()
	{
		String name = txtUniqueName.getText();
		NumberNetService.Util.getInstance().addNameToTable(name, table, new AsyncCallback<Void>()
		{

			@Override
			public void onFailure(Throwable caught)
			{
				new MessageDialogBox(caught.getMessage()).show();
			}

			@Override
			public void onSuccess(Void result)
			{
				loadParticipantList();
				txtUniqueName.setText("");
				txtUniqueName.setFocus(true);
			}
		});
	}

	/**
	 * Load participant list.
	 */
	protected void loadParticipantList()
	{
		NumberNetService.Util.getInstance().getNamesForTable(table, new AsyncCallback<List<Participant>>()
		{

			@Override
			public void onFailure(Throwable caught)
			{
				new MessageDialogBox(caught.getMessage()).show();
			}

			@Override
			public void onSuccess(List<Participant> result)
			{
				listBox.removeAllItems();
				for (Participant p : result)
				{
					listBox.addItem(p.getName());
				}
			}
		});
	}

	/**
	 * Removes the selected name.
	 */
	protected void removeSelectedName()
	{
		String selectedName = listBox.getSelectedItem();
		if (selectedName == null)
		{
			return;
		}
		NumberNetService.Util.getInstance().removeFromTable(selectedName, table, new AsyncCallback<Void>()
		{

			@Override
			public void onFailure(Throwable caught)
			{
				new MessageDialogBox(caught.getMessage()).show();
			}

			@Override
			public void onSuccess(Void result)
			{
				loadParticipantList();
			}
		});
	}

}
