package multiplicity3.csys.behaviours;

import java.util.logging.Logger;

import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;

import multiplicity3.csys.items.item.IItem;
import multiplicity3.csys.stage.IStage;
import multiplicity3.input.IMultiTouchEventListener;
import multiplicity3.input.events.MultiTouchCursorEvent;
import multiplicity3.input.events.MultiTouchObjectEvent;

public class RotateTranslateScaleBehaviour implements IBehaviour, IMultiTouchEventListener {
	private static final Logger log = Logger.getLogger(RotateTranslateScaleBehaviour.class.getName());
	
	
	protected Vector2f cursor1WorldPosition = new Vector2f();
	protected Vector2f cursor2WorldPosition = new Vector2f();
	protected Vector2f cursor1OldWorldPosition = new Vector2f();
	protected Vector2f cursor2OldWorldPosition = new Vector2f();

	protected float maxScale = 4.0f;
	protected float minScale = 0.1f;

	private IItem eventSourceItem;
	private IItem affectedItem;

	private long cursor1ID = Long.MAX_VALUE;
	private long cursor2ID = Long.MAX_VALUE;
	private boolean scaleDisabled = false;
	private boolean rotationDisabled = false;
	private boolean active = true;

	private IStage stage;

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
	}
	
	@Override
	public void setItemActingOn(IItem item) {
		log.fine("Adding rotate translate scale behaviour to " + item);
		this.affectedItem = item;		
	}

	private void applyMultiCursorTransform() {
		log.fine("multi-cursor on item at " + affectedItem.getWorldLocation());

		Vector2f oldCenter = new Vector2f();
		oldCenter.interpolate(cursor1OldWorldPosition, cursor2OldWorldPosition, 0.5f);
		Vector2f currentCenter = new Vector2f();
		currentCenter.interpolate(cursor1WorldPosition, cursor2WorldPosition, 0.5f);

		float oldAngle = cursor2OldWorldPosition.subtract(cursor1OldWorldPosition).getAngle();
		float curAngle = cursor2WorldPosition.subtract(cursor1WorldPosition).getAngle();
		float angleChange = curAngle - oldAngle;
		
		if(rotationDisabled)angleChange = 0f;		

		Vector2f centerToSpatial = affectedItem.getWorldLocation().subtract(oldCenter);
		
		float currentCenterToSpatialAngle = centerToSpatial.getAngle() + angleChange;

		float oldLength = cursor2OldWorldPosition.subtract(cursor1OldWorldPosition).length();
		float newLength = cursor2WorldPosition.subtract(cursor1WorldPosition).length();
		float scaleChange = newLength / oldLength;

		if(scaleDisabled  
				|| affectedItem.getRelativeScale() * scaleChange < minScale 
				|| affectedItem.getRelativeScale() * scaleChange > maxScale) {
			scaleChange = 1f;
		}

		float newDistFromCurrentCenterToSpatial = scaleChange * centerToSpatial.length();

		float dx = newDistFromCurrentCenterToSpatial * FastMath.cos(currentCenterToSpatialAngle);
		float dy = newDistFromCurrentCenterToSpatial * FastMath.sin(currentCenterToSpatialAngle);

		Vector2f dxdy = new Vector2f(dx, dy);
		Vector2f newScreenPosition = currentCenter.add(dxdy);
		if(Float.isNaN(dx) || Float.isNaN(dy)) newScreenPosition = currentCenter;
		
		affectedItem.setWorldLocation(newScreenPosition);
		affectedItem.setRelativeRotation(affectedItem.getRelativeRotation() + angleChange);
		affectedItem.setRelativeScale(affectedItem.getRelativeScale() * scaleChange);

		float angle = affectedItem.getRelativeRotation();
		if(affectedItem.getRelativeRotation() <0) angle = FastMath.TWO_PI - angle;
	}

	private void applySingleCursorTransform() {
		if(cursor1ID != Long.MAX_VALUE) {
			affectedItem.setWorldLocation(affectedItem.getWorldLocation().add(cursor1WorldPosition.subtract(cursor1OldWorldPosition)));
		}else if(cursor2ID != Long.MAX_VALUE) {
			affectedItem.setWorldLocation(affectedItem.getWorldLocation().add(cursor2WorldPosition.subtract(cursor2OldWorldPosition)));	
		}
	}

	public void setScaleEnabled(boolean b) {
		scaleDisabled = !b;
	}
	
	public void setScaleLimits(float minScale, float maxScale){
		this.minScale = minScale;
		this.maxScale = maxScale;
	}
	
	public void setScaleMinLimit(float minScale){
		this.minScale = minScale;
	}
	
	public void setScaleMaxLimit(float maxScale){
		this.maxScale = maxScale;
	}
	
	public boolean isScaleEnabled() {
		return !scaleDisabled;
	}
	
	public void setRotationEnabled(boolean b) {
		rotationDisabled = !b;
	}
	
	
	public boolean isRotationEnabled(){
		return rotationDisabled;
	}

	@Override
	public void cursorClicked(MultiTouchCursorEvent event) {}

	@Override
	public void cursorPressed(MultiTouchCursorEvent event) {			
		if(!active ) return;
		if(event.getCursorID() == cursor1ID || event.getCursorID() == cursor2ID) {
			return;
		}

		if(cursor1ID == Long.MAX_VALUE) {
			stage.tableToWorld(event.getPosition(), cursor1WorldPosition);
			stage.tableToWorld(event.getPosition(), cursor1OldWorldPosition);
			cursor1ID = event.getCursorID();
		}else if(cursor2ID == Long.MAX_VALUE) {
			stage.tableToWorld(event.getPosition(), cursor2WorldPosition);
			stage.tableToWorld(event.getPosition(), cursor2OldWorldPosition);
			cursor2ID = event.getCursorID();
		}		
	}

	@Override
	public void cursorChanged(MultiTouchCursorEvent event) {			
		if(!active ) return;
		updateCursor(event);

		if(getCursorCount() == 1) {			
			applySingleCursorTransform();
		}else if (getCursorCount() == 2 && event.getCursorID() == cursor2ID){			
			applyMultiCursorTransform();			
		}		
	}

	private int getCursorCount() {
		int count = 0;
		if(cursor1ID != Long.MAX_VALUE) count++;
		if(cursor2ID != Long.MAX_VALUE) count++;		
		return count;
	}

	@Override
	public void cursorReleased(MultiTouchCursorEvent event) {			
		if(event.getCursorID() == cursor1ID && cursor1ID != Long.MAX_VALUE) {
			cursor1ID = Long.MAX_VALUE;
		}else if(event.getCursorID() == cursor2ID && cursor2ID != Long.MAX_VALUE) {
			cursor2ID = Long.MAX_VALUE;
		}

		if(getCursorCount() == 1) {
			updateCursor(event);
			applySingleCursorTransform();
		}
	}

	@Override
	public void objectAdded(MultiTouchObjectEvent event) {}

	@Override
	public void objectChanged(MultiTouchObjectEvent event) {}

	@Override
	public void objectRemoved(MultiTouchObjectEvent event) {}

	//******

	protected void updateCursor(MultiTouchCursorEvent event) {
		if(event.getCursorID() == cursor1ID) {
			updateCursor1(event);
		}else if(event.getCursorID() == cursor2ID) {
			updateCursor2(event);
		}		
	}

	protected void updateCursor1(MultiTouchCursorEvent event) {	
		cursor1OldWorldPosition.x = cursor1WorldPosition.x;
		cursor1OldWorldPosition.y = cursor1WorldPosition.y;
		stage.tableToWorld(event.getPosition(), cursor1WorldPosition);
	}

	protected void updateCursor2(MultiTouchCursorEvent event) {
		cursor2OldWorldPosition.x = cursor2WorldPosition.x;
		cursor2OldWorldPosition.y = cursor2WorldPosition.y;
		stage.tableToWorld(event.getPosition(), cursor2WorldPosition);
	}

	@Override
	public void setStage(IStage stage) {
		this.stage = stage;		
	}

	public boolean isActive() {
		return getCursorCount() > 0;
	}

	@Override
	public void setActive(boolean active) {
		this.active = active;
	}

}
