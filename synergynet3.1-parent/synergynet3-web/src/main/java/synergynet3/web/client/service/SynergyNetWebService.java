package synergynet3.web.client.service;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The Interface SynergyNetWebService.
 */
@RemoteServiceRelativePath("SynergyNetWebService")
public interface SynergyNetWebService extends RemoteService {

	/**
	 * The Class Util.
	 */
	public static class Util {

		/** The instance. */
		private static SynergyNetWebServiceAsync instance;

		/**
		 * Gets the single instance of Util.
		 *
		 * @return single instance of Util
		 */
		public static SynergyNetWebServiceAsync getInstance() {
			if (instance == null) {
				instance = GWT.create(SynergyNetWebService.class);
			}
			return instance;
		}
	}

	/**
	 * Test.
	 *
	 * @param in the in
	 */
	public void test(String in);
}
