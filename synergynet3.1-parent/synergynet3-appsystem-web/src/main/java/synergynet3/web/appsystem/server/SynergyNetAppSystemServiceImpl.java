package synergynet3.web.appsystem.server;

import java.util.ArrayList;
import java.util.List;

import synergynet3.cluster.SynergyNetCluster;
import synergynet3.cluster.xmpp.messaging.MessagingManager;
import synergynet3.cluster.xmpp.messaging.appcontrol.SwitchToApplication;
import synergynet3.databasemanagement.StudentDatabaseServerControl;
import synergynet3.projector.web.ProjectorControlComms;
import synergynet3.web.appsystem.client.service.SynergyNetAppSystemService;
import synergynet3.web.appsystem.persistence.ClassroomList;
import synergynet3.web.appsystem.persistence.DatabaseServer;
import synergynet3.web.core.AppSystemControlComms;
import synergynet3.web.shared.ClassRoom;
import synergynet3.web.shared.DevicesSelected;
import synergynet3.web.shared.Student;
import synergynet3.web.shared.messages.PerformActionMessage;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class SynergyNetAppSystemServiceImpl extends RemoteServiceServlet implements SynergyNetAppSystemService {
	private static final long serialVersionUID = 762348478211327434L;
	
	protected DatabaseServer databaseController = new DatabaseServer();
	private ClassroomList classroomList = new ClassroomList();

	@Override
	public void devicesShouldOpenApplication(String className, List<String> devices) {
		MessagingManager mm = SynergyNetCluster.get().getMessagingManager();
		String[] devicesStringArray = (String[]) devices.toArray(new String[0]);
		SwitchToApplication message = new SwitchToApplication(className);
		mm.sendMessageToDevices(message, devicesStringArray);
	}
	
	@Override
	public ArrayList<ClassRoom> getCurrentClasses(){
		ArrayList<ClassRoom> classrooms = new ArrayList<ClassRoom>();
		for (String name: classroomList.getClassroomNames()){
			ClassRoom database = new ClassRoom();
			database.setName(name);
			classrooms.add(database);
		}
		return classrooms;
	}
	
	@Override
	public void addClassRoom(ClassRoom classroom){
		classroomList.addClassroom(classroom.getName());
	} 
	
	@Override
	public void removeClassRoom(ClassRoom classroom){
		classroomList.removeClassroom(classroom.getName());
		StudentDatabaseServerControl.removeStudentsOfClass(classroom.getName());
	}
	
	@Override
	public void startDatabase() {
		databaseController.runServer();
	}
	
	@Override
	public void stopDatabase() {
		databaseController.close();
	}

	@Override
	public void sendStudentToTable(String ID, String table) {
		AppSystemControlComms.get().sendStudentToTable(ID, table);		
	}

	@Override
	public void removeStudentFromTable(String ID, String table) {
		AppSystemControlComms.get().removeStudentFromTable(ID, table);		
	}
	
	@Override
	public ArrayList<Student> getStudentsFromClass(String className){	
		ArrayList<Student> students = new ArrayList<Student>();
		for (Student student: StudentDatabaseServerControl.getStudents()){
			if (student.getClassName().equalsIgnoreCase(className)){
				students.add(student);
			}
		}
		return students;
	}
	
	@Override
	public void createStudent(Student student) {
		StudentDatabaseServerControl.createStudent(student);	
	}

	@Override
	public void modifyStudent(Student student) {
		StudentDatabaseServerControl.modifyStudent(student);	
	}	

	@Override
	public void removeStudent(String ID) {
		StudentDatabaseServerControl.removeStudent(ID);		
	}

	@Override
	public void changeStudentClass(String ID, String newClass) {
		StudentDatabaseServerControl.moveStudent(ID, newClass);
	}

	@Override
	public void removeStudentsOfClass(String className) {
		AppSystemControlComms.get().removeStudentsOfClass(className);
	}
	
	@Override
	public void setNetworkFlick(PerformActionMessage message, String[] tables) {
		if (tables.length < 1)return;
		for (String table: tables){
			if (table.equals(DevicesSelected.ALL_TABLES_ID)){
				AppSystemControlComms.get().allTablesSetNetworkFlick(message);
			}else{
				AppSystemControlComms.get().specificTableSetNetworkFlick(message, table);
			}
		}			
	}

	@Override
	public void reloadServerContents(PerformActionMessage message, String[] tables) {
		if (tables.length < 1)return;
		for (String table: tables){
			if (table.equals(DevicesSelected.ALL_TABLES_ID)){
				AppSystemControlComms.get().allTablesReloadServerContents(message);
			}else{
				AppSystemControlComms.get().specificTableReloadServerContents(message, table);
			}
		}		
	}

	@Override
	public void reloadRemovableDriveContents(PerformActionMessage message, String[] tables) {
		if (tables.length < 1)return;
		for (String table: tables){
			if (table.equals(DevicesSelected.ALL_TABLES_ID)){
				AppSystemControlComms.get().allTablesReloadRemovableDriveContents(message);
			}else{
				AppSystemControlComms.get().specificTableReloadRemovableDriveContents(message, table);
			}
		}	
	}

	@Override
	public void bringStudentsToTop(PerformActionMessage message, String[] tables) {
		if (tables.length < 1)return;
		for (String table: tables){
			if (table.equals(DevicesSelected.ALL_TABLES_ID)){
				AppSystemControlComms.get().allTablesBringStudentsToTop(message);
			}else{
				AppSystemControlComms.get().specificTableBringStudentsToTop(message, table);
			}
		}	
		
	}
	
	@Override
	public void takeScreenshot(PerformActionMessage message, String[] tables) {
		if (tables.length < 1)return;
		for (String table: tables){
			if (table.equals(DevicesSelected.ALL_TABLES_ID)){
				AppSystemControlComms.get().allTablesTakeScreenshot(message);
			}else{
				AppSystemControlComms.get().specificTableTakeScreenshot(message, table);
			}
		}	
		
	}

	@Override
	public void removeAdditionalContent(PerformActionMessage message, String[] tables) {
		if (tables.length < 1)return;
		for (String table: tables){
			if (table.equals(DevicesSelected.ALL_TABLES_ID)){
				AppSystemControlComms.get().allTablesRemoveAdditionalContent(message);
			}else{
				AppSystemControlComms.get().specificTableRemoveAdditionalContent(message, table);
			}
		}			
	}
	
	@Override
	public void toggleFreeze(PerformActionMessage message,	String[] tables) {
		if (tables.length < 1)return;
		for (String table: tables){
			if (table.equals(DevicesSelected.ALL_TABLES_ID)){
				AppSystemControlComms.get().allTablesFreeze(message);
			}else{
				AppSystemControlComms.get().specificTablesFreeze(message, table);
			}
		}			
	}

	@Override
	public void clearProjectorContents(PerformActionMessage message, String[] projectors) {
		if (projectors.length < 1)return;
		for (String projector: projectors){
			if (projector.equals(DevicesSelected.ALL_PROJECTORS_ID)){
				ProjectorControlComms.get().allProjectorsClear(message);
			}else{
				ProjectorControlComms.get().specificProjectorClear(message, projector);
			}
		}	
	}

	@Override
	public void align(PerformActionMessage message, String[] projectors) {
		if (projectors.length < 1)return;
		for (String projector: projectors){
			if (projector.equals(DevicesSelected.ALL_PROJECTORS_ID)){
				ProjectorControlComms.get().allProjectorsAlign(message);
			}else{
				ProjectorControlComms.get().specificProjectorAlign(message, projector);
			}
		}	
	}

	@Override
	public void sendScreenshotsToProjector(String[] projectorsToSendTo,	String[] tablesToSendTo) {
		if (projectorsToSendTo.length < 1 || tablesToSendTo.length < 1)return;
		for (String table: tablesToSendTo){
			if (table.equals(DevicesSelected.ALL_TABLES_ID)){
				AppSystemControlComms.get().allTablesSendScreenshotsToProjectors(projectorsToSendTo);
			}else{
				AppSystemControlComms.get().specificTablesSendScreenshotsToProjectors(projectorsToSendTo, table);
			}
		}
	}

	@Override
	public void sendContentsToProjector(String[] projectorsToSendTo, String[] tablesToSendTo) {
		if (projectorsToSendTo.length < 1 || tablesToSendTo.length < 1)return;
		for (String table: tablesToSendTo){
			if (table.equals(DevicesSelected.ALL_TABLES_ID)){
				AppSystemControlComms.get().allTablesSendContentsToProjectors(projectorsToSendTo);
			}else{
				AppSystemControlComms.get().specificTablesSendContentsToProjectors(projectorsToSendTo, table);
			}
		}
	}

	@Override
	public void sendProjectedContentsToTable(String[] tablesToSendTo, String[] projectorsToSendTo) {
		if (projectorsToSendTo.length < 1 || tablesToSendTo.length < 1)return;
		for (String projector: projectorsToSendTo){
			if (projector.equals(DevicesSelected.ALL_PROJECTORS_ID)){
				ProjectorControlComms.get().allProjectorsSendContentsToTables(tablesToSendTo);
			}else{
				ProjectorControlComms.get().specificProjectorsSendContentsToTables(tablesToSendTo, projector);
			}
		}
	}

}
