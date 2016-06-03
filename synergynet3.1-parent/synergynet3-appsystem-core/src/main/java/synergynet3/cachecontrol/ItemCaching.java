package synergynet3.cachecontrol;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.regex.PatternSyntaxException;

import multiplicity3.appsystem.MultiplicityClient;
import multiplicity3.csys.behaviours.RotateTranslateScaleBehaviour;
import multiplicity3.csys.items.item.IItem;
import multiplicity3.csys.stage.IStage;
import synergynet3.additionalitems.interfaces.ICachableImage;
import synergynet3.additionalitems.jme.CachableImage;
import synergynet3.additionalitems.jme.CachableLine;
import synergynet3.additionalitems.jme.MediaPlayer;
import synergynet3.additionalitems.jme.ScreenshotContainer;
import synergynet3.additionalitems.jme.Textbox;
import synergynet3.behaviours.networkflick.NetworkFlickBehaviour;
import synergynet3.config.web.CacheOrganisation;
import synergynet3.databasemanagement.GalleryItemDatabaseFormat;
import synergynet3.feedbacksystem.FeedbackContainer;
import synergynet3.feedbacksystem.FeedbackItem;
import synergynet3.feedbacksystem.FeedbackSystem;
import synergynet3.feedbacksystem.defaultfeedbacktypes.AudioFeedback;
import synergynet3.feedbacksystem.defaultfeedbacktypes.SimpleTrafficLightFeedback;
import synergynet3.feedbacksystem.defaultfeedbacktypes.SmilieFeedback;
import synergynet3.feedbacksystem.defaultfeedbacktypes.YesOrNoFeedback;

import com.jme3.asset.AssetManager;

/**
 * The Class ItemCaching.
 */
public class ItemCaching {

	/**
	 * Cache file.
	 *
	 * @param file the file
	 * @param studentID the student id
	 */
	public static void cacheFile(File file, String studentID) {
		if (file != null) {
			if (file.isFile()) {
				copyFile(file, CacheOrganisation.getSpecificDir(studentID)
						+ File.separator + file.getName());
			}
		}
	}

	/**
	 * Cache image.
	 *
	 * @param image the image
	 * @param studentID the student id
	 */
	public static void cacheImage(String image, String studentID) {
		copyImage(image, removeExtension(getFullImageName(image, studentID)));
	}

	/**
	 * Copy image.
	 *
	 * @param oldImage the old image
	 * @param newAddress the new address
	 */
	public static void copyImage(String oldImage, String newAddress) {
		newAddress += "." + getExtension(oldImage);
		File outputFile = new File(newAddress);
		AssetManager assetManager = MultiplicityClient.assetManager;
		try {
			if (outputFile.isFile()) {
				return;
			}
			outputFile.createNewFile();

			InputStream in = assetManager.locateAsset(
					assetManager.loadTexture(oldImage).getKey()).openStream();
			OutputStream out = new FileOutputStream(outputFile);

			byte[] buf = new byte[1024];

			int len;

			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}

			in.close();
			out.close();

		} catch (IOException e) {
			e.printStackTrace();
			outputFile.delete();
		}
	}

	/**
	 * Deconstruct item.
	 *
	 * @param item the item
	 * @param info the info
	 * @param studentID the student id
	 * @return the gallery item database format
	 */
	public static GalleryItemDatabaseFormat deconstructItem(IItem item,
			Object[] info, String studentID) {

		GalleryItemDatabaseFormat itemRepresentation = null;
		FeedbackContainer feedbackContainer = null;

		if (info[2] != null) {
			if (info[2] instanceof FeedbackContainer) {
				feedbackContainer = (FeedbackContainer) info[2];
				item = feedbackContainer.getContainedItem();
			}
		}

		if (item instanceof IItemCachable) {
			IItemCachable cacheItem = (IItemCachable) item;
			itemRepresentation = cacheItem.deconstruct(studentID);
			if (itemRepresentation == null) {
				return null;
			}
			itemRepresentation.setWidth((Float) info[0]);
			itemRepresentation.setHeight((Float) info[1]);

			if (feedbackContainer != null) {
				for (FeedbackItem feedbackItem : feedbackContainer
						.getFeedbackViewer().getFeedbackItems()) {
					if (feedbackItem instanceof IFeedbackItemCachable) {
						IFeedbackItemCachable feedbackCacheItem = feedbackItem;
						itemRepresentation.addFeedbackItem(feedbackCacheItem
								.deconstruct(studentID));
					}
				}
			}
		}

		return itemRepresentation;
	}

	/**
	 * Gets the extension.
	 *
	 * @param s the s
	 * @return the extension
	 */
	public static String getExtension(String s) {
		String ext = null;
		int i = s.lastIndexOf('.');

		if ((i > 0) && (i < (s.length() - 1))) {
			ext = s.substring(i + 1);
		}

		if (ext == null) {
			return "";
		}

		return ext;
	}

	/**
	 * Gets the full image name.
	 *
	 * @param image the image
	 * @param studentID the student id
	 * @return the full image name
	 */
	public static String getFullImageName(String image, String studentID) {
		String pathValues[];
		try {
			pathValues = image.split(File.separator);
		} catch (PatternSyntaxException e) {
			pathValues = image.split("/");
		}

		return CacheOrganisation.getSpecificDir(studentID) + File.separator
				+ pathValues[pathValues.length - 1];
	}

	/**
	 * Reconstruct item.
	 *
	 * @param itemRepresentation the item representation
	 * @param stage the stage
	 * @param studentID the student id
	 * @return the i item
	 */
	public static IItem reconstructItem(
			GalleryItemDatabaseFormat itemRepresentation, IStage stage,
			String studentID) {
		return reconstructItem(itemRepresentation, stage, studentID, 0);
	}

	/**
	 * Reconstruct item.
	 *
	 * @param itemRepresentation the item representation
	 * @param stage the stage
	 * @param studentID the student id
	 * @param deceleration the deceleration
	 * @return the i item
	 */
	public static IItem reconstructItem(
			GalleryItemDatabaseFormat itemRepresentation, IStage stage,
			String studentID, float deceleration) {
		String type = itemRepresentation.getType();
		IItem toReturn = null;
		if (type.equalsIgnoreCase(CachableImage.CACHABLE_TYPE)) {
			ICachableImage image = CachableImage.reconstruct(
					itemRepresentation, stage, studentID);
			toReturn = image;
			image.setVisible(false);
			if (image != null) {
				stage.getBehaviourMaker().addBehaviour(image,
						RotateTranslateScaleBehaviour.class);
				if (deceleration >= 0) {
					stage.getBehaviourMaker()
							.addBehaviour(image, NetworkFlickBehaviour.class)
							.setDeceleration(deceleration);
				}
			}
		} else if (type.equalsIgnoreCase(ScreenshotContainer.CACHABLE_TYPE)) {
			ScreenshotContainer screenshotContainer = ScreenshotContainer
					.reconstruct(itemRepresentation, stage, studentID);
			screenshotContainer.setVisible(false);
			toReturn = screenshotContainer;
		} else if (type.equalsIgnoreCase(MediaPlayer.CACHABLE_TYPE)) {
			MediaPlayer mediaPlayer = MediaPlayer.reconstruct(
					itemRepresentation, stage, studentID);
			mediaPlayer.setVisible(false);
			toReturn = mediaPlayer;
		} else if (type.equalsIgnoreCase(Textbox.CACHABLE_TYPE)) {
			Textbox textBox = Textbox.reconstruct(itemRepresentation, stage,
					studentID);
			textBox.setVisible(false);
			toReturn = textBox;
			if (deceleration >= 0) {
				NetworkFlickBehaviour nf = stage.getBehaviourMaker()
						.addBehaviour(textBox.getListenBlock(), NetworkFlickBehaviour.class);
				nf.setItemActingOn(textBox);
				nf.setDeceleration(deceleration);
			}
		} else if (type.equalsIgnoreCase(CachableLine.CACHABLE_TYPE)) {
			CachableLine line = CachableLine.reconstruct(itemRepresentation,
					stage, studentID);
			line.setVisible(false);
			toReturn = line;
		}
		if (toReturn != null) {
			if (itemRepresentation.getFeedbackItems().size() > 0) {
				FeedbackContainer feedbackContainer = new FeedbackContainer(
						stage);

				FeedbackSystem.registerAsFeedbackEligible(toReturn,
						itemRepresentation.getWidth(),
						itemRepresentation.getHeight(), stage);
				feedbackContainer.setItem(toReturn);

				for (Object[] feedbackItem : itemRepresentation
						.getFeedbackItems()) {

					String feedbackType = (String) feedbackItem[0];
					FeedbackItem feedback = null;

					if (feedbackType
							.equalsIgnoreCase(AudioFeedback.CACHABLE_TYPE)) {
						feedback = AudioFeedback.reconstruct(feedbackItem);
					} else if (feedbackType
							.equalsIgnoreCase(YesOrNoFeedback.CACHABLE_TYPE)) {
						feedback = YesOrNoFeedback.reconstruct(feedbackItem);
					} else if (feedbackType
							.equalsIgnoreCase(SmilieFeedback.CACHABLE_TYPE)) {
						feedback = SmilieFeedback.reconstruct(feedbackItem);
					} else if (feedbackType
							.equalsIgnoreCase(SimpleTrafficLightFeedback.CACHABLE_TYPE)) {
						feedback = SimpleTrafficLightFeedback
								.reconstruct(feedbackItem);
					}

					if (feedback != null) {
						feedback.setStage(stage);
						feedbackContainer.getFeedbackViewer().addFeedback(
								feedback);
						feedbackContainer.getFeedbackViewer().getContainer()
								.setVisibility(false);
					}
				}

				toReturn = feedbackContainer.getWrapper();
			}

			if (!FeedbackSystem.isItemFeedbackEligible(toReturn)) {
				FeedbackSystem.registerAsFeedbackEligible(toReturn,
						itemRepresentation.getWidth(),
						itemRepresentation.getHeight(), stage);
			}
		}
		return toReturn;
	}

	/**
	 * Removes the extension.
	 *
	 * @param s the s
	 * @return the string
	 */
	public static String removeExtension(String s) {
		String sFull = null;
		int i = s.lastIndexOf('.');

		if (i > 1) {
			sFull = s.substring(0, i);
		}

		if (sFull == null) {
			return "";
		}

		return sFull;
	}

	/**
	 * Copy file.
	 *
	 * @param inputFile the input file
	 * @param newAddress the new address
	 */
	private static void copyFile(File inputFile, String newAddress) {
		File outputFile = new File(newAddress);
		try {
			if (outputFile.isFile()) {
				return;
			}
			outputFile.createNewFile();

			InputStream in = new FileInputStream(inputFile);
			OutputStream out = new FileOutputStream(outputFile);

			byte[] buf = new byte[1024];

			int len;

			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}

			in.close();
			out.close();

		} catch (IOException e) {
			e.printStackTrace();
			outputFile.delete();
		}

	}

}
