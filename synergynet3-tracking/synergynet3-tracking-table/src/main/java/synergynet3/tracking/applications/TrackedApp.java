package synergynet3.tracking.applications;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import multiplicity3.appsystem.IQueueOwner;
import multiplicity3.config.display.DisplayPrefsItem;
import multiplicity3.csys.factory.ContentTypeNotBoundException;
import multiplicity3.csys.items.border.IRoundedBorder;
import multiplicity3.input.IMultiTouchEventListener;
import multiplicity3.input.MultiTouchInputComponent;
import multiplicity3.input.events.MultiTouchCursorEvent;
import multiplicity3.input.events.MultiTouchObjectEvent;
import synergynet3.SynergyNetApp;
import synergynet3.cluster.SynergyNetCluster;
import synergynet3.positioning.SynergyNetPositioning;
import synergynet3.tracking.network.TrackedAppSync;
import synergynet3.tracking.network.core.TrackingControlComms;
import synergynet3.tracking.network.core.TrackingDeviceControl;
import synergynet3.tracking.network.shared.CombinedUserEntity;
import synergynet3.tracking.network.shared.PointDirection;
import synergynet3.tracking.network.shared.UserColourUtils;
import synergynet3.tracking.network.shared.UserLocation;
import synergynet3.tracking.network.shared.UserLocation.USERSTATE;

import com.jme3.collision.CollisionResults;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;

/**
 * The Class TrackedApp.
 */
abstract public class TrackedApp extends SynergyNetApp implements IMultiTouchEventListener
{

	/** The body threshold distance. */
	public static float BODY_THRESHOLD_DISTANCE = 1;

	/** The hand threshold distance. */
	public static float HAND_THRESHOLD_DISTANCE = 0.5f;

	/** The table height. */
	public static float TABLE_HEIGHT = 1f;

	/** The table orientation. */
	public static float TABLE_LOCATION_X, TABLE_LOCATION_Y, TABLE_ORIENTATION = 0;

	/** The Constant SLEEP_TIME. */
	private static final int SLEEP_TIME = 2000;

	/** The tracking table identity. */
	private static String trackingTableIdentity;

	/** The gesture mode. */
	private boolean gestureMode = false;

	/** The individual mode. */
	private boolean individualMode = false;

	/** The is selected. */
	private boolean isSelected = false;

	/** The selection action made recently. */
	private boolean selectionActionMadeRecently = false;

	/** The selection action made timer. */
	private Runnable selectionActionMadeTimer = new Runnable()
	{
		@Override
		public void run()
		{
			try
			{
				Thread.sleep(SLEEP_TIME);
				selectionActionMadeRecently = false;
			}
			catch (InterruptedException ie)
			{
			}
		}
	};

	/** The selection border. */
	private IRoundedBorder selectionBorder;

	/** The table rep. */
	private Spatial tableRep;

	/** The tracking sync. */
	private TrackedAppSync trackingSync;

	/** The display height. */
	protected int displayHeight = 768;

	/** The display width. */
	protected int displayWidth = 1024;

	/** The input. */
	protected MultiTouchInputComponent input;

	/** The touches. */
	protected HashMap<Long, Integer> touches = new HashMap<Long, Integer>();

	/** The user locations. */
	protected ArrayList<CombinedUserEntity> userLocations = new ArrayList<CombinedUserEntity>();

	/**
	 * Gets the real value.
	 *
	 * @param value
	 *            the value
	 * @return the real value
	 */
	public static float getRealValue(float value)
	{
		DisplayPrefsItem displayPrefs = new DisplayPrefsItem();
		return value * (displayPrefs.getRealWidth() / displayPrefs.getWidth());
	}

	/**
	 * Initialise tracking app args.
	 *
	 * @param args
	 *            the args
	 */
	public static void initialiseTrackingAppArgs(String[] args)
	{

		try
		{
			BODY_THRESHOLD_DISTANCE = Float.parseFloat(ManagementFactory.getRuntimeMXBean().getSystemProperties().get("bodythreshold"));
		}
		catch (Exception e)
		{
			System.out.println("No body threshold argument given, using default.");
		}

		BODY_THRESHOLD_DISTANCE = SynergyNetPositioning.getPixelValue(BODY_THRESHOLD_DISTANCE);
		System.out.println("Body Threshold: " + BODY_THRESHOLD_DISTANCE);

		try
		{
			HAND_THRESHOLD_DISTANCE = Float.parseFloat(ManagementFactory.getRuntimeMXBean().getSystemProperties().get("handthreshold"));
		}
		catch (Exception e)
		{
			System.out.println("No hand threshold argument given, using default.");
		}

		HAND_THRESHOLD_DISTANCE = SynergyNetPositioning.getPixelValue(HAND_THRESHOLD_DISTANCE);
		System.out.println("Hand Threshold: " + HAND_THRESHOLD_DISTANCE);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.input.IMultiTouchEventListener#cursorChanged(multiplicity3
	 * .input.events.MultiTouchCursorEvent)
	 */
	@Override
	public void cursorChanged(MultiTouchCursorEvent event)
	{
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.input.IMultiTouchEventListener#cursorClicked(multiplicity3
	 * .input.events.MultiTouchCursorEvent)
	 */
	@Override
	public void cursorClicked(MultiTouchCursorEvent event)
	{
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.input.IMultiTouchEventListener#cursorPressed(multiplicity3
	 * .input.events.MultiTouchCursorEvent)
	 */
	@Override
	public void cursorPressed(MultiTouchCursorEvent event)
	{
		CombinedUserEntity userToStore = null;

		float distance = -1;

		for (CombinedUserEntity user : userLocations)
		{
			UserLocation userLocation = user.getUserLocation();
			float[] userLocs = userLocation.getLocationOfUser();
			Vector3f touchLoc = new Vector3f(event.getPosition().x * displayWidth, event.getPosition().y * displayHeight, TABLE_HEIGHT);

			if (userLocation.getUserState() == USERSTATE.BODY)
			{
				Vector2f userLoc = new Vector2f(userLocs[0], userLocs[1]);
				float distanceTemp = userLoc.distance(new Vector2f(touchLoc.x, touchLoc.y));
				if (distanceTemp < BODY_THRESHOLD_DISTANCE)
				{
					if ((distance == -1) || (distanceTemp < distance))
					{
						distance = distanceTemp;
						userToStore = user;
					}
				}
			}
			else if (userLocation.getUserState() == USERSTATE.ONE_HAND)
			{
				Vector3f userLoc = new Vector3f(userLocs[0], userLocs[1], userLocs[2]);
				float distanceTemp = userLoc.distance(touchLoc);
				if (distanceTemp < HAND_THRESHOLD_DISTANCE)
				{
					if ((distance == -1) || (distanceTemp < distance))
					{
						distance = distanceTemp;
						userToStore = user;
					}
				}
			}
			else if (userLocation.getUserState() == USERSTATE.TWO_HANDS)
			{

				Vector3f handOneLoc = new Vector3f(userLocs[0], userLocs[1], userLocs[2]);
				Vector3f handTwoLoc = new Vector3f(userLocs[3], userLocs[4], userLocs[5]);

				float distanceTemp = handOneLoc.distance(touchLoc);
				float distanceTempTwo = handTwoLoc.distance(touchLoc);

				if (distanceTempTwo < distanceTemp)
				{
					distanceTemp = distanceTempTwo;
				}

				if (distanceTemp < HAND_THRESHOLD_DISTANCE)
				{
					if ((distance == -1) || (distanceTemp < distance))
					{
						distance = distanceTemp;
						userToStore = user;
					}
				}
			}
		}

		boolean isTeacher = false;
		if (userToStore != null)
		{
			isTeacher = userToStore.isTeacher();

			if (gestureMode && isTeacher)
			{
				if (individualMode)
				{
					if (isSelected)
					{
						deselected();
					}
					else
					{
						selected();
					}
				}
				else
				{
					gestureIndividualModeEnabled();
					selected();
				}
			}
			touches.put(event.getCursorID(), userToStore.getUniqueID());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.input.IMultiTouchEventListener#cursorReleased(multiplicity3
	 * .input.events.MultiTouchCursorEvent)
	 */
	@Override
	public void cursorReleased(MultiTouchCursorEvent event)
	{
		if (touches.containsKey(event.getCursorID()))
		{
			touches.remove(event.getCursorID());
		}
	}

	/**
	 * Gesturea all mode enabled.
	 */
	public void gestureaAllModeEnabled()
	{
		gestureMode = true;
		isSelected = true;
		individualMode = false;
		showSelectionBorder(new ColorRGBA(0.5f, 0.5f, 0.5f, 0.5f));
	}

	/**
	 * Gesture individual mode enabled.
	 */
	public void gestureIndividualModeEnabled()
	{
		if (!individualMode)
		{
			gestureMode = true;
			individualMode = true;
			hideSelectionBorder();
		}
	}

	/**
	 * Gesture mode disabled.
	 */
	public void gestureModeDisabled()
	{
		gestureMode = false;
		isSelected = false;
		individualMode = false;
		hideSelectionBorder();
	}

	/**
	 * Localise vector.
	 *
	 * @param vec
	 *            the vec
	 * @return the vector2f
	 */
	public Vector2f localiseVector(Vector2f vec)
	{

		vec.setX(vec.x - TABLE_LOCATION_X);
		vec.setY(vec.y - TABLE_LOCATION_Y);

		vec.setX(SynergyNetPositioning.getPixelValue(vec.x));
		vec.setY(SynergyNetPositioning.getPixelValue(vec.y));

		vec.setX(vec.x - (displayWidth / 2));
		vec.setY(vec.y - (displayHeight / 2));
		vec.rotateAroundOrigin(-TABLE_ORIENTATION, true);
		vec.setX(vec.x + (displayWidth / 2));
		vec.setY(vec.y + (displayHeight / 2));
		return vec;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.input.IMultiTouchEventListener#objectAdded(multiplicity3
	 * .input.events.MultiTouchObjectEvent)
	 */
	@Override
	public void objectAdded(MultiTouchObjectEvent event)
	{
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.input.IMultiTouchEventListener#objectChanged(multiplicity3
	 * .input.events.MultiTouchObjectEvent)
	 */
	@Override
	public void objectChanged(MultiTouchObjectEvent event)
	{
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.input.IMultiTouchEventListener#objectRemoved(multiplicity3
	 * .input.events.MultiTouchObjectEvent)
	 */
	@Override
	public void objectRemoved(MultiTouchObjectEvent event)
	{
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.SynergyNetApp#onDestroy()
	 */
	@Override
	public void onDestroy()
	{
		if (trackingSync != null)
		{
			trackingSync.stop();
		}
		super.onDestroy();
	}

	/**
	 * Sets the user locations.
	 *
	 * @param remoteUserLocations
	 *            the new user locations
	 */
	public void setUserLocations(ArrayList<CombinedUserEntity> remoteUserLocations)
	{
		userLocations = new ArrayList<CombinedUserEntity>();

		for (CombinedUserEntity user : remoteUserLocations)
		{
			UserLocation userLocation = localiseUserLocation(user.getUserLocation());
			user.setUserLocation(userLocation);
			userLocations.add(user);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.SynergyNetApp#shouldStart(multiplicity3.input.
	 * MultiTouchInputComponent, multiplicity3.appsystem.IQueueOwner)
	 */
	@Override
	public void shouldStart(MultiTouchInputComponent input, IQueueOwner iqo)
	{
		super.shouldStart(input, iqo);
		this.input = input;

		displayWidth = (int) (stage.getWorldLocation().x * 2);
		displayHeight = (int) (stage.getWorldLocation().y * 2);

		trackingTableIdentity = SynergyNetCluster.get().getIdentity();
		TrackingDeviceControl trackingDeviceController = new TrackingDeviceControl(trackingTableIdentity);
		trackingSync = new TrackedAppSync(trackingDeviceController, this);

		TABLE_LOCATION_X = localDevicePosition.getXinMetres();
		TABLE_LOCATION_Y = localDevicePosition.getYinMetres();
		TABLE_ORIENTATION = localDevicePosition.getOrientation();
		TABLE_HEIGHT = SynergyNetPositioning.getPixelValue(localDevicePosition.getInterfaceHeightFromFloorinMetres());

		Box box = new Box(new Vector3f(), displayWidth / 2, displayHeight / 2, 100);
		tableRep = new Geometry("localTable", box);
		tableRep.setLocalTranslation(displayWidth / 2, displayHeight / 2, TABLE_HEIGHT);
	}

	/**
	 * User pointing.
	 *
	 * @param pointDirection
	 *            the point direction
	 */
	public void userPointing(PointDirection pointDirection)
	{
		if (!selectionActionMadeRecently)
		{
			Vector3f startPoint = generateLocalised3dVector(pointDirection.getStartPoint());
			Vector3f endPoint = generateLocalised3dVector(pointDirection.getEndPoint());

			startPoint.setX(startPoint.getX() - (displayWidth / 2));
			startPoint.setY(startPoint.getY() - (displayHeight / 2));
			endPoint.setX(endPoint.getX() - (displayWidth / 2));
			endPoint.setY(endPoint.getY() - (displayHeight / 2));

			Vector3f direction = generatorVectorBetween(startPoint, endPoint).normalize();
			Ray ray = new Ray(endPoint, direction);
			CollisionResults results = new CollisionResults();
			ray.collideWith(tableRep.getWorldBound(), results);
			if (results.size() > 0)
			{
				if (gestureMode)
				{
					if (individualMode)
					{
						if (isSelected)
						{
							deselected();
						}
						else
						{
							selected();
						}
					}
					else
					{
						gestureIndividualModeEnabled();
						selected();
					}
				}
			}
		}
	}

	/**
	 * Deselected.
	 */
	private void deselected()
	{
		if (!selectionActionMadeRecently)
		{
			isSelected = false;
			hideSelectionBorder();
			TrackingControlComms.get().announceDeSelection(trackingTableIdentity);
			selectionActionMade();
		}
	}

	/**
	 * Generate localised3d vector.
	 *
	 * @param vectorRep
	 *            the vector rep
	 * @return the vector3f
	 */
	private Vector3f generateLocalised3dVector(float[] vectorRep)
	{
		Vector2f originTemp = new Vector2f(vectorRep[0], vectorRep[1]);
		originTemp = localiseVector(originTemp);
		return new Vector3f(originTemp.x, originTemp.y, SynergyNetPositioning.getPixelValue(vectorRep[2]));
	}

	/**
	 * Generator vector between.
	 *
	 * @param from
	 *            the from
	 * @param to
	 *            the to
	 * @return the vector3f
	 */
	private Vector3f generatorVectorBetween(Vector3f from, Vector3f to)
	{
		return new Vector3f(to.getX() - from.getX(), to.getY() - from.getY(), to.getZ() - from.getZ());
	}

	/**
	 * Hide selection border.
	 */
	private void hideSelectionBorder()
	{
		if (selectionBorder != null)
		{
			stage.removeItem(selectionBorder);
			selectionBorder = null;
		}
	}

	/**
	 * Localise user location.
	 *
	 * @param userLocation
	 *            the user location
	 * @return the user location
	 */
	private UserLocation localiseUserLocation(UserLocation userLocation)
	{
		if (userLocation.getUserState() == USERSTATE.BODY)
		{

			Vector2f bodyLoc = new Vector2f(userLocation.getLocationOfUser()[0], userLocation.getLocationOfUser()[1]);
			bodyLoc = localiseVector(bodyLoc);

			userLocation.setUserBodyLocation(bodyLoc.x, bodyLoc.y);

		}
		else if (userLocation.getUserState() == USERSTATE.ONE_HAND)
		{

			Vector2f handLoc = new Vector2f(userLocation.getLocationOfUser()[0], userLocation.getLocationOfUser()[1]);
			handLoc = localiseVector(handLoc);

			userLocation.setSingleUserHandLocation(handLoc.x, handLoc.y, SynergyNetPositioning.getPixelValue(userLocation.getLocationOfUser()[2]));

		}
		else if (userLocation.getUserState() == USERSTATE.TWO_HANDS)
		{

			Vector2f handOneLoc = new Vector2f(userLocation.getLocationOfUser()[0], userLocation.getLocationOfUser()[1]);
			handOneLoc = localiseVector(handOneLoc);

			Vector2f handTwoLoc = new Vector2f(userLocation.getLocationOfUser()[3], userLocation.getLocationOfUser()[4]);
			handTwoLoc = localiseVector(handTwoLoc);

			userLocation.setBothUserHandLocations(handOneLoc.x, handOneLoc.y, SynergyNetPositioning.getPixelValue(userLocation.getLocationOfUser()[2]), handTwoLoc.x, handTwoLoc.y, SynergyNetPositioning.getPixelValue(userLocation.getLocationOfUser()[5]));

		}
		return userLocation;
	}

	/**
	 * Selected.
	 */
	private void selected()
	{
		if (!selectionActionMadeRecently)
		{
			isSelected = true;
			showSelectionBorder(new ColorRGBA(1f, 1f, 1f, 1f));
			TrackingControlComms.get().announceSelection(trackingTableIdentity);
			selectionActionMade();
		}
	}

	/**
	 * Selection action made.
	 */
	private void selectionActionMade()
	{
		if (!selectionActionMadeRecently)
		{
			selectionActionMadeRecently = true;
			new Thread(selectionActionMadeTimer).start();
		}
	}

	/**
	 * Show selection border.
	 *
	 * @param colour
	 *            the colour
	 */
	private void showSelectionBorder(ColorRGBA colour)
	{
		if (selectionBorder != null)
		{
			hideSelectionBorder();
		}
		try
		{
			selectionBorder = stage.getContentFactory().create(IRoundedBorder.class, "selectionBorder", UUID.randomUUID());
			selectionBorder.setBorderWidth(30f);
			selectionBorder.setSize(displayWidth - 30, displayHeight - 30);
			selectionBorder.setColor(colour);
			stage.addItem(selectionBorder);
			selectionBorder.setInteractionEnabled(false);
		}
		catch (ContentTypeNotBoundException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Gets the user.
	 *
	 * @param userID
	 *            the user id
	 * @return the user
	 */
	protected CombinedUserEntity getUser(int userID)
	{
		for (CombinedUserEntity user : userLocations)
		{
			if (user.getUniqueID() == userID)
			{
				return user;
			}
		}
		return null;
	}

	/**
	 * Gets the user colour.
	 *
	 * @param userID
	 *            the user id
	 * @return the user colour
	 */
	protected ColorRGBA getUserColour(int userID)
	{
		return UserColourUtils.getRGBAColour(userID);
	}

	/**
	 * Checks if is teacher.
	 *
	 * @param userID
	 *            the user id
	 * @return true, if is teacher
	 */
	protected boolean isTeacher(int userID)
	{
		return getUser(userID).isTeacher();
	}

}
