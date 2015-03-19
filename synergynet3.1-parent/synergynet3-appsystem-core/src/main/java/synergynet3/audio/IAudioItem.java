package synergynet3.audio;

import com.jme3.math.ColorRGBA;

public interface IAudioItem {	
	
	public void setOwner(String owner);	
	public String getOwner();	
	public void setBackgroundColour(ColorRGBA colour);
	public void stopPlay();
	public void setAudioControlObject(SNAudioController audioControl);
	public void stopRecord(boolean success);
	public int getWidth();
	public int getHeight();
	void makeImmovable();

}
