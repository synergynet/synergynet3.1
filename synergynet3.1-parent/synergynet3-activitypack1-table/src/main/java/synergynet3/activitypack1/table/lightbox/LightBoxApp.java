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

public class LightBoxApp implements IMultiplicityApp {

	private static final String DEFAULT_PPT_FILE = "example_a.ppt";
	private static final String RESOURCES_DIR = "synergynet3/activitypack1/table/lightbox/";
	private static final String ARIAL_WHITE_PATH = "/arial32_white.fnt";

	public static void main(String[] args) {
		MultiplicityClient client = MultiplicityClient.get();
		client.start();
		LightBoxApp app = new LightBoxApp();
		client.setCurrentApp(app);
	}

	private IContentFactory contentFactory;
	private IStage stage;
	private BehaviourMaker behaviourMaker;

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

	private void loadDefaultContent() throws IOException, ContentTypeNotBoundException {
		File lightBoxDirectory = getTemporaryLightBoxDirectory();
		LightBox model = getDefaultLightBoxFromPPTFile(lightBoxDirectory);
		loadAllTextItems(model);
		loadAllImageItems(model, lightBoxDirectory);
	}

	private void loadAllTextItems(LightBox model) throws ContentTypeNotBoundException {
		for(TextItem item : model.getTextItems()) {
			IItem onScreenItem = loadTextItem(item);
			stage.addItem(onScreenItem);
			setPosition(onScreenItem, item);
			float rotationRadians = FastMath.DEG_TO_RAD * item.getRotationDegrees();
			rotationRadians *= -1;
			onScreenItem.setRelativeRotation(rotationRadians);
		}
	}
	
	private void loadAllImageItems(LightBox model, File lightBoxDirectory) throws ContentTypeNotBoundException {
		for(ImageItem item : model.getImageItems()) {
			IItem onScreenItem = loadImageItem(item, lightBoxDirectory);			
			stage.addItem(onScreenItem);
			setPosition(onScreenItem, item);
		}
	}

	private IItem loadImageItem(ImageItem item, File lightBoxDirectory) throws ContentTypeNotBoundException {
		IImage image = contentFactory.create(IImage.class, "", UUID.randomUUID());
		Vector2f imageSizeOnScreen = convertZeroToOneSizeToScreenSize(item.getSize());
		image.setImage(new File(lightBoxDirectory, item.getImageFileName()));
		image.setSize(imageSizeOnScreen);
		float rotationRadians = FastMath.DEG_TO_RAD * item.getRotationDegrees();
		rotationRadians *= -1;
		image.setRelativeRotation(rotationRadians);
		if(item.isMoveable()) {
			RotateTranslateScaleBehaviour rts = behaviourMaker.addBehaviour(image, RotateTranslateScaleBehaviour.class);
			rts.setScaleEnabled(false);
		}
		return image;
	}

	private IItem loadTextItem(TextItem item) throws ContentTypeNotBoundException {
		IMutableLabel lbl = contentFactory.create(IMutableLabel.class, "", UUID.randomUUID());
		lbl.setFont(RESOURCES_DIR + ARIAL_WHITE_PATH);
		lbl.setText(item.getText());
		if(item.isMoveable()) {
			RotateTranslateScaleBehaviour rts = behaviourMaker.addBehaviour(lbl, RotateTranslateScaleBehaviour.class);
			rts.setScaleEnabled(false);
		}
		float fontScale = item.getFontSize() / 32.0f * 1.35f;
		lbl.setFontScale(fontScale);
		Vector2f boxSize = convertZeroToOneSizeToScreenSize(item.getSize());
		lbl.setBoxSize(boxSize.x, boxSize.y);
		return lbl;
	}
	
	private void setPosition(IItem onScreenItem, LightBoxItem item) {
		Vector2f itemPositionInTableCoordinates = new Vector2f(item.getPosition().x, 1-item.getPosition().y);
		Vector2f itemPositionInScreenCoordinates = new Vector2f();			
		stage.tableToScreen(itemPositionInTableCoordinates, itemPositionInScreenCoordinates);
		onScreenItem.setWorldLocation(itemPositionInScreenCoordinates);
	}

	private Vector2f convertZeroToOneSizeToScreenSize(Point2D.Float size) {
		Vector2f screenSize = new Vector2f();
		stage.tableToWorld(new Vector2f(size.x, size.y), screenSize);
		return screenSize;
	}

	private LightBox getDefaultLightBoxFromPPTFile(File lightBoxDirectory) throws IOException {
		InputStream pptFileInputStream = getDefaultPPTStream();
		return PowerpointToLightBoxConvertor.convertSlideFromStream(0, pptFileInputStream, lightBoxDirectory, "default");
	}

	private File getTemporaryLightBoxDirectory() throws IOException {
		File tempFile = File.createTempFile("lightbox-app", ".tmp");
		tempFile.delete();
		tempFile.mkdir();
		return tempFile;
	}

	private InputStream getDefaultPPTStream() {
		return MultiplicityClient.assetManager.locateAsset(new AssetKey<File>(RESOURCES_DIR + DEFAULT_PPT_FILE)).openStream();
	}

	@Override
	public String getFriendlyAppName() {
		return "LightBox";
	}

	@Override
	public void shouldStop() {}

	@Override
	public void onDestroy() {
		SynergyNetCluster.get().shutdown();
	}

}
