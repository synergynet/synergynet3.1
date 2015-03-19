package synergynet3.web.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface SynergyNetWebServiceAsync {

	void test(String in, AsyncCallback<Void> callback);

}
