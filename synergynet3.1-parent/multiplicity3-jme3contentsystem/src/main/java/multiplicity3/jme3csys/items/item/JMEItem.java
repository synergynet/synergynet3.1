package multiplicity3.jme3csys.items.item;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

import multiplicity3.csys.annotations.ImplementsContentItem;
import multiplicity3.csys.behaviours.IBehaviour;
import multiplicity3.csys.items.events.IItemListener;
import multiplicity3.csys.items.events.MultiTouchEventDispatcher;
import multiplicity3.csys.items.item.IItem;
import multiplicity3.csys.zorder.IZOrderManager;
import multiplicity3.csys.zorder.ZOrderManager;
import multiplicity3.input.IMultiTouchEventListener;
import multiplicity3.input.events.MultiTouchCursorEvent;
import multiplicity3.input.events.MultiTouchObjectEvent;
import multiplicity3.jme3csys.items.IInitable;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

@ImplementsContentItem(target = IItem.class)
public abstract class JMEItem extends Node implements IItem, IInitable {
	private static final Logger log = Logger.getLogger(JMEItem.class.getName());

	public static final String KEY_JMEITEMDATA = "KEY_JMEITEMDATA";

	protected MultiTouchEventDispatcher dispatcher = new MultiTouchEventDispatcher();
	protected UUID uuid;
	protected IItem parentItem;
	protected IZOrderManager zOrderManager;
	private List<IItemListener> itemListeners = new CopyOnWriteArrayList<IItemListener>();
	private String itemName;
	private List<IItem> itemChildren = new ArrayList<IItem>();
	private float relativeRotation;
	private List<IBehaviour> behaviours = new ArrayList<IBehaviour>();
	private boolean isVisible = true;	
	private Quaternion trot = new Quaternion(); // temp
	private Vector3f tempWorldRotation = new Vector3f(); // temp

	public JMEItem(String name, UUID uuid) {
		super(name);
		this.itemName = name;
		this.uuid = uuid;
		zOrderManager = new ZOrderManager(this, 5);
		final IItem instance = this;
		dispatcher.addListener(new IMultiTouchEventListener() {			
			@Override
			public void cursorReleased(MultiTouchCursorEvent event) {
				for(IItemListener l : getItemListeners()) l.itemCursorReleased(instance, event);
			}

			@Override
			public void cursorPressed(MultiTouchCursorEvent event) {
				for(IItemListener l : getItemListeners()) {
					l.itemCursorPressed(instance, event);
				}
			}

			@Override
			public void cursorClicked(MultiTouchCursorEvent event) {
				for(IItemListener l : getItemListeners()) l.itemCursorClicked(instance, event);	
			}

			@Override
			public void cursorChanged(MultiTouchCursorEvent event) {
				for(IItemListener l : getItemListeners()) l.itemCursorChanged(instance, event);
			}

			@Override public void objectRemoved(MultiTouchObjectEvent event) {}
			@Override public void objectChanged(MultiTouchObjectEvent event) {}
			@Override public void objectAdded(MultiTouchObjectEvent event) {}
		});

	}


	@Override
	public void setRelativeLocation(Vector2f newLoc) {
		setLocalTranslation(newLoc.x, newLoc.y, getLocalTranslation().z);
		updateGeometricState();
		for(IItemListener l : getItemListeners()) {
			l.itemMoved(this);
		}
	}

	@Override
	public void setWorldLocation(Vector2f loc) {
		if(getParent() == null) {
			setRelativeLocation(new Vector2f(loc.x, loc.y));
		}else{
			Vector3f store = new Vector3f();
			getParent().worldToLocal(new Vector3f(loc.x, loc.y, getWorldTranslation().z), store);
			setRelativeLocation(new Vector2f(store.x, store.y));
		}	
		updateGeometricState();
	}

	@Override
	public Vector2f getRelativeLocation() {
		return new Vector2f(getLocalTranslation().x, getLocalTranslation().y);
	}

	private Vector3f out = new Vector3f();
	@Override
	public Vector2f getWorldLocation() {
		if(getParent() == null) return getRelativeLocation();
		getParent().localToWorld(getLocalTranslation(), out);
		return new Vector2f(out.x, out.y);
	}

	@Override
	public Vector2f getWorldLocation(Vector2f v) {
		if(getParent() == null) return v;
		getParent().localToWorld(new Vector3f(v.x, v.y, getLocalTranslation().z), out);
		return new Vector2f(out.x, out.y);
	}

	/**
	 * Converts an arbitrary location in world coordinates into local coordinates
	 * relative to this item.
	 */
	@Override
	public Vector2f getRelativeLocationOfWorldLocation(Vector2f worldloc) {
		if(getParent() == null) {
			return worldloc;
		}else{		
			Vector3f in = new Vector3f(worldloc.x, worldloc.y, getLocalTranslation().z);
			getParent().worldToLocal(in, out);
			return new Vector2f(out.x, out.y);
		}
	}

	public Vector2f convertWorldVelocityToLocalVelocity(Vector2f loc) {		
		if(getParent() == null) return null;
		Vector3f in = new Vector3f(loc.x, loc.y, getLocalTranslation().z);
		Vector3f store = getWorldRotation().inverse().mult(in);				
		return new Vector2f(store.x, store.y);
	}

	@Override
	public void setRelativeRotation(float angle) {
		this.relativeRotation = angle;
		trot.fromAngleAxis(angle, Vector3f.UNIT_Z);
		setLocalRotation(trot);		
		updateGeometricState();
		for(IItemListener l : getItemListeners()) {
			l.itemRotated(this);
		}
	}

	@Override
	public float getRelativeRotation() {
		return relativeRotation;
	}


	@Override
	public float getWorldRotationDegrees() {
		getWorldRotation().toAngleAxis(tempWorldRotation);
		return tempWorldRotation.z;
	}

	@Override
	public void setRelativeScale(float scale) {
		setLocalScale(scale);
		for(IItemListener l : getItemListeners()) {
			l.itemScaled(this);
		}
	}

	//TODO: support x, y, z scale
	@Override
	public float getRelativeScale() {
		return getLocalScale().z;
	}


	public String getName() {
		return itemName;

	}

	public UUID getUUID() {
		return uuid;
	}

	public List<IItem> getItemChildren() {
		return itemChildren;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	@Override
	public List<IItemListener> getItemListeners() {
		return itemListeners;
	}

	@Override
	public void setItemListeners(List<IItemListener> itemListeners){
		this.itemListeners = itemListeners;
	}


	List<IChildrenChangedListener> childrenChangedListeners = new ArrayList<IChildrenChangedListener>();

	private int zorder;


	@Override
	public void registerChildrenChangedListener(IChildrenChangedListener listener) {
		if(!childrenChangedListeners.contains(listener)) childrenChangedListeners.add(listener);
	}

	@Override
	public void deRegisterChildrenChangedListener(IChildrenChangedListener listener) {
		childrenChangedListeners.remove(listener);
	}

	@Override
	public IZOrderManager getZOrderManager() {
		return zOrderManager;
	}

	@Override
	public void setInteractionEnabled(boolean b) {
		this.dispatcher.setEnabled(b);
	}



	@Override
	public MultiTouchEventDispatcher getMultiTouchDispatcher() {
		return dispatcher;
	}

	@Override
	public IItem getParentItem() {
		return parentItem;
	}

	@Override
	public void setParentItem(IItem parent) {
		log.fine(getManipulableSpatial() + " parent item changing to " + parent);
		this.removeFromParent();
		if(parent != null) {
			((JMEItem)parent).attachChild(this);
		}		
		parentItem = parent;
	}


	@Override
	public void addItem(IItem item) {
		log.fine("Adding " + item + " to " + this);
		getItemChildren().add(item);
		getZOrderManager().registerForZOrdering(item);
		item.setParentItem(this);
		notifyChildrenChanged();
	}

	@Override
	public void removeItem(IItem item) {
		log.fine("Removing " + item + " from " + this);
		getItemChildren().remove(item);
		getZOrderManager().unregisterForZOrdering(item);		
		item.setParentItem(null);		
		notifyChildrenChanged();
	}

	@Override
	public void removeAllItems(boolean recursive) {
		while(getItemChildren().size() > 0) {
			IItem item = getItemChildren().get(0);
			if(recursive) item.removeAllItems(recursive);
			removeItem(item);
		}
	}

	@Override 
	public List<IItem> getChildItems() {
		return getItemChildren();
	}

	@Override
	public int getChildrenCount() {
		return getItemChildren().size();
	}

	@Override
	public boolean hasChildren() {
		return getItemChildren().size() > 0;
	}

	@Override
	public void addItemListener(IItemListener itemListener) {
		if(!getItemListeners().contains(itemListener)) {
			getItemListeners().add(itemListener);
		}	
	}

	public void removeItemListener(IItemListener itemListener) {
		getItemListeners().remove(itemListener);
	}



	@Override
	public void behaviourAdded(IBehaviour behaviour) {
		behaviours.add(behaviour);
	}

	@Override
	public List<IBehaviour> getBehaviours() {
		return behaviours;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends IBehaviour> List<T> getBehaviours(Class<T> clazz) {
		List<T> bs = new ArrayList<T>();
		for(IBehaviour b : getBehaviours()) {
			if(b.getClass().equals(clazz)) {				
				bs.add((T)b);
			}
		}
		return bs;
	}

	@Override
	public void centerItem() {
		Vector2f center = new Vector2f(0, 0);
		setRelativeLocation(center);
	}

	@Override
	public void setVisible(boolean isVisible) {
		if(isVisible == this.isVisible) return;		

		if(isVisible) {			
			this.setCullHint(CullHint.Never);
		}else{
			this.setCullHint(CullHint.Always);
		}

		this.isVisible = isVisible;
		
		for(IItemListener l : itemListeners) {
			l.itemVisibilityChanged(this, isVisible);
		}

		for(int i = 0; i < itemChildren.size(); i++) {
			itemChildren.get(i).setVisible(isVisible);
		}
	}

	@Override
	public boolean isVisible() {
		return isVisible;
	}

	@Override
	public void setZOrder(int zOrder) {
		this.zorder = zOrder;
		if(getParent() != null && this.getParentItem() != null) {
			setLocalTranslation(getLocalTranslation().x, getLocalTranslation().y, zOrder - this.getParentItem().getZOrder());
		}else{
			Vector3f newZOrder = getLocalTranslation().clone();
			setLocalTranslation(newZOrder.x, newZOrder.y, zOrder);
		}
		updateGeometricState();
	}
	
	public int getZOrder() {
		return this.zorder;
	}

	private void notifyChildrenChanged() {
		for(IChildrenChangedListener l : childrenChangedListeners) {
			l.childrenChanged(this, itemChildren);
		}
	}

	public String toString() {
		return this.getClass().getName() + " (" + getItemName() + ")";
	}
}



