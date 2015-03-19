package synergynet3.web.earlyyears.server;

import synergynet3.web.earlyyears.client.EarlyYearsUI;
import synergynet3.web.earlyyears.client.service.EarlyYearsService;
import synergynet3.web.earlyyears.core.EarlyYearsControlComms;
import synergynet3.web.earlyyears.shared.EarlyYearsActivity;
import synergynet3.web.shared.messages.PerformActionMessage;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class EarlyYearsServiceImpl extends RemoteServiceServlet implements EarlyYearsService {

	private static final long serialVersionUID = 888142181330339335L;
	
	@Override
	public void setActivity(EarlyYearsActivity scenario, String[] tables) {
		if (tables.length < 1)return;
		for (String table: tables){
			if (table.equals(EarlyYearsUI.ALL_TABLES_ID)){
				EarlyYearsControlComms.get().setAllTablesScenario(scenario);
				break;
			}else{
				EarlyYearsControlComms.get().setSpecificTablesScenario(scenario, table);
			}
		}
	}

	@Override
	public void setRailwayCornerNum(int newNum, String[] tables) {
		if (tables.length < 1)return;
		for (String table: tables){
			if (table.equals(EarlyYearsUI.ALL_TABLES_ID)){
				EarlyYearsControlComms.get().setAllTablesRailwayCorners(newNum);
			}else{
				EarlyYearsControlComms.get().setSpecificTablesRailwayCorners(newNum, table);
			}
		}
	}

	@Override
	public void setRailwayCrossNum(int newNum, String[] tables) {
		if (tables.length < 1)return;
		for (String table: tables){
			if (table.equals(EarlyYearsUI.ALL_TABLES_ID)){
				EarlyYearsControlComms.get().setAllTablesRailwayCrosses(newNum);
			}else{
				EarlyYearsControlComms.get().setSpecificTablesRailwayCrosses(newNum, table);
			}
		}
	}

	@Override
	public void setRailwayStraightNum(int newNum, String[] tables) {
		if (tables.length < 1)return;
		for (String table: tables){
			if (table.equals(EarlyYearsUI.ALL_TABLES_ID)){
				EarlyYearsControlComms.get().setAllTablesRailwayStraights(newNum);
			}else{
				EarlyYearsControlComms.get().setSpecificTablesRailwayStraights(newNum, table);
			}
		}
	}
	
	@Override
	public void setRoadMode(PerformActionMessage roadMode, String[] tables) {
		if (tables.length < 1)return;
		for (String table: tables){
			if (table.equals(EarlyYearsUI.ALL_TABLES_ID)){
				EarlyYearsControlComms.get().setAllRoadMode(roadMode);
			}else{
				EarlyYearsControlComms.get().setSpecificRoadMode(roadMode, table);
			}
		}
	}

	@Override
	public void showExplorerTeacherConsole(PerformActionMessage show, String[] tables) {
		if (tables.length < 1)return;
		for (String table: tables){
			if (table.equals(EarlyYearsUI.ALL_TABLES_ID)){
				EarlyYearsControlComms.get().setAllTablesExplorerShowTeacherControl(show);
			}else{
				EarlyYearsControlComms.get().setSpecificTablesExplorerShowTeacherControl(show, table);
			}
		}		
	}

}
