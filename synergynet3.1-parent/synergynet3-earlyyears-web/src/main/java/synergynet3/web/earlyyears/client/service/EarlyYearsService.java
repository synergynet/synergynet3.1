package synergynet3.web.earlyyears.client.service;

import synergynet3.web.earlyyears.shared.EarlyYearsActivity;
import synergynet3.web.shared.messages.PerformActionMessage;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("EarlyYearsService")
public interface EarlyYearsService extends RemoteService {

	//App functions
	public void setActivity(EarlyYearsActivity scenario, String[]tables);
	
	//Railway app functions
	public void setRailwayCornerNum(int i, String[] tables);
	public void setRailwayCrossNum(int i, String[] tables);
	public void setRailwayStraightNum(int i, String[] tables);
	public void setRoadMode(PerformActionMessage b, String[] tables);
	
	//Explorer app functions
	public void showExplorerTeacherConsole(PerformActionMessage b, String[] deviceToSendTo);
	
	public static class Util {
		private static EarlyYearsServiceAsync instance;
		public static EarlyYearsServiceAsync get(){
			if (instance == null) {
				instance = GWT.create(EarlyYearsService.class);
			}
			return instance;
		}
	}

}