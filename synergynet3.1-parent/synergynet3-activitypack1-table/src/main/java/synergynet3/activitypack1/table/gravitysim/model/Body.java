package synergynet3.activitypack1.table.gravitysim.model;

import com.jme3.math.Vector2f;

public class Body {
	private static int globalId = 0;
	
	private int id = 0;
	
	public MassReference mass;
	public Vector2f velocity = new Vector2f();
	private String name;
	private Vector2f pos = new Vector2f();
	private boolean ignore = true;

	public Body(String name, MassReference mass, Vector2f position, Vector2f velocity) {
		id = globalId;
		globalId++;
		this.name = name;
		this.mass = mass;
		this.velocity = velocity;
		this.pos = position;
	}
	
	public int getId() {
		return this.id;
	}
	
	public boolean shouldIgnore() {
		return ignore;
	}
	
	public void setIgnore() {
		ignore = true;
	}
	
	public void setActive() {
		ignore = false;
	}
	
	public void setPosition(Vector2f pos) {
		this.pos.set(pos);
	}

	public Vector2f getPosition() {
		return pos;
	}

	public String getName() {
		return this.name;
	}

	public void setPosition(float x, float y) {
		this.pos.set(x,y);
	}
	
	public String toString() {
		return name + "[" + mass + "]";
	}

	public void setVelocity(Vector2f v) {
		this.velocity.set(v);
	}

}
