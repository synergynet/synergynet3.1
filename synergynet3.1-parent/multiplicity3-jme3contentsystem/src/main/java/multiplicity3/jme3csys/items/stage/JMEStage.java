package multiplicity3.jme3csys.items.stage;

import java.util.UUID;

import multiplicity3.csys.animation.AnimationSystem;
import multiplicity3.csys.annotations.ImplementsContentItem;
import multiplicity3.csys.behaviours.BehaviourMaker;
import multiplicity3.csys.draganddrop.DragAndDropSystem;
import multiplicity3.csys.factory.IContentFactory;
import multiplicity3.csys.picksystem.IPickSystem;
import multiplicity3.csys.stage.IStage;
import multiplicity3.jme3csys.items.IInitable;
import multiplicity3.jme3csys.items.item.JMEItem;

import com.jme3.asset.AssetManager;
import com.jme3.math.Vector2f;
import com.jme3.scene.Spatial;

/**
 * The Class JMEStage.
 */
@ImplementsContentItem(target = IStage.class)
public class JMEStage extends JMEItem implements IStage, IInitable {

	/** The animation system. */
	private AnimationSystem animationSystem;

	/** The behaviour maker. */
	private BehaviourMaker behaviourMaker;

	/** The content factory. */
	private IContentFactory contentFactory;

	/** The drag and drop system. */
	private DragAndDropSystem dragAndDropSystem;

	/** The height. */
	private int height;

	/** The local stage. */
	private IStage localStage;

	/** The pick system. */
	private IPickSystem pickSystem;

	/** The width. */
	private int width;

	/**
	 * Instantiates a new JME stage.
	 *
	 * @param name the name
	 * @param uuid the uuid
	 */
	public JMEStage(String name, UUID uuid) {
		super(name, uuid);
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.csys.stage.IStage#getAnimationSystem()
	 */
	public AnimationSystem getAnimationSystem() {
		return animationSystem;
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.csys.stage.IStage#getBehaviourMaker()
	 */
	public BehaviourMaker getBehaviourMaker() {
		return behaviourMaker;
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.csys.stage.IStage#getContentFactory()
	 */
	public IContentFactory getContentFactory() {
		return this.contentFactory;
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.csys.stage.IStage#getDisplayHeight()
	 */
	public int getDisplayHeight() {
		return height;
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.csys.stage.IStage#getDisplayWidth()
	 */
	public int getDisplayWidth() {
		return width;
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.csys.stage.IStage#getDragAndDropSystem()
	 */
	public DragAndDropSystem getDragAndDropSystem() {
		return dragAndDropSystem;
	}

	/**
	 * Gets the local stage.
	 *
	 * @return the local stage
	 */
	public IStage getLocalStage() {
		return localStage;
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.csys.items.item.IItem#getManipulableSpatial()
	 */
	@Override
	public Spatial getManipulableSpatial() {
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.csys.stage.IStage#getPickSystem()
	 */
	public IPickSystem getPickSystem() {
		return pickSystem;
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.csys.stage.IStage#getScreenBottom()
	 */
	public float getScreenBottom() {
		return -getDisplayHeight() / 2;
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.csys.stage.IStage#getScreenLeft()
	 */
	public float getScreenLeft() {
		return -getDisplayWidth() / 2;
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.csys.stage.IStage#getScreenRight()
	 */
	public float getScreenRight() {
		return getDisplayWidth() / 2;
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.csys.stage.IStage#getScreenTop()
	 */
	public float getScreenTop() {
		return getDisplayHeight() / 2;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.jme3csys.items.IInitable#initializeGeometry(com.jme3.asset
	 * .AssetManager)
	 */
	@Override
	public void initializeGeometry(AssetManager assetManager) {
		// no geometry to initialize
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.csys.stage.IStage#isLocal()
	 */
	@Override
	public boolean isLocal() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.csys.stage.IStage#screenToWorld(com.jme3.math.Vector2f)
	 */
	@Override
	public Vector2f screenToWorld(Vector2f pos) {
		return pos;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.csys.stage.IStage#setAnimationSystem(multiplicity3.csys
	 * .animation.AnimationSystem)
	 */
	public void setAnimationSystem(AnimationSystem animationSystem) {
		this.animationSystem = animationSystem;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.csys.stage.IStage#setBehaviourMaker(multiplicity3.csys.
	 * behaviours.BehaviourMaker)
	 */
	public void setBehaviourMaker(BehaviourMaker bm) {
		this.behaviourMaker = bm;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.csys.stage.IStage#setContentFactory(multiplicity3.csys.
	 * factory.IContentFactory)
	 */
	public void setContentFactory(IContentFactory contentFactory) {
		this.contentFactory = contentFactory;
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.csys.stage.IStage#setDisplayDimensions(int, int)
	 */
	public void setDisplayDimensions(int width, int height) {
		this.width = width;
		this.height = height;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.csys.stage.IStage#setDragAndDropSystem(multiplicity3.csys
	 * .draganddrop.DragAndDropSystem)
	 */
	public void setDragAndDropSystem(DragAndDropSystem dragAndDropSystem) {
		this.dragAndDropSystem = dragAndDropSystem;
	}

	/**
	 * Sets the local stage.
	 *
	 * @param stage the new local stage
	 */
	public void setLocalStage(IStage stage) {
		this.localStage = stage;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.csys.stage.IStage#setPickSystem(multiplicity3.csys.picksystem
	 * .IPickSystem)
	 */
	public void setPickSystem(IPickSystem pickSystem) {
		this.pickSystem = pickSystem;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.csys.stage.IStage#tableToScreen(com.jme3.math.Vector2f,
	 * com.jme3.math.Vector2f)
	 */
	public void tableToScreen(Vector2f in, Vector2f out) {
		out.x = (width * in.x);
		out.y = (height * in.y);
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.csys.stage.IStage#tableToWorld(com.jme3.math.Vector2f)
	 */
	public Vector2f tableToWorld(Vector2f in) {
		return new Vector2f((width * in.x), (height * in.y));
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.csys.stage.IStage#tableToWorld(com.jme3.math.Vector2f,
	 * com.jme3.math.Vector2f)
	 */
	public void tableToWorld(Vector2f in, Vector2f out) {
		out.x = width * in.x;
		out.y = height * in.y;
	}

}
