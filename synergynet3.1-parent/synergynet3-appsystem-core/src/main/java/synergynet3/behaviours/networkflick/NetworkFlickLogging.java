package synergynet3.behaviours.networkflick;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.jme3.math.Vector2f;

/**
 * The Class NetworkFlickLogging.
 */
public class NetworkFlickLogging
{

	/**
	 * The Enum FLICKTYPE.
	 */
	public enum FLICKTYPE
	{

		/** The instant. */
		INSTANT,
		/** The normal. */
		NORMAL,
		/** The proportional. */
		PROPORTIONAL
	}

	/** The Constant DATE_FORMAT. */
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");

	/** The Constant df. */
	private static final DecimalFormat df = new DecimalFormat("#.##");

	/** The Constant LOG_PARSE_TOKEN. */
	public static final String LOG_PARSE_TOKEN = "#";

	/** The Constant TIME_FORMAT. */
	private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm ss.SS ");

	/** The arrival count. */
	public static int ARRIVAL_COUNT = 0;

	/** The bounce limit. */
	public static int BOUNCE_LIMIT = -1;

	/** The bounce log. */
	public static String BOUNCE_LOG = "Bounce Log, X, Y" + LOG_PARSE_TOKEN;

	/** The departure count. */
	public static int DEPARTURE_COUNT = 0;

	/** The flick log. */
	public static String FLICK_LOG = "Flick Log, X, Y" + LOG_PARSE_TOKEN;

	/** The inaccurate bounce count. */
	public static int INACCURATE_BOUNCE_COUNT = 0;

	/** The lack of momentum bounce count. */
	public static int LACK_OF_MOMENTUM_BOUNCE_COUNT = 0;

	/** The logging enabled. */
	public static Boolean LOGGING_ENABLED = false;

	/**
	 * Generate flick arrival message.
	 *
	 * @param itemName
	 *            the item name
	 * @param from
	 *            the from
	 * @param loc
	 *            the loc
	 */
	public static void generateFlickArrivalMessage(String itemName, String from, Vector2f loc)
	{
		FLICK_LOG += generateDateAndLocationText(loc) + itemName + " arrived from " + from + "." + LOG_PARSE_TOKEN;
		ARRIVAL_COUNT++;
	}

	/**
	 * Generate flick sent message.
	 *
	 * @param itemName
	 *            the item name
	 * @param to
	 *            the to
	 * @param loc
	 *            the loc
	 */
	public static void generateFlickSentMessage(String itemName, String to, Vector2f loc)
	{
		FLICK_LOG += generateDateAndLocationText(loc) + itemName + " sent to " + to + "." + LOG_PARSE_TOKEN;
		DEPARTURE_COUNT++;
	}

	/**
	 * Generate inaccurate bounce message.
	 *
	 * @param itemName
	 *            the item name
	 * @param loc
	 *            the loc
	 */
	public static void generateInaccurateBounceMessage(String itemName, Vector2f loc)
	{
		BOUNCE_LOG += generateDateAndLocationText(loc) + itemName + " bounced due to an inaccurate flick." + LOG_PARSE_TOKEN;
		INACCURATE_BOUNCE_COUNT++;
	}

	/**
	 * Generate lack of momentum bounce message.
	 *
	 * @param itemName
	 *            the item name
	 * @param loc
	 *            the loc
	 */
	public static void generateLackOfMomentumBounceMessage(String itemName, Vector2f loc)
	{
		BOUNCE_LOG += generateDateAndLocationText(loc) + itemName + " bounced due to a flick with a lack of momentum." + LOG_PARSE_TOKEN;
		LACK_OF_MOMENTUM_BOUNCE_COUNT++;
	}

	/**
	 * Generate removal due to inaccurate bounce message.
	 *
	 * @param itemName
	 *            the item name
	 * @param loc
	 *            the loc
	 */
	public static void generateRemovalDueToInaccurateBounceMessage(String itemName, Vector2f loc)
	{
		BOUNCE_LOG += generateDateAndLocationText(loc) + itemName + " removed due to too many inaccurate flicks." + LOG_PARSE_TOKEN;
		ARRIVAL_COUNT++;
	}

	/**
	 * Generate removal due to lack of momentum bounce message.
	 *
	 * @param itemName
	 *            the item name
	 * @param loc
	 *            the loc
	 */
	public static void generateRemovalDueToLackOfMomentumBounceMessage(String itemName, Vector2f loc)
	{
		BOUNCE_LOG += generateDateAndLocationText(loc) + itemName + " removed due to too many flicks with a lack of momentum." + LOG_PARSE_TOKEN;
		ARRIVAL_COUNT++;
	}

	/**
	 * Generate date and location text.
	 *
	 * @param loc
	 *            the loc
	 * @return the string
	 */
	private static String generateDateAndLocationText(Vector2f loc)
	{
		Date date = new Date();
		String timing = DATE_FORMAT.format(date) + " " + TIME_FORMAT.format(date);
		String location = df.format(loc.x) + ", " + df.format(loc.y);
		return timing + ", " + location + ", ";
	}
}
