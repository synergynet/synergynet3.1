package synergynet3.additionalitems.interfaces;

import java.io.File;

import multiplicity3.csys.items.item.IItem;

/**
 * The Interface IAudioPlayer.
 */
public interface IAudioPlayer extends IItem {

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
	 * Make movable.
	 */
	public void makeMovable();

	/**
	 * Sets the audio recording.
	 *
	 * @param recording the new audio recording
	 */
	public void setAudioRecording(File recording);

}
