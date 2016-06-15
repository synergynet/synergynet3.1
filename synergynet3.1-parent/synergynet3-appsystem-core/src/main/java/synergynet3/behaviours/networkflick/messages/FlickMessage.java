package synergynet3.behaviours.networkflick.messages;

import java.io.Serializable;

import synergynet3.databasemanagement.GalleryItemDatabaseFormat;
import synergynet3.web.shared.messages.PerformActionMessage;

/**
 * Structured message representing the item, its target and trajectory when
 * transferred through a network flick.
 */
public class FlickMessage extends PerformActionMessage implements Serializable
{

	/** Unique ID for serialisation. */
	private static final long serialVersionUID = 5350752882462365142L;

	/**
	 * Object representing the flicked item and its attached feedback stored in
	 * a serialisable form.
	 */
	private GalleryItemDatabaseFormat galleryItem;

	/** Rotation of the flicked item on arrival. */
	private float rotation;

	/** Scale of the flicked item on arrival. */
	private float scale;

	/** Cluster ID of the source device. */
	private String sourceTableID;

	/** Cluster ID of the target device. */
	private String targetTableID;

	/** The arrival location of the flicked item. */
	private float x, y;

	/** Direction of the flicked item on arrival */
	private float xDir, yDir;

	/**
	 * Empty Constructor used to initialise value in the network cluster.
	 */
	public FlickMessage()
	{
		super();
	}

	/**
	 * Create a structured network flick message for transmission through the
	 * cluster.
	 *
	 * @param targetTableIdentity
	 *            Target table's identity in the cluster.
	 * @param galleryItem
	 *            Object representing the flicked item and its attached
	 *            feedback.
	 * @param x
	 *            The X value of the flicked item's arrival location.
	 * @param y
	 *            The Y value of the flicked item's arrival location.
	 * @param targetAngle
	 *            Rotation of the flicked item on arrival.
	 * @param targetScale
	 *            Scale of the flicked item on arrival.
	 * @param xDir
	 *            The X value of the flicked item's direction on arrival.
	 * @param yDir
	 *            The Y value of the flicked item's direction on arrival.
	 * @param flickID
	 *            The unique ID of the flick message.
	 */
	public FlickMessage(String targetTableID, String sourceTableID, GalleryItemDatabaseFormat galleryItem, float x, float y, float targetAngle, float targetScale, float xDir, float yDir)
	{
		super(MESSAGESTATE.ACTIVATE);
		this.targetTableID = targetTableID;
		this.sourceTableID = sourceTableID;
		this.galleryItem = galleryItem;
		this.x = x;
		this.y = y;
		this.rotation = targetAngle;
		this.scale = targetScale;
		this.xDir = xDir;
		this.yDir = yDir;
	}

	/**
	 * Get the object representing the flicked item and its attached feedback.
	 *
	 * @return Object representing the flicked item and its attached feedback.
	 */
	public GalleryItemDatabaseFormat getGalleryItem()
	{
		return galleryItem;
	}

	/**
	 * Get the rotation of the flicked item on arrival.
	 *
	 * @return The rotation of the flicked item on arrival.
	 */
	public float getRotation()
	{
		return rotation;
	}

	/**
	 * Get the scale of the flicked item on arrival.
	 *
	 * @return The scale of the flicked item on arrival.
	 */
	public float getScale()
	{
		return scale;
	}

	/**
	 * Get the source table's ID in the cluster.
	 *
	 * @return String representing the source table's ID.
	 */
	public String getSourceTableID()
	{
		return sourceTableID;
	}

	/**
	 * Get the target table's ID in the cluster.
	 *
	 * @return String representing the target table's ID.
	 */
	public String getTargetTableID()
	{
		return targetTableID;
	}

	/**
	 * Get the X value of the flicked item's direction on arrival.
	 *
	 * @return The X value of the flicked item's direction on arrival.
	 */
	public float getXDir()
	{
		return xDir;
	}

	/**
	 * Get the X value of the flicked item's arrival location.
	 *
	 * @return The X value of the flicked item's arrival location.
	 */
	public float getXinMetres()
	{
		return x;
	}

	/**
	 * Get the Y value of the flicked item's direction on arrival.
	 *
	 * @return The Y value of the flicked item's direction on arrival.
	 */
	public float getYDir()
	{
		return yDir;
	}

	/**
	 * Get the Y value of the flicked item's arrival location.
	 *
	 * @return The Y value of the flicked item's arrival location.
	 */
	public float getYinMetres()
	{
		return y;
	}

	/**
	 * Set the object representing the flicked item and its attached feedback.
	 *
	 * @param galleryItem
	 *            Object representing the flicked item and its attached
	 *            feedback.
	 */
	public void setGalleryItem(GalleryItemDatabaseFormat galleryItem)
	{
		this.galleryItem = galleryItem;
	}

	/**
	 * Set the rotation of the flicked item on arrival.
	 *
	 * @param rotation
	 *            The rotation of the flicked item on arrival.
	 */
	public void setRotation(float rotation)
	{
		this.rotation = rotation;
	}

	/**
	 * Set the scale of the flicked item on arrival.
	 *
	 * @param scale
	 *            The scale of the flicked item on arrival.
	 */
	public void setScale(float scale)
	{
		this.scale = scale;
	}

	/**
	 * Set the source table's ID in the cluster.
	 *
	 * @param sourceTableID
	 *            The source table's ID
	 */
	public void setSourceTableID(String sourceTableID)
	{
		this.sourceTableID = sourceTableID;
	}

	/**
	 * Set the target table's ID in the cluster.
	 *
	 * @param targetTableID
	 *            The target table's ID
	 */
	public void setTargetTableID(String targetTableID)
	{
		this.targetTableID = targetTableID;
	}

	/**
	 * Set the X value of the flicked item's direction on arrival.
	 *
	 * @param xDir
	 *            The X value of the flicked item's direction on arrival.
	 */
	public void setXDir(float xDir)
	{
		this.xDir = xDir;
	}

	/**
	 * Set the X value of the flicked item's arrival location.
	 *
	 * @param x
	 *            The X value of the flicked item's arrival location.
	 */
	public void setXinMetres(float x)
	{
		this.x = x;
	}

	/**
	 * Set the Y value of the flicked item's direction on arrival.
	 *
	 * @param yDir
	 *            The Y value of the flicked item's direction on arrival.
	 */
	public void setYDir(float yDir)
	{
		this.yDir = yDir;
	}

	/**
	 * Set the Y value of the flicked item's arrival location.
	 *
	 * @param y
	 *            The Y value of the flicked item's arrival location.
	 */
	public void setYinMetres(float y)
	{
		this.y = y;
	}

}
