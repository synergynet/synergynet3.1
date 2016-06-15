package synergynet3.web.earlyyears.client.service;

import synergynet3.web.earlyyears.shared.EarlyYearsActivity;
import synergynet3.web.shared.messages.PerformActionMessage;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The Interface EarlyYearsService.
 */
@RemoteServiceRelativePath("EarlyYearsService")
public interface EarlyYearsService extends RemoteService
{

	/**
	 * The Class Util.
	 */
	public static class Util
	{

		/** The instance. */
		private static EarlyYearsServiceAsync instance;

		/**
		 * Gets the.
		 *
		 * @return the early years service async
		 */
		public static EarlyYearsServiceAsync get()
		{
			if (instance == null)
			{
				instance = GWT.create(EarlyYearsService.class);
			}
			return instance;
		}
	}

	// App functions
	/**
	 * Sets the activity.
	 *
	 * @param scenario
	 *            the scenario
	 * @param tables
	 *            the tables
	 */
	public void setActivity(EarlyYearsActivity scenario, String[] tables);

	// Railway app functions
	/**
	 * Sets the railway corner num.
	 *
	 * @param i
	 *            the i
	 * @param tables
	 *            the tables
	 */
	public void setRailwayCornerNum(int i, String[] tables);

	/**
	 * Sets the railway cross num.
	 *
	 * @param i
	 *            the i
	 * @param tables
	 *            the tables
	 */
	public void setRailwayCrossNum(int i, String[] tables);

	/**
	 * Sets the railway straight num.
	 *
	 * @param i
	 *            the i
	 * @param tables
	 *            the tables
	 */
	public void setRailwayStraightNum(int i, String[] tables);

	/**
	 * Sets the road mode.
	 *
	 * @param b
	 *            the b
	 * @param tables
	 *            the tables
	 */
	public void setRoadMode(PerformActionMessage b, String[] tables);

	// Explorer app functions
	/**
	 * Show explorer teacher console.
	 *
	 * @param b
	 *            the b
	 * @param deviceToSendTo
	 *            the device to send to
	 */
	public void showExplorerTeacherConsole(PerformActionMessage b, String[] deviceToSendTo);

}
