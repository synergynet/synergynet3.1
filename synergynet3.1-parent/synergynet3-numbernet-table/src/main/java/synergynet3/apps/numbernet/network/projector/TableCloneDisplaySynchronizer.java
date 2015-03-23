package synergynet3.apps.numbernet.network.projector;

import java.util.logging.Level;
import java.util.logging.Logger;

import multiplicity3.csys.factory.ContentTypeNotBoundException;
import synergynet3.apps.numbernet.ui.projection.expressions.ProjectExpressionsUI;
import synergynet3.cluster.sharedmemory.DistributedPropertyChangedAction;
import synergynet3.web.apps.numbernet.comms.table.ProjectorDevice;

import com.hazelcast.core.Member;

/**
 * The Class TableCloneDisplaySynchronizer.
 */
public class TableCloneDisplaySynchronizer {

	/** The Constant log. */
	private static final Logger log = Logger
			.getLogger(TableCloneDisplaySynchronizer.class.getName());

	/**
	 * Instantiates a new table clone display synchronizer.
	 *
	 * @param projectExpressionsUI the project expressions ui
	 * @param device the device
	 */
	public TableCloneDisplaySynchronizer(
			final ProjectExpressionsUI projectExpressionsUI,
			ProjectorDevice device) {
		device.getTargetToShowControlVariable().registerChangeListener(
				new DistributedPropertyChangedAction<Double>() {
					@Override
					public void distributedPropertyDidChange(Member member,
							Double oldValue, Double newValue) {
						log.info("Now switching display to " + newValue);
						if (newValue == null) {
							return;
						}

						if (newValue == Double.NaN) {
							projectExpressionsUI.clearDisplay();
						} else {
							try {
								projectExpressionsUI.displayTarget(newValue);
							} catch (ContentTypeNotBoundException e) {
								log.log(Level.WARNING, "Could not project!", e);
							}
						}
					}
				});

		device.getShouldUpdatePositionControlVariable().registerChangeListener(
				new DistributedPropertyChangedAction<Integer>() {
					@Override
					public void distributedPropertyDidChange(Member member,
							Integer oldValue, Integer newValue) {
						log.info("Got request to update position info.");
						projectExpressionsUI.updateDistributedPositionData();

					}
				});

		device.getUnifyRotationControlVariable().registerChangeListener(
				new DistributedPropertyChangedAction<Boolean>() {

					@Override
					public void distributedPropertyDidChange(Member member,
							Boolean oldValue, Boolean newValue) {
						log.info("unify rotation property changed.");
						if (newValue != null) {
							projectExpressionsUI.setUnifyRotationMode(newValue);
						}
					}
				});

		Boolean unifyRotation = device.getUnifyRotationControlVariable()
				.getValue();
		if (unifyRotation != null) {
			projectExpressionsUI.setUnifyRotationMode(unifyRotation);
		}

	}

}
