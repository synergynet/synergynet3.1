package synergynet3.additionalitems.interfaces;

import multiplicity3.csys.items.item.IItem;
import synergynet3.audio.SNAudioController;

/**
 * The Interface IAudioContainer.
 */
public interface IAudioContainer extends IItem {

	/**
	 * Gets the audio controller.
	 *
	 * @return the audio controller
	 */
	public SNAudioController getAudioController();

	/**
	 * Sets the audio controller.
	 *
	 * @param info the new audio controller
	 */
	public void setAudioController(SNAudioController info);

}
