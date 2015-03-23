package synergynet3.cluster.fileshare;

import java.net.URL;

import synergynet3.cluster.fileshare.localfilecache.MD5Hash;

/**
 * When registering or retrieving content through an
 * {@link IFileDistributionManager}, changes in status are reported through this
 * interface.
 * 
 * @author dcs0ah1
 */
public interface FileShareSystemContentRegistrationDelegate {
	/**
	 * Called if there was a problem retrieving the requested content.
	 * 
	 * @param contentID
	 */
	public void contentNotReceived(MD5Hash contentID);

	/**
	 * Content was succesfully retrieved, and is available at the provided URL.
	 * If the content was retrieved locally (e.g. by some file cache), then the
	 * <code>wasLocal</code> flag will be <code>true</code>.
	 * 
	 * @param contentID
	 * @param url
	 * @param wasLocal
	 */
	public void contentReceived(MD5Hash contentID, URL url, boolean wasLocal);

}
