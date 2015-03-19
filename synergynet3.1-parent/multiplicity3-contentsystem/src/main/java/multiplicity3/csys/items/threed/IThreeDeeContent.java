package multiplicity3.csys.items.threed;

import java.io.File;
import java.util.UUID;

import multiplicity3.csys.items.events.MultiTouchEventDispatcher;
import multiplicity3.csys.items.item.IItem;

import com.jme3.math.Vector3f;

public interface IThreeDeeContent extends IItem {
	public UUID getUUID();
	public MultiTouchEventDispatcher getMultiTouchDispatcher();
	void setModel(File modelFile);
	void setModel(String modelResource);
	void setSize(float width, float height, float depth);
	Vector3f getSize();
	String getModel();
	void setTexture(File textureFile);
	void setTexture(String textureResource);
	String getTexture();
}