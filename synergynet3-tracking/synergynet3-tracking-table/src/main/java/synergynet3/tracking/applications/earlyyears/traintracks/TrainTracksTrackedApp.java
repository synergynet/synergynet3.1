package synergynet3.tracking.applications.earlyyears.traintracks;

import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Level;

import multiplicity3.appsystem.MultiplicityClient;
import multiplicity3.config.identity.IdentityConfigPrefsItem;
import multiplicity3.csys.behaviours.RotateTranslateScaleBehaviour;
import multiplicity3.csys.factory.ContentTypeNotBoundException;
import multiplicity3.input.MultiTouchEventAdapter;
import multiplicity3.input.events.MultiTouchCursorEvent;
import synergynet3.additionalUtils.AdditionalSynergyNetUtilities;
import synergynet3.additionalitems.interfaces.ICachableImage;
import synergynet3.feedbacksystem.defaultfeedbacktypes.AudioFeedback;
import synergynet3.feedbacksystem.defaultfeedbacktypes.SimpleTrafficLightFeedback;
import synergynet3.feedbacksystem.defaultfeedbacktypes.SmilieFeedback;
import synergynet3.projector.network.ProjectorTransferUtilities;
import synergynet3.tracking.applications.TrackedApp;
import synergynet3.tracking.applications.earlyyears.EarlyYearsTrackedApp;
import synergynet3.web.earlyyears.shared.EarlyYearsActivity;

import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;

/**
 * The Class TrainTracksTrackedApp.
 */
public class TrainTracksTrackedApp extends EarlyYearsTrackedApp
{

	/** The Constant RESOURCES_DIR. */
	private static final String RESOURCES_DIR = "synergynet3/earlyyears/table/applications/traintracks/";

	/** The Constant ROAD. */
	private static final String ROAD = "rr";

	/** The Constant TRACK_DIMENSION. */
	private static final int TRACK_DIMENSION = 200;

	/** The Constant TRAIN. */
	private static final String TRAIN = "tt";

	/** The mode. */
	private static String MODE = TRAIN;

	/** The corners. */
	private ArrayList<ICachableImage> corners = new ArrayList<ICachableImage>();

	/** The crosses. */
	private ArrayList<ICachableImage> crosses = new ArrayList<ICachableImage>();

	/** The snap points. */
	private ArrayList<Vector2f> snapPoints = new ArrayList<Vector2f>();

	/** The straights. */
	private ArrayList<ICachableImage> straights = new ArrayList<ICachableImage>();

	/** The track piece count. */
	private int trackPieceCount = 0;

	/** The placed track pieces. */
	ArrayList<ICachableImage> placedTrackPieces = new ArrayList<ICachableImage>();

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 * @throws SocketException
	 *             the socket exception
	 */
	public static void main(String[] args) throws SocketException
	{
		if (args.length > 0)
		{
			IdentityConfigPrefsItem idprefs = new IdentityConfigPrefsItem();
			idprefs.setID(args[0]);
		}

		TrackedApp.initialiseTrackingAppArgs(args);

		MultiplicityClient client = MultiplicityClient.get();
		client.start();
		TrainTracksTrackedApp app = new TrainTracksTrackedApp();
		client.setCurrentApp(app);

	}

	/**
	 * Adds the corner.
	 */
	public void addCorner()
	{
		createTrackPiece("corner");
	}

	/**
	 * Adds the cross.
	 */
	public void addCross()
	{
		createTrackPiece("cross");
	}

	/**
	 * Adds the straight.
	 */
	public void addStraight()
	{
		createTrackPiece("straight");
	}

	/**
	 * Gets the num of corners.
	 *
	 * @return the num of corners
	 */
	public Integer getNumOfCorners()
	{
		return corners.size();
	}

	/**
	 * Gets the num of crosses.
	 *
	 * @return the num of crosses
	 */
	public Integer getNumOfCrosses()
	{
		return crosses.size();
	}

	/**
	 * Gets the num of straights.
	 *
	 * @return the num of straights
	 */
	public Integer getNumOfStraights()
	{
		return straights.size();
	}

	/**
	 * Removes the corner.
	 */
	public void removeCorner()
	{
		stage.removeItem(corners.get(corners.size() - 1));
		corners.remove(corners.get(corners.size() - 1));
		updateTrackedPieces();
	}

	/**
	 * Removes the cross.
	 */
	public void removeCross()
	{
		stage.removeItem(crosses.get(crosses.size() - 1));
		crosses.remove(crosses.get(crosses.size() - 1));
		updateTrackedPieces();
	}

	/**
	 * Removes the straight.
	 */
	public void removeStraight()
	{
		stage.removeItem(straights.get(straights.size() - 1));
		straights.remove(straights.get(straights.size() - 1));
		updateTrackedPieces();
	}

	/**
	 * Sets the mode.
	 *
	 * @param mode
	 *            the new mode
	 */
	public void setMode(boolean mode)
	{
		if (mode)
		{
			setRoadMode();
		}
		else
		{
			setTrainMode();
		}
	}

	/**
	 * Change piece images.
	 */
	private void changePieceImages()
	{
		for (ICachableImage trackPiece : crosses)
		{
			trackPiece.setImage(RESOURCES_DIR + MODE + "cross" + ".png");
		}
		for (ICachableImage trackPiece : corners)
		{
			trackPiece.setImage(RESOURCES_DIR + MODE + "corner" + ".png");
		}
		for (ICachableImage trackPiece : straights)
		{
			trackPiece.setImage(RESOURCES_DIR + MODE + "straight" + ".png");
		}
	}

	/**
	 * Creates the pieces.
	 *
	 * @param number
	 *            the number
	 * @param name
	 *            the name
	 */
	private void createPieces(int number, String name)
	{
		for (int i = 0; i < number; i++)
		{
			createTrackPiece(name);
		}
	}

	/**
	 * Creates the track piece.
	 *
	 * @param piece
	 *            the piece
	 */
	private void createTrackPiece(String piece)
	{
		try
		{
			final ICachableImage trackPiece = contentFactory.create(ICachableImage.class, "trackPiece" + trackPieceCount, UUID.randomUUID());
			trackPiece.setImage(RESOURCES_DIR + MODE + piece + ".png");
			behaviourMaker.addBehaviour(trackPiece, RotateTranslateScaleBehaviour.class).setScaleEnabled(false);
			stage.addItem(trackPiece);
			randomlyPlaceTrackPiece(trackPiece);
			randomlyRotateTrackPiece(trackPiece);

			if (piece.equals("corner"))
			{
				trackPiece.setSize(TRACK_DIMENSION, TRACK_DIMENSION);
				ProjectorTransferUtilities.get().addToTransferableContents(trackPiece, 163, 164, "corner" + corners.size());
				corners.add(trackPiece);
			}
			else if (piece.equals("cross"))
			{
				trackPiece.setSize(TRACK_DIMENSION, TRACK_DIMENSION);
				ProjectorTransferUtilities.get().addToTransferableContents(trackPiece, 208, 208, "cross" + crosses.size());
				crosses.add(trackPiece);
			}
			else
			{
				trackPiece.setSize(TRACK_DIMENSION, TRACK_DIMENSION);
				ProjectorTransferUtilities.get().addToTransferableContents(trackPiece, 257, 120, "straight" + straights.size());
				straights.add(trackPiece);
			}

			trackPiece.getMultiTouchDispatcher().addListener(new MultiTouchEventAdapter()
			{
				@Override
				public void cursorReleased(MultiTouchCursorEvent event)
				{

					Vector2f target = null;
					float distance = 0;

					for (Vector2f potentialTarget : snapPoints)
					{
						if (target == null)
						{
							target = potentialTarget;
							distance = potentialTarget.distance(trackPiece.getWorldLocation());
						}
						else
						{
							float potentialDistance = potentialTarget.distance(trackPiece.getWorldLocation());
							if (potentialDistance < distance)
							{
								target = potentialTarget;
								distance = potentialTarget.distance(trackPiece.getWorldLocation());
							}
						}
					}

					for (ICachableImage placedTrackPiece : placedTrackPieces)
					{
						if (placedTrackPiece != trackPiece)
						{
							if (target.distance(placedTrackPiece.getWorldLocation()) < 0.1)
							{
								return;
							}
						}
					}

					trackPiece.setWorldLocation(target);

					float angle = trackPiece.getRelativeRotation() * FastMath.RAD_TO_DEG;
					float offSetAngle = 0;
					if ((angle % 90) > 45)
					{
						offSetAngle = 1;
					}
					angle = (FastMath.floor(angle / 90) + offSetAngle) * 90;
					trackPiece.setRelativeRotation(angle * FastMath.DEG_TO_RAD);

					stage.getZOrderManager().sendToBottom(trackPiece);
				}
			});

			updateTrackedPieces();
			trackPieceCount++;

		}
		catch (ContentTypeNotBoundException e)
		{
			AdditionalSynergyNetUtilities.log(Level.SEVERE, "ContentTypeNotBoundException", e);
		}
	}

	/**
	 * Randomly place track piece.
	 *
	 * @param trackPiece
	 *            the track piece
	 */
	private void randomlyPlaceTrackPiece(ICachableImage trackPiece)
	{
		int lowerBound = (int) ((-displayWidth / 2) + (trackPiece.getWidth() / 2) + 0.5);
		int upperBound = (int) (((displayWidth / 2) - (trackPiece.getWidth() / 2)) + 0.5);
		int x = lowerBound + (int) ((Math.random() * (upperBound - lowerBound)) + 0.5);
		lowerBound = (int) ((-displayHeight / 2) + (trackPiece.getHeight() / 2) + 0.5);
		upperBound = (int) (((displayHeight / 2) - (trackPiece.getHeight() / 2)) + 0.5);
		int y = lowerBound + (int) ((Math.random() * (upperBound - lowerBound)) + 0.5);
		trackPiece.setRelativeLocation(new Vector2f(x, y));
	}

	/**
	 * Randomly rotate track piece.
	 *
	 * @param trackPiece
	 *            the track piece
	 */
	private void randomlyRotateTrackPiece(ICachableImage trackPiece)
	{
		int angle = (int) ((Math.random() * (360)) + 0.5);
		trackPiece.setRelativeRotation((float) Math.toRadians(angle));
	}

	/**
	 * Sets the road mode.
	 */
	private void setRoadMode()
	{
		MODE = ROAD;
		changePieceImages();
	}

	/**
	 * Sets the train mode.
	 */
	private void setTrainMode()
	{
		MODE = TRAIN;
		changePieceImages();
	}

	/**
	 * Setup snap points.
	 */
	private void setupSnapPoints()
	{
		float x = TRACK_DIMENSION / 2;
		while (x < displayWidth)
		{
			float y = TRACK_DIMENSION / 2;
			while (y < displayHeight)
			{
				snapPoints.add(new Vector2f(x, y));
				y += TRACK_DIMENSION;
			}
			x += TRACK_DIMENSION;
		}
	}

	/**
	 * Update tracked pieces.
	 */
	private void updateTrackedPieces()
	{
		placedTrackPieces.clear();
		placedTrackPieces.addAll(corners);
		placedTrackPieces.addAll(crosses);
		placedTrackPieces.addAll(straights);
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.SynergyNetApp#getSpecificFriendlyAppName()
	 */
	@Override
	protected String getSpecificFriendlyAppName()
	{
		return "TrainTracks";
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.SynergyNetApp#loadDefaultContent()
	 */
	@Override
	protected void loadDefaultContent() throws IOException, ContentTypeNotBoundException
	{

		feedbackTypes.add(SimpleTrafficLightFeedback.class);
		feedbackTypes.add(AudioFeedback.class);
		feedbackTypes.add(SmilieFeedback.class);

		syncName = EarlyYearsActivity.TRAIN_TRACKS;
		createPieces(6, "corner");
		createPieces(1, "cross");
		createPieces(4, "straight");

		setAbilityToAddContentFromGallery(false);

		setupSnapPoints();

	}

}
