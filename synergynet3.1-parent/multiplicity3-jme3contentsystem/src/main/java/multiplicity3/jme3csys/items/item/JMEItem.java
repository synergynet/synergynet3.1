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

/**
 * The Class JMEItem.
 */
@ImplementsContentItem(target = IItem.class)
public abstract class JMEItem extends Node implements IItem, IInitable
{

	/** The Constant KEY_JMEITEMDATA. */
	public static final String KEY_JMEITEMDATA = "KEY_JMEITEMDATA";

	/** The Constant log. */
	private static final Logger log = Logger.getLogger(JMEItem.class.getName());

	/** The behaviours. */
	private List<IBehaviour> behaviours = new ArrayList<IBehaviour>();

	/** The is visible. */
	private boolean isVisible = true;

	/** The item children. */
	private List<IItem> itemChildren = new ArrayList<IItem>();

	/** The item listeners. */
	private List<IItemListener> itemListeners = new CopyOnWriteArrayList<IItemListener>();

	/** The item name. */
	private String itemName;

	/** The out. */
	private Vector3f out = new Vector3f();

	/** The relative rotation. */
	private float relativeRotation;

	/** The temp world rotation. */
	private Vector3f tempWorldRotation = new Vector3f(); // temp

	/** The trot. */
	private Quaternion trot = new Quaternion(); // temp

	/** The zorder. */
	private int zorder;

	/** The dispatcher. */
	protected MultiTouchEventDispatcher dispatcher = new MultiTouchEventDispatcher();

	/** The parent item. */
	protected IItem parentItem;

	/** The uuid. */
	protected UUID uuid;

	/** The z order manager. */
	protected IZOrderManager zOrderManager;

	/** The children changed listeners. */
	List<IChildrenChangedListener> childrenChangedListeners = new ArrayList<IChildrenChangedListener>();

	/**
	 * Instantiates a new JME item.
	 *
	 * @param name
	 *            the name
	 * @param uuid
	 *            the uuid
	 */
	public JMEItem(String name, UUID uuid)
	{
		super(name);
		this.itemName = name;
		this.uuid = uuid;
		zOrderManager = new ZOrderManager(this, 5);
		final IItem instance = this;
		dispatcher.addListener(new IMultiTouchEventListener()
		{
			@Override
			public void cursorChanged(MultiTouchCursorEvent event)
			{
				for (IItemListener l : getItemListeners())
				{
					l.itemCursorChanged(instance, event);
				}
			}

			@Override
			public void cursorClicked(MultiTouchCursorEvent event)
			{
				for (IItemListener l : getItemListeners())
				{
					l.itemCursorClicked(instance, event);
				}
			}

			@Override
			public void cursorPressed(MultiTouchCursorEvent event)
			{
				for (IItemListener l : getItemListeners())
				{
					l.itemCursorPressed(instance, event);
				}
			}

			@Override
			public void cursorReleased(MultiTouchCursorEvent event)
			{
				for (IItemListener l : getItemListeners())
				{
					l.itemCursorReleased(instance, event);
				}
			}

			@Override
			public void objectAdded(MultiTouchObjectEvent event)
			{
			}

			@Override
			public void objectChanged(MultiTouchObjectEvent event)
			{
			}

			@Override
			public void objectRemoved(MultiTouchObjectEvent event)
			{
			}
		});

	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.csys.items.INestable#addItem(multiplicity3.csys.items.item
	 * .IItem)
	 */
	@Override
	public void addItem(IItem item)
	{
		log.fine("Adding " + item + " to " + this);
		getItemChildren().add(item);
		getZOrderManager().registerForZOrdering(item);
		item.setParentItem(this);
		notifyChildrenChanged();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.csys.items.item.IItem#addItemListener(multiplicity3.csys
	 * .items.events.IItemListener)
	 */
	@Override
	public void addItemListener(IItemListener itemListener)
	{
		if (!getItemListeners().contains(itemListener))
		{
			getItemListeners().add(itemListener);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.csys.items.item.IItem#behaviourAdded(multiplicity3.csys
	 * .behaviours.IBehaviour)
	 */
	@Override
	public void behaviourAdded(IBehaviour behaviour)
	{
		behaviours.add(behaviour);
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.csys.items.item.IItem#centerItem()
	 */
	@Override
	public void centerItem()
	{
		Vector2f center = new Vector2f(0, 0);
		setRelativeLocation(center);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.csys.items.item.IItem#convertWorldVelocityToLocalVelocity
	 * (com.jme3.math.Vector2f)
	 */
	@Override
	public Vector2f convertWorldVelocityToLocalVelocity(Vector2f loc)
	{
		if (getParent() == null)
		{
			return null;
		}
		Vector3f in = new Vector3f(loc.x, loc.y, getLocalTranslation().z);
		Vector3f store = getWorldRotation().inverse().mult(in);
		return new Vector2f(store.x, store.y);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.csys.items.INestable#deRegisterChildrenChangedListener(
	 * multiplicity3.csys.items.INestable.IChildrenChangedListener)
	 */
	@Override
	public void deRegisterChildrenChangedListener(IChildrenChangedListener listener)
	{
		childrenChangedListeners.remove(listener);
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.csys.items.item.IItem#getBehaviours()
	 */
	@Override
	public List<IBehaviour> getBehaviours()
	{
		return behaviours;
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.csys.items.item.IItem#getBehaviours(java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T extends IBehaviour> List<T> getBehaviours(Class<T> clazz)
	{
		List<T> bs = new ArrayList<T>();
		for (IBehaviour b : getBehaviours())
		{
			if (b.getClass().equals(clazz))
			{
				bs.add((T) b);
			}
		}
		return bs;
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.csys.items.INestable#getChildItems()
	 */
	@Override
	public List<IItem> getChildItems()
	{
		return getItemChildren();
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.csys.items.INestable#getChildrenCount()
	 */
	@Override
	public int getChildrenCount()
	{
		return getItemChildren().size();
	}

	/**
	 * Gets the item children.
	 *
	 * @return the item children
	 */
	public List<IItem> getItemChildren()
	{
		return itemChildren;
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.csys.items.item.IItem#getItemListeners()
	 */
	@Override
	public List<IItemListener> getItemListeners()
	{
		return itemListeners;
	}

	/**
	 * Gets the item name.
	 *
	 * @return the item name
	 */
	public String getItemName()
	{
		return itemName;
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.csys.items.item.IItem#getMultiTouchDispatcher()
	 */
	@Override
	public MultiTouchEventDispatcher getMultiTouchDispatcher()
	{
		return dispatcher;
	}

	/*
	 * (non-Javadoc)
	 * @see com.jme3.scene.Spatial#getName()
	 */
	@Override
	public String getName()
	{
		return itemName;

	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.csys.items.item.IItem#getParentItem()
	 */
	@Override
	public IItem getParentItem()
	{
		return parentItem;
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.csys.items.item.IItem#getRelativeLocation()
	 */
	@Override
	public Vector2f getRelativeLocation()
	{
		return new Vector2f(getLocalTranslation().x, getLocalTranslation().y);
	}

	/**
	 * Converts an arbitrary location in world coordinates into local
	 * coordinates relative to this item.
	 */
	@Override
	public Vector2f getRelativeLocationOfWorldLocation(Vector2f worldloc)
	{
		if (getParent() == null)
		{
			return worldloc;
		}
		else
		{
			Vector3f in = new Vector3f(worldloc.x, worldloc.y, getLocalTranslation().z);
			getParent().worldToLocal(in, out);
			return new Vector2f(out.x, out.y);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.csys.items.item.IItem#getRelativeRotation()
	 */
	@Override
	public float getRelativeRotation()
	{
		return relativeRotation;
	}

	// TODO: support x, y, z scale
	/*
	 * (non-Javadoc)
	 * @see multiplicity3.csys.items.item.IItem#getRelativeScale()
	 */
	@Override
	public float getRelativeScale()
	{
		return getLocalScale().z;
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.csys.items.item.IItem#getUUID()
	 */
	@Override
	public UUID getUUID()
	{
		return uuid;
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.csys.items.item.IItem#getWorldLocation()
	 */
	@Override
	public Vector2f getWorldLocation()
	{
		if (getParent() == null)
		{
			return getRelativeLocation();
		}
		getParent().localToWorld(getLocalTranslation(), out);
		return new Vector2f(out.x, out.y);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.csys.items.item.IItem#getWorldLocation(com.jme3.math.Vector2f
	 * )
	 */
	@Override
	public Vector2f getWorldLocation(Vector2f v)
	{
		if (getParent() == null)
		{
			return v;
		}
		getParent().localToWorld(new Vector3f(v.x, v.y, getLocalTranslation().z), out);
		return new Vector2f(out.x, out.y);
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.csys.items.item.IItem#getWorldRotationDegrees()
	 */
	@Override
	public float getWorldRotationDegrees()
	{
		getWorldRotation().toAngleAxis(tempWorldRotation);
		return tempWorldRotation.z;
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.csys.items.item.IItem#getZOrder()
	 */
	@Override
	public int getZOrder()
	{
		return this.zorder;
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.csys.items.item.IItem#getZOrderManager()
	 */
	@Override
	public IZOrderManager getZOrderManager()
	{
		return zOrderManager;
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.csys.items.INestable#hasChildren()
	 */
	@Override
	public boolean hasChildren()
	{
		return getItemChildren().size() > 0;
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.csys.items.item.IItem#isVisible()
	 */
	@Override
	public boolean isVisible()
	{
		return isVisible;
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.csys.items.INestable#registerChildrenChangedListener(
	 * multiplicity3.csys.items.INestable.IChildrenChangedListener)
	 */
	@Override
	public void registerChildrenChangedListener(IChildrenChangedListener listener)
	{
		if (!childrenChangedListeners.contains(listener))
		{
			childrenChangedListeners.add(listener);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.csys.items.INestable#removeAllItems(boolean)
	 */
	@Override
	public void removeAllItems(boolean recursive)
	{
		while (getItemChildren().size() > 0)
		{
			IItem item = getItemChildren().get(0);
			if (recursive)
			{
				item.removeAllItems(recursive);
			}
			removeItem(item);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.csys.items.INestable#removeItem(multiplicity3.csys.items
	 * .item.IItem)
	 */
	@Override
	public void removeItem(IItem item)
	{
		log.fine("Removing " + item + " from " + this);
		getItemChildren().remove(item);
		getZOrderManager().unregisterForZOrdering(item);
		item.setParentItem(null);
		notifyChildrenChanged();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.csys.items.item.IItem#removeItemListener(multiplicity3.
	 * csys.items.events.IItemListener)
	 */
	@Override
	public void removeItemListener(IItemListener itemListener)
	{
		getItemListeners().remove(itemListener);
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.csys.items.item.IItem#setInteractionEnabled(boolean)
	 */
	@Override
	public void setInteractionEnabled(boolean b)
	{
		this.dispatcher.setEnabled(b);
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.csys.items.item.IItem#setItemListeners(java.util.List)
	 */
	@Override
	public void setItemListeners(List<IItemListener> itemListeners)
	{
		this.itemListeners = itemListeners;
	}

	/**
	 * Sets the item name.
	 *
	 * @param itemName
	 *            the new item name
	 */
	public void setItemName(String itemName)
	{
		this.itemName = itemName;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.csys.items.item.IItem#setParentItem(multiplicity3.csys.
	 * items.item.IItem)
	 */
	@Override
	public void setParentItem(IItem parent)
	{
		log.fine(getManipulableSpatial() + " parent item changing to " + parent);
		this.removeFromParent();
		if (parent != null)
		{
			((JMEItem) parent).attachChild(this);
		}
		parentItem = parent;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.csys.items.item.IItem#setRelativeLocation(com.jme3.math
	 * .Vector2f)
	 */
	@Override
	public void setRelativeLocation(Vector2f newLoc)
	{
		setLocalTranslation(newLoc.x, newLoc.y, getLocalTranslation().z);
		updateGeometricState();
		for (IItemListener l : getItemListeners())
		{
			l.itemMoved(this);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.csys.items.item.IItem#setRelativeRotation(float)
	 */
	@Override
	public void setRelativeRotation(float angle)
	{
		this.relativeRotation = angle;
		trot.fromAngleAxis(angle, Vector3f.UNIT_Z);
		setLocalRotation(trot);
		updateGeometricState();
		for (IItemListener l : getItemListeners())
		{
			l.itemRotated(this);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.csys.items.item.IItem#setRelativeScale(float)
	 */
	@Override
	public void setRelativeScale(float scale)
	{
		setLocalScale(scale);
		for (IItemListener l : getItemListeners())
		{
			l.itemScaled(this);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.csys.items.item.IItem#setVisible(boolean)
	 */
	@Override
	public void setVisible(boolean isVisible)
	{
		if (isVisible == this.isVisible)
		{
			return;
		}

		if (isVisible)
		{
			this.setCullHint(CullHint.Never);
		}
		else
		{
			this.setCullHint(CullHint.Always);
		}

		this.isVisible = isVisible;

		for (IItemListener l : itemListeners)
		{
			l.itemVisibilityChanged(this, isVisible);
		}

		for (int i = 0; i < itemChildren.size(); i++)
		{
			itemChildren.get(i).setVisible(isVisible);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.csys.items.item.IItem#setWorldLocation(com.jme3.math.Vector2f
	 * )
	 */
	@Override
	public void setWorldLocation(Vector2f loc)
	{
		if (getParent() == null)
		{
			setRelativeLocation(new Vector2f(loc.x, loc.y));
		}
		else
		{
			Vector3f store = new Vector3f();
			getParent().worldToLocal(new Vector3f(loc.x, loc.y, getWorldTranslation().z), store);
			setRelativeLocation(new Vector2f(store.x, store.y));
		}
		updateGeometricState();
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.csys.items.item.IItem#setZOrder(int)
	 */
	@Override
	public void setZOrder(int zOrder)
	{
		this.zorder = zOrder;
		if ((getParent() != null) && (this.getParentItem() != null))
		{
			setLocalTranslation(getLocalTranslation().x, getLocalTranslation().y, zOrder - this.getParentItem().getZOrder());
		}
		else
		{
			Vector3f newZOrder = getLocalTranslation().clone();
			setLocalTranslation(newZOrder.x, newZOrder.y, zOrder);
		}
		updateGeometricState();
	}

	/*
	 * (non-Javadoc)
	 * @see com.jme3.scene.Spatial#toString()
	 */
	@Override
	public String toString()
	{
		return this.getClass().getName() + " (" + getItemName() + ")";
	}

	/**
	 * Notify children changed.
	 */
	private void notifyChildrenChanged()
	{
		for (IChildrenChangedListener l : childrenChangedListeners)
		{
			l.childrenChanged(this, itemChildren);
		}
	}
}
