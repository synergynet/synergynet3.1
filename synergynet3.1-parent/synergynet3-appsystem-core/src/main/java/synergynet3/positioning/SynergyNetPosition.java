package synergynet3.positioning;

import java.io.Serializable;

import synergynet3.web.shared.messages.PerformActionMessage;

/** Structured message representing the location of a remote device's interface. */
public class SynergyNetPosition extends PerformActionMessage implements
		Serializable {

	/** Unique ID for serialisation. */
	private static final long serialVersionUID = -4235788275756387238L;

	/** Dimensions of the device interface represented. */
	private float displayWidth, displayHeight;

	/** Height of the interface represented. */
	private float interfaceHeightFromFloor;

	/** Pixel width of the interface represented. */
	private float pixelWidth;

	/** Rotation of the device interface represented. */
	private float rotation;

	/** Cluster ID of the device interface represented. */
	private String tableID;

	/** Location of the device interface represented. */
	private float x, y;

	/**
	 * Empty Constructor used to initialise value in the network cluster.
	 */
	public SynergyNetPosition() {
		super();
	}

	/**
	 * Create a structured device position message for transmission through the
	 * cluster. This message is used to initialise the removal of a virtual
	 * device representation.
	 *
	 * @param tableIdentity
	 * @param remove
	 */
	public SynergyNetPosition(String tableIdentity, boolean remove) {
		super(MESSAGESTATE.DEACTIVATE);
		this.tableID = tableIdentity;
	}

	/**
	 * Create a structured device position message for transmission through the
	 * cluster. This message is used to initialise the creation of a virtual
	 * device representation.
	 *
	 * @param tableIdentity Cluster ID of the device interface represented.
	 * @param x X value of location of the device interface represented.
	 * @param y Y value of location of the device interface represented.
	 * @param thisTableAngleDegrees Rotation of the device interface represented
	 * @param displayWidth Width of the device interface represented.
	 * @param displayHeight Height of the device interface represented.
	 * @param pixelWidth Pixel width of the interface represented.
	 */
	public SynergyNetPosition(String tableIdentity, float x, float y,
			float rotation, float displayWidth, float displayHeight,
			float interfaceHeightFromFloor, float pixelWidth) {
		super(MESSAGESTATE.ACTIVATE);
		this.tableID = tableIdentity;
		this.x = x;
		this.y = y;
		this.rotation = rotation;
		this.displayWidth = displayWidth;
		this.displayHeight = displayHeight;
		this.interfaceHeightFromFloor = interfaceHeightFromFloor;
		this.pixelWidth = pixelWidth;
	}

	/**
	 * Get the height of the device interface represented.
	 *
	 * @return Float representing the height of the device interface.
	 */
	public float getHeightinMetres() {
		return displayHeight;
	}

	/**
	 * Get the height from the floor of the device interface represented.
	 *
	 * @return Float representing the height from the floor of the device
	 *         interface.
	 */
	public float getInterfaceHeightFromFloorinMetres() {
		return interfaceHeightFromFloor;
	}

	/**
	 * Get the rotation of the device interface represented.
	 *
	 * @return Float representing the rotation of the device interface.
	 */
	public float getOrientation() {
		return rotation;
	}

	/**
	 * Get the pixel width of the interface represented in pixels.
	 *
	 * @return Float representing the pixel width of the interface represented
	 *         in pixels.
	 */
	public float getPixelWidth() {
		return pixelWidth;
	}

	/**
	 * Get the cluster ID of the device interface represented.
	 *
	 * @return String representing the cluster ID of the device interface.
	 */
	public String getTableID() {
		return tableID;
	}

	/**
	 * Get the width of the device interface represented.
	 *
	 * @return Float representing the width of the device interface.
	 */
	public float getWidthinMetres() {
		return displayWidth;
	}

	/**
	 * Get the X value of the device interface represented's location.
	 *
	 * @return Float representing the X value of the device interface's
	 *         location.
	 */
	public float getXinMetres() {
		return x;
	}

	/**
	 * Get the Y value of the device interface represented's location.
	 *
	 * @return Float representing the Y value of the device interface's
	 *         location.
	 */
	public float getYinMetres() {
		return y;
	}

	/**
	 * Set the height of the device interface represented.
	 *
	 * @param height The height of the device interface represented.
	 */
	public void setHeightinMetres(float height) {
		this.displayHeight = height;
	}

	/**
	 * Set the height from the floor of the interface represented.
	 *
	 * @param height The height from the floor of the device interface
	 *            represented.
	 */
	public void setInterfaceHeightFromFloorinMetres(
			float interfaceHeightFromFloor) {
		this.interfaceHeightFromFloor = interfaceHeightFromFloor;
	}

	/**
	 * Set the the pixel width of the interface represented in pixels.
	 *
	 * @param pixelWidth The pixel width of the interface represented in pixels.
	 */
	public void setPixelWidth(float pixelWidth) {
		this.pixelWidth = pixelWidth;
	}

	/**
	 * Set the rotation of the device interface represented.
	 *
	 * @param rotation The rotation of the device interface represented.
	 */
	public void setRotation(float rotation) {
		this.rotation = rotation;
	}

	/**
	 * Set the cluster ID of the device interface represented.
	 *
	 * @param tableID The cluster ID of the device interface represented.
	 */
	public void setTableID(String tableID) {
		this.tableID = tableID;
	}

	/**
	 * Set the width of the device interface represented.
	 *
	 * @param width The width of the device interface represented.
	 */
	public void setWidthinMetres(float width) {
		this.displayWidth = width;
	}

	/**
	 * Set the X value of the device interface represented's location.
	 *
	 * @param x The X value of the device interface represented's location.
	 */
	public void setXinMetres(float x) {
		this.x = x;
	}

	/**
	 * Set the Y value of the device interface represented's location.
	 *
	 * @param y The Y value of the device interface represented's location.
	 */
	public void setYinMetres(float y) {
		this.y = y;
	}

}
