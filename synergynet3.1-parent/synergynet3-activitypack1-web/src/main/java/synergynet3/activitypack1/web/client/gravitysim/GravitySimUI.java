package synergynet3.activitypack1.web.client.gravitysim;

import synergynet3.activitypack1.web.client.service.GravitySimService;
import synergynet3.activitypack1.web.shared.UniverseScenario;
import synergynet3.web.commons.client.dialogs.MessageDialogBox;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * The Class GravitySimUI.
 */
public class GravitySimUI extends VerticalPanel
{

	/**
	 * Instantiates a new gravity sim ui.
	 */
	public GravitySimUI()
	{
		super();

		DisclosurePanel pnlScenarios = new DisclosurePanel("Scenarios");
		pnlScenarios.setOpen(true);
		add(pnlScenarios);
		pnlScenarios.setWidth("400px");

		VerticalPanel verticalPanel_1 = new VerticalPanel();
		pnlScenarios.setContent(verticalPanel_1);
		verticalPanel_1.setSize("354px", "4cm");

		CaptionPanel cptnpnlSunInThe = new CaptionPanel("Sun in the middle");
		verticalPanel_1.add(cptnpnlSunInThe);

		VerticalPanel verticalPanel_2 = new VerticalPanel();
		cptnpnlSunInThe.setContentWidget(verticalPanel_2);
		verticalPanel_2.setSize("300px", "59px");

		Label lblAfafa = new Label("Sun in the middle. Moons on touch");
		verticalPanel_2.add(lblAfafa);

		Button btnSunAndMoons = new Button("Activate");
		btnSunAndMoons.addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(ClickEvent event)
			{
				GravitySimService.Util.get().setScenario(UniverseScenario.SUN_AND_MOONS, new AsyncCallback<Void>()
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
		});
		verticalPanel_2.add(btnSunAndMoons);

		CaptionPanel cptnpnlJustMoons = new CaptionPanel("Just moons");
		verticalPanel_1.add(cptnpnlJustMoons);
		cptnpnlJustMoons.setWidth("231px");

		VerticalPanel verticalPanel_3 = new VerticalPanel();
		cptnpnlJustMoons.setContentWidget(verticalPanel_3);
		verticalPanel_3.setSize("5cm", "56px");

		Label lblMoonsOnly = new Label("Moons only.");
		verticalPanel_3.add(lblMoonsOnly);

		Button btnMoonsonly = new Button("Activate");
		btnMoonsonly.addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(ClickEvent event)
			{
				GravitySimService.Util.get().setScenario(UniverseScenario.MOONS_ONLY, new AsyncCallback<Void>()
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
		});
		verticalPanel_3.add(btnMoonsonly);

		CaptionPanel cptnpnlBinaryStarSystem = new CaptionPanel("Binary Star System");
		verticalPanel_1.add(cptnpnlBinaryStarSystem);

		VerticalPanel verticalPanel_5 = new VerticalPanel();
		cptnpnlBinaryStarSystem.setContentWidget(verticalPanel_5);
		verticalPanel_5.setSize("5cm", "48px");

		Label lblTwoStarsIn = new Label("Two stars in circular co-orbit");
		verticalPanel_5.add(lblTwoStarsIn);

		Button btnBinaryStarScenario = new Button("Activate");
		btnBinaryStarScenario.addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(ClickEvent event)
			{
				GravitySimService.Util.get().setScenario(UniverseScenario.BINARY_STAR_SYSTEM, new AsyncCallback<Void>()
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
		});
		verticalPanel_5.add(btnBinaryStarScenario);

		DisclosurePanel pnlAdjustments = new DisclosurePanel("Adjustments");
		pnlAdjustments.setOpen(true);
		add(pnlAdjustments);
		pnlAdjustments.setWidth("380px");

		VerticalPanel verticalPanel_4 = new VerticalPanel();
		pnlAdjustments.setContent(verticalPanel_4);
		verticalPanel_4.setSize("344px", "4cm");

		Button btnClearAllBodies = new Button("Clear All Bodies");
		btnClearAllBodies.addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(ClickEvent event)
			{
				GravitySimService.Util.get().clearAllBodies(new AsyncCallback<Void>()
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
		});
		verticalPanel_4.add(btnClearAllBodies);

		HorizontalPanel horizontalPanel_1 = new HorizontalPanel();
		horizontalPanel_1.setSpacing(5);
		verticalPanel_4.add(horizontalPanel_1);

		Button btnIncreaseGravity = new Button("Increase Gravity");
		btnIncreaseGravity.addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(ClickEvent event)
			{
				GravitySimService.Util.get().increaseGravity(new AsyncCallback<Void>()
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
		});
		horizontalPanel_1.add(btnIncreaseGravity);

		Button btnDecreaseGravity = new Button("Decrease Gravity");
		btnDecreaseGravity.addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(ClickEvent event)
			{
				GravitySimService.Util.get().decreaseGravity(new AsyncCallback<Void>()
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
		});
		horizontalPanel_1.add(btnDecreaseGravity);

		HorizontalPanel horizontalPanel_2 = new HorizontalPanel();
		horizontalPanel_2.setSpacing(5);
		verticalPanel_4.add(horizontalPanel_2);

		Button btnIncreaseSimulationSpeed = new Button("Increase Sim Speed");
		btnIncreaseSimulationSpeed.addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(ClickEvent event)
			{
				GravitySimService.Util.get().increaseSimulationSpeed(new AsyncCallback<Void>()
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
		});

		horizontalPanel_2.add(btnIncreaseSimulationSpeed);

		Button btnDecreaseSimSpeed = new Button("Decrease Sim Speed");
		btnDecreaseSimSpeed.addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(ClickEvent event)
			{
				GravitySimService.Util.get().decreaseSimulationSpeed(new AsyncCallback<Void>()
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
		});
		horizontalPanel_2.add(btnDecreaseSimSpeed);

		HorizontalPanel horizontalPanel_3 = new HorizontalPanel();
		horizontalPanel_3.setSpacing(5);
		verticalPanel_4.add(horizontalPanel_3);

		Label lblLimitBodies = new Label("Limit Bodies:");
		horizontalPanel_3.add(lblLimitBodies);

		Button buttonLimit4 = new Button("4");
		buttonLimit4.addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(ClickEvent event)
			{
				GravitySimService.Util.get().setBodyLimit(4, new AsyncCallback<Void>()
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
		});
		horizontalPanel_3.add(buttonLimit4);

		Button btnLimit10 = new Button("10");
		btnLimit10.addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(ClickEvent event)
			{
				GravitySimService.Util.get().setBodyLimit(10, new AsyncCallback<Void>()
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
		});
		horizontalPanel_3.add(btnLimit10);

		Button btnLimit100 = new Button("100");
		btnLimit100.addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(ClickEvent event)
			{
				GravitySimService.Util.get().setBodyLimit(100, new AsyncCallback<Void>()
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
		});
		horizontalPanel_3.add(btnLimit100);

		Button btnLimit1000 = new Button("1000");
		btnLimit1000.addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(ClickEvent event)
			{
				GravitySimService.Util.get().setBodyLimit(1000, new AsyncCallback<Void>()
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
		});
		horizontalPanel_3.add(btnLimit1000);
	}
}
