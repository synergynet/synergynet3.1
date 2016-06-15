package synergynet3.additionalitems.jme;

import java.util.UUID;

import multiplicity3.csys.annotations.ImplementsContentItem;
import multiplicity3.csys.behaviours.RotateTranslateScaleBehaviour;
import multiplicity3.csys.factory.ContentTypeNotBoundException;
import multiplicity3.csys.items.border.IRoundedBorder;
import multiplicity3.csys.items.shapes.IColourRectangle;
import multiplicity3.csys.stage.IStage;
import multiplicity3.input.MultiTouchEventAdapter;
import multiplicity3.input.events.MultiTouchCursorEvent;
import multiplicity3.jme3csys.items.IInitable;
import multiplicity3.jme3csys.items.container.JMEContainer;
import synergynet3.additionalUtils.IgnoreDoubleClick;
import synergynet3.additionalitems.interfaces.IButtonbox;
import synergynet3.additionalitems.interfaces.ISimpleKeypad;
import synergynet3.fonts.FontColour;
import synergynet3.keyboard.KeyboardOutput;
import synergynet3.keyboard.KeyboardSpecialKeys;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;

/**
 * The Class SimpleKeypad.
 */
@ImplementsContentItem(target = ISimpleKeypad.class)
public class SimpleKeypad extends JMEContainer implements IInitable, ISimpleKeypad
{

	/** The bg colour. */
	private ColorRGBA bgColour = ColorRGBA.Black;

	/** The board border colour. */
	private ColorRGBA boardBorderColour = ColorRGBA.Gray;

	/** The button size. */
	private float buttonSize = 40;

	/** The button spacing. */
	private float buttonSpacing = 50;

	/** The font colour. */
	private FontColour fontColour = FontColour.White;

	/** The height. */
	private float height = buttonSpacing * 5;

	/** The keyboard output. */
	private KeyboardOutput keyboardOutput;

	/** The key border colour. */
	private ColorRGBA keyBorderColour = ColorRGBA.Gray;

	/** The key colour. */
	private ColorRGBA keyColour = ColorRGBA.Black;

	/** The max scale. */
	private float minScale, maxScale = 1;

	/** The movable. */
	private boolean movable = true;

	/** The rts border. */
	private RotateTranslateScaleBehaviour rtsBackground, rtsBorder;

	/** The scale limits set. */
	private boolean scaleLimitsSet = false;

	/** The stage. */
	private IStage stage;

	/** The width. */
	private float width = buttonSpacing * 4;

	/** The x. */
	private float x = -width / 2;

	/** The y. */
	private float y = height / 2;

	/**
	 * Instantiates a new simple keypad.
	 *
	 * @param name
	 *            the name
	 * @param uuid
	 *            the uuid
	 */
	public SimpleKeypad(String name, UUID uuid)
	{
		super(name, uuid);
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.additionalitems.interfaces.ISimpleKeypad#generateKeys(
	 * multiplicity3.csys.stage.IStage, synergynet3.keyboard.KeyboardOutput)
	 */
	@Override
	public void generateKeys(IStage stage, KeyboardOutput keyboardOutput)
	{
		this.stage = stage;
		this.keyboardOutput = keyboardOutput;

		try
		{
			IColourRectangle background = stage.getContentFactory().create(IColourRectangle.class, "keyboardBg", UUID.randomUUID());
			background.enableTransparency();
			background.setSolidBackgroundColour(bgColour);
			background.setSize(width, height);

			IRoundedBorder keyboardBorder = stage.getContentFactory().create(IRoundedBorder.class, "keyboardBorder", UUID.randomUUID());
			keyboardBorder.setBorderWidth(10f);
			keyboardBorder.setSize(width, height);
			keyboardBorder.setColor(boardBorderColour);

			this.addItem(background);
			this.addItem(keyboardBorder);

			x += buttonSpacing;
			y -= buttonSpacing;

			generateKey("7");
			generateKey("8");
			generateKey("9");

			x = -width / 2;

			x += buttonSpacing;
			y -= buttonSpacing;

			generateKey("4");
			generateKey("5");
			generateKey("6");

			x = -width / 2;

			x += buttonSpacing;
			y -= buttonSpacing;

			generateKey("1");
			generateKey("2");
			generateKey("3");

			x = -width / 2;

			x += buttonSpacing;
			y -= buttonSpacing;

			generateKey("0");
			generateBackspaceKey();

			this.zOrderManager.setAutoBringToTop(false);

			if (movable)
			{
				rtsBackground = stage.getBehaviourMaker().addBehaviour(background, RotateTranslateScaleBehaviour.class);
				rtsBackground.setItemActingOn(this);
				rtsBorder = stage.getBehaviourMaker().addBehaviour(keyboardBorder, RotateTranslateScaleBehaviour.class);
				rtsBorder.setItemActingOn(this);
				if (scaleLimitsSet)
				{
					rtsBackground.setScaleLimits(minScale, maxScale);
					rtsBorder.setScaleLimits(minScale, maxScale);
				}
			}

		}
		catch (ContentTypeNotBoundException e)
		{
		}
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.additionalitems.interfaces.ISimpleKeypad#getHeight()
	 */
	@Override
	public float getHeight()
	{
		return height;
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.additionalitems.interfaces.ISimpleKeypad#getWidth()
	 */
	@Override
	public float getWidth()
	{
		return width;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynet3.additionalitems.interfaces.ISimpleKeypad#setButtonSizeAndSpacing
	 * (float, float)
	 */
	@Override
	public void setButtonSizeAndSpacing(float buttonSize, float buttonSpacing)
	{
		this.buttonSize = buttonSize;
		this.buttonSpacing = buttonSpacing;

		width = buttonSpacing * 4;
		height = buttonSpacing * 5;

		x = -width / 2;
		y = height / 2;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynet3.additionalitems.interfaces.ISimpleKeypad#setColours(com.jme3
	 * .math.ColorRGBA, com.jme3.math.ColorRGBA, com.jme3.math.ColorRGBA,
	 * com.jme3.math.ColorRGBA, synergynet3.fonts.FontColour)
	 */
	@Override
	public void setColours(ColorRGBA bgColour, ColorRGBA keyColour, ColorRGBA keyBorderColour, ColorRGBA boardBorderColour, FontColour fontColour)
	{
		this.bgColour = bgColour;
		this.keyColour = keyColour;
		this.keyBorderColour = keyBorderColour;
		this.boardBorderColour = boardBorderColour;
		this.fontColour = fontColour;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynet3.additionalitems.interfaces.ISimpleKeypad#setMovable(boolean)
	 */
	@Override
	public void setMovable(boolean movable)
	{
		this.movable = movable;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynet3.additionalitems.interfaces.ISimpleKeypad#setScaleLimits(float
	 * , float)
	 */
	@Override
	public void setScaleLimits(float minScale, float maxScale)
	{
		this.minScale = minScale;
		this.maxScale = maxScale;
		if (rtsBackground != null)
		{
			rtsBackground.setScaleLimits(minScale, maxScale);
		}
		if (rtsBorder != null)
		{
			rtsBorder.setScaleLimits(minScale, maxScale);
		}
		scaleLimitsSet = true;
	}

	/**
	 * Generate backspace key.
	 *
	 * @return the i buttonbox
	 * @throws ContentTypeNotBoundException
	 *             the content type not bound exception
	 */
	private IButtonbox generateBackspaceKey() throws ContentTypeNotBoundException
	{

		x -= buttonSpacing;

		float backspaceKeyWidth = (buttonSize * 2) + (buttonSpacing - buttonSize);
		x += ((buttonSize / 2) + (backspaceKeyWidth / 2)) + (buttonSpacing - buttonSize);

		IButtonbox button = stage.getContentFactory().create(IButtonbox.class, "button", UUID.randomUUID());
		button.setText("<", keyColour, keyBorderColour, fontColour, backspaceKeyWidth, buttonSize, stage);

		final IgnoreDoubleClick clicker = new IgnoreDoubleClick(500)
		{
			@Override
			public void onAction(MultiTouchCursorEvent event)
			{
				keyboardOutput.onKeyboardOutput(KeyboardSpecialKeys.BACKSPACE.toString());
			}
		};
		button.getListener().getMultiTouchDispatcher().addListener(new MultiTouchEventAdapter()
		{
			@Override
			public void cursorPressed(MultiTouchCursorEvent event)
			{
				clicker.click(event);
			}
		});
		button.setRelativeLocation(new Vector2f(x, y));

		this.addItem(button);
		return button;
	}

	/**
	 * Generate key.
	 *
	 * @param key
	 *            the key
	 * @throws ContentTypeNotBoundException
	 *             the content type not bound exception
	 */
	private void generateKey(final String key) throws ContentTypeNotBoundException
	{

		IButtonbox button = stage.getContentFactory().create(IButtonbox.class, "button", UUID.randomUUID());
		button.setText(key, keyColour, keyBorderColour, fontColour, buttonSize, buttonSize, stage);

		final IgnoreDoubleClick clicker = new IgnoreDoubleClick(500)
		{
			@Override
			public void onAction(MultiTouchCursorEvent event)
			{
				keyboardOutput.onKeyboardOutput(key);
			}
		};

		button.getListener().getMultiTouchDispatcher().addListener(new MultiTouchEventAdapter()
		{
			@Override
			public void cursorPressed(MultiTouchCursorEvent event)
			{
				clicker.click(event);
			}
		});

		button.setRelativeLocation(new Vector2f(x, y));
		this.addItem(button);
		x += buttonSpacing;
	}

}
