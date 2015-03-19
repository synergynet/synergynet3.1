package synergynet3.web.apps.numbernet.comms.table;

import java.util.List;
import java.util.Map;

import synergynet3.cluster.clustereddevice.StudentTableClusteredDevice;
import synergynet3.cluster.sharedmemory.DistributedProperty;
import synergynet3.web.apps.numbernet.shared.CalculatorKey;
import synergynet3.web.apps.numbernet.shared.Participant;
import synergynet3.web.shared.messages.PerformActionMessage;

public class NumberNetStudentTableClusteredData extends StudentTableClusteredDevice {
	public static String deviceType = "student";

	private DistributedProperty<Boolean> calculatorVisibleControl;
	private DistributedProperty<Boolean> scoresVisibleControl;
	private DistributedProperty<Boolean> graphingModeControl;
	private DistributedProperty<Boolean> incorrectExpressionsVisibleControl;
	private DistributedProperty<Boolean> correctExpressionsVisibleControl;
	private DistributedProperty<Boolean> othersIncorrectExpressionsVisibleControl;
	private DistributedProperty<Boolean> othersCorrectExpressionsVisibleControl;	
	private DistributedProperty<PerformActionMessage> resetGraphingLinesControl;	
	
	private DistributedProperty<Double> targetValueControl;
	private DistributedProperty<List<Participant>> participantListControl;	
	
	private DistributedProperty<Map<CalculatorKey,Boolean>> calculatorKeyStateMap;

	public NumberNetStudentTableClusteredData(String deviceName) {
		super(deviceName);
		
		calculatorVisibleControl 					= getDistributedPropertyMap().createDistributedProperty("calculatorvisible");
		graphingModeControl 						= getDistributedPropertyMap().createDistributedProperty("graphingmode");
		scoresVisibleControl 						= getDistributedPropertyMap().createDistributedProperty("scoresvisible");
		incorrectExpressionsVisibleControl 			= getDistributedPropertyMap().createDistributedProperty("incorrectexpressionsvisible");
		correctExpressionsVisibleControl 			= getDistributedPropertyMap().createDistributedProperty("correctexpressionsvisible");
		othersIncorrectExpressionsVisibleControl 	= getDistributedPropertyMap().createDistributedProperty("othersincorrectexpressionsvisible");
		othersCorrectExpressionsVisibleControl 		= getDistributedPropertyMap().createDistributedProperty("otherscorrectexpressionsvisible");		
		targetValueControl 							= getDistributedPropertyMap().createDistributedProperty("targetvalue");
		participantListControl 						= getDistributedPropertyMap().createDistributedProperty("participantlist");
		calculatorKeyStateMap 						= getDistributedPropertyMap().createDistributedProperty("calculatorKeyState");	
		resetGraphingLinesControl 					= getDistributedPropertyMap().createDistributedProperty("resetGraphingLinesControl");		
	}
	
	public DistributedProperty<Map<CalculatorKey, Boolean>> getCalculatorKeyStateMap() {
		return calculatorKeyStateMap;
	}
	
	public DistributedProperty<Boolean> getCalculatorVisibleControlVariable() {
		return calculatorVisibleControl;
	}
	
	public DistributedProperty<Boolean> getScoresVisibleControlVariable() {
		return scoresVisibleControl;
	}
	
	public DistributedProperty<Boolean> getCorrectExpressionsVisibleControlVariable() {
		return correctExpressionsVisibleControl;
	}
	
	public DistributedProperty<Boolean> getIncorrectExpressionsVisibleControlVariable() {
		return incorrectExpressionsVisibleControl;
	}

	public DistributedProperty<List<Participant>> getParticipantListControlVariable() {
		return participantListControl;
	}

	public DistributedProperty<Double> getTargetValueControlVariable() {
		return targetValueControl;
	}

	public DistributedProperty<Boolean> getGraphingModeControl() {
		return graphingModeControl;
	}

	public DistributedProperty<Boolean> getOthersIncorrectExpressionsVisibleControlVariable() {
		return othersIncorrectExpressionsVisibleControl;
	}

	public DistributedProperty<Boolean> getOthersCorrectExpressionsVisibleControlVariable() {
		return othersCorrectExpressionsVisibleControl;
	}
	
	public DistributedProperty<PerformActionMessage> getResetGraphingLinesControl() {
		return resetGraphingLinesControl;
	}
}
