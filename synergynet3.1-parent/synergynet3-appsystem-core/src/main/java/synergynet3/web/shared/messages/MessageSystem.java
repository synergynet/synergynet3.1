package synergynet3.web.shared.messages;

import java.util.ArrayList;
import java.util.Date;

public class MessageSystem{	
	
	private static long generatedIDs = 0;
	private static ArrayList<String> registeredIDs = new ArrayList<String>();
	private static String tableID = "";
	
	public static String generateMessageID(){
		if (tableID.equals(""))tableID = "" + new Date().getTime();
		String messageID = tableID + generatedIDs;
		incrementGeneratedMessageIDs();
		return messageID;
	}	
	
	private static void incrementGeneratedMessageIDs(){
		generatedIDs++;
		if (generatedIDs == Long.MAX_VALUE)generatedIDs = 0;
	}
	
	public static boolean messageAlreadyReceived(PerformActionMessage message){
		if (registeredIDs.contains(message.getMessageID())){
			return true;
		}else{
			registeredIDs.add(message.getMessageID());
			return false;
		}
	}
}