package synergynet3.web.apps.numbernet.client;

import synergynet3.web.apps.numbernet.client.expressionview.ExpressionView;
// import synergynet3.web.apps.numbernet.client.projection.ProjectionControl;
import synergynet3.web.apps.numbernet.client.tablemanager.TableManager;
import synergynet3.web.apps.numbernet.client.targetsetter.TargetSetterPanel;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * The Class NumberNetUI.
 */
public class NumberNetUI extends VerticalPanel {

	/** The shared instance. */
	private static NumberNetUI sharedInstance;

	/** The tab panel. */
	private TabPanel tabPanel;

	/**
	 * Instantiates a new number net ui.
	 */
	public NumberNetUI() {
		super();
		sharedInstance = this;
		tabPanel = new TabPanel();

		TableManager tableManagerTab = new TableManager();
		tabPanel.add(tableManagerTab, tableManagerTab.getTitle(), false);
		TargetSetterPanel targetSetterPanel = new TargetSetterPanel();
		tabPanel.add(targetSetterPanel, targetSetterPanel.getTitle(), false);

		// ProjectionControl projectionControlPanel = new ProjectionControl();
		// tabPanel.add(projectionControlPanel,
		// projectionControlPanel.getTitle(), false);

		ExpressionView ev = new ExpressionView();
		tabPanel.add(ev, ev.getTitle(), false);

		tabPanel.selectTab(0);
		add(tabPanel);
	}

	/**
	 * Gets the.
	 *
	 * @return the number net ui
	 */
	public static NumberNetUI get() {
		return sharedInstance;
	}

	/**
	 * Adds the to tab panel.
	 *
	 * @param vp the vp
	 * @param title the title
	 */
	public void addToTabPanel(VerticalPanel vp, String title) {
		tabPanel.add(vp, title);
		int index = tabPanel.getWidgetIndex(vp);
		addCloseButtonToPanel(vp, index);
		tabPanel.selectTab(index);
	}

	/**
	 * Adds the close button to panel.
	 *
	 * @param vp the vp
	 * @param tabIndex the tab index
	 */
	private void addCloseButtonToPanel(VerticalPanel vp, final int tabIndex) {
		Button b = new Button("Close Tab");
		b.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				tabPanel.selectTab(0);
				tabPanel.remove(tabIndex);
			}
		});
		vp.add(b);
	}
}
