package synergynet3.web.commons.client.service;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The Interface SynergyNetWebCommonsServiceAsync.
 */
public interface SynergyNetWebCommonsServiceAsync {

	/**
	 * Gets the devices currently online.
	 *
	 * @param deviceType the device type
	 * @param callback the callback
	 * @return the devices currently online
	 */
	void getDevicesCurrentlyOnline(String deviceType,
			AsyncCallback<List<String>> callback);

}
