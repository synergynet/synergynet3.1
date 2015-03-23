package synergynet3.web.commons.client.service;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The Interface SynergyNetWebCommonsService.
 */
@RemoteServiceRelativePath("SynergyNetWebCommonsService")
public interface SynergyNetWebCommonsService extends RemoteService {

	/**
	 * The Class Util.
	 */
	public static class Util {

		/** The instance. */
		private static SynergyNetWebCommonsServiceAsync instance;

		/**
		 * Gets the single instance of Util.
		 *
		 * @return single instance of Util
		 */
		public static SynergyNetWebCommonsServiceAsync getInstance() {
			if (instance == null) {
				instance = GWT.create(SynergyNetWebCommonsService.class);
			}
			return instance;
		}
	}

	/**
	 * Gets the devices currently online.
	 *
	 * @param deviceType the device type
	 * @return the devices currently online
	 */
	List<String> getDevicesCurrentlyOnline(String deviceType);

}
