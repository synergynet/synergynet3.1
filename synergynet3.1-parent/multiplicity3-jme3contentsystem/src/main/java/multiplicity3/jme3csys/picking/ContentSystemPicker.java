package multiplicity3.jme3csys.picking;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import multiplicity3.csys.items.item.IItem;
import multiplicity3.csys.picksystem.IPickSystem;
import multiplicity3.jme3csys.items.item.JMEItem;

import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

public class ContentSystemPicker implements IPickSystem {
	private static final Logger log = Logger.getLogger(ContentSystemPicker.class.getName());

	private Node rootNode;
	private int displayWidth;
	private int displayHeight;

	public ContentSystemPicker(Node rootOrthoNode, int displayWidth, int displayHeight) {		
		this.rootNode = rootOrthoNode;
		this.displayWidth = displayWidth;
		this.displayHeight = displayHeight;
	}
	
	@Override
	public List<IItem> findItemsOnTableAtPosition(Vector2f pickPoint) {
		List<IItem> foundItems = new ArrayList<IItem>();		
		
		log.finer("Find items at position " + pickPoint);
		CollisionResults results = new CollisionResults();
		Vector3f loc = new Vector3f(pickPoint.x * displayWidth, pickPoint.y * displayHeight, 999999);
		Ray ray = new Ray(loc, new Vector3f(0,0, -1f));
		rootNode.updateGeometricState();
		rootNode.collideWith(ray, results);
		if(results.size() < 1) {
			log.fine("  Did not find any geometry under cursor.");
		}else{			
			log.fine("  Found " + results.size() + " hits.");
		}
				
		//TODO: make sure pick distance sorting is working
		for (int i = 0; i < results.size(); i++) {
			log.fine("  Examining geometry at index " + i);
			float dist = results.getCollision(i).getDistance();
			Vector3f pt = results.getCollision(i).getContactPoint();
			String hit = results.getCollision(i).getGeometry().getName();
			log.fine("    Hit geometry " + hit + " at " + pt + ", " + dist + " world units away.");
			log.fine("    Geometry is a " + results.getCollision(i).getGeometry().getClass().getName());
			log.fine("    Testing for a UUID");
			try {					
				String uuidStr = (String) results.getCollision(i).getGeometry().getUserData(JMEItem.KEY_JMEITEMDATA);
				if(uuidStr != null) {					
					UUID uuid = UUID.fromString(uuidStr);
					log.fine("    UUID found: " + uuid);
					List<IItem> items = ItemMap.getItem(uuid);
					for(IItem itm : items) {
						if(itm.isVisible()) foundItems.add(itm);
					}
					
				}else{
					log.fine("    No UUID associated with " + hit);
				}
			}catch(IllegalArgumentException ex) {
				// TODO: propagate?
			}
		}
		log.fine("Returning " + foundItems);
		return foundItems;
	}

}
