package synergynet3.web.apps.numbernet.client.expressionview;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import synergynet3.web.apps.numbernet.shared.CalculatorKey;
import synergynet3.web.apps.numbernet.shared.Expression;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * The Class ExpressionCollectionView.
 */
public class ExpressionCollectionView extends VerticalPanel
{

	/** The panel for expressions. */
	private VerticalPanel panelForExpressions;

	/**
	 * Instantiates a new expression collection view.
	 */
	public ExpressionCollectionView()
	{
		super();
		panelForExpressions = new VerticalPanel();
		panelForExpressions.setStylePrimaryName("expressionCollectionBox");
		add(panelForExpressions);
		setSpacing(3);
	}

	/**
	 * Sets the expression collection.
	 *
	 * @param expressions
	 *            the new expression collection
	 */
	public void setExpressionCollection(List<Expression> expressions)
	{
		removeAllExpressionsFromView();
		addExpressionTableHeader();
		addExpressionViewsForEachExpression(expressions);
		addScores(expressions);
		addOperatorFrequencyTable(expressions);
	}

	/**
	 * Adds the expression table header.
	 */
	private void addExpressionTableHeader()
	{
		panelForExpressions.add(new ExpressionTableHeader());
	}

	/**
	 * Adds the expression view for expression.
	 *
	 * @param e
	 *            the e
	 */
	private void addExpressionViewForExpression(Expression e)
	{
		SingleExpressionWidget ev = new SingleExpressionWidget();
		ev.setExpression(e);
		panelForExpressions.add(ev);
	}

	/**
	 * Adds the expression views for each expression.
	 *
	 * @param expressions
	 *            the expressions
	 */
	private void addExpressionViewsForEachExpression(List<Expression> expressions)
	{
		for (Expression e : expressions)
		{
			addExpressionViewForExpression(e);
		}
	}

	/**
	 * Adds the operator frequency table.
	 *
	 * @param expressions
	 *            the expressions
	 */
	private void addOperatorFrequencyTable(List<Expression> expressions)
	{
		Map<String, Integer> operatorFrequencies = getOperatorFrequencies(expressions);
		FlexTable t = new FlexTable();
		t.setStylePrimaryName("operatorFrequencyTable");
		int row = 0;
		t.setText(row, 0, "Operator");
		t.setText(row, 1, "Frequency");
		row++;
		for (String key : operatorFrequencies.keySet())
		{
			t.setText(row, 0, key);
			t.setText(row, 1, "" + operatorFrequencies.get(key));
			row++;
		}
		add(t);
	}

	/**
	 * Adds the scores.
	 *
	 * @param expressions
	 *            the expressions
	 */
	private void addScores(List<Expression> expressions)
	{
		int correct = getCorrectCount(expressions);
		int incorrect = getIncorrectCount(expressions);
		FlexTable t = new FlexTable();
		t.setText(0, 0, "Correct:");
		t.setText(0, 1, "" + correct);
		t.setText(1, 0, "Incorrect:");
		t.setText(1, 1, "" + incorrect);
		add(t);
	}

	/**
	 * Gets the correct count.
	 *
	 * @param expressions
	 *            the expressions
	 * @return the correct count
	 */
	private int getCorrectCount(List<Expression> expressions)
	{
		int count = 0;
		for (Expression e : expressions)
		{
			if (e.isCorrect())
			{
				count++;
			}
		}
		return count;
	}

	/**
	 * Gets the incorrect count.
	 *
	 * @param expressions
	 *            the expressions
	 * @return the incorrect count
	 */
	private int getIncorrectCount(List<Expression> expressions)
	{
		int count = 0;
		for (Expression e : expressions)
		{
			if (!e.isCorrect())
			{
				count++;
			}
		}
		return count;
	}

	/**
	 * Gets the operator frequencies.
	 *
	 * @param expressions
	 *            the expressions
	 * @return the operator frequencies
	 */
	private Map<String, Integer> getOperatorFrequencies(List<Expression> expressions)
	{
		int plusFreq = 0;
		int minusFreq = 0;
		int divideFreq = 0;
		int multiplyFreq = 0;
		for (Expression e : expressions)
		{
			if (e.getExpression().indexOf(CalculatorKey.PLUS.getStringRepresentation()) != -1)
			{
				plusFreq++;
			}
			if (e.getExpression().indexOf(CalculatorKey.MINUS.getStringRepresentation()) != -1)
			{
				minusFreq++;
			}
			if (e.getExpression().indexOf(CalculatorKey.DIVIDE.getStringRepresentation()) != -1)
			{
				divideFreq++;
			}
			if (e.getExpression().indexOf(CalculatorKey.MULTIPLY.getStringRepresentation()) != -1)
			{
				multiplyFreq++;
			}
		}

		Map<String, Integer> frequencies = new HashMap<String, Integer>();
		frequencies.put(CalculatorKey.PLUS.getStringRepresentation(), plusFreq);
		frequencies.put(CalculatorKey.MINUS.getStringRepresentation(), minusFreq);
		frequencies.put(CalculatorKey.DIVIDE.getStringRepresentation(), divideFreq);
		frequencies.put(CalculatorKey.MULTIPLY.getStringRepresentation(), multiplyFreq);
		return frequencies;
	}

	/**
	 * Removes the all expressions from view.
	 */
	private void removeAllExpressionsFromView()
	{
		int widgets = panelForExpressions.getWidgetCount();
		for (int i = 0; i < widgets; i++)
		{
			panelForExpressions.remove(i);
		}
	}
}
