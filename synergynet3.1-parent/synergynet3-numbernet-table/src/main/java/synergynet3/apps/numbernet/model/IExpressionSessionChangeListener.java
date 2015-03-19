package synergynet3.apps.numbernet.model;

import synergynet3.web.apps.numbernet.shared.Expression;

public interface IExpressionSessionChangeListener {
	public void expressionAddedFromCalculator(Expression e);
	public void expressionAddedFromNetwork(Expression e);
	public void expressionRemoved(Expression expression);
	public void allExpressionsRemoved();		
	public void targetChanged(Double newValue);
}
