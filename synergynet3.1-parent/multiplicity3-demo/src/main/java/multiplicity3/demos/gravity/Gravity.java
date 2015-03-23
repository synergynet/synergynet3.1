package multiplicity3.demos.gravity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

import multiplicity3.appsystem.IMultiplicityApp;
import multiplicity3.appsystem.IQueueOwner;
import multiplicity3.appsystem.MultiplicityClient;
import multiplicity3.csys.MultiplicityEnvironment;
import multiplicity3.csys.factory.ContentTypeNotBoundException;
import multiplicity3.csys.factory.IContentFactory;
import multiplicity3.csys.items.image.IImage;
import multiplicity3.csys.items.item.IItem;
import multiplicity3.csys.items.line.ILine;
import multiplicity3.csys.stage.IStage;
import multiplicity3.demos.gravity.model.Body;
import multiplicity3.demos.gravity.model.Cursor;
import multiplicity3.demos.gravity.model.MassReference;
import multiplicity3.demos.gravity.model.Universe;
import multiplicity3.demos.gravity.model.UniverseChangeDelegate;
import multiplicity3.input.IMultiTouchEventListener;
import multiplicity3.input.MultiTouchInputComponent;
import multiplicity3.input.events.MultiTouchCursorEvent;
import multiplicity3.input.events.MultiTouchObjectEvent;

import com.jme3.math.Vector2f;

/**
 * The Class Gravity.
 */
public class Gravity implements IMultiplicityApp, IMultiTouchEventListener,
		UniverseChangeDelegate {

	/** The Constant MASS_EARTH. */
	public static final double MASS_EARTH = 1;

	/** The Constant MASS_SUN. */
	public static final double MASS_SUN = 1e6;

	/** The Constant log. */
	private static final Logger log = Logger.getLogger(Gravity.class.getName());

	/** The content factory. */
	private IContentFactory contentFactory;

	/** The cursor map. */
	private Map<Long, Cursor> cursorMap = new HashMap<Long, Cursor>();

	/** The earth mass. */
	private MassReference earthMass = new MassReference(MASS_EARTH);

	/** The input. */
	private MultiTouchInputComponent input;

	/** The item map. */
	private Map<Long, IItem> itemMap = new HashMap<Long, IItem>();

	/** The lines. */
	private Map<Long, ILine> lines = new HashMap<Long, ILine>();

	/** The stage. */
	private IStage stage;

	/** The sun mass. */
	private MassReference sunMass = new MassReference(MASS_SUN);

	/** The universe. */
	private Universe universe;

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		MultiplicityClient client = MultiplicityClient.get();
		client.start();
		Gravity app = new Gravity();
		client.setCurrentApp(app);
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.demos.gravity.model.UniverseChangeDelegate#bodyAdded(
	 * multiplicity3.demos.gravity.model.Body)
	 */
	@Override
	public void bodyAdded(Body b) {
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.demos.gravity.model.UniverseChangeDelegate#bodyPositionChanged
	 * (multiplicity3.demos.gravity.model.Body)
	 */
	@Override
	public void bodyPositionChanged(Body body) {
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.demos.gravity.model.UniverseChangeDelegate#bodyRemoved(
	 * multiplicity3.demos.gravity.model.Body)
	 */
	@Override
	public void bodyRemoved(Body b) {
		log.fine(b + " removed");
		stage.removeItem(b.getRepresentation());
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.input.IMultiTouchEventListener#cursorChanged(multiplicity3
	 * .input.events.MultiTouchCursorEvent)
	 */
	@Override
	public void cursorChanged(MultiTouchCursorEvent event) {
		Vector2f worldPositionOfCursor = new Vector2f();
		stage.tableToWorld(event.getPosition(), worldPositionOfCursor);

		Cursor c = cursorMap.get(event.getCursorID());
		if (c == null) {
			return;
		}

		c.setCurrentPosition(worldPositionOfCursor);
		IItem item = itemMap.get(event.getCursorID());
		if (item != null) {
			item.setWorldLocation(worldPositionOfCursor);
		}
		ILine line = lines.get(event.getCursorID());
		if (line != null) {
			line.setEndPosition(worldPositionOfCursor);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.input.IMultiTouchEventListener#cursorClicked(multiplicity3
	 * .input.events.MultiTouchCursorEvent)
	 */
	@Override
	public void cursorClicked(MultiTouchCursorEvent event) {
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.input.IMultiTouchEventListener#cursorPressed(multiplicity3
	 * .input.events.MultiTouchCursorEvent)
	 */
	@Override
	public void cursorPressed(MultiTouchCursorEvent event) {
		if (!universe.canAddMore()) {
			return;
		}
		try {
			IImage moon = contentFactory.create(IImage.class, "moon",
					UUID.randomUUID());
			moon.setImage("multiplicity3/demos/gravity/moon_64.png");
			moon.setSize(8, 8);
			stage.addItem(moon);
			stage.getZOrderManager().bringToTop(moon);

			Vector2f worldPositionOfCursor = new Vector2f();
			stage.tableToScreen(event.getPosition(), worldPositionOfCursor);
			moon.setWorldLocation(worldPositionOfCursor);

			Cursor c = new Cursor(event.getCursorID(), worldPositionOfCursor);
			cursorMap.put(event.getCursorID(), c);
			itemMap.put(event.getCursorID(), moon);
			ILine line = contentFactory.create(ILine.class, "line",
					UUID.randomUUID());
			stage.addItem(line);
			line.setStartPosition(worldPositionOfCursor);
			line.setEndPosition(worldPositionOfCursor.add(new Vector2f(0.1f,
					0.1f)));

			lines.put(event.getCursorID(), line);

		} catch (ContentTypeNotBoundException e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.input.IMultiTouchEventListener#cursorReleased(multiplicity3
	 * .input.events.MultiTouchCursorEvent)
	 */
	@Override
	public void cursorReleased(MultiTouchCursorEvent event) {
		Vector2f screenPos = new Vector2f();
		stage.tableToScreen(event.getPosition(), screenPos);
		Cursor c = cursorMap.get(event.getCursorID());
		if (c == null) {
			return;
		}
		c.setEndPosition(screenPos);
		Vector2f velocity = c.startpos.subtract(c.endpos);
		velocity.multLocal(5e3f);
		IItem representation = itemMap.get(event.getCursorID());
		if (representation != null) {
			Body b = new Body("earth", representation, earthMass, screenPos,
					velocity);
			universe.addBody(b);
		}
		ILine line = lines.get(event.getCursorID());
		if (line != null) {
			stage.removeItem(line);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.appsystem.IMultiplicityApp#getFriendlyAppName()
	 */
	@Override
	public String getFriendlyAppName() {
		return "Gravity";
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.input.IMultiTouchEventListener#objectAdded(multiplicity3
	 * .input.events.MultiTouchObjectEvent)
	 */
	@Override
	public void objectAdded(MultiTouchObjectEvent event) {
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.input.IMultiTouchEventListener#objectChanged(multiplicity3
	 * .input.events.MultiTouchObjectEvent)
	 */
	@Override
	public void objectChanged(MultiTouchObjectEvent event) {
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.input.IMultiTouchEventListener#objectRemoved(multiplicity3
	 * .input.events.MultiTouchObjectEvent)
	 */
	@Override
	public void objectRemoved(MultiTouchObjectEvent event) {
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.appsystem.IMultiplicityApp#onDestroy()
	 */
	@Override
	public void onDestroy() {
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.appsystem.IMultiplicityApp#shouldStart(multiplicity3.input
	 * .MultiTouchInputComponent, multiplicity3.appsystem.IQueueOwner)
	 */
	@Override
	public void shouldStart(MultiTouchInputComponent input, IQueueOwner iqo) {
		log.info("init");
		universe = new Universe(this);
		this.input = input;
		input.registerMultiTouchEventListener(this);
		this.stage = MultiplicityEnvironment.get().getLocalStages().get(0);
		this.stage.getZOrderManager().setAutoBringToTop(false);
		this.contentFactory = stage.getContentFactory();
		stage.getAnimationSystem().add(universe);

		try {
			IImage bg = contentFactory.create(IImage.class, "bg",
					UUID.randomUUID());
			bg.setImage("multiplicity3/demos/gravity/starfield.png");
			bg.setSize(1024, 768);
			stage.addItem(bg);
			stage.getZOrderManager().ignoreItemClickedBehaviour(bg);

			IImage sun = contentFactory.create(IImage.class, "sun",
					UUID.randomUUID());
			sun.setImage("multiplicity3/demos/gravity/sun_128.png");
			sun.setSize(32, 32);
			stage.addItem(sun);
			stage.getZOrderManager().bringToTop(sun);

			universe.addBody(new Body("sun", sun, sunMass, new Vector2f(0, 0),
					new Vector2f(0, 0)));

			universe.setMaxBodies(1000);

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
		stage.getAnimationSystem().remove(universe);
		this.input.unregisterMultiTouchEventListener(this);
	}

}
