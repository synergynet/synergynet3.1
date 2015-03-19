package multiplicity3.demos.gravity.model;

import multiplicity3.csys.items.item.IItem;

import com.jme3.math.Vector2f;

public class Body {	
	public MassReference mass;
	public Vector2f velocity;
	private IItem representation;
	private String name;


	public Body(String name, IItem representation, MassReference mass, Vector2f position, Vector2f velocity) {
		this.name = name;
		this.representation = representation;
		this.mass = mass;
		this.velocity = velocity;
	}
	
	public void setPosition(Vector2f pos) {
		representation.setRelativeLocation(pos);
	}

	public Vector2f getPosition() {
		return representation.getRelativeLocation();
	}

	public String getName() {
		return representation.getName();
	}

	public void setPosition(float x, float y) {
		representation.setRelativeLocation(new Vector2f(x,y));		
	}
	

	public IItem getRepresentation() {
		return representation;
	}
	
	public String toString() {
		return name + "[" + mass + "]";
	}
	
}
