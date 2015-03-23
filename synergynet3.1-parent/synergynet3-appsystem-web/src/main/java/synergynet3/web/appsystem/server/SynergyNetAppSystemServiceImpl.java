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

/**
 * The Class SynergyNetAppSystemServiceImpl.
 */
public class SynergyNetAppSystemServiceImpl extends RemoteServiceServlet
		implements SynergyNetAppSystemService {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 762348478211327434L;

	/** The classroom list. */
	private ClassroomList classroomList = new ClassroomList();

	/** The database controller. */
	protected DatabaseServer databaseController = new DatabaseServer();

	/*
	 * (non-Javadoc)
	 * @see synergynet3.web.appsystem.client.service.SynergyNetAppSystemService#
	 * addClassRoom(synergynet3.web.shared.ClassRoom)
	 */
	@Override
	public void addClassRoom(ClassRoom classroom) {
		classroomList.addClassroom(classroom.getName());
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynet3.web.appsystem.client.service.SynergyNetAppSystemService#align
	 * (synergynet3.web.shared.messages.PerformActionMessage,
	 * java.lang.String[])
	 */
	@Override
	public void align(PerformActionMessage message, String[] projectors) {
		if (projectors.length < 1) {
			return;
		}
		for (String projector : projectors) {
			if (projector.equals(DevicesSelected.ALL_PROJECTORS_ID)) {
				ProjectorControlComms.get().allProjectorsAlign(message);
			} else {
				ProjectorControlComms.get().specificProjectorAlign(message,
						projector);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.web.appsystem.client.service.SynergyNetAppSystemService#
	 * bringStudentsToTop(synergynet3.web.shared.messages.PerformActionMessage,
	 * java.lang.String[])
	 */
	@Override
	public void bringStudentsToTop(PerformActionMessage message, String[] tables) {
		if (tables.length < 1) {
			return;
		}
		for (String table : tables) {
			if (table.equals(DevicesSelected.ALL_TABLES_ID)) {
				AppSystemControlComms.get()
						.allTablesBringStudentsToTop(message);
			} else {
				AppSystemControlComms.get().specificTableBringStudentsToTop(
						message, table);
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.web.appsystem.client.service.SynergyNetAppSystemService#
	 * changeStudentClass(java.lang.String, java.lang.String)
	 */
	@Override
	public void changeStudentClass(String ID, String newClass) {
		StudentDatabaseServerControl.moveStudent(ID, newClass);
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.web.appsystem.client.service.SynergyNetAppSystemService#
	 * clearProjectorContents
	 * (synergynet3.web.shared.messages.PerformActionMessage,
	 * java.lang.String[])
	 */
	@Override
	public void clearProjectorContents(PerformActionMessage message,
			String[] projectors) {
		if (projectors.length < 1) {
			return;
		}
		for (String projector : projectors) {
			if (projector.equals(DevicesSelected.ALL_PROJECTORS_ID)) {
				ProjectorControlComms.get().allProjectorsClear(message);
			} else {
				ProjectorControlComms.get().specificProjectorClear(message,
						projector);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.web.appsystem.client.service.SynergyNetAppSystemService#
	 * createStudent(synergynet3.web.shared.Student)
	 */
	@Override
	public void createStudent(Student student) {
		StudentDatabaseServerControl.createStudent(student);
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.web.appsystem.client.service.SynergyNetAppSystemService#
	 * devicesShouldOpenApplication(java.lang.String, java.util.List)
	 */
	@Override
	public void devicesShouldOpenApplication(String className,
			List<String> devices) {
		MessagingManager mm = SynergyNetCluster.get().getMessagingManager();
		String[] devicesStringArray = devices.toArray(new String[0]);
		SwitchToApplication message = new SwitchToApplication(className);
		mm.sendMessageToDevices(message, devicesStringArray);
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.web.appsystem.client.service.SynergyNetAppSystemService#
	 * getCurrentClasses()
	 */
	@Override
	public ArrayList<ClassRoom> getCurrentClasses() {
		ArrayList<ClassRoom> classrooms = new ArrayList<ClassRoom>();
		for (String name : classroomList.getClassroomNames()) {
			ClassRoom database = new ClassRoom();
			database.setName(name);
			classrooms.add(database);
		}
		return classrooms;
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.web.appsystem.client.service.SynergyNetAppSystemService#
	 * getStudentsFromClass(java.lang.String)
	 */
	@Override
	public ArrayList<Student> getStudentsFromClass(String className) {
		ArrayList<Student> students = new ArrayList<Student>();
		for (Student student : StudentDatabaseServerControl.getStudents()) {
			if (student.getClassName().equalsIgnoreCase(className)) {
				students.add(student);
			}
		}
		return students;
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.web.appsystem.client.service.SynergyNetAppSystemService#
	 * modifyStudent(synergynet3.web.shared.Student)
	 */
	@Override
	public void modifyStudent(Student student) {
		StudentDatabaseServerControl.modifyStudent(student);
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.web.appsystem.client.service.SynergyNetAppSystemService#
	 * reloadRemovableDriveContents
	 * (synergynet3.web.shared.messages.PerformActionMessage,
	 * java.lang.String[])
	 */
	@Override
	public void reloadRemovableDriveContents(PerformActionMessage message,
			String[] tables) {
		if (tables.length < 1) {
			return;
		}
		for (String table : tables) {
			if (table.equals(DevicesSelected.ALL_TABLES_ID)) {
				AppSystemControlComms.get()
						.allTablesReloadRemovableDriveContents(message);
			} else {
				AppSystemControlComms.get()
						.specificTableReloadRemovableDriveContents(message,
								table);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.web.appsystem.client.service.SynergyNetAppSystemService#
	 * reloadServerContents
	 * (synergynet3.web.shared.messages.PerformActionMessage,
	 * java.lang.String[])
	 */
	@Override
	public void reloadServerContents(PerformActionMessage message,
			String[] tables) {
		if (tables.length < 1) {
			return;
		}
		for (String table : tables) {
			if (table.equals(DevicesSelected.ALL_TABLES_ID)) {
				AppSystemControlComms.get().allTablesReloadServerContents(
						message);
			} else {
				AppSystemControlComms.get().specificTableReloadServerContents(
						message, table);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.web.appsystem.client.service.SynergyNetAppSystemService#
	 * removeAdditionalContent
	 * (synergynet3.web.shared.messages.PerformActionMessage,
	 * java.lang.String[])
	 */
	@Override
	public void removeAdditionalContent(PerformActionMessage message,
			String[] tables) {
		if (tables.length < 1) {
			return;
		}
		for (String table : tables) {
			if (table.equals(DevicesSelected.ALL_TABLES_ID)) {
				AppSystemControlComms.get().allTablesRemoveAdditionalContent(
						message);
			} else {
				AppSystemControlComms.get()
						.specificTableRemoveAdditionalContent(message, table);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.web.appsystem.client.service.SynergyNetAppSystemService#
	 * removeClassRoom(synergynet3.web.shared.ClassRoom)
	 */
	@Override
	public void removeClassRoom(ClassRoom classroom) {
		classroomList.removeClassroom(classroom.getName());
		StudentDatabaseServerControl.removeStudentsOfClass(classroom.getName());
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.web.appsystem.client.service.SynergyNetAppSystemService#
	 * removeStudent(java.lang.String)
	 */
	@Override
	public void removeStudent(String ID) {
		StudentDatabaseServerControl.removeStudent(ID);
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.web.appsystem.client.service.SynergyNetAppSystemService#
	 * removeStudentFromTable(java.lang.String, java.lang.String)
	 */
	@Override
	public void removeStudentFromTable(String ID, String table) {
		AppSystemControlComms.get().removeStudentFromTable(ID, table);
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.web.appsystem.client.service.SynergyNetAppSystemService#
	 * removeStudentsOfClass(java.lang.String)
	 */
	@Override
	public void removeStudentsOfClass(String className) {
		AppSystemControlComms.get().removeStudentsOfClass(className);
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.web.appsystem.client.service.SynergyNetAppSystemService#
	 * sendContentsToProjector(java.lang.String[], java.lang.String[])
	 */
	@Override
	public void sendContentsToProjector(String[] projectorsToSendTo,
			String[] tablesToSendTo) {
		if ((projectorsToSendTo.length < 1) || (tablesToSendTo.length < 1)) {
			return;
		}
		for (String table : tablesToSendTo) {
			if (table.equals(DevicesSelected.ALL_TABLES_ID)) {
				AppSystemControlComms.get().allTablesSendContentsToProjectors(
						projectorsToSendTo);
			} else {
				AppSystemControlComms.get()
						.specificTablesSendContentsToProjectors(
								projectorsToSendTo, table);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.web.appsystem.client.service.SynergyNetAppSystemService#
	 * sendProjectedContentsToTable(java.lang.String[], java.lang.String[])
	 */
	@Override
	public void sendProjectedContentsToTable(String[] tablesToSendTo,
			String[] projectorsToSendTo) {
		if ((projectorsToSendTo.length < 1) || (tablesToSendTo.length < 1)) {
			return;
		}
		for (String projector : projectorsToSendTo) {
			if (projector.equals(DevicesSelected.ALL_PROJECTORS_ID)) {
				ProjectorControlComms.get().allProjectorsSendContentsToTables(
						tablesToSendTo);
			} else {
				ProjectorControlComms.get()
						.specificProjectorsSendContentsToTables(tablesToSendTo,
								projector);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.web.appsystem.client.service.SynergyNetAppSystemService#
	 * sendScreenshotsToProjector(java.lang.String[], java.lang.String[])
	 */
	@Override
	public void sendScreenshotsToProjector(String[] projectorsToSendTo,
			String[] tablesToSendTo) {
		if ((projectorsToSendTo.length < 1) || (tablesToSendTo.length < 1)) {
			return;
		}
		for (String table : tablesToSendTo) {
			if (table.equals(DevicesSelected.ALL_TABLES_ID)) {
				AppSystemControlComms.get()
						.allTablesSendScreenshotsToProjectors(
								projectorsToSendTo);
			} else {
				AppSystemControlComms.get()
						.specificTablesSendScreenshotsToProjectors(
								projectorsToSendTo, table);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.web.appsystem.client.service.SynergyNetAppSystemService#
	 * sendStudentToTable(java.lang.String, java.lang.String)
	 */
	@Override
	public void sendStudentToTable(String ID, String table) {
		AppSystemControlComms.get().sendStudentToTable(ID, table);
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.web.appsystem.client.service.SynergyNetAppSystemService#
	 * setNetworkFlick(synergynet3.web.shared.messages.PerformActionMessage,
	 * java.lang.String[])
	 */
	@Override
	public void setNetworkFlick(PerformActionMessage message, String[] tables) {
		if (tables.length < 1) {
			return;
		}
		for (String table : tables) {
			if (table.equals(DevicesSelected.ALL_TABLES_ID)) {
				AppSystemControlComms.get().allTablesSetNetworkFlick(message);
			} else {
				AppSystemControlComms.get().specificTableSetNetworkFlick(
						message, table);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.web.appsystem.client.service.SynergyNetAppSystemService#
	 * startDatabase()
	 */
	@Override
	public void startDatabase() {
		databaseController.runServer();
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.web.appsystem.client.service.SynergyNetAppSystemService#
	 * stopDatabase()
	 */
	@Override
	public void stopDatabase() {
		databaseController.close();
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.web.appsystem.client.service.SynergyNetAppSystemService#
	 * takeScreenshot(synergynet3.web.shared.messages.PerformActionMessage,
	 * java.lang.String[])
	 */
	@Override
	public void takeScreenshot(PerformActionMessage message, String[] tables) {
		if (tables.length < 1) {
			return;
		}
		for (String table : tables) {
			if (table.equals(DevicesSelected.ALL_TABLES_ID)) {
				AppSystemControlComms.get().allTablesTakeScreenshot(message);
			} else {
				AppSystemControlComms.get().specificTableTakeScreenshot(
						message, table);
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.web.appsystem.client.service.SynergyNetAppSystemService#
	 * toggleFreeze(synergynet3.web.shared.messages.PerformActionMessage,
	 * java.lang.String[])
	 */
	@Override
	public void toggleFreeze(PerformActionMessage message, String[] tables) {
		if (tables.length < 1) {
			return;
		}
		for (String table : tables) {
			if (table.equals(DevicesSelected.ALL_TABLES_ID)) {
				AppSystemControlComms.get().allTablesFreeze(message);
			} else {
				AppSystemControlComms.get()
						.specificTablesFreeze(message, table);
			}
		}
	}

}
