package synergynet3.museum.table.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import multiplicity3.appsystem.MultiplicityClient;
import multiplicity3.csys.factory.ContentTypeNotBoundException;
import multiplicity3.csys.gfx.Gradient;
import multiplicity3.csys.gfx.Gradient.GradientDirection;
import multiplicity3.csys.items.border.IRoundedBorder;
import multiplicity3.csys.items.image.IImage;
import multiplicity3.csys.items.item.IItem;
import multiplicity3.csys.items.shapes.IColourRectangle;
import multiplicity3.csys.stage.IStage;
import multiplicity3.input.MultiTouchEventAdapter;
import multiplicity3.input.events.MultiTouchCursorEvent;
import synergynet3.additionalitems.interfaces.IButtonbox;
import synergynet3.additionalitems.interfaces.ISimpleKeypad;
import synergynet3.additionalitems.interfaces.ITextbox;
import synergynet3.keyboard.KeyboardOutput;
import synergynet3.keyboard.KeyboardSpecialKeys;
import synergynet3.museum.table.mainapp.LabelGenerator;
import synergynet3.museum.table.settingsapp.MuseumAppPreferences;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;

/**
 * The Class ShutdownGUI.
 */
public class ShutdownGUI implements KeyboardOutput {

	/** The Constant FIELD_HEIGHT. */
	private static final float FIELD_HEIGHT = 60f;

	/** The height. */
	private static float HEIGHT = 540f;

	/** The Constant HOLD. */
	private final static int HOLD = 2000;

	/** The Constant INVISIBLE. */
	private static final ColorRGBA INVISIBLE = new ColorRGBA(0, 0, 0, 0);

	/** The Constant SPACING. */
	private static final float SPACING = 10f;

	/** The Constant TIME_OUT. */
	private final static int TIME_OUT = 7500;

	/** The width. */
	private static float WIDTH = 600f;

	/** The corner button. */
	private IImage cornerButton;

	/** The entered code. */
	private String enteredCode = "";

	/** The last updated. */
	private Date lastUpdated;

	/** The open. */
	private boolean open = true;

	/** The pin field. */
	private IButtonbox pinField;

	/** The screen items. */
	private ArrayList<IItem> screenItems = new ArrayList<IItem>();

	/** The warning. */
	private ITextbox warning;

	/**
	 * Instantiates a new shutdown gui.
	 *
	 * @param stage the stage
	 * @param displayWidth the display width
	 * @param displayHeight the display height
	 * @param buttonWidth the button width
	 * @param textScale the text scale
	 * @param code the code
	 * @throws ContentTypeNotBoundException the content type not bound exception
	 */
	public ShutdownGUI(final IStage stage, float displayWidth,
			float displayHeight, float buttonWidth, float textScale,
			final String code) throws ContentTypeNotBoundException {

		cornerButton = stage.getContentFactory().create(IImage.class,
				"closeButton", UUID.randomUUID());
		cornerButton.setImage(ImageUtils.getImage(
				MuseumAppPreferences.getCloseButtonColour(),
				ImageUtils.RESOURCE_DIR + "entitybuttons/close/",
				"_close_button.png"));
		cornerButton.setSize(LabelGenerator.TEXT_HEIGHT / 2,
				LabelGenerator.TEXT_HEIGHT / 2);
		cornerButton
				.setRelativeLocation(new Vector2f(
						((stage.getDisplayWidth() / 2) - (cornerButton
								.getWidth() / 2)),
						((stage.getDisplayHeight() / 2) - (cornerButton
								.getHeight() / 2))));

		cornerButton.getMultiTouchDispatcher().addListener(
				new MultiTouchEventAdapter() {

					private boolean held = true;
					private Date lastUpdated = new Date();

					@Override
					public void cursorPressed(MultiTouchCursorEvent event) {
						lastUpdated = new Date();
						held = true;

						Thread timeoutThread = new Thread(new Runnable() {
							public void run() {
								while (held) {
									try {
										Thread.sleep(100);
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
									if (((new Date().getTime() - lastUpdated
											.getTime()) > HOLD) && held) {
										setVisibility(true, stage);
										held = false;
									}
								}
							}
						});
						timeoutThread.start();
					}

					@Override
					public void cursorReleased(MultiTouchCursorEvent event) {
						held = false;
					}

				});
		stage.addItem(cornerButton);
		stage.getZOrderManager().bringToTop(cornerButton);
		stage.getZOrderManager().unregisterForZOrdering(cornerButton);

		screenItems.add(generateFullScreenBackground(stage));

		IColourRectangle background = stage.getContentFactory().create(
				IColourRectangle.class, "recorderContainerBg",
				UUID.randomUUID());
		background.setSolidBackgroundColour(MuseumAppPreferences
				.getShutdownControlsBackgroundColour());
		background.setSize(WIDTH, HEIGHT);
		background.setInteractionEnabled(false);
		background.setVisible(false);
		stage.addItem(background);
		stage.getZOrderManager().bringToTop(background);
		stage.getZOrderManager().unregisterForZOrdering(background);
		screenItems.add(background);

		IRoundedBorder frameBorder = stage.getContentFactory().create(
				IRoundedBorder.class, "containerBorder", UUID.randomUUID());
		frameBorder.setBorderWidth(5);
		frameBorder.setSize(WIDTH, HEIGHT);
		frameBorder.setColor(MuseumAppPreferences
				.getShutdownControlsBorderColour());
		frameBorder.setVisible(false);
		stage.addItem(frameBorder);
		stage.getZOrderManager().bringToTop(frameBorder);
		stage.getZOrderManager().unregisterForZOrdering(frameBorder);
		screenItems.add(frameBorder);

		ISimpleKeypad keypad = stage.getContentFactory().create(
				ISimpleKeypad.class, "keyboard", UUID.randomUUID());
		keypad.setColours(INVISIBLE,
				MuseumAppPreferences.getShutdownControlsBackgroundColour(),
				MuseumAppPreferences.getShutdownControlsBorderColour(),
				INVISIBLE, MuseumAppPreferences.getShutdownControlsFontColour());
		keypad.setButtonSizeAndSpacing(55, 65);
		keypad.setMovable(false);
		keypad.generateKeys(stage, this);
		keypad.setVisible(false);
		keypad.getZOrderManager().setBringToTopPropagatesUp(false);
		stage.addItem(keypad);
		screenItems.add(keypad);

		pinField = generateTextbox(stage);
		screenItems.add(pinField);

		ITextbox label = stage.getContentFactory().create(ITextbox.class,
				"textLabel", UUID.randomUUID());
		label.setColours(INVISIBLE, INVISIBLE,
				MuseumAppPreferences.getShutdownControlsFontColour());
		label.setMovable(false);
		label.setWidth(WIDTH);
		label.setHeight(FIELD_HEIGHT);
		label.setText(MuseumAppPreferences.getShutdownInstructionsText(), stage);
		label.setInteractionEnabled(false);
		label.setVisible(false);
		stage.addItem(label);
		screenItems.add(label);

		IButtonbox okButton = generateOkButton("OK", buttonWidth - 20,
				FIELD_HEIGHT, stage);
		okButton.setVisible(false);
		okButton.getListener().getMultiTouchDispatcher()
				.addListener(new MultiTouchEventAdapter() {
					@Override
					public void cursorPressed(MultiTouchCursorEvent event) {
						if (enteredCode.equals(code)) {
							MultiplicityClient.get().stop();
						} else {
							warning.setVisible(true);
							stage.getZOrderManager().bringToTop(warning);
						}
					}
				});
		stage.addItem(okButton);
		screenItems.add(okButton);

		warning = generateErrorMessage("Incorrect", okButton.getWidth(),
				FIELD_HEIGHT, stage);

		IImage backButton = stage.getContentFactory().create(IImage.class,
				"closeButton", UUID.randomUUID());
		backButton.setImage(ImageUtils.getImage(
				MuseumAppPreferences.getCloseButtonColour(),
				ImageUtils.RESOURCE_DIR + "entitybuttons/close/",
				"_close_button.png"));
		backButton.setSize(LabelGenerator.TEXT_HEIGHT / 2,
				LabelGenerator.TEXT_HEIGHT / 2);
		backButton.setRelativeLocation(new Vector2f(
				((WIDTH / 2) - (cornerButton.getWidth() / 2)),
				((HEIGHT / 2) - (cornerButton.getHeight() / 2))));
		backButton.getMultiTouchDispatcher().addListener(
				new MultiTouchEventAdapter() {
					@Override
					public void cursorPressed(MultiTouchCursorEvent event) {
						setVisibility(false, stage);
					}
				});
		backButton.setVisible(false);
		stage.addItem(backButton);
		screenItems.add(backButton);

		float y = HEIGHT / 2;
		y -= SPACING + (label.getHeight() / 2);
		label.setRelativeLocation(new Vector2f(0, y));
		y -= (label.getHeight() / 2) + SPACING + (pinField.getHeight() / 2);
		pinField.setRelativeLocation(new Vector2f(0, y));
		y -= (pinField.getHeight() / 2) + (keypad.getHeight() / 2);
		keypad.setRelativeLocation(new Vector2f(0, y));
		y -= (keypad.getHeight() / 2) + (okButton.getHeight() / 2);
		okButton.setRelativeLocation(new Vector2f(0, y));
		warning.setRelativeLocation(okButton.getRelativeLocation());

	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynet3.keyboard.KeyboardOutput#onKeyboardOutput(java.lang.String)
	 */
	public void onKeyboardOutput(String letter) {

		lastUpdated = new Date();

		if (warning.isVisible()) {
			warning.setVisible(false);
		}

		if (letter.equalsIgnoreCase(KeyboardSpecialKeys.BACKSPACE.toString())) {
			if (enteredCode.length() != 0) {
				enteredCode = enteredCode
						.substring(0, enteredCode.length() - 1);
			}
		} else if (letter
				.equalsIgnoreCase(KeyboardSpecialKeys.ENTER.toString())) {

		} else {
			enteredCode += letter;
		}

		String blankedCode = "";
		for (int i = 0; i < enteredCode.length(); i++) {
			blankedCode += "*";
		}

		pinField.getTextLabel().setText(blankedCode);
	}

	/**
	 * Generate error message.
	 *
	 * @param text the text
	 * @param width the width
	 * @param height the height
	 * @param stage the stage
	 * @return the i textbox
	 * @throws ContentTypeNotBoundException the content type not bound exception
	 */
	private ITextbox generateErrorMessage(String text, float width,
			float height, IStage stage) throws ContentTypeNotBoundException {
		ITextbox error = stage.getContentFactory().create(ITextbox.class,
				"textLabel", UUID.randomUUID());
		error.setColours(
				MuseumAppPreferences.getShutdownControlsBackgroundColour(),
				MuseumAppPreferences.getErrorColourAsRGBA(),
				MuseumAppPreferences.getErrorColourAsFontColour());
		error.setMovable(false);
		error.setWidth(width);
		error.setHeight(height);
		error.setText(text, stage);
		error.setInteractionEnabled(false);
		error.setVisible(false);
		stage.addItem(error);
		return error;
	}

	/**
	 * Generate full screen background.
	 *
	 * @param stage the stage
	 * @return the i colour rectangle
	 * @throws ContentTypeNotBoundException the content type not bound exception
	 */
	private IColourRectangle generateFullScreenBackground(IStage stage)
			throws ContentTypeNotBoundException {
		IColourRectangle background = stage.getContentFactory().create(
				IColourRectangle.class, "test", UUID.randomUUID());
		background.setSize(stage.getDisplayWidth(), stage.getDisplayHeight());
		background.enableTransparency();
		background.setGradientBackground(new Gradient(MuseumAppPreferences
				.getShutdownBackgroundColour(), MuseumAppPreferences
				.getShutdownBackgroundColour(), GradientDirection.DIAGONAL));
		background.setVisible(false);
		background.setInteractionEnabled(false);
		stage.addItem(background);
		return background;
	}

	/**
	 * Generate ok button.
	 *
	 * @param text the text
	 * @param buttonWidth the button width
	 * @param buttonHeight the button height
	 * @param stage the stage
	 * @return the i buttonbox
	 * @throws ContentTypeNotBoundException the content type not bound exception
	 */
	private IButtonbox generateOkButton(String text, float buttonWidth,
			float buttonHeight, IStage stage)
			throws ContentTypeNotBoundException {
		IButtonbox okButton = stage.getContentFactory().create(
				IButtonbox.class, "okButton", UUID.randomUUID());
		okButton.setText(text,
				MuseumAppPreferences.getShutdownControlsBackgroundColour(),
				MuseumAppPreferences.getShutdownControlsBorderColour(),
				MuseumAppPreferences.getShutdownControlsFontColour(),
				buttonWidth - 20, 60, stage);
		okButton.setVisible(false);
		stage.addItem(okButton);
		return okButton;
	}

	/**
	 * Generate textbox.
	 *
	 * @param stage the stage
	 * @return the i buttonbox
	 * @throws ContentTypeNotBoundException the content type not bound exception
	 */
	private IButtonbox generateTextbox(IStage stage)
			throws ContentTypeNotBoundException {
		IButtonbox textBox = stage.getContentFactory().create(IButtonbox.class,
				"textBox", UUID.randomUUID());
		textBox.setText("",
				MuseumAppPreferences.getShutdownControlsBackgroundColour(),
				MuseumAppPreferences.getShutdownControlsBorderColour(),
				MuseumAppPreferences.getShutdownControlsFontColour(),
				WIDTH / 2, 60, stage);
		textBox.setInteractionEnabled(false);
		textBox.setVisible(false);
		stage.addItem(textBox);
		return textBox;
	}

	/**
	 * Sets the visibility.
	 *
	 * @param visibility the visibility
	 * @param stage the stage
	 */
	private void setVisibility(boolean visibility, IStage stage) {
		onVisibilityChanged(visibility, stage);
		for (IItem item : screenItems) {
			item.setVisible(visibility);
			stage.getZOrderManager().bringToTop(item);
		}
	}

	/**
	 * On visibility changed.
	 *
	 * @param visibility the visibility
	 * @param stage the stage
	 */
	protected void onVisibilityChanged(boolean visibility, final IStage stage) {

		if (visibility) {
			open = true;
			lastUpdated = new Date();
			Thread timeoutThread = new Thread(new Runnable() {
				public void run() {
					while (open) {
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						if ((new Date().getTime() - lastUpdated.getTime()) > TIME_OUT) {
							setVisibility(false, stage);
						}
					}
				}
			});
			timeoutThread.start();
		} else {
			open = false;
		}

		warning.setVisible(false);
		enteredCode = "";
		pinField.getTextLabel().setText("");
		cornerButton.setVisible(!visibility);
	}

}
