package multiplicity3.appsystem.jme;

import java.util.logging.Logger;

import multiplicity3.config.display.DisplayPrefsItem;

import com.jme3.app.Application;
import com.jme3.app.StatsView;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial.CullHint;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeContext.Type;
import com.jme3.util.BufferUtils;

/**
 * <code>SimpleApplication</code> extends the {@link com.jme3.app.Application}
 * class to provide default functionality like a first-person camera, and an
 * accessible root node that is updated and rendered regularly. Additionally,
 * <code>SimpleApplication</code> will display a statistics view using the
 * {@link com.jme3.app.StatsView} class. It will display the current
 * frames-per-second value on-screen in addition to the statistics. Several keys
 * have special functionality in <code>SimpleApplication</code>:<br/>
 * <table>
 * <tr>
 * <td>Esc</td>
 * <td>- Close the application</td>
 * </tr>
 * <tr>
 * <td>C</td>
 * <td>- Display the camera position and rotation in the console.</td>
 * </tr>
 * <tr>
 * <td>M</td>
 * <td>- Display memory usage in the console.</td>
 * </tr>
 * </table>
 */
public abstract class JMEAppRoot extends Application {

	/**
	 * The listener interface for receiving appAction events. The class that is
	 * interested in processing a appAction event implements this interface, and
	 * the object created with that class is registered with a component using
	 * the component's <code>addAppActionListener<code> method. When
	 * the appAction event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see AppActionEvent
	 */
	private class AppActionListener implements ActionListener {

		/*
		 * (non-Javadoc)
		 * @see
		 * com.jme3.input.controls.ActionListener#onAction(java.lang.String,
		 * boolean, float)
		 */
		public void onAction(String name, boolean value, float tpf) {
			if (!value) {
				return;
			}

			if (name.equals("SIMPLEAPP_Exit")) {
				stop();
			} else if (name.equals("SIMPLEAPP_CameraPos")) {
				if (cam != null) {
					Vector3f loc = cam.getLocation();
					Quaternion rot = cam.getRotation();
					log.info("Camera Position: (" + loc.x + ", " + loc.y + ", "
							+ loc.z + ")");
					log.info("Camera Rotation: " + rot);
					log.info("Camera Direction: " + cam.getDirection());
				}
			} else if (name.equals("SIMPLEAPP_Memory")) {
				BufferUtils.printCurrentDirectMemory(null);
			}
		}
	}

	/** The Constant log. */
	private static final Logger log = Logger.getLogger(JMEAppRoot.class
			.getName());

	/** The action listener. */
	private AppActionListener actionListener = new AppActionListener();

	/** The root node. */
	private Node rootNode = new Node("Root Node");

	/** The fps text. */
	protected BitmapText fpsText;

	/** The gui font. */
	protected BitmapFont guiFont;

	/** The multiplicity root node. */
	protected Node multiplicityRootNode = new Node("Multiplicity Root Node");

	/** The second counter. */
	protected float secondCounter = 0.0f;

	/** The stats view. */
	protected StatsView statsView;

	/**
	 * Instantiates a new JME app root.
	 */
	public JMEAppRoot() {
		super();
	}

	/**
	 * Gets the gui node.
	 *
	 * @return the gui node
	 */
	public Node getGuiNode() {
		return multiplicityRootNode;
	}

	/**
	 * Gets the root node.
	 *
	 * @return the root node
	 */
	public Node getRootNode() {
		return rootNode;
	}

	/*
	 * (non-Javadoc)
	 * @see com.jme3.app.Application#initialize()
	 */
	@Override
	public void initialize() {
		super.initialize();

		multiplicityRootNode.setQueueBucket(Bucket.Gui);
		multiplicityRootNode.setCullHint(CullHint.Never);
		loadFPSText();
		loadStatsView();
		viewPort.attachScene(rootNode);
		guiViewPort.attachScene(multiplicityRootNode);

		if (inputManager != null) {
			if (context.getType() == Type.Display) {
				inputManager.addMapping("SIMPLEAPP_Exit", new KeyTrigger(
						KeyInput.KEY_ESCAPE));
			}

			inputManager.addMapping("SIMPLEAPP_CameraPos", new KeyTrigger(
					KeyInput.KEY_C));
			inputManager.addMapping("SIMPLEAPP_Memory", new KeyTrigger(
					KeyInput.KEY_M));
			inputManager.addListener(actionListener, "SIMPLEAPP_Exit",
					"SIMPLEAPP_CameraPos", "SIMPLEAPP_Memory");
		}

		// call user code
		simpleInitApp();
	}

	/**
	 * Load fps text.
	 */
	public void loadFPSText() {
		guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
		fpsText = new BitmapText(guiFont, false);
		fpsText.setLocalTranslation(0, fpsText.getLineHeight(), 0);
		fpsText.setText("Frames per second");
		multiplicityRootNode.attachChild(fpsText);
	}

	/**
	 * Load stats view.
	 */
	public void loadStatsView() {
		statsView = new StatsView("Statistics View", assetManager,
				renderer.getStatistics());
		// move it up so it appears above fps text
		statsView.setLocalTranslation(0, fpsText.getLineHeight(), 0);
		multiplicityRootNode.attachChild(statsView);
	}

	/**
	 * Simple init app.
	 */
	public abstract void simpleInitApp();

	/**
	 * Simple render.
	 *
	 * @param rm the rm
	 */
	public void simpleRender(RenderManager rm) {
	}

	/**
	 * Simple update.
	 *
	 * @param tpf the tpf
	 */
	public void simpleUpdate(float tpf) {
	}

	/*
	 * (non-Javadoc)
	 * @see com.jme3.app.Application#start()
	 */
	@Override
	public void start() {
		DisplayPrefsItem dprefs = new DisplayPrefsItem();
		AppSettings settings = new AppSettings(true);
		settings.setTitle("Multiplicity v3.0");
		settings.setBitsPerPixel(dprefs.getBitsPerPixel());
		settings.setWidth(dprefs.getWidth());
		settings.setHeight(dprefs.getHeight());
		settings.setFullscreen(dprefs.getFullScreen());
		settings.setVSync(true);
		settings.setSamples(dprefs.getMinimumAntiAliasSamples());
		setSettings(settings);

		super.start();
	}

	/*
	 * (non-Javadoc)
	 * @see com.jme3.app.Application#update()
	 */
	@Override
	public void update() {
		if ((speed == 0) || paused) {
			return;
		}

		super.update();
		float tpf = timer.getTimePerFrame() * speed;

		secondCounter += timer.getTimePerFrame();
		int fps = (int) timer.getFrameRate();
		if (secondCounter >= 1.0f) {
			fpsText.setText("Frames per second: " + fps);
			secondCounter = 0.0f;
		}

		// update states
		stateManager.update(tpf);

		// simple update and root node
		simpleUpdate(tpf);
		rootNode.updateLogicalState(tpf);
		multiplicityRootNode.updateLogicalState(tpf);
		rootNode.updateGeometricState();

		try {
			multiplicityRootNode.updateGeometricState();

			// render states
			stateManager.render(renderManager);
			renderManager.render(tpf);
		} catch (Exception e) {
			// Not a perfect solution but does make apps more stable
		}
		simpleRender(renderManager);
		stateManager.postRender();

	}

}
