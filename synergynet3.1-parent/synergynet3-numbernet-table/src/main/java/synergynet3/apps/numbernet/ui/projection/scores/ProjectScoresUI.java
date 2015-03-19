package synergynet3.apps.numbernet.ui.projection.scores;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.jme3.math.Vector2f;

import multiplicity3.csys.factory.ContentTypeNotBoundException;
import multiplicity3.csys.items.container.IContainer;
import multiplicity3.csys.items.item.IItem;
import multiplicity3.csys.stage.IStage;

public class ProjectScoresUI {
	private Map<String,TableScoresUI> tableToScoresUI = new HashMap<String,TableScoresUI>();
	private IStage stage;
	private IContainer container;
	private int tableCount = 0;

	public ProjectScoresUI(IStage stage) {
		this.stage = stage;
		try {
			IContainer container = stage.getContentFactory().create(IContainer.class, "scoresui", UUID.randomUUID());
			this.container = container;
			stage.addItem(container);
			this.container.setVisible(false);
		} catch (ContentTypeNotBoundException e) {
			// log
		}
	}

	public TableScoresUI getTableUI(String table) {
		TableScoresUI tableUI = tableToScoresUI.get(table);
		if(tableUI == null) {
			try {
				tableUI = createTableScoresUI(table);
			} catch (ContentTypeNotBoundException e) {
				//log
			}
			tableToScoresUI.put(table, tableUI);
			tableCount++;
		}		
		return tableUI;
	}

	private TableScoresUI createTableScoresUI(String table) throws ContentTypeNotBoundException {
		TableScoresUI ui = new TableScoresUI(table);
		IContainer rootContainer = stage.getContentFactory().create(IContainer.class, "", UUID.randomUUID());
		container.addItem(rootContainer);
		
		ui.buildUI(rootContainer, stage.getContentFactory());
		setLocationForTableUI(rootContainer);
		container.getZOrderManager().updateOrder();
		return ui;
	}

	private void setLocationForTableUI(IItem tableUIRoot) {
		Vector2f loc = new Vector2f();
		switch(tableCount) {
		case 0: {
			loc.set(-250, 200);
			break;
		}
		case 1: {
			loc.set(250, 200);
			break;
		}
		case 2: {
			loc.set(-250, -200);
			break;
		}
		case 3: {
			loc.set(250, -200);
			break;
		}
		}
		tableUIRoot.setRelativeLocation(loc);
	}
	
	public void loadTest() {
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

	public void setVisibility(boolean b) {
		this.container.setVisible(b);
	}
}
