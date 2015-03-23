package synergynet3.web.apps.numbernet.client.projection;

import synergynet3.web.apps.numbernet.client.service.NumberNetService;
import synergynet3.web.apps.numbernet.shared.ProjectionDisplayMode;
import synergynet3.web.commons.client.devicesonline.DevicesOnlineWidget;
import synergynet3.web.commons.client.dialogs.MessageDialogBox;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * The Class ProjectionControlPanel.
 */
public class ProjectionControlPanel extends VerticalPanel {

	/** The projector name. */
	private String projectorName;

	// ListBox listOnlineTables;
	/** The projectors online. */
	private DevicesOnlineWidget projectorsOnline;
	/** The table last projected. */
	private String tableLastProjected;

	/** The disclosure panel. */
	DisclosurePanel disclosurePanel;

	/**
	 * Instantiates a new projection control panel.
	 */
	public ProjectionControlPanel() {
		setSpacing(5);

		disclosurePanel = new DisclosurePanel("");
		disclosurePanel.setOpen(true);
		add(disclosurePanel);

		VerticalPanel onlineTablesVerticalPanel = new VerticalPanel();
		disclosurePanel.setContent(onlineTablesVerticalPanel);
		onlineTablesVerticalPanel.setSize("5cm", "4cm");

		Label lblOnlineTables = new Label("Online Tables:");
		onlineTablesVerticalPanel.add(lblOnlineTables);

		HorizontalPanel mainPanel = new HorizontalPanel();
		onlineTablesVerticalPanel.add(mainPanel);
		mainPanel.setSpacing(5);

		projectorsOnline = new DevicesOnlineWidget();
		projectorsOnline.getButtonsPanel().setSpacing(5);
		projectorsOnline.setDeviceType("tables");
		mainPanel.add(projectorsOnline);
		projectorsOnline.setSize("212px", "184px");

		VerticalPanel buttonsPanel = new VerticalPanel();
		buttonsPanel.setSpacing(5);
		mainPanel.add(buttonsPanel);

		DisclosurePanel cloning = new DisclosurePanel("Cloning Tables");
		cloning.setOpen(true);
		buttonsPanel.add(cloning);
		cloning.setSize("263", "96px");

		VerticalPanel verticalPanel = new VerticalPanel();
		verticalPanel.setSpacing(5);
		cloning.setContent(verticalPanel);
		verticalPanel.setSize("260", "");

		Button btnCloneSelectedTable = new Button("Clone Table");
		verticalPanel.add(btnCloneSelectedTable);
		btnCloneSelectedTable.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				cloneTableButtonPressed();
			}
		});
		btnCloneSelectedTable.setWidth("119px");

		Button btnReplaceTableContent = new Button("Push to Table");
		verticalPanel.add(btnReplaceTableContent);
		btnReplaceTableContent.setWidth("118px");

		Button btnUntifyRotation = new Button("Untify Rotation");
		verticalPanel.add(btnUntifyRotation);
		btnUntifyRotation.setWidth("119px");

		Button btnClearProjector = new Button("Clear Projector");
		btnClearProjector.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				clearProjectorButtonPressed();
			}
		});

		Button btnShowScores = new Button("Show Scores");
		buttonsPanel.add(btnShowScores);
		btnShowScores.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				setScoreDisplay();
			}
		});
		buttonsPanel.add(btnClearProjector);
		btnUntifyRotation.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				setUnifyRotation(false);
				setUnifyRotation(true);
			}
		});
		btnReplaceTableContent.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				replaceTableButtonPressed();
			}
		});
	}

	/**
	 * Gets the projector name.
	 *
	 * @return the projector name
	 */
	public String getProjectorName() {
		return this.projectorName;
	}

	/**
	 * Sets the projector name.
	 *
	 * @param name the new projector name
	 */
	public void setProjectorName(String name) {
		this.projectorName = name;
		this.disclosurePanel.getHeaderTextAccessor().setText(name);
	}

	/**
	 * Gets the table last projected.
	 *
	 * @return the table last projected
	 */
	private String getTableLastProjected() {
		return tableLastProjected;
	}

	/**
	 * Sets the projection display mode.
	 *
	 * @param mode the new projection display mode
	 */
	private void setProjectionDisplayMode(ProjectionDisplayMode mode) {
		NumberNetService.Util.getInstance().setProjectorDisplayMode(
				getProjectorName(), mode, new AsyncCallback<Void>() {
					@Override
					public void onFailure(Throwable caught) {
						new MessageDialogBox(caught.getMessage()).show();
					}

					@Override
					public void onSuccess(Void result) {
					}
				});
	}

	/**
	 * Clear projector button pressed.
	 */
	protected void clearProjectorButtonPressed() {
		setProjectionDisplayMode(ProjectionDisplayMode.BLANK);
	}

	/**
	 * Clone table button pressed.
	 */
	protected void cloneTableButtonPressed() {
		final String selectedTable = projectorsOnline.getDeviceSelected();

		NumberNetService.Util.getInstance().projectTable(selectedTable,
				getProjectorName(), new AsyncCallback<Void>() {

					@Override
					public void onFailure(Throwable caught) {
						new MessageDialogBox(caught.getMessage()).show();
					}

					@Override
					public void onSuccess(Void result) {
						tableLastProjected = selectedTable;
					}
				});

	}

	/**
	 * Replace table button pressed.
	 */
	protected void replaceTableButtonPressed() {
		String table = getTableLastProjected();
		if (table == null) {
			return;
			// NumberNetService.Util.getInstance().copyPositionInformationFromProjectorToTable(getProjectorName(),
			// table, new AsyncCallback<Void>() {
			// @Override
			// public void onFailure(Throwable caught) {
			// new MessageDialogBox(caught.getMessage()).show();
			// }
			//
			// @Override
			// public void onSuccess(Void result) {
			// new MessageDialogBox("Message sent to projector.").show();
			// }
			// });
		}
	}

	/**
	 * Sets the score display.
	 */
	protected void setScoreDisplay() {
		setProjectionDisplayMode(ProjectionDisplayMode.SCORES);
	}

	/**
	 * Sets the unify rotation.
	 *
	 * @param value the new unify rotation
	 */
	protected void setUnifyRotation(boolean value) {
		NumberNetService.Util.getInstance().setUnifyRotationModeEnabled(
				getProjectorName(), value, new AsyncCallback<Void>() {

					@Override
					public void onFailure(Throwable caught) {
						new MessageDialogBox(caught.getMessage()).show();
					}

					@Override
					public void onSuccess(Void result) {
					}
				});
	}
}
