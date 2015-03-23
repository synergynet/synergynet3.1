package synergynet3.web.appsystem.client.service;

import java.util.ArrayList;
import java.util.List;

import synergynet3.web.shared.ClassRoom;
import synergynet3.web.shared.Student;
import synergynet3.web.shared.messages.PerformActionMessage;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The Interface SynergyNetAppSystemService.
 */
@RemoteServiceRelativePath("SynergyNetAppSystemService")
public interface SynergyNetAppSystemService extends RemoteService {

	/**
	 * The Class Util.
	 */
	public static class Util {

		/** The instance. */
		private static SynergyNetAppSystemServiceAsync instance;

		/**
		 * Gets the.
		 *
		 * @return the synergy net app system service async
		 */
		public static SynergyNetAppSystemServiceAsync get() {
			if (instance == null) {
				instance = GWT.create(SynergyNetAppSystemService.class);
			}
			return instance;
		}
	}

	/**
	 * Adds the class room.
	 *
	 * @param classroom the classroom
	 */
	public void addClassRoom(ClassRoom classroom);

	/**
	 * Align.
	 *
	 * @param message the message
	 * @param deviceToSendTo the device to send to
	 */
	public void align(PerformActionMessage message, String[] deviceToSendTo);

	/**
	 * Bring students to top.
	 *
	 * @param message the message
	 * @param deviceToSendTo the device to send to
	 */
	public void bringStudentsToTop(PerformActionMessage message,
			String[] deviceToSendTo);

	/**
	 * Change student class.
	 *
	 * @param ID the id
	 * @param newClass the new class
	 */
	public void changeStudentClass(String ID, String newClass);

	// Projector control functions
	/**
	 * Clear projector contents.
	 *
	 * @param message the message
	 * @param deviceToSendTo the device to send to
	 */
	public void clearProjectorContents(PerformActionMessage message,
			String[] deviceToSendTo);

	/**
	 * Creates the student.
	 *
	 * @param student the student
	 */
	public void createStudent(Student student);

	/**
	 * Devices should open application.
	 *
	 * @param className the class name
	 * @param devices the devices
	 */
	public void devicesShouldOpenApplication(String className,
			List<String> devices);

	// Class selection functions
	/**
	 * Gets the current classes.
	 *
	 * @return the current classes
	 */
	public ArrayList<ClassRoom> getCurrentClasses();

	/**
	 * Gets the students from class.
	 *
	 * @param classSelected the class selected
	 * @return the students from class
	 */
	public ArrayList<Student> getStudentsFromClass(String classSelected);

	/**
	 * Modify student.
	 *
	 * @param student the student
	 */
	public void modifyStudent(Student student);

	/**
	 * Reload removable drive contents.
	 *
	 * @param message the message
	 * @param deviceToSendTo the device to send to
	 */
	public void reloadRemovableDriveContents(PerformActionMessage message,
			String[] deviceToSendTo);

	/**
	 * Reload server contents.
	 *
	 * @param message the message
	 * @param deviceToSendTo the device to send to
	 */
	public void reloadServerContents(PerformActionMessage message,
			String[] deviceToSendTo);

	/**
	 * Removes the additional content.
	 *
	 * @param message the message
	 * @param deviceToSendTo the device to send to
	 */
	public void removeAdditionalContent(PerformActionMessage message,
			String[] deviceToSendTo);

	/**
	 * Removes the class room.
	 *
	 * @param classroom the classroom
	 */
	public void removeClassRoom(ClassRoom classroom);

	/**
	 * Removes the student.
	 *
	 * @param ID the id
	 */
	public void removeStudent(String ID);

	/**
	 * Removes the student from table.
	 *
	 * @param ID the id
	 * @param table the table
	 */
	public void removeStudentFromTable(String ID, String table);

	/**
	 * Removes the students of class.
	 *
	 * @param name the name
	 */
	public void removeStudentsOfClass(String name);

	/**
	 * Send contents to projector.
	 *
	 * @param projectorsToSendTo the projectors to send to
	 * @param tablesToSendTo the tables to send to
	 */
	public void sendContentsToProjector(String[] projectorsToSendTo,
			String[] tablesToSendTo);

	/**
	 * Send projected contents to table.
	 *
	 * @param tablesToSendTo the tables to send to
	 * @param projectorsToSendTo the projectors to send to
	 */
	public void sendProjectedContentsToTable(String[] tablesToSendTo,
			String[] projectorsToSendTo);

	/**
	 * Send screenshots to projector.
	 *
	 * @param projectorsToSendTo the projectors to send to
	 * @param tablesToSendTo the tables to send to
	 */
	public void sendScreenshotsToProjector(String[] projectorsToSendTo,
			String[] tablesToSendTo);

	// Student control functions
	/**
	 * Send student to table.
	 *
	 * @param ID the id
	 * @param deviceSelected the device selected
	 */
	public void sendStudentToTable(String ID, String deviceSelected);

	// Media control functions
	/**
	 * Sets the network flick.
	 *
	 * @param message the message
	 * @param deviceToSendTo the device to send to
	 */
	public void setNetworkFlick(PerformActionMessage message,
			String[] deviceToSendTo);

	// Database control functions
	/**
	 * Start database.
	 */
	public void startDatabase();

	/**
	 * Stop database.
	 */
	public void stopDatabase();

	/**
	 * Take screenshot.
	 *
	 * @param message the message
	 * @param deviceToSendTo the device to send to
	 */
	public void takeScreenshot(PerformActionMessage message,
			String[] deviceToSendTo);

	/**
	 * Toggle freeze.
	 *
	 * @param message the message
	 * @param deviceToSendTo the device to send to
	 */
	public void toggleFreeze(PerformActionMessage message,
			String[] deviceToSendTo);

}