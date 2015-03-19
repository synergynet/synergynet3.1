package synergynet3.activitypack1.table.gravitysim.model;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import synergynet3.activitypack1.web.shared.UniverseScenario;

import com.jme3.math.Vector2f;

import multiplicity3.csys.animation.elements.AnimationElement;

public class Universe extends AnimationElement {
	private static final Logger log = Logger.getLogger(Universe.class.getName());

	private int maxBodies = 20;
	private double gravitationalConstant = 1e5;
	private double timeMultiplier = 0.001;


	public static final double MASS_SUN = 1e6;
	public static final double MASS_EARTH = 1;

	private static final float MAX_DIST_FROM_UNIVERSE_CENTER = 1200;	

	public float minimum_distance_apart = 1f;	

	private List<Body> bodies;
	private UniverseChangeDelegate delegate;

	private MassReference defaultBodyMass = new MassReference(1);;

	public Universe(UniverseChangeDelegate delegate) {
		bodies = new ArrayList<Body>();
		this.delegate = delegate;
	}

	public void addBody(Body b) {
		if(bodies.contains(b)) {
			log.fine("body already there...");
			return;
		}

		this.bodies.add(b);
		log.fine("telling delegate " + delegate);
		delegate.bodyAdded(b);
	}

	@Override
	public void updateAnimationState(float tpf) {
		if(bodies.size() < 2) return;

		updateUniverse(tpf);

	}

	private void updateUniverse(float tpf) {
		double universeTime = tpf * timeMultiplier;		
		updateAllBodyVelocities(universeTime);		
		updateAllBodyPositions(universeTime);		
		removeFarAwayBodies();
	}

	private void removeFarAwayBodies() {
		List<Body> toRemove = new ArrayList<Body>();
		Body a;
		for(int i = 0; i < bodies.size(); i++) {
			a = bodies.get(i);
			if(a.getPosition().subtract(512,768/2).length() > MAX_DIST_FROM_UNIVERSE_CENTER) {
				toRemove.add(a);
			}
		}

		removeBodies(toRemove);
	}

	public void removeAllBodies() {
		List<Body> removeList = new ArrayList<Body>();
		removeList.addAll(bodies);
		bodies.clear();
		for(Body b : removeList) {
			delegate.bodyRemoved(b);
		}
	}

	private void updateAllBodyPositions(double universeTime) {
		Body body;
		for(int i = 0; i < bodies.size(); i++) {
			body = bodies.get(i);
			if(body.shouldIgnore()) continue;
			body.setPosition(
					body.getPosition().x + (float)(body.velocity.x * universeTime),
					body.getPosition().y + (float)(body.velocity.y * universeTime));
			delegate.bodyPositionChanged(body);
		}
	}

	private void updateAllBodyVelocities(double universeTime) {
		Body a, b;
		Vector2f oldVelocity = new Vector2f();
		for(int i = 0; i < bodies.size(); i++) {			
			a = bodies.get(i);
			if(a.shouldIgnore()) continue;
			oldVelocity.set(a.velocity.x, a.velocity.y);			

			double accel_x = 0;
			double accel_y = 0;
			double dist_x = 0;
			double dist_y = 0;
			for(int j = 0; j < bodies.size(); j++) {				
				if(i != j) {					
					b = bodies.get(j);
					if(b.shouldIgnore()) continue;
					dist_x = b.getPosition().x - a.getPosition().x;
					dist_y = b.getPosition().y - a.getPosition().y;					
					double r2 = b.getPosition().subtract(a.getPosition()).length();
					r2 = r2 * r2;
					if(r2 < 0.00000001) r2 = 0.00000001;
					double force = getGravitationalConstant() * b.mass.getMass() / r2;
					accel_x += dist_x * force;
					accel_y += dist_y * force;
				}				
			}

			//accel_x /= bodies.size() - 1;
			//accel_y /= bodies.size() - 1;

			a.velocity.addLocal((float) (universeTime * accel_x), (float) (universeTime * accel_y));

		}
	}

	@Override
	public void reset() {}

	@Override
	public void elementStart(float tpf) {}

	@Override
	public boolean isFinished() {
		return false;
	}

	public boolean canAddMore() {
		return bodies.size() < getMaxBodies();
	}

	public void setMaxBodies(int maxBodies) {
		this.maxBodies = maxBodies;
		List<Body> toRemove = new ArrayList<Body>();
		if(bodies.size() > maxBodies) {
			for(int i = maxBodies; i < bodies.size(); i++) {
				toRemove.add(bodies.get(i));
			}
		}
		removeBodies(toRemove);
	}

	private void removeBodies(List<Body> toRemove) {
		bodies.removeAll(toRemove);

		for(Body b : toRemove) {
			delegate.bodyRemoved(b);
		}
	}

	public int getMaxBodies() {
		return maxBodies;
	}

	public void setGravitationalConstant(double gravitationalConstant) {
		this.gravitationalConstant = gravitationalConstant;
	}

	public double getGravitationalConstant() {
		return gravitationalConstant;
	}

	public double getTimeMultiplier() {
		return timeMultiplier;
	}

	public void setTimeMultiplier(double timeMultiplier) {
		this.timeMultiplier = timeMultiplier;
	}

	public void setScenario(UniverseScenario newValue) {
		log.info("loading scenario " + newValue);
		switch(newValue) {
		case SUN_AND_MOONS: {
			log.info("Loading scenario for sun and moons");
			removeAllBodies();
			gravitationalConstant = 1e5;
			timeMultiplier = 0.001;
			Body sun = new Body("sun", new MassReference(MASS_SUN), new Vector2f(512, 768/2), new Vector2f(0,0));
			log.fine("adding sun");
			addBody(sun);
			log.fine("updating position via delegate");
			delegate.bodyPositionChanged(sun);
			sun.setActive();
			defaultBodyMass = new MassReference(1);
			break;
		}
		case MOONS_ONLY: {
			log.info("Loading scenario for moons only");
			gravitationalConstant = 1e11;
			timeMultiplier = 0.0001;
			defaultBodyMass = new MassReference(1);
			removeAllBodies();
			break;
		}
		
		case BINARY_STAR_SYSTEM: {
			log.info("Loading binary star system.");
			Vector2f middle = new Vector2f(512, 768/2);
			removeAllBodies();
			gravitationalConstant = 1e5;
			timeMultiplier = 0.0002;
//			timeMultiplier = 0.0006;
			
			int distDiff = 100;
			int vel = 50;
			
			Vector2f vel1 = new Vector2f(vel,0);
			vel1.multLocal(5e3f);
			Body sun = new Body("sun", new MassReference(MASS_SUN), middle.add(new Vector2f(0,distDiff)), vel1);
			log.fine("adding sun");
			addBody(sun);
			log.fine("updating position via delegate");
			delegate.bodyPositionChanged(sun);
			sun.setActive();

			Vector2f vel2 = new Vector2f(-vel,0);
			vel2.multLocal(5e3f);			
			Body sun2 = new Body("sun", new MassReference(MASS_SUN), middle.add(new Vector2f(0,-distDiff)), vel2);
			log.fine("adding sun");
			addBody(sun2);
			log.fine("updating position via delegate");
			delegate.bodyPositionChanged(sun2);
			sun2.setActive();
			
			defaultBodyMass  = new MassReference(1);
			
			break;
		}
		
		}
	}

	public MassReference getDefaultBodyMass() {
		return defaultBodyMass;
	}
}
