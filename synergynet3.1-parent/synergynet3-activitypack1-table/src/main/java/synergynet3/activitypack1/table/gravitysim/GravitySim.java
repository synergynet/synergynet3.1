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

public class GravitySim implements IMultiplicityApp, IMultiTouchEventListener, UniverseChangeDelegate {
	private static final Logger log = Logger.getLogger(GravitySim.class.getName());
	
	private static final String RESOURCE_PATH = "synergynet3/activitypack1/table/gravitysim/";
	
	private IStage stage;
	private IContentFactory contentFactory;
	private Universe universe;
	private UniverseSync universeSync;
	
	private Map<Long,Cursor> cursorMap = new HashMap<Long,Cursor>();
	private Map<Long,Body> cursorIdToBodyMap = new HashMap<Long,Body>();
	private Map<Long,ILine> lines = new HashMap<Long,ILine>();

	private MultiTouchInputComponent input;
	
	
	public static void main(String[] args) {
		MultiplicityClient client = MultiplicityClient.get();
		client.start();
		GravitySim app = new GravitySim();
		client.setCurrentApp(app);
	}

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
			IImage bg = contentFactory.create(IImage.class, "bg", UUID.randomUUID());
			bg.setImage(RESOURCE_PATH + "starfield.png");
			bg.setSize(1024,768);
			stage.addItem(bg);
			stage.getZOrderManager().ignoreItemClickedBehaviour(bg);
			
			universe.setMaxBodies(1000);
			universe.setScenario(UniverseScenario.BINARY_STAR_SYSTEM);
			
		} catch (ContentTypeNotBoundException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void shouldStop() {
		stage.getAnimationSystem().remove(universe);
		this.input.unregisterMultiTouchEventListener(this);
	}

	@Override
	public void cursorPressed(MultiTouchCursorEvent event) {
		if(!universe.canAddMore()) return;
		try {			
			Body b = new Body("moon", universe.getDefaultBodyMass(), new Vector2f(0,0), new Vector2f(0,0));
			universe.addBody(b);
			
			Vector2f worldPositionOfCursor = new Vector2f();
			stage.tableToScreen(event.getPosition(), worldPositionOfCursor);
			b.setPosition(worldPositionOfCursor);
			bodyPositionChanged(b);			
			
			Cursor c = new Cursor(event.getCursorID(), worldPositionOfCursor);
			cursorMap.put(event.getCursorID(), c);
			cursorIdToBodyMap.put(event.getCursorID(), b);
			ILine line = contentFactory.create(ILine.class, "line", UUID.randomUUID());
			stage.addItem(line);
			line.setStartPosition(worldPositionOfCursor);
			line.setEndPosition(worldPositionOfCursor.add(new Vector2f(0.1f, 0.1f)));
			
			lines.put(event.getCursorID(), line);
			
		} catch (ContentTypeNotBoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void cursorReleased(MultiTouchCursorEvent event) {
		Vector2f screenPos = new Vector2f();
		stage.tableToScreen(event.getPosition(), screenPos);			
		Cursor c = cursorMap.get(event.getCursorID());
		if(c == null) return;
		c.setEndPosition(screenPos);
		Vector2f velocity = c.startpos.subtract(c.endpos);		
		velocity.multLocal(5e3f);
		ILine line = lines.get(event.getCursorID());
		if(line != null) {
			stage.removeItem(line);
		}
		Body b = cursorIdToBodyMap.get(event.getCursorID());
		b.setVelocity(velocity);
		if(b != null) b.setActive();
	}
	
	@Override
	public void cursorChanged(MultiTouchCursorEvent event) {
		Vector2f worldPositionOfCursor = new Vector2f();
		stage.tableToWorld(event.getPosition(), worldPositionOfCursor);
		
		Cursor c = cursorMap.get(event.getCursorID());
		if(c == null) return;
		
		c.setCurrentPosition(worldPositionOfCursor);		
		Body b = cursorIdToBodyMap.get(event.getCursorID());
		
		if(b != null) {
			b.setPosition(worldPositionOfCursor);
			this.bodyPositionChanged(b);
		}

		ILine line = lines.get(event.getCursorID());
		if(line != null) {
			line.setEndPosition(worldPositionOfCursor);
		}
	}

	@Override
	public void cursorClicked(MultiTouchCursorEvent event) {}



	@Override
	public void objectAdded(MultiTouchObjectEvent event) {}

	@Override
	public void objectRemoved(MultiTouchObjectEvent event) {}

	@Override
	public void objectChanged(MultiTouchObjectEvent event) {}

	@Override
	public String getFriendlyAppName() {
		return "Gravity";
	}

	@Override
	public void bodyRemoved(Body b) {
		log.fine(b + " removed");
		IItem item = representationsForBodyById.get(b.getId());
		if(item == null) return;
		stage.removeItem(item);
		representationsForBodyById.remove(b.getId());
	}

	@Override
	public void bodyAdded(Body b) {
		log.fine("Sorting out a representation for " + b.getName());
		IItem item;
		try {
			item = createRepresentationForBody(b);
			if(item == null) return;
			representationsForBodyById.put(b.getId(), item);
			stage.addItem(item);
			stage.getZOrderManager().bringToTop(item);
		} catch (ContentTypeNotBoundException e) {
			log.log(Level.WARNING, "Problem creating body representation", e);
		}
		
	}

	private IItem createRepresentationForBody(Body b) throws ContentTypeNotBoundException {
		if("sun".equals(b.getName())) {
			IImage sun = contentFactory.create(IImage.class, "sun", UUID.randomUUID());
			sun.setImage(RESOURCE_PATH + "sun_128.png");
			sun.setSize(32,32);
			return sun;
		}else if("moon".equals(b.getName())) {
			IImage moon = contentFactory.create(IImage.class, "moon", UUID.randomUUID());
			moon.setImage(RESOURCE_PATH + "moon_64.png");
			moon.setSize(8,8);
			return moon;
		}
		return null;
	}

	@Override
	public void bodyPositionChanged(Body body) {
		IItem rep = getRepresentation(body);
		if(rep == null) return;
		rep.setWorldLocation(body.getPosition());
	}
	
	private Map<Integer,IItem> representationsForBodyById = new HashMap<Integer,IItem>();

	private IItem getRepresentation(Body body) {
		return representationsForBodyById.get(body.getId());
	}

	@Override
	public void onDestroy() {
		if (universeSync != null)universeSync.stop();
		SynergyNetCluster.get().shutdown();
	}


}
