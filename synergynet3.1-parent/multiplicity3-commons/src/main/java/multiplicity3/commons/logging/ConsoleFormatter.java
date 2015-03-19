package multiplicity3.commons.logging;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;

public class ConsoleFormatter extends Formatter {

	private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
	private boolean showDate = false;
	
	public ConsoleFormatter() {		
	     String showDateProperty = LogManager.getLogManager().getProperty(getClass().getName() + ".showDate");
	     if("true".equals(showDateProperty)) {
	    	 showDate = true;
	    	 String dateFormatProperty = LogManager.getLogManager().getProperty(getClass().getName() + ".dateFormat");
	    	 if(dateFormatProperty != null) {
	    		 dateFormat = new SimpleDateFormat(dateFormatProperty);
	    	 }
	     }
	}
	
	@Override
	public String format(LogRecord record) {
		String logString = "";
		
		if(showDate) logString += dateFormat.format(new Date(record.getMillis())) + " ";
		
		logString += "[" + record.getLevel().getName() + "] ";
		logString += record.getSourceClassName() + "." + record.getSourceMethodName() + " ";
		logString += record.getMessage();
		logString += "\n";
		
		return logString;
	}

}
