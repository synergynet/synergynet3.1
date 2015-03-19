package multiplicity3.csys.behaviours;

import java.util.logging.Logger;

import com.jme3.bounding.BoundingBox;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;

import multiplicity3.csys.items.item.IItem;
import multiplicity3.csys.stage.IStage;
import multiplicity3.input.IMultiTouchEventListener;
import multiplicity3.input.events.MultiTouchCursorEvent;
import multiplicity3.input.events.MultiTouchObjectEvent;

public class ThreeDRotateInteraction implements IBehaviour, IMultiTouchEventListener {
	private static final Logger log = Logger.getLogger(ThreeDRotateInteraction.class.getName());

	private long currentCursorID;
	private Vector2f cursorPressedPosition;

	private IItem eventSourceItem;
	private IItem affectedItem;
	
	private IItem parentItem;

	private IStage stage;
	private boolean active = true;
	
	private float width, height, depth, maxDim = 0;
	

	@Override
	public void objectRemoved(MultiTouchObjectEvent event) {}

	@Override
	public void objectChanged(MultiTouchObjectEvent event) {}

	@Override
	public void objectAdded(MultiTouchObjectEvent event) {}

	@Override
	public void cursorReleased(MultiTouchCursorEvent event) {
		this.currentCursorID = -1;
	}

	@Override
	public void cursorPressed(MultiTouchCursorEvent event) {			
		if(!active ) return;
		this.currentCursorID = event.getCursorID();
		this.cursorPressedPosition = event.getPosition();
		cursorPressedPosition = stage.tableToWorld(cursorPressedPosition);
		cursorPressedPosition = cursorPressedPosition.subtract(parentItem.getWorldLocation());
		cursorPressedPosition.rotateAroundOrigin(-parentItem.getRelativeRotation(), false);
	}

	@Override
	public void cursorClicked(MultiTouchCursorEvent event) {}

	@Override
	public void cursorChanged(MultiTouchCursorEvent event) {		
		if(!active ) return;
		if(currentCursorID == event.getCursorID()) {
			Vector2f newPos = event.getPosition();
			newPos = stage.tableToWorld(newPos);
			newPos = newPos.subtract(parentItem.getWorldLocation());
			newPos.rotateAroundOrigin(-parentItem.getRelativeRotation(), false);	
			if (newPos.length() < (maxDim*parentItem.getRelativeScale())){
				Vector2f change = newPos.subtract(cursorPressedPosition);	
				float xChange = (((-change.y)/(height*parentItem.getRelativeScale())) / (360 * FastMath.DEG_TO_RAD));
				float yChange = (((change.x)/(width*parentItem.getRelativeScale())) / (360 * FastMath.DEG_TO_RAD));
				
				float[] angles = {xChange, yChange, 0};
				Quaternion q = new Quaternion();
				q.fromAngles(angles);           
				q.multLocal(affectedItem.getManipulableSpatial().getLocalRotation());
				affectedItem.getManipulableSpatial().setLocalRotation(q);
			}			
		}		
	}


	@Override
	public void setEventSource(IItem newSourceItem) {
		if(newSourceItem == eventSourceItem) {
			// no change
			return;
		}
		
		// already have an event source, so unregister it
		if(this.eventSourceItem != null) {
			this.eventSourceItem.getMultiTouchDispatcher().remove(this);
		}
		
		eventSourceItem = newSourceItem;
		eventSourceItem.getMultiTouchDispatcher().addListener(this);
		
		width = ((BoundingBox)eventSourceItem.getManipulableSpatial().getWorldBound()).getXExtent();
		height = ((BoundingBox)eventSourceItem.getManipulableSpatial().getWorldBound()).getYExtent();
		depth = ((BoundingBox)eventSourceItem.getManipulableSpatial().getWorldBound()).getZExtent();
			
		maxDim = width;
		if (maxDim < height){
			maxDim = height;
		}
		if (maxDim < depth){
			maxDim = depth;
		}
		maxDim /= 2;
	}
	
	@Override
	public void setItemActingOn(IItem item) {
		log.fine("Adding 3D rotation behaviour to " + item);
		this.affectedItem = item;			
	}


	@Override
	public void setStage(IStage stage) {
		this.stage = stage;		
	}


	@Override
	public void setActive(boolean active) {
		this.active = active;		
	}

	public IItem getParentItem() {
		return parentItem;
	}

	public void setParentItem(IItem parentItem) {
		this.parentItem = parentItem;
	}

}