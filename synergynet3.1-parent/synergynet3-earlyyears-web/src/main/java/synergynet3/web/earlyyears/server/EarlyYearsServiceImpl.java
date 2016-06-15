package synergynet3.web.earlyyears.server;

import synergynet3.web.earlyyears.client.EarlyYearsUI;
import synergynet3.web.earlyyears.client.service.EarlyYearsService;
import synergynet3.web.earlyyears.core.EarlyYearsControlComms;
import synergynet3.web.earlyyears.shared.EarlyYearsActivity;
import synergynet3.web.shared.messages.PerformActionMessage;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The Class EarlyYearsServiceImpl.
 */
public class EarlyYearsServiceImpl extends RemoteServiceServlet implements EarlyYearsService
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 888142181330339335L;

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynet3.web.earlyyears.client.service.EarlyYearsService#setActivity
	 * (synergynet3.web.earlyyears.shared.EarlyYearsActivity,
	 * java.lang.String[])
	 */
	@Override
	public void setActivity(EarlyYearsActivity scenario, String[] tables)
	{
		if (tables.length < 1)
		{
			return;
		}
		for (String table : tables)
		{
			if (table.equals(EarlyYearsUI.ALL_TABLES_ID))
			{
				EarlyYearsControlComms.get().setAllTablesScenario(scenario);
				break;
			}
			else
			{
				EarlyYearsControlComms.get().setSpecificTablesScenario(scenario, table);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.web.earlyyears.client.service.EarlyYearsService#
	 * setRailwayCornerNum(int, java.lang.String[])
	 */
	@Override
	public void setRailwayCornerNum(int newNum, String[] tables)
	{
		if (tables.length < 1)
		{
			return;
		}
		for (String table : tables)
		{
			if (table.equals(EarlyYearsUI.ALL_TABLES_ID))
			{
				EarlyYearsControlComms.get().setAllTablesRailwayCorners(newNum);
			}
			else
			{
				EarlyYearsControlComms.get().setSpecificTablesRailwayCorners(newNum, table);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.web.earlyyears.client.service.EarlyYearsService#
	 * setRailwayCrossNum(int, java.lang.String[])
	 */
	@Override
	public void setRailwayCrossNum(int newNum, String[] tables)
	{
		if (tables.length < 1)
		{
			return;
		}
		for (String table : tables)
		{
			if (table.equals(EarlyYearsUI.ALL_TABLES_ID))
			{
				EarlyYearsControlComms.get().setAllTablesRailwayCrosses(newNum);
			}
			else
			{
				EarlyYearsControlComms.get().setSpecificTablesRailwayCrosses(newNum, table);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.web.earlyyears.client.service.EarlyYearsService#
	 * setRailwayStraightNum(int, java.lang.String[])
	 */
	@Override
	public void setRailwayStraightNum(int newNum, String[] tables)
	{
		if (tables.length < 1)
		{
			return;
		}
		for (String table : tables)
		{
			if (table.equals(EarlyYearsUI.ALL_TABLES_ID))
			{
				EarlyYearsControlComms.get().setAllTablesRailwayStraights(newNum);
			}
			else
			{
				EarlyYearsControlComms.get().setSpecificTablesRailwayStraights(newNum, table);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynet3.web.earlyyears.client.service.EarlyYearsService#setRoadMode
	 * (synergynet3.web.shared.messages.PerformActionMessage,
	 * java.lang.String[])
	 */
	@Override
	public void setRoadMode(PerformActionMessage roadMode, String[] tables)
	{
		if (tables.length < 1)
		{
			return;
		}
		for (String table : tables)
		{
			if (table.equals(EarlyYearsUI.ALL_TABLES_ID))
			{
				EarlyYearsControlComms.get().setAllRoadMode(roadMode);
			}
			else
			{
				EarlyYearsControlComms.get().setSpecificRoadMode(roadMode, table);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.web.earlyyears.client.service.EarlyYearsService#
	 * showExplorerTeacherConsole
	 * (synergynet3.web.shared.messages.PerformActionMessage,
	 * java.lang.String[])
	 */
	@Override
	public void showExplorerTeacherConsole(PerformActionMessage show, String[] tables)
	{
		if (tables.length < 1)
		{
			return;
		}
		for (String table : tables)
		{
			if (table.equals(EarlyYearsUI.ALL_TABLES_ID))
			{
				EarlyYearsControlComms.get().setAllTablesExplorerShowTeacherControl(show);
			}
			else
			{
				EarlyYearsControlComms.get().setSpecificTablesExplorerShowTeacherControl(show, table);
			}
		}
	}

}
