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

@ImplementsContentItem(target = IStage.class)
public class JMEStage extends JMEItem implements IStage, IInitable {
	private IContentFactory contentFactory;
	private AnimationSystem animationSystem;
	private DragAndDropSystem dragAndDropSystem;
	private IPickSystem pickSystem;
	private IStage localStage;
	private BehaviourMaker behaviourMaker;

	private int width;
	private int height;

	public JMEStage(String name, UUID uuid) {
		super(name, uuid);
	}

	@Override
	public boolean isLocal() {	
		return true;
	}

	@Override
	public Spatial getManipulableSpatial() {
		return this;
	}

	@Override
	public void initializeGeometry(AssetManager assetManager) {
		// no geometry to initialize
	}
	
	public void setContentFactory(IContentFactory contentFactory) {
		this.contentFactory = contentFactory;
	}
	
	public IContentFactory getContentFactory() {
		return this.contentFactory;
	}

	public void setAnimationSystem(AnimationSystem animationSystem) {
		this.animationSystem = animationSystem;
	}

	public AnimationSystem getAnimationSystem() {
		return animationSystem;
	}

	public void setDragAndDropSystem(DragAndDropSystem dragAndDropSystem) {
		this.dragAndDropSystem = dragAndDropSystem;
	}

	public DragAndDropSystem getDragAndDropSystem() {
		return dragAndDropSystem;
	}

	public void setPickSystem(IPickSystem pickSystem) {
		this.pickSystem = pickSystem;
	}

	public IPickSystem getPickSystem() {
		return pickSystem;
	}

	public void setLocalStage(IStage stage) {
		this.localStage = stage;		
	}
	
	public IStage getLocalStage() {
		return localStage;
	}
	
	public void setBehaviourMaker(BehaviourMaker bm) {
		this.behaviourMaker = bm;
	}

	public BehaviourMaker getBehaviourMaker() {
		return behaviourMaker;
	}

	
	public void setDisplayDimensions(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public void tableToScreen(Vector2f in, Vector2f out) {
		out.x = (width * in.x);
		out.y = (height * in.y);
	}
	
	public Vector2f tableToWorld(Vector2f in) {
		return new Vector2f((width * in.x), (height * in.y));
	}
	
	public void tableToWorld(Vector2f in, Vector2f out) {
		out.x = width * in.x;
		out.y = height * in.y;
	}	
	
	public int getDisplayWidth() {
		return width;
	}

	public int getDisplayHeight() {
		return height;
	}

	public float getScreenLeft() {
		return -getDisplayWidth()/2;
	}
	
	public float getScreenRight() {
		return getDisplayWidth()/2;
	}
	
	public float getScreenTop() {
		return getDisplayHeight()/2;
	}
	
	public float getScreenBottom() {
		return -getDisplayHeight()/2;
	}

	@Override
	public Vector2f screenToWorld(Vector2f pos) {
		return pos;
	}
	
}
