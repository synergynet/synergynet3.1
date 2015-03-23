package synergynet3.web.apps.numbernet.expressionevaluator;

import java.util.HashMap;

/**
 * Java Math Expression Evaluator Freeware V1.01 by The-Son LAI
 * laitheson@gmail.com http://Lts.online.fr
 */

/************************************************************************
 * <i>Mathematic expression evaluator.</i> Supports the following functions: +,
 * -, *, /, ^, %, cos, sin, tan, acos, asin, atan, sqrt, sqr, log, min, max,
 * ceil, floor, abs, neg, rndr.<br>
 * When the getValue() is called, a Double object is returned. If it returns
 * null, an error occured.
 * <p>
 * 
 * <pre>
 * Sample:
 * MathEvaluator m = new MathEvaluator("-5-6/(-2) + sqr(15+x)");
 * m.addVariable("x", 15.1d);
 * System.out.println( m.getValue() );
 * </pre>
 * 
 * @version 1.1
 * @author The-Son LAI, <a href="mailto:Lts@writeme.com">Lts@writeme.com</a>
 * @date April 2001
 ************************************************************************/
public class MathEvaluator {

	/**
	 * The Class Node.
	 */
	protected class Node {

		/** The n left. */
		public Node nLeft = null;

		/** The n level. */
		public int nLevel = 0;

		/** The n operator. */
		public Operator nOperator = null;

		/** The n parent. */
		public Node nParent = null;

		/** The n right. */
		public Node nRight = null;

		/** The n string. */
		public String nString = null;

		/** The n value. */
		public Double nValue = null;

		/**
		 * Instantiates a new node.
		 *
		 * @param parent the parent
		 * @param s the s
		 * @param level the level
		 * @throws Exception the exception
		 */
		public Node(Node parent, String s, int level) throws Exception {
			init(parent, s, level);
		}

		/**
		 * Instantiates a new node.
		 *
		 * @param s the s
		 * @throws Exception the exception
		 */
		public Node(String s) throws Exception {
			init(null, s, 0);
		}

		/***
		 * Removes spaces, tabs and brackets at the begining
		 */
		public String removeBrackets(String s) {
			String res = s;
			if ((s.length() > 2) && res.startsWith("(") && res.endsWith(")")
					&& (checkBrackets(s.substring(1, s.length() - 1)) == 0)) {
				res = res.substring(1, res.length() - 1);
			}
			if (res != s) {
				return removeBrackets(res);
			} else {
				return res;
			}
		}

		/***
		 * Removes illegal characters
		 */
		public String removeIllegalCharacters(String s) {
			char[] illegalCharacters = { ' ' };
			String res = s;

			for (int j = 0; j < illegalCharacters.length; j++) {
				int i = res.lastIndexOf(illegalCharacters[j], res.length());
				while (i != -1) {
					String temp = res;
					res = temp.substring(0, i);
					res += temp.substring(i + 1);
					i = res.lastIndexOf(illegalCharacters[j], s.length());
				}
			}
			return res;
		}

		/***
		 * displays the tree of the expression
		 */
		public void trace() {
			String op = getOperator() == null ? " " : getOperator()
					.getOperator();
			_D(op + " : " + getString());
			if (this.hasChild()) {
				if (hasLeft()) {
					getLeft().trace();
				}
				if (hasRight()) {
					getRight().trace();
				}
			}
		}

		/**
		 * Gets the next word.
		 *
		 * @param s the s
		 * @return the next word
		 */
		private String getNextWord(String s) {
			int sLength = s.length();
			for (int i = 1; i < sLength; i++) {
				char c = s.charAt(i);
				if (((c > 'z') || (c < 'a')) && ((c > '9') || (c < '0'))) {
					return s.substring(0, i);
				}
			}
			return s;
		}

		/**
		 * Gets the operator.
		 *
		 * @param s the s
		 * @param start the start
		 * @return the operator
		 */
		private Operator getOperator(String s, int start) {
			Operator[] operators = getOperators();
			String temp = s.substring(start);
			temp = getNextWord(temp);
			for (int i = 0; i < operators.length; i++) {
				if (temp.startsWith(operators[i].getOperator())) {
					return operators[i];
				}
			}
			return null;
		}

		/**
		 * Inits the.
		 *
		 * @param parent the parent
		 * @param s the s
		 * @param level the level
		 * @throws Exception the exception
		 */
		private void init(Node parent, String s, int level) throws Exception {
			s = removeIllegalCharacters(s);
			s = removeBrackets(s);
			s = addZero(s);
			if (checkBrackets(s) != 0) {
				throw new Exception("Wrong number of brackets in [" + s + "]");
			}

			nParent = parent;
			nString = s;
			nValue = getDouble(s);
			nLevel = level;
			int sLength = s.length();
			int inBrackets = 0;
			int startOperator = 0;

			for (int i = 0; i < sLength; i++) {
				if (s.charAt(i) == '(') {
					inBrackets++;
				} else if (s.charAt(i) == ')') {
					inBrackets--;
				} else {
					// the expression must be at "root" level
					if (inBrackets == 0) {
						Operator o = getOperator(nString, i);
						if (o != null) {
							// if first operator or lower priority operator
							if ((nOperator == null)
									|| (nOperator.getPriority() >= o
											.getPriority())) {
								nOperator = o;
								startOperator = i;
							}
						}
					}
				}
			}

			if (nOperator != null) {
				// one operand, should always be at the beginning
				if ((startOperator == 0) && (nOperator.getType() == 1)) {
					// the brackets must be ok
					if (checkBrackets(s.substring(nOperator.getOperator()
							.length())) == 0) {
						nLeft = new Node(this, s.substring(nOperator
								.getOperator().length()), nLevel + 1);
						nRight = null;
						return;
					} else {
						throw new Exception(
								"Error during parsing... missing brackets in ["
										+ s + "]");
					}
				}
				// two operands
				else if ((startOperator > 0) && (nOperator.getType() == 2)) {
					// nOperator = nOperator;
					nLeft = new Node(this, s.substring(0, startOperator),
							nLevel + 1);
					nRight = new Node(this, s.substring(startOperator
							+ nOperator.getOperator().length()), nLevel + 1);
				}
			}
		}

		/**
		 * d.
		 *
		 * @param s the s
		 */
		protected void _D(String s) {
			String nbSpaces = "";
			for (int i = 0; i < nLevel; i++) {
				nbSpaces += "  ";
			}
			System.out.println(nbSpaces + "|" + s);
		}

		/***
		 * returns a string that doesnt start with a + or a -
		 */
		protected String addZero(String s) {
			if (s.startsWith("+") || s.startsWith("-")) {
				int sLength = s.length();
				for (int i = 0; i < sLength; i++) {
					if (getOperator(s, i) != null) {
						return "0" + s;
					}
				}
			}

			return s;
		}

		/***
		 * checks if there is any missing brackets
		 * 
		 * @return true if s is valid
		 */
		protected int checkBrackets(String s) {
			int sLength = s.length();
			int inBracket = 0;

			for (int i = 0; i < sLength; i++) {
				if ((s.charAt(i) == '(') && (inBracket >= 0)) {
					inBracket++;
				} else if (s.charAt(i) == ')') {
					inBracket--;
				}
			}

			return inBracket;
		}

		/**
		 * Gets the left.
		 *
		 * @return the left
		 */
		protected Node getLeft() {
			return nLeft;
		}

		/**
		 * Gets the level.
		 *
		 * @return the level
		 */
		protected int getLevel() {
			return nLevel;
		}

		/**
		 * Gets the operator.
		 *
		 * @return the operator
		 */
		protected Operator getOperator() {
			return nOperator;
		}

		/**
		 * Gets the right.
		 *
		 * @return the right
		 */
		protected Node getRight() {
			return nRight;
		}

		/**
		 * Gets the string.
		 *
		 * @return the string
		 */
		protected String getString() {
			return nString;
		}

		/**
		 * Gets the value.
		 *
		 * @return the value
		 */
		protected Double getValue() {
			return nValue;
		}

		/**
		 * Checks for child.
		 *
		 * @return true, if successful
		 */
		protected boolean hasChild() {
			return ((nLeft != null) || (nRight != null));
		}

		/**
		 * Checks for left.
		 *
		 * @return true, if successful
		 */
		protected boolean hasLeft() {
			return (nLeft != null);
		}

		/**
		 * Checks for operator.
		 *
		 * @return true, if successful
		 */
		protected boolean hasOperator() {
			return (nOperator != null);
		}

		/**
		 * Checks for right.
		 *
		 * @return true, if successful
		 */
		protected boolean hasRight() {
			return (nRight != null);
		}

		/**
		 * Sets the value.
		 *
		 * @param f the new value
		 */
		protected void setValue(Double f) {
			nValue = f;
		}
	}

	/**
	 * The Class Operator.
	 */
	protected class Operator {

		/** The op. */
		private String op;

		/** The priority. */
		private int priority;

		/** The type. */
		private int type;

		/**
		 * Instantiates a new operator.
		 *
		 * @param o the o
		 * @param t the t
		 * @param p the p
		 */
		public Operator(String o, int t, int p) {
			op = o;
			type = t;
			priority = p;
		}

		/**
		 * Gets the operator.
		 *
		 * @return the operator
		 */
		public String getOperator() {
			return op;
		}

		/**
		 * Gets the priority.
		 *
		 * @return the priority
		 */
		public int getPriority() {
			return priority;
		}

		/**
		 * Gets the type.
		 *
		 * @return the type
		 */
		public int getType() {
			return type;
		}

		/**
		 * Sets the operator.
		 *
		 * @param o the new operator
		 */
		public void setOperator(String o) {
			op = o;
		}
	}

	/** The operators. */
	protected static Operator[] operators = null;

	/** The expression. */
	private String expression = null;

	/** The node. */
	private Node node = null;

	/** The variables. */
	private HashMap<String, Double> variables = new HashMap<String, Double>();

	/***
	 * creates an empty MathEvaluator. You need to use setExpression(String s)
	 * to assign a math expression string to it.
	 */
	public MathEvaluator() {
		init();
	}

	/***
	 * creates a MathEvaluator and assign the math expression string.
	 */
	public MathEvaluator(String s) {
		init();
		setExpression(s);
	}

	/***
	 * Main. To run the program in command line. Usage: java MathEvaluator.main
	 * [your math expression]
	 */
	public static void main(String[] args) {
		if ((args == null) || (args.length != 1)) {
			System.err
					.println("Math Expression Evaluator by The-Son LAI Lts@writeme.com C(2001):");
			System.err
					.println("Usage: java MathEvaluator.main [your math expression]");
			System.exit(0);
		}

		try {
			MathEvaluator m = new MathEvaluator(args[0]);
			System.out.println(m.getValue());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Evaluate.
	 *
	 * @param n the n
	 * @return the double
	 */
	private static Double evaluate(Node n) {
		if (n.hasOperator() && n.hasChild()) {
			if (n.getOperator().getType() == 1) {
				n.setValue(evaluateExpression(n.getOperator(),
						evaluate(n.getLeft()), null));
			} else if (n.getOperator().getType() == 2) {
				n.setValue(evaluateExpression(n.getOperator(),
						evaluate(n.getLeft()), evaluate(n.getRight())));
			}
		}
		return n.getValue();
	}

	/**
	 * Evaluate expression.
	 *
	 * @param o the o
	 * @param f1 the f1
	 * @param f2 the f2
	 * @return the double
	 */
	private static Double evaluateExpression(Operator o, Double f1, Double f2) {
		String op = o.getOperator();
		Double res = null;

		if ("+".equals(op)) {
			res = new Double(f1.doubleValue() + f2.doubleValue());
		} else if ("-".equals(op)) {
			res = new Double(f1.doubleValue() - f2.doubleValue());
		} else if ("*".equals(op)) {
			res = new Double(f1.doubleValue() * f2.doubleValue());
		} else if ("\u00D7".equals(op)) {
			res = new Double(f1.doubleValue() * f2.doubleValue());
		} else if ("/".equals(op)) {
			res = new Double(f1.doubleValue() / f2.doubleValue());
		} else if ("\u00F7".equals(op)) {
			res = new Double(f1.doubleValue() / f2.doubleValue());
		} else if ("^".equals(op)) {
			res = new Double(Math.pow(f1.doubleValue(), f2.doubleValue()));
		} else if ("%".equals(op)) {
			res = new Double(f1.doubleValue() % f2.doubleValue());
		} else if ("&".equals(op)) {
			res = new Double(f1.doubleValue() + f2.doubleValue()); // todo
		} else if ("|".equals(op)) {
			res = new Double(f1.doubleValue() + f2.doubleValue()); // todo
		} else if ("cos".equals(op)) {
			res = new Double(Math.cos(f1.doubleValue()));
		} else if ("sin".equals(op)) {
			res = new Double(Math.sin(f1.doubleValue()));
		} else if ("tan".equals(op)) {
			res = new Double(Math.tan(f1.doubleValue()));
		} else if ("acos".equals(op)) {
			res = new Double(Math.acos(f1.doubleValue()));
		} else if ("asin".equals(op)) {
			res = new Double(Math.asin(f1.doubleValue()));
		} else if ("atan".equals(op)) {
			res = new Double(Math.atan(f1.doubleValue()));
		} else if ("sqr".equals(op)) {
			res = new Double(f1.doubleValue() * f1.doubleValue());
		} else if ("sqrt".equals(op)) {
			res = new Double(Math.sqrt(f1.doubleValue()));
		} else if ("log".equals(op)) {
			res = new Double(Math.log(f1.doubleValue()));
		} else if ("min".equals(op)) {
			res = new Double(Math.min(f1.doubleValue(), f2.doubleValue()));
		} else if ("max".equals(op)) {
			res = new Double(Math.max(f1.doubleValue(), f2.doubleValue()));
		} else if ("exp".equals(op)) {
			res = new Double(Math.exp(f1.doubleValue()));
		} else if ("floor".equals(op)) {
			res = new Double(Math.floor(f1.doubleValue()));
		} else if ("ceil".equals(op)) {
			res = new Double(Math.ceil(f1.doubleValue()));
		} else if ("abs".equals(op)) {
			res = new Double(Math.abs(f1.doubleValue()));
		} else if ("neg".equals(op)) {
			res = new Double(-f1.doubleValue());
		} else if ("rnd".equals(op)) {
			res = new Double(Math.random() * f1.doubleValue());
		}

		return res;
	}

	/**
	 * d.
	 *
	 * @param s the s
	 */
	protected static void _D(String s) {
		System.err.println(s);
	}

	/***
	 * adds a variable and its value in the MathEvaluator
	 */
	public void addVariable(String v, double val) {
		addVariable(v, new Double(val));
	}

	/***
	 * adds a variable and its value in the MathEvaluator
	 */
	public void addVariable(String v, Double val) {
		variables.put(v, val);
	}

	/***
	 * evaluates and returns the value of the expression
	 */
	public Double getValue() throws Exception {
		if (expression == null) {
			return null;
		}

		// try
		// {
		node = new Node(expression);
		return evaluate(node);
		// }
		// catch (Exception e)
		// {
		// e.printStackTrace();
		// return null;
		// }
	}

	/***
	 * gets the variable's value that was assigned previously
	 */
	public Double getVariable(String s) {
		return variables.get(s);
	}

	/***
	 * resets the evaluator
	 */
	public void reset() {
		node = null;
		expression = null;
		variables = new HashMap<String, Double>();
	}

	/***
	 * sets the expression
	 */
	public void setExpression(String s) {
		expression = s;
	}

	/***
	 * trace the binary tree for debug
	 */
	public void trace() {
		try {
			node = new Node(expression);
			node.trace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gets the double.
	 *
	 * @param s the s
	 * @return the double
	 */
	private Double getDouble(String s) {
		if (s == null) {
			return null;
		}

		Double res = null;
		try {
			res = new Double(Double.parseDouble(s));
		} catch (Exception e) {
			return getVariable(s);
		}

		return res;
	}

	/**
	 * Inits the.
	 */
	private void init() {
		if (operators == null) {
			initializeOperators();
		}
	}

	/**
	 * Initialize operators.
	 */
	private void initializeOperators() {
		operators = new Operator[27];
		operators[0] = new Operator("+", 2, 0);
		operators[1] = new Operator("-", 2, 0);
		operators[2] = new Operator("*", 2, 10);
		operators[3] = new Operator("\u00D7", 2, 10);
		operators[4] = new Operator("/", 2, 10);
		operators[5] = new Operator("\u00F7", 2, 10);
		operators[6] = new Operator("^", 2, 10);
		operators[7] = new Operator("%", 2, 10);
		operators[8] = new Operator("&", 2, 0);
		operators[9] = new Operator("|", 2, 0);
		operators[10] = new Operator("cos", 1, 20);
		operators[11] = new Operator("sin", 1, 20);
		operators[12] = new Operator("tan", 1, 20);
		operators[13] = new Operator("acos", 1, 20);
		operators[14] = new Operator("asin", 1, 20);
		operators[15] = new Operator("atan", 1, 20);
		operators[16] = new Operator("sqrt", 1, 20);
		operators[17] = new Operator("sqr", 1, 20);
		operators[18] = new Operator("log", 1, 20);
		operators[19] = new Operator("min", 2, 0);
		operators[20] = new Operator("max", 2, 0);
		operators[21] = new Operator("exp", 1, 20);
		operators[22] = new Operator("floor", 1, 20);
		operators[23] = new Operator("ceil", 1, 20);
		operators[24] = new Operator("abs", 1, 20);
		operators[25] = new Operator("neg", 1, 20);
		operators[26] = new Operator("rnd", 1, 20);
	}

	/**
	 * Gets the operators.
	 *
	 * @return the operators
	 */
	protected Operator[] getOperators() {
		return operators;
	}
}