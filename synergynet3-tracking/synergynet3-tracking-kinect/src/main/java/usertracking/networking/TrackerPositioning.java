package usertracking.networking;

import multiplicity3.config.position.PositionConfigPrefsItem;
import synergynet3.positioning.SynergyNetPosition;
import synergynet3.tracking.network.core.TrackingControlComms;
import usertracking.UserTracker;

import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;

/**
 * The Class TrackerPositioning.
 */
public class TrackerPositioning
{

	/**
	 * Gets the local device location pos only.
	 *
	 * @return the local device location pos only
	 */
	public static SynergyNetPosition getLocalDeviceLocationPosOnly()
	{
		PositionConfigPrefsItem prefs = new PositionConfigPrefsItem();

		float tableLocationX = prefs.getXPos();
		float tableLocationY = prefs.getYPos();

		int currentConnectionsCount = TrackingControlComms.get().getNumberOfTrackersOnline() - 1;

		if (prefs.getDeveloperMode())
		{
			if (prefs.getHorizontalPlacement())
			{
				if (prefs.getGridLimitX() != 0)
				{
					int xPos = currentConnectionsCount % prefs.getGridLimitX();
					tableLocationX = xPos * prefs.getGridDistanceX();
					int yPos = currentConnectionsCount / prefs.getGridLimitX();
					tableLocationY = yPos * prefs.getGridDistanceY();
				}
				else
				{
					tableLocationX = currentConnectionsCount * prefs.getGridDistanceX();
					tableLocationY = 0;
				}
			}
			else
			{
				if (prefs.getGridLimitY() != 0)
				{
					int yPos = currentConnectionsCount % prefs.getGridLimitY();
					tableLocationY = yPos * prefs.getGridDistanceY();
					int xPos = currentConnectionsCount / prefs.getGridLimitY();
					tableLocationX = xPos * prefs.getGridDistanceX();
				}
				else
				{
					tableLocationY = currentConnectionsCount * prefs.getGridDistanceY();
					tableLocationX = 0;
				}
			}
		}

		float orientation = FastMath.DEG_TO_RAD * prefs.getAngle();

		float heightFromFloor = prefs.getTableHeight();

		return new SynergyNetPosition("", tableLocationX, tableLocationY, orientation, 0, 0, heightFromFloor, 0);
	}

	/**
	 * To real world vector in m.
	 *
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @param z
	 *            the z
	 * @return the vector3f
	 */
	public static Vector3f toRealWorldVectorInM(float x, float y, float z)
	{
		x /= 1000;
		y /= 1000;
		z /= 1000;

		// This is where additional rotation calculations would be required if
		// the Kinect was rotated around the x axis.

		Vector2f originLoc = new Vector2f(-x, z);
		originLoc.rotateAroundOrigin(UserTracker.TRACKER_ORIENTATION, true);
		originLoc.setX(originLoc.x + UserTracker.TRACKER_LOCATION_X);
		originLoc.setY(originLoc.y + UserTracker.TRACKER_LOCATION_Y);

		float height = UserTracker.TRACKER_HEIGHT + y;
		return new Vector3f(originLoc.x, originLoc.y, height);

	}

}
