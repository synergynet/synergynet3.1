package synergynet3.web.apps.numbernet.client.calckeys;

import java.util.HashMap;
import java.util.Map;

import synergynet3.web.apps.numbernet.shared.CalculatorKey;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * The Class CalculatorKeyControlPanel.
 */
public class CalculatorKeyControlPanel extends VerticalPanel
{

	/**
	 * The Interface CalculatorKeyControlPanelDelegate.
	 */
	public static interface CalculatorKeyControlPanelDelegate
	{

		/**
		 * Key state changed.
		 *
		 * @param key
		 *            the key
		 * @param state
		 *            the state
		 */
		public void keyStateChanged(CalculatorKey key, boolean state);
	}

	/** The delegate. */
	public CalculatorKeyControlPanelDelegate delegate;

	/** The key to check box. */
	private Map<CalculatorKey, CheckBox> keyToCheckBox;

	/**
	 * Instantiates a new calculator key control panel.
	 */
	public CalculatorKeyControlPanel()
	{
		super();

		keyToCheckBox = new HashMap<CalculatorKey, CheckBox>();

		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.setSpacing(5);
		add(horizontalPanel);
		horizontalPanel.setWidth("428px");

		final CheckBox chckbxPlus = new CheckBox("Plus (+)");
		keyToCheckBox.put(CalculatorKey.PLUS, chckbxPlus);
		chckbxPlus.addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(ClickEvent event)
			{
				if (delegate != null)
				{
					delegate.keyStateChanged(CalculatorKey.PLUS, chckbxPlus.getValue());
				}
			}
		});
		chckbxPlus.setValue(true);
		horizontalPanel.add(chckbxPlus);

		final CheckBox chckbxMinus = new CheckBox("Minus (-)");
		keyToCheckBox.put(CalculatorKey.MINUS, chckbxMinus);
		chckbxMinus.addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(ClickEvent event)
			{
				if (delegate != null)
				{
					delegate.keyStateChanged(CalculatorKey.MINUS, chckbxMinus.getValue());
				}
			}
		});
		chckbxMinus.setValue(true);
		horizontalPanel.add(chckbxMinus);

		final CheckBox chckbxMultiply = new CheckBox("Multiply (x)");
		keyToCheckBox.put(CalculatorKey.MULTIPLY, chckbxMultiply);
		chckbxMultiply.addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(ClickEvent event)
			{
				if (delegate != null)
				{
					delegate.keyStateChanged(CalculatorKey.MULTIPLY, chckbxMultiply.getValue());
				}
			}
		});
		chckbxMultiply.setValue(true);
		horizontalPanel.add(chckbxMultiply);

		final CheckBox chckbxDivide = new CheckBox("Divide (/)");
		keyToCheckBox.put(CalculatorKey.DIVIDE, chckbxDivide);
		chckbxDivide.addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(ClickEvent event)
			{
				if (delegate != null)
				{
					delegate.keyStateChanged(CalculatorKey.DIVIDE, chckbxDivide.getValue());
				}
			}
		});
		chckbxDivide.setValue(true);
		horizontalPanel.add(chckbxDivide);

		HorizontalPanel horizontalPanel_1 = new HorizontalPanel();
		horizontalPanel_1.setSpacing(10);
		add(horizontalPanel_1);

		final CheckBox chckbxSeven = new CheckBox("7");
		keyToCheckBox.put(CalculatorKey.SEVEN, chckbxSeven);
		chckbxSeven.setValue(true);
		chckbxSeven.addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(ClickEvent event)
			{
				if (delegate != null)
				{
					delegate.keyStateChanged(CalculatorKey.SEVEN, chckbxSeven.getValue());
				}
			}
		});
		horizontalPanel_1.add(chckbxSeven);

		final CheckBox chckbxEight = new CheckBox("8");
		keyToCheckBox.put(CalculatorKey.EIGHT, chckbxEight);
		chckbxEight.setValue(true);
		chckbxEight.addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(ClickEvent event)
			{
				if (delegate != null)
				{
					delegate.keyStateChanged(CalculatorKey.EIGHT, chckbxEight.getValue());
				}
			}
		});
		horizontalPanel_1.add(chckbxEight);

		final CheckBox chckbxNine = new CheckBox("9");
		keyToCheckBox.put(CalculatorKey.NINE, chckbxNine);
		chckbxNine.setValue(true);
		chckbxNine.addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(ClickEvent event)
			{
				if (delegate != null)
				{
					delegate.keyStateChanged(CalculatorKey.NINE, chckbxNine.getValue());
				}
			}
		});
		horizontalPanel_1.add(chckbxNine);

		HorizontalPanel horizontalPanel_2 = new HorizontalPanel();
		horizontalPanel_2.setSpacing(10);
		add(horizontalPanel_2);

		final CheckBox chckbxFour = new CheckBox("4");
		keyToCheckBox.put(CalculatorKey.FOUR, chckbxFour);
		chckbxFour.setValue(true);
		chckbxFour.addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(ClickEvent event)
			{
				if (delegate != null)
				{
					delegate.keyStateChanged(CalculatorKey.FOUR, chckbxFour.getValue());
				}
			}
		});
		horizontalPanel_2.add(chckbxFour);

		final CheckBox chckbxFive = new CheckBox("5");
		keyToCheckBox.put(CalculatorKey.FIVE, chckbxFive);
		chckbxFive.setValue(true);
		chckbxFive.addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(ClickEvent event)
			{
				if (delegate != null)
				{
					delegate.keyStateChanged(CalculatorKey.FIVE, chckbxFive.getValue());
				}
			}
		});
		horizontalPanel_2.add(chckbxFive);

		final CheckBox chckbxSix = new CheckBox("6");
		keyToCheckBox.put(CalculatorKey.SIX, chckbxSix);
		chckbxSix.setValue(true);
		chckbxSix.addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(ClickEvent event)
			{
				if (delegate != null)
				{
					delegate.keyStateChanged(CalculatorKey.SIX, chckbxSix.getValue());
				}
			}
		});
		horizontalPanel_2.add(chckbxSix);

		final CheckBox chckbxLeftBracket = new CheckBox("(");
		keyToCheckBox.put(CalculatorKey.LEFTBRACKET, chckbxLeftBracket);
		chckbxLeftBracket.setValue(true);
		chckbxLeftBracket.addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(ClickEvent event)
			{
				if (delegate != null)
				{
					delegate.keyStateChanged(CalculatorKey.LEFTBRACKET, chckbxLeftBracket.getValue());
				}
			}
		});
		horizontalPanel_2.add(chckbxLeftBracket);

		HorizontalPanel horizontalPanel_3 = new HorizontalPanel();
		horizontalPanel_3.setSpacing(10);
		add(horizontalPanel_3);

		final CheckBox chckbxOne = new CheckBox("1");
		keyToCheckBox.put(CalculatorKey.ONE, chckbxOne);
		chckbxOne.setValue(true);
		chckbxOne.addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(ClickEvent event)
			{
				if (delegate != null)
				{
					delegate.keyStateChanged(CalculatorKey.ONE, chckbxOne.getValue());
				}
			}
		});
		horizontalPanel_3.add(chckbxOne);

		final CheckBox chckbxTwo = new CheckBox("2");
		keyToCheckBox.put(CalculatorKey.TWO, chckbxTwo);
		chckbxTwo.setValue(true);
		chckbxTwo.addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(ClickEvent event)
			{
				if (delegate != null)
				{
					delegate.keyStateChanged(CalculatorKey.TWO, chckbxTwo.getValue());
				}
			}
		});
		horizontalPanel_3.add(chckbxTwo);

		final CheckBox chckbxThree = new CheckBox("3");
		keyToCheckBox.put(CalculatorKey.THREE, chckbxThree);
		chckbxThree.setValue(true);
		chckbxThree.addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(ClickEvent event)
			{
				if (delegate != null)
				{
					delegate.keyStateChanged(CalculatorKey.THREE, chckbxThree.getValue());
				}
			}
		});
		horizontalPanel_3.add(chckbxThree);

		final CheckBox chckbxRightBracket = new CheckBox(")");
		keyToCheckBox.put(CalculatorKey.RIGHTBRACKET, chckbxRightBracket);
		chckbxRightBracket.setValue(true);
		chckbxRightBracket.addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(ClickEvent event)
			{
				if (delegate != null)
				{
					delegate.keyStateChanged(CalculatorKey.RIGHTBRACKET, chckbxRightBracket.getValue());
				}
			}
		});
		horizontalPanel_3.add(chckbxRightBracket);

		HorizontalPanel horizontalPanel_4 = new HorizontalPanel();
		horizontalPanel_4.setSpacing(10);
		add(horizontalPanel_4);

		final CheckBox chckbxZero = new CheckBox("0");
		keyToCheckBox.put(CalculatorKey.ZERO, chckbxZero);
		chckbxZero.setValue(true);
		chckbxZero.addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(ClickEvent event)
			{
				if (delegate != null)
				{
					delegate.keyStateChanged(CalculatorKey.ZERO, chckbxZero.getValue());
				}
			}
		});
		horizontalPanel_4.add(chckbxZero);

		final CheckBox chckbxDot = new CheckBox("Point ( . )");
		keyToCheckBox.put(CalculatorKey.POINT, chckbxDot);
		chckbxDot.setValue(true);
		chckbxDot.addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(ClickEvent event)
			{
				if (delegate != null)
				{
					delegate.keyStateChanged(CalculatorKey.POINT, chckbxDot.getValue());
				}
			}
		});
		horizontalPanel_4.add(chckbxDot);
	}

	/**
	 * Sets the values for check boxes with key state info.
	 *
	 * @param keyStateInfo
	 *            the key state info
	 */
	public void setValuesForCheckBoxesWithKeyStateInfo(Map<CalculatorKey, Boolean> keyStateInfo)
	{
		for (CalculatorKey key : keyStateInfo.keySet())
		{
			Boolean value = keyStateInfo.get(key);
			if (value != null)
			{
				keyToCheckBox.get(key).setValue(value);
			}
		}
	}

}
