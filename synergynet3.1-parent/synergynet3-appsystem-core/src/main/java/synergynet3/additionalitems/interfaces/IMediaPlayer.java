package synergynet3.additionalitems.interfaces;

import java.io.File;

import multiplicity3.csys.items.item.IItem;
import multiplicity3.csys.stage.IStage;

import com.jme3.math.ColorRGBA;

/**
 * The Interface IMediaPlayer.
 */
public interface IMediaPlayer extends IItem
{

	/**
	 * Destroy.
	 */
	public void destroy();

	/**
	 * Checks if is repeated.
	 *
	 * @return true, if is repeated
	 */
	public boolean isRepeated();

	/**
	 * Pause.
	 */
	public void pause();

	/**
	 * Sets the background colour.
	 *
	 * @param backgroundColour
	 *            the new background colour
	 */
	public void setBackgroundColour(ColorRGBA backgroundColour);

	/**
	 * Sets the border colour.
	 *
	 * @param borderColour
	 *            the new border colour
	 */
	public void setBorderColour(ColorRGBA borderColour);

	/**
	 * Sets the deceleration.
	 *
	 * @param deceleration
	 *            the new deceleration
	 */
	public void setDeceleration(float deceleration);

	/**
	 * Sets the local resource.
	 *
	 * @param file
	 *            the file
	 * @param autostart
	 *            the autostart
	 * @param repeat
	 *            the repeat
	 * @param stage
	 *            the stage
	 */
	public void setLocalResource(File file, boolean autostart, boolean repeat, IStage stage);

	/**
	 * Sets the local resource.
	 *
	 * @param localPath
	 *            the local path
	 * @param autostart
	 *            the autostart
	 * @param repeat
	 *            the repeat
	 * @param stage
	 *            the stage
	 */
	public void setLocalResource(String localPath, boolean autostart, boolean repeat, IStage stage);

	/**
	 * Sets the position.
	 *
	 * @param pos
	 *            the new position
	 */
	public void setPosition(float pos);

	/**
	 * Sets the remote resource.
	 *
	 * @param remotePath
	 *            the remote path
	 * @param autostart
	 *            the autostart
	 * @param repeat
	 *            the repeat
	 * @param stage
	 *            the stage
	 */
	public void setRemoteResource(String remotePath, boolean autostart, boolean repeat, IStage stage);

	/**
	 * Sets the repeated.
	 *
	 * @param repeated
	 *            the new repeated
	 */
	public void setRepeated(boolean repeated);

	/**
	 * Sets the scale limits.
	 *
	 * @param minScale
	 *            the min scale
	 * @param maxScale
	 *            the max scale
	 */
	public void setScaleLimits(float minScale, float maxScale);

	/**
	 * Sets the size.
	 *
	 * @param width
	 *            the width
	 * @param height
	 *            the height
	 */
	public void setSize(float width, float height);
}
