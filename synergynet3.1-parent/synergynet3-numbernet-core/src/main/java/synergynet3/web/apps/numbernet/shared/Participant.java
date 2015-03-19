package synergynet3.web.apps.numbernet.shared;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Represents an individual person.
 * @author dcs0ah1
 *
 */
public class Participant implements Serializable, IsSerializable {
	private static final long serialVersionUID = 2647062936457560005L;
	private String name;
	
	public Participant() {
		this.name = "<none>";
	}

	public Participant(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public String toString() {
		return getName();
	}
	
}
