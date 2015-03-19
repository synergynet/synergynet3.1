package synergynet3.activitypack1.web.client.service;

import synergynet3.activitypack1.web.shared.UniverseScenario;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("GravitySimService")
public interface GravitySimService extends RemoteService {

	public void setScenario(UniverseScenario scenario);
	public void clearAllBodies();
	public void increaseGravity();
	public void decreaseGravity();
	public void increaseSimulationSpeed();
	public void decreaseSimulationSpeed();
	public void setBodyLimit(int newLimit);
	
	public static class Util {
		private static GravitySimServiceAsync instance;
		public static GravitySimServiceAsync get(){
			if (instance == null) {
				instance = GWT.create(GravitySimService.class);
			}
			return instance;
		}
	}

}
