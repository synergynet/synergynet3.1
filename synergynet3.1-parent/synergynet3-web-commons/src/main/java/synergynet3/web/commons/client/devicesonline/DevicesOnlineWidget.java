package synergynet3.web.commons.client.devicesonline;

import java.util.ArrayList;
import java.util.List;

import synergynet3.web.commons.client.service.SynergyNetWebCommonsService;
import synergynet3.web.commons.client.ui.FixedSizeScrollableListBox;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * The Class DevicesOnlineWidget.
 */
public class DevicesOnlineWidget extends VerticalPanel
{

	/**
	 * The Interface DevicesOnlineListBoxDelegate.
	 */
	public static interface DevicesOnlineListBoxDelegate
	{

		/**
		 * Devices online list box did reload.
		 */
		public void devicesOnlineListBoxDidReload();

		/**
		 * Devices online list box reload failed.
		 *
		 * @param caught
		 *            the caught
		 */
		public void devicesOnlineListBoxReloadFailed(Throwable caught);

		/**
		 * Devices selected.
		 *
		 * @param device
		 *            the device
		 */
		public void devicesSelected(List<String> device);

		/**
		 * No devices selected.
		 */
		public void noDevicesSelected();
	}

	/** The all tables. */
	private CheckBox allTables;

	/** The all tables offset. */
	private String allTablesOffset = "50px";

	/** The btn refresh. */
	private Button btnRefresh;

	/** The buffer label. */
	private Label bufferLabel;

	/** The buttons panel. */
	private HorizontalPanel buttonsPanel;

	/** The delegate. */
	private DevicesOnlineListBoxDelegate delegate;

	/** The device type. */
	private String deviceType = "tables";

	/** The list box. */
	private FixedSizeScrollableListBox listBox;

	/** The tables present. */
	private List<String> tablesPresent = new ArrayList<String>();

	/**
	 * Instantiates a new devices online widget.
	 */
	public DevicesOnlineWidget()
	{
		super();

		listBox = new FixedSizeScrollableListBox();
		listBox.addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(ClickEvent event)
			{
				List<String> itemsSelected = listBox.getSelectedItems();
				if (delegate != null)
				{
					delegate.devicesSelected(itemsSelected);
				}
			}
		});

		add(listBox);

		buttonsPanel = new HorizontalPanel();

		btnRefresh = new Button("Refresh");
		btnRefresh.addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(ClickEvent event)
			{
				loadOnlineTableList();
			}
		});
		buttonsPanel.add(btnRefresh);

		bufferLabel = new Label();
		bufferLabel.setWidth("0px");
		buttonsPanel.add(bufferLabel);

		allTables = new CheckBox("Select All");
		allTables.setVisible(false);
		allTables.setEnabled(false);
		buttonsPanel.add(allTables);

		add(buttonsPanel);
	}

	/**
	 * Gets the all devices option check.
	 *
	 * @return the all devices option check
	 */
	public boolean getAllDevicesOptionCheck()
	{
		return allTables.getValue();
	}

	/**
	 * Gets the buttons panel.
	 *
	 * @return the buttons panel
	 */
	public HorizontalPanel getButtonsPanel()
	{
		return this.buttonsPanel;
	}

	/**
	 * Gets the device selected.
	 *
	 * @return the device selected
	 */
	public String getDeviceSelected()
	{
		List<String> devices = getDevicesSelected();
		if (devices.size() < 1)
		{
			return null;
		}
		return devices.get(0);
	}

	/**
	 * Gets the devices present.
	 *
	 * @return the devices present
	 */
	public List<String> getDevicesPresent()
	{
		return tablesPresent;
	}

	/**
	 * Gets the devices selected.
	 *
	 * @return the devices selected
	 */
	public List<String> getDevicesSelected()
	{
		return listBox.getSelectedItems();
	}

	/**
	 * Gets the refresh button.
	 *
	 * @return the refresh button
	 */
	public Button getRefreshButton()
	{
		return btnRefresh;
	}

	/**
	 * Sets the all tables check box offset.
	 *
	 * @param allTablesOffset
	 *            the new all tables check box offset
	 */
	public void setAllTablesCheckBoxOffset(String allTablesOffset)
	{
		this.allTablesOffset = allTablesOffset;
	}

	/**
	 * Sets the all tables check option enabled.
	 *
	 * @param allTablesSelect
	 *            the new all tables check option enabled
	 */
	public void setAllTablesCheckOptionEnabled(boolean allTablesSelect)
	{
		allTables.setVisible(allTablesSelect);
		allTables.setEnabled(allTablesSelect);
		if (allTablesSelect)
		{
			bufferLabel.setWidth(allTablesOffset);
		}
		else
		{
			bufferLabel.setWidth("0px");
		}
	}

	/**
	 * Sets the delegate.
	 *
	 * @param delegate
	 *            the new delegate
	 */
	public void setDelegate(DevicesOnlineListBoxDelegate delegate)
	{
		this.delegate = delegate;
	}

	/**
	 * Sets the device type.
	 *
	 * @param deviceType
	 *            the new device type
	 */
	public void setDeviceType(String deviceType)
	{
		this.deviceType = deviceType;
	}

	/**
	 * Sets the list box size.
	 *
	 * @param width
	 *            the width
	 * @param height
	 *            the height
	 */
	public void setListBoxSize(String width, String height)
	{
		listBox.setSize(width, height);
	}

	/**
	 * Sets the multiple selection allowed.
	 *
	 * @param multipleSelect
	 *            the new multiple selection allowed
	 */
	public void setMultipleSelectionAllowed(boolean multipleSelect)
	{
		listBox.setMultipleSelect(multipleSelect);
	}

	/**
	 * Sets the refresh button visibility.
	 *
	 * @param visible
	 *            the new refresh button visibility
	 */
	public void setRefreshButtonVisibility(boolean visible)
	{
		this.btnRefresh.setVisible(visible);
	}

	/*
	 * (non-Javadoc)
	 * @see com.google.gwt.user.client.ui.UIObject#setSize(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void setSize(String width, String height)
	{
		super.setSize(width, height);
		listBox.setSize(width, height);
	}

	/**
	 * Update list.
	 */
	public void updateList()
	{
		loadOnlineTableList();
	}

	/**
	 * Load online table list.
	 */
	protected void loadOnlineTableList()
	{
		SynergyNetWebCommonsService.Util.getInstance().getDevicesCurrentlyOnline(deviceType, new AsyncCallback<List<String>>()
		{
			@Override
			public void onFailure(Throwable caught)
			{
				if (delegate != null)
				{
					delegate.devicesOnlineListBoxReloadFailed(caught);
				}
			}

			@Override
			public void onSuccess(List<String> result)
			{
				listBox.removeAllItems();
				tablesPresent.clear();
				if (result.size() > 0)
				{
					for (int row = 0; row < result.size(); row++)
					{
						listBox.addItem(result.get(row));
						tablesPresent.add(result.get(row));
					}
				}

				if (delegate != null)
				{
					delegate.devicesOnlineListBoxDidReload();
					delegate.noDevicesSelected();
				}

				onRefresh();
			}
		});
	}

	/**
	 * On refresh.
	 */
	protected void onRefresh()
	{
	}
}
