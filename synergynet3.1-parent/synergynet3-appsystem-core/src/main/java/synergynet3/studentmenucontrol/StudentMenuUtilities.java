package synergynet3.studentmenucontrol;

import java.util.ArrayList;
import java.util.logging.Level;

import multiplicity3.csys.factory.ContentTypeNotBoundException;
import multiplicity3.csys.stage.IStage;
import synergynet3.SynergyNetApp;
import synergynet3.additionalUtils.AdditionalSynergyNetUtilities;
import synergynet3.cluster.SynergyNetCluster;
import synergynet3.databasemanagement.DatabaseActivity;
import synergynet3.personalcontentcontrol.StudentRepresentation;

import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;

/**
 * The Class StudentMenuUtilities.
 */
public class StudentMenuUtilities {

	/** Student menus currently present in the environment. */
	public static ArrayList<StudentMenu> studentMenus = new ArrayList<StudentMenu>();

	/** Students currently logged in on the device. */
	public static ArrayList<StudentRepresentation> studentRepresentations = new ArrayList<StudentRepresentation>();

	/**
	 * Brings all student menus and menu icons to the top of the environment.
	 */
	public static void bringAllStudentsToTop(IStage stage) {
		for (StudentMenu menu : studentMenus) {
			stage.getZOrderManager().bringToTop(menu.getRadialMenu());
		}
	}

	/**
	 * Creates a student menu for a given student representation.
	 * 
	 * @param student The student representation to create a student menu for.
	 * @return The student menu item created for the corresponding student.
	 **/
	public static StudentMenu generateStudentMenu(
			StudentRepresentation student, IStage stage, SynergyNetApp app) {
		StudentMenu menu = new StudentMenu(student, stage, null, app);
		try {
			app.modifyMenus(menu);
		} catch (ContentTypeNotBoundException e) {
			AdditionalSynergyNetUtilities.log(Level.SEVERE,
					"Content not Bound", e);
		}
		student.getGallery().setMenu(menu);
		return menu;
	}

	/**
	 * Will log in a student with the supplied ID. When a student is logged in
	 * their representation is retrieved from the database service using their
	 * ID and a menu for the student is created and positioned randomly in the
	 * environment. When a student logs in their personal gallery is also
	 * created and populated using details from their database entry.
	 * 
	 * @param studentID ID of the student to be logged in.
	 * @return The student menu item created for the corresponding student.
	 **/
	public static StudentMenu login(String studentID, IStage stage,
			SynergyNetApp app) {

		for (StudentRepresentation studentRepresentation : studentRepresentations) {
			if (studentRepresentation.getStudentId()
					.equalsIgnoreCase(studentID)) {
				return null;
			}
		}

		int displayWidth = (int) (stage.getWorldLocation().x * 2);
		int displayHeight = (int) (stage.getWorldLocation().y * 2);

		StudentRepresentation studentRepresentation = DatabaseActivity
				.getStudentRepresentationFromDatabase(studentID,
						SynergyNetCluster.get().getXMPPConnection().getHost(),
						stage);
		StudentMenu menu = StudentMenuUtilities.generateStudentMenu(
				studentRepresentation, stage, app);

		menu.getRadialMenu().setRelativeLocation(
				new Vector2f((FastMath.rand.nextFloat() * (displayWidth - 40))
						- ((displayWidth - 40) / 2),
						(FastMath.rand.nextFloat() * (displayHeight - 40))
								- ((displayHeight - 40) / 2)));
		menu.getRadialMenu().setRelativeRotation(
				FastMath.DEG_TO_RAD * (FastMath.rand.nextFloat() * 360f));

		menu.setVisibility(true);

		studentMenus.add(menu);
		studentRepresentations.add(studentRepresentation);

		return menu;
	}

	/**
	 * Logs out a specific student from the environment and removes their menu.
	 * When logging out their details, such as items and feedback stored in
	 * their personal gallery, are updated in the database.
	 * 
	 * @param studentID ID of the student to be logged out.
	 **/
	public static void logout(String studentID, IStage stage) {
		int toRemove = -1;
		for (int i = 0; i < studentRepresentations.size(); i++) {
			if (studentID.equals(studentRepresentations.get(i).getStudent()
					.getStudentID())) {
				toRemove = i;
				break;
			}
		}

		if (toRemove >= 0) {
			try {
				DatabaseActivity.updateStudentRep(
						studentRepresentations.get(toRemove), SynergyNetCluster
								.get().getXMPPConnection().getHost());
				studentRepresentations.get(toRemove).getGallery()
						.removeFrom(stage);
				studentMenus.get(toRemove).removeMenu(stage);
				studentRepresentations.remove(toRemove);
				studentMenus.remove(toRemove);
			} catch (NullPointerException e) {
			}
		}
	}

	/**
	 * Logs out all students in the environment and removes their menus.
	 **/
	public static void logoutAll(IStage stage) {
		for (int i = 0; i < studentRepresentations.size(); i++) {
			try {
				DatabaseActivity.updateStudentRep(
						studentRepresentations.get(i), SynergyNetCluster.get()
								.getXMPPConnection().getHost());
			} catch (NullPointerException e) {
			}
			studentRepresentations.get(i).getGallery().removeFrom(stage);
			studentMenus.get(i).removeMenu(stage);
		}
		studentRepresentations.clear();
		studentMenus.clear();
	}

	/**
	 * Logs out all students belonging to a specific class.
	 * 
	 * @param className Name of the class for which all students belonging to it
	 *            should be logged out.
	 */
	public static void logoutAllOfClass(String className, IStage stage) {
		ArrayList<StudentRepresentation> studentsToRemove = new ArrayList<StudentRepresentation>();
		ArrayList<StudentMenu> menusToRemove = new ArrayList<StudentMenu>();
		for (int i = 0; i < studentRepresentations.size(); i++) {
			try {
				if (studentRepresentations.get(i).getStudent().getClassName()
						.equalsIgnoreCase(className)) {
					DatabaseActivity.updateStudentRep(
							studentRepresentations.get(i), SynergyNetCluster
									.get().getXMPPConnection().getHost());
					studentRepresentations.get(i).getGallery()
							.removeFrom(stage);
					studentMenus.get(i).removeMenu(stage);
					studentsToRemove.add(studentRepresentations.get(i));
					menusToRemove.add(studentMenus.get(i));
				}
			} catch (NullPointerException e) {
			}
		}
		for (StudentRepresentation student : studentsToRemove) {
			studentRepresentations.remove(student);
		}
		for (StudentMenu menu : menusToRemove) {
			studentMenus.remove(menu);
		}
	}

	/**
	 * Sets whether students representations present in the environment can add
	 * content from their personal galleries to the applications.
	 *
	 * @param allowGalleryAdd true = items can be added from personal galleries.
	 *            false = items cannot be added from personal galleries.
	 **/
	public static void setAbilityToAddContentFromGallery(boolean allowGalleryAdd) {
		for (StudentRepresentation studentRepresentation : studentRepresentations) {
			studentRepresentation.getGallery()
					.setAbilityToAddContentFromGallery(allowGalleryAdd);
		}
	}

}
