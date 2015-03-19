package synergynet3.web.apps.numbernet.shared;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Edge implements Serializable, IsSerializable {
	private static final long serialVersionUID = -738061162769704771L;
	
	public Node a;
	public Node b;
		
	public Edge(Node a, Node b) {
		this.a = a;
		this.b = b;
	}

	public float getLength() {
		float diffX = b.getX() - a.getX();
		float diffY = b.getY() - a.getY();
		return (float)Math.sqrt(diffX * diffX + diffY * diffY);
	}

	public String getKey() {
		return a.getID() + "-" + b.getID();
	}
}
