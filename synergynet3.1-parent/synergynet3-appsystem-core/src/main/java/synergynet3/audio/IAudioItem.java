package synergynet3.audio;

import com.jme3.math.ColorRGBA;

/**
 * The Interface IAudioItem.
 */
public interface IAudioItem {

	/**
	 * Gets the height.
	 *
	 * @return the height
	 */
	public int getHeight();

	/**
	 * Gets the owner.
	 *
	 * @return the owner
	 */
	public String getOwner();

	/**
	 * Gets the width.
	 *
	 * @return the width
	 */
	public int getWidth();

	/**
	 * Sets the audio control object.
	 *
	 * @param audioControl the new audio control object
	 */
	public void setAudioControlObject(SNAudioController audioControl);

	/**
	 * Sets the background colour.
	 *
	 * @param colour the new background colour
	 */
	public void setBackgroundColour(ColorRGBA colour);

	/**
	 * Sets the owner.
	 *
	 * @param owner the new owner
	 */
	public void setOwner(String owner);

	/**
	 * Stop play.
	 */
	public void stopPlay();

	/**
	 * Stop record.
	 *
	 * @param success the success
	 */
	public void stopRecord(boolean success);

	/**
	 * Make immovable.
	 */
	void makeImmovable();

}
