package synergynet3.additionalitems.interfaces;

import java.io.File;

import multiplicity3.csys.items.item.IItem;

public interface IAudioRecorder extends IItem {
	
	public boolean hasRecorded();
	public void makeImmovable();
	public File getAudioFile();
	public void removeBorder();
	
}
