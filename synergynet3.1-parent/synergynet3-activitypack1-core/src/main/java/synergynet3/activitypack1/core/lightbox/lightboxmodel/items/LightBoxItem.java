package synergynet3.activitypack1.core.lightbox.lightboxmodel.items;

import java.awt.geom.Point2D.Float;
import java.io.Serializable;

public class LightBoxItem implements Serializable {
	private static final long serialVersionUID = 6773468172887101449L;
	
	private Float position;
	private boolean moveable;
	private int rotationDegrees;
	
	public void setPosition(Float position) {
		this.position = position;		
	}
	
	public Float getPosition() {
		return this.position;
	}
	
	public void setMoveable(boolean isMoveable) {
		this.moveable = isMoveable;		
	}
	
	public boolean isMoveable() {
		return this.moveable;
	}

	public void setRotationDegrees(int rotationDegrees) {
		this.rotationDegrees = rotationDegrees;
	}

	public int getRotationDegrees() {
		return rotationDegrees;
	}
}
