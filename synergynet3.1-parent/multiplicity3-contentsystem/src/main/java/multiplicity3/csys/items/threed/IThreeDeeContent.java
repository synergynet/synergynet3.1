package multiplicity3.csys.items.threed;

import java.io.File;
import java.util.UUID;

import multiplicity3.csys.items.events.MultiTouchEventDispatcher;
import multiplicity3.csys.items.item.IItem;

import com.jme3.math.Vector3f;

/**
 * The Interface IThreeDeeContent.
 */
public interface IThreeDeeContent extends IItem {

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.csys.items.item.IItem#getMultiTouchDispatcher()
	 */
	public MultiTouchEventDispatcher getMultiTouchDispatcher();

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.csys.items.item.IItem#getUUID()
	 */
	public UUID getUUID();

	/**
	 * Gets the model.
	 *
	 * @return the model
	 */
	String getModel();

	/**
	 * Gets the size.
	 *
	 * @return the size
	 */
	Vector3f getSize();

	/**
	 * Gets the texture.
	 *
	 * @return the texture
	 */
	String getTexture();

	/**
	 * Sets the model.
	 *
	 * @param modelFile the new model
	 */
	void setModel(File modelFile);

	/**
	 * Sets the model.
	 *
	 * @param modelResource the new model
	 */
	void setModel(String modelResource);

	/**
	 * Sets the size.
	 *
	 * @param width the width
	 * @param height the height
	 * @param depth the depth
	 */
	void setSize(float width, float height, float depth);

	/**
	 * Sets the texture.
	 *
	 * @param textureFile the new texture
	 */
	void setTexture(File textureFile);

	/**
	 * Sets the texture.
	 *
	 * @param textureResource the new texture
	 */
	void setTexture(String textureResource);
}