package synergynet3.web.apps.numbernet.expressionevaluator;

/**
 * The Class ExpressionEvaluator.
 */
public class ExpressionEvaluator
{

	/** The instance. */
	private static ExpressionEvaluator instance;

	/** The evaluator. */
	private MathEvaluator evaluator;

	/**
	 * Instantiates a new expression evaluator.
	 */
	public ExpressionEvaluator()
	{
		evaluator = new MathEvaluator();
	}

	/**
	 * Gets the single instance of ExpressionEvaluator.
	 *
	 * @return single instance of ExpressionEvaluator
	 */
	public static ExpressionEvaluator getInstance()
	{
		synchronized (ExpressionEvaluator.class)
		{
			if (instance == null)
			{
				instance = new ExpressionEvaluator();
			}
		}
		return instance;
	}

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args)
	{
		MathEvaluator m = new MathEvaluator();
		m.setExpression("(4+3)*2");
		try
		{
			Double value = m.getValue();
			System.out.println(value);
		}
		catch (Exception ex)
		{
			System.out.println("Does not evaluate. " + ex.getMessage());
		}
	}

	/**
	 * Evaluate.
	 *
	 * @param expression
	 *            the expression
	 * @return the double
	 * @throws Exception
	 *             the exception
	 */
	public double evaluate(String expression) throws Exception
	{
		evaluator.setExpression(expression);
		return evaluator.getValue();
	}
}
