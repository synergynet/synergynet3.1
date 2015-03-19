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

public interface IItem extends INestable {
	public UUID getUUID();
	public void setParentItem(IItem parent);
	public IItem getParentItem();	
	public String getName();
	public MultiTouchEventDispatcher getMultiTouchDispatcher();
	
	public void addItemListener(IItemListener itemListener);
	public void removeItemListener(IItemListener itemListener);
	public List<IItemListener> getItemListeners();
	public void setItemListeners(List<IItemListener> itemListeners);
	
	/**
	 * Puts the item to a location on screen, given by screen coordinates
	 * @param loc a position, in screen coordinates
	 */
	public void setWorldLocation(Vector2f loc);
	public Vector2f getWorldLocation();
	
	/**
	 * Get the world location of an arbitrary point, where the
	 * arbitrary point is given in relative coordinates to this item.
	 * @param v
	 * @return
	 */
	public Vector2f getWorldLocation(Vector2f v);
	public void setRelativeLocation(Vector2f loc);
	public Vector2f getRelativeLocation();
	public Vector2f getRelativeLocationOfWorldLocation(Vector2f worldloc);
	public Vector2f convertWorldVelocityToLocalVelocity(Vector2f loc);
	public void centerItem();	
	public void setRelativeRotation(float angle);
	public float getRelativeRotation();
	public float getWorldRotationDegrees();
	public void setRelativeScale(float scale);
	public float getRelativeScale();
	
	public IZOrderManager getZOrderManager();
	

	public void setInteractionEnabled(boolean b);
	
	// 'undocumented' API methods
	//public Spatial getTreeRootSpatial();
	//public Spatial getManipulableSpatial();
	
	public void behaviourAdded(IBehaviour behaviour);
	public List<IBehaviour> getBehaviours();
	public <T extends IBehaviour> List<T> getBehaviours(Class<T> clazz);

    public void setVisible(boolean isVisible);
	public boolean isVisible();
	public int getZOrder();
	void setZOrder(int zOrder);
	

	public Spatial getManipulableSpatial();
	
	
}
