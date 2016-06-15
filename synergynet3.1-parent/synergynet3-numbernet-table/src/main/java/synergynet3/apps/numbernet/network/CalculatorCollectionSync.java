package synergynet3.apps.numbernet.network;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import synergynet3.apps.numbernet.controller.numbernettable.CalculatorCollectionManager;
import synergynet3.cluster.sharedmemory.DistributedPropertyChangedAction;
import synergynet3.web.apps.numbernet.comms.table.NumberNetStudentTableClusteredData;
import synergynet3.web.apps.numbernet.shared.Participant;

import com.hazelcast.core.Member;

/**
 * The Class CalculatorCollectionSync.
 */
public class CalculatorCollectionSync
{

	/** The Constant log. */
	private static final Logger log = Logger.getLogger(CalculatorCollectionSync.class.getName());

	/** The calculator collection manager. */
	private CalculatorCollectionManager calculatorCollectionManager;

	/** The table cluster data. */
	private NumberNetStudentTableClusteredData tableClusterData;

	/**
	 * Instantiates a new calculator collection sync.
	 *
	 * @param calculatorCollectionManager
	 *            the calculator collection manager
	 * @param tableClusterData
	 *            the table cluster data
	 */
	public CalculatorCollectionSync(CalculatorCollectionManager calculatorCollectionManager, NumberNetStudentTableClusteredData tableClusterData)
	{
		this.calculatorCollectionManager = calculatorCollectionManager;
		this.tableClusterData = tableClusterData;
	}

	/**
	 * Start.
	 */
	public void start()
	{
		initCalculatorCollectionSync();
		initCalculatorVisibilitySync();
	}

	/**
	 * Stop.
	 */
	public void stop()
	{
		// nothing to do
	}

	/**
	 * Gets the names list from participant list.
	 *
	 * @param list
	 *            the list
	 * @return the names list from participant list
	 */
	private List<String> getNamesListFromParticipantList(List<Participant> list)
	{
		List<String> names = new ArrayList<String>();
		if (list == null)
		{
			return names;
		}
		for (Participant p : list)
		{
			names.add(p.getName());
		}
		return names;
	}

	/**
	 * Inits the calculator collection sync.
	 */
	private void initCalculatorCollectionSync()
	{
		List<String> participants = getNamesListFromParticipantList(tableClusterData.getParticipantListControlVariable().getValue());
		calculatorCollectionManager.setAvailableCalculatorCollection(participants);

		tableClusterData.getParticipantListControlVariable().registerChangeListener(new DistributedPropertyChangedAction<List<Participant>>()
		{
			@Override
			public void distributedPropertyDidChange(Member m, List<Participant> oldValue, List<Participant> newValue)
			{
				log.fine("Update received for participant list: " + newValue);
				List<String> names = getNamesListFromParticipantList(newValue);
				calculatorCollectionManager.setAvailableCalculatorCollection(names);
			}
		});
	}

	/**
	 * Inits the calculator visibility sync.
	 */
	private void initCalculatorVisibilitySync()
	{
		tableClusterData.getCalculatorVisibleControlVariable().registerChangeListener(new DistributedPropertyChangedAction<Boolean>()
		{
			@Override
			public void distributedPropertyDidChange(Member m, Boolean oldValue, Boolean newValue)
			{
				log.finer("Visibility property change");
				calculatorCollectionManager.setAllCalculatorsVisible(newValue);
			}
		});
	}

}
