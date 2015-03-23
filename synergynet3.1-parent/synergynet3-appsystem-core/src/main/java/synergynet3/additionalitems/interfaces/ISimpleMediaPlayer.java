package synergynet3.additionalitems.interfaces;

import java.io.File;

import multiplicity3.csys.items.item.IItem;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;

/**
 * The Interface ISimpleMediaPlayer.
 */
public interface ISimpleMediaPlayer extends IItem {

	/**
	 * Adds the media player event listener.
	 *
	 * @param mediaPlayerEventAdapter the media player event adapter
	 */
	public void addMediaPlayerEventListener(
			MediaPlayerEventAdapter mediaPlayerEventAdapter);

	/**
	 * Destroy.
	 */
	public void destroy();

	/**
	 * Gets the height.
	 *
	 * @return the height
	 */
	public float getHeight();

	/**
	 * Gets the position.
	 *
	 * @return the position
	 */
	public float getPosition();

	/**
	 * Gets the repeat.
	 *
	 * @return the repeat
	 */
	public boolean getRepeat();

	/**
	 * Gets the width.
	 *
	 * @return the width
	 */
	public float getWidth();

	/**
	 * Checks if is playing.
	 *
	 * @return true, if is playing
	 */
	public boolean isPlaying();

	/**
	 * Pause.
	 */
	public void pause();

	/**
	 * Play.
	 */
	public void play();

	/**
	 * Sets the action on video end listener.
	 *
	 * @param actionOnVideoEndListener the new action on video end listener
	 */
	public void setActionOnVideoEndListener(
			IActionOnVideoEndListener actionOnVideoEndListener);

	/**
	 * Sets the local resource.
	 *
	 * @param file the file
	 * @param autostart the autostart
	 */
	public void setLocalResource(File file, boolean autostart);

	/**
	 * Sets the local resource.
	 *
	 * @param localPath the local path
	 * @param autostart the autostart
	 */
	public void setLocalResource(String localPath, boolean autostart);

	/**
	 * Sets the position.
	 *
	 * @param pos the new position
	 */
	public void setPosition(float pos);

	/**
	 * Sets the remote resource.
	 *
	 * @param remotePath the remote path
	 * @param autostart the autostart
	 */
	public void setRemoteResource(String remotePath, boolean autostart);

	/**
	 * Sets the repeat.
	 *
	 * @param repeat the new repeat
	 */
	public void setRepeat(boolean repeat);

	/**
	 * Sets the size.
	 *
	 * @param width the width
	 * @param height the height
	 */
	public void setSize(float width, float height);

	/**
	 * Unpause.
	 */
	public void unpause();
}
