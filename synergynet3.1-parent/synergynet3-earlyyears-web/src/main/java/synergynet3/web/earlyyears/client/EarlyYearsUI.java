package synergynet3.web.earlyyears.client;

import synergynet3.web.commons.client.devicesonline.DevicesOnlineWidget;
import synergynet3.web.commons.client.dialogs.MessageDialogBox;
import synergynet3.web.earlyyears.client.service.EarlyYearsService;
import synergynet3.web.earlyyears.shared.EarlyYearsActivity;
import synergynet3.web.shared.messages.PerformActionMessage;
import synergynet3.web.shared.messages.PerformActionMessage.MESSAGESTATE;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * The Class EarlyYearsUI.
 */
public class EarlyYearsUI extends VerticalPanel
{

	/** The Constant ALL_TABLES_ID. */
	public static final String ALL_TABLES_ID = "ALL TABLES";

	/** The Constant PIECE_LIMIT. */
	public static final int PIECE_LIMIT = 20;

	/** The corner num box. */
	private IntegerBox cornerNumBox;

	/** The cross num box. */
	private IntegerBox crossNumBox;

	/** The devices online. */
	private DevicesOnlineWidget devicesOnline;

	/** The environment explorer settings panel. */
	private DisclosurePanel environmentExplorerSettingsPanel;

	/** The road mode check box. */
	private CheckBox roadModeCheckBox;

	/** The straight num. */
	private IntegerBox straightNum;

	/** The traintrack settings panel. */
	private DisclosurePanel traintrackSettingsPanel;

	/**
	 * Instantiates a new early years ui.
	 */
	public EarlyYearsUI()
	{
		setTitle("Early Years");
		setSpacing(5);

		DisclosurePanel tableSelectionDisclosurePanel = new DisclosurePanel("Tables Online");
		add(tableSelectionDisclosurePanel);
		tableSelectionDisclosurePanel.setOpen(true);
		tableSelectionDisclosurePanel.setWidth("347px");

		devicesOnline = new DevicesOnlineWidget();
		tableSelectionDisclosurePanel.setContent(devicesOnline);
		devicesOnline.setMultipleSelectionAllowed(true);
		devicesOnline.setSize("270px", "100px");
		devicesOnline.setDeviceType("tables");
		devicesOnline.setAllTablesCheckBoxOffset("120px");
		devicesOnline.setAllTablesCheckOptionEnabled(true);
		devicesOnline.updateList();

		DisclosurePanel activityPanel = new DisclosurePanel("Activities");
		activityPanel.setOpen(true);
		add(activityPanel);
		activityPanel.setWidth("400px");

		VerticalPanel verticalActivityPanel = new VerticalPanel();
		activityPanel.setContent(verticalActivityPanel);
		verticalActivityPanel.setSize("5cm", "");

		HorizontalPanel horizontalActivityPanel = new HorizontalPanel();
		horizontalActivityPanel.setSpacing(5);
		verticalActivityPanel.add(horizontalActivityPanel);
		horizontalActivityPanel.setWidth("262px");

		Button stickerbookButton = new Button("Sticker Book");
		stickerbookButton.addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(ClickEvent event)
			{
				EarlyYearsService.Util.get().setActivity(EarlyYearsActivity.STICKER_BOOK, getDeviceToSendTo(), new AsyncCallback<Void>()
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
		stickerbookButton.setWidth("100px");
		horizontalActivityPanel.add(stickerbookButton);

		Button environmentExplorer = new Button("Explorer");
		environmentExplorer.addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(ClickEvent event)
			{
				environmentExplorerSettingsPanel.setOpen(true);
				EarlyYearsService.Util.get().setActivity(EarlyYearsActivity.ENVIRONMENT_EXPLORER, getDeviceToSendTo(), new AsyncCallback<Void>()
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
		environmentExplorer.setWidth("100px");
		horizontalActivityPanel.add(environmentExplorer);

		Button trainButton = new Button("Train Tracks");
		trainButton.addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(ClickEvent event)
			{
				traintrackSettingsPanel.setOpen(true);
				EarlyYearsService.Util.get().setActivity(EarlyYearsActivity.TRAIN_TRACKS, getDeviceToSendTo(), new AsyncCallback<Void>()
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
		trainButton.setWidth("100px");
		horizontalActivityPanel.add(trainButton);

		traintrackSettingsPanel = new DisclosurePanel("Train Track Settings");
		traintrackSettingsPanel.setOpen(false);
		add(traintrackSettingsPanel);
		traintrackSettingsPanel.setWidth("380px");

		VerticalPanel traintrackSettingsVerticalPanel = new VerticalPanel();
		traintrackSettingsPanel.setContent(traintrackSettingsVerticalPanel);
		traintrackSettingsVerticalPanel.setSize("344px", "4cm");

		HorizontalPanel traintrackCornersHorizontalPanel = new HorizontalPanel();
		traintrackCornersHorizontalPanel.setSpacing(5);
		traintrackSettingsVerticalPanel.add(traintrackCornersHorizontalPanel);

		Label lblCornerNum = new Label("Corner Pieces: ");
		lblCornerNum.setWidth("100px");
		traintrackCornersHorizontalPanel.add(lblCornerNum);

		cornerNumBox = new IntegerBox();
		cornerNumBox.setWidth("50px");
		cornerNumBox.setValue(6);
		traintrackCornersHorizontalPanel.add(cornerNumBox);

		cornerNumBox.addKeyUpHandler(new KeyUpHandler()
		{
			@Override
			public void onKeyUp(KeyUpEvent event)
			{
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER)
				{
					cornerUpdate();
				}
			}
		});

		Button buttonCornerDecrease = new Button("Set");
		buttonCornerDecrease.addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(ClickEvent event)
			{
				cornerUpdate();
			}

		});
		traintrackCornersHorizontalPanel.add(buttonCornerDecrease);

		HorizontalPanel traintrackCrossesHorizontalPanel = new HorizontalPanel();
		traintrackCrossesHorizontalPanel.setSpacing(5);
		traintrackSettingsVerticalPanel.add(traintrackCrossesHorizontalPanel);

		Label lblCrossNum = new Label("Cross Pieces: ");
		lblCrossNum.setWidth("100px");
		traintrackCrossesHorizontalPanel.add(lblCrossNum);

		crossNumBox = new IntegerBox();
		crossNumBox.setWidth("50px");
		crossNumBox.setValue(1);
		traintrackCrossesHorizontalPanel.add(crossNumBox);

		crossNumBox.addKeyUpHandler(new KeyUpHandler()
		{
			@Override
			public void onKeyUp(KeyUpEvent event)
			{
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER)
				{
					crossUpdate();
				}
			}
		});

		Button buttonCrossDecrease = new Button("Set");
		buttonCrossDecrease.addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(ClickEvent event)
			{
				crossUpdate();
			}
		});
		traintrackCrossesHorizontalPanel.add(buttonCrossDecrease);

		HorizontalPanel traintrackStraightsHorizontalPanel = new HorizontalPanel();
		traintrackStraightsHorizontalPanel.setSpacing(5);
		traintrackSettingsVerticalPanel.add(traintrackStraightsHorizontalPanel);

		Label lblStraightNum = new Label("Straight Pieces: ");
		lblStraightNum.setWidth("100px");
		traintrackStraightsHorizontalPanel.add(lblStraightNum);

		straightNum = new IntegerBox();
		straightNum.setWidth("50px");
		straightNum.setValue(4);
		traintrackStraightsHorizontalPanel.add(straightNum);

		straightNum.addKeyUpHandler(new KeyUpHandler()
		{
			@Override
			public void onKeyUp(KeyUpEvent event)
			{
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER)
				{
					straightUpdate();
				}
			}
		});

		Button buttonStraightDecrease = new Button("Set");
		buttonStraightDecrease.addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(ClickEvent event)
			{
				straightUpdate();
			}
		});
		traintrackStraightsHorizontalPanel.add(buttonStraightDecrease);

		HorizontalPanel traintrackRoadModeHorizontalPanel = new HorizontalPanel();
		traintrackRoadModeHorizontalPanel.setSpacing(5);
		traintrackSettingsVerticalPanel.add(traintrackRoadModeHorizontalPanel);

		roadModeCheckBox = new CheckBox();
		roadModeCheckBox.setText("Road Mode");
		traintrackRoadModeHorizontalPanel.add(roadModeCheckBox);

		roadModeCheckBox.addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(ClickEvent event)
			{
				roadModeUpdate();
			}
		});

		environmentExplorerSettingsPanel = new DisclosurePanel("Explorer Settings");
		environmentExplorerSettingsPanel.setOpen(false);
		add(environmentExplorerSettingsPanel);
		environmentExplorerSettingsPanel.setWidth("380px");

		VerticalPanel environmentExplorerSettingsVerticalPanel = new VerticalPanel();
		environmentExplorerSettingsPanel.setContent(environmentExplorerSettingsVerticalPanel);
		environmentExplorerSettingsVerticalPanel.setSize("344px", "65px");

		HorizontalPanel horizontalExplorerPanel = new HorizontalPanel();
		horizontalExplorerPanel.setSpacing(5);
		environmentExplorerSettingsVerticalPanel.add(horizontalExplorerPanel);
		horizontalExplorerPanel.setWidth("262px");

		Button showTeacherControlButton = new Button("Show Teacher Controls");
		showTeacherControlButton.addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(ClickEvent event)
			{
				EarlyYearsService.Util.get().showExplorerTeacherConsole(new PerformActionMessage(MESSAGESTATE.ACTIVATE), getDeviceToSendTo(), new AsyncCallback<Void>()
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
		horizontalExplorerPanel.add(showTeacherControlButton);

		Button hideTeacherControlButton = new Button("Hide Teacher Controls");
		hideTeacherControlButton.addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(ClickEvent event)
			{
				EarlyYearsService.Util.get().showExplorerTeacherConsole(new PerformActionMessage(MESSAGESTATE.DEACTIVATE), getDeviceToSendTo(), new AsyncCallback<Void>()
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
		horizontalExplorerPanel.add(hideTeacherControlButton);

	}

	/**
	 * Corner update.
	 */
	private void cornerUpdate()
	{
		Integer toSend = getTrainTrackPiecesNum(cornerNumBox);
		if (toSend != null)
		{
			EarlyYearsService.Util.get().setRailwayCornerNum(toSend, getDeviceToSendTo(), new AsyncCallback<Void>()
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

	/**
	 * Cross update.
	 */
	private void crossUpdate()
	{
		Integer toSend = getTrainTrackPiecesNum(crossNumBox);
		if (toSend != null)
		{
			EarlyYearsService.Util.get().setRailwayCrossNum(toSend, getDeviceToSendTo(), new AsyncCallback<Void>()
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

	/**
	 * Gets the device to send to.
	 *
	 * @return the device to send to
	 */
	private String[] getDeviceToSendTo()
	{
		String[] toReturn;
		if (devicesOnline.getAllDevicesOptionCheck())
		{
			toReturn = new String[1];
			toReturn[0] = ALL_TABLES_ID;
		}
		else
		{
			toReturn = new String[devicesOnline.getDevicesSelected().size()];
			devicesOnline.getDevicesSelected().toArray(toReturn);
		}
		return toReturn;
	}

	/**
	 * Gets the train track pieces num.
	 *
	 * @param box
	 *            the box
	 * @return the train track pieces num
	 */
	private Integer getTrainTrackPiecesNum(IntegerBox box)
	{
		Integer toSend = box.getValue();
		if (toSend == null)
		{
			return null;
		}
		if (toSend < 0)
		{
			toSend = 0;
		}
		else if (toSend > PIECE_LIMIT)
		{
			toSend = PIECE_LIMIT;
		}
		return toSend;
	}

	/**
	 * Road mode update.
	 */
	private void roadModeUpdate()
	{
		PerformActionMessage message = null;
		if (roadModeCheckBox.getValue())
		{
			message = new PerformActionMessage(MESSAGESTATE.ACTIVATE);
		}
		else
		{
			message = new PerformActionMessage(MESSAGESTATE.DEACTIVATE);
		}

		EarlyYearsService.Util.get().setRoadMode(message, getDeviceToSendTo(), new AsyncCallback<Void>()
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

	/**
	 * Straight update.
	 */
	private void straightUpdate()
	{
		Integer toSend = getTrainTrackPiecesNum(straightNum);
		if (toSend != null)
		{
			EarlyYearsService.Util.get().setRailwayStraightNum(toSend, getDeviceToSendTo(), new AsyncCallback<Void>()
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

}
