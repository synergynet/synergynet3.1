package synergynet3.web.appsystem.client.service;

import java.util.ArrayList;
import java.util.List;

import synergynet3.web.shared.ClassRoom;
import synergynet3.web.shared.Student;
import synergynet3.web.shared.messages.PerformActionMessage;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The Interface SynergyNetAppSystemServiceAsync.
 */
public interface SynergyNetAppSystemServiceAsync {

	/**
	 * Adds the class room.
	 *
	 * @param database the database
	 * @param callback the callback
	 */
	void addClassRoom(ClassRoom database, AsyncCallback<Void> callback);

	/**
	 * Align.
	 *
	 * @param message the message
	 * @param deviceToSendTo the device to send to
	 * @param asyncCallback the async callback
	 */
	void align(PerformActionMessage message, String[] deviceToSendTo,
			AsyncCallback<Void> asyncCallback);

	/**
	 * Bring students to top.
	 *
	 * @param message the message
	 * @param deviceToSendTo the device to send to
	 * @param asyncCallback the async callback
	 */
	void bringStudentsToTop(PerformActionMessage message,
			String[] deviceToSendTo, AsyncCallback<Void> asyncCallback);

	/**
	 * Change student class.
	 *
	 * @param ID the id
	 * @param newClass the new class
	 * @param asyncCallback the async callback
	 */
	void changeStudentClass(String ID, String newClass,
			AsyncCallback<Void> asyncCallback);

	/**
	 * Clear projector contents.
	 *
	 * @param message the message
	 * @param deviceToSendTo the device to send to
	 * @param asyncCallback the async callback
	 */
	void clearProjectorContents(PerformActionMessage message,
			String[] deviceToSendTo, AsyncCallback<Void> asyncCallback);

	/**
	 * Creates the student.
	 *
	 * @param student the student
	 * @param callback the callback
	 */
	void createStudent(Student student, AsyncCallback<Void> callback);

	/**
	 * Devices should open application.
	 *
	 * @param className the class name
	 * @param devices the devices
	 * @param callback the callback
	 */
	void devicesShouldOpenApplication(String className, List<String> devices,
			AsyncCallback<Void> callback);

	/**
	 * Gets the current classes.
	 *
	 * @param callback the callback
	 * @return the current classes
	 */
	void getCurrentClasses(AsyncCallback<ArrayList<ClassRoom>> callback);

	/**
	 * Gets the students from class.
	 *
	 * @param classSelected the class selected
	 * @param asyncCallback the async callback
	 * @return the students from class
	 */
	void getStudentsFromClass(String classSelected,
			AsyncCallback<ArrayList<Student>> asyncCallback);

	/**
	 * Modify student.
	 *
	 * @param student the student
	 * @param asyncCallback the async callback
	 */
	void modifyStudent(Student student, AsyncCallback<Void> asyncCallback);

	/**
	 * Reload removable drive contents.
	 *
	 * @param message the message
	 * @param deviceToSendTo the device to send to
	 * @param asyncCallback the async callback
	 */
	void reloadRemovableDriveContents(PerformActionMessage message,
			String[] deviceToSendTo, AsyncCallback<Void> asyncCallback);

	/**
	 * Reload server contents.
	 *
	 * @param message the message
	 * @param deviceToSendTo the device to send to
	 * @param asyncCallback the async callback
	 */
	void reloadServerContents(PerformActionMessage message,
			String[] deviceToSendTo, AsyncCallback<Void> asyncCallback);

	/**
	 * Removes the additional content.
	 *
	 * @param message the message
	 * @param deviceToSendTo the device to send to
	 * @param asyncCallback the async callback
	 */
	void removeAdditionalContent(PerformActionMessage message,
			String[] deviceToSendTo, AsyncCallback<Void> asyncCallback);

	/**
	 * Removes the class room.
	 *
	 * @param database the database
	 * @param callback the callback
	 */
	void removeClassRoom(ClassRoom database, AsyncCallback<Void> callback);

	/**
	 * Removes the student.
	 *
	 * @param ID the id
	 * @param callback the callback
	 */
	void removeStudent(String ID, AsyncCallback<Void> callback);

	/**
	 * Removes the student from table.
	 *
	 * @param ID the id
	 * @param table the table
	 * @param asyncCallback the async callback
	 */
	void removeStudentFromTable(String ID, String table,
			AsyncCallback<Void> asyncCallback);

	/**
	 * Removes the students of class.
	 *
	 * @param name the name
	 * @param asyncCallback the async callback
	 */
	void removeStudentsOfClass(String name, AsyncCallback<Void> asyncCallback);

	/**
	 * Send contents to projector.
	 *
	 * @param projectorsToSendTo the projectors to send to
	 * @param tablesToSendTo the tables to send to
	 * @param asyncCallback the async callback
	 */
	void sendContentsToProjector(String[] projectorsToSendTo,
			String[] tablesToSendTo, AsyncCallback<Void> asyncCallback);

	/**
	 * Send projected contents to table.
	 *
	 * @param tablesToSendTo the tables to send to
	 * @param projectorsToSendTo the projectors to send to
	 * @param asyncCallback the async callback
	 */
	void sendProjectedContentsToTable(String[] tablesToSendTo,
			String[] projectorsToSendTo, AsyncCallback<Void> asyncCallback);

	/**
	 * Send screenshots to projector.
	 *
	 * @param projectorsToSendTo the projectors to send to
	 * @param tablesToSendTo the tables to send to
	 * @param asyncCallback the async callback
	 */
	void sendScreenshotsToProjector(String[] projectorsToSendTo,
			String[] tablesToSendTo, AsyncCallback<Void> asyncCallback);

	/**
	 * Send student to table.
	 *
	 * @param ID the id
	 * @param deviceSelected the device selected
	 * @param asyncCallback the async callback
	 */
	void sendStudentToTable(String ID, String deviceSelected,
			AsyncCallback<Void> asyncCallback);

	/**
	 * Sets the network flick.
	 *
	 * @param message the message
	 * @param deviceToSendTo the device to send to
	 * @param asyncCallback the async callback
	 */
	void setNetworkFlick(PerformActionMessage message, String[] deviceToSendTo,
			AsyncCallback<Void> asyncCallback);

	/**
	 * Start database.
	 *
	 * @param callback the callback
	 */
	void startDatabase(AsyncCallback<Void> callback);

	/**
	 * Stop database.
	 *
	 * @param asyncCallback the async callback
	 */
	void stopDatabase(AsyncCallback<Void> asyncCallback);

	/**
	 * Take screenshot.
	 *
	 * @param message the message
	 * @param deviceToSendTo the device to send to
	 * @param asyncCallback the async callback
	 */
	void takeScreenshot(PerformActionMessage message, String[] deviceToSendTo,
			AsyncCallback<Void> asyncCallback);

	/**
	 * Toggle freeze.
	 *
	 * @param message the message
	 * @param deviceToSendTo the device to send to
	 * @param asyncCallback the async callback
	 */
	void toggleFreeze(PerformActionMessage message, String[] deviceToSendTo,
			AsyncCallback<Void> asyncCallback);

}
