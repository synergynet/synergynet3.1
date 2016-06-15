package synergynet3.apps.numbernet.validation;

/**
 * The Interface IValidationChecker.
 */
public interface IValidationChecker
{

	/**
	 * The Enum ValidationResult.
	 */
	public static enum ValidationResult
	{

		/** The duplicate. */
		DUPLICATE,

		/** The identical. */
		IDENTICAL,

		/** The invalid. */
		INVALID,

		/** The valid. */
		VALID
	}

	/**
	 * Checks if is valid.
	 *
	 * @param expression
	 *            the expression
	 * @return the validation result
	 */
	public ValidationResult isValid(String expression);
}
