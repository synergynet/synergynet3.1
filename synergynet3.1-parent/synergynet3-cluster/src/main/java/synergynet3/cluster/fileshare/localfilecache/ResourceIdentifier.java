package synergynet3.cluster.fileshare.localfilecache;

import java.io.Serializable;

public class ResourceIdentifier implements Serializable {
	private static final long serialVersionUID = 5375357594780526289L;
	
	private String uniqueID;

	public ResourceIdentifier(String uniqueID) {
		this.uniqueID = uniqueID;
	}
	
	public String getID() {
		return this.uniqueID;
	}
}
