package synergynet3.web.commons.client.service;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("SynergyNetWebCommonsService")
public interface SynergyNetWebCommonsService extends RemoteService {

	List<String> getDevicesCurrentlyOnline(String deviceType);
	
	public static class Util {
		private static SynergyNetWebCommonsServiceAsync instance;
		public static SynergyNetWebCommonsServiceAsync getInstance(){
			if (instance == null) {
				instance = GWT.create(SynergyNetWebCommonsService.class);
			}
			return instance;
		}
	}
	
}
