package synergynet3.behaviours.bouncer;

import java.util.HashMap;
import java.util.Map.Entry;

import multiplicity3.csys.animation.elements.AnimationElement;
import multiplicity3.csys.behaviours.inertia.ItemPositionHistory;
import multiplicity3.csys.items.item.IItem;
import multiplicity3.csys.stage.IStage;
import synergynet3.additionalitems.PerformActionOnAllDescendents;
import synergynet3.behaviours.BehaviourUtilities.RelativePosition;
import synergynet3.behaviours.networkflick.NetworkFlickBehaviour;

/**
 * Repositions an item when flick using the JME animation system. Calculates
 * trajectories when an item bounces off a display border and detects when an
 * item should be transferred. Initiates network transfers when required.
 */
public class CircleBouncerAnimationElement extends AnimationElement {

	/**
	 * Objects for the item to collide with
	 */
	private HashMap<IItem, Float> collidables = new HashMap<IItem, Float>();

	/**
	 * Item is currently colliding with.
	 */
	private HashMap<IItem, Boolean> collidingWith = new HashMap<IItem, Boolean>();

	/**
	 * Used to stop further calculations when an item when an item is to be
	 * transferred or has stopped moving.
	 */
	private boolean finished;

	/**
	 * The item which is being managed by this class.
	 */
	private IItem item;

	/**
	 * Collection of positions at which the item has currently been positioned
	 * when moved by a user.
	 */
	private ItemPositionHistory positionHistory;

	/**
	 * Radius of the managed item.
	 */
	private float radius = 0f;

	/**
	 * How often the item has hit a collidable.
	 */
	private int tapCount = 0;

	/**
	 * Creates an animation element for the supplied item which can then create
	 * the flick motion by repositioning the managed item using JME's animation
	 * system.
	 *
	 * @param item The item to be influenced by this animation.
	 * @param stage The stage the item currently resides in.
	 */
	public CircleBouncerAnimationElement(IItem item, IStage stage) {
		this.item = item;
		positionHistory = new ItemPositionHistory(item);
		positionHistory.setStage(stage);
	}

	/**
	 * @param collidables the collidables to set
	 */
	public void addCollidable(IItem item, Float radius) {
		collidables.put(item, radius);
		collidingWith.put(item, false);
	}

	/**
	 * Clear collidables
	 */
	public void clearCollidable() {
		collidables.clear();
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
	 * Gets the tap count.
	 *
	 * @return the tap count
	 */
	public int getTapCount() {
		return tapCount;
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
	 * @param collidables the collidable item to remove
	 */
	public void removeCollidable(IItem item) {
		collidables.remove(item);
		collidingWith.remove(item);
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.csys.animation.elements.AnimationElement#reset()
	 */
	@Override
	public void reset() {
		finished = true;
	}

	/**
	 * Reset tap count.
	 */
	public void resetTapCount() {
		tapCount = 0;
	}

	/**
	 * Sets the radius.
	 *
	 * @param radius the new radius
	 */
	public void setRadius(float radius) {
		this.radius = radius;
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
			positionHistory.add(item.getRelativeLocation(),
					System.currentTimeMillis());
			for (Entry<IItem, Float> entry : collidables.entrySet()) {
				float currentDistance = item.getRelativeLocation().distance(
						entry.getKey().getRelativeLocation());
				float collisionDistance = entry.getValue() + radius;
				if (collidingWith.get(entry.getKey())) {
					if (currentDistance > collisionDistance) {
						collidingWith.put(entry.getKey(), false);
					}
				} else {
					if (currentDistance < collisionDistance) {
						collidingWith.put(entry.getKey(), true);
						onEnteringCollision(entry.getKey(), tpf);
					}
				}
			}
		}
	}

	/**
	 * Action performed on entering a collision.
	 *
	 * @param collidale The item the managed item collided with.
	 */
	private void onEnteringCollision(IItem collidale, final float tpf) {

		tapCount++;

		float colliderX = collidale.getRelativeLocation().x;
		float colliderY = collidale.getRelativeLocation().y;
		float itemX = item.getRelativeLocation().x;
		float itemY = item.getRelativeLocation().y;

		RelativePosition bouncePosition = RelativePosition.TOP;

		if ((colliderX < itemX) && (colliderY > itemY)) {
			bouncePosition = RelativePosition.TOPLEFT;
		} else if ((colliderX > itemX) && (colliderY > itemY)) {
			bouncePosition = RelativePosition.TOPRIGHT;
		} else if ((colliderX < itemX) && (colliderY < itemY)) {
			bouncePosition = RelativePosition.BOTTOMLEFT;
		} else if ((colliderX > itemX) && (colliderY < itemY)) {
			bouncePosition = RelativePosition.BOTTOMRIGHT;
		} else if (colliderX < itemX) {
			bouncePosition = RelativePosition.LEFT;
		} else if (colliderX > itemX) {
			bouncePosition = RelativePosition.RIGHT;
		} else if (colliderY < itemY) {
			bouncePosition = RelativePosition.BOTTOM;
		}

		final RelativePosition rp = bouncePosition;

		new PerformActionOnAllDescendents(collidale, false, false) {
			@Override
			protected void actionOnDescendent(IItem child) {
				for (NetworkFlickBehaviour behaviour : child
						.getBehaviours(NetworkFlickBehaviour.class)) {
					behaviour.bounce(positionHistory.getVelocity().x,
							positionHistory.getVelocity().y, tpf, rp);
				}
			}
		};
	}

}
