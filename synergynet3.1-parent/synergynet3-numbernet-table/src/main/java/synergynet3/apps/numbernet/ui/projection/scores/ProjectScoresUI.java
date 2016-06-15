package synergynet3.apps.numbernet.ui.projection.scores;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import multiplicity3.csys.factory.ContentTypeNotBoundException;
import multiplicity3.csys.items.container.IContainer;
import multiplicity3.csys.items.item.IItem;
import multiplicity3.csys.stage.IStage;

import com.jme3.math.Vector2f;

/**
 * The Class ProjectScoresUI.
 */
public class ProjectScoresUI
{

	/** The container. */
	private IContainer container;

	/** The stage. */
	private IStage stage;

	/** The table count. */
	private int tableCount = 0;

	/** The table to scores ui. */
	private Map<String, TableScoresUI> tableToScoresUI = new HashMap<String, TableScoresUI>();

	/**
	 * Instantiates a new project scores ui.
	 *
	 * @param stage
	 *            the stage
	 */
	public ProjectScoresUI(IStage stage)
	{
		this.stage = stage;
		try
		{
			IContainer container = stage.getContentFactory().create(IContainer.class, "scoresui", UUID.randomUUID());
			this.container = container;
			stage.addItem(container);
			this.container.setVisible(false);
		}
		catch (ContentTypeNotBoundException e)
		{
			// log
		}
	}

	/**
	 * Gets the table ui.
	 *
	 * @param table
	 *            the table
	 * @return the table ui
	 */
	public TableScoresUI getTableUI(String table)
	{
		TableScoresUI tableUI = tableToScoresUI.get(table);
		if (tableUI == null)
		{
			try
			{
				tableUI = createTableScoresUI(table);
			}
			catch (ContentTypeNotBoundException e)
			{
				// log
			}
			tableToScoresUI.put(table, tableUI);
			tableCount++;
		}
		return tableUI;
	}

	/**
	 * Load test.
	 */
	public void loadTest()
	{
		TableScoresUI green = getTableUI("green");
		green.setCorrectExpressionCount(10);
		green.setIncorrectExpressionCount(1);
		green.setBracketUseCount(50);
		green.setUsedAllOperators(false);
		green.setMoreThanOneOperatorCount(3);

		TableScoresUI blue = getTableUI("blue");
		blue.setCorrectExpressionCount(10);
		blue.setIncorrectExpressionCount(1);
		blue.setBracketUseCount(5);
		blue.setUsedAllOperators(false);
		blue.setMoreThanOneOperatorCount(1);

		TableScoresUI yellow = getTableUI("yellow");
		yellow.setCorrectExpressionCount(10);
		yellow.setIncorrectExpressionCount(1);
		yellow.setBracketUseCount(15);
		yellow.setUsedAllOperators(true);
		yellow.setMoreThanOneOperatorCount(5);

		TableScoresUI red = getTableUI("red");
		red.setCorrectExpressionCount(10);
		red.setIncorrectExpressionCount(1);
		red.setBracketUseCount(3);
		red.setUsedAllOperators(false);
		red.setMoreThanOneOperatorCount(13);
	}

	/**
	 * Sets the visibility.
	 *
	 * @param b
	 *            the new visibility
	 */
	public void setVisibility(boolean b)
	{
		this.container.setVisible(b);
	}

	/**
	 * Creates the table scores ui.
	 *
	 * @param table
	 *            the table
	 * @return the table scores ui
	 * @throws ContentTypeNotBoundException
	 *             the content type not bound exception
	 */
	private TableScoresUI createTableScoresUI(String table) throws ContentTypeNotBoundException
	{
		TableScoresUI ui = new TableScoresUI(table);
		IContainer rootContainer = stage.getContentFactory().create(IContainer.class, "", UUID.randomUUID());
		container.addItem(rootContainer);

		ui.buildUI(rootContainer, stage.getContentFactory());
		setLocationForTableUI(rootContainer);
		container.getZOrderManager().updateOrder();
		return ui;
	}

	/**
	 * Sets the location for table ui.
	 *
	 * @param tableUIRoot
	 *            the new location for table ui
	 */
	private void setLocationForTableUI(IItem tableUIRoot)
	{
		Vector2f loc = new Vector2f();
		switch (tableCount)
		{
			case 0:
			{
				loc.set(-250, 200);
				break;
			}
			case 1:
			{
				loc.set(250, 200);
				break;
			}
			case 2:
			{
				loc.set(-250, -200);
				break;
			}
			case 3:
			{
				loc.set(250, -200);
				break;
			}
		}
		tableUIRoot.setRelativeLocation(loc);
	}
}
