package synergynet3.apps.numbernet.network;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import synergynet3.apps.numbernet.controller.numbernettable.CalculatorCollectionManager;
import synergynet3.apps.numbernet.ui.calculator.Calculator;
import synergynet3.cluster.sharedmemory.DistributedPropertyChangedAction;
import synergynet3.web.apps.numbernet.comms.table.NumberNetStudentTableClusteredData;
import synergynet3.web.apps.numbernet.shared.CalculatorKey;

import com.hazelcast.core.Member;

/**
 * The Class CalculatorKeySynchronizer.
 */
public class CalculatorKeySynchronizer
{

	/** The calculator collection manager. */
	private CalculatorCollectionManager calculatorCollectionManager;

	/** The student table data cluster. */
	private NumberNetStudentTableClusteredData studentTableDataCluster;

	/**
	 * Instantiates a new calculator key synchronizer.
	 *
	 * @param calculatorCollectionManager
	 *            the calculator collection manager
	 * @param studentTableDataCluster
	 *            the student table data cluster
	 */
	public CalculatorKeySynchronizer(CalculatorCollectionManager calculatorCollectionManager, NumberNetStudentTableClusteredData studentTableDataCluster)
	{
		this.calculatorCollectionManager = calculatorCollectionManager;
		this.studentTableDataCluster = studentTableDataCluster;
	}

	/**
	 * Start.
	 */
	public void start()
	{
		this.studentTableDataCluster.getCalculatorKeyStateMap().registerChangeListener(new DistributedPropertyChangedAction<Map<CalculatorKey, Boolean>>()
		{
			@Override
			public void distributedPropertyDidChange(Member m, Map<CalculatorKey, Boolean> oldValue, Map<CalculatorKey, Boolean> newValue)
			{
				updateAllCalculatorsSetKeyStateWithValue(newValue);
			}
		});
	}

	/**
	 * Update calculator set key state with value.
	 *
	 * @param calc
	 *            the calc
	 * @param itemKey
	 *            the item key
	 * @param state
	 *            the state
	 */
	private void updateCalculatorSetKeyStateWithValue(Calculator calc, CalculatorKey itemKey, Boolean state)
	{
		calc.setKeyVisible(itemKey.getStringRepresentation(), state);
	}

	/**
	 * Update all calculators set key state with value.
	 *
	 * @param keyMap
	 *            the key map
	 */
	protected void updateAllCalculatorsSetKeyStateWithValue(Map<CalculatorKey, Boolean> keyMap)
	{
		Collection<String> users = calculatorCollectionManager.getUsersWhoHaveCalculators();
		for (String user : users)
		{
			Calculator calc = calculatorCollectionManager.getCalculatorForUser(user);
			for (Entry<CalculatorKey, Boolean> entry : keyMap.entrySet())
			{
				updateCalculatorSetKeyStateWithValue(calc, entry.getKey(), entry.getValue());
			}
		}

	}

}
