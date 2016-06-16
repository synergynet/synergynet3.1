package synergynet3.museum.table.mainapp.userrecorder;

import java.util.UUID;

import multiplicity3.csys.factory.ContentTypeNotBoundException;
import multiplicity3.csys.items.border.IRoundedBorder;
import multiplicity3.csys.items.container.IContainer;
import multiplicity3.csys.items.image.IImage;
import multiplicity3.csys.stage.IStage;
import synergynet3.additionalitems.interfaces.ITextbox;
import synergynet3.fonts.FontColour;
import synergynet3.museum.table.settingsapp.MuseumAppPreferences;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;

/**
 * The Class RecorderButton.
 */
public class RecorderButton
{

	/** The Constant IMAGESCALE. */
	private static final float IMAGESCALE = 0.75f;

	/** The Constant INVIS. */
	private static final ColorRGBA INVIS = new ColorRGBA(0, 0, 0, 0);

	/** The Constant RESOURCE_DIR. */
	private static final String RESOURCE_DIR = "synergynet3/museum/table/userrecorder/";

	/** The Constant TEXTSCALE. */
	private static final float TEXTSCALE = 0.5f;

	/** The button container. */
	private IContainer buttonContainer;

	/** The listener. */
	private IImage listener;

	/**
	 * Instantiates a new recorder button.
	 *
	 * @param stage
	 *            the stage
	 * @param imageName
	 *            the image name
	 * @param text
	 *            the text
	 * @param active
	 *            the active
	 * @throws ContentTypeNotBoundException
	 *             the content type not bound exception
	 */
	public RecorderButton(IStage stage, String imageName, String text, boolean active) throws ContentTypeNotBoundException
	{
		buttonContainer = stage.getContentFactory().create(IContainer.class, "buttonContainer", UUID.randomUUID());

		FontColour fontCol = MuseumAppPreferences.getRecorderActiveButtonFontColour();
		ColorRGBA borderCol = MuseumAppPreferences.getRecorderActiveButtonBorderColour();

		if (!active)
		{
			fontCol = MuseumAppPreferences.getRecorderInactiveButtonFontColour();
			borderCol = MuseumAppPreferences.getRecorderInactiveButtonBorderColour();
		}

		IImage buttonImage = stage.getContentFactory().create(IImage.class, "buttonImage", UUID.randomUUID());
		buttonImage.setImage(RESOURCE_DIR + imageName + ".png");
		buttonImage.setSize(UserRecorder.BUTTON_WIDTH, UserRecorder.BUTTON_HEIGHT);
		buttonImage.setRelativeScale(IMAGESCALE);
		buttonImage.setRelativeLocation(new Vector2f(0, (UserRecorder.BUTTON_HEIGHT / 2) - ((UserRecorder.BUTTON_HEIGHT * IMAGESCALE) / 2) - 5f));
		buttonContainer.addItem(buttonImage);

		ITextbox textLabel = stage.getContentFactory().create(ITextbox.class, "textLabel", UUID.randomUUID());
		textLabel.setColours(INVIS, INVIS, fontCol);
		textLabel.setWidth(UserRecorder.BUTTON_WIDTH / TEXTSCALE);
		textLabel.setHeight(UserRecorder.BUTTON_HEIGHT / 4);
		textLabel.setText(text, stage);
		textLabel.setRelativeScale(TEXTSCALE);
		textLabel.setRelativeLocation(new Vector2f(0, -(UserRecorder.BUTTON_HEIGHT / 2) + ((textLabel.getHeight() * TEXTSCALE) / 2)));
		textLabel.setMovable(false);
		buttonContainer.addItem(textLabel);

		IRoundedBorder textBorder = stage.getContentFactory().create(IRoundedBorder.class, "textBorder", UUID.randomUUID());
		textBorder.setBorderWidth(3);
		textBorder.setSize(UserRecorder.BUTTON_WIDTH, UserRecorder.BUTTON_HEIGHT);
		textBorder.setColor(borderCol);
		buttonContainer.addItem(textBorder);

		if (active)
		{
			listener = stage.getContentFactory().create(IImage.class, "listenBlock", UUID.randomUUID());
			listener.setSize(UserRecorder.BUTTON_WIDTH, UserRecorder.BUTTON_HEIGHT);
			buttonContainer.addItem(listener);
		}
	}

	/**
	 * @return the buttonContainer
	 */
	public IContainer getButtonContainer()
	{
		return buttonContainer;
	}

	/**
	 * @return the listener
	 */
	public IImage getListener()
	{
		return listener;
	}

	/**
	 * @param buttonContainer
	 *            the buttonContainer to set
	 */
	public void setButtonContainer(IContainer buttonContainer)
	{
		this.buttonContainer = buttonContainer;
	}

	/**
	 * @param listener
	 *            the listener to set
	 */
	public void setListener(IImage listener)
	{
		this.listener = listener;
	}

}
