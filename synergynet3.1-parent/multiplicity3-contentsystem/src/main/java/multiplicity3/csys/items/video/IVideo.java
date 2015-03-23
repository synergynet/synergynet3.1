package multiplicity3.csys.items.video;

import multiplicity3.csys.items.item.IItem;
import multiplicity3.csys.items.video.exceptions.CouldNotPlayVideoException;

/**
 * The Interface IVideo.
 */
public interface IVideo extends IItem {

	/**
	 * Sets the resource.
	 *
	 * @param resource the new resource
	 */
	public void setResource(String resource);

	/**
	 * Sets the size.
	 *
	 * @param width the width
	 * @param height the height
	 */
	public void setSize(float width, float height);

	/**
	 * Start playing.
	 *
	 * @throws CouldNotPlayVideoException the could not play video exception
	 */
	public void startPlaying() throws CouldNotPlayVideoException;

	/**
	 * Stop playing.
	 */
	public void stopPlaying();

	/**
	 * Checks if is playing.
	 *
	 * @return true, if is playing
	 */
	boolean isPlaying();
}
