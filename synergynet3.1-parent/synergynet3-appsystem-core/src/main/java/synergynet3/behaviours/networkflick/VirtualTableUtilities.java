package synergynet3.behaviours.networkflick;

import java.awt.Color;
import java.util.HashMap;
import java.util.UUID;

import synergynet3.SynergyNetApp;
import synergynet3.positioning.SynergyNetPosition;
import synergynet3.positioning.SynergyNetPositioning;
import synergynet3.web.core.AppSystemControlComms;

import multiplicity3.appsystem.MultiplicityClient;
import multiplicity3.csys.factory.ContentTypeNotBoundException;
import multiplicity3.csys.gfx.ColourUtils;
import multiplicity3.csys.items.container.IContainer;
import multiplicity3.csys.items.shapes.IColourRectangle;
import multiplicity3.csys.stage.IStage;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;

/**
 *  A collection of methods for the management of representations of other device interfaces running instances
 *  of SynergyNetApp applications.
 */
public class VirtualTableUtilities {
	
	/**Collection of spatials representing other device interfaces running instances of SynergyNetApp applications. */
	public static HashMap<String, Spatial> otherTables = new HashMap<String, Spatial>();
	
	/**Collection of orientations of other device interfaces running instances of SynergyNetApp applications. */
	public static HashMap<String, Float> otherTablesOrientation = new HashMap<String, Float>();
	
	/**Collection of scale differences of other device interfaces running instances of SynergyNetApp applications. */
	public static HashMap<String, Float> otherTablesScaleChanges = new HashMap<String, Float>();
	
	/**
	 * Sends out a structured message to all devices on the network cluster defining the 
	 * current device's interface's location and orientation in its physical environment.
	 */
	public static void announceTablePositionToAll(){
		SynergyNetApp.localDevicePosition.regenerateID();
		AppSystemControlComms.get().allTablePositionUpdate(SynergyNetApp.localDevicePosition);		
	}	

	/**
	 * Creates a spatial representing another device connected to the network cluster's interface.
	 * The method adds this in the same position as its real life counterpart in relation to the local device's interface position.
	 * The method then sends a structured message detailing the local device's interface position to the device represented by the spatial.
	 * 
	 * @param tableID ID of the device of the interface represented on the network cluster.
	 * @param location Location of the interface represented in the physical environment relative to the local device's interface position.
	 * @param orientation Orientation of the interface represented in the physical environment relative to the local device's interface position.
	 * @param dimensions The width and height of the device represented's interface.
	 * @param pixelWidth The width of the device represented's interface in pixels.
	 */
	public static void addTable(String tableID, Vector2f location, float orientation, Vector2f dimensions, float pixelWidth, float metreWidth, IStage stage){

		if (tableID == null)return;
		if (tableID.equals(SynergyNetApp.getTableIdentity()))return;
		if (otherTables.containsKey(tableID))return;				
		
		Box box = new Box(new Vector3f(), dimensions.x/2, dimensions.y/2, 10);
		Spatial virtualTable = new Geometry(tableID, box);			
		otherTables.put(tableID, virtualTable);
		otherTablesOrientation.put(tableID, orientation);
		
		Vector2f thisTableLocation = new Vector2f(SynergyNetPositioning.getPixelValue(SynergyNetApp.localDevicePosition.getXinMetres()),
				SynergyNetPositioning.getPixelValue(SynergyNetApp.localDevicePosition.getYinMetres()));		
		Vector2f relativePosition = location.subtract(thisTableLocation);		

		MultiplicityClient.get().getRootNode().attachChild(virtualTable);
		
		announceTablePositionToSpecificTable(tableID);
		
		float localPixelsPerM = SynergyNetApp.localDevicePosition.getPixelWidth() / SynergyNetApp.localDevicePosition.getWidthinMetres();
		float remotePixelsPerM = pixelWidth / metreWidth;
		
		float scaleChange = remotePixelsPerM / localPixelsPerM;			
		otherTablesScaleChanges.put(tableID, scaleChange);
		
		try {
			IContainer container = stage.getContentFactory().create(IContainer.class, "RectangleContainer", UUID.randomUUID());					
			IColourRectangle tableRep = stage.getContentFactory().create(IColourRectangle.class, "virtualTable", UUID.randomUUID());				
			tableRep.setSize(dimensions.x, dimensions.y);		
			Color colour = ColourUtils.colorFromString(tableID);
			if (colour == null){
				tableRep.setSolidBackgroundColour(ColorRGBA.White);	
			}else{
				tableRep.setSolidBackgroundColour(ColourUtils.getColorRGBAFromColor(colour));	
			}				
			tableRep.setRelativeRotation(-orientation);
			tableRep.setRelativeLocation(new Vector2f(relativePosition.x, relativePosition.y));				
			container.addItem(tableRep);				
			container.setRelativeRotation(SynergyNetApp.localDevicePosition.getOrientation());
			stage.addItem(container);

			virtualTable.setLocalTranslation(tableRep.getWorldLocation().x, tableRep.getWorldLocation().y, 0);	
			virtualTable.setLocalRotation(tableRep.getManipulableSpatial().getWorldRotation());
			
		} catch (ContentTypeNotBoundException e) {
			e.printStackTrace();
		}	
		
	}
	
	/**
	 * Sends out a structured message to a specific device on the network cluster defining the 
	 * current device's interface's location and orientation in its physical environment. 
	 * 
	 * @param targetTableID The ID of the target device on the network cluster.
	 */
	public static void announceTablePositionToSpecificTable(String targetTableID){
		AppSystemControlComms.get().specificTablePositionUpdate(SynergyNetApp.localDevicePosition, targetTableID);
	}
	
	/**
	 * Removes the spatial representing the identified device's interface from the environment.
	 * The method also removes the entries from the collections of spatials, IDs and orientations
	 * of the other devices on the network cluster.
	 * 
	 * @param tableID ID of the device to be removed on the network cluster.
	 */
	public static void removeTable(String tableID){
		if (otherTables.containsKey(tableID)){
			otherTables.get(tableID).removeFromParent();
			otherTables.remove(tableID);
		}
		if (otherTablesOrientation.containsKey(tableID))otherTablesOrientation.remove(tableID);		
		if (otherTablesScaleChanges.containsKey(tableID))otherTablesScaleChanges.remove(tableID);		
		
	}
	
	/**
	 * Sends a structured message announcing that the local representation of the current device's interface
	 * should be removed from all instances of SynergyNetApp extending applications. 
	 */
	public static void removeTableFromAll(){		
		SynergyNetPosition localTable = new SynergyNetPosition(SynergyNetApp.getTableIdentity(), true);		
		AppSystemControlComms.get().allTablePositionUpdate(localTable);
	}

	/**
	 * Retrieves the network cluster ID of the device interface represented by a spatial.
	 * 
	 * @param targetTable Spatial representing a remote device interface.
	 * @return A String representing the ID of the device represented on the network cluster.
	 */
	public static String getIDForTable(Spatial targetTable) {
	    for(String key : otherTables.keySet()){
	        if(otherTables.get(key).equals(targetTable)) {
	            return key;
	        }
	    }		
		return null;
	}
}