package synergynet3.web.apps.numbernet.shared;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Node implements Serializable, IsSerializable {
	private static final long serialVersionUID = 3955541802925698325L;
	
	private float x;
	private float y;
	private boolean ignore;
	private String id;
	
	public Node(String id) {
		this.id = id;
	}
	
	public String getID() {
		return id;
	}
	
	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public float getX() {
		return this.x;
	}
	
	public float getY() {
		return this.y;
	}
	
	@Override
	public int hashCode() {
		return id.hashCode();
	}

	public void setIgnore(boolean b) {
		ignore = b;		
	}
	
	public boolean shouldIgnore() {
		return ignore;
	}
}
