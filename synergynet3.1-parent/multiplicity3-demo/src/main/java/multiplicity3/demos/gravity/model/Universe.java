package multiplicity3.demos.gravity.model;

import java.util.ArrayList;
import java.util.List;

import com.jme3.math.Vector2f;

import multiplicity3.csys.animation.elements.AnimationElement;

public class Universe extends AnimationElement {
	
	private int maxBodies = 20;
	private double gravitationalConstant = 1e5;
	private double timeMultiplier = 0.001;
	


	private static final float MAX_DIST_FROM_UNIVERSE_CENTER = 1200;	
	
	public float minimum_distance_apart = 1f;	
	
	private List<Body> bodies;
	private UniverseChangeDelegate delegate;
	
	public Universe(UniverseChangeDelegate delegate) {
		bodies = new ArrayList<Body>();
		this.delegate = delegate;
	}
	
	public void addBody(Body b) {
		if(bodies.contains(b)) return;
		
		this.bodies.add(b);
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
			if(a.getPosition().length() > MAX_DIST_FROM_UNIVERSE_CENTER) {
				toRemove.add(a);
			}
		}

		bodies.removeAll(toRemove);
		
		for(Body b : toRemove) {
			delegate.bodyRemoved(b);
		}
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
			oldVelocity.set(a.velocity.x, a.velocity.y);			

			double accel_x = 0;
			double accel_y = 0;
			double dist_x = 0;
			double dist_y = 0;
			for(int j = 0; j < bodies.size(); j++) {				
				if(i != j) {
					b = bodies.get(j);	
					dist_x = b.getPosition().x - a.getPosition().x;
					dist_y = b.getPosition().y - a.getPosition().y;					
					double r2 = b.getPosition().subtract(a.getPosition()).length();
					r2 = r2 * r2;					
					double force = getGravitationalConstant() * b.mass.getMass() / r2;
					accel_x += dist_x * force;
					accel_y += dist_y * force;
				}				
			}
			
			accel_x /= bodies.size() - 1;
			accel_y /= bodies.size() - 1;
			
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
}
