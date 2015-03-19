package synergynet3.apps.numbernet.network.projector;

import java.util.logging.Logger;

import com.hazelcast.core.Member;

import synergynet3.apps.numbernet.ui.projection.expressions.ProjectExpressionsUI;
import synergynet3.apps.numbernet.ui.projection.scores.ProjectScoresUI;
import synergynet3.cluster.sharedmemory.DistributedPropertyChangedAction;
import synergynet3.web.apps.numbernet.comms.table.ProjectorDevice;
import synergynet3.web.apps.numbernet.shared.ProjectionDisplayMode;

public class ProjectionModeSynchronizer {
	private static final Logger log = Logger.getLogger(ProjectionModeSynchronizer.class.getName());

	public ProjectionModeSynchronizer(ProjectorDevice device,
			final ProjectScoresUI projectScoresUI,
			final ScoresSynchronizer scoresSync,
			final ProjectExpressionsUI projectExpressionsUI) {
		device.getProjectionDisplayModeControlVariable().registerChangeListener(new DistributedPropertyChangedAction<ProjectionDisplayMode>() {
			
			@Override
			public void distributedPropertyDidChange(Member member,
					ProjectionDisplayMode oldValue, ProjectionDisplayMode newValue) {
				switch(newValue) {
				case BLANK: {
					log.fine("Blanking projection.");
					projectExpressionsUI.setVisibility(false);
					projectScoresUI.setVisibility(false);
					break;
				}
				
				case SCORES: {
					log.fine("Displaying scores.");
					scoresSync.update();
					projectExpressionsUI.setVisibility(false);
					projectScoresUI.setVisibility(true);
					break;
				}
				
				case TABLE_CLONE: {
					log.fine("Displaying cloned table.");
					projectExpressionsUI.setVisibility(true);
					projectScoresUI.setVisibility(false);
					break;
				}
				}
			}
		});
	}

}
