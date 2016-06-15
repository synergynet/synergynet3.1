package synergynet3.web.apps.numbernet.client.expressionview;

import synergynet3.web.apps.numbernet.shared.Expression;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

/**
 * The Class SingleExpressionWidget.
 */
public class SingleExpressionWidget extends HorizontalPanel
{

	/** The lbl expression. */
	private Label lblExpression;

	/** The lbl name. */
	private Label lblName;

	/** The lbl table. */
	private Label lblTable;

	/** The lbl target. */
	private Label lblTarget;

	/** The lbl value. */
	private Label lblValue;

	/**
	 * Instantiates a new single expression widget.
	 */
	public SingleExpressionWidget()
	{
		super();
		setSize("394px", "22px");
		setSpacing(2);

		lblTable = new Label("Red");
		add(lblTable);
		lblTable.setWidth("49px");

		lblName = new Label("John Doe");
		add(lblName);
		lblName.setWidth("97px");

		lblExpression = new Label("3*4+(2-1)");
		add(lblExpression);
		lblExpression.setWidth("117px");

		lblValue = new Label("13");
		add(lblValue);
		lblValue.setWidth("54px");

		lblTarget = new Label("14");
		add(lblTarget);
		lblTarget.setWidth("33px");
	}

	/**
	 * Sets the expression.
	 *
	 * @param e
	 *            the new expression
	 */
	public void setExpression(Expression e)
	{
		if (e == null)
		{
			return;
		}
		setExpressionProperties(e);
		updateStyles(e);
	}

	/**
	 * Sets the expression properties.
	 *
	 * @param e
	 *            the new expression properties
	 */
	private void setExpressionProperties(Expression e)
	{
		lblTable.setText(e.getCreatedOnTable());
		lblName.setText(e.getCreatedBy());
		lblExpression.setText(e.getExpression());
		lblValue.setText(e.getValue() + "");
		lblTarget.setText(e.getTarget() + "");
	}

	/**
	 * Update styles.
	 *
	 * @param e
	 *            the e
	 */
	private void updateStyles(Expression e)
	{
		if (e.isCorrect())
		{
			this.setStyleName("correctExpression");
		}
		else
		{
			this.setStyleName("incorrectExpression");
		}
	}
}
