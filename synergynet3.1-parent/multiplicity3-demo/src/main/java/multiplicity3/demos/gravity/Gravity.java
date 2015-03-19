package multiplicity3.demos.gravity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

import com.jme3.math.Vector2f;

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

public class Gravity implements IMultiplicityApp, IMultiTouchEventListener, UniverseChangeDelegate {
	private static final Logger log = Logger.getLogger(Gravity.class.getName());
	
	public static final double MASS_SUN = 1e6;
	public static final double MASS_EARTH = 1;
	
	private IStage stage;
	private IContentFactory contentFactory;
	private Universe universe;
	
	private Map<Long,Cursor> cursorMap = new HashMap<Long,Cursor>();
	private Map<Long,IItem> itemMap = new HashMap<Long,IItem>();
	private Map<Long,ILine> lines = new HashMap<Long,ILine>();

	private MultiTouchInputComponent input;
	
	private MassReference sunMass = new MassReference(MASS_SUN);
	private MassReference earthMass = new MassReference(MASS_EARTH);
	
	
	public static void main(String[] args) {
		MultiplicityClient client = MultiplicityClient.get();
		client.start();
		Gravity app = new Gravity();
		client.setCurrentApp(app);
	}

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
			IImage bg = contentFactory.create(IImage.class, "bg", UUID.randomUUID());
			bg.setImage("multiplicity3/demos/gravity/starfield.png");
			bg.setSize(1024,768);
			stage.addItem(bg);
			stage.getZOrderManager().ignoreItemClickedBehaviour(bg);
			
			IImage sun = contentFactory.create(IImage.class, "sun", UUID.randomUUID());
			sun.setImage("multiplicity3/demos/gravity/sun_128.png");
			sun.setSize(32,32);
			stage.addItem(sun);
			stage.getZOrderManager().bringToTop(sun);
			
			universe.addBody(new Body("sun", sun, sunMass, new Vector2f(0, 0), new Vector2f(0,0)));
			
			universe.setMaxBodies(1000);
			
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
	public void onDestroy() {}


	@Override
	public void cursorPressed(MultiTouchCursorEvent event) {
		if(!universe.canAddMore()) return;
		try {
			IImage moon = contentFactory.create(IImage.class, "moon", UUID.randomUUID());
			moon.setImage("multiplicity3/demos/gravity/moon_64.png");
			moon.setSize(8,8);
			stage.addItem(moon);
			stage.getZOrderManager().bringToTop(moon);
			
			Vector2f worldPositionOfCursor = new Vector2f();
			stage.tableToScreen(event.getPosition(), worldPositionOfCursor);
			moon.setWorldLocation(worldPositionOfCursor);			
			
			Cursor c = new Cursor(event.getCursorID(), worldPositionOfCursor);
			cursorMap.put(event.getCursorID(), c);
			itemMap.put(event.getCursorID(), moon);
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
		IItem representation = itemMap.get(event.getCursorID());
		if(representation != null) {
			Body b = new Body("earth", representation, earthMass, screenPos, velocity);
			universe.addBody(b);
		}
		ILine line = lines.get(event.getCursorID());
		if(line != null) {
			stage.removeItem(line);
		}
	}
	
	@Override
	public void cursorChanged(MultiTouchCursorEvent event) {
		Vector2f worldPositionOfCursor = new Vector2f();
		stage.tableToWorld(event.getPosition(), worldPositionOfCursor);
		
		Cursor c = cursorMap.get(event.getCursorID());
		if(c == null) return;
		
		c.setCurrentPosition(worldPositionOfCursor);		
		IItem item = itemMap.get(event.getCursorID());
		if(item != null) {			
			item.setWorldLocation(worldPositionOfCursor);
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
		stage.removeItem(b.getRepresentation());
	}

	@Override
	public void bodyAdded(Body b) {}

	@Override
	public void bodyPositionChanged(Body body) {}


}
