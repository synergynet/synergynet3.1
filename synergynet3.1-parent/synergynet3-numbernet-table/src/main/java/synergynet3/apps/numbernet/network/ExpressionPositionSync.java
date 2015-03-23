package synergynet3.apps.numbernet.network;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

import synergynet3.apps.numbernet.ui.expression.ExpressionDisplay;
import synergynet3.cluster.sharedmemory.DistributedMap;
import synergynet3.web.apps.numbernet.comms.table.NumberNetStudentTableClusteredData;
import synergynet3.web.apps.numbernet.comms.table.TargetMaps;
import synergynet3.web.apps.numbernet.shared.ExpressionVisibleProperties;

/**
 * The Class ExpressionPositionSync.
 */
public class ExpressionPositionSync {

	/** The Constant log. */
	private static final Logger log = Logger
			.getLogger(ExpressionPositionSync.class.getName());

	// private NumberNetStudentTableClusteredData tableData;
	/** The expression display. */
	private ExpressionDisplay expressionDisplay;

	/** The update position info timer. */
	private Timer updatePositionInfoTimer;

	/**
	 * Instantiates a new expression position sync.
	 *
	 * @param expressionDisplay the expression display
	 * @param tableData the table data
	 */
	public ExpressionPositionSync(ExpressionDisplay expressionDisplay,
			NumberNetStudentTableClusteredData tableData) {
		this.expressionDisplay = expressionDisplay;
		// this.tableData = tableData;

	}

	/**
	 * Start.
	 */
	public void start() {
		initExpressionPositionSync();
	}

	/**
	 * Stop.
	 */
	public void stop() {
		updatePositionInfoTimer.cancel();
	}

	/**
	 * Inits the expression position sync.
	 */
	private void initExpressionPositionSync() {
		// tableData.getExpressionVisualPropertiesMap().registerChangeListener(new
		// DistributedPropertyChangedAction<Map<String,ExpressionVisibleProperties>>()
		// {
		// @Override
		// public void distributedPropertyDidChange(Member m,
		// Map<String, ExpressionVisibleProperties> oldValue,
		// Map<String, ExpressionVisibleProperties> newValue) {
		// if(!m.localMember()) {
		// expressionDisplay.updateExpressionsVisualProperties(newValue);
		// }
		// }
		// });

		updatePositionInfoTimer = new Timer();
		updatePositionInfoTimer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				log.info("Updating position info");

				Double target = expressionDisplay.getCurrentTarget();
				if (target != null) {
					Map<String, ExpressionVisibleProperties> posInfo = expressionDisplay
							.getCurrentIDToExpressionVisibleMap();
					DistributedMap<String, ExpressionVisibleProperties> dmap = TargetMaps
							.get().getExpressionVisiblePropertiesMapForTarget(
									target);
					for (ExpressionVisibleProperties evp : posInfo.values()) {
						dmap.put(evp.id, evp);
					}
				}

				// tableData.getExpressionVisualPropertiesMap().setValue(posInfo);
			}
		}, 0, 10 * 1000);
	}
}
