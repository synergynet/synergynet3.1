package synergynet3.activitypack2.table.flickgame;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;
import java.util.logging.Level;

import multiplicity3.appsystem.IQueueOwner;
import multiplicity3.appsystem.MultiplicityClient;
import multiplicity3.config.identity.IdentityConfigPrefsItem;
import multiplicity3.csys.animation.AnimationSystem;
import multiplicity3.csys.behaviours.IBehaviour;
import multiplicity3.csys.behaviours.RotateTranslateScaleBehaviour;
import multiplicity3.csys.behaviours.inertia.InertiaBehaviour;
import multiplicity3.csys.factory.ContentTypeNotBoundException;
import multiplicity3.csys.items.item.IItem;
import multiplicity3.csys.items.mutablelabel.IMutableLabel;
import multiplicity3.csys.items.shapes.IColourRectangle;
import multiplicity3.input.IMultiTouchEventListener;
import multiplicity3.input.MultiTouchEventAdapter;
import multiplicity3.input.MultiTouchInputComponent;
import multiplicity3.input.events.MultiTouchCursorEvent;
import multiplicity3.input.events.MultiTouchObjectEvent;
import synergynet3.SynergyNetApp;
import synergynet3.activitypack2.core.FlickGameControlComms;
import synergynet3.activitypack2.core.FlickGameDeviceControl;
import synergynet3.activitypack2.table.flickgame.network.FlickGameSync;
import synergynet3.activitypack2.web.shared.flickgame.FlickGameScore;
import synergynet3.additionalUtils.AdditionalSynergyNetUtilities;
import synergynet3.additionalUtils.EnvironmentAlignment;
import synergynet3.additionalitems.PerformActionOnAllDescendents;
import synergynet3.additionalitems.interfaces.IButtonbox;
import synergynet3.additionalitems.interfaces.ICachableImage;
import synergynet3.behaviours.BehaviourUtilities;
import synergynet3.behaviours.bouncer.CircleBouncerAnimationElement;
import synergynet3.behaviours.collision.CollisionBoxAnimationElement;
import synergynet3.behaviours.networkflick.NetworkFlickBehaviour;
import synergynet3.behaviours.networkflick.NetworkFlickLogging;
import synergynet3.behaviours.networkflick.NetworkFlickLogging.FLICKTYPE;
import synergynet3.behaviours.networkflick.messages.FlickMessage;
import synergynet3.feedbacksystem.FeedbackSystem;
import synergynet3.fonts.FontColour;
import synergynet3.fonts.FontUtil;

import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;

/**
 * The Class FlickGameApp.
 */
public class FlickGameApp extends SynergyNetApp implements IMultiTouchEventListener
{

	/** The alignment. */
	private static EnvironmentAlignment alignment = EnvironmentAlignment.HORIZONTAL;

	/** The Constant BALL_RADIUS. */
	private static final float BALL_RADIUS = 25f;

	/** The Constant BAR_WIDTH. */
	private final static float BAR_WIDTH = 5f;

	/** The Constant DATE_FORMAT. */
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");

	/** The deceleration. */
	private static float deceleration = 7.5f;

	/** The Constant END_ZONE_WIDTH. */
	private final static float END_ZONE_WIDTH = 100f;

	/** The flick game sync. */
	private static FlickGameSync flickGameSync = null;

	/** The Constant LOG_NAME_FORMAT. */
	private static final SimpleDateFormat LOG_NAME_FORMAT = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");

	/** The log address. */
	private static String logAddress = "";

	/** The Constant PADDLE_LIMIT. */
	private static final int PADDLE_LIMIT = 4;

	/** The Constant PADDLE_RADIUS. */
	private static final float PADDLE_RADIUS = 75f;

	/** The paddle choice. */
	private static boolean paddleChoice = true;

	/** The paddle number. */
	private static int paddleNumber = 1;

	/** The Constant RESOURCES_DIR. */
	private static final String RESOURCES_DIR = "synergynet3/activitypack2/table/flickgame/";

	/** The score limit. */
	private static int scoreLimit = 5;

	/** The table colour. */
	private static String tableColour = "red";

	/** The Constant TIME_FORMAT. */
	private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm ss.SS");

	/** The red score. */
	private int blueScore, redScore = 0;

	/** The cbaes. */
	private ArrayList<CircleBouncerAnimationElement> cbaes = new ArrayList<CircleBouncerAnimationElement>();

	/** The end zone. */
	private IColourRectangle endZone;

	/** The end zone dimensions. */
	private Vector2f endZoneDimensions;

	/** The first touch. */
	private Date firstTouch = null;

	/** The game items. */
	private ArrayList<IItem> gameItems = new ArrayList<IItem>();

	/** The id. */
	private String id = "";

	/** The initialised. */
	private boolean initialised = false;

	/** The last touch. */
	private Date lastTouch = null;

	/** The nf. */
	private NetworkFlickBehaviour nf;

	/** The paddles. */
	private ArrayList<ICachableImage> paddles = new ArrayList<ICachableImage>();

	/** The paddles label. */
	private IMutableLabel paddlesLabel;

	/** The score label red. */
	private IMutableLabel scoreLabelBlue, scoreLabelRed;

	/** The start screen items. */
	private ArrayList<IItem> startScreenItems = new ArrayList<IItem>();

	/** The start time. */
	private Date startTime = new Date();

	/** The touch count. */
	private int touchCount = 0;

	/** The winner label. */
	private IMutableLabel winnerLabel;

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args)
	{
		if (args.length > 0)
		{
			IdentityConfigPrefsItem idprefs = new IdentityConfigPrefsItem();
			idprefs.setID(args[0]);
		}

		try
		{
			logAddress = ManagementFactory.getRuntimeMXBean().getSystemProperties().get("log");
			AdditionalSynergyNetUtilities.logInfo("Using log at: " + logAddress);
		}
		catch (Exception e)
		{
			AdditionalSynergyNetUtilities.logInfo("No valid log address in arguments, no logging will take place.");
		}

		try
		{
			int alignmentIn = Integer.parseInt(ManagementFactory.getRuntimeMXBean().getSystemProperties().get("alignment"));
			if (alignmentIn == 0)
			{
				alignment = EnvironmentAlignment.HORIZONTAL;
			}
			else if (alignmentIn == 1)
			{
				alignment = EnvironmentAlignment.HORIZONTAL_INVERTED;
			}
			else if (alignmentIn == 2)
			{
				alignment = EnvironmentAlignment.VERTICAL;
			}
			else if (alignmentIn == 3)
			{
				alignment = EnvironmentAlignment.VERTICAL_INVERTED;
			}
		}
		catch (Exception e)
		{
		}

		AdditionalSynergyNetUtilities.logInfo("Using " + alignment + " alignment.");

		try
		{
			deceleration = Float.parseFloat(ManagementFactory.getRuntimeMXBean().getSystemProperties().get("deceleration"));
		}
		catch (Exception e)
		{
			AdditionalSynergyNetUtilities.logInfo("No valid deceleration value given, using default.");
		}
		AdditionalSynergyNetUtilities.logInfo("Using deceleration of: " + deceleration);

		try
		{
			scoreLimit = Integer.parseInt(ManagementFactory.getRuntimeMXBean().getSystemProperties().get("limit"));
		}
		catch (Exception e)
		{
			AdditionalSynergyNetUtilities.logInfo("No valid limit value given, using default.");
		}
		AdditionalSynergyNetUtilities.logInfo("Using limit of: " + scoreLimit);

		try
		{
			tableColour = ManagementFactory.getRuntimeMXBean().getSystemProperties().get("table");
		}
		catch (Exception e)
		{
			AdditionalSynergyNetUtilities.logInfo("No valid table colour value given, using default.");
		}
		if (tableColour == null)
		{
			tableColour = "red";
		}
		AdditionalSynergyNetUtilities.logInfo("Table colour is " + tableColour);

		try
		{
			String paddleAmount = ManagementFactory.getRuntimeMXBean().getSystemProperties().get("paddles");
			if (paddleAmount != null)
			{
				paddleChoice = false;
				paddleNumber = Integer.parseInt(ManagementFactory.getRuntimeMXBean().getSystemProperties().get("paddles"));
			}
		}
		catch (Exception e)
		{
			AdditionalSynergyNetUtilities.logInfo("No valid number of paddles predefined.");
		}
		if (paddleChoice)
		{
			AdditionalSynergyNetUtilities.logInfo("Number of paddles not predefined, leaving choice to the players.");
		}
		else
		{
			AdditionalSynergyNetUtilities.logInfo("Number of paddles set to " + paddleNumber);
		}

		MultiplicityClient client = MultiplicityClient.get();
		client.start();
		FlickGameApp app = new FlickGameApp();
		client.setCurrentApp(app);
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
		if (firstTouch == null)
		{
			firstTouch = new Date();
		}
		touchCount++;
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
		lastTouch = new Date();
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.SynergyNetApp#getSpecificFriendlyAppName()
	 */
	@Override
	public String getSpecificFriendlyAppName()
	{
		return "FlickGame";
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
		if (flickGameSync != null)
		{
			flickGameSync.stop();
		}
		super.onDestroy();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynet3.SynergyNetApp#onFlickArrival(synergynet3.behaviours.networkflick
	 * .messages.FlickMessage)
	 */
	@Override
	public void onFlickArrival(final FlickMessage message)
	{
		IItem item = BehaviourUtilities.onFlickArrival(message, stage, tableIdentity, deceleration);
		new PerformActionOnAllDescendents(item, false, false)
		{
			@Override
			protected void actionOnDescendent(IItem child)
			{
				for (IBehaviour behaviour : child.getBehaviours())
				{
					behaviour.setActive(false);
				}
			}
		};
		enableCollisionDetection(item);
		makeBallCollidable(item);
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.SynergyNetApp#shouldStart(multiplicity3.input.
	 * MultiTouchInputComponent, multiplicity3.appsystem.IQueueOwner)
	 */
	@Override
	public void shouldStart(MultiTouchInputComponent input, IQueueOwner iqo)
	{
		input.registerMultiTouchEventListener(this);
		super.shouldStart(input, iqo);
	}

	/**
	 * Update score.
	 *
	 * @param score
	 *            the score
	 */
	public void updateScore(FlickGameScore score)
	{
		blueScore = score.getScoreBlue();
		redScore = score.getScoreRed();
		if ((blueScore == 0) && (redScore == 0) && initialised)
		{
			showStartScreen(false);
		}
		setScoreLabel();
	}

	/**
	 * Destroy paddles.
	 */
	private void destroyPaddles()
	{
		for (int i = 0; i < paddles.size(); i++)
		{
			stage.removeItem(paddles.get(i));
			AnimationSystem.getInstance().remove(cbaes.get(i));
			cbaes.get(i).reset();
		}
		paddles.clear();
		cbaes.clear();
	}

	/**
	 * Enable collision detection.
	 *
	 * @param item
	 *            the item
	 */
	private void enableCollisionDetection(final IItem item)
	{
		CollisionBoxAnimationElement collisionAnimationElement = new CollisionBoxAnimationElement(item)
		{
			@Override
			protected void onEnteringCollision(IItem collidale)
			{
				stage.removeItem(item);

				for (NetworkFlickBehaviour behaviour : item.getBehaviours(NetworkFlickBehaviour.class))
				{
					behaviour.reset();
				}

				if (tableColour.equals("red"))
				{
					blueScore++;
				}
				else
				{
					redScore++;
				}

				FlickGameControlComms.get().setAllTablesScore(new FlickGameScore(blueScore, redScore));
				setScoreLabel();

				try
				{
					if ((blueScore < scoreLimit) && (redScore < scoreLimit))
					{
						generateBall();
					}
				}
				catch (ContentTypeNotBoundException e)
				{
					AdditionalSynergyNetUtilities.log(Level.SEVERE, "Content Type Not Bound", e);
				}

				AnimationSystem.getInstance().remove(this);
			}
		};
		collisionAnimationElement.addCollidable(endZone, endZoneDimensions);
		AnimationSystem.getInstance().add(collisionAnimationElement);
	}

	/**
	 * Generate ball.
	 *
	 * @throws ContentTypeNotBoundException
	 *             the content type not bound exception
	 */
	private void generateBall() throws ContentTypeNotBoundException
	{
		String fileName = "ball.png";
		ICachableImage ball = contentFactory.create(ICachableImage.class, fileName, UUID.randomUUID());
		ball.setImage(RESOURCES_DIR + fileName);
		ball.setSize(BALL_RADIUS * 2, BALL_RADIUS * 2);
		nf = behaviourMaker.addBehaviour(ball, NetworkFlickBehaviour.class);
		nf.setDeceleration(deceleration);
		nf.setMaxDimension(200f);
		ball.setRelativeLocation(new Vector2f(0, 0));
		ball.setInteractionEnabled(false);
		stage.addItem(ball);

		FeedbackSystem.registerAsFeedbackEligible(ball, BALL_RADIUS * 2, BALL_RADIUS * 2, stage);
		enableCollisionDetection(ball);

		makeBallCollidable(ball);
	}

	/**
	 * Generate end zone.
	 *
	 * @throws ContentTypeNotBoundException
	 *             the content type not bound exception
	 */
	private void generateEndZone() throws ContentTypeNotBoundException
	{
		endZone = contentFactory.create(IColourRectangle.class, "bg", UUID.randomUUID());
		endZone.setSolidBackgroundColour(ColorRGBA.Black);
		endZone.setInteractionEnabled(false);
		stage.addItem(endZone);
		gameItems.add(endZone);

		IColourRectangle bar = contentFactory.create(IColourRectangle.class, "bg", UUID.randomUUID());
		bar.setSolidBackgroundColour(ColorRGBA.White);
		bar.setInteractionEnabled(false);
		stage.addItem(bar);
		gameItems.add(bar);
		Vector2f barDimension;

		if (alignment == EnvironmentAlignment.HORIZONTAL)
		{
			endZoneDimensions = new Vector2f(END_ZONE_WIDTH, stage.getDisplayHeight());
			endZone.setRelativeLocation(new Vector2f(-stage.getDisplayWidth() / 2, 0));
			barDimension = new Vector2f(BAR_WIDTH, stage.getDisplayHeight());
			bar.setRelativeLocation(new Vector2f((-stage.getDisplayWidth() / 2) + (END_ZONE_WIDTH / 2), 0));
		}
		else if (alignment == EnvironmentAlignment.HORIZONTAL_INVERTED)
		{
			endZoneDimensions = new Vector2f(END_ZONE_WIDTH, stage.getDisplayHeight());
			endZone.setRelativeLocation(new Vector2f(stage.getDisplayWidth() / 2, 0));
			barDimension = new Vector2f(BAR_WIDTH, stage.getDisplayHeight());
			bar.setRelativeLocation(new Vector2f((stage.getDisplayWidth() / 2) - (END_ZONE_WIDTH / 2), 0));
		}
		else if (alignment == EnvironmentAlignment.VERTICAL)
		{
			endZoneDimensions = new Vector2f(stage.getDisplayWidth(), END_ZONE_WIDTH);
			endZone.setRelativeLocation(new Vector2f(0, stage.getDisplayHeight() / 2));
			barDimension = new Vector2f(stage.getDisplayWidth(), BAR_WIDTH);
			bar.setRelativeLocation(new Vector2f(0, (stage.getDisplayHeight() / 2) - (END_ZONE_WIDTH / 2)));
		}
		else
		{
			endZoneDimensions = new Vector2f(stage.getDisplayWidth(), END_ZONE_WIDTH);
			endZone.setRelativeLocation(new Vector2f(0, -stage.getDisplayHeight() / 2));
			barDimension = new Vector2f(stage.getDisplayWidth(), BAR_WIDTH);
			bar.setRelativeLocation(new Vector2f(0, (-stage.getDisplayHeight() / 2) + (END_ZONE_WIDTH / 2)));
		}
		endZone.setSize(endZoneDimensions);
		bar.setSize(barDimension);
	}

	/**
	 * Generate paddle number controls.
	 *
	 * @throws ContentTypeNotBoundException
	 *             the content type not bound exception
	 */
	private void generatePaddleNumberControls() throws ContentTypeNotBoundException
	{
		paddlesLabel = this.stage.getContentFactory().create(IMutableLabel.class, "paddlesLabel", UUID.randomUUID());
		paddlesLabel.setBoxSize(200, END_ZONE_WIDTH);
		paddlesLabel.setFont(FontUtil.getFont(FontColour.White));
		paddlesLabel.setInteractionEnabled(false);
		stage.addItem(paddlesLabel);
		paddlesLabel.getZOrderManager().setAutoBringToTop(true);
		paddlesLabel.getZOrderManager().setBringToTopPropagatesUp(false);
		startScreenItems.add(paddlesLabel);
		updatePaddlesLabel();

		if (alignment == EnvironmentAlignment.HORIZONTAL)
		{
			paddlesLabel.setRelativeRotation(FastMath.DEG_TO_RAD * -90);
			paddlesLabel.setRelativeLocation(new Vector2f(-stage.getDisplayWidth() / 4, 0));
		}
		else if (alignment == EnvironmentAlignment.HORIZONTAL_INVERTED)
		{
			paddlesLabel.setRelativeRotation(FastMath.DEG_TO_RAD * 90);
			paddlesLabel.setRelativeLocation(new Vector2f(stage.getDisplayWidth() / 4, 0));
		}
		else if (alignment == EnvironmentAlignment.VERTICAL)
		{
			paddlesLabel.setRelativeRotation(FastMath.DEG_TO_RAD * 180);
			paddlesLabel.setRelativeLocation(new Vector2f(0, stage.getDisplayHeight() / 4));
		}
		else
		{
			paddlesLabel.setRelativeLocation(new Vector2f(0, -stage.getDisplayHeight() / 4));
		}

		if (!paddleChoice)
		{
			return;
		}

		IButtonbox decreasePaddlesButton = stage.getContentFactory().create(IButtonbox.class, "button", UUID.randomUUID());
		decreasePaddlesButton.setText("-", ColorRGBA.Black, ColorRGBA.White, FontColour.White, END_ZONE_WIDTH / 4, END_ZONE_WIDTH / 4, stage);
		decreasePaddlesButton.setInteractionEnabled(false);
		stage.addItem(decreasePaddlesButton);
		decreasePaddlesButton.getZOrderManager().setAutoBringToTop(true);
		decreasePaddlesButton.getZOrderManager().setBringToTopPropagatesUp(false);
		startScreenItems.add(decreasePaddlesButton);
		decreasePaddlesButton.getListener().getMultiTouchDispatcher().addListener(new MultiTouchEventAdapter()
		{
			@Override
			public void cursorClicked(MultiTouchCursorEvent event)
			{
				if (paddleNumber > 1)
				{
					paddleNumber--;
				}
				updatePaddlesLabel();
			}
		});

		IButtonbox increasePaddlesButton = stage.getContentFactory().create(IButtonbox.class, "button", UUID.randomUUID());
		increasePaddlesButton.setText("+", ColorRGBA.Black, ColorRGBA.White, FontColour.White, END_ZONE_WIDTH / 4, END_ZONE_WIDTH / 4, stage);
		increasePaddlesButton.setInteractionEnabled(false);
		stage.addItem(increasePaddlesButton);
		increasePaddlesButton.getZOrderManager().setAutoBringToTop(true);
		increasePaddlesButton.getZOrderManager().setBringToTopPropagatesUp(false);
		startScreenItems.add(increasePaddlesButton);
		increasePaddlesButton.getListener().getMultiTouchDispatcher().addListener(new MultiTouchEventAdapter()
		{
			@Override
			public void cursorClicked(MultiTouchCursorEvent event)
			{
				if (paddleNumber < PADDLE_LIMIT)
				{
					paddleNumber++;
				}
				updatePaddlesLabel();
			}
		});

		if (alignment == EnvironmentAlignment.HORIZONTAL)
		{
			decreasePaddlesButton.setRelativeRotation(FastMath.DEG_TO_RAD * -90);
			decreasePaddlesButton.setRelativeLocation(new Vector2f(-stage.getDisplayWidth() / 4, 125));
			increasePaddlesButton.setRelativeRotation(FastMath.DEG_TO_RAD * -90);
			increasePaddlesButton.setRelativeLocation(new Vector2f(-stage.getDisplayWidth() / 4, -125));
		}
		else if (alignment == EnvironmentAlignment.HORIZONTAL_INVERTED)
		{
			decreasePaddlesButton.setRelativeRotation(FastMath.DEG_TO_RAD * 90);
			decreasePaddlesButton.setRelativeLocation(new Vector2f(stage.getDisplayWidth() / 4, -125));
			increasePaddlesButton.setRelativeRotation(FastMath.DEG_TO_RAD * 90);
			increasePaddlesButton.setRelativeLocation(new Vector2f(stage.getDisplayWidth() / 4, 125));
		}
		else if (alignment == EnvironmentAlignment.VERTICAL)
		{
			decreasePaddlesButton.setRelativeRotation(FastMath.DEG_TO_RAD * 180);
			decreasePaddlesButton.setRelativeLocation(new Vector2f(125, stage.getDisplayHeight() / 4));
			increasePaddlesButton.setRelativeRotation(FastMath.DEG_TO_RAD * 180);
			increasePaddlesButton.setRelativeLocation(new Vector2f(-125, stage.getDisplayHeight() / 4));
		}
		else
		{
			decreasePaddlesButton.setRelativeLocation(new Vector2f(-125, -stage.getDisplayHeight() / 4));
			increasePaddlesButton.setRelativeLocation(new Vector2f(125, -stage.getDisplayHeight() / 4));
		}
	}

	/**
	 * Generate paddles.
	 *
	 * @throws ContentTypeNotBoundException
	 *             the content type not bound exception
	 */
	private void generatePaddles() throws ContentTypeNotBoundException
	{
		String fileName = "blue_paddle.png";
		if (tableColour.equals("red"))
		{
			fileName = "red_paddle.png";
		}

		for (int i = 0; i < paddleNumber; i++)
		{
			ICachableImage paddle = contentFactory.create(ICachableImage.class, fileName, UUID.randomUUID());
			paddle.setImage(RESOURCES_DIR + fileName);
			paddle.setSize(PADDLE_RADIUS * 2, PADDLE_RADIUS * 2);

			RotateTranslateScaleBehaviour rts = behaviourMaker.addBehaviour(paddle, RotateTranslateScaleBehaviour.class);
			rts.setScaleEnabled(false);
			rts.setRotationEnabled(false);

			InertiaBehaviour ib = behaviourMaker.addBehaviour(paddle, InertiaBehaviour.class);
			ib.setDeceleration(200f);

			CircleBouncerAnimationElement cbae = new CircleBouncerAnimationElement(paddle, stage);
			cbae.setRadius(PADDLE_RADIUS);
			AnimationSystem.getInstance().add(cbae);

			stage.addItem(paddle);

			paddles.add(paddle);
			cbaes.add(cbae);
		}
		positionPaddles();
	}

	/**
	 * Generate scores.
	 *
	 * @throws ContentTypeNotBoundException
	 *             the content type not bound exception
	 */
	private void generateScores() throws ContentTypeNotBoundException
	{
		scoreLabelBlue = this.stage.getContentFactory().create(IMutableLabel.class, "scoreOneLabel", UUID.randomUUID());
		scoreLabelBlue.setFont(FontUtil.getFont(FontColour.Blue));
		scoreLabelBlue.setBoxSize((stage.getDisplayWidth()), END_ZONE_WIDTH);
		scoreLabelBlue.setRelativeLocation(new Vector2f(0, (-stage.getDisplayHeight() / 2) + 25));
		scoreLabelBlue.setInteractionEnabled(false);
		scoreLabelBlue.setRelativeScale(2f);
		stage.addItem(scoreLabelBlue);
		scoreLabelBlue.getZOrderManager().setAutoBringToTop(true);
		scoreLabelBlue.getZOrderManager().setBringToTopPropagatesUp(false);

		scoreLabelRed = this.stage.getContentFactory().create(IMutableLabel.class, "scoreTwoLabel", UUID.randomUUID());
		scoreLabelRed.setFont(FontUtil.getFont(FontColour.Red));
		scoreLabelRed.setBoxSize((stage.getDisplayWidth()), END_ZONE_WIDTH);
		scoreLabelRed.setRelativeLocation(new Vector2f(0, (-stage.getDisplayHeight() / 2) + 25));
		scoreLabelRed.setInteractionEnabled(false);
		scoreLabelRed.setRelativeScale(2f);
		stage.addItem(scoreLabelRed);
		scoreLabelRed.getZOrderManager().setAutoBringToTop(true);
		scoreLabelRed.getZOrderManager().setBringToTopPropagatesUp(false);

		if (alignment == EnvironmentAlignment.HORIZONTAL)
		{
			scoreLabelBlue.setRelativeLocation(new Vector2f((-stage.getDisplayWidth() / 2) + END_ZONE_WIDTH, (-stage.getDisplayHeight() / 2) + (END_ZONE_WIDTH / 2)));
			scoreLabelBlue.setRelativeRotation(FastMath.DEG_TO_RAD * -90);
			scoreLabelRed.setRelativeLocation(new Vector2f((-stage.getDisplayWidth() / 2) + END_ZONE_WIDTH, (stage.getDisplayHeight() / 2) - (END_ZONE_WIDTH / 2)));
			scoreLabelRed.setRelativeRotation(FastMath.DEG_TO_RAD * -90);
		}
		else if (alignment == EnvironmentAlignment.HORIZONTAL_INVERTED)
		{
			scoreLabelBlue.setRelativeLocation(new Vector2f((stage.getDisplayWidth() / 2) - END_ZONE_WIDTH, (stage.getDisplayHeight() / 2) - (END_ZONE_WIDTH / 2)));
			scoreLabelBlue.setRelativeRotation(FastMath.DEG_TO_RAD * 90);
			scoreLabelRed.setRelativeLocation(new Vector2f((stage.getDisplayWidth() / 2) - END_ZONE_WIDTH, (-stage.getDisplayHeight() / 2) + (END_ZONE_WIDTH / 2)));
			scoreLabelRed.setRelativeRotation(FastMath.DEG_TO_RAD * 90);
		}
		else if (alignment == EnvironmentAlignment.VERTICAL)
		{
			scoreLabelBlue.setRelativeLocation(new Vector2f((stage.getDisplayWidth() / 2) - (END_ZONE_WIDTH / 2), (stage.getDisplayHeight() / 2) - END_ZONE_WIDTH));
			scoreLabelBlue.setRelativeRotation(FastMath.DEG_TO_RAD * 180);
			scoreLabelRed.setRelativeLocation(new Vector2f((-stage.getDisplayWidth() / 2) + (END_ZONE_WIDTH / 2), (stage.getDisplayHeight() / 2) - END_ZONE_WIDTH));
			scoreLabelRed.setRelativeRotation(FastMath.DEG_TO_RAD * 180);
		}
		else
		{
			scoreLabelBlue.setRelativeLocation(new Vector2f((-stage.getDisplayWidth() / 2) + (END_ZONE_WIDTH / 2), (-stage.getDisplayHeight() / 2) + END_ZONE_WIDTH));
			scoreLabelRed.setRelativeLocation(new Vector2f((stage.getDisplayWidth() / 2) - (END_ZONE_WIDTH / 2), (-stage.getDisplayHeight() / 2) + END_ZONE_WIDTH));
		}
		setScoreLabel();
	}

	/**
	 * Generate start screen.
	 *
	 * @throws ContentTypeNotBoundException
	 *             the content type not bound exception
	 */
	private void generateStartScreen() throws ContentTypeNotBoundException
	{

		IButtonbox serveButton = stage.getContentFactory().create(IButtonbox.class, "button", UUID.randomUUID());
		serveButton.setText("New Game", ColorRGBA.Black, ColorRGBA.White, FontColour.White, 200, 75, stage);
		serveButton.getListener().getMultiTouchDispatcher().addListener(new MultiTouchEventAdapter()
		{
			@Override
			public void cursorClicked(MultiTouchCursorEvent event)
			{
				blueScore = 0;
				redScore = 0;
				setScoreLabel();
				FlickGameControlComms.get().setAllTablesScore(new FlickGameScore(blueScore, redScore));
				showStartScreen(false);
				try
				{
					generateBall();
				}
				catch (ContentTypeNotBoundException e)
				{
					AdditionalSynergyNetUtilities.log(Level.SEVERE, "Content Type Not Bound", e);
				}
			}
		});
		stage.addItem(serveButton);
		startScreenItems.add(serveButton);

		winnerLabel = this.stage.getContentFactory().create(IMutableLabel.class, "winnerLabel", UUID.randomUUID());
		winnerLabel.setBoxSize((stage.getDisplayWidth()), END_ZONE_WIDTH);
		winnerLabel.setInteractionEnabled(false);
		winnerLabel.setRelativeScale(2f);
		winnerLabel.setText("");
		stage.addItem(winnerLabel);
		winnerLabel.getZOrderManager().setAutoBringToTop(true);
		winnerLabel.getZOrderManager().setBringToTopPropagatesUp(false);
		startScreenItems.add(winnerLabel);

		IMutableLabel firstToLabel = stage.getContentFactory().create(IMutableLabel.class, "button", UUID.randomUUID());
		firstToLabel.setBoxSize((stage.getDisplayWidth()), END_ZONE_WIDTH);
		firstToLabel.setText("First to " + scoreLimit);
		firstToLabel.setFont(FontUtil.getFont(FontColour.White));
		firstToLabel.setInteractionEnabled(false);
		firstToLabel.setRelativeScale(0.8f);
		stage.addItem(firstToLabel);
		firstToLabel.getZOrderManager().setAutoBringToTop(true);
		firstToLabel.getZOrderManager().setBringToTopPropagatesUp(false);
		startScreenItems.add(firstToLabel);

		if (alignment == EnvironmentAlignment.HORIZONTAL)
		{
			serveButton.setRelativeRotation(FastMath.DEG_TO_RAD * -90);
			winnerLabel.setRelativeRotation(FastMath.DEG_TO_RAD * -90);
			winnerLabel.setRelativeLocation(new Vector2f(stage.getDisplayWidth() / 4, 0));
			firstToLabel.setRelativeRotation(FastMath.DEG_TO_RAD * -90);
			firstToLabel.setRelativeLocation(new Vector2f((-stage.getDisplayWidth() / 2) + END_ZONE_WIDTH, 0));
		}
		else if (alignment == EnvironmentAlignment.HORIZONTAL_INVERTED)
		{
			serveButton.setRelativeRotation(FastMath.DEG_TO_RAD * 90);
			winnerLabel.setRelativeRotation(FastMath.DEG_TO_RAD * 90);
			winnerLabel.setRelativeLocation(new Vector2f(-stage.getDisplayWidth() / 4, 0));
			firstToLabel.setRelativeRotation(FastMath.DEG_TO_RAD * 90);
			firstToLabel.setRelativeLocation(new Vector2f((stage.getDisplayWidth() / 2) - END_ZONE_WIDTH, 0));
		}
		else if (alignment == EnvironmentAlignment.VERTICAL)
		{
			serveButton.setRelativeRotation(FastMath.DEG_TO_RAD * 180);
			winnerLabel.setRelativeRotation(FastMath.DEG_TO_RAD * 180);
			winnerLabel.setRelativeLocation(new Vector2f(0, -stage.getDisplayHeight() / 4));
			firstToLabel.setRelativeRotation(FastMath.DEG_TO_RAD * 180);
			firstToLabel.setRelativeLocation(new Vector2f(0, (stage.getDisplayHeight() / 2) - END_ZONE_WIDTH));
		}
		else
		{
			winnerLabel.setRelativeLocation(new Vector2f(0, stage.getDisplayHeight() / 4));
			firstToLabel.setRelativeLocation(new Vector2f(0, (-stage.getDisplayHeight() / 2) + END_ZONE_WIDTH));
		}

		generatePaddleNumberControls();
	}

	/**
	 * Make ball collidable.
	 *
	 * @param ball
	 *            the ball
	 */
	private void makeBallCollidable(IItem ball)
	{
		for (CircleBouncerAnimationElement cbae : cbaes)
		{
			cbae.clearCollidable();
			cbae.addCollidable(ball, BALL_RADIUS);
		}
	}

	/**
	 * Position paddles.
	 */
	private void positionPaddles()
	{
		if (alignment == EnvironmentAlignment.HORIZONTAL)
		{
			for (int i = 0; i < paddles.size(); i++)
			{
				paddles.get(i).setRelativeLocation(new Vector2f(-stage.getDisplayWidth() / 4, (-stage.getDisplayHeight() / 2) + (((i + 1) * stage.getDisplayHeight()) / (paddles.size() + 1))));
			}
		}
		else if (alignment == EnvironmentAlignment.HORIZONTAL_INVERTED)
		{
			for (int i = 0; i < paddles.size(); i++)
			{
				paddles.get(i).setRelativeLocation(new Vector2f(stage.getDisplayWidth() / 4, (stage.getDisplayHeight() / 2) - (((i + 1) * stage.getDisplayHeight()) / (paddles.size() + 1))));
			}
		}
		else if (alignment == EnvironmentAlignment.VERTICAL)
		{
			for (int i = 0; i < paddles.size(); i++)
			{
				paddles.get(i).setRelativeLocation(new Vector2f((stage.getDisplayWidth() / 2) - (((i + 1) * stage.getDisplayWidth()) / (paddles.size() + 1)), stage.getDisplayHeight() / 4));
			}
		}
		else
		{
			for (int i = 0; i < paddles.size(); i++)
			{
				paddles.get(i).setRelativeLocation(new Vector2f((-stage.getDisplayWidth() / 2) + (((i + 1) * stage.getDisplayWidth()) / (paddles.size() + 1)), -stage.getDisplayHeight() / 4));
			}
		}
	}

	/**
	 * Sets the score label.
	 */
	private void setScoreLabel()
	{
		if (scoreLabelBlue != null)
		{
			scoreLabelBlue.setText("" + blueScore);
		}
		if (scoreLabelRed != null)
		{
			scoreLabelRed.setText("" + redScore);
		}
		if ((blueScore >= scoreLimit) || (redScore >= scoreLimit))
		{
			showStartScreen(true);
		}
	}

	/**
	 * Show start screen.
	 *
	 * @param isVisible
	 *            the is visible
	 */
	private void showStartScreen(boolean isVisible)
	{
		for (IItem item : startScreenItems)
		{
			item.setVisible(isVisible);
		}
		for (IItem item : gameItems)
		{
			item.setVisible(!isVisible);
		}

		if (!isVisible)
		{
			try
			{
				generatePaddles();
			}
			catch (ContentTypeNotBoundException e)
			{
				AdditionalSynergyNetUtilities.log(Level.SEVERE, "Content Type Not Bound", e);
			}
			firstTouch = null;
			startTime = new Date();
			touchCount = 0;
		}
		else
		{
			tidyAwayIrrelevantItems();
			writeToLog();
			destroyPaddles();
			if (paddleChoice)
			{
				paddleNumber = 1;
			}
			updatePaddlesLabel();
			if (redScore > blueScore)
			{
				winnerLabel.setText("Red Wins!");
				winnerLabel.setFont(FontUtil.getFont(FontColour.Red));
			}
			else if (redScore < blueScore)
			{
				winnerLabel.setText("Blue Wins!");
				winnerLabel.setFont(FontUtil.getFont(FontColour.Blue));
			}
		}
	}

	/**
	 * Tidy away irrelevant items.
	 */
	private void tidyAwayIrrelevantItems()
	{
		ArrayList<IItem> toRemove = new ArrayList<IItem>();
		for (IItem item : stage.getChildItems())
		{
			boolean shouldRemove = true;
			if (item.equals(scoreLabelBlue) || item.equals(scoreLabelRed))
			{
				shouldRemove = false;
			}
			if (shouldRemove)
			{
				for (IItem relevantItem : startScreenItems)
				{
					if (item.equals(relevantItem))
					{
						shouldRemove = false;
						break;
					}
				}
				if (shouldRemove)
				{
					for (IItem relevantItem : gameItems)
					{
						if (item.equals(relevantItem))
						{
							shouldRemove = false;
							break;
						}
					}
					if (shouldRemove)
					{
						toRemove.add(item);
					}
				}
			}
		}
		for (IItem item : toRemove)
		{
			stage.removeItem(item);
		}
	}

	/**
	 * Update paddles label.
	 */
	private void updatePaddlesLabel()
	{
		if (paddleNumber == 1)
		{
			paddlesLabel.setText(paddleNumber + " paddle");

		}
		else
		{
			paddlesLabel.setText(paddleNumber + " paddles");
		}
	}

	/**
	 * Write to log.
	 */
	private void writeToLog()
	{
		if ((firstTouch == null) || (lastTouch == null))
		{
			return;
		}
		if (logAddress != null)
		{
			if (!logAddress.equals(""))
			{
				String address = logAddress + File.separator + LOG_NAME_FORMAT.format(startTime) + "_" + id + "_FlickingLog.csv";
				File logFile = new File(address);
				if (!logFile.isFile())
				{
					AdditionalSynergyNetUtilities.logInfo("No log file found, creating new one.");
					try
					{
						if (!logFile.createNewFile())
						{
							AdditionalSynergyNetUtilities.logInfo("Unable to create new log file.");
							return;
						}
					}
					catch (IOException e1)
					{
						AdditionalSynergyNetUtilities.logInfo("Unable to create new log file.");
						return;
					}
				}
				AdditionalSynergyNetUtilities.logInfo("Saving data to " + address);
				try
				{
					FileWriter out = new FileWriter(address, true);
					BufferedWriter writer = new BufferedWriter(out);

					writer.write("Table," + id);
					writer.newLine();
					writer.write("Date," + DATE_FORMAT.format(startTime));
					writer.newLine();
					writer.write("Start," + TIME_FORMAT.format(startTime));
					writer.newLine();
					writer.write("End," + TIME_FORMAT.format(new Date()));
					writer.newLine();
					writer.newLine();

					writer.write("Deceleration," + deceleration);
					writer.newLine();
					writer.write("Alignment," + alignment);
					writer.newLine();
					writer.write("Limit," + scoreLimit);
					writer.newLine();
					writer.newLine();

					writer.write("Score, Red, Blue ");
					writer.newLine();
					writer.write(" , " + redScore + ", " + blueScore);
					writer.newLine();
					writer.newLine();

					writer.write("Paddles," + paddleNumber);
					writer.newLine();
					writer.newLine();

					String paddleTitles = "Puck interactions,";
					String paddleTaps = " ,";
					for (int i = 0; i < cbaes.size(); i++)
					{
						paddleTitles += ", Paddle " + (i + 1);
						paddleTaps += ", " + cbaes.get(i).getTapCount();
					}

					writer.write(paddleTitles);
					writer.newLine();
					writer.write(paddleTaps);
					writer.newLine();
					writer.newLine();

					writer.write("First Touch," + TIME_FORMAT.format(firstTouch));
					writer.newLine();
					writer.write("Last Touch," + TIME_FORMAT.format(lastTouch));
					writer.newLine();
					writer.newLine();

					writer.write("Total Number of Touches," + touchCount);
					writer.newLine();
					writer.newLine();

					writer.write("Bounces due to innacuracy," + NetworkFlickLogging.INACCURATE_BOUNCE_COUNT);
					writer.newLine();
					writer.write("Bounces due to lack of momentum," + NetworkFlickLogging.LACK_OF_MOMENTUM_BOUNCE_COUNT);
					writer.newLine();
					writer.newLine();

					writer.write("Flick Departures," + NetworkFlickLogging.DEPARTURE_COUNT);
					writer.newLine();
					writer.write("Flick Arrivals," + NetworkFlickLogging.ARRIVAL_COUNT);
					writer.newLine();
					writer.newLine();

					String[] bounceLogMessages = NetworkFlickLogging.BOUNCE_LOG.split(NetworkFlickLogging.LOG_PARSE_TOKEN);
					for (String message : bounceLogMessages)
					{
						writer.write(message);
						writer.newLine();
					}
					writer.newLine();

					String[] flickLogMessages = NetworkFlickLogging.FLICK_LOG.split(NetworkFlickLogging.LOG_PARSE_TOKEN);
					for (String message : flickLogMessages)
					{
						writer.write(message);
						writer.newLine();
					}

					writer.close();
				}
				catch (IOException e1)
				{
					AdditionalSynergyNetUtilities.logInfo("Unable to write to log file.");
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.SynergyNetApp#loadDefaultContent()
	 */
	@Override
	protected void loadDefaultContent()
	{
		id = getTableIdentity();
		FlickGameDeviceControl flickGameDeviceController = new FlickGameDeviceControl(id);
		flickGameSync = new FlickGameSync(flickGameDeviceController, this);

		enableNetworkFlick();
		NetworkFlickLogging.LOGGING_ENABLED = true;
		BehaviourUtilities.FLICK_TYPE = FLICKTYPE.PROPORTIONAL;

		try
		{
			generateStartScreen();
			generateEndZone();
			generateScores();
		}
		catch (ContentTypeNotBoundException e)
		{
			AdditionalSynergyNetUtilities.log(Level.SEVERE, "Content Type Not Bound", e);
		}
		initialised = true;
		showStartScreen(true);
	}

}
