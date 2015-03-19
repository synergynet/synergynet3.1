package synergynet3.apps.numbernet.network;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import com.hazelcast.core.Member;

import synergynet3.apps.numbernet.controller.numbernettable.CalculatorCollectionManager;
import synergynet3.apps.numbernet.ui.calculator.Calculator;
import synergynet3.cluster.sharedmemory.DistributedPropertyChangedAction;
import synergynet3.web.apps.numbernet.comms.table.NumberNetStudentTableClusteredData;
import synergynet3.web.apps.numbernet.shared.CalculatorKey;

public class CalculatorKeySynchronizer {
	private CalculatorCollectionManager calculatorCollectionManager;
	private NumberNetStudentTableClusteredData studentTableDataCluster;

	public CalculatorKeySynchronizer(
			CalculatorCollectionManager calculatorCollectionManager, NumberNetStudentTableClusteredData studentTableDataCluster) {
		this.calculatorCollectionManager = calculatorCollectionManager;
		this.studentTableDataCluster = studentTableDataCluster;
	}

	public void start() 
	{		
		this.studentTableDataCluster.getCalculatorKeyStateMap().registerChangeListener(new DistributedPropertyChangedAction<Map<CalculatorKey, Boolean>>() {			
			@Override
			public void distributedPropertyDidChange(Member m, Map<CalculatorKey, Boolean> oldValue, Map<CalculatorKey, Boolean> newValue) {
				updateAllCalculatorsSetKeyStateWithValue(newValue);		
			}
		});		
	}

	protected void updateAllCalculatorsSetKeyStateWithValue(Map<CalculatorKey, Boolean> keyMap) 
	{		
		Collection<String> users = calculatorCollectionManager.getUsersWhoHaveCalculators();
		for(String user : users) {
			Calculator calc = calculatorCollectionManager.getCalculatorForUser(user);
			for (Entry<CalculatorKey, Boolean> entry: keyMap.entrySet())updateCalculatorSetKeyStateWithValue(calc, entry.getKey(), entry.getValue());
		}

	}

	private void updateCalculatorSetKeyStateWithValue(Calculator calc,
			CalculatorKey itemKey, Boolean state) 
	{
		calc.setKeyVisible(itemKey.getStringRepresentation(), state);
	}

}
