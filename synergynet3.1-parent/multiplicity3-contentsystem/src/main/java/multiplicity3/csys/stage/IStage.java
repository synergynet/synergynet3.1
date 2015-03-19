package multiplicity3.csys.stage;

import com.jme3.math.Vector2f;

import multiplicity3.csys.animation.AnimationSystem;
import multiplicity3.csys.behaviours.BehaviourMaker;
import multiplicity3.csys.draganddrop.DragAndDropSystem;
import multiplicity3.csys.factory.IContentFactory;
import multiplicity3.csys.items.container.IContainer;
import multiplicity3.csys.picksystem.IPickSystem;

public interface IStage extends IContainer {
	public boolean isLocal();
	public void setContentFactory(IContentFactory contentFactory);	
	public IContentFactory getContentFactory();
	public void setAnimationSystem(AnimationSystem animationSystem);
	public AnimationSystem getAnimationSystem();
	public void setDragAndDropSystem(DragAndDropSystem dragAndDropSystem);
	public DragAndDropSystem getDragAndDropSystem();
	public void setPickSystem(IPickSystem pickSystem);
	public IPickSystem getPickSystem();
	public void setBehaviourMaker(BehaviourMaker bm);
	public BehaviourMaker getBehaviourMaker();
	public void setDisplayDimensions(int width, int height);
	public void tableToScreen(Vector2f in, Vector2f out);
	public Vector2f tableToWorld(Vector2f in);
	public void tableToWorld(Vector2f in, Vector2f out);
	public int getDisplayWidth();
	public int getDisplayHeight();
	public float getScreenLeft();
	public float getScreenRight();
	public float getScreenTop();
	public float getScreenBottom();
	public Vector2f screenToWorld(Vector2f newScreenPosition);
}
