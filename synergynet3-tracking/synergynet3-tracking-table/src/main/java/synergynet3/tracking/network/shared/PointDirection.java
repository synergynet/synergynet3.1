package synergynet3.tracking.network.shared;

import java.io.Serializable;

/**
 * The Class PointDirection.
 */
public class PointDirection implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -7460630412851354054L;

	/** The end point. */
	private float[] endPoint = { 0, 0, 0 };

	/** The start point. */
	private float[] startPoint = { 0, 0, 0 };

	/**
	 * Instantiates a new point direction.
	 */
	public PointDirection() {
	} // Constructor for initialisation on the network

	/**
	 * @return the end point
	 */
	public float[] getEndPoint() {
		return endPoint;
	}

	/**
	 * @return the start point
	 */
	public float[] getStartPoint() {
		return startPoint;
	}

	/**
	 * @param x the x value of the end point to set
	 * @param y the y value of the end point to set
	 * @param z the z value of the end point to set
	 */
	public void setEndPoint(float x, float y, float z) {
		endPoint[0] = x;
		endPoint[1] = y;
		endPoint[2] = z;
	}

	/**
	 * @param x the x value of the start point to set
	 * @param y the y value of the start point to set
	 * @param z the z value of the start point to set
	 */
	public void setStartPoint(float x, float y, float z) {
		startPoint[0] = x;
		startPoint[1] = y;
		startPoint[2] = z;
	}
}