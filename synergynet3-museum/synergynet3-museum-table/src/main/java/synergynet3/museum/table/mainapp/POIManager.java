package synergynet3.museum.table.mainapp;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

import multiplicity3.appsystem.MultiplicityClient;
import multiplicity3.csys.factory.ContentTypeNotBoundException;
import multiplicity3.csys.items.image.IImage;
import multiplicity3.csys.items.item.IItem;
import multiplicity3.csys.items.line.ILine;
import multiplicity3.csys.stage.IStage;
import multiplicity3.input.MultiTouchEventAdapter;
import multiplicity3.input.events.MultiTouchCursorEvent;
import synergynet3.additionalUtils.IgnoreDoubleClick;
import synergynet3.additionalitems.interfaces.IButtonbox;
import synergynet3.museum.table.settingsapp.MuseumAppPreferences;
import synergynet3.museum.table.settingsapp.SettingsUtil;
import synergynet3.museum.table.utils.ImageUtils;
import synergynet3.museum.table.utils.LensUtils;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;

/**
 * The Class POIManager.
 */
public class POIManager
{

	/** The lenses. */
	public static ArrayList<Lens> lenses = new ArrayList<Lens>();

	/** The display height. */
	private float displayHeight;

	/** The display offset. */
	private Vector2f displayOffset;

	/** The display width. */
	private float displayWidth;

	/** The entity manager. */
	private EntityManager entityManager;

	/** The instance. */
	private POIManager instance;

	/** The lenses in use. */
	private ArrayList<String> lensesInUse = new ArrayList<String>();

	/** The lens visible po is. */
	private ArrayList<LensVisiblePOI> lensVisiblePOIs = new ArrayList<LensVisiblePOI>();

	/** The regular po is. */
	private ArrayList<IImage> regularPOIs = new ArrayList<IImage>();

	/** The stage. */
	private IStage stage;

	/**
	 * Instantiates a new POI manager.
	 *
	 * @param stage
	 *            the stage
	 * @param displayWidth
	 *            the display width
	 * @param displayHeight
	 *            the display height
	 * @param displayOffset
	 *            the display offset
	 * @param entityManager
	 *            the entity manager
	 */
	public POIManager(IStage stage, float displayWidth, float displayHeight, Vector2f displayOffset, EntityManager entityManager)
	{
		this.stage = stage;
		this.displayWidth = displayWidth;
		this.displayHeight = displayHeight;
		this.displayOffset = displayOffset;
		this.entityManager = entityManager;
		instance = this;
	}

	/**
	 * Generate background.
	 *
	 * @throws ContentTypeNotBoundException
	 *             the content type not bound exception
	 */
	public void generateBackground() throws ContentTypeNotBoundException
	{

		MultiplicityClient.get().getViewPort().setBackgroundColor(MuseumAppPreferences.getBackgroundColour());

		File imageFile = MuseumAppPreferences.getBackgroundImage();
		if (imageFile != null)
		{
			if (imageFile.exists())
			{
				IImage background = stage.getContentFactory().create(IImage.class, "background", UUID.randomUUID());
				background.setImage(imageFile);
				background.setSize(displayWidth, displayHeight);
				background.setRelativeLocation(new Vector2f());
				background.setInteractionEnabled(false);
				stage.getZOrderManager().sendToBottom(background);
				stage.addItem(background);
			}
		}
	}

	/**
	 * Generate lens button.
	 *
	 * @throws ContentTypeNotBoundException
	 *             the content type not bound exception
	 */
	public void generateLensButton() throws ContentTypeNotBoundException
	{

		IButtonbox lensButton = stage.getContentFactory().create(IButtonbox.class, "lensMakerButton", UUID.randomUUID());
		lensButton.setText(MuseumAppPreferences.getLensButtonText(), MuseumAppPreferences.getLensButtonBackgroundColour(), MuseumAppPreferences.getLensButtonBorderColour(), MuseumAppPreferences.getLensButtonFontColour(), 150, 60, stage);
		lensButton.setRelativeLocation(new Vector2f((-stage.getDisplayWidth() / 2) + 10 + (lensButton.getWidth() / 2), (-displayHeight / 2) + 10 + (60 / 2)));

		final IgnoreDoubleClick clicker = new IgnoreDoubleClick()
		{
			@Override
			public void onAction(MultiTouchCursorEvent event)
			{
				try
				{
					String[] lensesToUse = new String[lensesInUse.size()];
					lensesToUse = lensesInUse.toArray(lensesToUse);
					lenses.add(new Lens(stage, lensesToUse, instance));
				}
				catch (ContentTypeNotBoundException e)
				{
					e.printStackTrace();
				}
			}
		};

		lensButton.getListener().getMultiTouchDispatcher().addListener(new MultiTouchEventAdapter()
		{
			@Override
			public void cursorPressed(MultiTouchCursorEvent event)
			{
				clicker.click(event);
			}
		});

		lensButton.getZOrderManager().setBringToTopPropagatesUp(false);
		stage.addItem(lensButton);

	}

	/**
	 * Generate lensed poi.
	 *
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @param lensValue
	 *            the lens value
	 * @param name
	 *            the name
	 * @return the i image
	 * @throws ContentTypeNotBoundException
	 *             the content type not bound exception
	 */
	public IImage generateLensedPOI(final float x, final float y, String lensValue, final String name) throws ContentTypeNotBoundException
	{
		if (!lensesInUse.contains(lensValue))
		{
			lensesInUse.add(lensValue);
		}
		String col = LensUtils.getLensColour(lensValue);

		final IImage poi = stage.getContentFactory().create(IImage.class, "poiBackground", UUID.randomUUID());
		poi.setImage(ImageUtils.getImage(col, ImageUtils.RESOURCE_DIR + "pois/", "_poi.png"));
		poi.setSize(MuseumAppPreferences.getPoiWidth(), MuseumAppPreferences.getPoiWidth());

		final IImage poiBorder = stage.getContentFactory().create(IImage.class, "poiBorder", UUID.randomUUID());
		poiBorder.setImage(ImageUtils.getImage(MuseumAppPreferences.getPOIBorderColour(), ImageUtils.RESOURCE_DIR + "pois/borders/", "_poi_border.png"));
		poiBorder.setSize(MuseumAppPreferences.getPoiWidth(), MuseumAppPreferences.getPoiWidth());
		poi.addItem(poiBorder);

		setItemLocation(poi, x, y);

		ColorRGBA colour = SettingsUtil.getColorRGBA(col);
		final ILine line = stage.getContentFactory().create(ILine.class, "line", UUID.randomUUID());
		line.setLineWidth(6f);
		line.setInteractionEnabled(false);
		line.getZOrderManager().setAutoBringToTop(false);
		line.setLineColour(colour);
		line.setLineVisibilityChangesWithItemVisibility(true);
		line.setVisible(false);

		final EntityItem entity = entityManager.getEntities().get(name);
		if (entity != null)
		{
			line.setSourceItem(entity.getCentralItem());
			line.setDestinationItem(poi);
			line.setVisible(false);
			entity.setLineFromPOI(line);
			stage.addItem(line);
		}

		final IgnoreDoubleClick clicker = new IgnoreDoubleClick()
		{
			@Override
			public void onAction(MultiTouchCursorEvent event)
			{

				Vector2f touchLoc = new Vector2f(event.getPosition().x * displayWidth, event.getPosition().y * displayHeight);
				Vector2f poiLoc = new Vector2f(x * displayWidth, y * displayHeight);
				if (touchLoc.distance(poiLoc) <= (MuseumAppPreferences.getPoiWidth() / 2))
				{

					if (poi.isVisible())
					{
						EntityItem entity = entityManager.getEntities().get(name);
						if (entity != null)
						{
							entity.setVisible(!entity.isVisible());
							line.setVisible(entity.isVisible());
							if (entity.isVisible())
							{
								entity.regenerate(poi.getRelativeLocation().x, poi.getRelativeLocation().y);
							}
						}
					}

				}

			}
		};

		poi.getMultiTouchDispatcher().addListener(new MultiTouchEventAdapter()
		{
			@Override
			public void cursorPressed(MultiTouchCursorEvent event)
			{
				clicker.click(event);
			}
		});

		poi.setVisible(false);

		lensVisiblePOIs.add(new LensVisiblePOI(lensValue, poi, line, x, y));

		return poi;
	}

	/**
	 * Generate poi.
	 *
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @param name
	 *            the name
	 * @return the i image
	 * @throws ContentTypeNotBoundException
	 *             the content type not bound exception
	 */
	public IImage generatePOI(final float x, final float y, final String name) throws ContentTypeNotBoundException
	{

		final IImage poi = stage.getContentFactory().create(IImage.class, "poiBackground", UUID.randomUUID());
		poi.setImage(ImageUtils.getImage(MuseumAppPreferences.getPOIColourAsString(), ImageUtils.RESOURCE_DIR + "pois/", "_poi.png"));
		poi.setSize(MuseumAppPreferences.getPoiWidth(), MuseumAppPreferences.getPoiWidth());

		final IImage poiBorder = stage.getContentFactory().create(IImage.class, "poiBorder", UUID.randomUUID());
		poiBorder.setImage(ImageUtils.getImage(MuseumAppPreferences.getPOIBorderColour(), ImageUtils.RESOURCE_DIR + "pois/borders/", "_poi_border.png"));
		poiBorder.setSize(MuseumAppPreferences.getPoiWidth(), MuseumAppPreferences.getPoiWidth());
		poi.addItem(poiBorder);

		setItemLocation(poi, x, y);

		final ILine line = stage.getContentFactory().create(ILine.class, "line", UUID.randomUUID());
		line.setLineWidth(6f);
		line.setInteractionEnabled(false);
		line.getZOrderManager().setAutoBringToTop(false);
		line.setLineColour(MuseumAppPreferences.getPOIColourAsColorRGBA());
		line.setVisible(false);

		final EntityItem entity = entityManager.getEntities().get(name);
		if (entity != null)
		{
			line.setSourceItem(entity.getCentralItem());
			line.setDestinationItem(poi);
			line.setLineVisibilityChangesWithItemVisibility(true);
			stage.addItem(line);
		}

		final IgnoreDoubleClick clicker = new IgnoreDoubleClick()
		{
			@Override
			public void onAction(MultiTouchCursorEvent event)
			{

				Vector2f touchLoc = new Vector2f(event.getPosition().x * displayWidth, event.getPosition().y * displayHeight);
				Vector2f poiLoc = new Vector2f(x * displayWidth, y * displayHeight);
				if (touchLoc.distance(poiLoc) <= (MuseumAppPreferences.getPoiWidth() / 2))
				{

					EntityItem entity = entityManager.getEntities().get(name);
					if (entity != null)
					{
						entity.setVisible(!entity.isVisible());
						if (entity.isVisible())
						{
							entity.regenerate(poi.getRelativeLocation().x, poi.getRelativeLocation().y);
						}
					}
				}
			}
		};

		poi.getMultiTouchDispatcher().addListener(new MultiTouchEventAdapter()
		{
			@Override
			public void cursorPressed(MultiTouchCursorEvent event)
			{
				clicker.click(event);
			}
		});

		regularPOIs.add(poi);

		return poi;
	}

	/**
	 * @return the eventPOIs
	 */
	public ArrayList<LensVisiblePOI> getlensVisiblePOIs()
	{
		return lensVisiblePOIs;
	}

	/**
	 * @return the pOIs
	 */
	public ArrayList<IImage> getPOIs()
	{
		return regularPOIs;
	}

	/**
	 * Sets the item location.
	 *
	 * @param item
	 *            the item
	 * @param worldX
	 *            the world x
	 * @param worldY
	 *            the world y
	 */
	private void setItemLocation(IItem item, float worldX, float worldY)
	{
		float x = worldX * displayWidth;
		float y = worldY * displayHeight;
		item.setRelativeLocation(new Vector2f(x, y).subtract(displayOffset));
	}

}
