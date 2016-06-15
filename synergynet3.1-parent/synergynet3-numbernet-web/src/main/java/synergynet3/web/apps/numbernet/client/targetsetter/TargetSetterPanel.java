package synergynet3.web.apps.numbernet.client.targetsetter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import synergynet3.web.apps.numbernet.client.calckeys.CalculatorKeyControlPanel;
import synergynet3.web.apps.numbernet.client.logic.ClassWideCalculatorSync;
import synergynet3.web.apps.numbernet.client.service.NumberNetService;
import synergynet3.web.apps.numbernet.shared.TableTarget;
import synergynet3.web.commons.client.dialogs.MessageDialogBox;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * The Class TargetSetterPanel.
 */
public class TargetSetterPanel extends VerticalPanel
{

	/** The btn control all calculator. */
	private Button btnControlAllCalculator;

	/** The class wide calculator key control panel. */
	private CalculatorKeyControlPanel classWideCalculatorKeyControlPanel;

	/** The class wide calculator sync. */
	private ClassWideCalculatorSync classWideCalculatorSync;

	/** The flex table. */
	private FlexTable flexTable;

	/** The graphing mode check box. */
	private CheckBox graphingModeCheckBox;

	/** The ordered tables. */
	private List<TableTarget> orderedTables;

	/** The targets. */
	private Map<String, String> targets = new HashMap<String, String>();

	/** The vertical panel. */
	private VerticalPanel verticalPanel;

	/**
	 * Instantiates a new target setter panel.
	 */
	public TargetSetterPanel()
	{
		setTitle("NumberNet Control");
		setSpacing(5);

		flexTable = new FlexTable();
		add(flexTable);
		flexTable.setSize("380px", "138px");

		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.setSpacing(5);
		add(horizontalPanel);

		Button btnNewButton = new Button("Refresh table list");
		btnNewButton.addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(ClickEvent event)
			{
				loadExpectedTablesList();
			}
		});
		horizontalPanel.add(btnNewButton);

		Button btnRotateAndSet = new Button("Rotate and Set");
		btnRotateAndSet.addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(ClickEvent event)
			{
				rotateAndSetTargets();
			}
		});
		horizontalPanel.add(btnRotateAndSet);

		Button btnSet = new Button("Set");
		btnSet.addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(ClickEvent event)
			{
				setTargets();
			}
		});
		horizontalPanel.add(btnSet);

		DisclosurePanel disclosurePanel = new DisclosurePanel("Class Wide Controls");
		disclosurePanel.setOpen(true);
		add(disclosurePanel);
		disclosurePanel.setWidth("330px");

		verticalPanel = new VerticalPanel();
		verticalPanel.setSpacing(10);
		disclosurePanel.setContent(verticalPanel);
		verticalPanel.setSize("266px", "55px");

		final CheckBox chckbxHideIncorrect = new CheckBox("Hide own incorrect");
		verticalPanel.add(chckbxHideIncorrect);
		chckbxHideIncorrect.addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(ClickEvent event)
			{
				shouldChangeDisplayOfIncorrectExpressions(!chckbxHideIncorrect.getValue());
			}
		});

		final CheckBox chckbxHideCorrect = new CheckBox("Hide own correct");
		verticalPanel.add(chckbxHideCorrect);
		chckbxHideCorrect.addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(ClickEvent event)
			{
				shouldChangeDisplayOfCorrectExpressions(!chckbxHideCorrect.getValue());
			}
		});

		final CheckBox chckbxHideOthersIncorrect = new CheckBox("Hide others' incorrect");
		chckbxHideOthersIncorrect.addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(ClickEvent event)
			{
				shouldChangeDisplayOfOthersIncorrectExpressions(!chckbxHideOthersIncorrect.getValue());
			}
		});
		verticalPanel.add(chckbxHideOthersIncorrect);

		final CheckBox chckbxHideOthersCorrect = new CheckBox("Hide others' correct");
		chckbxHideOthersCorrect.addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(ClickEvent event)
			{
				shouldChangeDisplayOfOthersCorrectExpressions(!chckbxHideOthersCorrect.getValue());
			}
		});
		verticalPanel.add(chckbxHideOthersCorrect);

		final CheckBox chckbxShowScores = new CheckBox("Show Scores");
		verticalPanel.add(chckbxShowScores);
		chckbxShowScores.addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(ClickEvent event)
			{
				shouldChangeDisplayOfScores(chckbxShowScores.getValue());
			}
		});

		final CheckBox checkBox = new CheckBox("Calculators visible");
		checkBox.addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(ClickEvent event)
			{
				changeCalculatorVisibility(checkBox.getValue());
			}

		});
		checkBox.setValue(true);
		verticalPanel.add(checkBox);

		final CheckBox checkBox_1 = new CheckBox("Will respond to touches");
		checkBox_1.addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(ClickEvent event)
			{
				setAllTablesShouldRespondToTouches(checkBox_1.getValue());
			}

		});
		checkBox_1.setValue(true);
		verticalPanel.add(checkBox_1);

		graphingModeCheckBox = new CheckBox("Graphing mode");
		graphingModeCheckBox.addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(ClickEvent event)
			{
				setGraphingModeEnabled(graphingModeCheckBox.getValue());
			}
		});
		verticalPanel.add(graphingModeCheckBox);

		btnControlAllCalculator = new Button("Control All Calculator Keys");
		btnControlAllCalculator.addClickHandler(new ClickHandler()
		{

			@Override
			public void onClick(ClickEvent event)
			{
				openSystemWideCalculatorControl();
			}

		});
		verticalPanel.add(btnControlAllCalculator);

	}

	/**
	 * Change calculator visibility.
	 *
	 * @param value
	 *            the value
	 */
	protected void changeCalculatorVisibility(Boolean value)
	{
		NumberNetService.Util.getInstance().setCalculatorVisibility(value, new AsyncCallback<Void>()
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
	 * Load expected tables list.
	 */
	protected void loadExpectedTablesList()
	{
		NumberNetService.Util.getInstance().getTableTargets(new AsyncCallback<List<TableTarget>>()
		{

			@Override
			public void onFailure(Throwable caught)
			{
				new MessageDialogBox(caught.getMessage()).show();
			}

			@Override
			public void onSuccess(List<TableTarget> result)
			{
				updateOrderedTablesListWithNewTableList(result);
				updateList();
			}
		});
	}

	/**
	 * Move down.
	 *
	 * @param index
	 *            the index
	 */
	protected void moveDown(int index)
	{
		System.out.println("down on " + index + " with list of " + orderedTables.size() + " items");
		TableTarget taken = orderedTables.remove(index);
		if ((index + 1) > orderedTables.size())
		{
			System.out.println("case A, putting in at 0");
			orderedTables.add(0, taken);
		}
		else
		{
			System.out.println("case B, putting in at " + (index + 1));
			orderedTables.add(index + 1, taken);
		}
	}

	/**
	 * Move up.
	 *
	 * @param index
	 *            the index
	 */
	protected void moveUp(int index)
	{
		System.out.println("up on " + index + " with list of " + orderedTables.size() + " items.");
		TableTarget taken = orderedTables.remove(index);
		if ((index - 1) < 0)
		{
			orderedTables.add(orderedTables.size(), taken);
		}
		else
		{
			orderedTables.add(index - 1, taken);
		}
		System.out.println(orderedTables.size());
	}

	/**
	 * Open system wide calculator control.
	 */
	protected void openSystemWideCalculatorControl()
	{
		if (classWideCalculatorKeyControlPanel == null)
		{
			System.out.println("Creating...");
			classWideCalculatorKeyControlPanel = new CalculatorKeyControlPanel();

			classWideCalculatorSync = new ClassWideCalculatorSync();
			classWideCalculatorKeyControlPanel.delegate = classWideCalculatorSync;

			verticalPanel.remove(btnControlAllCalculator);
			verticalPanel.add(classWideCalculatorKeyControlPanel);
		}
	}

	/**
	 * Rotate and set targets.
	 */
	protected void rotateAndSetTargets()
	{
		if (graphingModeCheckBox.getValue())
		{
			new MessageDialogBox("Cannot change targets while in graphing mode.").show();
			return;
		}
		List<TableTarget> targetsList = new ArrayList<TableTarget>();

		for (int row = 0; row < flexTable.getRowCount(); row++)
		{
			String table = ((Label) flexTable.getWidget(row, 1)).getText();
			double target = Double.parseDouble(targets.get(table));
			System.out.println(table + " > " + target);
		}

		int tableCount = flexTable.getRowCount();
		for (int row = 0; row < flexTable.getRowCount(); row++)
		{

			String table = ((Label) flexTable.getWidget(row, 1)).getText();
			int targetIndex = row - 1;
			if (targetIndex < 0)
			{
				targetIndex = tableCount - 1;
			}
			String tableFrom = ((Label) flexTable.getWidget(targetIndex, 1)).getText();
			double target = Double.parseDouble(targets.get(tableFrom));
			targetsList.add(new TableTarget(table, target));
		}

		NumberNetService.Util.getInstance().rotateTableContentAndTargets(targetsList, new AsyncCallback<Void>()
		{

			@Override
			public void onFailure(Throwable caught)
			{
				new MessageDialogBox(caught.getMessage()).show();
			}

			@Override
			public void onSuccess(Void result)
			{
				// new MessageDialogBox("Done").show();
				loadExpectedTablesList();
			}
		});
	}

	/**
	 * Sets the all tables should respond to touches.
	 *
	 * @param value
	 *            the new all tables should respond to touches
	 */
	protected void setAllTablesShouldRespondToTouches(boolean value)
	{
		NumberNetService.Util.getInstance().setTableInputEnabled(value, new AsyncCallback<Void>()
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
	 * Sets the graphing mode enabled.
	 *
	 * @param value
	 *            the new graphing mode enabled
	 */
	protected void setGraphingModeEnabled(Boolean value)
	{
		NumberNetService.Util.getInstance().setGraphingModeEnabled(value, new AsyncCallback<Void>()
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
	 * Sets the targets.
	 */
	protected void setTargets()
	{
		if (graphingModeCheckBox.getValue())
		{
			new MessageDialogBox("Cannot change targets while in graphing mode.").show();
			return;
		}
		try
		{
			List<TableTarget> targetsList = new ArrayList<TableTarget>();
			for (int row = 0; row < flexTable.getRowCount(); row++)
			{
				String table = ((Label) flexTable.getWidget(row, 1)).getText();
				double target = Double.parseDouble(((TextBox) flexTable.getWidget(row, 2)).getText());
				targetsList.add(new TableTarget(table, target));
			}

			NumberNetService.Util.getInstance().setTableTargets(targetsList, new AsyncCallback<Void>()
			{
				@Override
				public void onFailure(Throwable caught)
				{
					for (int i = 0; i < caught.getStackTrace().length; i++)
					{
						System.out.println(caught.getStackTrace()[i]);
					}
					new MessageDialogBox("Error." + caught.getMessage()).show();
				}

				@Override
				public void onSuccess(Void result)
				{
					// new MessageDialogBox("Done").show();
				}
			});
		}
		catch (NumberFormatException ex)
		{
			new MessageDialogBox("Please make sure all tables have a numerical target.").show();
		}
	}

	/**
	 * Should change display of correct expressions.
	 *
	 * @param shouldBeVisible
	 *            the should be visible
	 */
	protected void shouldChangeDisplayOfCorrectExpressions(boolean shouldBeVisible)
	{
		NumberNetService.Util.getInstance().setCorrectExpressionsVisibleForAllTables(shouldBeVisible, new AsyncCallback<Void>()
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
	 * Should change display of incorrect expressions.
	 *
	 * @param shouldBeVisible
	 *            the should be visible
	 */
	protected void shouldChangeDisplayOfIncorrectExpressions(boolean shouldBeVisible)
	{
		NumberNetService.Util.getInstance().setIncorrectExpressionsVisibleForAllTables(shouldBeVisible, new AsyncCallback<Void>()
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
	 * Should change display of others correct expressions.
	 *
	 * @param shouldBeVisible
	 *            the should be visible
	 */
	protected void shouldChangeDisplayOfOthersCorrectExpressions(boolean shouldBeVisible)
	{
		NumberNetService.Util.getInstance().setOthersCorrectExpressionsVisibleForAllTables(shouldBeVisible, new AsyncCallback<Void>()
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
	 * Should change display of others incorrect expressions.
	 *
	 * @param shouldBeVisible
	 *            the should be visible
	 */
	protected void shouldChangeDisplayOfOthersIncorrectExpressions(boolean shouldBeVisible)
	{
		NumberNetService.Util.getInstance().setOthersIncorrectExpressionsVisibleForAllTables(shouldBeVisible, new AsyncCallback<Void>()
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
	 * Should change display of scores.
	 *
	 * @param shouldBeVisible
	 *            the should be visible
	 */
	protected void shouldChangeDisplayOfScores(boolean shouldBeVisible)
	{
		NumberNetService.Util.getInstance().setScoresVisibleForAllTables(shouldBeVisible, new AsyncCallback<Void>()
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
	 * Update list.
	 */
	protected void updateList()
	{
		flexTable.removeAllRows();

		for (int row = 0; row < orderedTables.size(); row++)
		{
			HorizontalPanel bpanel = new HorizontalPanel();
			Button up = new Button("up");
			final int index = row;
			up.addClickHandler(new ClickHandler()
			{
				@Override
				public void onClick(ClickEvent event)
				{
					moveUp(index);
					updateList();
				}
			});
			Button down = new Button("down");
			down.addClickHandler(new ClickHandler()
			{
				@Override
				public void onClick(ClickEvent event)
				{
					moveDown(index);
					updateList();
				}
			});
			bpanel.add(up);
			bpanel.add(down);
			flexTable.setWidget(row, 0, bpanel);
			final TableTarget tt = orderedTables.get(row);
			flexTable.setWidget(row, 1, new Label(tt.getTable()));
			String currentTarget = targets.get(tt.getTable());
			if (currentTarget == null)
			{
				currentTarget = "";
			}
			final TextBox valueTextBox = new TextBox();
			valueTextBox.setText(currentTarget);
			valueTextBox.addChangeHandler(new ChangeHandler()
			{
				@Override
				public void onChange(ChangeEvent event)
				{
					targets.put(tt.getTable(), valueTextBox.getText());
				}
			});
			flexTable.setWidget(row, 2, valueTextBox);
		}
	}

	/**
	 * Update ordered tables list with new table list.
	 *
	 * @param result
	 *            the result
	 */
	protected void updateOrderedTablesListWithNewTableList(List<TableTarget> result)
	{

		for (TableTarget tt : result)
		{
			targets.put(tt.getTable(), "" + tt.getTarget());
		}

		if (orderedTables == null)
		{
			orderedTables = new ArrayList<TableTarget>();
			for (TableTarget tt : result)
			{
				orderedTables.add(tt);

			}
		}

		List<TableTarget> ordered = new ArrayList<TableTarget>();

		for (TableTarget o : orderedTables)
		{
			if (result.contains(o))
			{
				ordered.add(o);
			}
		}

		for (TableTarget tt : result)
		{
			if (!ordered.contains(tt))
			{
				ordered.add(tt);
			}
		}

		orderedTables = ordered;
	}

}
