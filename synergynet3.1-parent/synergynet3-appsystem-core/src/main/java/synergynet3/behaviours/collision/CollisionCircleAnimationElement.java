package synergynet3.behaviours.collision;

import java.util.HashMap;
import java.util.Map.Entry;

import synergynet3.additionalUtils.AdditionalSynergyNetUtilities;
import multiplicity3.csys.animation.elements.AnimationElement;
import multiplicity3.csys.items.item.IItem;

/**
 * Repositions an item when flick using the JME animation system.
 * Calculates trajectories when an item bounces off a display border and
 * detects when an item should be transferred.  Initiates network transfers
 * when required.
 *
 */
public class CollisionCircleAnimationElement extends AnimationElement {
	
	/**
	 * Objects for the item to collide with
	 */
	private HashMap<IItem, Float> collidables = new HashMap<IItem, Float>();
	
	/**
	 * Item is currently colliding with.
	 */
	private HashMap<IItem, Boolean> collidingWith = new HashMap<IItem, Boolean>();
	
	/**
	 * The item which is being managed by this class.
	 */
	private IItem item;
	
	/**
	 * Used to stop further calculations when an item when an item is to be transferred or has stopped moving.
	 */
	private boolean finished;
	
	
	/**
	 * Creates an animation element for the supplied item which can then create the flick motion
	 * by repositioning the managed item using JME's animation system.
	 * 
	 * @param item The item to be influenced by this animation.
	 * @param stage The stage the item currently resides in.
	 */
	public CollisionCircleAnimationElement(IItem item) {
		this.item = item;
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
	}
	
	@Override
	public void updateAnimationState(float tpf) {
		if (!finished){
			for (Entry<IItem, Float> entry: collidables.entrySet()){
				if (collidingWith.get(entry.getKey())){
					if (!AdditionalSynergyNetUtilities.inRadius(item.getRelativeLocation(), entry.getKey().getRelativeLocation(), entry.getValue())){
						collidingWith.put(entry.getKey(), false);
						onLeavingCollision(entry.getKey());
					}
				}else{
					if (AdditionalSynergyNetUtilities.inRadius(item.getRelativeLocation(), entry.getKey().getRelativeLocation(), entry.getValue())){
						collidingWith.put(entry.getKey(), true);
						onEnteringCollision(entry.getKey());
					}
				}
			}
		}
	}
	
	/**
	 * Override this method to set what action should be performed on entering a collision.
	 * 
	 * @param collidale The item the managed item collided with.
	 */
	protected void onEnteringCollision(IItem collidale){}
	
	/**
	 * Override this method to set what action should be performed on leaving a collision.
	 * 
	 * @param collidale The item the managed item collided with.
	 */
	protected void onLeavingCollision(IItem collidale){}

	/**
	 * @param collidables the collidables to set
	 */
	public void addCollidable(IItem item, Float radius) {
		collidables.put(item, radius);
		collidingWith.put(item, false);
	}	
	
	/**
	 * @param collidables the collidable item to remove
	 */
	public void removeCollidable(IItem item) {
		collidables.remove(item);
		collidingWith.remove(item);
	}	
	
}
