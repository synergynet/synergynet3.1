package synergynet3.tracking.network.shared;

import java.io.Serializable;
import java.util.ArrayList;

public class UserLocations implements Serializable{
	
	private static final long serialVersionUID = -5757410165005878179L;
	
	private ArrayList<UserLocation> userLocations = new ArrayList<UserLocation>();
	private String source = "";
	
	public UserLocations(String source){
		this.source = source;
	}
	
	public void add(UserLocation userLocation){
		userLocations.add(userLocation);
	}
	
	public void remove(UserLocation userLocation){
		userLocations.remove(userLocation);
	}

	public void clear() {
		userLocations.clear();		
	}
	
	/**
	 * @return the userLocations
	 */
	public ArrayList<UserLocation> getUserLocations() {
		return userLocations;
	}

	/**
	 * @return the source
	 */
	public String getSource() {
		return source;
	}
	
	/**
	 * @param source the source to set
	 */
	public void setSource(String source) {
		this.source = source;
	}

}