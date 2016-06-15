package synergynet3.apps.numbernet.network;

import java.util.logging.Logger;

import multiplicity3.input.MultiTouchInputComponent;
import synergynet3.cluster.sharedmemory.DistributedPropertyChangedAction;
import synergynet3.web.apps.numbernet.comms.table.NumberNetStudentTableClusteredData;

import com.hazelcast.core.Member;

/**
 * The Class MultiTouchEnabledSync.
 */
public class MultiTouchEnabledSync
{

	/** The Constant log. */
	private static final Logger log = Logger.getLogger(MultiTouchEnabledSync.class.getName());

	/** The input. */
	private MultiTouchInputComponent input;

	/** The table cluster data. */
	private NumberNetStudentTableClusteredData tableClusterData;

	/**
	 * Instantiates a new multi touch enabled sync.
	 *
	 * @param tableClusterData
	 *            the table cluster data
	 * @param mtInput
	 *            the mt input
	 */
	public MultiTouchEnabledSync(NumberNetStudentTableClusteredData tableClusterData, MultiTouchInputComponent mtInput)
	{
		this.tableClusterData = tableClusterData;
		this.input = mtInput;
	}

	/**
	 * Start.
	 */
	public void start()
	{
		setupTableDataClusterListeners();
	}

	/**
	 * Stop.
	 */
	public void stop()
	{
		// nothing to do
	}

	/**
	 * Setup table data cluster listeners.
	 */
	private void setupTableDataClusterListeners()
	{
		tableClusterData.getTouchEnabledControlVariable().registerChangeListener(new DistributedPropertyChangedAction<Boolean>()
		{
			@Override
			public void distributedPropertyDidChange(Member m, Boolean oldValue, Boolean newValue)
			{
				log.info("Setting multi-touch enabled property to " + newValue);
				input.setMultiTouchInputEnabled(newValue);
			}
		});
	}

}
