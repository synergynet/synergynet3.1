package synergynet3.web.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The Interface SynergyNetWebServiceAsync.
 */
public interface SynergyNetWebServiceAsync {

	/**
	 * Test.
	 *
	 * @param in the in
	 * @param callback the callback
	 */
	void test(String in, AsyncCallback<Void> callback);

}
