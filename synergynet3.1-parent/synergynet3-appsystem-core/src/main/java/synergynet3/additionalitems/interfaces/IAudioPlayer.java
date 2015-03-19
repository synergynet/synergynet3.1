package synergynet3.additionalitems.interfaces;

import java.io.File;

import multiplicity3.csys.items.item.IItem;

public interface IAudioPlayer extends IItem {

	public void setAudioRecording(File recording);
	public void makeMovable();
	public int getWidth();
	public int getHeight();
	
}
