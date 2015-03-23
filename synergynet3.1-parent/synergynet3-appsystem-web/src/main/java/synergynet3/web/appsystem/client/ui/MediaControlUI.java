package synergynet3.web.appsystem.client.ui;

import synergynet3.web.appsystem.client.service.SynergyNetAppSystemService;
import synergynet3.web.commons.client.devicesonline.DevicesOnlineWidget;
import synergynet3.web.commons.client.dialogs.MessageDialogBox;
import synergynet3.web.shared.DevicesSelected;
import synergynet3.web.shared.messages.PerformActionMessage;
import synergynet3.web.shared.messages.PerformActionMessage.MESSAGESTATE;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * The Class MediaControlUI.
 */
public class MediaControlUI extends VerticalPanel {

	/** The devices online. */
	private DevicesOnlineWidget devicesOnline;

	/**
	 * Instantiates a new media control ui.
	 */
	public MediaControlUI() {
		setTitle("Media Control");
		setSpacing(5);

		DisclosurePanel tableSelectionDisclosurePanel = new DisclosurePanel(
				"Tables Online");
		add(tableSelectionDisclosurePanel);
		tableSelectionDisclosurePanel.setOpen(true);
		tableSelectionDisclosurePanel.setWidth("270px");

		devicesOnline = new DevicesOnlineWidget();
		tableSelectionDisclosurePanel.setContent(devicesOnline);
		devicesOnline.setMultipleSelectionAllowed(true);
		devicesOnline.setSize("270px", "100px");
		devicesOnline.setDeviceType("tables");
		devicesOnline.setAllTablesCheckBoxOffset("120px");
		devicesOnline.setAllTablesCheckOptionEnabled(true);
		devicesOnline.updateList();

		mainButtons();
		networkFlickButtons();
		reloadContentsControls();
		additionalControls();
	}

	/**
	 * Additional controls.
	 */
	private void additionalControls() {
		DisclosurePanel bringStudentsToTopPanel = new DisclosurePanel(
				"Additional controls");
		bringStudentsToTopPanel.setOpen(false);
		add(bringStudentsToTopPanel);
		bringStudentsToTopPanel.setWidth("270px");

		VerticalPanel tidyContentsVerticalPanel = new VerticalPanel();
		bringStudentsToTopPanel.setContent(tidyContentsVerticalPanel);
		tidyContentsVerticalPanel.setSize("5cm", "");

		HorizontalPanel bringStudentsToTopHorizontalPanel = new HorizontalPanel();
		bringStudentsToTopHorizontalPanel.setSpacing(5);
		tidyContentsVerticalPanel.add(bringStudentsToTopHorizontalPanel);
		bringStudentsToTopHorizontalPanel.setWidth("262px");

		Button bringStudentsToTopButton = new Button(
				"Bring student icons to top");
		bringStudentsToTopButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				SynergyNetAppSystemService.Util.get().bringStudentsToTop(
						new PerformActionMessage(MESSAGESTATE.ACTIVATE),
						getDeviceToSendTo(), new AsyncCallback<Void>() {
							@Override
							public void onFailure(Throwable caught) {
								new MessageDialogBox(
										"Communication error when bringing students to the top: "
												+ caught.getMessage()).show();
							}

							@Override
							public void onSuccess(Void result) {
							}
						});
			}
		});
		bringStudentsToTopButton.setWidth("272px");
		bringStudentsToTopHorizontalPanel.add(bringStudentsToTopButton);

		HorizontalPanel screenshotHorizontalPanel = new HorizontalPanel();
		screenshotHorizontalPanel.setSpacing(5);
		tidyContentsVerticalPanel.add(screenshotHorizontalPanel);
		screenshotHorizontalPanel.setWidth("262px");

		Button takeScreenshotButton = new Button("Take Screenshot");
		takeScreenshotButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				SynergyNetAppSystemService.Util.get().takeScreenshot(
						new PerformActionMessage(MESSAGESTATE.ACTIVATE),
						getDeviceToSendTo(), new AsyncCallback<Void>() {
							@Override
							public void onFailure(Throwable caught) {
								new MessageDialogBox(
										"Communication error when taking screenshot: "
												+ caught.getMessage()).show();
							}

							@Override
							public void onSuccess(Void result) {
							}
						});
			}
		});
		takeScreenshotButton.setWidth("272px");
		screenshotHorizontalPanel.add(takeScreenshotButton);
	}

	/**
	 * Gets the device to send to.
	 *
	 * @return the device to send to
	 */
	private String[] getDeviceToSendTo() {
		String[] toReturn;
		if (devicesOnline.getAllDevicesOptionCheck()) {
			toReturn = new String[1];
			toReturn[0] = DevicesSelected.ALL_TABLES_ID;
		} else {
			toReturn = new String[devicesOnline.getDevicesSelected().size()];
			devicesOnline.getDevicesSelected().toArray(toReturn);
		}
		return toReturn;
	}

	/**
	 * Main buttons.
	 */
	private void mainButtons() {
		HorizontalPanel removeAdditionalContentHorizontalPanel = new HorizontalPanel();
		removeAdditionalContentHorizontalPanel.setSpacing(5);
		add(removeAdditionalContentHorizontalPanel);
		removeAdditionalContentHorizontalPanel.setWidth("280px");

		Button removeAdditionalContentButton = new Button("Clear content");
		removeAdditionalContentButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				SynergyNetAppSystemService.Util.get().removeAdditionalContent(
						new PerformActionMessage(MESSAGESTATE.ACTIVATE),
						getDeviceToSendTo(), new AsyncCallback<Void>() {
							@Override
							public void onFailure(Throwable caught) {
								new MessageDialogBox(
										"Communication error when removing additional content: "
												+ caught.getMessage()).show();
							}

							@Override
							public void onSuccess(Void result) {
							}
						});
			}
		});
		removeAdditionalContentButton.setWidth("280px");
		removeAdditionalContentHorizontalPanel
				.add(removeAdditionalContentButton);

		HorizontalPanel freezeContentHorizontalPanel = new HorizontalPanel();
		freezeContentHorizontalPanel.setSpacing(5);
		add(freezeContentHorizontalPanel);
		freezeContentHorizontalPanel.setWidth("280px");

		Button freezeAdditionalContentButton = new Button("Freeze/Unfreeze");
		freezeAdditionalContentButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				SynergyNetAppSystemService.Util.get().toggleFreeze(
						new PerformActionMessage(MESSAGESTATE.ACTIVATE),
						getDeviceToSendTo(), new AsyncCallback<Void>() {
							@Override
							public void onFailure(Throwable caught) {
								new MessageDialogBox(
										"Communication error when removing additional content: "
												+ caught.getMessage()).show();
							}

							@Override
							public void onSuccess(Void result) {
							}
						});
			}
		});
		freezeAdditionalContentButton.setWidth("280px");
		freezeContentHorizontalPanel.add(freezeAdditionalContentButton);
	}

	/**
	 * Network flick buttons.
	 */
	private void networkFlickButtons() {
		DisclosurePanel networkFlickPanel = new DisclosurePanel(
				"Network Flick Control");
		networkFlickPanel.setOpen(false);
		add(networkFlickPanel);
		networkFlickPanel.setWidth("270px");

		VerticalPanel verticalNetworkFlickPanel = new VerticalPanel();
		networkFlickPanel.setContent(verticalNetworkFlickPanel);
		verticalNetworkFlickPanel.setSize("5cm", "");

		HorizontalPanel horizontalNetworkFlickEnablePanel = new HorizontalPanel();
		horizontalNetworkFlickEnablePanel.setSpacing(5);
		verticalNetworkFlickPanel.add(horizontalNetworkFlickEnablePanel);
		horizontalNetworkFlickEnablePanel.setWidth("262px");

		Button enableNetworkFlickButton = new Button("Enable Network Flick");
		enableNetworkFlickButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				SynergyNetAppSystemService.Util.get().setNetworkFlick(
						new PerformActionMessage(MESSAGESTATE.ACTIVATE),
						getDeviceToSendTo(), new AsyncCallback<Void>() {
							@Override
							public void onFailure(Throwable caught) {
								new MessageDialogBox(
										"Communication error when enabling network flick: "
												+ caught.getMessage()).show();
							}

							@Override
							public void onSuccess(Void result) {
							}
						});
			}
		});
		enableNetworkFlickButton.setWidth("272px");
		horizontalNetworkFlickEnablePanel.add(enableNetworkFlickButton);

		HorizontalPanel horizontalNetworkFlickDisablePanel = new HorizontalPanel();
		horizontalNetworkFlickDisablePanel.setSpacing(5);
		verticalNetworkFlickPanel.add(horizontalNetworkFlickDisablePanel);
		horizontalNetworkFlickDisablePanel.setWidth("262px");

		Button disableNetworkFlickButton = new Button("Disable Network Flick");
		disableNetworkFlickButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				SynergyNetAppSystemService.Util.get().setNetworkFlick(
						new PerformActionMessage(MESSAGESTATE.DEACTIVATE),
						getDeviceToSendTo(), new AsyncCallback<Void>() {
							@Override
							public void onFailure(Throwable caught) {
								new MessageDialogBox(
										"Communication error when disabling network flick: "
												+ caught.getMessage()).show();
							}

							@Override
							public void onSuccess(Void result) {
							}
						});
			}
		});
		disableNetworkFlickButton.setWidth("272px");
		horizontalNetworkFlickDisablePanel.add(disableNetworkFlickButton);
	}

	/**
	 * Reload contents controls.
	 */
	private void reloadContentsControls() {
		DisclosurePanel reloadPanel = new DisclosurePanel(
				"Reload contents for selected tables");
		reloadPanel.setOpen(false);
		add(reloadPanel);
		reloadPanel.setWidth("270px");

		VerticalPanel verticalReloadPanel = new VerticalPanel();
		reloadPanel.setContent(verticalReloadPanel);
		verticalReloadPanel.setSize("5cm", "");

		HorizontalPanel horizontalReloadServerContentsPanel = new HorizontalPanel();
		horizontalReloadServerContentsPanel.setSpacing(5);
		verticalReloadPanel.add(horizontalReloadServerContentsPanel);
		horizontalReloadServerContentsPanel.setWidth("262px");

		Button serverReloadButton = new Button("Reload Contents From Server");
		serverReloadButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				SynergyNetAppSystemService.Util.get().reloadServerContents(
						new PerformActionMessage(MESSAGESTATE.ACTIVATE),
						getDeviceToSendTo(), new AsyncCallback<Void>() {
							@Override
							public void onFailure(Throwable caught) {
								new MessageDialogBox(
										"Communication error when reloading server contents: "
												+ caught.getMessage()).show();
							}

							@Override
							public void onSuccess(Void result) {
							}
						});
			}
		});
		serverReloadButton.setWidth("272px");
		horizontalReloadServerContentsPanel.add(serverReloadButton);

		HorizontalPanel horizontalReloadRemovableMediaContentsPanel = new HorizontalPanel();
		horizontalReloadRemovableMediaContentsPanel.setSpacing(5);
		verticalReloadPanel.add(horizontalReloadRemovableMediaContentsPanel);
		horizontalReloadRemovableMediaContentsPanel.setWidth("262px");

		Button removableDriveReloadButton = new Button(
				"Reload contents from removable media");
		removableDriveReloadButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				SynergyNetAppSystemService.Util
						.get()
						.reloadRemovableDriveContents(
								new PerformActionMessage(MESSAGESTATE.ACTIVATE),
								getDeviceToSendTo(), new AsyncCallback<Void>() {
									@Override
									public void onFailure(Throwable caught) {
										new MessageDialogBox(
												"Communication error when reloading removable media contents: "
														+ caught.getMessage())
												.show();
									}

									@Override
									public void onSuccess(Void result) {
									}
								});
			}
		});
		removableDriveReloadButton.setWidth("272px");
		horizontalReloadRemovableMediaContentsPanel
				.add(removableDriveReloadButton);
	}

}
