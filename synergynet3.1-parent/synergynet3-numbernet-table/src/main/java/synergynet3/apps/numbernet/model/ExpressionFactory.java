package synergynet3.apps.numbernet.model;

import java.util.UUID;

import synergynet3.apps.numbernet.ui.calculator.Calculator;
import synergynet3.web.apps.numbernet.expressionevaluator.ExpressionEvaluator;
import synergynet3.web.apps.numbernet.shared.Expression;

/**
 * A factory for creating Expression objects.
 */
public class ExpressionFactory {

	/** The evaluator. */
	private static ExpressionEvaluator evaluator;

	static {
		evaluator = new ExpressionEvaluator();
	}

	/**
	 * Creates a new Expression object.
	 *
	 * @param expression the expression
	 * @param calc the calc
	 * @param tableID the table id
	 * @param target the target
	 * @param isEdit the is edit
	 * @return the expression
	 */
	public static Expression createExpressionFromStringAndCalculator(
			String expression, Calculator calc, String tableID, double target,
			boolean isEdit) {
		String id = UUID.randomUUID().toString();
		String owner = calc.getOwner();
		double value = Double.NaN;
		String error = null;

		try {
			value = evaluator.evaluate(expression);
		} catch (Exception e1) {
			value = Double.NaN;
			error = e1.getMessage();
		}

		Expression e = new Expression(id, expression, value, target, error,
				owner, tableID, isEdit);

		return e;
	}
}
