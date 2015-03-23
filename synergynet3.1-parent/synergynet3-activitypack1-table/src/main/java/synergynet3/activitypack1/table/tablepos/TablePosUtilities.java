package synergynet3.activitypack1.table.tablepos;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import multiplicity3.config.position.PositionConfigPrefsItem;
import multiplicity3.csys.factory.ContentTypeNotBoundException;
import multiplicity3.csys.items.image.IImage;
import multiplicity3.csys.items.line.ILine;
import multiplicity3.csys.items.mutablelabel.IMutableLabel;
import multiplicity3.csys.stage.IStage;
import multiplicity3.input.MultiTouchEventAdapter;
import multiplicity3.input.events.MultiTouchCursorEvent;
import synergynet3.config.web.WebConfigPrefsItem;

import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;

/**
 * The Class TablePosUtilities.
 */
public class TablePosUtilities {

	/** The Constant DATE_FORMAT. */
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
			"dd/MM/yyyy");

	/** The Constant df. */
	private static final DecimalFormat df = new DecimalFormat("#.##");

	/** The Constant EXECUTION_TOKEN. */
	private static final String EXECUTION_TOKEN = "|||";

	/** The Constant FONT_LOC. */
	private static final String FONT_LOC = "synergynet3/activitypack1/table/common/arial64_red.fnt";

	/** The Constant log. */
	private static final Logger log = Logger.getLogger(TablePosUtilities.class
			.getName());

	/** The Constant LOG_NAME_FORMAT. */
	private static final SimpleDateFormat LOG_NAME_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd_HH-mm-ss");

	/** The Constant RESOURCE_PATH. */
	private static final String RESOURCE_PATH = "synergynet3/activitypack1/table/tablepos/";

	/** The Constant SECTION_TOKEN. */
	private static final String SECTION_TOKEN = "|||||";

	/** The Constant TIME_FORMAT. */
	private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat(
			"HH:mm ss.SS");

	/**
	 * Adds the pivot.
	 *
	 * @param stage the stage
	 * @param originSize the origin size
	 * @throws ContentTypeNotBoundException the content type not bound exception
	 */
	public static void addPivot(IStage stage, float originSize)
			throws ContentTypeNotBoundException {
		IImage pivot = stage.getContentFactory().create(IImage.class,
				"originOne", UUID.randomUUID());
		pivot.setImage(RESOURCE_PATH + "pivot.png");
		pivot.setSize(originSize / 2, originSize / 2);
		pivot.setRelativeLocation(new Vector2f());
		stage.addItem(pivot);
		pivot.getZOrderManager().setAutoBringToTop(false);
	}

	/**
	 * Angle between lines.
	 *
	 * @param firstLine the first line
	 * @param secondLine the second line
	 * @return the float
	 */
	public static float angleBetweenLines(ILine firstLine, ILine secondLine) {
		float x1 = firstLine.getEndPosition().x
				- firstLine.getStartPosition().x;
		float y1 = firstLine.getEndPosition().y
				- firstLine.getStartPosition().y;
		float x2 = secondLine.getEndPosition().x
				- secondLine.getStartPosition().x;
		float y2 = secondLine.getEndPosition().y
				- secondLine.getStartPosition().y;
		return -FastMath.atan2((x1 * y2) - (y1 * x2), (x1 * x2) + (y1 * y2));
	}

	/**
	 * Display results.
	 *
	 * @param stage the stage
	 * @param tableX the table x
	 * @param tableY the table y
	 * @param tableO the table o
	 * @param app the app
	 */
	public static void displayResults(IStage stage, float tableX, float tableY,
			float tableO, final TablePosApp app) {

		stage.removeAllItems(true);
		try {

			IMutableLabel positionLabel = stage.getContentFactory().create(
					IMutableLabel.class, "positionLabel", UUID.randomUUID());
			positionLabel.setFont(FONT_LOC);
			positionLabel.setText("Table Coordinates = ("
					+ new DecimalFormat("0.##").format(tableX) + "m, "
					+ new DecimalFormat("0.##").format(tableY) + "m)");
			positionLabel.setFontScale(1f);
			positionLabel.setRelativeLocation(new Vector2f(125, stage
					.getDisplayHeight() / 6));
			positionLabel.setInteractionEnabled(false);

			IMutableLabel orientationLabel = stage.getContentFactory().create(
					IMutableLabel.class, "orientationLabel", UUID.randomUUID());
			orientationLabel.setFont(FONT_LOC);
			orientationLabel.setText("Table Orientation = "
					+ new DecimalFormat("0.##")
							.format((FastMath.RAD_TO_DEG * tableO))
					+ " degrees");
			orientationLabel.setFontScale(1f);
			orientationLabel.setRelativeLocation(new Vector2f(125, -stage
					.getDisplayHeight() / 6));
			orientationLabel.setInteractionEnabled(false);

			IImage backButton = stage.getContentFactory().create(IImage.class,
					"okButton", UUID.randomUUID());
			backButton.setImage(RESOURCE_PATH + "back.png");
			backButton.setSize(100, 36);
			backButton.setRelativeLocation(new Vector2f((stage
					.getDisplayWidth() / 2) - (backButton.getWidth() / 2),
					(stage.getDisplayHeight() / 2)
							- (backButton.getHeight() / 2)));
			backButton.getMultiTouchDispatcher().addListener(
					new MultiTouchEventAdapter() {
						@Override
						public void cursorPressed(MultiTouchCursorEvent event) {
							app.freshStart();
						}
					});

			PositionConfigPrefsItem prefs = new PositionConfigPrefsItem();
			prefs.setXPos(tableX);
			prefs.setYPos(tableY);
			prefs.setAngle(FastMath.RAD_TO_DEG * tableO);

			stage.addItem(positionLabel);
			stage.addItem(orientationLabel);
			stage.addItem(backButton);
		} catch (ContentTypeNotBoundException e) {
			log.log(Level.SEVERE, "Content Type Not Bound", e);
		}
	}

	/**
	 * Generate prompt.
	 *
	 * @param message the message
	 * @param stage the stage
	 * @param currentLoop the current loop
	 * @param loops the loops
	 * @throws ContentTypeNotBoundException the content type not bound exception
	 */
	public static void generatePrompt(String message, IStage stage,
			int currentLoop, int loops) throws ContentTypeNotBoundException {

		IMutableLabel promptLabel = stage.getContentFactory().create(
				IMutableLabel.class, "positionLabel", UUID.randomUUID());
		promptLabel.setFont(FONT_LOC);
		promptLabel.setText(message);
		promptLabel.setBoxSize((stage.getDisplayWidth()), 50);
		promptLabel.setFontScale(0.75f);
		promptLabel.setRelativeLocation(new Vector2f(0, (-stage
				.getDisplayHeight() / 2) + 50));
		promptLabel.setInteractionEnabled(false);

		stage.addItem(promptLabel);
		promptLabel.getZOrderManager().setAutoBringToTop(true);
		promptLabel.getZOrderManager().setBringToTopPropagatesUp(false);

		IMutableLabel loopLabel = stage.getContentFactory().create(
				IMutableLabel.class, "positionLabel", UUID.randomUUID());
		loopLabel.setFont(FONT_LOC);
		loopLabel.setText("Executions remaining: " + (loops - currentLoop));
		loopLabel.setBoxSize((stage.getDisplayWidth()), 50);
		loopLabel.setFontScale(0.75f);
		loopLabel.setRelativeLocation(new Vector2f(0,
				(stage.getDisplayHeight() / 2) - 50));
		loopLabel.setInteractionEnabled(false);

		stage.addItem(loopLabel);
		loopLabel.getZOrderManager().setAutoBringToTop(true);
		loopLabel.getZOrderManager().setBringToTopPropagatesUp(false);
	}

	/**
	 * Gets the average of angles.
	 *
	 * @param range the range
	 * @return the average of angles
	 */
	public static float getAverageOfAngles(float[] range) {
		return getAverageOfAngles(range, range.length);
	}

	/**
	 * Gets the average of angles.
	 *
	 * @param range the range
	 * @param rangeToCheck the range to check
	 * @return the average of angles
	 */
	public static float getAverageOfAngles(float[] range, int rangeToCheck) {

		float[] sinTotals = new float[rangeToCheck];
		float[] cosTotals = new float[rangeToCheck];

		for (int i = 0; i < rangeToCheck; i++) {
			sinTotals[i] = FastMath.sin(standardiseAngle(range[i]));
			cosTotals[i] = FastMath.cos(standardiseAngle(range[i]));
		}

		float sinAverage = getAverageOfLinearValues(sinTotals);
		float cosAverage = getAverageOfLinearValues(cosTotals);

		return ((FastMath.atan2(sinAverage, cosAverage)) + (FastMath.DEG_TO_RAD * 360))
				% (FastMath.DEG_TO_RAD * 360);

	}

	/**
	 * Checks if is within x degrees of.
	 *
	 * @param range the range
	 * @param average the average
	 * @param value the value
	 * @return true, if is within x degrees of
	 */
	public static boolean isWithinXDegreesOf(float range, float average,
			float value) {
		average = standardiseAngle(average);
		value = standardiseAngle(value);

		float lowerLimit = average - range;
		if (lowerLimit >= 0) {
			if ((value >= lowerLimit) && (value <= average)) {
				return true;
			}
		} else {
			if ((value >= 0) && (value <= average)) {
				return true;
			}
			lowerLimit = (FastMath.DEG_TO_RAD * 360) + lowerLimit;
			if ((value >= lowerLimit) && (value <= (FastMath.DEG_TO_RAD * 360))) {
				return true;
			}
		}

		float higherLimit = average + range;
		if (higherLimit <= (FastMath.DEG_TO_RAD * 360)) {
			if ((value >= average) && (value <= higherLimit)) {
				return true;
			}
		} else {
			if ((value >= average) && (value <= (FastMath.DEG_TO_RAD * 360))) {
				return true;
			}
			higherLimit = higherLimit - (FastMath.DEG_TO_RAD * 360);
			if ((value >= 0) && (value <= higherLimit)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Write to log.
	 *
	 * @param logAddress the log address
	 * @param startTime the start time
	 * @param lineToBAngle the line to b angle
	 * @param lineToAAngle the line to a angle
	 * @param S the s
	 * @param T the t
	 * @param R the r
	 * @param A the a
	 * @param B the b
	 * @param d the d
	 * @param actualLineToAAngle the actual line to a angle
	 * @param actualLineToBAngle the actual line to b angle
	 * @param actualS the actual s
	 * @param actualT the actual t
	 * @param actualR the actual r
	 * @param actualA the actual a
	 * @param actualB the actual b
	 * @param actuald the actuald
	 * @param actualTableX the actual table x
	 * @param actualTableY the actual table y
	 * @param actualTableO the actual table o
	 * @param referenceDistance the reference distance
	 * @param loops the loops
	 * @param firstTouch the first touch
	 * @param changeOver the change over
	 * @param lastTouch the last touch
	 * @param orientationTouchCount the orientation touch count
	 * @param positioningTouchCount the positioning touch count
	 * @param lineToAAngleArray the line to a angle array
	 * @param lineToBAngleArray the line to b angle array
	 * @param SArray the s array
	 * @param TArray the t array
	 * @param RArray the r array
	 * @param AArray the a array
	 * @param BArray the b array
	 * @param tableOArray the table o array
	 * @param XArray the x array
	 * @param YArray the y array
	 * @param dArray the d array
	 * @param tableX the table x
	 * @param tableY the table y
	 * @param tableO the table o
	 * @param redoLog the redo log
	 */
	public static void writeToLog(String logAddress, Date startTime,
			float lineToBAngle, float lineToAAngle, float S, float T, float R,
			float A, float B, float d, float actualLineToAAngle,
			float actualLineToBAngle, float actualS, float actualT,
			float actualR, float actualA, float actualB, float actuald,
			float actualTableX, float actualTableY, float actualTableO,
			float referenceDistance, int loops, Date[] firstTouch,
			Date[] changeOver, Date[] lastTouch, int[] orientationTouchCount,
			int[] positioningTouchCount, float[] lineToAAngleArray,
			float[] lineToBAngleArray, float[] SArray, float[] TArray,
			float[] RArray, float[] AArray, float[] BArray,
			float[] tableOArray, float[] XArray, float[] YArray,
			float[] dArray, float tableX, float tableY, float tableO,
			ArrayList<String> redoLog) {

		if (logAddress != null) {
			if (!logAddress.equals("")) {
				String id = new WebConfigPrefsItem().getClusterUserName();
				String address = logAddress + File.separator
						+ LOG_NAME_FORMAT.format(startTime) + "_" + id
						+ "_PositionLog.csv";
				File logFile = new File(address);

				if (!logFile.isFile()) {
					log.info("No log file found, creating new one.");
					try {
						if (!logFile.createNewFile()) {
							log.info("Unable to create new log file.");
							return;
						}
					} catch (IOException e1) {
						log.info("Unable to create new log file.");
						return;
					}
				}
				log.info("Saving data to " + address);
				try {
					FileWriter out = new FileWriter(address, true);
					BufferedWriter writer = new BufferedWriter(out);

					writer.write(makeTitle("TABLE INFO"));
					writer.newLine();
					writer.newLine();

					writer.write("Actual line to A angle, "
							+ df.format(FastMath.RAD_TO_DEG
									* actualLineToAAngle) + ", degrees");
					writer.newLine();
					writer.write("Actual line to B angle, "
							+ df.format(FastMath.RAD_TO_DEG
									* actualLineToBAngle) + ", degrees");
					writer.newLine();
					writer.newLine();

					writer.write("Actual S, "
							+ df.format(FastMath.RAD_TO_DEG * actualS)
							+ ", degrees");
					writer.newLine();
					writer.write("Actual T, "
							+ df.format(FastMath.RAD_TO_DEG * actualT)
							+ ", degrees");
					writer.newLine();
					writer.write("Actual R, "
							+ df.format(FastMath.RAD_TO_DEG * actualR)
							+ ", degrees");
					writer.newLine();
					writer.write("Actual A, "
							+ df.format(FastMath.RAD_TO_DEG * actualA)
							+ ", degrees");
					writer.newLine();
					writer.write("Actual B, "
							+ df.format(FastMath.RAD_TO_DEG * actualB)
							+ ", degrees");
					writer.newLine();
					writer.write("Actual d, " + df.format(actuald) + ", metres");
					writer.newLine();
					writer.newLine();

					writer.write("Actual table X, " + df.format(actualTableX)
							+ ", metres");
					writer.newLine();
					writer.write("Actual table Y, " + df.format(actualTableY)
							+ ", metres");
					writer.newLine();
					writer.write("Actual table orientation, "
							+ df.format(FastMath.RAD_TO_DEG * actualTableO)
							+ ", degrees");
					writer.newLine();
					writer.newLine();

					writer.write("Reference Distance (between landmarks), "
							+ referenceDistance + ", metres");
					writer.newLine();
					writer.newLine();
					writer.newLine();
					writer.newLine();

					writer.write(makeTitle("INTERACTION INFO"));
					writer.newLine();
					writer.newLine();

					writer.write("Table," + id);
					writer.newLine();
					writer.write("Date," + DATE_FORMAT.format(startTime));
					writer.newLine();
					writer.write("Start," + TIME_FORMAT.format(startTime));
					writer.newLine();
					writer.write("End," + TIME_FORMAT.format(new Date()));
					writer.newLine();
					writer.newLine();

					writer.write("Executions," + loops);
					writer.newLine();
					writer.newLine();

					for (int i = 0; i < loops; i++) {
						writer.write(makeExecutionTitle(i));
						writer.newLine();
						writer.write(" First Touch,"
								+ TIME_FORMAT.format(firstTouch[i]));
						writer.newLine();
						writer.write(" Changeover (1st OK),"
								+ TIME_FORMAT.format(changeOver[i]));
						writer.newLine();
						writer.write(" Last Touch (2nd OK),"
								+ TIME_FORMAT.format(lastTouch[i]));
						writer.newLine();
						writer.write(" Number of Touches during Orientation phase,"
								+ orientationTouchCount[i]);
						writer.newLine();
						writer.write(" Number of Touches during Positioning phase,"
								+ positioningTouchCount[i]);
						writer.newLine();
						writer.newLine();
					}

					writer.newLine();
					writer.newLine();

					writer.write(makeTitle("CALCULATED INFO"));
					writer.newLine();
					writer.newLine();

					for (int i = 0; i < loops; i++) {
						writer.write(makeExecutionTitle(i));
						writer.newLine();
						writer.write(" Line to A angle,"
								+ df.format(FastMath.RAD_TO_DEG
										* lineToAAngleArray[i]) + ", degrees");
						writer.newLine();
						writer.write(" Line to B angle,"
								+ df.format(FastMath.RAD_TO_DEG
										* lineToBAngleArray[i]) + ", degrees");
						writer.newLine();
						writer.newLine();

						writer.write(" S,"
								+ df.format(FastMath.RAD_TO_DEG * SArray[i])
								+ ", degrees");
						writer.newLine();
						writer.write(" T,"
								+ df.format(FastMath.RAD_TO_DEG * TArray[i])
								+ ", degrees");
						writer.newLine();
						writer.write(" R,"
								+ df.format(FastMath.RAD_TO_DEG * RArray[i])
								+ ", degrees");
						writer.newLine();
						writer.write(" A,"
								+ df.format(FastMath.RAD_TO_DEG * AArray[i])
								+ ", degrees");
						writer.newLine();
						writer.write(" B,"
								+ df.format(FastMath.RAD_TO_DEG * BArray[i])
								+ ", degrees");
						writer.newLine();
						writer.write(" d," + df.format(d) + ", metres");
						writer.newLine();
						writer.newLine();

						writer.write(" Calculated table X,"
								+ df.format(XArray[i]) + ", metres");
						writer.newLine();
						writer.write(" Calculated table Y,"
								+ df.format(YArray[i]) + ", metres");
						writer.newLine();
						writer.write(" Calculated table orientation,"
								+ df.format(FastMath.RAD_TO_DEG
										* tableOArray[i]) + ", degrees");
						writer.newLine();
						writer.newLine();
					}

					writer.newLine();
					writer.newLine();

					writer.write(makeTitle("DEVIATION INFO"));
					writer.newLine();
					writer.newLine();

					for (int i = 0; i < loops; i++) {
						writer.write(makeExecutionTitle(i));
						writer.newLine();
						writer.write(" Line to A angle deviation,"
								+ getAngleDeviation(lineToAAngleArray[i],
										actualLineToAAngle) + ", degrees");
						writer.newLine();
						writer.write(" Line to B angle deviation,"
								+ getAngleDeviation(lineToBAngleArray[i],
										actualLineToBAngle) + ", degrees");
						writer.newLine();
						writer.newLine();

						writer.write(" S deviation,"
								+ getAngleDeviation(SArray[i], actualS)
								+ ", degrees");
						writer.newLine();
						writer.write(" T deviation,"
								+ getAngleDeviation(TArray[i], actualT)
								+ ", degrees");
						writer.newLine();
						writer.write(" R deviation,"
								+ getAngleDeviation(RArray[i], actualR)
								+ ", degrees");
						writer.newLine();
						writer.write(" A deviation,"
								+ getAngleDeviation(AArray[i], actualA)
								+ ", degrees");
						writer.newLine();
						writer.write(" B deviation,"
								+ getAngleDeviation(BArray[i], actualB)
								+ ", degrees");
						writer.newLine();
						writer.write(" d deviation,"
								+ df.format(dArray[i] - actuald) + ", metres");
						writer.newLine();
						writer.newLine();

						float distance = new Vector2f(XArray[i], YArray[i])
								.distance(new Vector2f(actualTableX,
										actualTableY));

						writer.write(" Total position deviation,"
								+ df.format(distance) + ", metres");
						writer.newLine();
						writer.write(" Table X deviation,"
								+ df.format(XArray[i] - actualTableX)
								+ ", metres");
						writer.newLine();
						writer.write(" Table Y deviation,"
								+ df.format(YArray[i] - actualTableY)
								+ ", metres");
						writer.newLine();
						writer.write(" Table orientation deviation,"
								+ getAngleDeviation(tableOArray[i],
										actualTableO) + ", degrees");
						writer.newLine();
						writer.newLine();
					}

					writer.newLine();
					writer.newLine();

					writer.write(makeTitle("AVERAGE CALCULATED INFO"));
					writer.newLine();
					writer.write("(Using averaged user input.)");
					writer.newLine();
					writer.newLine();

					writer.write("Line to A angle,"
							+ df.format(FastMath.RAD_TO_DEG * lineToAAngle)
							+ ", degrees");
					writer.newLine();
					writer.write("Line to B angle,"
							+ df.format(FastMath.RAD_TO_DEG * lineToBAngle)
							+ ", degrees");
					writer.newLine();
					writer.newLine();

					writer.write("S," + df.format(FastMath.RAD_TO_DEG * S)
							+ ", degrees");
					writer.newLine();
					writer.write("T," + df.format(FastMath.RAD_TO_DEG * T)
							+ ", degrees");
					writer.newLine();
					writer.write("R," + df.format(FastMath.RAD_TO_DEG * R)
							+ ", degrees");
					writer.newLine();
					writer.write("A," + df.format(FastMath.RAD_TO_DEG * A)
							+ ", degrees");
					writer.newLine();
					writer.write("B," + df.format(FastMath.RAD_TO_DEG * B)
							+ ", degrees");
					writer.newLine();
					writer.write("d," + df.format(d) + ", metres");
					writer.newLine();
					writer.newLine();

					writer.write("Calculated table X," + df.format(tableX)
							+ ", metres");
					writer.newLine();
					writer.write("Calculated table Y," + df.format(tableY)
							+ ", metres");
					writer.newLine();
					writer.write("Calculated table orientation,"
							+ df.format(FastMath.RAD_TO_DEG * tableO)
							+ ", degrees");
					writer.newLine();
					writer.newLine();
					writer.newLine();
					writer.newLine();

					writer.write(makeTitle("AVERAGE DEVIATION INFO"));
					writer.newLine();
					writer.write("(Using averaged user input.)");
					writer.newLine();
					writer.newLine();

					writer.write("Line to A angle deviation,"
							+ getAngleDeviation(lineToAAngle,
									actualLineToAAngle) + ", degrees");
					writer.newLine();
					writer.write("Line to B angle deviation ,"
							+ getAngleDeviation(lineToBAngle,
									actualLineToBAngle) + ", degrees");
					writer.newLine();
					writer.newLine();

					writer.write("S deviation," + getAngleDeviation(S, actualS)
							+ ", degrees");
					writer.newLine();
					writer.write("T deviation," + getAngleDeviation(T, actualT)
							+ ", degrees");
					writer.newLine();
					writer.write("R deviation," + getAngleDeviation(R, actualR)
							+ ", degrees");
					writer.newLine();
					writer.write("A deviation," + getAngleDeviation(A, actualA)
							+ ", degrees");
					writer.newLine();
					writer.write("B deviation," + getAngleDeviation(B, actualB)
							+ ", degrees");
					writer.newLine();
					writer.write("d deviation," + df.format(d - actuald)
							+ ", metres");
					writer.newLine();
					writer.newLine();

					float distance = new Vector2f(tableX, tableY)
							.distance(new Vector2f(actualTableX, actualTableY));

					writer.write("Total position deviation,"
							+ df.format(distance) + ", metres");
					writer.newLine();
					writer.write("Table X deviation,"
							+ df.format(tableX - actualTableX) + ", metres");
					writer.newLine();
					writer.write("Table Y deviation,"
							+ df.format(tableY - actualTableY) + ", metres");
					writer.newLine();
					writer.write("Table orientation deviation,"
							+ getAngleDeviation(tableO, actualTableO)
							+ ", degrees");
					writer.newLine();
					writer.newLine();
					writer.newLine();
					writer.newLine();
					writer.newLine();
					writer.newLine();

					writer.write(makeTitle("AVERAGE CALCULATED INFO"));
					writer.newLine();
					writer.write("(Using averaged user results.)");
					writer.newLine();
					writer.newLine();

					writer.write("S,"
							+ df.format(FastMath.RAD_TO_DEG
									* getAverageOfAngles(SArray)) + ", degrees");
					writer.newLine();
					writer.write("T,"
							+ df.format(FastMath.RAD_TO_DEG
									* getAverageOfAngles(TArray)) + ", degrees");
					writer.newLine();
					writer.write("R,"
							+ df.format(FastMath.RAD_TO_DEG
									* getAverageOfAngles(RArray)) + ", degrees");
					writer.newLine();
					writer.write("A,"
							+ df.format(FastMath.RAD_TO_DEG
									* getAverageOfAngles(AArray)) + ", degrees");
					writer.newLine();
					writer.write("B,"
							+ df.format(FastMath.RAD_TO_DEG
									* getAverageOfAngles(BArray)) + ", degrees");
					writer.newLine();
					writer.write("d,"
							+ df.format(getAverageOfLinearValues(dArray))
							+ ", metres");
					writer.newLine();
					writer.newLine();

					writer.write("Calculated table X,"
							+ df.format(getAverageOfLinearValues(XArray))
							+ ", metres");
					writer.newLine();
					writer.write("Calculated table Y,"
							+ df.format(getAverageOfLinearValues(YArray))
							+ ", metres");
					writer.newLine();
					writer.write("Calculated table orientation,"
							+ df.format(FastMath.RAD_TO_DEG
									* getAverageOfAngles(tableOArray))
							+ ", degrees");
					writer.newLine();
					writer.newLine();
					writer.newLine();
					writer.newLine();

					writer.write(makeTitle("AVERAGE DEVIATION INFO"));
					writer.newLine();
					writer.write("(Using averaged user results.)");
					writer.newLine();
					writer.newLine();

					writer.write("S deviation,"
							+ getAngleDeviation(getAverageOfAngles(SArray),
									actualS) + ", degrees");
					writer.newLine();
					writer.write("T deviation,"
							+ getAngleDeviation(getAverageOfAngles(TArray),
									actualT) + ", degrees");
					writer.newLine();
					writer.write("R deviation,"
							+ getAngleDeviation(getAverageOfAngles(RArray),
									actualR) + ", degrees");
					writer.newLine();
					writer.write("A deviation,"
							+ getAngleDeviation(getAverageOfAngles(AArray),
									actualA) + ", degrees");
					writer.newLine();
					writer.write("B deviation,"
							+ getAngleDeviation(getAverageOfAngles(BArray),
									actualB) + ", degrees");
					writer.newLine();
					writer.write("d deviation,"
							+ df.format(getAverageOfLinearValues(dArray)
									- actuald) + ", metres");
					writer.newLine();
					writer.newLine();

					distance = new Vector2f(getAverageOfLinearValues(XArray),
							getAverageOfLinearValues(YArray))
							.distance(new Vector2f(actualTableX, actualTableY));

					writer.write("Total position deviation,"
							+ df.format(distance) + ", metres");
					writer.newLine();
					writer.write("Table X deviation,"
							+ df.format(getAverageOfLinearValues(XArray)
									- actualTableX) + ", metres");
					writer.newLine();
					writer.write("Table Y deviation,"
							+ df.format(getAverageOfLinearValues(YArray)
									- actualTableY) + ", metres");
					writer.newLine();
					writer.write("Table orientation deviation,"
							+ getAngleDeviation(
									getAverageOfAngles(tableOArray),
									actualTableO) + ", degrees");
					writer.newLine();
					writer.newLine();
					writer.newLine();
					writer.newLine();
					writer.newLine();
					writer.newLine();

					writer.write(makeTitle("AVERAGED INPUTS VS AVERAGED RESULTS"));
					writer.newLine();
					writer.newLine();

					writer.write("S deviation,"
							+ getAngleDeviation(S, getAverageOfAngles(SArray))
							+ ", degrees");
					writer.newLine();
					writer.write("T deviation,"
							+ getAngleDeviation(T, getAverageOfAngles(TArray))
							+ ", degrees");
					writer.newLine();
					writer.write("R deviation,"
							+ getAngleDeviation(R, getAverageOfAngles(RArray))
							+ ", degrees");
					writer.newLine();
					writer.write("A deviation,"
							+ getAngleDeviation(A, getAverageOfAngles(AArray))
							+ ", degrees");
					writer.newLine();
					writer.write("B deviation,"
							+ getAngleDeviation(B, getAverageOfAngles(BArray))
							+ ", degrees");
					writer.newLine();
					writer.write("d deviation,"
							+ df.format(d - getAverageOfLinearValues(dArray))
							+ ", metres");
					writer.newLine();
					writer.newLine();

					distance = new Vector2f(getAverageOfLinearValues(XArray),
							getAverageOfLinearValues(YArray))
							.distance(new Vector2f(tableX, tableY));

					writer.write("Total position deviation,"
							+ df.format(distance) + ", metres");
					writer.newLine();
					writer.write("Table X deviation,"
							+ df.format(tableX
									- getAverageOfLinearValues(XArray))
							+ ", metres");
					writer.newLine();
					writer.write("Table Y deviation,"
							+ df.format(tableY
									- getAverageOfLinearValues(YArray))
							+ ", metres");
					writer.newLine();
					writer.write("Table orientation deviation,"
							+ getAngleDeviation(tableO,
									getAverageOfAngles(tableOArray))
							+ ", degrees");
					writer.newLine();
					writer.newLine();
					writer.newLine();
					writer.newLine();
					writer.newLine();
					writer.newLine();

					writer.write(makeTitle("REDO LOG"));
					writer.newLine();
					writer.newLine();

					if (redoLog.size() > 0) {
						writer.write(", Orientation (degrees), Angle to A (degrees), Angle to B (degrees)");
						writer.newLine();
						for (String message : redoLog) {
							writer.write(message);
							writer.newLine();
						}
					} else {
						writer.write("No outlying executions.");
						writer.newLine();
						writer.newLine();
					}

					writer.close();
				} catch (IOException e1) {
					log.info("Unable to write to log file.");
				}
			}
		}
	}

	/**
	 * Gets the angle deviation.
	 *
	 * @param calculated the calculated
	 * @param actual the actual
	 * @return the angle deviation
	 */
	private static String getAngleDeviation(float calculated, float actual) {
		float deviation = calculated - actual;
		deviation %= (FastMath.DEG_TO_RAD * 360);
		if (deviation > (FastMath.DEG_TO_RAD * 180)) {
			deviation = -((FastMath.DEG_TO_RAD * 360) - deviation);
		}
		if (deviation < (FastMath.DEG_TO_RAD * -180)) {
			deviation = (FastMath.DEG_TO_RAD * 360) + deviation;
		}
		return df.format(FastMath.RAD_TO_DEG * deviation);
	}

	/**
	 * Gets the average of linear values.
	 *
	 * @param range the range
	 * @return the average of linear values
	 */
	private static float getAverageOfLinearValues(float[] range) {
		float total = 0;
		for (float f : range) {
			total += f;
		}
		return total / range.length;
	}

	/**
	 * Make execution title.
	 *
	 * @param i the i
	 * @return the string
	 */
	private static String makeExecutionTitle(int i) {
		return EXECUTION_TOKEN + " EXECUTION " + (i + 1) + " "
				+ EXECUTION_TOKEN;
	}

	/**
	 * Make title.
	 *
	 * @param title the title
	 * @return the string
	 */
	private static String makeTitle(String title) {
		return SECTION_TOKEN + " " + title + " " + SECTION_TOKEN;
	}

	/**
	 * Standardise angle.
	 *
	 * @param angle the angle
	 * @return the float
	 */
	private static float standardiseAngle(float angle) {
		while (angle < 0) {
			angle = (FastMath.DEG_TO_RAD * 360) + angle;
		}
		while (angle > (FastMath.DEG_TO_RAD * 360)) {
			angle = angle - (FastMath.DEG_TO_RAD * 360);
		}
		return angle;
	}

}
