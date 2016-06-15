package synergynet3.activitypack1.web.client.service;

import synergynet3.activitypack1.web.shared.UniverseScenario;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The Interface GravitySimServiceAsync.
 */
public interface GravitySimServiceAsync
{

	/**
	 * Clear all bodies.
	 *
	 * @param callback
	 *            the callback
	 */
	void clearAllBodies(AsyncCallback<Void> callback);

	/**
	 * Decrease gravity.
	 *
	 * @param callback
	 *            the callback
	 */
	void decreaseGravity(AsyncCallback<Void> callback);

	/**
	 * Decrease simulation speed.
	 *
	 * @param callback
	 *            the callback
	 */
	void decreaseSimulationSpeed(AsyncCallback<Void> callback);

	/**
	 * Increase gravity.
	 *
	 * @param callback
	 *            the callback
	 */
	void increaseGravity(AsyncCallback<Void> callback);

	/**
	 * Increase simulation speed.
	 *
	 * @param callback
	 *            the callback
	 */
	void increaseSimulationSpeed(AsyncCallback<Void> callback);

	/**
	 * Sets the body limit.
	 *
	 * @param newLimit
	 *            the new limit
	 * @param callback
	 *            the callback
	 */
	void setBodyLimit(int newLimit, AsyncCallback<Void> callback);

	/**
	 * Sets the scenario.
	 *
	 * @param scenario
	 *            the scenario
	 * @param callback
	 *            the callback
	 */
	void setScenario(UniverseScenario scenario, AsyncCallback<Void> callback);

}
