package synergynet3.cluster.fileshare.localfilecache;

import java.io.Serializable;

/**
 * The Class ResourceIdentifier.
 */
public class ResourceIdentifier implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 5375357594780526289L;

	/** The unique id. */
	private String uniqueID;

	/**
	 * Instantiates a new resource identifier.
	 *
	 * @param uniqueID the unique id
	 */
	public ResourceIdentifier(String uniqueID) {
		this.uniqueID = uniqueID;
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public String getID() {
		return this.uniqueID;
	}
}
