package synergynet3.additionalitems.jme;

import java.io.File;
import java.util.UUID;

import multiplicity3.csys.annotations.ImplementsContentItem;
import multiplicity3.csys.behaviours.RotateTranslateScaleBehaviour;
import multiplicity3.csys.factory.ContentTypeNotBoundException;
import multiplicity3.csys.items.border.IRoundedBorder;
import multiplicity3.csys.items.shapes.IColourRectangle;
import multiplicity3.csys.stage.IStage;
import multiplicity3.jme3csys.items.IInitable;
import multiplicity3.jme3csys.items.container.JMEContainer;
import synergynet3.additionalitems.interfaces.ICachableImage;
import synergynet3.additionalitems.interfaces.IScreenshotContainer;
import synergynet3.behaviours.networkflick.NetworkFlickBehaviour;
import synergynet3.cachecontrol.IItemCachable;
import synergynet3.cachecontrol.ItemCaching;
import synergynet3.config.web.CacheOrganisation;
import synergynet3.databasemanagement.GalleryItemDatabaseFormat;

import com.jme3.math.ColorRGBA;

/**
 * The Class ScreenshotContainer.
 */
@ImplementsContentItem(target = IScreenshotContainer.class)
public class ScreenshotContainer extends JMEContainer implements
		IScreenshotContainer, IInitable, IItemCachable {

	/** The Constant CACHABLE_TYPE. */
	public static final String CACHABLE_TYPE = "CACHABLE_SCREENSHOTCONTAINER";

	/** The cached. */
	private String cached = "";

	/** The screen shot file. */
	private File screenShotFile;

	/**
	 * Instantiates a new screenshot container.
	 *
	 * @param name the name
	 * @param uuid the uuid
	 */
	public ScreenshotContainer(String name, UUID uuid) {
		super(name, uuid);
	}

	/**
	 * Reconstruct.
	 *
	 * @param galleryItem the gallery item
	 * @param stage the stage
	 * @param loc the loc
	 * @return the screenshot container
	 */
	public static ScreenshotContainer reconstruct(
			GalleryItemDatabaseFormat galleryItem, IStage stage, String loc) {
		try {
			ScreenshotContainer screenshotWrapperFrame = stage
					.getContentFactory().create(IScreenshotContainer.class,
							"screenshotwf", UUID.randomUUID());
			File screenShotFile = new File(
					CacheOrganisation.getSpecificDir(loc) + File.separator
							+ (String) galleryItem.getValues().get(0));
			if (!screenShotFile.isFile()) {
				return null;
			}
			if (!screenshotWrapperFrame.setScreenShotImage(screenShotFile,
					stage, galleryItem.getWidth(), galleryItem.getHeight())) {
				return null;
			}
			screenshotWrapperFrame.setCached(loc);
			return screenshotWrapperFrame;
		} catch (ContentTypeNotBoundException e) {
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.cachecontrol.IItemCachable#deconstruct(java.lang.String)
	 */
	@Override
	public GalleryItemDatabaseFormat deconstruct(String loc) {
		GalleryItemDatabaseFormat galleryItem = new GalleryItemDatabaseFormat();
		galleryItem.setType(CACHABLE_TYPE);
		if (screenShotFile == null) {
			return null;
		}
		if (!screenShotFile.isFile()) {
			return null;
		}
		if (!cached.equalsIgnoreCase(loc)) {
			ItemCaching.cacheFile(screenShotFile, loc);
		}
		galleryItem.addValue(screenShotFile.getName());
		return galleryItem;
	}

	/**
	 * @return the cached
	 */
	public String isCached() {
		return cached;
	}

	/**
	 * @param cached the cached to set
	 */
	public void setCached(String cached) {
		this.cached = cached;
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.additionalitems.interfaces.IScreenshotContainer#
	 * setScreenShotImage(java.io.File, multiplicity3.csys.stage.IStage, float,
	 * float)
	 */
	@Override
	public boolean setScreenShotImage(File screenShotFile, IStage stage,
			float width, float height) {

		this.screenShotFile = screenShotFile;

		try {

			IColourRectangle background = stage.getContentFactory().create(
					IColourRectangle.class, "screenshotbg", UUID.randomUUID());
			background.setSolidBackgroundColour(ColorRGBA.Black);
			background.setSize(width, height);

			ICachableImage screenShotImage = stage.getContentFactory().create(
					ICachableImage.class, "screenshot", UUID.randomUUID());
			screenShotImage.setImage(screenShotFile);
			screenShotImage.setSize(width, height);
			IRoundedBorder screenshotBorder = stage.getContentFactory()
					.create(IRoundedBorder.class, "screenshotborder",
							UUID.randomUUID());
			screenshotBorder.setBorderWidth(15f);
			screenshotBorder.setSize(width, height);
			screenshotBorder.setColor(new ColorRGBA(1, 1, 1, 0.75f));

			this.addItem(background);
			this.addItem(screenShotImage);
			this.addItem(screenshotBorder);

			RotateTranslateScaleBehaviour rtsBackground = stage
					.getBehaviourMaker().addBehaviour(background,
							RotateTranslateScaleBehaviour.class);
			rtsBackground.setItemActingOn(this);
			RotateTranslateScaleBehaviour rtsImage = stage.getBehaviourMaker()
					.addBehaviour(screenShotImage,
							RotateTranslateScaleBehaviour.class);
			rtsImage.setItemActingOn(this);
			RotateTranslateScaleBehaviour rtsBorder = stage.getBehaviourMaker()
					.addBehaviour(screenshotBorder,
							RotateTranslateScaleBehaviour.class);
			rtsBorder.setItemActingOn(this);

			NetworkFlickBehaviour nfImage = stage.getBehaviourMaker()
					.addBehaviour(screenShotImage, NetworkFlickBehaviour.class);
			nfImage.setMaxDimension(width);
			nfImage.setItemActingOn(this);
			nfImage.setDeceleration(100f);

			return true;
		} catch (ContentTypeNotBoundException e) {
			return false;
		} catch (NullPointerException e) {
			return false;
		}

	}

}
