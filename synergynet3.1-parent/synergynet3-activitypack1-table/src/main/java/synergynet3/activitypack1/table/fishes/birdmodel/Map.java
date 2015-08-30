package synergynet3.activitypack1.table.fishes.birdmodel;

import java.awt.Dimension;

/**
 * The Class Map.
 */
public class Map {
	
	/** The map. */
	public static Dimension map = new Dimension(1024, 768);
	
	/**
	 * Set the dimensions of the map.
	 * 
	 * @param width Width of the map.
	 * @param height Height of the map.
	 */
	public static void setMapDimensions(float width, float height){
		map = new Dimension((int)width, (int)height);
	}


}
