package synergynet3.web.apps.numbernet.comms.table;

import java.util.List;
import java.util.Map;

import synergynet3.cluster.clustereddevice.StudentTableClusteredDevice;
import synergynet3.cluster.sharedmemory.DistributedProperty;
import synergynet3.web.apps.numbernet.shared.CalculatorKey;
import synergynet3.web.apps.numbernet.shared.Participant;
import synergynet3.web.shared.messages.PerformActionMessage;

/**
 * The Class NumberNetStudentTableClusteredData.
 */
public class NumberNetStudentTableClusteredData extends
		StudentTableClusteredDevice {

	/** The device type. */
	public static String deviceType = "student";

	/** The calculator key state map. */
	private DistributedProperty<Map<CalculatorKey, Boolean>> calculatorKeyStateMap;

	/** The calculator visible control. */
	private DistributedProperty<Boolean> calculatorVisibleControl;

	/** The correct expressions visible control. */
	private DistributedProperty<Boolean> correctExpressionsVisibleControl;

	/** The graphing mode control. */
	private DistributedProperty<Boolean> graphingModeControl;

	/** The incorrect expressions visible control. */
	private DistributedProperty<Boolean> incorrectExpressionsVisibleControl;

	/** The others correct expressions visible control. */
	private DistributedProperty<Boolean> othersCorrectExpressionsVisibleControl;

	/** The others incorrect expressions visible control. */
	private DistributedProperty<Boolean> othersIncorrectExpressionsVisibleControl;

	/** The participant list control. */
	private DistributedProperty<List<Participant>> participantListControl;

	/** The reset graphing lines control. */
	private DistributedProperty<PerformActionMessage> resetGraphingLinesControl;

	/** The scores visible control. */
	private DistributedProperty<Boolean> scoresVisibleControl;

	/** The target value control. */
	private DistributedProperty<Double> targetValueControl;

	/**
	 * Instantiates a new number net student table clustered data.
	 *
	 * @param deviceName the device name
	 */
	public NumberNetStudentTableClusteredData(String deviceName) {
		super(deviceName);

		calculatorVisibleControl = getDistributedPropertyMap()
				.createDistributedProperty("calculatorvisible");
		graphingModeControl = getDistributedPropertyMap()
				.createDistributedProperty("graphingmode");
		scoresVisibleControl = getDistributedPropertyMap()
				.createDistributedProperty("scoresvisible");
		incorrectExpressionsVisibleControl = getDistributedPropertyMap()
				.createDistributedProperty("incorrectexpressionsvisible");
		correctExpressionsVisibleControl = getDistributedPropertyMap()
				.createDistributedProperty("correctexpressionsvisible");
		othersIncorrectExpressionsVisibleControl = getDistributedPropertyMap()
				.createDistributedProperty("othersincorrectexpressionsvisible");
		othersCorrectExpressionsVisibleControl = getDistributedPropertyMap()
				.createDistributedProperty("otherscorrectexpressionsvisible");
		targetValueControl = getDistributedPropertyMap()
				.createDistributedProperty("targetvalue");
		participantListControl = getDistributedPropertyMap()
				.createDistributedProperty("participantlist");
		calculatorKeyStateMap = getDistributedPropertyMap()
				.createDistributedProperty("calculatorKeyState");
		resetGraphingLinesControl = getDistributedPropertyMap()
				.createDistributedProperty("resetGraphingLinesControl");
	}

	/**
	 * Gets the calculator key state map.
	 *
	 * @return the calculator key state map
	 */
	public DistributedProperty<Map<CalculatorKey, Boolean>> getCalculatorKeyStateMap() {
		return calculatorKeyStateMap;
	}

	/**
	 * Gets the calculator visible control variable.
	 *
	 * @return the calculator visible control variable
	 */
	public DistributedProperty<Boolean> getCalculatorVisibleControlVariable() {
		return calculatorVisibleControl;
	}

	/**
	 * Gets the correct expressions visible control variable.
	 *
	 * @return the correct expressions visible control variable
	 */
	public DistributedProperty<Boolean> getCorrectExpressionsVisibleControlVariable() {
		return correctExpressionsVisibleControl;
	}

	/**
	 * Gets the graphing mode control.
	 *
	 * @return the graphing mode control
	 */
	public DistributedProperty<Boolean> getGraphingModeControl() {
		return graphingModeControl;
	}

	/**
	 * Gets the incorrect expressions visible control variable.
	 *
	 * @return the incorrect expressions visible control variable
	 */
	public DistributedProperty<Boolean> getIncorrectExpressionsVisibleControlVariable() {
		return incorrectExpressionsVisibleControl;
	}

	/**
	 * Gets the others correct expressions visible control variable.
	 *
	 * @return the others correct expressions visible control variable
	 */
	public DistributedProperty<Boolean> getOthersCorrectExpressionsVisibleControlVariable() {
		return othersCorrectExpressionsVisibleControl;
	}

	/**
	 * Gets the others incorrect expressions visible control variable.
	 *
	 * @return the others incorrect expressions visible control variable
	 */
	public DistributedProperty<Boolean> getOthersIncorrectExpressionsVisibleControlVariable() {
		return othersIncorrectExpressionsVisibleControl;
	}

	/**
	 * Gets the participant list control variable.
	 *
	 * @return the participant list control variable
	 */
	public DistributedProperty<List<Participant>> getParticipantListControlVariable() {
		return participantListControl;
	}

	/**
	 * Gets the reset graphing lines control.
	 *
	 * @return the reset graphing lines control
	 */
	public DistributedProperty<PerformActionMessage> getResetGraphingLinesControl() {
		return resetGraphingLinesControl;
	}

	/**
	 * Gets the scores visible control variable.
	 *
	 * @return the scores visible control variable
	 */
	public DistributedProperty<Boolean> getScoresVisibleControlVariable() {
		return scoresVisibleControl;
	}

	/**
	 * Gets the target value control variable.
	 *
	 * @return the target value control variable
	 */
	public DistributedProperty<Double> getTargetValueControlVariable() {
		return targetValueControl;
	}
}
