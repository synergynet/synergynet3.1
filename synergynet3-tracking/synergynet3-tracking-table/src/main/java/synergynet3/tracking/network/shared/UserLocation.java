package synergynet3.tracking.network.shared;

import java.io.Serializable;

public class UserLocation implements Serializable{
	
	public enum USERSTATE{
	    BODY, ONE_HAND, TWO_HANDS
	}
	
	private static final long serialVersionUID = 8614541915205145862L;
	private int ID = 0;
	private USERSTATE userState = USERSTATE.BODY;
	private float[] bodyLocation = {0,0};
	private float[] firstHandLocation = {0,0,0};
	private float[] secondHandLocation = {0,0,0};
	
	public UserLocation(int ID, String source){
		this.ID = ID;
	}
	
	public int getID(){
		return ID;
	}
	
	public USERSTATE getUserState(){
		return userState;
	}
	
	public float[] getLocationOfUser(){
		if (userState == USERSTATE.BODY){
			return getUserBodyLocation();
		}else if (userState == USERSTATE.ONE_HAND){
			return firstHandLocation;
		}else if (userState == USERSTATE.TWO_HANDS){
			float[] locationsOfUser = {firstHandLocation[0], firstHandLocation[1], firstHandLocation[2], secondHandLocation[0],secondHandLocation[1], secondHandLocation[2]};
			return locationsOfUser;
		}else{
			return new float[0] ;
		}
	}
	
	public void setUserBodyLocation(float x, float y){
		userState = USERSTATE.BODY;
		bodyLocation[0] = x;
		bodyLocation[1] = y;
	}
	
	public void setSingleUserHandLocation(float x, float y, float z){
		userState = USERSTATE.ONE_HAND;
		firstHandLocation[0] = x;
		firstHandLocation[1] = y;
		firstHandLocation[2] = z;
	}
	
	public void setBothUserHandLocations(float xOne, float yOne, float zOne, float xTwo, float yTwo, float zTwo){
		userState = USERSTATE.TWO_HANDS;
		
		firstHandLocation[0] = xOne;
		firstHandLocation[1] = yOne;
		firstHandLocation[2] = zOne;
		
		secondHandLocation[0] = xTwo;
		secondHandLocation[1] = yTwo;
		secondHandLocation[2] = zTwo;
	}

	public float[] getUserBodyLocation() {
		return bodyLocation;
	}

}