package usertracking;

/**
 * The Class TrackerUtils.
 */
public class TrackerUtils {

	/** The Constant MESSAGE_TOKEN. */
	private final static String MESSAGE_TOKEN = "-#-";

	/** The Constant TEACHER_STATUS_MESSAGE_IS_STUDENT. */
	private final static String TEACHER_STATUS_MESSAGE_IS_STUDENT = "student";

	/** The Constant TEACHER_STATUS_MESSAGE_IS_TEACHER. */
	private final static String TEACHER_STATUS_MESSAGE_IS_TEACHER = "teacher";

	/**
	 * Creates the teacher status message.
	 *
	 * @param id the id
	 * @param tableID the table id
	 * @param isTeacher the is teacher
	 * @return the string
	 */
	public static String createTeacherStatusMessage(int id, String tableID,
			boolean isTeacher) {
		String toReturn = id + MESSAGE_TOKEN;
		if (isTeacher) {
			toReturn += TEACHER_STATUS_MESSAGE_IS_TEACHER + MESSAGE_TOKEN;
		} else {
			toReturn += TEACHER_STATUS_MESSAGE_IS_STUDENT + MESSAGE_TOKEN;
		}
		toReturn += tableID;
		return toReturn;
	}

	/**
	 * Creates the unique id to trackers message.
	 *
	 * @param id the id
	 * @param uniqueID the unique id
	 * @return the string
	 */
	public static String createUniqueIDToTrackersMessage(int id, long uniqueID) {
		return id + MESSAGE_TOKEN + uniqueID;
	}

	/**
	 * Gets the table id from teacher status message.
	 *
	 * @param message the message
	 * @return the table id from teacher status message
	 */
	public static String getTableIdFromTeacherStatusMessage(String message) {
		try {
			return message.split(MESSAGE_TOKEN)[2];
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * Gets the teacher status from teacher status message.
	 *
	 * @param message the message
	 * @return the teacher status from teacher status message
	 */
	public static boolean getTeacherStatusFromTeacherStatusMessage(
			String message) {
		try {
			if (message.split(MESSAGE_TOKEN)[1]
					.equals(TEACHER_STATUS_MESSAGE_IS_TEACHER)) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Gets the unique id from message.
	 *
	 * @param message the message
	 * @return the unique id from message
	 */
	public static int getUniqueIDFromMessage(String message) {
		try {
			return Integer.parseInt(message.split(MESSAGE_TOKEN)[1]);
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * Gets the user id from message.
	 *
	 * @param message the message
	 * @return the user id from message
	 */
	public static int getUserIdFromMessage(String message) {
		try {
			return Integer.parseInt(message.split(MESSAGE_TOKEN)[0]);
		} catch (Exception e) {
			return -1;
		}
	}

}
