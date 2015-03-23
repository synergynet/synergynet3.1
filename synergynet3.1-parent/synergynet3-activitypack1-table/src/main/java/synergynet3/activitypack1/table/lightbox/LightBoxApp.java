package synergynet3.activitypack1.table.lightbox;

import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import multiplicity3.appsystem.IMultiplicityApp;
import multiplicity3.appsystem.IQueueOwner;
import multiplicity3.appsystem.MultiplicityClient;
import multiplicity3.csys.MultiplicityEnvironment;
import multiplicity3.csys.behaviours.BehaviourMaker;
import multiplicity3.csys.behaviours.RotateTranslateScaleBehaviour;
import multiplicity3.csys.factory.ContentTypeNotBoundException;
import multiplicity3.csys.factory.IContentFactory;
import multiplicity3.csys.items.image.IImage;
import multiplicity3.csys.items.item.IItem;
import multiplicity3.csys.items.mutablelabel.IMutableLabel;
import multiplicity3.csys.stage.IStage;
import multiplicity3.input.MultiTouchInputComponent;
import synergynet3.activitypack1.core.lightbox.lightboxmodel.LightBox;
import synergynet3.activitypack1.core.lightbox.lightboxmodel.items.ImageItem;
import synergynet3.activitypack1.core.lightbox.lightboxmodel.items.LightBoxItem;
import synergynet3.activitypack1.core.lightbox.lightboxmodel.items.TextItem;
import synergynet3.activitypack1.core.lightbox.ppt2lightbox.PowerpointToLightBoxConvertor;
import synergynet3.cluster.SynergyNetCluster;

import com.jme3.asset.AssetKey;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;

/**
 * The Class LightBoxApp.
 */
public class LightBoxApp implements IMultiplicityApp {

	/** The Constant ARIAL_WHITE_PATH. */
	private static final String ARIAL_WHITE_PATH = "/arial32_white.fnt";

	/** The Constant DEFAULT_PPT_FILE. */
	private static final String DEFAULT_PPT_FILE = "example_a.ppt";

	/** The Constant RESOURCES_DIR. */
	private static final String RESOURCES_DIR = "synergynet3/activitypack1/table/lightbox/";

	/** The behaviour maker. */
	private BehaviourMaker behaviourMaker;

	/** The content factory. */
	private IContentFactory contentFactory;

	/** The stage. */
	private IStage stage;

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		MultiplicityClient client = MultiplicityClient.get();
		client.start();
		LightBoxApp app = new LightBoxApp();
		client.setCurrentApp(app);
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.appsystem.IMultiplicityApp#getFriendlyAppName()
	 */
	@Override
	public String getFriendlyAppName() {
		return "LightBox";
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.appsystem.IMultiplicityApp#onDestroy()
	 */
	@Override
	public void onDestroy() {
		SynergyNetCluster.get().shutdown();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.appsystem.IMultiplicityApp#shouldStart(multiplicity3.input
	 * .MultiTouchInputComponent, multiplicity3.appsystem.IQueueOwner)
	 */
	@Override
	public void shouldStart(MultiTouchInputComponent input, IQueueOwner iqo) {
		this.stage = MultiplicityEnvironment.get().getLocalStages().get(0);
		stage.getZOrderManager().setAutoBringToTop(false);
		this.behaviourMaker = this.stage.getBehaviourMaker();
		this.contentFactory = this.stage.getContentFactory();
		try {
			loadDefaultContent();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ContentTypeNotBoundException e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.appsystem.IMultiplicityApp#shouldStop()
	 */
	@Override
	public void shouldStop() {
	}

	/**
	 * Convert zero to one size to screen size.
	 *
	 * @param size the size
	 * @return the vector2f
	 */
	private Vector2f convertZeroToOneSizeToScreenSize(Point2D.Float size) {
		Vector2f screenSize = new Vector2f();
		stage.tableToWorld(new Vector2f(size.x, size.y), screenSize);
		return screenSize;
	}

	/**
	 * Gets the default light box from ppt file.
	 *
	 * @param lightBoxDirectory the light box directory
	 * @return the default light box from ppt file
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private LightBox getDefaultLightBoxFromPPTFile(File lightBoxDirectory)
			throws IOException {
		InputStream pptFileInputStream = getDefaultPPTStream();
		return PowerpointToLightBoxConvertor.convertSlideFromStream(0,
				pptFileInputStream, lightBoxDirectory, "default");
	}

	/**
	 * Gets the default ppt stream.
	 *
	 * @return the default ppt stream
	 */
	private InputStream getDefaultPPTStream() {
		return MultiplicityClient.assetManager.locateAsset(
				new AssetKey<File>(RESOURCES_DIR + DEFAULT_PPT_FILE))
				.openStream();
	}

	/**
	 * Gets the temporary light box directory.
	 *
	 * @return the temporary light box directory
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private File getTemporaryLightBoxDirectory() throws IOException {
		File tempFile = File.createTempFile("lightbox-app", ".tmp");
		tempFile.delete();
		tempFile.mkdir();
		return tempFile;
	}

	/**
	 * Load all image items.
	 *
	 * @param model the model
	 * @param lightBoxDirectory the light box directory
	 * @throws ContentTypeNotBoundException the content type not bound exception
	 */
	private void loadAllImageItems(LightBox model, File lightBoxDirectory)
			throws ContentTypeNotBoundException {
		for (ImageItem item : model.getImageItems()) {
			IItem onScreenItem = loadImageItem(item, lightBoxDirectory);
			stage.addItem(onScreenItem);
			setPosition(onScreenItem, item);
		}
	}

	/**
	 * Load all text items.
	 *
	 * @param model the model
	 * @throws ContentTypeNotBoundException the content type not bound exception
	 */
	private void loadAllTextItems(LightBox model)
			throws ContentTypeNotBoundException {
		for (TextItem item : model.getTextItems()) {
			IItem onScreenItem = loadTextItem(item);
			stage.addItem(onScreenItem);
			setPosition(onScreenItem, item);
			float rotationRadians = FastMath.DEG_TO_RAD
					* item.getRotationDegrees();
			rotationRadians *= -1;
			onScreenItem.setRelativeRotation(rotationRadians);
		}
	}

	/**
	 * Load default content.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ContentTypeNotBoundException the content type not bound exception
	 */
	private void loadDefaultContent() throws IOException,
			ContentTypeNotBoundException {
		File lightBoxDirectory = getTemporaryLightBoxDirectory();
		LightBox model = getDefaultLightBoxFromPPTFile(lightBoxDirectory);
		loadAllTextItems(model);
		loadAllImageItems(model, lightBoxDirectory);
	}

	/**
	 * Load image item.
	 *
	 * @param item the item
	 * @param lightBoxDirectory the light box directory
	 * @return the i item
	 * @throws ContentTypeNotBoundException the content type not bound exception
	 */
	private IItem loadImageItem(ImageItem item, File lightBoxDirectory)
			throws ContentTypeNotBoundException {
		IImage image = contentFactory.create(IImage.class, "",
				UUID.randomUUID());
		Vector2f imageSizeOnScreen = convertZeroToOneSizeToScreenSize(item
				.getSize());
		image.setImage(new File(lightBoxDirectory, item.getImageFileName()));
		image.setSize(imageSizeOnScreen);
		float rotationRadians = FastMath.DEG_TO_RAD * item.getRotationDegrees();
		rotationRadians *= -1;
		image.setRelativeRotation(rotationRadians);
		if (item.isMoveable()) {
			RotateTranslateScaleBehaviour rts = behaviourMaker.addBehaviour(
					image, RotateTranslateScaleBehaviour.class);
			rts.setScaleEnabled(false);
		}
		return image;
	}

	/**
	 * Load text item.
	 *
	 * @param item the item
	 * @return the i item
	 * @throws ContentTypeNotBoundException the content type not bound exception
	 */
	private IItem loadTextItem(TextItem item)
			throws ContentTypeNotBoundException {
		IMutableLabel lbl = contentFactory.create(IMutableLabel.class, "",
				UUID.randomUUID());
		lbl.setFont(RESOURCES_DIR + ARIAL_WHITE_PATH);
		lbl.setText(item.getText());
		if (item.isMoveable()) {
			RotateTranslateScaleBehaviour rts = behaviourMaker.addBehaviour(
					lbl, RotateTranslateScaleBehaviour.class);
			rts.setScaleEnabled(false);
		}
		float fontScale = (item.getFontSize() / 32.0f) * 1.35f;
		lbl.setFontScale(fontScale);
		Vector2f boxSize = convertZeroToOneSizeToScreenSize(item.getSize());
		lbl.setBoxSize(boxSize.x, boxSize.y);
		return lbl;
	}

	/**
	 * Sets the position.
	 *
	 * @param onScreenItem the on screen item
	 * @param item the item
	 */
	private void setPosition(IItem onScreenItem, LightBoxItem item) {
		Vector2f itemPositionInTableCoordinates = new Vector2f(
				item.getPosition().x, 1 - item.getPosition().y);
		Vector2f itemPositionInScreenCoordinates = new Vector2f();
		stage.tableToScreen(itemPositionInTableCoordinates,
				itemPositionInScreenCoordinates);
		onScreenItem.setWorldLocation(itemPositionInScreenCoordinates);
	}

}
