package multiplicity3.csys.stage;

import multiplicity3.csys.animation.AnimationSystem;
import multiplicity3.csys.behaviours.BehaviourMaker;
import multiplicity3.csys.draganddrop.DragAndDropSystem;
import multiplicity3.csys.factory.IContentFactory;
import multiplicity3.csys.items.container.IContainer;
import multiplicity3.csys.picksystem.IPickSystem;

import com.jme3.math.Vector2f;

/**
 * The Interface IStage.
 */
public interface IStage extends IContainer {

	/**
	 * Gets the animation system.
	 *
	 * @return the animation system
	 */
	public AnimationSystem getAnimationSystem();

	/**
	 * Gets the behaviour maker.
	 *
	 * @return the behaviour maker
	 */
	public BehaviourMaker getBehaviourMaker();

	/**
	 * Gets the content factory.
	 *
	 * @return the content factory
	 */
	public IContentFactory getContentFactory();

	/**
	 * Gets the display height.
	 *
	 * @return the display height
	 */
	public int getDisplayHeight();

	/**
	 * Gets the display width.
	 *
	 * @return the display width
	 */
	public int getDisplayWidth();

	/**
	 * Gets the drag and drop system.
	 *
	 * @return the drag and drop system
	 */
	public DragAndDropSystem getDragAndDropSystem();

	/**
	 * Gets the pick system.
	 *
	 * @return the pick system
	 */
	public IPickSystem getPickSystem();

	/**
	 * Gets the screen bottom.
	 *
	 * @return the screen bottom
	 */
	public float getScreenBottom();

	/**
	 * Gets the screen left.
	 *
	 * @return the screen left
	 */
	public float getScreenLeft();

	/**
	 * Gets the screen right.
	 *
	 * @return the screen right
	 */
	public float getScreenRight();

	/**
	 * Gets the screen top.
	 *
	 * @return the screen top
	 */
	public float getScreenTop();

	/**
	 * Checks if is local.
	 *
	 * @return true, if is local
	 */
	public boolean isLocal();

	/**
	 * Screen to world.
	 *
	 * @param newScreenPosition the new screen position
	 * @return the vector2f
	 */
	public Vector2f screenToWorld(Vector2f newScreenPosition);

	/**
	 * Sets the animation system.
	 *
	 * @param animationSystem the new animation system
	 */
	public void setAnimationSystem(AnimationSystem animationSystem);

	/**
	 * Sets the behaviour maker.
	 *
	 * @param bm the new behaviour maker
	 */
	public void setBehaviourMaker(BehaviourMaker bm);

	/**
	 * Sets the content factory.
	 *
	 * @param contentFactory the new content factory
	 */
	public void setContentFactory(IContentFactory contentFactory);

	/**
	 * Sets the display dimensions.
	 *
	 * @param width the width
	 * @param height the height
	 */
	public void setDisplayDimensions(int width, int height);

	/**
	 * Sets the drag and drop system.
	 *
	 * @param dragAndDropSystem the new drag and drop system
	 */
	public void setDragAndDropSystem(DragAndDropSystem dragAndDropSystem);

	/**
	 * Sets the pick system.
	 *
	 * @param pickSystem the new pick system
	 */
	public void setPickSystem(IPickSystem pickSystem);

	/**
	 * Table to screen.
	 *
	 * @param in the in
	 * @param out the out
	 */
	public void tableToScreen(Vector2f in, Vector2f out);

	/**
	 * Table to world.
	 *
	 * @param in the in
	 * @return the vector2f
	 */
	public Vector2f tableToWorld(Vector2f in);

	/**
	 * Table to world.
	 *
	 * @param in the in
	 * @param out the out
	 */
	public void tableToWorld(Vector2f in, Vector2f out);
}
