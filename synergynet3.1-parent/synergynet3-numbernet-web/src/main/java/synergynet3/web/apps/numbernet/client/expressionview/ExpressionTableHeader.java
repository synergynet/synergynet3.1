package synergynet3.web.apps.numbernet.client.expressionview;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

/**
 * The Class ExpressionTableHeader.
 */
public class ExpressionTableHeader extends HorizontalPanel {

	/**
	 * Instantiates a new expression table header.
	 */
	public ExpressionTableHeader() {
		super();
		setSize("394px", "22px");
		setSpacing(2);

		Label lblTable = new Label("Table");
		add(lblTable);
		lblTable.setWidth("49px");

		Label lblName = new Label("Person");
		add(lblName);
		lblName.setWidth("97px");

		Label lblExpression = new Label("Expression");
		add(lblExpression);
		lblExpression.setWidth("117px");

		Label lblValue = new Label("Value");
		add(lblValue);
		lblValue.setWidth("54px");

		Label lblTarget = new Label("Target");
		add(lblTarget);
		lblTarget.setWidth("33px");

		this.setStylePrimaryName("expressionTableHeader");
	}
}
