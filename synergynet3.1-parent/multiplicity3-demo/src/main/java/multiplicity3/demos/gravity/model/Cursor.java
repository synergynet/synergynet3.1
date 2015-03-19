package multiplicity3.demos.gravity.model;

import com.jme3.math.Vector2f;

public class Cursor {
	
	public enum Mode {
		DRAGGING
	}
	
	public long id;
	public Vector2f startpos = new Vector2f();
	public Mode mode;
	public Vector2f endpos;
	public Vector2f current;

	public Cursor(long cursorID, Vector2f position) {
		this.id = cursorID;
		this.startpos = position.clone();
		this.mode = Mode.DRAGGING;
	}
	
	public void setCurrentPosition(Vector2f pos) {
		this.current = pos.clone();
	}
	
	public void setEndPosition(Vector2f pos) {
		this.endpos = pos.clone();
	}
}
