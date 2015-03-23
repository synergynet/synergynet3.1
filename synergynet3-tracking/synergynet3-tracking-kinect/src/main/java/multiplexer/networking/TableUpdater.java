package multiplexer.networking;

import java.util.ArrayList;

import multiplexer.Multiplexer;
import synergynet3.cluster.SynergyNetCluster;
import synergynet3.tracking.network.core.TrackingControlComms;
import synergynet3.tracking.network.shared.CombinedUserEntity;

import com.jme3.math.Vector2f;

/**
 * The Class TableUpdater.
 */
public class TableUpdater {

	/** The update tables thread. */
	public static Thread updateTablesThread = new Thread(new Runnable() {
		public void run() {
			try {
				while (Multiplexer.isRunning) {
					Thread.sleep(SLEEP_TIME);
					multiplexCheck();
					synchronized (Multiplexer.users) {
						TrackingControlComms
								.get()
								.sendUserLocationsToAllTables(Multiplexer.users);
					}
				}
				Multiplexer.multiplexerSync.stop();
				TableUpdater.clearUserLocations();
				SynergyNetCluster.get().shutdown();
				System.exit(0);
			} catch (InterruptedException ie) {
			}
		}
	});

	/** The Constant SLEEP_TIME. */
	private static final int SLEEP_TIME = 100;

	/**
	 * Clear user locations.
	 */
	private static void clearUserLocations() {
		Multiplexer.users.clear();
		TrackingControlComms.get().sendUserLocationsToAllTables(
				Multiplexer.users);
	}

	/**
	 * Multiplex check.
	 */
	private static void multiplexCheck() {
		synchronized (Multiplexer.users) {

			ArrayList<CombinedUserEntity[]> toCombine = new ArrayList<CombinedUserEntity[]>();

			for (CombinedUserEntity userOne : Multiplexer.users) {
				Vector2f userOneBodyLoc = toBodyVector(userOne
						.getUserLocation().getUserBodyLocation());
				for (CombinedUserEntity userTwo : Multiplexer.users) {
					if (!userOne.equals(userTwo)) {
						Vector2f userTwoBodyLoc = toBodyVector(userTwo
								.getUserLocation().getUserBodyLocation());
						if (userOneBodyLoc.distance(userTwoBodyLoc) < Multiplexer.MULTIPLEXING_THRESHOLD_IN_METRES) {
							toCombine.add(new CombinedUserEntity[] { userOne,
									userTwo });
							break;
						}
					}
				}
			}

			for (CombinedUserEntity[] combination : toCombine) {
				CombinedUserEntity userOne = combination[0];
				CombinedUserEntity userTwo = combination[1];

				int indexOfUserOne = Multiplexer.users.indexOf(userOne);

				boolean okToContinue = true;

				for (String idOne : userOne.getUserIDs()) {
					String sourceOne = CombinedUserEntity
							.getTrackerSourceFromID(idOne);
					for (String idTwo : userTwo.getUserIDs()) {
						String sourceTwo = CombinedUserEntity
								.getTrackerSourceFromID(idTwo);
						if (sourceOne.equals(sourceTwo)) {
							okToContinue = false;
							break;
						}
					}
					if (!okToContinue) {
						break;
					}
				}

				if (okToContinue) {

					for (String id : userTwo.getUserIDs()) {
						userOne.getUserIDs().add(id);
					}

					boolean isTeacher = false;
					if (userOne.isTeacher() || userTwo.isTeacher()) {
						isTeacher = true;
					}

					userOne.setTeacher(isTeacher);
					Multiplexer.users.set(indexOfUserOne, userOne);
					Multiplexer.users.remove(userTwo);

					Multiplexer.writeToAnnouncementBox("Entity "
							+ userTwo.getUniqueID()
							+ " has been merged into Entity "
							+ userOne.getUniqueID());

					TrackerUpdater.sendTeacherStatusUpdateToTrackers(userOne);
					TrackerUpdater.sendUniqueIDUpdateToTrackers(userOne);
				}
			}

		}
	}

	/**
	 * To body vector.
	 *
	 * @param bodyLoc the body loc
	 * @return the vector2f
	 */
	private static Vector2f toBodyVector(float[] bodyLoc) {
		return new Vector2f(bodyLoc[0], bodyLoc[1]);
	}

}
