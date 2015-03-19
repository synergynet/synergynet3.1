package synergynet3.apps.numbernet.validation;

import java.util.logging.Level;
import java.util.logging.Logger;

import synergynet3.apps.numbernet.model.ExpressionSession;

public class DefaultValidationChecker implements IValidationChecker {
	private static final Logger log = Logger.getLogger(DefaultValidationChecker.class.getName());
	
	private ExpressionSession expressionSession;

	public DefaultValidationChecker() {
	}
	
	public void setCurrentExpressionSession(ExpressionSession expressionSession) {
		this.expressionSession = expressionSession;
	}

	@Override
	public ValidationResult isValid(String expression) {
		expression = expression.trim();
		try {
			if(expression.equals(expressionSession.getCurrentTargetValueAsString())) return ValidationResult.IDENTICAL;
			if(expressionSession.containsExpression(expression)) return ValidationResult.DUPLICATE;
			return ValidationResult.VALID;
		} catch (Exception e) {
			log.log(Level.FINE, "Expression " + expression + " causes an exception " + e);			
			return ValidationResult.INVALID;
		}
	}

}
