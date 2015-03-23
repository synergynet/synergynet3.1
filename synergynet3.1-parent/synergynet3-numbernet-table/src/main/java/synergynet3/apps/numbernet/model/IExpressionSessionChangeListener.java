package synergynet3.apps.numbernet.model;

import synergynet3.web.apps.numbernet.shared.Expression;

/**
 * The listener interface for receiving IExpressionSessionChange events. The
 * class that is interested in processing a IExpressionSessionChange event
 * implements this interface, and the object created with that class is
 * registered with a component using the component's
 * <code>addIExpressionSessionChangeListener<code> method. When
 * the IExpressionSessionChange event occurs, that object's appropriate
 * method is invoked.
 *
 * @see IExpressionSessionChangeEvent
 */
public interface IExpressionSessionChangeListener {

	/**
	 * All expressions removed.
	 */
	public void allExpressionsRemoved();

	/**
	 * Expression added from calculator.
	 *
	 * @param e the e
	 */
	public void expressionAddedFromCalculator(Expression e);

	/**
	 * Expression added from network.
	 *
	 * @param e the e
	 */
	public void expressionAddedFromNetwork(Expression e);

	/**
	 * Expression removed.
	 *
	 * @param expression the expression
	 */
	public void expressionRemoved(Expression expression);

	/**
	 * Target changed.
	 *
	 * @param newValue the new value
	 */
	public void targetChanged(Double newValue);
}
