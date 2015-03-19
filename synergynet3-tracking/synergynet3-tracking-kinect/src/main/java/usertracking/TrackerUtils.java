package usertracking;

public class TrackerUtils {
	
	private final static String MESSAGE_TOKEN = "-#-";
	private final static String TEACHER_STATUS_MESSAGE_IS_TEACHER = "teacher";
	private final static String TEACHER_STATUS_MESSAGE_IS_STUDENT = "student";
	
	public static String createTeacherStatusMessage(int id, String tableID, boolean isTeacher){
		String toReturn = id + MESSAGE_TOKEN;
		if (isTeacher){
			toReturn += TEACHER_STATUS_MESSAGE_IS_TEACHER + MESSAGE_TOKEN;
		}else{
			toReturn += TEACHER_STATUS_MESSAGE_IS_STUDENT + MESSAGE_TOKEN;
		}
		toReturn += tableID;
		return toReturn;
	}
	
	public static int getUserIdFromMessage(String message){
		try{
			return Integer.parseInt(message.split(MESSAGE_TOKEN)[0]);
		}catch (Exception e){
			return -1;
		}
	}
	
	public static boolean getTeacherStatusFromTeacherStatusMessage(String message){
		try{
			if (message.split(MESSAGE_TOKEN)[1].equals(TEACHER_STATUS_MESSAGE_IS_TEACHER)){
				return true;
			}else{
				return false;
			}
		}catch (Exception e){
			return false;
		}
	}
	
	public static String getTableIdFromTeacherStatusMessage(String message){
		try{
			return message.split(MESSAGE_TOKEN)[2];
		}catch (Exception e){
			return "";
		}
	}
	
	public static String createUniqueIDToTrackersMessage(int id, long uniqueID) {
		return id + MESSAGE_TOKEN + uniqueID;
	}
	
	public static int getUniqueIDFromMessage(String message){
		try{
			return Integer.parseInt(message.split(MESSAGE_TOKEN)[1]);
		}catch (Exception e){
			return 0;
		}
	}

}
