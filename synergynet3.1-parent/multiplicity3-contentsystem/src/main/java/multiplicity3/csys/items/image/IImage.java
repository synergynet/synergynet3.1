package multiplicity3.csys.items.image;

import java.io.File;

import multiplicity3.csys.items.shapes.IRectangularItem;

import com.jme3.texture.Texture;

/**
 * The Interface IImage.
 */
public interface IImage extends IRectangularItem
{

	/**
	 * Gets the image.
	 *
	 * @return the image
	 */
	public String getImage();

	/**
	 * Sets the image.
	 *
	 * @param imageFile
	 *            the new image
	 */
	public void setImage(File imageFile);

	/**
	 * Sets the image.
	 *
	 * @param imageResoure
	 *            the new image
	 */
	public void setImage(String imageResoure);

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.csys.items.shapes.IRectangularItem#setSize(float,
	 * float)
	 */
	@Override
	public void setSize(float width, float height);

	/**
	 * Sets the texture.
	 *
	 * @param tex
	 *            the new texture
	 */
	public void setTexture(Texture tex);

	/**
	 * Sets the wrapping.
	 *
	 * @param xscale
	 *            the xscale
	 * @param yscale
	 *            the yscale
	 */
	public void setWrapping(float xscale, float yscale);
}
