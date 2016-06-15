package synergynet3.web.appsystem.client;

import java.util.List;
import java.util.Map;

import synergynet3.web.appsystem.client.service.SynergyNetAppSystemService;
import synergynet3.web.appsystem.client.ui.ApplicationsAvailableWidget;
import synergynet3.web.commons.client.devicesonline.DevicesOnlineWidget;
import synergynet3.web.commons.client.devicesonline.DevicesOnlineWidget.DevicesOnlineListBoxDelegate;
import synergynet3.web.commons.client.dialogs.MessageDialogBox;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * The Class SynergyNetAppSystemUI.
 */
public class SynergyNetAppSystemUI extends VerticalPanel
{

	/**
	 * The Interface SynergyNetAppServiceUIDelegate.
	 */
	public static interface SynergyNetAppServiceUIDelegate
	{

		/**
		 * Should hide db controls.
		 */
		void shouldHideDBControls();

		/**
		 * Should open management ui for class name.
		 *
		 * @param appClass
		 *            the app class
		 */
		void shouldOpenManagementUIForClassName(String appClass);
	}

	/** The available applications list. */
	private ApplicationsAvailableWidget availableApplicationsList;

	/** The available app names to class name. */
	private Map<String, String> availableAppNamesToClassName;

	/** The btn open management ui. */
	private Button btnOpenManagementUi;

	/** The delegate. */
	private SynergyNetAppServiceUIDelegate delegate;

	/** The dow. */
	private DevicesOnlineWidget dow;

	/**
	 * Instantiates a new synergy net app system ui.
	 */
	public SynergyNetAppSystemUI()
	{
		super();

		this.setSpacing(5);

		dow = new DevicesOnlineWidget();
		dow.setMultipleSelectionAllowed(true);
		dow.setSpacing(5);
		dow.setSize("272px", "100px");
		dow.setDeviceType("tables");
		dow.setDelegate(new DevicesOnlineListBoxDelegate()
		{
			@Override
			public void devicesOnlineListBoxDidReload()
			{
			}

			@Override
			public void devicesOnlineListBoxReloadFailed(Throwable caught)
			{
				new MessageDialogBox("Communication error listing online devices: " + caught.getMessage()).show();
			}

			@Override
			public void devicesSelected(List<String> device)
			{
			}

			@Override
			public void noDevicesSelected()
			{
			}
		});
		dow.setAllTablesCheckBoxOffset("120px");
		dow.setAllTablesCheckOptionEnabled(true);
		dow.updateList();
		add(dow);

		availableApplicationsList = new ApplicationsAvailableWidget();
		availableApplicationsList.setSpacing(5);
		availableApplicationsList.setSize("270px", "100px");
		availableApplicationsList.setShouldAllowSelection(true);
		add(availableApplicationsList);

		Button btnSetActiveApplication = new Button("Set Active Application");
		btnSetActiveApplication.setWidth("272px");
		btnSetActiveApplication.addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(ClickEvent event)
			{
				setActiveApplicationButtonClicked(event);
			}
		});
		availableApplicationsList.getButtonsPanel().add(btnSetActiveApplication);

		btnOpenManagementUi = new Button("Open Management UI");
		btnOpenManagementUi.setWidth("272px");
		btnOpenManagementUi.addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(ClickEvent event)
			{
				openManagementUIButtonClicked();
			}
		});
		availableApplicationsList.add(btnOpenManagementUi);
	}

	/**
	 * Gets the delegate.
	 *
	 * @return the delegate
	 */
	public SynergyNetAppServiceUIDelegate getDelegate()
	{
		return delegate;
	}

	/**
	 * Sets the delegate.
	 *
	 * @param delegate
	 *            the new delegate
	 */
	public void setDelegate(SynergyNetAppServiceUIDelegate delegate)
	{
		this.delegate = delegate;
	}

	/**
	 * Sets the known application names with classes.
	 *
	 * @param availableAppNamesToClassName
	 *            the available app names to class name
	 */
	public void setKnownApplicationNamesWithClasses(Map<String, String> availableAppNamesToClassName)
	{
		this.availableAppNamesToClassName = availableAppNamesToClassName;
		availableApplicationsList.removeAllApplications();
		for (String key : availableAppNamesToClassName.keySet())
		{
			availableApplicationsList.addApplicationName(key);
		}
	}

	/**
	 * Gets the selected application class name.
	 *
	 * @return the selected application class name
	 */
	private String getSelectedApplicationClassName()
	{
		String appNameSelected = availableApplicationsList.getSelectedApplication();
		if (appNameSelected == null)
		{
			return null;
		}
		return availableAppNamesToClassName.get(appNameSelected);
	}

	/**
	 * Open management ui button clicked.
	 */
	protected void openManagementUIButtonClicked()
	{
		String appClass = getSelectedApplicationClassName();
		delegate.shouldOpenManagementUIForClassName(appClass);
	}

	// *** private methods ***

	/**
	 * Sets the active application button clicked.
	 *
	 * @param event
	 *            the new active application button clicked
	 */
	protected void setActiveApplicationButtonClicked(ClickEvent event)
	{
		String appClass = getSelectedApplicationClassName();
		if (appClass == null)
		{
			new MessageDialogBox("Please select at least one application").show();
			return;
		}

		if ((dow.getDevicesSelected().size() < 1) && !dow.getAllDevicesOptionCheck())
		{
			new MessageDialogBox("Please select at least one device").show();
			return;
		}
		List<String> devicesSelected;
		if (dow.getAllDevicesOptionCheck())
		{
			devicesSelected = dow.getDevicesPresent();
		}
		else
		{
			devicesSelected = dow.getDevicesSelected();
		}
		SynergyNetAppSystemService.Util.get().devicesShouldOpenApplication(appClass, devicesSelected, new AsyncCallback<Void>()
		{
			@Override
			public void onFailure(Throwable caught)
			{
				new MessageDialogBox("Communication error setting app: " + caught.getMessage()).show();
			}

			@Override
			public void onSuccess(Void result)
			{
			}
		});
	}

}
