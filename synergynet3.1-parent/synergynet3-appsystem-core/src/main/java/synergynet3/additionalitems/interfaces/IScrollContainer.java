package synergynet3.additionalitems.interfaces;

import java.util.logging.Logger;

import multiplicity3.csys.items.border.IRoundedBorder;
import multiplicity3.csys.items.item.IItem;
import multiplicity3.csys.items.shapes.IColourRectangle;
import multiplicity3.csys.stage.IStage;
import multiplicity3.input.MultiTouchEventAdapter;

import com.jme3.math.ColorRGBA;

/**
 * The Interface IScrollContainer.
 */
public interface IScrollContainer extends IItem
{

	/**
	 * Adds the frame.
	 *
	 * @return the int
	 */
	public int addFrame();

	/**
	 * Adds the listener to arrows.
	 *
	 * @param multiTouchEventAdapter
	 *            the multi touch event adapter
	 */
	public void addListenerToArrows(MultiTouchEventAdapter multiTouchEventAdapter);

	/**
	 * Adds the to all frames.
	 *
	 * @param userIcon
	 *            the user icon
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 */
	public void addToAllFrames(IItem userIcon, int x, int y);

	/**
	 * Adds the to frame.
	 *
	 * @param audioRecorder
	 *            the audio recorder
	 * @param frame
	 *            the frame
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 */
	public void addToFrame(IItem audioRecorder, int frame, int x, int y);

	/**
	 * Gets the background.
	 *
	 * @return the background
	 */
	public IColourRectangle getBackground();

	/**
	 * Gets the border.
	 *
	 * @return the border
	 */
	public IRoundedBorder getBorder();

	/**
	 * Gets the current frame.
	 *
	 * @return the current frame
	 */
	public int getCurrentFrame();

	/**
	 * Gets the height.
	 *
	 * @return the height
	 */
	public int getHeight();

	/**
	 * Gets the width.
	 *
	 * @return the width
	 */
	public int getWidth();

	/**
	 * Hide scroll buttons.
	 */
	public void hideScrollButtons();

	/**
	 * Removes the frame.
	 *
	 * @param toRemove
	 *            the to remove
	 */
	public void removeFrame(int toRemove);

	/**
	 * Scroll to frame.
	 *
	 * @param frame
	 *            the frame
	 */
	public void scrollToFrame(int frame);

	/**
	 * Sets the active.
	 *
	 * @param active
	 *            the new active
	 */
	public void setActive(boolean active);

	/**
	 * Sets the arrow height override.
	 *
	 * @param newHeight
	 *            the new arrow height override
	 */
	public void setArrowHeightOverride(float newHeight);

	/**
	 * Sets the arrow width override.
	 *
	 * @param width
	 *            the new arrow width override
	 */
	public void setArrowWidthOverride(float width);

	/**
	 * Sets the arrow y override.
	 *
	 * @param newY
	 *            the new arrow y override
	 */
	public void setArrowYOverride(float newY);

	/**
	 * Sets the dimensions.
	 *
	 * @param stage
	 *            the stage
	 * @param log
	 *            the log
	 * @param width
	 *            the width
	 * @param height
	 *            the height
	 */
	public void setDimensions(IStage stage, Logger log, int width, int height);

	/**
	 * Sets the frame colour.
	 *
	 * @param studentColour
	 *            the new frame colour
	 */
	public void setFrameColour(ColorRGBA studentColour);

	/**
	 * Sets the visibility.
	 *
	 * @param isVisible
	 *            the new visibility
	 */
	public void setVisibility(final Boolean isVisible);

	/**
	 * Show current frame contents.
	 */
	public void showCurrentFrameContents();

}
