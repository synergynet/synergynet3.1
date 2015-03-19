package synergynet3.web.earlyyears.client.service;

import synergynet3.web.earlyyears.shared.EarlyYearsActivity;
import synergynet3.web.shared.messages.PerformActionMessage;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface EarlyYearsServiceAsync {

	void setActivity(EarlyYearsActivity scenario, String[] tables, AsyncCallback<Void> callback);
	void setRailwayCornerNum(int i, String[] tables, AsyncCallback<Void> asyncCallback);
	void setRailwayCrossNum(int i, String[] tables, AsyncCallback<Void> asyncCallback);
	void setRailwayStraightNum(int i, String[] tables, AsyncCallback<Void> asyncCallback);
	void setRoadMode(PerformActionMessage b, String[] tables, AsyncCallback<Void> asyncCallback);
	void showExplorerTeacherConsole(PerformActionMessage b, String[] deviceToSendTo, AsyncCallback<Void> asyncCallback);

}
