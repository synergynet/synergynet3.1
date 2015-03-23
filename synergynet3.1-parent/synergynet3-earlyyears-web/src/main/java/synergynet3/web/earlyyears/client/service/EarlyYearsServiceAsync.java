package synergynet3.web.earlyyears.client.service;

import synergynet3.web.earlyyears.shared.EarlyYearsActivity;
import synergynet3.web.shared.messages.PerformActionMessage;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The Interface EarlyYearsServiceAsync.
 */
public interface EarlyYearsServiceAsync {

	/**
	 * Sets the activity.
	 *
	 * @param scenario the scenario
	 * @param tables the tables
	 * @param callback the callback
	 */
	void setActivity(EarlyYearsActivity scenario, String[] tables,
			AsyncCallback<Void> callback);

	/**
	 * Sets the railway corner num.
	 *
	 * @param i the i
	 * @param tables the tables
	 * @param asyncCallback the async callback
	 */
	void setRailwayCornerNum(int i, String[] tables,
			AsyncCallback<Void> asyncCallback);

	/**
	 * Sets the railway cross num.
	 *
	 * @param i the i
	 * @param tables the tables
	 * @param asyncCallback the async callback
	 */
	void setRailwayCrossNum(int i, String[] tables,
			AsyncCallback<Void> asyncCallback);

	/**
	 * Sets the railway straight num.
	 *
	 * @param i the i
	 * @param tables the tables
	 * @param asyncCallback the async callback
	 */
	void setRailwayStraightNum(int i, String[] tables,
			AsyncCallback<Void> asyncCallback);

	/**
	 * Sets the road mode.
	 *
	 * @param b the b
	 * @param tables the tables
	 * @param asyncCallback the async callback
	 */
	void setRoadMode(PerformActionMessage b, String[] tables,
			AsyncCallback<Void> asyncCallback);

	/**
	 * Show explorer teacher console.
	 *
	 * @param b the b
	 * @param deviceToSendTo the device to send to
	 * @param asyncCallback the async callback
	 */
	void showExplorerTeacherConsole(PerformActionMessage b,
			String[] deviceToSendTo, AsyncCallback<Void> asyncCallback);

}
