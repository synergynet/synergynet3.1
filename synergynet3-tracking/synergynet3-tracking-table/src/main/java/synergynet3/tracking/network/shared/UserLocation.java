package synergynet3.tracking.network.shared;

import java.io.Serializable;

/**
 * The Class UserLocation.
 */
public class UserLocation implements Serializable {

	/**
	 * The Enum USERSTATE.
	 */
	public enum USERSTATE {

		/** The body. */
		BODY,
		/** The one hand. */
		ONE_HAND,
		/** The two hands. */
		TWO_HANDS
	}

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 8614541915205145862L;

	/** The body location. */
	private float[] bodyLocation = { 0, 0 };

	/** The first hand location. */
	private float[] firstHandLocation = { 0, 0, 0 };

	/** The id. */
	private int ID = 0;

	/** The second hand location. */
	private float[] secondHandLocation = { 0, 0, 0 };

	/** The user state. */
	private USERSTATE userState = USERSTATE.BODY;

	/**
	 * Instantiates a new user location.
	 *
	 * @param ID the id
	 * @param source the source
	 */
	public UserLocation(int ID, String source) {
		this.ID = ID;
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public int getID() {
		return ID;
	}

	/**
	 * Gets the location of user.
	 *
	 * @return the location of user
	 */
	public float[] getLocationOfUser() {
		if (userState == USERSTATE.BODY) {
			return getUserBodyLocation();
		} else if (userState == USERSTATE.ONE_HAND) {
			return firstHandLocation;
		} else if (userState == USERSTATE.TWO_HANDS) {
			float[] locationsOfUser = { firstHandLocation[0],
					firstHandLocation[1], firstHandLocation[2],
					secondHandLocation[0], secondHandLocation[1],
					secondHandLocation[2] };
			return locationsOfUser;
		} else {
			return new float[0];
		}
	}

	/**
	 * Gets the user body location.
	 *
	 * @return the user body location
	 */
	public float[] getUserBodyLocation() {
		return bodyLocation;
	}

	/**
	 * Gets the user state.
	 *
	 * @return the user state
	 */
	public USERSTATE getUserState() {
		return userState;
	}

	/**
	 * Sets the both user hand locations.
	 *
	 * @param xOne the x one
	 * @param yOne the y one
	 * @param zOne the z one
	 * @param xTwo the x two
	 * @param yTwo the y two
	 * @param zTwo the z two
	 */
	public void setBothUserHandLocations(float xOne, float yOne, float zOne,
			float xTwo, float yTwo, float zTwo) {
		userState = USERSTATE.TWO_HANDS;

		firstHandLocation[0] = xOne;
		firstHandLocation[1] = yOne;
		firstHandLocation[2] = zOne;

		secondHandLocation[0] = xTwo;
		secondHandLocation[1] = yTwo;
		secondHandLocation[2] = zTwo;
	}

	/**
	 * Sets the single user hand location.
	 *
	 * @param x the x
	 * @param y the y
	 * @param z the z
	 */
	public void setSingleUserHandLocation(float x, float y, float z) {
		userState = USERSTATE.ONE_HAND;
		firstHandLocation[0] = x;
		firstHandLocation[1] = y;
		firstHandLocation[2] = z;
	}

	/**
	 * Sets the user body location.
	 *
	 * @param x the x
	 * @param y the y
	 */
	public void setUserBodyLocation(float x, float y) {
		userState = USERSTATE.BODY;
		bodyLocation[0] = x;
		bodyLocation[1] = y;
	}

}