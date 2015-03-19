package synergynet3.web.appsystem.client.service;

import java.util.ArrayList;
import java.util.List;

import synergynet3.web.shared.ClassRoom;
import synergynet3.web.shared.Student;
import synergynet3.web.shared.messages.PerformActionMessage;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface SynergyNetAppSystemServiceAsync {
	
	void devicesShouldOpenApplication(String className, List<String> devices, AsyncCallback<Void> callback);	
	void sendStudentToTable(String ID, String deviceSelected,	AsyncCallback<Void> asyncCallback);
	void removeStudentFromTable(String ID, String table, AsyncCallback<Void> asyncCallback);
	void startDatabase(AsyncCallback<Void> callback);
	void stopDatabase(AsyncCallback<Void> asyncCallback);
	void getCurrentClasses(AsyncCallback<ArrayList<ClassRoom>> callback);
	void addClassRoom(ClassRoom database, AsyncCallback<Void> callback);
	void removeClassRoom(ClassRoom database, AsyncCallback<Void> callback);
	void modifyStudent(Student student, AsyncCallback<Void> asyncCallback);
	void createStudent(Student student, AsyncCallback<Void> callback);
	void removeStudent(String ID, AsyncCallback<Void> callback);
	void changeStudentClass(String ID, String newClass, AsyncCallback<Void> asyncCallback);
	void getStudentsFromClass(String classSelected, AsyncCallback<ArrayList<Student>> asyncCallback);
	void removeStudentsOfClass(String name, AsyncCallback<Void> asyncCallback);
	void bringStudentsToTop(PerformActionMessage message, String[] deviceToSendTo,	AsyncCallback<Void> asyncCallback);
	void reloadRemovableDriveContents(PerformActionMessage message, String[] deviceToSendTo, AsyncCallback<Void> asyncCallback);
	void reloadServerContents(PerformActionMessage message, String[] deviceToSendTo, AsyncCallback<Void> asyncCallback);
	void takeScreenshot(PerformActionMessage message, String[] deviceToSendTo,	AsyncCallback<Void> asyncCallback);
	void toggleFreeze(PerformActionMessage message, String[] deviceToSendTo, AsyncCallback<Void> asyncCallback);
	void removeAdditionalContent(PerformActionMessage message, String[] deviceToSendTo, AsyncCallback<Void> asyncCallback);
	void setNetworkFlick(PerformActionMessage message, String[] deviceToSendTo, AsyncCallback<Void> asyncCallback);
	void clearProjectorContents(PerformActionMessage message, String[] deviceToSendTo, AsyncCallback<Void> asyncCallback);
	void align(PerformActionMessage message, String[] deviceToSendTo, AsyncCallback<Void> asyncCallback);
	void sendScreenshotsToProjector(String[] projectorsToSendTo, String[] tablesToSendTo, AsyncCallback<Void> asyncCallback);
	void sendContentsToProjector(String[] projectorsToSendTo, String[] tablesToSendTo, AsyncCallback<Void> asyncCallback);
	void sendProjectedContentsToTable(String[] tablesToSendTo, String[] projectorsToSendTo, AsyncCallback<Void> asyncCallback);
	
}
