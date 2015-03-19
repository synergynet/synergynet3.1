package synergynet3.additionalitems.interfaces;

import java.io.File;

import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;

import multiplicity3.csys.items.item.IItem;

public interface ISimpleMediaPlayer extends IItem {
	public void setLocalResource(File file, boolean autostart);	
	public void setLocalResource(String localPath, boolean autostart);	
	public void setRemoteResource(String remotePath, boolean autostart);
	public void setSize(float width, float height);
	public void play();
	public void pause();
	public void unpause();
	public void destroy();	
    public boolean isPlaying();
    public float getPosition();
    public void setPosition(float pos);
    public float getWidth();
    public float getHeight();
    public void setRepeat(boolean repeat);
    public boolean getRepeat();
    public void addMediaPlayerEventListener(MediaPlayerEventAdapter mediaPlayerEventAdapter);
    public void setActionOnVideoEndListener(IActionOnVideoEndListener actionOnVideoEndListener);
}
