package synergynet3.cluster.xmpp.presence;

/**
 * Very simple presence support.
 * 
 * @author ashatch
 */
public interface IPresenceListener {
	/**
	 * A device has become available after previouly being unavailable. This
	 * will be called if the device was already online as this system joins the
	 * network.
	 * 
	 * @param id
	 */
	public void deviceAvailable(String id);

	/**
	 * A device has become unavailable after previously being seen as available
	 * while this system is online.
	 * 
	 * @param id
	 */
	public void deviceUnavailable(String id);
}
