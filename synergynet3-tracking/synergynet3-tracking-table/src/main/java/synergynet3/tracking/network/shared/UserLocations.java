package synergynet3.tracking.network.shared;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * The Class UserLocations.
 */
public class UserLocations implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -5757410165005878179L;

	/** The source. */
	private String source = "";

	/** The user locations. */
	private ArrayList<UserLocation> userLocations = new ArrayList<UserLocation>();

	/**
	 * Instantiates a new user locations.
	 *
	 * @param source the source
	 */
	public UserLocations(String source) {
		this.source = source;
	}

	/**
	 * Adds the.
	 *
	 * @param userLocation the user location
	 */
	public void add(UserLocation userLocation) {
		userLocations.add(userLocation);
	}

	/**
	 * Clear.
	 */
	public void clear() {
		userLocations.clear();
	}

	/**
	 * @return the source
	 */
	public String getSource() {
		return source;
	}

	/**
	 * @return the userLocations
	 */
	public ArrayList<UserLocation> getUserLocations() {
		return userLocations;
	}

	/**
	 * Removes the.
	 *
	 * @param userLocation the user location
	 */
	public void remove(UserLocation userLocation) {
		userLocations.remove(userLocation);
	}

	/**
	 * @param source the source to set
	 */
	public void setSource(String source) {
		this.source = source;
	}

}