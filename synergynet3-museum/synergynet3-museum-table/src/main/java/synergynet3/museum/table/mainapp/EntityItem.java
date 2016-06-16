package synergynet3.museum.table.mainapp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

import multiplicity3.csys.factory.ContentTypeNotBoundException;
import multiplicity3.csys.items.item.IItem;
import multiplicity3.csys.items.line.ILine;
import multiplicity3.csys.stage.IStage;
import synergynet3.additionalitems.interfaces.IMediaPlayer;
import synergynet3.additionalitems.interfaces.ITextbox;
import synergynet3.museum.table.MuseumApp;
import synergynet3.museum.table.mainapp.userrecorder.UserRecordingPromptLabel;
import synergynet3.museum.table.settingsapp.MuseumAppPreferences;

import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;

/**
 * The Class EntityItem.
 */
public class EntityItem
{

	/** The Constant ANGLE_OFFSET. */
	private static final float ANGLE_OFFSET = 20;

	/** The Constant SCALE_OFFSET. */
	private static final float SCALE_OFFSET = 0.2f;

	/** The entity manager. */
	private EntityManager entityManager;

	/** The is visible. */
	private boolean isVisible;

	/** The items. */
	private ArrayList<IItem> items;

	/** The line from poi. */
	private ILine lineFromPOI;

	/** The lines. */
	private ArrayList<ILine> lines = new ArrayList<ILine>();

	/** The name label. */
	private ITextbox nameLabel;

	/** The stage. */
	private IStage stage;

	/** The user recording prompt labels. */
	private ArrayList<UserRecordingPromptLabel> userRecordingPromptLabels = new ArrayList<UserRecordingPromptLabel>();

	/**
	 * Instantiates a new entity item.
	 *
	 * @param stage
	 *            the stage
	 * @param name
	 *            the name
	 * @param items
	 *            the items
	 * @param userGeneratedItems
	 *            the user generated items
	 * @param app
	 *            the app
	 */
	public EntityItem(IStage stage, String name, ArrayList<IItem> items, ArrayList<IItem> userGeneratedItems, MuseumApp app)
	{
		this.items = items;
		this.stage = stage;
		entityManager = app.getEntityManager();

		try
		{
			nameLabel = LabelGenerator.generateName(name, stage, app);
			nameLabel.setVisible(false);
			stage.addItem(nameLabel);

			for (IItem item : items)
			{
				initialiseItem(item, false);
			}
			for (IItem item : userGeneratedItems)
			{
				initialiseItem(item, true);
				items.add(item);
			}

			regenerate(nameLabel.getRelativeLocation().x, nameLabel.getRelativeLocation().y);

		}
		catch (ContentTypeNotBoundException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Adds the user recording prompt label.
	 *
	 * @param userRecordingPromptLabel
	 *            the user recording prompt label
	 */
	public void addUserRecordingPromptLabel(UserRecordingPromptLabel userRecordingPromptLabel)
	{
		userRecordingPromptLabels.add(userRecordingPromptLabel);
	}

	/**
	 * Gets the central item.
	 *
	 * @return the central item
	 */
	public ITextbox getCentralItem()
	{
		return nameLabel;
	}

	/**
	 * Checks if is visible.
	 *
	 * @return true, if is visible
	 */
	public boolean isVisible()
	{
		return isVisible;
	}

	/**
	 * Regenerate.
	 *
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 */
	public void regenerate(float x, float y)
	{
		setAskew(nameLabel);
		setRandomScale(nameLabel);

		float offSetAngle = (float) (Math.random()) * 360;
		nameLabel.setRelativeLocation(moveAwayFromEdge(getItemPosition(offSetAngle, 60f).add(new Vector2f(x, y)), MuseumAppPreferences.getEntitySpread()));

		float angle = (float) (Math.random()) * 360;
		int angleStep = 0;
		if (items.size() > 0)
		{
			angleStep = 360 / items.size();
		}
		Collections.shuffle(items);
		for (IItem item : items)
		{
			setAskew(item);
			Vector2f newLoc = getItemPosition(angle, MuseumAppPreferences.getEntitySpread()).add(new Vector2f(nameLabel.getRelativeLocation().x, nameLabel.getRelativeLocation().y));
			item.setRelativeLocation(newLoc);
			angle += angleStep;
		}
	}

	/**
	 * Sets the line from poi.
	 *
	 * @param lineFromPOI
	 *            the new line from poi
	 */
	public void setLineFromPOI(ILine lineFromPOI)
	{
		this.lineFromPOI = lineFromPOI;
	}

	/**
	 * Sets the user recording prompt label visibility.
	 *
	 * @param visibility
	 *            the new user recording prompt label visibility
	 */
	public void setUserRecordingPromptLabelVisibility(boolean visibility)
	{
		if (isVisible)
		{
			for (UserRecordingPromptLabel userRecordingPromptLabel : userRecordingPromptLabels)
			{
				userRecordingPromptLabel.getTextItem().setVisible(visibility);
				for (ILine line : lines)
				{
					if (userRecordingPromptLabel.getTextItem() == line.getSourceItem())
					{
						line.setVisible(visibility);
					}
				}
			}
		}
	}

	/**
	 * Sets the visible.
	 *
	 * @param isVisible
	 *            the new visible
	 */
	public void setVisible(boolean isVisible)
	{
		if (!(isVisible && isVisible()))
		{
			this.isVisible = isVisible;
			for (ILine line : lines)
			{
				if (!(isVisible && isUserRecordingPromptLabel(line.getSourceItem()) && !entityManager.areRecordingPromptsVisible()))
				{
					line.setVisible(isVisible);
				}
			}
			for (IItem item : items)
			{
				if (!(isVisible && isUserRecordingPromptLabel(item) && !entityManager.areRecordingPromptsVisible()))
				{
					item.setVisible(isVisible);
				}
			}
			if (nameLabel != null)
			{
				nameLabel.setVisible(isVisible);
			}
			if (isVisible)
			{
				regenerate(nameLabel.getRelativeLocation().x, nameLabel.getRelativeLocation().y);
				for (IItem line : lines)
				{
					stage.getZOrderManager().bringToTop(line);
				}
				for (IItem item : items)
				{
					stage.getZOrderManager().bringToTop(item);
				}
				if (nameLabel != null)
				{
					stage.getZOrderManager().bringToTop(nameLabel);
				}
			}
			else
			{
				for (IItem item : items)
				{
					if (item instanceof IMediaPlayer)
					{
						IMediaPlayer player = (IMediaPlayer) item;
						player.pause();
						player.setPosition(0);
					}
				}
				closeRecorder();
			}
			if (lineFromPOI != null)
			{
				if (lineFromPOI.getSourceItem().isVisible() && lineFromPOI.getDestinationItem().isVisible())
				{
					lineFromPOI.setVisible(true);
				}
				else
				{
					lineFromPOI.setVisible(false);
				}
			}

		}
	}

	/**
	 * Close recorder.
	 */
	private void closeRecorder()
	{
		for (UserRecordingPromptLabel userRecordingPromptLabel : userRecordingPromptLabels)
		{
			userRecordingPromptLabel.closeRecorder();
		}
	}

	/**
	 * Generate line.
	 *
	 * @param item
	 *            the item
	 * @param stage
	 *            the stage
	 * @param userGenerated
	 *            the user generated
	 * @throws ContentTypeNotBoundException
	 *             the content type not bound exception
	 */
	private void generateLine(final IItem item, IStage stage, boolean userGenerated) throws ContentTypeNotBoundException
	{
		ILine line = stage.getContentFactory().create(ILine.class, "line", UUID.randomUUID());
		line.setLineWidth(6f);
		line.setInteractionEnabled(false);
		line.getZOrderManager().setAutoBringToTop(false);
		if (userGenerated)
		{
			line.setLineColour(MuseumAppPreferences.getUserGeneratedContentColour());
		}
		else
		{
			line.setLineColour(MuseumAppPreferences.getEntityBorderColour());
		}
		line.setSourceItem(item);
		line.setDestinationItem(nameLabel);
		line.setVisible(false);
		stage.addItem(line);
		lines.add(line);
	}

	/**
	 * Gets the item position.
	 *
	 * @param angle
	 *            the angle
	 * @param radius
	 *            the radius
	 * @return the item position
	 */
	private Vector2f getItemPosition(float angle, float radius)
	{
		Vector2f position = new Vector2f(0, radius);
		position.rotateAroundOrigin(FastMath.DEG_TO_RAD * angle, true);
		return position;
	}

	/**
	 * Initialise item.
	 *
	 * @param item
	 *            the item
	 * @param userGenerated
	 *            the user generated
	 * @throws ContentTypeNotBoundException
	 *             the content type not bound exception
	 */
	private void initialiseItem(IItem item, boolean userGenerated) throws ContentTypeNotBoundException
	{
		generateLine(item, stage, userGenerated);

		item.setVisible(false);
		stage.addItem(item);
	}

	/**
	 * Checks if is user recording prompt label.
	 *
	 * @param possibleUserRecordingPromptLabel
	 *            the possible user recording prompt label
	 * @return true, if is user recording prompt label
	 */
	private boolean isUserRecordingPromptLabel(IItem possibleUserRecordingPromptLabel)
	{
		for (UserRecordingPromptLabel userRecordingPromptLabel : userRecordingPromptLabels)
		{
			if (userRecordingPromptLabel.getTextItem() == possibleUserRecordingPromptLabel)
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * Move away from edge.
	 *
	 * @param loc
	 *            the loc
	 * @param buffer
	 *            the buffer
	 * @return the vector2f
	 */
	private Vector2f moveAwayFromEdge(Vector2f loc, float buffer)
	{
		if (loc.x < ((-stage.getDisplayWidth() / 2) + buffer))
		{
			loc.setX((-stage.getDisplayWidth() / 2) + buffer);
		}
		else if (loc.x > ((stage.getDisplayWidth() / 2) - buffer))
		{
			loc.setX((stage.getDisplayWidth() / 2) - buffer);
		}
		if (loc.y < ((-stage.getDisplayHeight() / 2) + buffer))
		{
			loc.setY((-stage.getDisplayHeight() / 2) + buffer);
		}
		else if (loc.y > ((stage.getDisplayHeight() / 2) - buffer))
		{
			loc.setY((stage.getDisplayHeight() / 2) - buffer);
		}
		return loc;
	}

	/**
	 * Sets the askew.
	 *
	 * @param item
	 *            the new askew
	 */
	private void setAskew(IItem item)
	{
		int angle = (int) ((Math.random() * (ANGLE_OFFSET)) + 0.5);
		if (Math.random() > 0.5)
		{
			angle = -angle;
		}
		item.setRelativeRotation((float) Math.toRadians(angle));
	}

	/**
	 * Sets the random scale.
	 *
	 * @param item
	 *            the new random scale
	 */
	private void setRandomScale(IItem item)
	{
		int scale = (int) (Math.random() * (SCALE_OFFSET));
		if (Math.random() > 0.5)
		{
			scale = -scale;
		}
		item.setRelativeScale(1 + scale);
	}

}
