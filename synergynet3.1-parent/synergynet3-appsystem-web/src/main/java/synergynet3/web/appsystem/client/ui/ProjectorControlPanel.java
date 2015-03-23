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
 * The Class ProjectorControlPanel.
 */
public class ProjectorControlPanel extends VerticalPanel {

	/** The projectors online. */
	private DevicesOnlineWidget projectorsOnline;

	/** The tables online. */
	private DevicesOnlineWidget tablesOnline;

	/**
	 * Instantiates a new projector control panel.
	 */
	public ProjectorControlPanel() {

		setTitle("Projector Control");
		setSpacing(5);

		DisclosurePanel pnlProjectors = new DisclosurePanel("Projectors");
		pnlProjectors.setOpen(true);
		add(pnlProjectors);
		pnlProjectors.setWidth("272px");

		VerticalPanel verticalProjectorPanel = new VerticalPanel();
		pnlProjectors.setContent(verticalProjectorPanel);

		projectorsOnline = new DevicesOnlineWidget();
		pnlProjectors.setContent(projectorsOnline);
		projectorsOnline.setMultipleSelectionAllowed(true);
		projectorsOnline.setSize("270px", "100px");
		projectorsOnline.setDeviceType("projectors");
		projectorsOnline.setAllTablesCheckBoxOffset("120px");
		projectorsOnline.setAllTablesCheckOptionEnabled(true);
		projectorsOnline.updateList();

		DisclosurePanel tableSelectionDisclosurePanel = new DisclosurePanel(
				"Tables");
		add(tableSelectionDisclosurePanel);
		tableSelectionDisclosurePanel.setOpen(true);
		tableSelectionDisclosurePanel.setWidth("272");

		tablesOnline = new DevicesOnlineWidget();
		tableSelectionDisclosurePanel.setContent(tablesOnline);
		tablesOnline.setMultipleSelectionAllowed(true);
		tablesOnline.setSize("270px", "100px");
		tablesOnline.setDeviceType("tables");
		tablesOnline.setAllTablesCheckBoxOffset("120px");
		tablesOnline.setAllTablesCheckOptionEnabled(true);
		tablesOnline.updateList();

		transferButtons();

		manageSelectedProjectors();

		tablesOnline.updateList();

	}

	/**
	 * Gets the projectors to send to.
	 *
	 * @return the projectors to send to
	 */
	private String[] getProjectorsToSendTo() {
		String[] toReturn;
		if (projectorsOnline.getAllDevicesOptionCheck()) {
			toReturn = new String[1];
			toReturn[0] = DevicesSelected.ALL_PROJECTORS_ID;
		} else {
			toReturn = new String[projectorsOnline.getDevicesSelected().size()];
			projectorsOnline.getDevicesSelected().toArray(toReturn);
		}
		return toReturn;
	}

	/**
	 * Gets the tables to send to.
	 *
	 * @return the tables to send to
	 */
	private String[] getTablesToSendTo() {
		String[] toReturn;
		if (tablesOnline.getAllDevicesOptionCheck()) {
			toReturn = new String[1];
			toReturn[0] = DevicesSelected.ALL_TABLES_ID;
		} else {
			toReturn = new String[tablesOnline.getDevicesSelected().size()];
			tablesOnline.getDevicesSelected().toArray(toReturn);
		}
		return toReturn;
	}

	/**
	 * Checks if is at least one projector selected.
	 *
	 * @return true, if is at least one projector selected
	 */
	private boolean isAtLeastOneProjectorSelected() {
		if (projectorsOnline.getDevicesPresent().size() < 1) {
			new MessageDialogBox("No projectors present.").show();
			return false;
		} else if (!projectorsOnline.getAllDevicesOptionCheck()
				&& (projectorsOnline.getDevicesSelected().size() < 1)) {
			new MessageDialogBox("No projector selected.").show();
			return false;
		}

		return true;
	}

	/**
	 * Checks if is at least one table selected.
	 *
	 * @return true, if is at least one table selected
	 */
	private boolean isAtLeastOneTableSelected() {
		if (tablesOnline.getDevicesPresent().size() < 1) {
			new MessageDialogBox("No tables present.").show();
			return false;
		} else if (!tablesOnline.getAllDevicesOptionCheck()
				&& (tablesOnline.getDevicesSelected().size() < 1)) {
			new MessageDialogBox("No table selected.").show();
			return false;
		}
		return true;
	}

	/**
	 * Manage selected projectors.
	 */
	private void manageSelectedProjectors() {
		DisclosurePanel pnlProjectorManagement = new DisclosurePanel(
				"Manage Selected Projector(s)");
		pnlProjectorManagement.setOpen(false);
		add(pnlProjectorManagement);
		pnlProjectorManagement.setWidth("272px");

		VerticalPanel verticalManagementPanel = new VerticalPanel();
		pnlProjectorManagement.setContent(verticalManagementPanel);

		HorizontalPanel horizontalManagementPanel_1 = new HorizontalPanel();
		verticalManagementPanel.add(horizontalManagementPanel_1);

		Button buttonRemoveContents = new Button("Clear Contents");
		buttonRemoveContents.setWidth("272px");
		buttonRemoveContents.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {

				if (isAtLeastOneProjectorSelected()) {
					SynergyNetAppSystemService.Util.get()
							.clearProjectorContents(
									new PerformActionMessage(
											MESSAGESTATE.ACTIVATE),
									getProjectorsToSendTo(),
									new AsyncCallback<Void>() {
										@Override
										public void onFailure(Throwable caught) {
											new MessageDialogBox(
													"Communication error when aligning projector contents: "
															+ caught.getMessage())
													.show();
										}

										@Override
										public void onSuccess(Void result) {
										}
									});
				}
			}
		});
		horizontalManagementPanel_1.add(buttonRemoveContents);

		HorizontalPanel horizontalManagementPanel_2 = new HorizontalPanel();
		verticalManagementPanel.add(horizontalManagementPanel_2);

		Button buttonAlignContents = new Button("Align Contents");
		buttonAlignContents.setWidth("272px");
		buttonAlignContents.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {

				if (isAtLeastOneProjectorSelected()) {
					SynergyNetAppSystemService.Util.get().align(
							new PerformActionMessage(MESSAGESTATE.ACTIVATE),
							getProjectorsToSendTo(), new AsyncCallback<Void>() {
								@Override
								public void onFailure(Throwable caught) {
									new MessageDialogBox(
											"Communication error when aligning projector contents: "
													+ caught.getMessage())
											.show();
								}

								@Override
								public void onSuccess(Void result) {
								}
							});
				}
			}
		});
		horizontalManagementPanel_2.add(buttonAlignContents);

	}

	/**
	 * Transfer buttons.
	 */
	private void transferButtons() {
		HorizontalPanel horizontalTransferToTablePanel_1 = new HorizontalPanel();
		horizontalTransferToTablePanel_1.setSpacing(5);
		add(horizontalTransferToTablePanel_1);

		Button buttonTransferScreenshotsToTable = new Button(
				"Projectors --> Tables");
		buttonTransferScreenshotsToTable.setWidth("280px");
		buttonTransferScreenshotsToTable.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {

				if (isAtLeastOneProjectorSelected()
						&& isAtLeastOneTableSelected()) {
					SynergyNetAppSystemService.Util.get()
							.sendProjectedContentsToTable(getTablesToSendTo(),
									getProjectorsToSendTo(),
									new AsyncCallback<Void>() {
										@Override
										public void onFailure(Throwable caught) {
											new MessageDialogBox(
													"Communication error when aligning projector contents: "
															+ caught.getMessage())
													.show();
										}

										@Override
										public void onSuccess(Void result) {
										}
									});
				}
			}
		});
		horizontalTransferToTablePanel_1.add(buttonTransferScreenshotsToTable);

		HorizontalPanel horizontalTransferToProjectorPanel_2 = new HorizontalPanel();
		horizontalTransferToProjectorPanel_2.setSpacing(5);
		add(horizontalTransferToProjectorPanel_2);

		Button buttonTransferContentsToProjector = new Button(
				"Projectors <-- Tables");
		buttonTransferContentsToProjector.setWidth("280px");
		buttonTransferContentsToProjector.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {

				if (isAtLeastOneProjectorSelected()
						&& isAtLeastOneTableSelected()) {
					SynergyNetAppSystemService.Util.get()
							.sendContentsToProjector(getProjectorsToSendTo(),
									getTablesToSendTo(),
									new AsyncCallback<Void>() {
										@Override
										public void onFailure(Throwable caught) {
											new MessageDialogBox(
													"Communication error when aligning projector contents: "
															+ caught.getMessage())
													.show();
										}

										@Override
										public void onSuccess(Void result) {
										}
									});
				}
			}
		});
		horizontalTransferToProjectorPanel_2
				.add(buttonTransferContentsToProjector);

		HorizontalPanel horizontalTransferToProjectorPanel_1 = new HorizontalPanel();
		horizontalTransferToProjectorPanel_1.setSpacing(5);
		add(horizontalTransferToProjectorPanel_1);

		Button buttonTransferScreenshotsToProjector = new Button(
				"Show screenshots of tables");
		buttonTransferScreenshotsToProjector.setWidth("280px");
		buttonTransferScreenshotsToProjector
				.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {

						if (isAtLeastOneProjectorSelected()
								&& isAtLeastOneTableSelected()) {
							SynergyNetAppSystemService.Util.get()
									.sendScreenshotsToProjector(
											getProjectorsToSendTo(),
											getTablesToSendTo(),
											new AsyncCallback<Void>() {
												@Override
												public void onFailure(
														Throwable caught) {
													new MessageDialogBox(
															"Communication error when aligning projector contents: "
																	+ caught.getMessage())
															.show();
												}

												@Override
												public void onSuccess(
														Void result) {
												}
											});
						}
					}
				});
		horizontalTransferToProjectorPanel_1
				.add(buttonTransferScreenshotsToProjector);

	}

}
