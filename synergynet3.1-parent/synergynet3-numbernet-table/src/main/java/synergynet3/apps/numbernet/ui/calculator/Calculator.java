package synergynet3.apps.numbernet.ui.calculator;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import multiplicity3.csys.behaviours.BehaviourMaker;
import multiplicity3.csys.behaviours.RotateTranslateScaleBehaviour;
import multiplicity3.csys.factory.ContentTypeNotBoundException;
import multiplicity3.csys.factory.IContentFactory;
import multiplicity3.csys.gfx.Gradient;
import multiplicity3.csys.gfx.Gradient.GradientDirection;
import multiplicity3.csys.items.border.IRoundedBorder;
import multiplicity3.csys.items.container.IContainer;
import multiplicity3.csys.items.item.IItem;
import multiplicity3.csys.items.keyboard.IKeyboard;
import multiplicity3.csys.items.keyboard.behaviour.IMultiTouchKeyboardListener;
import multiplicity3.csys.items.keyboard.behaviour.KeyboardBehaviour;
import multiplicity3.csys.items.keyboard.model.KeyboardKey;
import multiplicity3.csys.items.mutablelabel.IMutableLabel;
import multiplicity3.csys.items.shapes.IColourRectangle;
import multiplicity3.csys.stage.IStage;
import synergynet3.apps.numbernet.ui.calculator.calckeydef.CalculatorKeyboardDefinition;
import synergynet3.apps.numbernet.ui.calculator.calckeydef.CalculatorKeyboardRenderer;
import synergynet3.apps.numbernet.validation.IValidationChecker.ValidationResult;
import synergynet3.fonts.FontColour;
import synergynet3.fonts.FontUtil;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;

/**
 * The Class Calculator.
 */
public class Calculator implements IMultiTouchKeyboardListener
{

	/** The Constant CHARACTER_DISPLAY_LIMIT. */
	private static final int CHARACTER_DISPLAY_LIMIT = 8;

	/** The behaviour maker. */
	private BehaviourMaker behaviourMaker;

	/** The content factory. */
	private IContentFactory contentFactory;

	/** The current output value. */
	private String currentOutputValue = "";

	/** The keyboard wrapper frame. */
	private IItem keyboardWrapperFrame;

	/** The keypad keyboard. */
	private IKeyboard keypadKeyboard;

	/** The listeners. */
	private List<ICalculatorEventListener> listeners = new ArrayList<ICalculatorEventListener>();

	// private IStage stage;
	/** The output label. */
	private IMutableLabel outputLabel;

	/** The output label background. */
	private IColourRectangle outputLabelBackground;

	/** The owner. */
	private String owner = "";

	/** The owner label. */
	private IMutableLabel ownerLabel;

	/** The parent. */
	private IContainer parent;

	/**
	 * Instantiates a new calculator.
	 *
	 * @param parent
	 *            the parent
	 * @param stage
	 *            the stage
	 */
	public Calculator(IContainer parent, IStage stage)
	{
		this.parent = parent;
		this.behaviourMaker = stage.getBehaviourMaker();
		this.contentFactory = stage.getContentFactory();
	}

	/**
	 * Adds the calculator event listener.
	 *
	 * @param listener
	 *            the listener
	 */
	public void addCalculatorEventListener(ICalculatorEventListener listener)
	{
		if (listeners.contains(listener))
		{
			return;
		}
		listeners.add(listener);
	}

	/**
	 * Builds the and attach.
	 *
	 * @throws ContentTypeNotBoundException
	 *             the content type not bound exception
	 */
	public void buildAndAttach() throws ContentTypeNotBoundException
	{
		float borderTransparency = 0.9f;
		float borderGreyValue = 0.5f;

		keyboardWrapperFrame = contentFactory.create(IContainer.class, "keywf", UUID.randomUUID());
		parent.addItem(keyboardWrapperFrame);

		keypadKeyboard = createKeypad(contentFactory);
		keyboardWrapperFrame.addItem(keypadKeyboard);
		Vector2f keypadSize = new Vector2f((float) keypadKeyboard.getKeyboardDefinition().getBounds().getWidth(), (float) keypadKeyboard.getKeyboardDefinition().getBounds().getHeight());

		KeyboardBehaviour keyboardBehaviour = behaviourMaker.addBehaviour(keypadKeyboard, KeyboardBehaviour.class);
		keyboardBehaviour.addListener(this);

		IRoundedBorder keyboardWrapperFrameBorder = contentFactory.create(IRoundedBorder.class, "border", UUID.randomUUID());
		keyboardWrapperFrame.addItem(keyboardWrapperFrameBorder);
		keyboardWrapperFrameBorder.setBorderWidth(45f);
		keyboardWrapperFrameBorder.setSize(keypadSize.x, keypadSize.y);
		keyboardWrapperFrameBorder.setColor(new ColorRGBA(borderGreyValue, borderGreyValue, borderGreyValue, borderTransparency));

		float outputLabelWidth = keypadSize.x + 60f;
		float outputLabelHeight = 60f;

		IRoundedBorder outputLabelBorder = contentFactory.create(IRoundedBorder.class, "border", UUID.randomUUID());
		keyboardWrapperFrame.addItem(outputLabelBorder);
		outputLabelBorder.setBorderWidth(10f);
		outputLabelBorder.setSize(outputLabelWidth, outputLabelHeight);
		outputLabelBorder.setColor(new ColorRGBA(borderGreyValue, borderGreyValue, borderGreyValue, borderTransparency));

		outputLabelBackground = contentFactory.create(IColourRectangle.class, "labelbg", UUID.randomUUID());
		keyboardWrapperFrame.addItem(outputLabelBackground);
		ColorRGBA topGreen = new ColorRGBA(0.3f, 0.8f, 0.3f, 1f);
		ColorRGBA bottomGreen = new ColorRGBA(0.2f, 0.6f, 0.2f, 1f);
		Gradient greenGradient = new Gradient(topGreen, bottomGreen, GradientDirection.VERTICAL);
		outputLabelBackground.setGradientBackground(greenGradient);
		outputLabelBackground.setSize(outputLabelWidth, outputLabelHeight);

		outputLabel = createKeypadOutputLabel(contentFactory, outputLabelWidth);
		keyboardWrapperFrame.addItem(outputLabel);

		Vector2f outputLocation = new Vector2f(0, 252f);
		outputLabel.setRelativeLocation(outputLocation);
		outputLabel.setRelativeScale(1.5f);
		outputLabelBorder.setRelativeLocation(outputLocation);
		outputLabelBackground.setRelativeLocation(outputLocation);

		ownerLabel = createCalculatorOwnerLabel(contentFactory);
		keyboardWrapperFrame.addItem(ownerLabel);

		keyboardWrapperFrame.getZOrderManager().setAutoBringToTop(false);
		keyboardWrapperFrame.getZOrderManager().setBringToTopPropagatesUp(true);
		keyboardWrapperFrame.getZOrderManager().bringToTop(outputLabel);

		keyboardWrapperFrame.setRelativeScale(0.5f);

		RotateTranslateScaleBehaviour rts = behaviourMaker.addBehaviour(keyboardWrapperFrameBorder, RotateTranslateScaleBehaviour.class);
		rts.setItemActingOn(keyboardWrapperFrame);
		rts.setScaleEnabled(false);
	}

	/**
	 * Gets the display item.
	 *
	 * @return the display item
	 */
	public IItem getDisplayItem()
	{
		return outputLabel;
	}

	/**
	 * Gets the drag and drop target.
	 *
	 * @return the drag and drop target
	 */
	public IItem getDragAndDropTarget()
	{
		return outputLabelBackground;
	}

	/**
	 * Gets the owner.
	 *
	 * @return the owner
	 */
	public String getOwner()
	{
		return this.owner;
	}

	/**
	 * Gets the rotation.
	 *
	 * @return the rotation
	 */
	public float getRotation()
	{
		return keyboardWrapperFrame.getRelativeRotation();
	}

	/**
	 * Checks if is visible.
	 *
	 * @return true, if is visible
	 */
	public boolean isVisible()
	{
		return keyboardWrapperFrame.isVisible();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.csys.items.keyboard.behaviour.IMultiTouchKeyboardListener
	 * #keyPressed(multiplicity3.csys.items.keyboard.model.KeyboardKey, boolean,
	 * boolean, boolean)
	 */
	@Override
	public void keyPressed(KeyboardKey k, boolean shiftDown, boolean altDown, boolean ctlDown)
	{
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.csys.items.keyboard.behaviour.IMultiTouchKeyboardListener
	 * #keyReleased(multiplicity3.csys.items.keyboard.model.KeyboardKey,
	 * boolean, boolean, boolean)
	 */
	@Override
	public void keyReleased(KeyboardKey k, boolean shiftDown, boolean altDown, boolean ctlDown)
	{
		setDisplayStyle(ValidationResult.VALID);
		if (k.getKeyCode() == KeyEvent.VK_C)
		{
			if (currentOutputValue.length() != 0)
			{
				setTextForCalculatorDisplay(currentOutputValue.substring(0, currentOutputValue.length() - 1));
				notifyListenersCharacterRemoved();
			}
		}
		else if (k.getKeyCode() == KeyEvent.VK_BACK_QUOTE)
		{
			if (currentOutputValue.length() < 1)
			{
				return;
			}
			notifyListenersEnterKeyPressed();
		}
		else
		{
			char character = k.getKeyStringRepresentation().charAt(0);
			setTextForCalculatorDisplay(currentOutputValue += character);
			notifyListenersCharacterAdded(character);
		}
	}

	/**
	 * Removes the.
	 */
	public void remove()
	{
		keyboardWrapperFrame.getParentItem().removeItem(keyboardWrapperFrame);
		listeners.clear();
	}

	/**
	 * Sets the display style.
	 *
	 * @param result
	 *            the new display style
	 */
	public void setDisplayStyle(ValidationResult result)
	{
		switch (result)
		{
			case VALID:
			{
				outputLabel.setFont(FontUtil.getFont(FontColour.White));
				break;
			}
			case INVALID:
			{
				outputLabel.setFont(FontUtil.getFont(FontColour.Red));
				break;
			}
			case DUPLICATE:
			{
				outputLabel.setFont(FontUtil.getFont(FontColour.Red));
				break;
			}
			case IDENTICAL:
			{
				outputLabel.setFont(FontUtil.getFont(FontColour.Red));
				break;
			}
		}
	}

	/**
	 * Sets the key visible.
	 *
	 * @param keyStringRepresentation
	 *            the key string representation
	 * @param state
	 *            the state
	 */
	public void setKeyVisible(String keyStringRepresentation, boolean state)
	{
		this.keypadKeyboard.getKeyboardDefinition().getKey(keyStringRepresentation).setEnabled(state);
		this.keypadKeyboard.reDraw();
	}

	/**
	 * Sets the owner.
	 *
	 * @param owner
	 *            the new owner
	 */
	public void setOwner(String owner)
	{
		this.owner = owner;
	}

	/**
	 * Sets the relative location.
	 *
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 */
	public void setRelativeLocation(float x, float y)
	{
		keyboardWrapperFrame.setRelativeLocation(new Vector2f(x, y));
	}

	/**
	 * Sets the text for calculator display.
	 *
	 * @param txt
	 *            the new text for calculator display
	 */
	public void setTextForCalculatorDisplay(String txt)
	{
		currentOutputValue = txt;
		if (txt.length() >= (CHARACTER_DISPLAY_LIMIT + 3))
		{
			outputLabel.setText("..." + txt.substring(txt.length() - (CHARACTER_DISPLAY_LIMIT), txt.length()));
		}
		else
		{
			outputLabel.setText(txt);
		}
		setDisplayStyle(ValidationResult.VALID);
	}

	/**
	 * Sets the visibility.
	 *
	 * @param visible
	 *            the new visibility
	 */
	public void setVisibility(Boolean visible)
	{
		keyboardWrapperFrame.setVisible(visible);
	}

	/**
	 * Creates the calculator owner label.
	 *
	 * @param cf
	 *            the cf
	 * @return the i mutable label
	 * @throws ContentTypeNotBoundException
	 *             the content type not bound exception
	 */
	private IMutableLabel createCalculatorOwnerLabel(IContentFactory cf) throws ContentTypeNotBoundException
	{
		IMutableLabel ownerLabel = cf.create(IMutableLabel.class, "", UUID.randomUUID());
		ownerLabel.setFont(FontUtil.getFont(FontColour.Blue));
		ownerLabel.setText(this.owner);
		ownerLabel.setBoxSize((float) keypadKeyboard.getKeyboardDefinition().getBounds().getWidth(), ownerLabel.getTextSize().y);
		ownerLabel.setRelativeLocation(new Vector2f(0, -190f));
		return ownerLabel;
	}

	/**
	 * Creates the keypad.
	 *
	 * @param cf
	 *            the cf
	 * @return the i keyboard
	 * @throws ContentTypeNotBoundException
	 *             the content type not bound exception
	 */
	private IKeyboard createKeypad(IContentFactory cf) throws ContentTypeNotBoundException
	{
		IKeyboard kb = cf.create(IKeyboard.class, "kb", UUID.randomUUID());
		kb.setKeyboardDefinition(new CalculatorKeyboardDefinition());
		kb.setKeyboardRenderer(new CalculatorKeyboardRenderer(kb.getKeyboardDefinition()));
		kb.reDraw();
		return kb;
	}

	/**
	 * Creates the keypad output label.
	 *
	 * @param cf
	 *            the cf
	 * @param outputLabelWidth
	 *            the output label width
	 * @return the i mutable label
	 * @throws ContentTypeNotBoundException
	 *             the content type not bound exception
	 */
	private IMutableLabel createKeypadOutputLabel(IContentFactory cf, float outputLabelWidth) throws ContentTypeNotBoundException
	{
		IMutableLabel outputLabel = cf.create(IMutableLabel.class, "", UUID.randomUUID());
		outputLabel.setFont(FontUtil.getFont(FontColour.White));
		outputLabel.setText("");
		outputLabel.setBoxSize(outputLabelWidth / 1.6f, outputLabel.getTextSize().y);
		return outputLabel;
	}

	/**
	 * Notify listeners character added.
	 *
	 * @param character
	 *            the character
	 */
	private void notifyListenersCharacterAdded(char character)
	{
		for (ICalculatorEventListener listener : listeners)
		{
			listener.characterAdded(character, this, currentOutputValue);
		}
	}

	/**
	 * Notify listeners character removed.
	 */
	private void notifyListenersCharacterRemoved()
	{
		for (ICalculatorEventListener listener : listeners)
		{
			listener.characterRemoved(this, currentOutputValue);
		}
	}

	/**
	 * Notify listeners enter key pressed.
	 */
	private void notifyListenersEnterKeyPressed()
	{
		for (ICalculatorEventListener listener : listeners)
		{
			listener.enterKeyPressed(this, currentOutputValue);
		}
	}

}
