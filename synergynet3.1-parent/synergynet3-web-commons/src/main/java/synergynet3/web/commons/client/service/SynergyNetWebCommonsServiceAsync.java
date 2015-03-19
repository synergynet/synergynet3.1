package synergynet3.web.commons.client.service;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface SynergyNetWebCommonsServiceAsync {

	void getDevicesCurrentlyOnline(String deviceType,
			AsyncCallback<List<String>> callback);

}
