package synergynet3.activitypack1.table.gravitysim;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
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
import multiplicity3.input.IMultiTouchEventListener;
import multiplicity3.input.MultiTouchInputComponent;
import multiplicity3.input.events.MultiTouchCursorEvent;
import multiplicity3.input.events.MultiTouchObjectEvent;
import synergynet3.activitypack1.table.gravitysim.model.Body;
import synergynet3.activitypack1.table.gravitysim.model.Cursor;
import synergynet3.activitypack1.table.gravitysim.model.Universe;
import synergynet3.activitypack1.table.gravitysim.model.UniverseChangeDelegate;
import synergynet3.activitypack1.table.gravitysim.network.UniverseSync;
import synergynet3.activitypack1.web.shared.UniverseScenario;
import synergynet3.cluster.SynergyNetCluster;

import com.jme3.math.Vector2f;

/**
 * The Class GravitySim.
 */
public class GravitySim implements IMultiplicityApp, IMultiTouchEventListener,
		UniverseChangeDelegate {

	/** The Constant log. */
	private static final Logger log = Logger.getLogger(GravitySim.class
			.getName());

	/** The Constant RESOURCE_PATH. */
	private static final String RESOURCE_PATH = "synergynet3/activitypack1/table/gravitysim/";

	/** The content factory. */
	private IContentFactory contentFactory;

	/** The cursor id to body map. */
	private Map<Long, Body> cursorIdToBodyMap = new HashMap<Long, Body>();

	/** The cursor map. */
	private Map<Long, Cursor> cursorMap = new HashMap<Long, Cursor>();

	/** The input. */
	private MultiTouchInputComponent input;

	/** The lines. */
	private Map<Long, ILine> lines = new HashMap<Long, ILine>();

	/** The representations for body by id. */
	private Map<Integer, IItem> representationsForBodyById = new HashMap<Integer, IItem>();

	/** The stage. */
	private IStage stage;

	/** The universe. */
	private Universe universe;

	/** The universe sync. */
	private UniverseSync universeSync;

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		MultiplicityClient client = MultiplicityClient.get();
		client.start();
		GravitySim app = new GravitySim();
		client.setCurrentApp(app);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynet3.activitypack1.table.gravitysim.model.UniverseChangeDelegate
	 * #bodyAdded(synergynet3.activitypack1.table.gravitysim.model.Body)
	 */
	@Override
	public void bodyAdded(Body b) {
		log.fine("Sorting out a representation for " + b.getName());
		IItem item;
		try {
			item = createRepresentationForBody(b);
			if (item == null) {
				return;
			}
			representationsForBodyById.put(b.getId(), item);
			stage.addItem(item);
			stage.getZOrderManager().bringToTop(item);
		} catch (ContentTypeNotBoundException e) {
			log.log(Level.WARNING, "Problem creating body representation", e);
		}

	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynet3.activitypack1.table.gravitysim.model.UniverseChangeDelegate
	 * #bodyPositionChanged
	 * (synergynet3.activitypack1.table.gravitysim.model.Body)
	 */
	@Override
	public void bodyPositionChanged(Body body) {
		IItem rep = getRepresentation(body);
		if (rep == null) {
			return;
		}
		rep.setWorldLocation(body.getPosition());
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynet3.activitypack1.table.gravitysim.model.UniverseChangeDelegate
	 * #bodyRemoved(synergynet3.activitypack1.table.gravitysim.model.Body)
	 */
	@Override
	public void bodyRemoved(Body b) {
		log.fine(b + " removed");
		IItem item = representationsForBodyById.get(b.getId());
		if (item == null) {
			return;
		}
		stage.removeItem(item);
		representationsForBodyById.remove(b.getId());
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
		Body b = cursorIdToBodyMap.get(event.getCursorID());

		if (b != null) {
			b.setPosition(worldPositionOfCursor);
			this.bodyPositionChanged(b);
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
			Body b = new Body("moon", universe.getDefaultBodyMass(),
					new Vector2f(0, 0), new Vector2f(0, 0));
			universe.addBody(b);

			Vector2f worldPositionOfCursor = new Vector2f();
			stage.tableToScreen(event.getPosition(), worldPositionOfCursor);
			b.setPosition(worldPositionOfCursor);
			bodyPositionChanged(b);

			Cursor c = new Cursor(event.getCursorID(), worldPositionOfCursor);
			cursorMap.put(event.getCursorID(), c);
			cursorIdToBodyMap.put(event.getCursorID(), b);
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
		ILine line = lines.get(event.getCursorID());
		if (line != null) {
			stage.removeItem(line);
		}
		Body b = cursorIdToBodyMap.get(event.getCursorID());
		b.setVelocity(velocity);
		if (b != null) {
			b.setActive();
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
		if (universeSync != null) {
			universeSync.stop();
		}
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
		log.info("init");
		this.input = input;
		input.registerMultiTouchEventListener(this);
		this.stage = MultiplicityEnvironment.get().getLocalStages().get(0);
		this.stage.getZOrderManager().setAutoBringToTop(false);
		this.contentFactory = stage.getContentFactory();
		universe = new Universe(this);
		universeSync = new UniverseSync(universe);
		stage.getAnimationSystem().add(universe);

		try {
			IImage bg = contentFactory.create(IImage.class, "bg",
					UUID.randomUUID());
			bg.setImage(RESOURCE_PATH + "starfield.png");
			bg.setSize(1024, 768);
			stage.addItem(bg);
			stage.getZOrderManager().ignoreItemClickedBehaviour(bg);

			universe.setMaxBodies(1000);
			universe.setScenario(UniverseScenario.BINARY_STAR_SYSTEM);

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

	/**
	 * Creates the representation for body.
	 *
	 * @param b the b
	 * @return the i item
	 * @throws ContentTypeNotBoundException the content type not bound exception
	 */
	private IItem createRepresentationForBody(Body b)
			throws ContentTypeNotBoundException {
		if ("sun".equals(b.getName())) {
			IImage sun = contentFactory.create(IImage.class, "sun",
					UUID.randomUUID());
			sun.setImage(RESOURCE_PATH + "sun_128.png");
			sun.setSize(32, 32);
			return sun;
		} else if ("moon".equals(b.getName())) {
			IImage moon = contentFactory.create(IImage.class, "moon",
					UUID.randomUUID());
			moon.setImage(RESOURCE_PATH + "moon_64.png");
			moon.setSize(8, 8);
			return moon;
		}
		return null;
	}

	/**
	 * Gets the representation.
	 *
	 * @param body the body
	 * @return the representation
	 */
	private IItem getRepresentation(Body body) {
		return representationsForBodyById.get(body.getId());
	}

}
