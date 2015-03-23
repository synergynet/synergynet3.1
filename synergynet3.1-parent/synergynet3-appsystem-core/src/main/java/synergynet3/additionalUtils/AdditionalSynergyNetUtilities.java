package synergynet3.additionalUtils;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import multiplicity3.csys.behaviours.BehaviourMaker;
import multiplicity3.csys.behaviours.RotateTranslateScaleBehaviour;
import multiplicity3.csys.behaviours.ThreeDRotateInteraction;
import multiplicity3.csys.factory.ContentTypeNotBoundException;
import multiplicity3.csys.factory.IContentFactory;
import multiplicity3.csys.items.border.IRoundedBorder;
import multiplicity3.csys.items.container.IContainer;
import multiplicity3.csys.items.image.IImage;
import multiplicity3.csys.items.item.IItem;
import multiplicity3.csys.items.shapes.IColourRectangle;
import multiplicity3.csys.items.threed.IThreeDeeContent;
import multiplicity3.csys.stage.IStage;
import synergynet3.additionalitems.interfaces.ICachableImage;
import synergynet3.additionalitems.interfaces.IMediaPlayer;
import synergynet3.additionalitems.interfaces.IScreenshotContainer;
import synergynet3.additionalitems.jme.ScreenshotContainer;
import synergynet3.behaviours.networkflick.NetworkFlickBehaviour;
import synergynet3.feedbacksystem.FeedbackSystem;
import synergynet3.mediadetection.mediasearchtypes.AudioSearchType;
import synergynet3.mediadetection.mediasearchtypes.ImageSearchType;
import synergynet3.mediadetection.mediasearchtypes.VideoSearchType;
import synergynet3.mediadetection.mediasearchtypes.WavefrontSearchType;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;

/**
 * The Class AdditionalSynergyNetUtilities.
 */
public class AdditionalSynergyNetUtilities {

	/** Verifies whether a file is a valid audio file. */
	private static final AudioSearchType AUDIO_FILE_TYPE = new AudioSearchType();

	/** Size for Borders. */
	private static final float BORDER_SIZE = 35f;

	/** Verifies whether a file is a valid image file. */
	private static final ImageSearchType IMAGE_FILE_TYPE = new ImageSearchType();

	/** Possible file types for texture files. */
	private static final String[] TEXTURE_EXTENSIONS = { ".jpg", ".JPG",
			".jpeg", ".JPEG", ".gif", ".GIF", ".png", ".PNG" };

	/** Verifies whether a file is a valid video file. */
	private static final VideoSearchType VIDEO_FILE_TYPE = new VideoSearchType();

	/** Verifies whether a file is a valid wavefront object file. */
	private static final WavefrontSearchType WAVEFRONT_FILE_TYPE = new WavefrontSearchType();

	/** Width and height of audio containers to be generated. */
	protected static float AUDIO_DIMENSION = 125;

	/**
	 * Logger used to provide details on the current execution of the utilities
	 * offered by this class.
	 */
	protected static final Logger log = Logger
			.getLogger(AdditionalSynergyNetUtilities.class.getName());

	/** Dimensions of wavefront objects to be generated. */
	protected static float OBJ_SIZE = 400;

	/** Height of videos to be generated. */
	protected static float VID_HEIGHT = 385;

	/** Width of videos to be generated. */
	protected static float VID_WIDTH = 640;

	/** The pressed. */
	static boolean pressed = false;

	/**
	 * Creates a screenshot item from an image file.
	 * 
	 * @param screenShotFile The image file the item is to display.
	 * @param loc The location of the item on screen. ~ @param stage
	 *            Environment's stage.
	 * @param rot Rotation of the item on screen.
	 * @param displayWidth Width of the environment.
	 * @param displayWidth Height of the environment.
	 **/
	public static void buildScreenshotItem(File screenShotFile, Vector2f loc,
			IStage stage, float rot) {
		try {

			int displayWidth = (int) (stage.getWorldLocation().x * 2);
			int displayHeight = (int) (stage.getWorldLocation().y * 2);

			IContentFactory contentFactory = stage.getContentFactory();

			ScreenshotContainer screenshotWrapperFrame = contentFactory.create(
					IScreenshotContainer.class, screenShotFile.getName(),
					UUID.randomUUID());
			screenshotWrapperFrame.setScreenShotImage(screenShotFile, stage,
					displayWidth / 4, displayHeight / 4);

			FeedbackSystem.registerAsFeedbackEligible(screenshotWrapperFrame,
					displayWidth / 4, displayHeight / 4, stage);

			stage.addItem(screenshotWrapperFrame);

			screenshotWrapperFrame.setRelativeLocation(loc);
			screenshotWrapperFrame.setRelativeRotation(rot);

		} catch (ContentTypeNotBoundException e) {
			AdditionalSynergyNetUtilities.log(Level.SEVERE,
					"Content not Bound", e);
		}

	}

	/**
	 * Creates item for a file. If the file has a wav or mp3 extension an audio
	 * playing item is created. If the file has a jpg or png extension then an
	 * image item is created. Otherwise no item is created. Any items created
	 * are made legible for feedback and network flick capable.
	 * 
	 * @param file The file to potentially create an item from.
	 * @param stage Environment stage node
	 * @param deceleration The rate of the deceleration when flicked. A value of
	 *            -1 will make the item unflickable.
	 * @param importImageSizeLimit The max width or height of an item.
	 * @param borderColour The colour of the border around the item. Can be
	 *            invisible.
	 * @return The Item generated for the given file, or null if the file is not
	 *         valid.
	 **/
	public static IItem generateItemFromFile(File file, IStage stage,
			float deceleration, float importImageSizeLimit,
			ColorRGBA borderColour) {
		return generateItemFromFile(file, stage, deceleration,
				importImageSizeLimit, borderColour, -1, -1);
	}

	/**
	 * Creates item for a file. If the file has a wav or mp3 extension an audio
	 * playing item is created. If the file has a jpg or png extension then an
	 * image item is created. Otherwise no item is created. Any items created
	 * are made legible for feedback and network flick capable.
	 * 
	 * @param file The file to potentially create an item from.
	 * @param stage Environment stage node
	 * @param deceleration The rate of the deceleration when flicked. A value of
	 *            -1 will make the item unflickable.
	 * @param importImageSizeLimit The max width or height of an item.
	 * @param borderColour The colour of the border around the item. Can be
	 *            invisible.
	 * @param minScale The minimum scale the item can be scaled it.
	 * @param maxScale The maximum scale the item can be scaled it.
	 * @return The Item generated for the given file, or null if the file is not
	 *         valid.
	 **/
	public static IItem generateItemFromFile(File file, IStage stage,
			float deceleration, float importImageSizeLimit,
			ColorRGBA borderColour, float minScale, float maxScale) {
		IItem toReturn = null;

		BehaviourMaker behaviourMaker = stage.getBehaviourMaker();
		IContentFactory contentFactory = stage.getContentFactory();

		if (AUDIO_FILE_TYPE.isFileOfSearchType(file)
				|| VIDEO_FILE_TYPE.isFileOfSearchType(file)) {
			try {
				IMediaPlayer mediaPlayer = stage.getContentFactory().create(
						IMediaPlayer.class, file.getName(), UUID.randomUUID());
				mediaPlayer.setBorderColour(borderColour);
				mediaPlayer.setDeceleration(deceleration);

				if ((minScale != -1) && (maxScale != -1)) {
					mediaPlayer.setScaleLimits(minScale, maxScale);
				}

				float width = VID_WIDTH;
				float height = VID_HEIGHT;

				if (AUDIO_FILE_TYPE.isFileOfSearchType(file)) {
					width = AUDIO_DIMENSION;
					height = AUDIO_DIMENSION;
				}

				mediaPlayer.setLocalResource(file, false, false, stage);

				if ((width > importImageSizeLimit)
						|| (height > importImageSizeLimit)) {
					if (width < height) {
						float newWidth = width
								* (importImageSizeLimit / height);
						mediaPlayer.setSize(newWidth, importImageSizeLimit);
					} else {
						float newHeight = height
								* (importImageSizeLimit / width);
						mediaPlayer.setSize(importImageSizeLimit, newHeight);
					}
				} else {
					mediaPlayer.setSize(width, height);
				}

				stage.addItem(mediaPlayer);
				toReturn = mediaPlayer;
				FeedbackSystem.registerAsFeedbackEligible(mediaPlayer, width,
						height, stage);
			} catch (Exception e) {
				log(Level.SEVERE, "Exception", e);
			}
		} else if (IMAGE_FILE_TYPE.isFileOfSearchType(file)) {
			// Check to make sure not texture for obj
			for (String ext : TEXTURE_EXTENSIONS) {
				if (file.getPath().contains(ext)) {
					File textureFile = new File(file.getPath().replace(ext,
							".obj"));
					if (textureFile.exists()) {
						return null;
					}
				}
			}

			try {
				ICachableImage fileImage = contentFactory
						.create(ICachableImage.class, file.getName(),
								UUID.randomUUID());
				fileImage.setImage(file);

				RotateTranslateScaleBehaviour rts = stage.getBehaviourMaker()
						.addBehaviour(fileImage,
								RotateTranslateScaleBehaviour.class);
				rts.setItemActingOn(fileImage);

				if ((minScale != -1) && (maxScale != -1)) {
					rts.setScaleLimits(minScale, maxScale);
				}

				if (deceleration >= 0) {
					behaviourMaker.addBehaviour(fileImage,
							NetworkFlickBehaviour.class).setDeceleration(
							deceleration);
				}

				int width = 50;
				int height = 50;
				try {
					ImageInputStream in = ImageIO.createImageInputStream(file);
					try {
						final Iterator<ImageReader> readers = ImageIO
								.getImageReaders(in);
						if (readers.hasNext()) {
							ImageReader reader = readers.next();
							try {
								reader.setInput(in);
								width = reader.getWidth(0);
								height = reader.getHeight(0);
							} finally {
								reader.dispose();
							}
						}
					} finally {
						if (in != null) {
							in.close();
						}
					}
				} catch (IOException e) {
					log(Level.SEVERE, "IOException: ", e);
				}

				if ((width > importImageSizeLimit)
						|| (height > importImageSizeLimit)) {
					if (width < height) {
						float newWidth = width
								* (importImageSizeLimit / height);
						fileImage.setSize(newWidth, importImageSizeLimit);
					} else {
						float newHeight = height
								* (importImageSizeLimit / width);
						fileImage.setSize(importImageSizeLimit, newHeight);
					}
				} else {
					fileImage.setSize(width, height);
				}

				stage.addItem(fileImage);
				toReturn = fileImage;

				fileImage.generateBorder(stage, borderColour, 5f);

				FeedbackSystem.registerAsFeedbackEligible(fileImage,
						fileImage.getWidth(), fileImage.getHeight(), stage);

			} catch (ContentTypeNotBoundException e) {
				log(Level.SEVERE, "Content not Bound", e);
			}
		} else if (WAVEFRONT_FILE_TYPE.isFileOfSearchType(file)) {

			try {

				final IThreeDeeContent threeDItem = stage.getContentFactory()
						.create(IThreeDeeContent.class, file.getName(),
								UUID.randomUUID());
				threeDItem.setModel(file);
				for (String ext : TEXTURE_EXTENSIONS) {
					File textureFile = new File(file.getPath().replace(".obj",
							ext));
					if (textureFile.exists()) {
						threeDItem.setTexture(textureFile);
						break;
					}

				}

				Vector3f originalDimensions = threeDItem.getSize();

				float width = originalDimensions.getX();
				float height = originalDimensions.getY();
				float depth = originalDimensions.getZ();

				threeDItem.setRelativeLocation(new Vector2f(-width / 2,
						-height / 2));

				float largestDim = width;
				if (largestDim < height) {
					largestDim = height;
				}
				if (largestDim < depth) {
					largestDim = depth;
				}
				largestDim *= 4;

				float scaleFactor = (OBJ_SIZE - (BORDER_SIZE * 2)) / largestDim;
				threeDItem.setRelativeScale(scaleFactor);

				IContainer objectContainer = contentFactory.create(
						IContainer.class, file.getName() + "Container",
						UUID.randomUUID());

				IColourRectangle objectBackground = stage.getContentFactory()
						.create(IColourRectangle.class, file.getName() + "Bg",
								UUID.randomUUID());
				objectBackground.setSolidBackgroundColour(ColorRGBA.Black);
				objectBackground.setSize(OBJ_SIZE, OBJ_SIZE);
				objectBackground.setInteractionEnabled(false);

				IImage listener = stage.getContentFactory().create(
						IImage.class, file.getName() + "listenBlock",
						UUID.randomUUID());
				listener.setSize(OBJ_SIZE - (BORDER_SIZE * 2), OBJ_SIZE
						- (BORDER_SIZE * 2));

				IRoundedBorder objectBorder = stage.getContentFactory().create(
						IRoundedBorder.class, file.getName() + "Border",
						UUID.randomUUID());
				objectBorder.setBorderWidth(BORDER_SIZE);
				objectBorder.setSize(OBJ_SIZE, OBJ_SIZE);
				objectBorder.setColor(borderColour);

				objectContainer.addItem(objectBackground);
				objectContainer.addItem(threeDItem);
				objectContainer.addItem(objectBorder);
				objectBorder.addItem(listener);

				// objectContainer.getZOrderManager().unregisterForZOrdering(objectBackground);

				ThreeDRotateInteraction threeDRotateInteraction = stage
						.getBehaviourMaker().addBehaviour(listener,
								ThreeDRotateInteraction.class);
				threeDRotateInteraction.setItemActingOn(threeDItem);
				threeDRotateInteraction.setParentItem(objectContainer);

				ThreeDRotateInteraction threeDRotateInteractionObject = stage
						.getBehaviourMaker().addBehaviour(threeDItem,
								ThreeDRotateInteraction.class);
				threeDRotateInteractionObject.setParentItem(objectContainer);

				RotateTranslateScaleBehaviour rts = stage.getBehaviourMaker()
						.addBehaviour(objectBorder,
								RotateTranslateScaleBehaviour.class);
				rts.setItemActingOn(objectContainer);
				if ((minScale != -1) && (maxScale != -1)) {
					rts.setScaleLimits(minScale, maxScale);
				}

				stage.addItem(objectContainer);
				toReturn = objectContainer;

				// This could be an issue in future circumstances - could make a
				// cachable wrapper
				FeedbackSystem.registerAsFeedbackEligible(objectContainer,
						OBJ_SIZE, OBJ_SIZE, stage);
			} catch (Exception e) {
				log(Level.SEVERE, "Exception", e);
			}
		}
		return toReturn;

	}

	/**
	 * Checks if a location is within a box.
	 * 
	 * @param loc The location to check is inside the box.
	 * @param boxLoc The location of the box.
	 * @param dim Dimensions of the box.
	 * @return Boolean representing whether the location given is in the box.
	 **/
	public static boolean inBox(Vector2f loc, Vector2f boxLoc, Vector2f dim) {
		if ((loc.x > (boxLoc.x - (dim.x / 2)))
				&& (loc.x < (boxLoc.x + (dim.x / 2)))
				&& (loc.y > (boxLoc.y - (dim.y / 2)))
				&& (loc.y < (boxLoc.y + (dim.y / 2)))) {
			return true;
		}
		return false;
	}

	/**
	 * Checks if a location is within a circle.
	 * 
	 * @param loc The location to check is inside the circle.
	 * @param circleLoc The location of the circle.
	 * @param radius Radius of the circle.
	 * @return Boolean representing whether the location given is in the box.
	 **/
	public static boolean inRadius(Vector2f loc, Vector2f circleLoc,
			float radius) {
		if (loc.distance(circleLoc) < radius) {
			return true;
		}
		return false;
	}

	/**
	 * Produces a log message using the logger.
	 * 
	 * @param level The level indicating the severity of the log message.
	 * @param logMessage The message to be shown in the log.
	 * @param ex The exception being logged.
	 **/
	public static void log(Level level, String logMessage, Exception ex) {
		log.log(level, logMessage, ex);
	}

	/**
	 * Produces a log message using the logger.
	 * 
	 * @param logMessage The message to be shown in the log.
	 **/
	public static void logInfo(String logMessage) {
		log.info(logMessage);
	}

	/**
	 * Takes an array of items and stacks them in a pile at various rotations.
	 *
	 * @param items The items to be stacked.
	 * @param x X location at which the pile of items should appear.
	 * @param y Y location at which the pile of items should appear.
	 * @param angleOffSet The degree to which items in the pile may be rotated.
	 * @param positionOffset How far along an axis from the centre of the pile
	 *            an item may be positioned.
	 **/
	public static void pile(IItem[] items, float x, float y, float angleOffSet,
			float positionOffset) {
		for (IItem item : items) {
			if (item != null) {
				float xOffset = (float) Math.random() * positionOffset;
				if (Math.random() > 0.5) {
					xOffset = -xOffset;
				}
				float yOffset = (float) Math.random() * positionOffset;
				if (Math.random() > 0.5) {
					yOffset = -yOffset;
				}
				item.setRelativeLocation(new Vector2f(x + xOffset, y + yOffset));
				int angle = (int) ((Math.random() * (angleOffSet)) + 0.5);
				if (Math.random() > 0.5) {
					angle = -angle;
				}
				item.setRelativeRotation((float) Math.toRadians(angle));
			}
		}
	}

}
