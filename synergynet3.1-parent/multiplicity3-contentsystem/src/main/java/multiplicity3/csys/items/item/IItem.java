package multiplicity3.csys.items.item;

import java.util.List;
import java.util.UUID;

import multiplicity3.csys.behaviours.IBehaviour;
import multiplicity3.csys.items.INestable;
import multiplicity3.csys.items.events.IItemListener;
import multiplicity3.csys.items.events.MultiTouchEventDispatcher;
import multiplicity3.csys.zorder.IZOrderManager;

import com.jme3.math.Vector2f;
import com.jme3.scene.Spatial;

/**
 * The Interface IItem.
 */
public interface IItem extends INestable
{

	/**
	 * Adds the item listener.
	 *
	 * @param itemListener
	 *            the item listener
	 */
	public void addItemListener(IItemListener itemListener);

	/**
	 * Behaviour added.
	 *
	 * @param behaviour
	 *            the behaviour
	 */
	public void behaviourAdded(IBehaviour behaviour);

	/**
	 * Center item.
	 */
	public void centerItem();

	/**
	 * Convert world velocity to local velocity.
	 *
	 * @param loc
	 *            the loc
	 * @return the vector2f
	 */
	public Vector2f convertWorldVelocityToLocalVelocity(Vector2f loc);

	/**
	 * Gets the behaviours.
	 *
	 * @return the behaviours
	 */
	public List<IBehaviour> getBehaviours();

	/**
	 * Gets the behaviours.
	 *
	 * @param <T>
	 *            the generic type
	 * @param clazz
	 *            the clazz
	 * @return the behaviours
	 */
	public <T extends IBehaviour> List<T> getBehaviours(Class<T> clazz);

	/**
	 * Gets the item listeners.
	 *
	 * @return the item listeners
	 */
	public List<IItemListener> getItemListeners();

	/**
	 * Gets the manipulable spatial.
	 *
	 * @return the manipulable spatial
	 */
	public Spatial getManipulableSpatial();

	/**
	 * Gets the multi touch dispatcher.
	 *
	 * @return the multi touch dispatcher
	 */
	public MultiTouchEventDispatcher getMultiTouchDispatcher();

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName();

	/**
	 * Gets the parent item.
	 *
	 * @return the parent item
	 */
	public IItem getParentItem();

	/**
	 * Gets the relative location.
	 *
	 * @return the relative location
	 */
	public Vector2f getRelativeLocation();

	/**
	 * Gets the relative location of world location.
	 *
	 * @param worldloc
	 *            the worldloc
	 * @return the relative location of world location
	 */
	public Vector2f getRelativeLocationOfWorldLocation(Vector2f worldloc);

	/**
	 * Gets the relative rotation.
	 *
	 * @return the relative rotation
	 */
	public float getRelativeRotation();

	/**
	 * Gets the relative scale.
	 *
	 * @return the relative scale
	 */
	public float getRelativeScale();

	/**
	 * Gets the uuid.
	 *
	 * @return the uuid
	 */
	public UUID getUUID();

	/**
	 * Gets the world location.
	 *
	 * @return the world location
	 */
	public Vector2f getWorldLocation();

	/**
	 * Get the world location of an arbitrary point, where the arbitrary point
	 * is given in relative coordinates to this item.
	 *
	 * @param v
	 * @return
	 */
	public Vector2f getWorldLocation(Vector2f v);

	/**
	 * Gets the world rotation degrees.
	 *
	 * @return the world rotation degrees
	 */
	public float getWorldRotationDegrees();

	/**
	 * Gets the z order.
	 *
	 * @return the z order
	 */
	public int getZOrder();

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.csys.items.INestable#getZOrderManager()
	 */
	@Override
	public IZOrderManager getZOrderManager();

	/**
	 * Checks if is visible.
	 *
	 * @return true, if is visible
	 */
	public boolean isVisible();

	/**
	 * Removes the item listener.
	 *
	 * @param itemListener
	 *            the item listener
	 */
	public void removeItemListener(IItemListener itemListener);

	/**
	 * Sets the interaction enabled.
	 *
	 * @param b
	 *            the new interaction enabled
	 */
	public void setInteractionEnabled(boolean b);

	// 'undocumented' API methods
	// public Spatial getTreeRootSpatial();
	// public Spatial getManipulableSpatial();

	/**
	 * Sets the item listeners.
	 *
	 * @param itemListeners
	 *            the new item listeners
	 */
	public void setItemListeners(List<IItemListener> itemListeners);

	/**
	 * Sets the parent item.
	 *
	 * @param parent
	 *            the new parent item
	 */
	public void setParentItem(IItem parent);

	/**
	 * Sets the relative location.
	 *
	 * @param loc
	 *            the new relative location
	 */
	public void setRelativeLocation(Vector2f loc);

	/**
	 * Sets the relative rotation.
	 *
	 * @param angle
	 *            the new relative rotation
	 */
	public void setRelativeRotation(float angle);

	/**
	 * Sets the relative scale.
	 *
	 * @param scale
	 *            the new relative scale
	 */
	public void setRelativeScale(float scale);

	/**
	 * Sets the visible.
	 *
	 * @param isVisible
	 *            the new visible
	 */
	public void setVisible(boolean isVisible);

	/**
	 * Puts the item to a location on screen, given by screen coordinates
	 *
	 * @param loc
	 *            a position, in screen coordinates
	 */
	public void setWorldLocation(Vector2f loc);

	/**
	 * Sets the z order.
	 *
	 * @param zOrder
	 *            the new z order
	 */
	void setZOrder(int zOrder);

}
