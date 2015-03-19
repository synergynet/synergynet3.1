package synergynet3.apps.numbernet.ui.animation;

import com.jme3.math.Vector2f;

import multiplicity3.csys.animation.elements.AnimationElement;
import multiplicity3.csys.items.item.IItem;


public class MoveItem extends AnimationElement {

	private IItem item;
	private Vector2f from;
	private Vector2f to;

	private Vector2f currentPos;
	private Vector2f direction;
	private float unitsPerSecond;
	private boolean finished;
	private float inSeconds;
	private boolean relative = true;
	private float elapsedTime = 0f;

	public MoveItem(Vector2f from, Vector2f to, float inSeconds, IItem item, boolean inWorldCoords) {
		this.inSeconds = inSeconds;
		this.from = from;
		this.to = to;
		this.item = item;
		relative = !inWorldCoords;
		reset();
	}

	@Override
	public void reset() {
		finished = false;
		currentPos = from.clone();
		direction = to.subtract(from).normalize();
		elapsedTime = 0f;
		unitsPerSecond = to.subtract(from).length() / inSeconds;
	}

	@Override
	public void elementStart(float tpf) {
		currentPos = from.clone();
		direction = to.subtract(from).normalize();
		unitsPerSecond = to.subtract(from).length() / inSeconds;
		elapsedTime = 0f;
	}

	@Override
	public void updateAnimationState(float tpf) {
		elapsedTime += tpf;
		currentPos.addLocal(direction.mult(unitsPerSecond * tpf));
//		if(currentPos.distance(to) < 0.2f) {
		if(elapsedTime > inSeconds) {
			currentPos.set(to);
			finished = true;	
		}
		updatePosition();
	}

	@Override
	public boolean isFinished() {
		return finished;
	}

	private void updatePosition() {
		if(relative) {
			item.setRelativeLocation(currentPos);
		}else{
			item.setWorldLocation(currentPos);
		}
	}

}
