package synergynet3.apps.numbernet.validation;

import java.util.logging.Level;
import java.util.logging.Logger;

import synergynet3.apps.numbernet.model.ExpressionSession;

/**
 * The Class DefaultValidationChecker.
 */
public class DefaultValidationChecker implements IValidationChecker {

	/** The Constant log. */
	private static final Logger log = Logger
			.getLogger(DefaultValidationChecker.class.getName());

	/** The expression session. */
	private ExpressionSession expressionSession;

	/**
	 * Instantiates a new default validation checker.
	 */
	public DefaultValidationChecker() {
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynet3.apps.numbernet.validation.IValidationChecker#isValid(java
	 * .lang.String)
	 */
	@Override
	public ValidationResult isValid(String expression) {
		expression = expression.trim();
		try {
			if (expression.equals(expressionSession
					.getCurrentTargetValueAsString())) {
				return ValidationResult.IDENTICAL;
			}
			if (expressionSession.containsExpression(expression)) {
				return ValidationResult.DUPLICATE;
			}
			return ValidationResult.VALID;
		} catch (Exception e) {
			log.log(Level.FINE, "Expression " + expression
					+ " causes an exception " + e);
			return ValidationResult.INVALID;
		}
	}

	/**
	 * Sets the current expression session.
	 *
	 * @param expressionSession the new current expression session
	 */
	public void setCurrentExpressionSession(ExpressionSession expressionSession) {
		this.expressionSession = expressionSession;
	}

}
