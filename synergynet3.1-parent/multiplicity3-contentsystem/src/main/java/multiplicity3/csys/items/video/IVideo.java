package multiplicity3.csys.items.video;

import multiplicity3.csys.items.item.IItem;
import multiplicity3.csys.items.video.exceptions.CouldNotPlayVideoException;

public interface IVideo extends IItem {
	public void setSize(float width, float height);
	public void setResource(String resource);
	public void startPlaying() throws CouldNotPlayVideoException;
	public void stopPlaying();
	boolean isPlaying();
}
