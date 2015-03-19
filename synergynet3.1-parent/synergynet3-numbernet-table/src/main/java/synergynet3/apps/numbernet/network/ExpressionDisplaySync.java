package synergynet3.apps.numbernet.network;

import java.util.logging.Logger;

import multiplicity3.csys.factory.ContentTypeNotBoundException;

import com.hazelcast.core.Member;

import synergynet3.apps.numbernet.ui.expression.ExpressionDisplay;
import synergynet3.cluster.sharedmemory.DistributedPropertyChangedAction;
import synergynet3.web.apps.numbernet.comms.table.NumberNetStudentTableClusteredData;

public class ExpressionDisplaySync {
	private static final Logger log = Logger.getLogger(ExpressionDisplaySync.class.getName());
	private NumberNetStudentTableClusteredData data;
	private ExpressionDisplay display;
	
	public ExpressionDisplaySync(ExpressionDisplay display, NumberNetStudentTableClusteredData tableClusterData) {
		this.display = display;
		this.data = tableClusterData;		
	}
	
	public void start() {
		setupScoreDisplaySync();
		setupCorrectIncorrectDisplaySync();
	}

	//********************************
	//********* [ private ] **********
	//********************************
	
	private void setupScoreDisplaySync() {
		Boolean value = data.getScoresVisibleControlVariable().getValue();
		if(value != null) {
			try {
				this.display.setScoresDisplayVisibility(value);
			} catch (ContentTypeNotBoundException e) {
				log.warning("Could not display scores properly.");
			}
		}
		
		data.getScoresVisibleControlVariable().registerChangeListener(new DistributedPropertyChangedAction<Boolean>() {
			@Override
			public void distributedPropertyDidChange(Member member,
					Boolean oldValue, Boolean newValue) {
				try {
					display.setScoresDisplayVisibility(newValue);
				} catch (ContentTypeNotBoundException e) {
					log.warning("Could not change score visibility.");
				}
			}			
		});
	}
	
	private void setupCorrectIncorrectDisplaySync() {
		Boolean incorrectFlag = data.getIncorrectExpressionsVisibleControlVariable().getValue();
		Boolean correctFlag = data.getCorrectExpressionsVisibleControlVariable().getValue();
		
		if(correctFlag != null) {
			this.display.setExpressionVisibilityCorrect(correctFlag);
		}				

		if(incorrectFlag != null) {
			this.display.setExpressionVisibilityIncorrect(incorrectFlag);
		}
		
		
		data.getCorrectExpressionsVisibleControlVariable().registerChangeListener(new DistributedPropertyChangedAction<Boolean>() {
			@Override
			public void distributedPropertyDidChange(Member member,
					Boolean oldValue, Boolean newValue) {
				display.setExpressionVisibilityCorrect(newValue);
			}
		});
		
		data.getIncorrectExpressionsVisibleControlVariable().registerChangeListener(new DistributedPropertyChangedAction<Boolean>() {
			@Override
			public void distributedPropertyDidChange(Member member,
					Boolean oldValue, Boolean newValue) {
				display.setExpressionVisibilityIncorrect(newValue);
			}
		});
		
		data.getOthersCorrectExpressionsVisibleControlVariable().registerChangeListener(new DistributedPropertyChangedAction<Boolean>() {
			@Override
			public void distributedPropertyDidChange(Member member,
					Boolean oldValue, Boolean newValue) {
				display.setOthersExpressionVisibilityCorrect(newValue);
			}
		});
		
		data.getOthersIncorrectExpressionsVisibleControlVariable().registerChangeListener(new DistributedPropertyChangedAction<Boolean>() {
			@Override
			public void distributedPropertyDidChange(Member member,
					Boolean oldValue, Boolean newValue) {
				display.setOthersExpressionVisibilityIncorrect(newValue);
			}
		});
	}
}
