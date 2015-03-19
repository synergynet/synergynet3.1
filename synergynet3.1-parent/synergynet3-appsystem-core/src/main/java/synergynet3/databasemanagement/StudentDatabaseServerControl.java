package synergynet3.databasemanagement;

import java.util.ArrayList;

import synergynet3.cachecontrol.CacheTidy;
import synergynet3.cluster.SynergyNetCluster;

import synergynet3.web.shared.Student;

public class StudentDatabaseServerControl {
	
	private static final String HOST_NAME = SynergyNetCluster.get().getXMPPConnection().getHost();

	public static Student[] getStudents() {
		ArrayList<Student> students = DatabaseActivity.getStudentsFromDatabase(HOST_NAME );
		Student[] studentsToReturn = new Student[students.size()];
		students.toArray(studentsToReturn);
		return studentsToReturn;
	}

	public static void createStudent(Student student) {
		CacheTidy.createStudentCache(student.getStudentID());
		DatabaseActivity.storeStudent(student, HOST_NAME );	
	}

	public static void modifyStudent(Student student) {
		DatabaseActivity.modifyStudent(student, HOST_NAME );	
	}

	public static void removeStudent(String studentID) {
		CacheTidy.removeStudentCache(studentID);
		DatabaseActivity.deleteStudent(studentID, HOST_NAME );		
	}

	public static void moveStudent(String studentID, String newClass) {
		DatabaseActivity.moveStudent(studentID, newClass, HOST_NAME );			
	}

	public static void removeStudentsOfClass(String className) {
		DatabaseActivity.removeStudentsOfClass(className, HOST_NAME );			
	}

}
