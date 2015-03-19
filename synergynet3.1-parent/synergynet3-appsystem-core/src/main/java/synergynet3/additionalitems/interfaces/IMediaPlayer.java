package synergynet3.additionalitems.interfaces;

import java.io.File;

import com.jme3.math.ColorRGBA;

import multiplicity3.csys.items.item.IItem;
import multiplicity3.csys.stage.IStage;

public interface IMediaPlayer extends IItem {
	public void setLocalResource(File file, boolean autostart, boolean repeat, IStage stage);	
	public void setLocalResource(String localPath, boolean autostart, boolean repeat, IStage stage);	
	public void setRemoteResource(String remotePath, boolean autostart, boolean repeat, IStage stage);
	public void setSize(float width, float height);
	public void destroy();	
	public void pause();	
	public void setPosition(float pos);
	public void setBorderColour(ColorRGBA borderColour);
	public void setDeceleration(float deceleration);
	public void setScaleLimits(float minScale, float maxScale);
	public void setRepeated(boolean repeated);
	public boolean isRepeated();
	public void setBackgroundColour(ColorRGBA backgroundColour);
}
