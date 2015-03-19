package synergynet3.additionalitems.interfaces;

import synergynet3.audio.SNAudioController;
import multiplicity3.csys.items.item.IItem;

public interface IAudioContainer extends IItem {
	
	public SNAudioController getAudioController();
	public void setAudioController(SNAudioController info);

}
