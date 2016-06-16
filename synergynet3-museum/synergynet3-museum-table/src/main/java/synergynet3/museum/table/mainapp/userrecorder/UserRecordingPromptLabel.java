package synergynet3.museum.table.mainapp.userrecorder;

import java.io.File;
import java.util.UUID;

import multiplicity3.csys.factory.ContentTypeNotBoundException;
import multiplicity3.csys.items.image.IImage;
import multiplicity3.csys.stage.IStage;
import multiplicity3.input.MultiTouchEventAdapter;
import multiplicity3.input.events.MultiTouchCursorEvent;
import synergynet3.additionalitems.interfaces.ITextbox;
import synergynet3.museum.table.mainapp.EntityManager;
import synergynet3.museum.table.mainapp.LabelGenerator;
import synergynet3.museum.table.settingsapp.MuseumAppPreferences;
import synergynet3.museum.table.utils.ImageUtils;

import com.jme3.math.Vector2f;

/**
 * The Class UserRecordingPromptLabel.
 */
public class UserRecordingPromptLabel
{

	/** The Constant RECORD_SCALE. */
	private static final float RECORD_SCALE = 0.7f;

	/** The entity item. */
	private ITextbox entityItem;

	/** The instance. */
	private UserRecordingPromptLabel instance;

	/** The text item. */
	private ITextbox textItem;

	/** The user recorder. */
	private UserRecorder userRecorder = null;

	/**
	 * Instantiates a new user recording prompt label.
	 *
	 * @param stage
	 *            the stage
	 * @param folder
	 *            the folder
	 * @param entityManager
	 *            the entity manager
	 * @param promptText
	 *            the prompt text
	 * @throws ContentTypeNotBoundException
	 *             the content type not bound exception
	 */
	public UserRecordingPromptLabel(final IStage stage, final File folder, final EntityManager entityManager, final String promptText) throws ContentTypeNotBoundException
	{
		instance = this;
		textItem = stage.getContentFactory().create(ITextbox.class, "audioPrompt", UUID.randomUUID());
		textItem.setMovable(true);
		textItem.setColours(MuseumAppPreferences.getEntityBackgroundColour(), MuseumAppPreferences.getEntityBorderColour(), MuseumAppPreferences.getEntityFontColour());
		textItem.setHeight(LabelGenerator.TEXT_HEIGHT);
		textItem.setText(promptText, stage);
		textItem.setWidth(LabelGenerator.TEXT_WIDTH_LIMIT);

		textItem.getBackground().setSize(textItem.getBackground().getWidth() + LabelGenerator.TEXT_HEIGHT, LabelGenerator.TEXT_HEIGHT);
		textItem.getBackground().setRelativeLocation(new Vector2f(LabelGenerator.TEXT_HEIGHT / 2, 0));
		textItem.getTextBorder().setSize(textItem.getBackground().getWidth(), LabelGenerator.TEXT_HEIGHT);
		textItem.getTextBorder().setRelativeLocation(new Vector2f(LabelGenerator.TEXT_HEIGHT / 2, 0));
		((IImage) textItem.getListenBlock()).setSize(textItem.getBackground().getWidth(), LabelGenerator.TEXT_HEIGHT);
		textItem.getListenBlock().setRelativeLocation(new Vector2f(LabelGenerator.TEXT_HEIGHT / 2, 0));
		textItem.setRelativeScale(RECORD_SCALE);
		textItem.setScaleLimits(RECORD_SCALE - 0.25f, RECORD_SCALE + 0.25f);

		IImage circle = stage.getContentFactory().create(IImage.class, "circleBackground", UUID.randomUUID());
		circle.setImage(ImageUtils.getImage(MuseumAppPreferences.getEntityRecordingButtonBackgroundColour(), ImageUtils.RESOURCE_DIR + "entitybuttons/record/", "_record_button.png"));
		circle.setSize(LabelGenerator.TEXT_HEIGHT, LabelGenerator.TEXT_HEIGHT);
		circle.setRelativeLocation(new Vector2f((textItem.getWidth() / 2) + (textItem.getHeight() / 2), 0));
		textItem.addItem(circle);

		circle.getMultiTouchDispatcher().addListener(new MultiTouchEventAdapter()
		{
			@Override
			public void cursorClicked(MultiTouchCursorEvent event)
			{
				entityManager.setUserRecordingPromptLabelsVisibility(false);
				try
				{
					if (entityItem != null)
					{
						userRecorder = new UserRecorder(promptText, stage, folder, instance, entityItem, entityManager);
					}
				}
				catch (ContentTypeNotBoundException e)
				{
					e.printStackTrace();
				}
			}
		});

		textItem.getZOrderManager().setAutoBringToTop(false);
	}

	/**
	 * Close recorder.
	 */
	public void closeRecorder()
	{
		if (userRecorder != null)
		{
			userRecorder.close();
		}
	}

	/**
	 * @return the textItem
	 */
	public ITextbox getTextItem()
	{
		return textItem;
	}

	/**
	 * On recorder close.
	 */
	public void onRecorderClose()
	{
		userRecorder = null;
	}

	/**
	 * Sets the entity item.
	 *
	 * @param entityItem
	 *            the new entity item
	 */
	public void setEntityItem(ITextbox entityItem)
	{
		this.entityItem = entityItem;
	}

}
