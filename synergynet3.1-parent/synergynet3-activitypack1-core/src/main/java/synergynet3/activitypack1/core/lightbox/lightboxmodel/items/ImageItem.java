package synergynet3.activitypack1.core.lightbox.lightboxmodel.items;

import java.awt.geom.Point2D.Float;
import java.io.Serializable;

/**
 * The Class ImageItem.
 */
public class ImageItem extends LightBoxItem implements Serializable
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 2815846342790448429L;

	/** The image file name. */
	private String imageFileName;

	/** The image size. */
	private Float imageSize;

	/**
	 * Gets the image file name.
	 *
	 * @return the image file name
	 */
	public String getImageFileName()
	{
		return this.imageFileName;
	}

	/**
	 * Gets the size.
	 *
	 * @return the size
	 */
	public Float getSize()
	{
		return this.imageSize;
	}

	/**
	 * Sets the image file name.
	 *
	 * @param imageFileName
	 *            the new image file name
	 */
	public void setImageFileName(String imageFileName)
	{
		this.imageFileName = imageFileName;
	}

	/**
	 * Sets the size.
	 *
	 * @param imageSize
	 *            the new size
	 */
	public void setSize(Float imageSize)
	{
		this.imageSize = imageSize;
	}
}
