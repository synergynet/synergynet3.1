package multiplicity3.csys.animation.elements.behaviourelements;

import com.jme3.math.Vector2f;

import multiplicity3.csys.animation.elements.AnimationElement;
import multiplicity3.csys.items.item.IItem;
import multiplicity3.csys.stage.IStage;

public class InertiaAnimationElement extends AnimationElement {
	
	private IItem item;
	private boolean finished;
	private Vector2f currentVelocity;
	private float deceleration = 100f;
	private IStage stage;

	public InertiaAnimationElement(IItem item, IStage stage) {
		this.item = item;
		this.stage = stage;
	}

	/*
	 * Value > 1 for less than default drag, value < 1 for more than default drag.
	 */
	public void setDeceleration(float deceleration) {
		this.deceleration = deceleration;
	}
	
	public float getDecelerationFactor() {
		return this.deceleration;
	}

	@Override
	public void elementStart(float tpf) {}

	@Override
	public boolean isFinished() {
		return finished;
	}

	@Override
	public void reset() {
		finished = true;	
		currentVelocity = new Vector2f();
	}

	@Override
	public void updateAnimationState(float tpf) {
		if (!finished){
			item.setWorldLocation(item.getWorldLocation().add(currentVelocity.mult(tpf)));
				
			if (item.getWorldLocation().x > stage.getDisplayWidth()){
				if (currentVelocity.x > 0)currentVelocity.setX(-currentVelocity.getX());				
			}else if (item.getWorldLocation().x < 0){
				if (currentVelocity.x < 0)currentVelocity.setX(-currentVelocity.getX());	
			}
			
			if (item.getWorldLocation().y > stage.getDisplayHeight()){
				if (currentVelocity.y > 0)currentVelocity.setY(-currentVelocity.getY());				
			}else if (item.getWorldLocation().y < 0){
				if (currentVelocity.y < 0)currentVelocity.setY(-currentVelocity.getY());	
			}
			
			Vector2f reduceBy = currentVelocity.normalize().mult(deceleration* tpf);
			currentVelocity.subtractLocal(reduceBy);
			
			if(currentVelocity.length() < 1f) {
				finished = true;
			}
		}
	}

	public void moveWithVelocity(Vector2f velocity) {
		this.currentVelocity = velocity.clone();
		this.finished = false;
	}

}
