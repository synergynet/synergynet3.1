package synergynet3.positioning;

import synergynet3.config.web.WebConfigPrefsItem;
import synergynet3.web.core.AppSystemControlComms;
import multiplicity3.config.display.DisplayPrefsItem;
import multiplicity3.config.position.PositionConfigPrefsItem;

import com.jme3.math.FastMath;

public class SynergyNetPositioning {
	
	private static float conversionBetweenPixelAndMetre = 0f;
	
	public static int getPixelValue(float value){
		if (conversionBetweenPixelAndMetre == 0f)initialiseConversionValue();
		return (int)(value * conversionBetweenPixelAndMetre);
	}
	
	public static float getMetreValue(int value){
		if (conversionBetweenPixelAndMetre == 0f)initialiseConversionValue();
		return (float)(value / conversionBetweenPixelAndMetre);
	}
	
	private static void initialiseConversionValue() {
		DisplayPrefsItem displayPrefs = new DisplayPrefsItem();	
		conversionBetweenPixelAndMetre = displayPrefs.getWidth()/displayPrefs.getRealWidth();
	}

	public static SynergyNetPosition getLocalDeviceLocationFull(){
		
		String identity = new WebConfigPrefsItem().getClusterUserName();
		
		PositionConfigPrefsItem prefs = new PositionConfigPrefsItem();
		
		float tableLocationX = prefs.getXPos();
		float tableLocationY = prefs.getYPos();
		
		if (prefs.getDeveloperMode()){
			int currentConnectionsCount = AppSystemControlComms.get().getNumberOfTablesOnline();
			if (prefs.getHorizontalPlacement()){							
				if(prefs.getGridLimitX() != 0){
					float xPos = currentConnectionsCount % prefs.getGridLimitX();
					tableLocationX = xPos * prefs.getGridDistanceX();
					float yPos = currentConnectionsCount / prefs.getGridLimitX();
					tableLocationY = yPos * prefs.getGridDistanceY();
				}else{
					tableLocationX = currentConnectionsCount * prefs.getGridDistanceX();
					tableLocationY = 0;
				}
			}else{
				if(prefs.getGridLimitY() != 0){
					float yPos = currentConnectionsCount % prefs.getGridLimitY();
					tableLocationY = yPos * prefs.getGridDistanceY();
					float xPos = currentConnectionsCount / prefs.getGridLimitY();
					tableLocationX = xPos * prefs.getGridDistanceX();
				}else{
					tableLocationY = currentConnectionsCount * prefs.getGridDistanceY();
					tableLocationX = 0;
				}
			}
		}	
		
		float orientation = FastMath.DEG_TO_RAD * prefs.getAngle();
		
		float heightFromFloor = prefs.getTableHeight();
		
		DisplayPrefsItem displayPrefs = new DisplayPrefsItem();			
		float width = getMetreValue(displayPrefs.getWidth());
		float height = getMetreValue(displayPrefs.getHeight());
		
		return new SynergyNetPosition(identity, tableLocationX, tableLocationY, orientation, width, height, heightFromFloor, displayPrefs.getWidth());
	}
	
	public static SynergyNetPosition getLocalDeviceLocationPosOnly(){
		PositionConfigPrefsItem prefs = new PositionConfigPrefsItem();
		
		float tableLocationX = prefs.getXPos();
		float tableLocationY = prefs.getYPos();
		
		int currentConnectionsCount = AppSystemControlComms.get().getNumberOfTablesOnline() - 1;
		
		if (prefs.getDeveloperMode()){
			if (prefs.getHorizontalPlacement()){
				if(prefs.getGridLimitX() != 0){
					int xPos = currentConnectionsCount % prefs.getGridLimitX();
					tableLocationX = xPos * prefs.getGridDistanceX();
					int yPos = currentConnectionsCount / prefs.getGridLimitX();
					tableLocationY = yPos * prefs.getGridDistanceY();
				}else{
					tableLocationX = currentConnectionsCount * prefs.getGridDistanceX();
					tableLocationY = 0;
				}
			}else{
				if(prefs.getGridLimitY() != 0){
					int yPos = currentConnectionsCount % prefs.getGridLimitY();
					tableLocationY = yPos * prefs.getGridDistanceY();
					int xPos = currentConnectionsCount / prefs.getGridLimitY();
					tableLocationX = xPos * prefs.getGridDistanceX();
				}else{
					tableLocationY = currentConnectionsCount * prefs.getGridDistanceY();
					tableLocationX = 0;
				}
			}
		}	
		
		float orientation = FastMath.DEG_TO_RAD * prefs.getAngle();
		
		float heightFromFloor = prefs.getTableHeight();
		
		return new SynergyNetPosition("", tableLocationX, tableLocationY, orientation, 0, 0, heightFromFloor, 0);
	}

}
