package synergynet3.web.client;

import java.util.HashMap;
import java.util.Map;

import synergynet3.activitypack1.web.client.ActivityPack1UI;
import synergynet3.web.apps.numbernet.client.NumberNetUI;
import synergynet3.web.appsystem.client.SynergyNetAppSystemUI;
import synergynet3.web.appsystem.client.SynergyNetAppSystemUI.SynergyNetAppServiceUIDelegate;
import synergynet3.web.appsystem.client.ui.MediaControlUI;
import synergynet3.web.appsystem.client.ui.ProjectorControlPanel;
import synergynet3.web.appsystem.client.ui.dbcontrol.DatabaseControlUI;
import synergynet3.web.client.service.SynergyNetWebService;
import synergynet3.web.earlyyears.client.EarlyYearsUI;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * The Class SynergyNetWebEntryPoint.
 */
public class SynergyNetWebEntryPoint implements EntryPoint, SynergyNetAppServiceUIDelegate
{

	/** The activity pack1 ui. */
	private ActivityPack1UI activityPack1UI;

	/** The earlyyears ui. */
	private EarlyYearsUI earlyyearsUI;

	/** The numbernet ui. */
	private NumberNetUI numbernetUI;

	/** The tab panel. */
	private TabPanel tabPanel;

	/** The has close button. */
	HashMap<VerticalPanel, Boolean> hasCloseButton = new HashMap<VerticalPanel, Boolean>();

	/**
	 * Adds the to tab panel.
	 *
	 * @param vp
	 *            the vp
	 * @param title
	 *            the title
	 */
	public void addToTabPanel(VerticalPanel vp, String title)
	{
		tabPanel.add(vp, title);
		int index = tabPanel.getWidgetIndex(vp);
		addCloseButtonToPanel(vp, index);
		tabPanel.selectTab(index);
	}

	/*
	 * (non-Javadoc)
	 * @see com.google.gwt.core.client.EntryPoint#onModuleLoad()
	 */
	@Override
	public void onModuleLoad()
	{

		Map<String, String> availableAppNamesToClassName = new HashMap<String, String>();
		availableAppNamesToClassName.put("Number Net", "synergynet3.apps.numbernet.NumberNet");
		availableAppNamesToClassName.put("Gravity Sim", "synergynet3.activitypack1.table.gravitysim.GravitySim");
		availableAppNamesToClassName.put("Early Years", "synergynet3.apps.earlyyears.applications.stickerbook.StickerbookApp");

		SynergyNetAppSystemUI appSystemUI = new SynergyNetAppSystemUI();
		appSystemUI.setDelegate(this);
		appSystemUI.setKnownApplicationNamesWithClasses(availableAppNamesToClassName);

		ProjectorControlPanel projectorControlUI = new ProjectorControlPanel();

		DatabaseControlUI databaseControlUI = new DatabaseControlUI();
		databaseControlUI.setDelegate(this);

		MediaControlUI mediaControlUI = new MediaControlUI();

		final RootPanel rootPanel = RootPanel.get();

		tabPanel = new TabPanel();
		tabPanel.add(appSystemUI, "Apps");
		tabPanel.add(projectorControlUI, "Projectors");
		tabPanel.add(mediaControlUI, "Tables");
		tabPanel.add(databaseControlUI, "Students");

		rootPanel.add(tabPanel);
		tabPanel.selectTab(0);

		SynergyNetWebService.Util.getInstance().test("", new AsyncCallback<Void>()
		{

			@Override
			public void onFailure(Throwable caught)
			{
				// System.out.println("unwoot");

			}

			@Override
			public void onSuccess(Void result)
			{
				// System.out.println("woot");
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.web.appsystem.client.SynergyNetAppSystemUI.
	 * SynergyNetAppServiceUIDelegate#shouldHideDBControls()
	 */
	@Override
	public void shouldHideDBControls()
	{
		tabPanel.selectTab(0);
		tabPanel.remove(1);
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.web.appsystem.client.SynergyNetAppSystemUI.
	 * SynergyNetAppServiceUIDelegate
	 * #shouldOpenManagementUIForClassName(java.lang.String)
	 */
	@Override
	public void shouldOpenManagementUIForClassName(String appClass)
	{
		if (appClass == null)
		{
			return;
		}
		if (appClass.equals("synergynet3.apps.numbernet.NumberNet"))
		{
			addToTabPanel(getNumberNetUI(), "NumberNet");
			showLastTab(tabPanel);
		}
		else if (appClass.equals("synergynet3.activitypack1.table.gravitysim.GravitySim"))
		{
			ActivityPack1UI apui = getActivityPack1UI("gravity");
			addToTabPanel(apui, "Gravity Sim");
			showLastTab(tabPanel);
		}
		else if (appClass.equals("synergynet3.apps.earlyyears.applications.stickerbook.StickerbookApp"))
		{
			addToTabPanel(getEarlyYearsUI(), "Early Years");
			showLastTab(tabPanel);
		}
	}

	/**
	 * Adds the close button to panel.
	 *
	 * @param vp
	 *            the vp
	 * @param tabIndex
	 *            the tab index
	 */
	private void addCloseButtonToPanel(VerticalPanel vp, final int tabIndex)
	{
		if (!hasCloseButton.containsKey(vp))
		{
			hasCloseButton.put(vp, false);
		}
		if (!hasCloseButton.get(vp))
		{
			Button b = new Button("Close Tab");
			b.addClickHandler(new ClickHandler()
			{
				@Override
				public void onClick(ClickEvent event)
				{
					tabPanel.selectTab(0);
					tabPanel.remove(tabIndex);
				}
			});
			vp.add(b);
			hasCloseButton.put(vp, true);
		}
	}

	/**
	 * Gets the activity pack1 ui.
	 *
	 * @param app
	 *            the app
	 * @return the activity pack1 ui
	 */
	private ActivityPack1UI getActivityPack1UI(String app)
	{
		if (activityPack1UI == null)
		{
			activityPack1UI = new ActivityPack1UI(app);
		}
		return activityPack1UI;
	}

	/**
	 * Gets the early years ui.
	 *
	 * @return the early years ui
	 */
	private EarlyYearsUI getEarlyYearsUI()
	{
		if (earlyyearsUI == null)
		{
			earlyyearsUI = new EarlyYearsUI();
		}
		return earlyyearsUI;
	}

	/**
	 * Gets the number net ui.
	 *
	 * @return the number net ui
	 */
	private VerticalPanel getNumberNetUI()
	{
		if (numbernetUI == null)
		{
			numbernetUI = new NumberNetUI();
		}
		return numbernetUI;
	}

	/**
	 * Show last tab.
	 *
	 * @param tabPanel2
	 *            the tab panel2
	 */
	private void showLastTab(TabPanel tabPanel2)
	{
		tabPanel.selectTab(tabPanel.getWidgetCount() - 1);
	}
}
