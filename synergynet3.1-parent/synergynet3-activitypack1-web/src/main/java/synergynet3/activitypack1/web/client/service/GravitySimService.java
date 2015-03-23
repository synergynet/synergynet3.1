package synergynet3.activitypack1.web.client.service;

import synergynet3.activitypack1.web.shared.UniverseScenario;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The Interface GravitySimService.
 */
@RemoteServiceRelativePath("GravitySimService")
public interface GravitySimService extends RemoteService {

	/**
	 * The Class Util.
	 */
	public static class Util {

		/** The instance. */
		private static GravitySimServiceAsync instance;

		/**
		 * Gets the.
		 *
		 * @return the gravity sim service async
		 */
		public static GravitySimServiceAsync get() {
			if (instance == null) {
				instance = GWT.create(GravitySimService.class);
			}
			return instance;
		}
	}

	/**
	 * Clear all bodies.
	 */
	public void clearAllBodies();

	/**
	 * Decrease gravity.
	 */
	public void decreaseGravity();

	/**
	 * Decrease simulation speed.
	 */
	public void decreaseSimulationSpeed();

	/**
	 * Increase gravity.
	 */
	public void increaseGravity();

	/**
	 * Increase simulation speed.
	 */
	public void increaseSimulationSpeed();

	/**
	 * Sets the body limit.
	 *
	 * @param newLimit the new body limit
	 */
	public void setBodyLimit(int newLimit);

	/**
	 * Sets the scenario.
	 *
	 * @param scenario the new scenario
	 */
	public void setScenario(UniverseScenario scenario);

}
