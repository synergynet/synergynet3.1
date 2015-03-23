package synergynet3.additionalitems.interfaces;

import java.io.File;

import multiplicity3.csys.items.item.IItem;

/**
 * The Interface IAudioRecorder.
 */
public interface IAudioRecorder extends IItem {

	/**
	 * Gets the audio file.
	 *
	 * @return the audio file
	 */
	public File getAudioFile();

	/**
	 * Checks for recorded.
	 *
	 * @return true, if successful
	 */
	public boolean hasRecorded();

	/**
	 * Make immovable.
	 */
	public void makeImmovable();

	/**
	 * Removes the border.
	 */
	public void removeBorder();

}
