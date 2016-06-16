package synergynet3.tracking.network.core;

import java.util.ArrayList;

import synergynet3.cluster.clustereddevice.StudentTableClusteredDevice;
import synergynet3.cluster.sharedmemory.DistributedProperty;
import synergynet3.tracking.network.shared.CombinedUserEntity;
import synergynet3.tracking.network.shared.PointDirection;
import synergynet3.tracking.network.shared.UserLocations;
import synergynet3.web.shared.messages.PerformActionMessage;

/**
 * The Class TrackingDeviceControl.
 */
public class TrackingDeviceControl extends StudentTableClusteredDevice
{

	/** The all tables selected mode enabled control variable. */
	private DistributedProperty<PerformActionMessage> allTablesSelectedModeEnabledControlVariable;

	/** The gesture mode control variable. */
	private DistributedProperty<Integer> gestureModeControlVariable;

	/** The individual table select mode enabled control variable. */
	private DistributedProperty<PerformActionMessage> individualTableSelectModeEnabledControlVariable;

	/** The pointing control variable. */
	private DistributedProperty<PointDirection> pointingControlVariable;

	/** The table deselected control. */
	private DistributedProperty<String> tableDeselectedControl;

	/** The table selectd mode disabled control variable. */
	private DistributedProperty<PerformActionMessage> tableSelectdModeDisabledControlVariable;

	/** The table selected control. */
	private DistributedProperty<String> tableSelectedControl;

	/** The teacher status control variable. */
	private DistributedProperty<String> teacherStatusControlVariable;

	/** The teacher status to tracker control variable. */
	private DistributedProperty<String> teacherStatusToTrackerControlVariable;

	/** The tracked user locations control. */
	private DistributedProperty<UserLocations> trackedUserLocationsControl;

	/** The unique id to tracker control variable. */
	private DistributedProperty<String> uniqueIDToTrackerControlVariable;

	/** The user locations control. */
	private DistributedProperty<ArrayList<CombinedUserEntity>> userLocationsControl;

	/**
	 * Instantiates a new tracking device control.
	 *
	 * @param deviceName
	 *            the device name
	 */
	public TrackingDeviceControl(String deviceName)
	{
		super(deviceName);
		trackedUserLocationsControl = getDistributedPropertyMap().createDistributedProperty("trackedIserLocationsControlVariable");
		initWithDefault(trackedUserLocationsControl, new UserLocations(""));
		tableSelectedControl = getDistributedPropertyMap().createDistributedProperty("tableSelectedControl");
		initWithDefault(tableSelectedControl, "");
		tableDeselectedControl = getDistributedPropertyMap().createDistributedProperty("tableDeselectedControl");
		initWithDefault(tableDeselectedControl, "");
		allTablesSelectedModeEnabledControlVariable = getDistributedPropertyMap().createDistributedProperty("allTablesSelectedModeEnabledControlVariable");
		initWithDefault(allTablesSelectedModeEnabledControlVariable, new PerformActionMessage());
		individualTableSelectModeEnabledControlVariable = getDistributedPropertyMap().createDistributedProperty("individualTableSelectModeEnabledControlVariable");
		initWithDefault(individualTableSelectModeEnabledControlVariable, new PerformActionMessage());
		tableSelectdModeDisabledControlVariable = getDistributedPropertyMap().createDistributedProperty("tableSelectdModeDisabledControlVariable");
		initWithDefault(tableSelectdModeDisabledControlVariable, new PerformActionMessage());
		gestureModeControlVariable = getDistributedPropertyMap().createDistributedProperty("gestureModeControlVariable");
		initWithDefault(gestureModeControlVariable, -1);
		userLocationsControl = getDistributedPropertyMap().createDistributedProperty("userLocationsControlVariable");
		initWithDefault(userLocationsControl, new ArrayList<CombinedUserEntity>());
		teacherStatusControlVariable = getDistributedPropertyMap().createDistributedProperty("teacherStatusControlVariable");
		initWithDefault(teacherStatusControlVariable, "");
		teacherStatusToTrackerControlVariable = getDistributedPropertyMap().createDistributedProperty("teacherStatusToTrackerControlVariable");
		initWithDefault(teacherStatusToTrackerControlVariable, "");
		uniqueIDToTrackerControlVariable = getDistributedPropertyMap().createDistributedProperty("uniqueIDToTrackerControlVariable");
		initWithDefault(uniqueIDToTrackerControlVariable, "");
		pointingControlVariable = getDistributedPropertyMap().createDistributedProperty("pointingControlVariable");
		initWithDefault(pointingControlVariable, new PointDirection());
	}

	/**
	 * Gets the all tables selected mode enabled control variable.
	 *
	 * @return the all tables selected mode enabled control variable
	 */
	public DistributedProperty<PerformActionMessage> getAllTablesSelectedModeEnabledControlVariable()
	{
		return allTablesSelectedModeEnabledControlVariable;
	}

	/**
	 * Gets the gesture control variable.
	 *
	 * @return the gesture control variable
	 */
	public DistributedProperty<Integer> getGestureControlVariable()
	{
		return gestureModeControlVariable;
	}

	/**
	 * Gets the individual table select mode enabled control variable.
	 *
	 * @return the individual table select mode enabled control variable
	 */
	public DistributedProperty<PerformActionMessage> getIndividualTableSelectModeEnabledControlVariable()
	{
		return individualTableSelectModeEnabledControlVariable;
	}

	/**
	 * Gets the pointing control variable.
	 *
	 * @return the pointing control variable
	 */
	public DistributedProperty<PointDirection> getPointingControlVariable()
	{
		return pointingControlVariable;
	}

	/**
	 * Gets the table deselected control variable.
	 *
	 * @return the table deselected control variable
	 */
	public DistributedProperty<String> getTableDeselectedControlVariable()
	{
		return tableDeselectedControl;
	}

	/**
	 * Gets the table selected control variable.
	 *
	 * @return the table selected control variable
	 */
	public DistributedProperty<String> getTableSelectedControlVariable()
	{
		return tableSelectedControl;
	}

	/**
	 * Gets the table selected mode disabled control variable.
	 *
	 * @return the table selected mode disabled control variable
	 */
	public DistributedProperty<PerformActionMessage> getTableSelectedModeDisabledControlVariable()
	{
		return tableSelectdModeDisabledControlVariable;
	}

	/**
	 * Gets the teacher status control variable.
	 *
	 * @return the teacher status control variable
	 */
	public DistributedProperty<String> getTeacherStatusControlVariable()
	{
		return teacherStatusControlVariable;
	}

	/**
	 * Gets the teacher status to tracker control variable.
	 *
	 * @return the teacher status to tracker control variable
	 */
	public DistributedProperty<String> getTeacherStatusToTrackerControlVariable()
	{
		return teacherStatusToTrackerControlVariable;
	}

	/**
	 * Gets the tracked user locations control variable.
	 *
	 * @return the tracked user locations control variable
	 */
	public DistributedProperty<UserLocations> getTrackedUserLocationsControlVariable()
	{
		return trackedUserLocationsControl;
	}

	/**
	 * Gets the unique id to tracker control variable.
	 *
	 * @return the unique id to tracker control variable
	 */
	public DistributedProperty<String> getUniqueIDToTrackerControlVariable()
	{
		return uniqueIDToTrackerControlVariable;
	}

	/**
	 * Gets the user locations control variable.
	 *
	 * @return the user locations control variable
	 */
	public DistributedProperty<ArrayList<CombinedUserEntity>> getUserLocationsControlVariable()
	{
		return userLocationsControl;
	}

	/**
	 * Inits the with default.
	 *
	 * @param control
	 *            the control
	 * @param defaultValue
	 *            the default value
	 */
	protected void initWithDefault(DistributedProperty<ArrayList<CombinedUserEntity>> control, ArrayList<CombinedUserEntity> defaultValue)
	{
		if (null == control.getValue())
		{
			control.setValue(defaultValue);
		}
	}

	/**
	 * Inits the with default.
	 *
	 * @param control
	 *            the control
	 * @param defaultValue
	 *            the default value
	 */
	protected void initWithDefault(DistributedProperty<Integer> control, Integer defaultValue)
	{
		if (null == control.getValue())
		{
			control.setValue(defaultValue);
		}
	}

	/**
	 * Inits the with default.
	 *
	 * @param control
	 *            the control
	 * @param defaultValue
	 *            the default value
	 */
	protected void initWithDefault(DistributedProperty<PerformActionMessage> control, PerformActionMessage defaultValue)
	{
		if (null == control.getValue())
		{
			control.setValue(defaultValue);
		}
	}

	/**
	 * Inits the with default.
	 *
	 * @param control
	 *            the control
	 * @param defaultValue
	 *            the default value
	 */
	protected void initWithDefault(DistributedProperty<PointDirection> control, PointDirection defaultValue)
	{
		if (null == control.getValue())
		{
			control.setValue(defaultValue);
		}
	}

	/**
	 * Inits the with default.
	 *
	 * @param control
	 *            the control
	 * @param defaultValue
	 *            the default value
	 */
	protected void initWithDefault(DistributedProperty<String> control, String defaultValue)
	{
		if (null == control.getValue())
		{
			control.setValue(defaultValue);
		}
	}

	/**
	 * Inits the with default.
	 *
	 * @param control
	 *            the control
	 * @param defaultValue
	 *            the default value
	 */
	protected void initWithDefault(DistributedProperty<UserLocations> control, UserLocations defaultValue)
	{
		if (null == control.getValue())
		{
			control.setValue(defaultValue);
		}
	}

	/**
	 * Inits the with default string array.
	 *
	 * @param control
	 *            the control
	 * @param defaultValue
	 *            the default value
	 */
	protected void initWithDefaultStringArray(DistributedProperty<ArrayList<String>> control, ArrayList<String> defaultValue)
	{
		if (null == control.getValue())
		{
			control.setValue(defaultValue);
		}
	}

}
