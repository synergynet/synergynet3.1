package synergynet3.web.apps.numbernet.client.expressionview;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import synergynet3.web.apps.numbernet.shared.CalculatorKey;
import synergynet3.web.apps.numbernet.shared.Expression;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ExpressionCollectionView extends VerticalPanel {
	
	private VerticalPanel panelForExpressions;
	
	public ExpressionCollectionView() {
		super();
		panelForExpressions = new VerticalPanel();
		panelForExpressions.setStylePrimaryName("expressionCollectionBox");
		add(panelForExpressions);
		setSpacing(3);
	}
	
	public void setExpressionCollection(List<Expression> expressions) {
		removeAllExpressionsFromView();
		addExpressionTableHeader();
		addExpressionViewsForEachExpression(expressions);
		addScores(expressions);
		addOperatorFrequencyTable(expressions);
	}

	


	private void addScores(List<Expression> expressions) {
		int correct = getCorrectCount(expressions);
		int incorrect = getIncorrectCount(expressions);
		FlexTable t = new FlexTable();
		t.setText(0, 0, "Correct:"); t.setText(0, 1, "" + correct);
		t.setText(1, 0, "Incorrect:"); t.setText(1, 1, "" + incorrect);
		add(t);
	}

	private int getIncorrectCount(List<Expression> expressions) {
		int count = 0;
		for(Expression e : expressions) {
			if(!e.isCorrect()) count++;
		}
		return count;
	}

	private int getCorrectCount(List<Expression> expressions) {
		int count = 0;
		for(Expression e : expressions) {
			if(e.isCorrect()) count++;
		}
		return count;
	}

	private void addExpressionTableHeader() {
		panelForExpressions.add(new ExpressionTableHeader());
	}

	private void addExpressionViewsForEachExpression(List<Expression> expressions) {
		for(Expression e : expressions) {
			addExpressionViewForExpression(e);
		}
	}

	private void addExpressionViewForExpression(Expression e) {
		SingleExpressionWidget ev = new SingleExpressionWidget();
		ev.setExpression(e);
		panelForExpressions.add(ev);
	}

	private void removeAllExpressionsFromView() {
		int widgets = panelForExpressions.getWidgetCount();
		for(int i = 0; i < widgets; i++) {
			panelForExpressions.remove(i);
		}
	}
	
	private void addOperatorFrequencyTable(List<Expression> expressions) {
		Map<String,Integer> operatorFrequencies = getOperatorFrequencies(expressions);
		FlexTable t = new FlexTable();
		t.setStylePrimaryName("operatorFrequencyTable");
		int row = 0;
		t.setText(row, 0, "Operator");
		t.setText(row, 1, "Frequency");
		row++;
		for(String key : operatorFrequencies.keySet()) {
			t.setText(row, 0, key);
			t.setText(row, 1, "" + operatorFrequencies.get(key));
			row++;
		}
		add(t);		
	}

	
	private Map<String, Integer> getOperatorFrequencies(
			List<Expression> expressions) {		
		int plusFreq = 0;
		int minusFreq = 0;
		int divideFreq = 0;
		int multiplyFreq = 0;
		for(Expression e : expressions) {
			if(e.getExpression().indexOf(CalculatorKey.PLUS.getStringRepresentation()) != -1) {
				plusFreq++;
			}
			if(e.getExpression().indexOf(CalculatorKey.MINUS.getStringRepresentation()) != -1) {
				minusFreq++;
			}
			if(e.getExpression().indexOf(CalculatorKey.DIVIDE.getStringRepresentation()) != -1) {
				divideFreq++;
			}
			if(e.getExpression().indexOf(CalculatorKey.MULTIPLY.getStringRepresentation()) != -1) {
				multiplyFreq++;
			}			
		}
		
		Map<String,Integer> frequencies = new HashMap<String,Integer>();
		frequencies.put(CalculatorKey.PLUS.getStringRepresentation(), plusFreq);
		frequencies.put(CalculatorKey.MINUS.getStringRepresentation(), minusFreq);
		frequencies.put(CalculatorKey.DIVIDE.getStringRepresentation(), divideFreq);
		frequencies.put(CalculatorKey.MULTIPLY.getStringRepresentation(), multiplyFreq);
		return frequencies;
	}
}
