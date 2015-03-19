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

public class ExpressionPositionSync {
	private static final Logger log = Logger.getLogger(ExpressionPositionSync.class.getName());

	//private NumberNetStudentTableClusteredData tableData;
	private ExpressionDisplay expressionDisplay;
	private Timer updatePositionInfoTimer;

	public ExpressionPositionSync(ExpressionDisplay expressionDisplay, NumberNetStudentTableClusteredData tableData) {
		this.expressionDisplay = expressionDisplay;
		//this.tableData = tableData;

	}

	public void start() {
		initExpressionPositionSync();
	}

	public void stop() {
		updatePositionInfoTimer.cancel();
	}

	private void initExpressionPositionSync() {
		//		tableData.getExpressionVisualPropertiesMap().registerChangeListener(new DistributedPropertyChangedAction<Map<String,ExpressionVisibleProperties>>() {
		//			@Override
		//			public void distributedPropertyDidChange(Member m,
		//					Map<String, ExpressionVisibleProperties> oldValue,
		//					Map<String, ExpressionVisibleProperties> newValue) {
		//				if(!m.localMember()) {
		//					expressionDisplay.updateExpressionsVisualProperties(newValue);
		//				}
		//			}
		//		});

		updatePositionInfoTimer = new Timer();
		updatePositionInfoTimer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				log.info("Updating position info");


				Double target = expressionDisplay.getCurrentTarget();
				if(target != null) {
					Map<String, ExpressionVisibleProperties> posInfo = expressionDisplay.getCurrentIDToExpressionVisibleMap();				
					DistributedMap<String, ExpressionVisibleProperties> dmap = TargetMaps.get().getExpressionVisiblePropertiesMapForTarget(target);
					for(ExpressionVisibleProperties evp : posInfo.values()) {
						dmap.put(evp.id, evp);
					}
				}

				//				tableData.getExpressionVisualPropertiesMap().setValue(posInfo);
			}			
		}, 0, 10 * 1000);
	}
}
