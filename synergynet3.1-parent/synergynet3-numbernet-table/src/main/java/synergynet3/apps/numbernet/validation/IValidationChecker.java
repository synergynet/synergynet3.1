package synergynet3.apps.numbernet.validation;

public interface IValidationChecker {
	
	public static enum ValidationResult {
		VALID,
		INVALID,
		DUPLICATE,
		IDENTICAL
	}
	
	public ValidationResult isValid(String expression);
}
