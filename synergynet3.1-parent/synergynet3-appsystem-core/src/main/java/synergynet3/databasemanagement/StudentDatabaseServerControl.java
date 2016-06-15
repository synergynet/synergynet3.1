package synergynet3.databasemanagement;

import java.util.ArrayList;

import synergynet3.cachecontrol.CacheTidy;
import synergynet3.cluster.SynergyNetCluster;
import synergynet3.web.shared.Student;

/**
 * The Class StudentDatabaseServerControl.
 */
public class StudentDatabaseServerControl
{

	/** The Constant HOST_NAME. */
	private static final String HOST_NAME = SynergyNetCluster.get().getXMPPConnection().getHost();

	/**
	 * Creates the student.
	 *
	 * @param student
	 *            the student
	 */
	public static void createStudent(Student student)
	{
		CacheTidy.createStudentCache(student.getStudentID());
		DatabaseActivity.storeStudent(student, HOST_NAME);
	}

	/**
	 * Gets the students.
	 *
	 * @return the students
	 */
	public static Student[] getStudents()
	{
		ArrayList<Student> students = DatabaseActivity.getStudentsFromDatabase(HOST_NAME);
		Student[] studentsToReturn = new Student[students.size()];
		students.toArray(studentsToReturn);
		return studentsToReturn;
	}

	/**
	 * Modify student.
	 *
	 * @param student
	 *            the student
	 */
	public static void modifyStudent(Student student)
	{
		DatabaseActivity.modifyStudent(student, HOST_NAME);
	}

	/**
	 * Move student.
	 *
	 * @param studentID
	 *            the student id
	 * @param newClass
	 *            the new class
	 */
	public static void moveStudent(String studentID, String newClass)
	{
		DatabaseActivity.moveStudent(studentID, newClass, HOST_NAME);
	}

	/**
	 * Removes the student.
	 *
	 * @param studentID
	 *            the student id
	 */
	public static void removeStudent(String studentID)
	{
		CacheTidy.removeStudentCache(studentID);
		DatabaseActivity.deleteStudent(studentID, HOST_NAME);
	}

	/**
	 * Removes the students of class.
	 *
	 * @param className
	 *            the class name
	 */
	public static void removeStudentsOfClass(String className)
	{
		DatabaseActivity.removeStudentsOfClass(className, HOST_NAME);
	}

}
