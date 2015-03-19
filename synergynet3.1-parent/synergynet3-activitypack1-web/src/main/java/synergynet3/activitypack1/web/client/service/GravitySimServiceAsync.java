package synergynet3.activitypack1.web.client.service;

import synergynet3.activitypack1.web.shared.UniverseScenario;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface GravitySimServiceAsync {

	void setScenario(UniverseScenario scenario, AsyncCallback<Void> callback);
	void clearAllBodies(AsyncCallback<Void> callback);
	void increaseGravity(AsyncCallback<Void> callback);
	void decreaseGravity(AsyncCallback<Void> callback);
	void increaseSimulationSpeed(AsyncCallback<Void> callback);
	void decreaseSimulationSpeed(AsyncCallback<Void> callback);
	void setBodyLimit(int newLimit, AsyncCallback<Void> callback);

}
