package multiplicity3.csys.animation.elements.behaviourelements;

import multiplicity3.csys.animation.elements.AnimationElement;
import multiplicity3.csys.items.item.IItem;
import multiplicity3.csys.stage.IStage;

import com.jme3.math.Vector2f;

/**
 * The Class InertiaAnimationElement.
 */
public class InertiaAnimationElement extends AnimationElement {

	/** The current velocity. */
	private Vector2f currentVelocity;

	/** The deceleration. */
	private float deceleration = 100f;

	/** The finished. */
	private boolean finished;

	/** The item. */
	private IItem item;

	/** The stage. */
	private IStage stage;

	/**
	 * Instantiates a new inertia animation element.
	 *
	 * @param item the item
	 * @param stage the stage
	 */
	public InertiaAnimationElement(IItem item, IStage stage) {
		this.item = item;
		this.stage = stage;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.csys.animation.elements.AnimationElement#elementStart(float
	 * )
	 */
	@Override
	public void elementStart(float tpf) {
	}

	/**
	 * Gets the deceleration factor.
	 *
	 * @return the deceleration factor
	 */
	public float getDecelerationFactor() {
		return this.deceleration;
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.csys.animation.elements.AnimationElement#isFinished()
	 */
	@Override
	public boolean isFinished() {
		return finished;
	}

	/**
	 * Move with velocity.
	 *
	 * @param velocity the velocity
	 */
	public void moveWithVelocity(Vector2f velocity) {
		this.currentVelocity = velocity.clone();
		this.finished = false;
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.csys.animation.elements.AnimationElement#reset()
	 */
	@Override
	public void reset() {
		finished = true;
		currentVelocity = new Vector2f();
	}

	/*
	 * Value > 1 for less than default drag, value < 1 for more than default
	 * drag.
	 */
	/**
	 * Sets the deceleration.
	 *
	 * @param deceleration the new deceleration
	 */
	public void setDeceleration(float deceleration) {
		this.deceleration = deceleration;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.csys.animation.elements.AnimationElement#updateAnimationState
	 * (float)
	 */
	@Override
	public void updateAnimationState(float tpf) {
		if (!finished) {
			item.setWorldLocation(item.getWorldLocation().add(
					currentVelocity.mult(tpf)));

			if (item.getWorldLocation().x > stage.getDisplayWidth()) {
				if (currentVelocity.x > 0) {
					currentVelocity.setX(-currentVelocity.getX());
				}
			} else if (item.getWorldLocation().x < 0) {
				if (currentVelocity.x < 0) {
					currentVelocity.setX(-currentVelocity.getX());
				}
			}

			if (item.getWorldLocation().y > stage.getDisplayHeight()) {
				if (currentVelocity.y > 0) {
					currentVelocity.setY(-currentVelocity.getY());
				}
			} else if (item.getWorldLocation().y < 0) {
				if (currentVelocity.y < 0) {
					currentVelocity.setY(-currentVelocity.getY());
				}
			}

			Vector2f reduceBy = currentVelocity.normalize().mult(
					deceleration * tpf);
			currentVelocity.subtractLocal(reduceBy);

			if (currentVelocity.length() < 1f) {
				finished = true;
			}
		}
	}

}
