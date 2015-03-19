package synergynet3.web.apps.numbernet.expressionevaluator;

public class ExpressionEvaluator {

	private static ExpressionEvaluator instance;

	public static ExpressionEvaluator getInstance() {
		synchronized(ExpressionEvaluator.class) { 
			if(instance == null) {
				instance = new ExpressionEvaluator();
			}
		}
		return instance;
	}


	private MathEvaluator evaluator;
	
	public ExpressionEvaluator() {
		evaluator = new MathEvaluator();
	}
	
	public double evaluate(String expression) throws Exception {
		evaluator.setExpression(expression);
		return evaluator.getValue();
	}


	public static void main(String[] args) {
		MathEvaluator m = new MathEvaluator();
		m.setExpression("(4+3)*2");
		try {
			Double value =  m.getValue();
			System.out.println(value );
		}catch(Exception ex) {
			System.out.println("Does not evaluate. " + ex.getMessage());
		}
	}
}
