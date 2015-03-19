package synergynet3.behaviours.networkflick;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.jme3.math.Vector2f;

public class NetworkFlickLogging{
	
	public enum FLICKTYPE{
	    NORMAL, INSTANT, PROPORTIONAL
	}
	
	private static final DecimalFormat df = new DecimalFormat("#.##");
	
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
	private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm ss.SS ");
	
	public static final String LOG_PARSE_TOKEN = "#";
	
	public static Boolean LOGGING_ENABLED = false;
	
	public static int BOUNCE_LIMIT = -1;
	
	public static String BOUNCE_LOG = "Bounce Log, X, Y" + LOG_PARSE_TOKEN;
	
	public static int INACCURATE_BOUNCE_COUNT = 0;
	
	public static int LACK_OF_MOMENTUM_BOUNCE_COUNT = 0;
	
	public static String FLICK_LOG = "Flick Log, X, Y" + LOG_PARSE_TOKEN;
	
	public static int DEPARTURE_COUNT = 0;
	
	public static int ARRIVAL_COUNT = 0;
	
	public static void generateInaccurateBounceMessage(String itemName, Vector2f loc){		
		BOUNCE_LOG += generateDateAndLocationText(loc) + itemName + " bounced due to an inaccurate flick." + LOG_PARSE_TOKEN;
		INACCURATE_BOUNCE_COUNT++;
	}
	
	public static void generateLackOfMomentumBounceMessage(String itemName, Vector2f loc){
		BOUNCE_LOG += generateDateAndLocationText(loc) + itemName + " bounced due to a flick with a lack of momentum." + LOG_PARSE_TOKEN;
		LACK_OF_MOMENTUM_BOUNCE_COUNT++;
	}	
	
	public static void generateFlickSentMessage(String itemName, String to, Vector2f loc){
		FLICK_LOG += generateDateAndLocationText(loc) + itemName + " sent to " + to + "." + LOG_PARSE_TOKEN;
		DEPARTURE_COUNT++;
	}
	
	public static void generateFlickArrivalMessage(String itemName, String from, Vector2f loc){
		FLICK_LOG += generateDateAndLocationText(loc) + itemName + " arrived from " + from + "." + LOG_PARSE_TOKEN;
		ARRIVAL_COUNT++;
	}
	
	public static void generateRemovalDueToInaccurateBounceMessage(String itemName, Vector2f loc){
		BOUNCE_LOG += generateDateAndLocationText(loc) + itemName + " removed due to too many inaccurate flicks." + LOG_PARSE_TOKEN;
		ARRIVAL_COUNT++;
	}
	
	public static void generateRemovalDueToLackOfMomentumBounceMessage(String itemName, Vector2f loc){
		BOUNCE_LOG += generateDateAndLocationText(loc) + itemName + " removed due to too many flicks with a lack of momentum." + LOG_PARSE_TOKEN;
		ARRIVAL_COUNT++;
	}
	
	private static String generateDateAndLocationText(Vector2f loc){
		Date date = new Date();
		String timing = DATE_FORMAT.format(date) + " " + TIME_FORMAT.format(date);
		String location = df.format(loc.x) + ", " + df.format(loc.y);
		return timing + ", " + location + ", ";
	}	
}
