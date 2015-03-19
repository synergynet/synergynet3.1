package synergynet3.web.client.service;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("SynergyNetWebService")
public interface SynergyNetWebService extends RemoteService {
	public void test(String in);
	
	public static class Util {
		private static SynergyNetWebServiceAsync instance;
		public static SynergyNetWebServiceAsync getInstance(){
			if (instance == null) {
				instance = GWT.create(SynergyNetWebService.class);
			}
			return instance;
		}
	}
}
