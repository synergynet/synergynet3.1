package multiplexer;

import java.util.ArrayList;

import synergynet3.tracking.network.shared.CombinedUserEntity;
import synergynet3.tracking.network.shared.UserLocation;
import synergynet3.tracking.network.shared.UserLocation.USERSTATE;

/**
 * The Class MultiplexerUtils.
 */
public class MultiplexerUtils {

	/**
	 * Gets the lowest available unique combined user id.
	 *
	 * @return the lowest available unique combined user id
	 */
	public static int getLowestAvailableUniqueCombinedUserID() {
		ArrayList<Integer> existingIDs = new ArrayList<Integer>();
		for (CombinedUserEntity user : Multiplexer.users) {
			existingIDs.add(user.getUniqueID());
		}
		for (int i = 1; i <= Multiplexer.users.size(); i++) {
			if (!existingIDs.contains(i)) {
				return i;
			}
		}
		return Multiplexer.users.size() + 1;
	}

	/**
	 * Merge user locations.
	 *
	 * @param newUserLoc the new user loc
	 * @param oldUserLoc the old user loc
	 * @return the user location
	 */
	public static UserLocation mergeUserLocations(UserLocation newUserLoc,
			UserLocation oldUserLoc) {
		if (!isCalibrated(newUserLoc) && isCalibrated(newUserLoc)) {
			float[] oldHandLocs = oldUserLoc.getLocationOfUser();
			newUserLoc.setBothUserHandLocations(oldHandLocs[0], oldHandLocs[1],
					oldHandLocs[2], oldHandLocs[3], oldHandLocs[4],
					oldHandLocs[5]);
		}
		return newUserLoc;
	}

	/**
	 * Checks if is calibrated.
	 *
	 * @param userLoc the user loc
	 * @return true, if is calibrated
	 */
	private static boolean isCalibrated(UserLocation userLoc) {
		if (userLoc.getUserState() == USERSTATE.TWO_HANDS) {
			return true;
		}
		return false;
	}

}
