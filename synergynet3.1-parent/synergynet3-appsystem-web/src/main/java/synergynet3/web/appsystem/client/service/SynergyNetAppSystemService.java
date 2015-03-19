package synergynet3.web.appsystem.client.service;


import java.util.ArrayList;
import java.util.List;

import synergynet3.web.shared.ClassRoom;
import synergynet3.web.shared.Student;
import synergynet3.web.shared.messages.PerformActionMessage;


import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("SynergyNetAppSystemService")
public interface SynergyNetAppSystemService extends RemoteService {
	
	public void devicesShouldOpenApplication(String className, List<String> devices);

	//Student control functions
	public void sendStudentToTable(String ID, String deviceSelected);
	public void removeStudentFromTable(String ID, String table);
	public void modifyStudent(Student student);
	public void createStudent(Student student);
	public void removeStudent(String ID);
	public ArrayList<Student> getStudentsFromClass(String classSelected);
	public void changeStudentClass(String ID, String newClass);
	public void removeStudentsOfClass(String name);
	
	//Database control functions
	public void startDatabase();
	public void stopDatabase();
	
	//Class selection functions
	public ArrayList<ClassRoom> getCurrentClasses();
	public void addClassRoom(ClassRoom classroom);
	public void removeClassRoom(ClassRoom classroom);
	
	//Media control functions
	public void setNetworkFlick(PerformActionMessage message, String[] deviceToSendTo);
	public void reloadServerContents(PerformActionMessage message, String[] deviceToSendTo);
	public void reloadRemovableDriveContents(PerformActionMessage message, String[] deviceToSendTo);
	public void bringStudentsToTop(PerformActionMessage message, String[] deviceToSendTo);
	public void takeScreenshot(PerformActionMessage message, String[] deviceToSendTo);
	public void removeAdditionalContent(PerformActionMessage message, String[] deviceToSendTo);
	public void toggleFreeze(PerformActionMessage message, String[] deviceToSendTo);
	
	//Projector control functions
	public void clearProjectorContents(PerformActionMessage message, String[] deviceToSendTo);
	public void align(PerformActionMessage message, String[] deviceToSendTo);
	public void sendScreenshotsToProjector(String[] projectorsToSendTo, String[] tablesToSendTo);
	public void sendContentsToProjector(String[] projectorsToSendTo, String[] tablesToSendTo);
	public void sendProjectedContentsToTable(String[] tablesToSendTo, String[] projectorsToSendTo);
	
	public static class Util {
		private static SynergyNetAppSystemServiceAsync instance;
		public static SynergyNetAppSystemServiceAsync get(){
			if (instance == null) {
				instance = GWT.create(SynergyNetAppSystemService.class);
			}
			return instance;
		}
	}



}