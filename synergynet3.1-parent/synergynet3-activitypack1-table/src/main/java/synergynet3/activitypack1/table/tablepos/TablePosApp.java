package synergynet3.activitypack1.table.tablepos;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import multiplicity3.appsystem.IMultiplicityApp;
import multiplicity3.appsystem.IQueueOwner;
import multiplicity3.appsystem.MultiplicityClient;
import multiplicity3.config.position.PositionConfigPrefsItem;
import multiplicity3.csys.MultiplicityEnvironment;
import multiplicity3.csys.factory.ContentTypeNotBoundException;
import multiplicity3.csys.items.container.IContainer;
import multiplicity3.csys.items.image.IImage;
import multiplicity3.csys.items.line.ILine;
import multiplicity3.csys.items.shapes.IColourRectangle;
import multiplicity3.csys.stage.IStage;
import multiplicity3.input.IMultiTouchEventListener;
import multiplicity3.input.MultiTouchEventAdapter;
import multiplicity3.input.MultiTouchInputComponent;
import multiplicity3.input.events.MultiTouchCursorEvent;
import multiplicity3.input.events.MultiTouchObjectEvent;

import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;

/**
 * The Class TablePosApp.
 */
public class TablePosApp implements IMultiTouchEventListener, IMultiplicityApp
{

	/** The actual line to a angle. */
	private static float actualLineToBAngle, actualLineToAAngle = 0;

	/** The actuald. */
	private static float actualS, actualT, actualR, actualA, actualB, actuald = 0;

	/** The actual table o. */
	private static float actualTableX, actualTableY, actualTableO = 0;

	/** The Constant ARROW_SIZE. */
	private static final float ARROW_SIZE = 35;

	/** The Constant INITIAL_ARROW_OFFSET. */
	private static final float INITIAL_ARROW_OFFSET = 2000f;

	/** The Constant LINE_WIDTH. */
	private static final float LINE_WIDTH = 10f;

	/** The Constant log. */
	private static final Logger log = Logger.getLogger(TablePosApp.class.getName());

	/** The log address. */
	private static String logAddress = "";

	/** The loops. */
	private static int loops = 1;

	/** The Constant ORIGIN_SIZE. */
	private static final float ORIGIN_SIZE = 60f;

	/** The reference distance. */
	private static float referenceDistance = 20;

	/** The Constant RESOURCE_PATH. */
	private static final String RESOURCE_PATH = "synergynet3/activitypack1/table/tablepos/";

	/** The Constant SECONDARY_ARROW_OFFSET. */
	private static final float SECONDARY_ARROW_OFFSET = 200f;

	/** The Constant THRESHOLD_DISTANCE. */
	private static final float THRESHOLD_DISTANCE = 100f;

	/** The A array. */
	private float[] AArray;

	/** The arrow two. */
	private IImage arrowOne, arrowTwo;

	/** The arrow time. */
	private boolean arrowTime = false;

	/** The background. */
	private IColourRectangle background;

	/** The B array. */
	private float[] BArray;

	/** The change over. */
	private Date[] changeOver;

	/** The container. */
	private IContainer container;

	/** The current loop. */
	private int currentLoop = 0;

	/** The d array. */
	private float[] dArray;

	/** The first touch. */
	private Date[] firstTouch;

	/** The last touch. */
	private Date[] lastTouch;

	/** The line moving touches. */
	private HashMap<Long, ILine> lineMovingTouches = new HashMap<Long, ILine>();

	/** The world south. */
	private ILine lineOne, lineTwo, localNorth, worldSouth;

	/** The line to a angle array. */
	private float[] lineToAAngleArray;

	/** The line to b angle array. */
	private float[] lineToBAngleArray;

	/** The orientation touch count. */
	private int[] orientationTouchCount;

	/** The point one. */
	private Vector2f pointOne;

	/** The positioning touch count. */
	private int[] positioningTouchCount;

	/** The R array. */
	private float[] RArray;

	// private boolean redoing = false;
	// private int redoLoop = 0;
	/** The redo log. */
	private ArrayList<String> redoLog = new ArrayList<String>();

	/** The S array. */
	private float[] SArray;

	/** The stage. */
	private IStage stage;

	/** The start time. */
	private Date startTime = null;

	/** The table o array. */
	private float[] tableOArray;

	/** The table o. */
	private float tableX, tableY, tableO = 0;

	/** The T array. */
	private float[] TArray;

	/** The to redo. */
	private ArrayList<Integer> toRedo = new ArrayList<Integer>();

	/** The X array. */
	private float[] XArray;
	/** The Y array. */
	private float[] YArray;

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args)
	{

		boolean isReset = false;
		try
		{
			isReset = Boolean.parseBoolean(ManagementFactory.getRuntimeMXBean().getSystemProperties().get("reset"));
		}
		catch (Exception e)
		{
		}

		if (isReset)
		{

			log.info("Reset mode selected.");

			PositionConfigPrefsItem prefs = new PositionConfigPrefsItem();
			try
			{
				float x = Float.parseFloat(ManagementFactory.getRuntimeMXBean().getSystemProperties().get("x"));
				log.info("Setting X to " + x);
				prefs.setXPos(x);
			}
			catch (Exception e)
			{
				log.info("No valid X value given, table position's X value will not be overwritten.");
			}
			try
			{
				float y = Float.parseFloat(ManagementFactory.getRuntimeMXBean().getSystemProperties().get("y"));
				log.info("Setting Y to " + y);
				prefs.setYPos(y);
			}
			catch (Exception e)
			{
				log.info("No valid Y value given, table position's Y value will not be overwritten.");
			}

			try
			{
				float angle = Float.parseFloat(ManagementFactory.getRuntimeMXBean().getSystemProperties().get("angle"));
				log.info("Setting Orientation to " + angle);
				prefs.setAngle(angle);
			}
			catch (Exception e)
			{
				log.info("No valid Y value given, table position's Y value will not be overwritten.");
			}
		}
		else
		{

			try
			{
				referenceDistance = Float.parseFloat(ManagementFactory.getRuntimeMXBean().getSystemProperties().get("referencedistance"));
			}
			catch (Exception e)
			{
				log.info("No reference distance set in arguments, using default.");
			}
			log.info("Reference Distance 1 set to: " + referenceDistance + " metres");
			try
			{
				logAddress = ManagementFactory.getRuntimeMXBean().getSystemProperties().get("log");
				log.info("Using log at: " + logAddress);
			}
			catch (Exception e)
			{
				log.info("No valid log address in arguments, no logging will take place.");
			}
			try
			{
				loops = Integer.parseInt(ManagementFactory.getRuntimeMXBean().getSystemProperties().get("loops"));
				log.info("Using log at: " + logAddress);
			}
			catch (Exception e)
			{
				log.info("No valid log address in arguments, no logging will take place.");
			}

			try
			{
				actualTableX = Float.parseFloat(ManagementFactory.getRuntimeMXBean().getSystemProperties().get("x"));
				actualTableY = Float.parseFloat(ManagementFactory.getRuntimeMXBean().getSystemProperties().get("y"));
				actualTableO = FastMath.DEG_TO_RAD * Float.parseFloat(ManagementFactory.getRuntimeMXBean().getSystemProperties().get("angle"));

				calculateOptimalTableValues();

				log.info("Setting actual Table Position");
			}
			catch (Exception e)
			{
				log.info("Tables actual position not defined, log will not give accurate deviation values.");
			}

			MultiplicityClient client = MultiplicityClient.get();
			client.start();
			TablePosApp app = new TablePosApp();
			client.setCurrentApp(app);
		}
	}

	/**
	 * Calculate optimal table values.
	 */
	private static void calculateOptimalTableValues()
	{
		actuald = FastMath.sqrt(FastMath.sqr(actualTableX) + FastMath.sqr(actualTableY));
		actualT = FastMath.asin((actualTableX * FastMath.sin(FastMath.DEG_TO_RAD * 90)) / actuald);
		actualR = (FastMath.DEG_TO_RAD * 90) - actualT;
		actualA = (FastMath.DEG_TO_RAD * 90) - actualR;
		float e = FastMath.sqrt((FastMath.sqr(referenceDistance) + FastMath.sqr(actuald)) - (2 * referenceDistance * actuald * FastMath.cos(actualT)));
		actualS = FastMath.asin((referenceDistance * FastMath.sin(actualT)) / e);
		actualB = (FastMath.DEG_TO_RAD * 180) - actualS - actualA;
		actualLineToAAngle = (actualT + (FastMath.DEG_TO_RAD * 180)) - actualTableO;
		actualLineToBAngle = actualLineToAAngle + actualS;
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
		if (arrowTime)
		{
			if (lineMovingTouches.containsKey(event.getCursorID()))
			{
				moveLine(event, lineMovingTouches.get(event.getCursorID()));
			}
		}
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

		if (currentLoop < loops)
		{
			if (firstTouch[currentLoop] == null)
			{
				firstTouch[currentLoop] = new Date();
			}
		}

		if (arrowTime)
		{
			positioningTouchCount[currentLoop]++;
			ILine line = lineOne;

			Vector2f worldPositionOfCursor = new Vector2f();
			stage.tableToWorld(event.getPosition(), worldPositionOfCursor);

			float distance = arrowOne.getWorldLocation().distance(worldPositionOfCursor);

			float distanceOfLineTwo = arrowTwo.getWorldLocation().distance(worldPositionOfCursor);

			if (distanceOfLineTwo < distance)
			{
				distance = distanceOfLineTwo;
				line = lineTwo;
			}

			if (distance < THRESHOLD_DISTANCE)
			{
				if (!lineMovingTouches.containsValue(line))
				{
					lineMovingTouches.put(event.getCursorID(), line);
					moveLine(event, line);
				}
			}
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
		if (arrowTime)
		{
			if (lineMovingTouches.containsKey(event.getCursorID()))
			{
				lineMovingTouches.remove(event.getCursorID());
			}
		}
	}

	/**
	 * Fresh start.
	 */
	public void freshStart()
	{
		currentLoop = 0;
		startTime = new Date();
		tableOArray = new float[loops];
		lineToBAngleArray = new float[loops];
		lineToAAngleArray = new float[loops];
		lastTouch = new Date[loops];
		changeOver = new Date[loops];
		firstTouch = new Date[loops];
		positioningTouchCount = new int[loops];
		orientationTouchCount = new int[loops];
		SArray = new float[loops];
		TArray = new float[loops];
		RArray = new float[loops];
		AArray = new float[loops];
		BArray = new float[loops];
		dArray = new float[loops];
		XArray = new float[loops];
		YArray = new float[loops];
		// redoLoop = 0;
		toRedo.clear();
		// redoing = false;
		redoLog.clear();
		setupInitialContent();
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.appsystem.IMultiplicityApp#getFriendlyAppName()
	 */
	@Override
	public String getFriendlyAppName()
	{
		return "TablePos";
	}

	/**
	 * Move line.
	 *
	 * @param event
	 *            the event
	 * @param line
	 *            the line
	 */
	public void moveLine(MultiTouchCursorEvent event, ILine line)
	{
		Vector2f worldPositionOfCursor = new Vector2f();
		stage.tableToWorld(event.getPosition(), worldPositionOfCursor);
		float arrowDistanceFromOrigin = pointOne.distance(worldPositionOfCursor);

		float x = worldPositionOfCursor.x + (((worldPositionOfCursor.x - pointOne.x) / arrowDistanceFromOrigin) * INITIAL_ARROW_OFFSET);
		float y = worldPositionOfCursor.y + (((worldPositionOfCursor.y - pointOne.y) / arrowDistanceFromOrigin) * INITIAL_ARROW_OFFSET);

		line.setEndPosition(new Vector2f(x, y));
		setArrowPosAndRot(worldPositionOfCursor, line);
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
	 * @see multiplicity3.appsystem.IMultiplicityApp#onDestroy()
	 */
	@Override
	public void onDestroy()
	{
	}

	// private float getStandardDeviationOfAngles(float[] range){
	//
	// float[] sinTotals = new float[range.length];
	// float[] cosTotals = new float[range.length];
	//
	// for (int i = 0; i < range.length; i++){
	// sinTotals[i] = FastMath.sin(standardiseAngle(range[i]));
	// cosTotals[i] = FastMath.cos(standardiseAngle(range[i]));
	// }
	//
	// float sinAverage = getAverageOfLinearValues(sinTotals);
	// float cosAverage = getAverageOfLinearValues(cosTotals);
	//
	// float sinSD = getStandardDeviationOfLinearValues(sinTotals, sinAverage);
	// float cosSD = getStandardDeviationOfLinearValues(cosTotals, cosAverage);
	//
	// float standardDeviation = ((FastMath.atan2(sinSD, cosSD)) +
	// (FastMath.DEG_TO_RAD * 360)) % (FastMath.DEG_TO_RAD * 360);
	//
	// return standardDeviation;
	// }
	//
	// private float getStandardDeviationOfLinearValues(float[] values, float
	// avg){
	// float sd = 0;
	// for (int i=0; i< values.length; i++)sd +=
	// FastMath.sqr(standardiseAngle(values[i])-avg);
	// sd /= values.length;
	// sd = FastMath.sqrt(sd);
	// return sd;
	// }
	//
	// private boolean isAngleWithinStandardDeviation(float value, float
	// average, float standardDeviation) {
	// return isWithinXDegreesOf(standardDeviation * 3, average, value);
	// }

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.appsystem.IMultiplicityApp#shouldStart(multiplicity3.input
	 * .MultiTouchInputComponent, multiplicity3.appsystem.IQueueOwner)
	 */
	@Override
	public void shouldStart(MultiTouchInputComponent input, IQueueOwner iqo)
	{
		input.registerMultiTouchEventListener(this);
		log.info("init");
		this.stage = MultiplicityEnvironment.get().getLocalStages().get(0);
		this.stage.getZOrderManager().setAutoBringToTop(false);
		freshStart();
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.appsystem.IMultiplicityApp#shouldStop()
	 */
	@Override
	public void shouldStop()
	{
	}

	/**
	 * Calculate table info.
	 */
	private void calculateTableInfo()
	{
		tableO = TablePosUtilities.getAverageOfAngles(tableOArray);
		float lineToBAngle = TablePosUtilities.getAverageOfAngles(lineToBAngleArray);
		float lineToAAngle = TablePosUtilities.getAverageOfAngles(lineToAAngleArray);
		float S = FastMath.abs(TablePosUtilities.angleBetweenLines(lineOne, lineTwo));
		float T = TablePosUtilities.angleBetweenLines(worldSouth, lineTwo);
		if (S < 0)
		{
			T += (FastMath.DEG_TO_RAD * 380);
		}
		float R = (FastMath.DEG_TO_RAD * 90) - T;
		float A = (FastMath.DEG_TO_RAD * 90) - R;
		float B = (FastMath.DEG_TO_RAD * 180) - S - A;
		float d = FastMath.sin(B) * (referenceDistance / FastMath.sin(S));
		tableX = FastMath.sin(T) * (d / FastMath.sin(FastMath.DEG_TO_RAD * 90));
		tableY = FastMath.sin(R) * (d / FastMath.sin(FastMath.DEG_TO_RAD * 90));

		TablePosUtilities.writeToLog(logAddress, startTime, lineToBAngle, lineToAAngle, S, T, R, A, B, d, actualLineToAAngle, actualLineToBAngle, actualS, actualT, actualR, actualA, actualB, actuald, actualTableX, actualTableY, actualTableO, referenceDistance, loops, firstTouch, changeOver, lastTouch, orientationTouchCount, positioningTouchCount, lineToAAngleArray, lineToBAngleArray, SArray, TArray, RArray, AArray, BArray, tableOArray, XArray, YArray, dArray, tableX, tableY, tableO, redoLog);
	}

	/**
	 * Ok on arrows.
	 */
	private void okOnArrows()
	{
		lastTouch[currentLoop] = new Date();
		recordLineAngles();

		if (!TablePosUtilities.isWithinXDegreesOf(FastMath.DEG_TO_RAD * 100, actualTableO, tableOArray[currentLoop]))
		{
			setupInitialContent();
			redoLog.add("Execution " + (currentLoop + 1) + "," + (FastMath.RAD_TO_DEG * tableOArray[currentLoop]) + ", " + (FastMath.RAD_TO_DEG * lineToAAngleArray[currentLoop]) + ", " + (FastMath.RAD_TO_DEG * lineToBAngleArray[currentLoop]) + "User's orientation is over 100 degrees from the table's actual orientation.");
			return;
		}

		// if (redoing){
		// redoLoop++;
		// if (redoLoop > toRedo.size()-1){
		// redoing = false;
		// currentLoop = loops;
		// }else{
		// currentLoop = toRedo.get(redoLoop);
		// }
		// }else{
		currentLoop++;
		// }

		if (currentLoop >= loops)
		{

			// toRedo.clear();
			//
			// float orientationAVG = getAverageOfAngles(tableOArray);
			// float aAVG = getAverageOfAngles(lineToAAngleArray);
			// float bAVG = getAverageOfAngles(lineToBAngleArray);
			// float orientationSD = getStandardDeviationOfAngles(tableOArray);
			// float aSD = getStandardDeviationOfAngles(lineToAAngleArray);
			// float bSD = getStandardDeviationOfAngles(lineToBAngleArray);
			//
			// for (int i = 0; i < loops; i++){
			// if (!(isAngleWithinStandardDeviation(tableOArray[i],
			// orientationAVG, orientationSD) &&
			// isAngleWithinStandardDeviation(lineToAAngleArray[i], aAVG, aSD)
			// && isAngleWithinStandardDeviation(lineToBAngleArray[i], bAVG,
			// bSD))){
			// redoing = true;
			// toRedo.add(i);
			// redoLog.add("Execution " + (i+1) +
			// " is outside the standard deviation, " + (FastMath.RAD_TO_DEG *
			// tableOArray[i])
			// + ", " + (FastMath.RAD_TO_DEG * lineToAAngleArray[i]) + ", " +
			// (FastMath.RAD_TO_DEG * lineToBAngleArray[i]));
			// }
			// }
			//
			// if (redoing){
			// redoLoop = 0;
			// currentLoop = toRedo.get(redoLoop);
			// setupInitialContent();
			// }else{
			calculateTableInfo();
			arrowTime = false;
			TablePosUtilities.displayResults(stage, tableX, tableY, tableO, this);
			// }
		}
		else
		{
			setupInitialContent();
		}
	}

	/**
	 * Record line angles.
	 */
	private void recordLineAngles()
	{
		float lineToBAngle = TablePosUtilities.angleBetweenLines(localNorth, lineOne);
		if (lineToBAngle < 0)
		{
			lineToBAngle += (FastMath.DEG_TO_RAD * 360);
		}
		lineToBAngleArray[currentLoop] = lineToBAngle;

		float lineToAAngle = TablePosUtilities.angleBetweenLines(localNorth, lineTwo);
		if (lineToAAngle < 0)
		{
			lineToAAngle += (FastMath.DEG_TO_RAD * 360);
		}
		lineToAAngleArray[currentLoop] = lineToAAngle;

		SArray[currentLoop] = FastMath.abs(TablePosUtilities.angleBetweenLines(lineOne, lineTwo));
		TArray[currentLoop] = TablePosUtilities.angleBetweenLines(worldSouth, lineTwo);
		if (SArray[currentLoop] < 0)
		{
			TArray[currentLoop] += (FastMath.DEG_TO_RAD * 380);
		}
		RArray[currentLoop] = (FastMath.DEG_TO_RAD * 90) - TArray[currentLoop];
		AArray[currentLoop] = (FastMath.DEG_TO_RAD * 90) - RArray[currentLoop];
		BArray[currentLoop] = (FastMath.DEG_TO_RAD * 180) - SArray[currentLoop] - AArray[currentLoop];
		dArray[currentLoop] = FastMath.sin(BArray[currentLoop]) * (referenceDistance / FastMath.sin(SArray[currentLoop]));
		XArray[currentLoop] = FastMath.sin(TArray[currentLoop]) * (dArray[currentLoop] / FastMath.sin(FastMath.DEG_TO_RAD * 90));
		YArray[currentLoop] = FastMath.sin(RArray[currentLoop]) * (dArray[currentLoop] / FastMath.sin(FastMath.DEG_TO_RAD * 90));
	}

	/**
	 * Sets the arrow pos and rot.
	 *
	 * @param worldPositionOfCursor
	 *            the world position of cursor
	 * @param line
	 *            the line
	 */
	private void setArrowPosAndRot(Vector2f worldPositionOfCursor, ILine line)
	{
		IImage arrow = arrowOne;
		if (line.equals(lineTwo))
		{
			arrow = arrowTwo;
		}
		arrow.setRelativeRotation(-FastMath.atan2(line.getEndPosition().x - line.getStartPosition().x, line.getEndPosition().y - line.getStartPosition().y));
		arrow.setWorldLocation(worldPositionOfCursor);
	}

	/**
	 * Setup arrow content.
	 */
	private void setupArrowContent()
	{
		pointOne = new Vector2f(0, 0);
		try
		{

			arrowTime = true;
			lineMovingTouches.clear();

			IImage okButton = this.stage.getContentFactory().create(IImage.class, "originOne", UUID.randomUUID());
			okButton.setImage(RESOURCE_PATH + "okpoint.png");
			okButton.setSize(ORIGIN_SIZE, ORIGIN_SIZE);
			okButton.getMultiTouchDispatcher().addListener(new MultiTouchEventAdapter()
			{
				@Override
				public void cursorPressed(MultiTouchCursorEvent event)
				{
					okOnArrows();
				}
			});

			okButton.setRelativeLocation(new Vector2f((stage.getDisplayWidth() / 2) - ORIGIN_SIZE - 25, (stage.getDisplayHeight() / 2) - ORIGIN_SIZE - 25));

			pointOne = new Vector2f(stage.getDisplayWidth() / 2, stage.getDisplayHeight() / 2);

			lineOne = this.stage.getContentFactory().create(ILine.class, "lineOne", UUID.randomUUID());
			lineOne.setStartPosition(pointOne);
			lineOne.setLineWidth(LINE_WIDTH / 2);
			lineOne.setLineColour(ColorRGBA.Orange);

			lineOne.getZOrderManager().setAutoBringToTop(false);
			lineOne.getZOrderManager().setBringToTopPropagatesUp(false);

			arrowOne = this.stage.getContentFactory().create(IImage.class, "arrowOne", UUID.randomUUID());
			arrowOne.setImage(RESOURCE_PATH + "orangeArrow.png");
			arrowOne.setSize(ARROW_SIZE * 3, ARROW_SIZE);

			arrowOne.getZOrderManager().setAutoBringToTop(false);
			arrowOne.getZOrderManager().setBringToTopPropagatesUp(false);

			lineTwo = this.stage.getContentFactory().create(ILine.class, "lineTwo", UUID.randomUUID());
			lineTwo.setStartPosition(pointOne);
			lineTwo.setLineWidth(LINE_WIDTH / 2);
			lineTwo.setLineColour(ColorRGBA.Cyan);

			lineTwo.getZOrderManager().setAutoBringToTop(false);
			lineTwo.getZOrderManager().setBringToTopPropagatesUp(false);

			arrowTwo = this.stage.getContentFactory().create(IImage.class, "arrowTwo = this", UUID.randomUUID());
			arrowTwo.setImage(RESOURCE_PATH + "cyanArrow.png");
			arrowTwo.setSize(ARROW_SIZE * 3, ARROW_SIZE);

			arrowTwo.getZOrderManager().setAutoBringToTop(false);
			arrowTwo.getZOrderManager().setBringToTopPropagatesUp(false);

			stage.addItem(lineOne);
			stage.addItem(lineTwo);

			lineOne.setEndPosition(new Vector2f(stage.getDisplayWidth() / 2, (stage.getDisplayHeight() / 2) + (INITIAL_ARROW_OFFSET / 2)));
			lineTwo.setEndPosition(new Vector2f(stage.getDisplayWidth() / 2, (stage.getDisplayHeight() / 2) - (INITIAL_ARROW_OFFSET / 2)));

			localNorth = this.stage.getContentFactory().create(ILine.class, "localNorth", UUID.randomUUID());
			localNorth.setStartPosition(pointOne);
			localNorth.setLineWidth(0f);
			localNorth.setLineColour(ColorRGBA.Black);
			localNorth.setVisible(false);
			localNorth.setInteractionEnabled(false);
			stage.addItem(localNorth);
			localNorth.setEndPosition(new Vector2f(stage.getDisplayWidth() / 2, (stage.getDisplayWidth() / 2) + 100f));

			worldSouth = this.stage.getContentFactory().create(ILine.class, "worldSouth", UUID.randomUUID());
			worldSouth.setStartPosition(pointOne);
			worldSouth.setLineWidth(0f);
			worldSouth.setLineColour(ColorRGBA.Black);
			worldSouth.setVisible(false);
			worldSouth.setInteractionEnabled(false);

			Vector2f southRep = new Vector2f(0, -100f);
			southRep.rotateAroundOrigin(-tableOArray[currentLoop], true);
			worldSouth.setStartPosition(new Vector2f());
			worldSouth.setEndPosition(new Vector2f(southRep.x, southRep.y));

			stage.addItem(worldSouth);

			stage.addItem(arrowOne);
			stage.addItem(arrowTwo);

			arrowOne.setWorldLocation(new Vector2f(stage.getDisplayWidth() / 2, (stage.getDisplayHeight() / 2) + SECONDARY_ARROW_OFFSET));
			arrowTwo.setWorldLocation(new Vector2f(stage.getDisplayWidth() / 2, (stage.getDisplayHeight() / 2) - SECONDARY_ARROW_OFFSET));

			arrowTwo.setRelativeRotation(FastMath.DEG_TO_RAD * 180);

			TablePosUtilities.generatePrompt("Drag the arrows to point to the landmarks of the same colour.", stage, currentLoop, loops);
			TablePosUtilities.addPivot(stage, ORIGIN_SIZE);

			stage.addItem(okButton);

		}
		catch (ContentTypeNotBoundException e)
		{
			log.log(Level.SEVERE, "Content Type Not Bound", e);
		}

	}

	/**
	 * Setup initial content.
	 */
	private void setupInitialContent()
	{
		try
		{

			positioningTouchCount[currentLoop] = 0;
			orientationTouchCount[currentLoop] = 0;

			stage.removeAllItems(true);

			background = this.stage.getContentFactory().create(IColourRectangle.class, "background", UUID.randomUUID());
			background.setSize(stage.getDisplayWidth(), stage.getDisplayHeight());
			background.setRelativeLocation(new Vector2f());
			background.getZOrderManager().setAutoBringToTop(false);
			background.getZOrderManager().setBringToTopPropagatesUp(false);
			background.setSolidBackgroundColour(ColorRGBA.Black);

			stage.addItem(background);

			IImage okButton = this.stage.getContentFactory().create(IImage.class, "okButton", UUID.randomUUID());
			okButton.setImage(RESOURCE_PATH + "okpoint.png");
			okButton.setSize(ORIGIN_SIZE, ORIGIN_SIZE);
			okButton.getMultiTouchDispatcher().addListener(new MultiTouchEventAdapter()
			{
				@Override
				public void cursorPressed(MultiTouchCursorEvent event)
				{
					orientationTouchCount[currentLoop]++;
					stage.removeAllItems(true);
					tableOArray[currentLoop] = container.getRelativeRotation();
					log.info("tableO," + (FastMath.RAD_TO_DEG * tableO) + ", degrees");
					changeOver[currentLoop] = new Date();
					setupArrowContent();
				}
			});
			okButton.setRelativeLocation(new Vector2f((-stage.getDisplayWidth() / 2) + ORIGIN_SIZE + 25, (stage.getDisplayHeight() / 2) - ORIGIN_SIZE - 25));

			try
			{
				container = stage.getContentFactory().create(IContainer.class, "rotArrowContainer", UUID.randomUUID());

				ILine lineA = this.stage.getContentFactory().create(ILine.class, "lineA", UUID.randomUUID());
				lineA.setStartPosition(new Vector2f(0, 0));
				lineA.setLineWidth(LINE_WIDTH * 2);
				lineA.setLineColour(ColorRGBA.White);
				lineA.setEndPosition(new Vector2f(0, INITIAL_ARROW_OFFSET));

				IImage labelA = this.stage.getContentFactory().create(IImage.class, "arrowOne", UUID.randomUUID());
				labelA.setImage(RESOURCE_PATH + "orangeLabel.png");
				labelA.setSize(ARROW_SIZE * 3, ARROW_SIZE);
				labelA.setRelativeLocation(new Vector2f(0, SECONDARY_ARROW_OFFSET * 1.5f));

				IColourRectangle orangeBox = stage.getContentFactory().create(IColourRectangle.class, "orangeBox", UUID.randomUUID());
				orangeBox.setSolidBackgroundColour(ColorRGBA.Orange);
				orangeBox.setSize(stage.getDisplayWidth() * 2, stage.getDisplayHeight());
				orangeBox.setRelativeLocation(new Vector2f(0, stage.getDisplayHeight() - 20));

				ILine lineB = this.stage.getContentFactory().create(ILine.class, "lineB", UUID.randomUUID());
				lineB.setStartPosition(new Vector2f(0, 0));
				lineB.setLineWidth(LINE_WIDTH * 2);
				lineB.setLineColour(ColorRGBA.White);
				lineB.setEndPosition(new Vector2f(0, -INITIAL_ARROW_OFFSET));

				IImage labelB = this.stage.getContentFactory().create(IImage.class, "arrowTwo = this", UUID.randomUUID());
				labelB.setImage(RESOURCE_PATH + "cyanLabel.png");
				labelB.setSize(ARROW_SIZE * 3, ARROW_SIZE);
				labelB.setRelativeLocation(new Vector2f(0, -SECONDARY_ARROW_OFFSET * 1.5f));
				labelB.setRelativeRotation(FastMath.DEG_TO_RAD * 180);

				IColourRectangle blueBox = stage.getContentFactory().create(IColourRectangle.class, "blueBox", UUID.randomUUID());
				blueBox.setSolidBackgroundColour(ColorRGBA.Cyan);
				blueBox.setSize(stage.getDisplayWidth() * 2, stage.getDisplayHeight());
				blueBox.setRelativeLocation(new Vector2f(0, -stage.getDisplayHeight() + 20));

				container.addItem(orangeBox);
				container.addItem(lineA);
				container.addItem(labelA);
				container.addItem(blueBox);
				container.addItem(lineB);
				container.addItem(labelB);

				MultiTouchEventAdapter rotateEvent = new MultiTouchEventAdapter()
				{
					private HashMap<Long, Float> angles = new HashMap<Long, Float>();
					private float dragAngle = 0;
					private Vector2f origin = new Vector2f(stage.getDisplayWidth() / 2, stage.getDisplayHeight() / 2);
					private HashMap<Long, Vector2f> touchDowns = new HashMap<Long, Vector2f>();

					@Override
					public void cursorChanged(MultiTouchCursorEvent event)
					{
						if (angles.containsKey(event.getCursorID()))
						{

							Vector2f worldPositionOfCursor = new Vector2f();
							stage.tableToWorld(event.getPosition(), worldPositionOfCursor);

							Vector2f touchDrag = new Vector2f(worldPositionOfCursor.x, worldPositionOfCursor.y);

							float angle = new Vector2f(touchDrag.x - origin.x, touchDrag.y - origin.y).angleBetween(new Vector2f(touchDowns.get(event.getCursorID()).x - origin.x, touchDowns.get(event.getCursorID()).y - origin.y));

							dragAngle += angles.get(event.getCursorID()) - angle;
							dragAngle %= (FastMath.DEG_TO_RAD * 360);

							container.setRelativeRotation(dragAngle);

							angles.put(event.getCursorID(), angle);
						}
					}

					@Override
					public void cursorPressed(MultiTouchCursorEvent event)
					{

						orientationTouchCount[currentLoop]++;

						Vector2f worldPositionOfCursor = new Vector2f();
						stage.tableToWorld(event.getPosition(), worldPositionOfCursor);

						angles.put(event.getCursorID(), 0f);
						touchDowns.put(event.getCursorID(), new Vector2f(worldPositionOfCursor.x, worldPositionOfCursor.y));
					}

					@Override
					public void cursorReleased(MultiTouchCursorEvent event)
					{
						if (angles.containsKey(event.getCursorID()))
						{
							touchDowns.remove(event.getCursorID());
							angles.remove(event.getCursorID());
						}
					}

				};

				container.getMultiTouchDispatcher().addListener(rotateEvent);
				orangeBox.getMultiTouchDispatcher().addListener(rotateEvent);
				blueBox.getMultiTouchDispatcher().addListener(rotateEvent);
				lineA.getMultiTouchDispatcher().addListener(rotateEvent);
				lineB.getMultiTouchDispatcher().addListener(rotateEvent);
				labelA.getMultiTouchDispatcher().addListener(rotateEvent);
				labelB.getMultiTouchDispatcher().addListener(rotateEvent);

				background.getMultiTouchDispatcher().addListener(rotateEvent);

			}
			catch (ContentTypeNotBoundException e)
			{
			}

			stage.addItem(container);
			stage.addItem(okButton);

			container.getZOrderManager().setAutoBringToTop(false);
			container.getZOrderManager().setBringToTopPropagatesUp(false);

			TablePosUtilities.addPivot(stage, ORIGIN_SIZE);

			TablePosUtilities.generatePrompt("Rotate the line to run parallel to the wall between the landmarks.", stage, currentLoop, loops);

		}
		catch (ContentTypeNotBoundException e)
		{
			log.log(Level.SEVERE, "Content Type Not Bound", e);
		}
	}

}
