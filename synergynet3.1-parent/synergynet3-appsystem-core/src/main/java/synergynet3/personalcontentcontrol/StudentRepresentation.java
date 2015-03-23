package synergynet3.personalcontentcontrol;

import java.util.logging.Logger;

import multiplicity3.csys.items.container.IContainer;
import multiplicity3.csys.items.item.IItem;
import synergynet3.web.shared.ColourManager;
import synergynet3.web.shared.Student;

import com.jme3.math.ColorRGBA;

/**
 * The Class StudentRepresentation.
 */
public class StudentRepresentation {

	/** The gallery. */
	private StudentGallery gallery;

	/** The student. */
	private Student student;

	/** The student colour. */
	private ColorRGBA studentColour;

	/** The wrapper frame. */
	private IContainer wrapperFrame;

	/**
	 * Instantiates a new student representation.
	 *
	 * @param student the student
	 */
	public StudentRepresentation(Student student) {
		Logger log = Logger.getLogger(StudentRepresentation.class.getName());
		initialise(student, log);
	}

	/**
	 * Instantiates a new student representation.
	 *
	 * @param student the student
	 * @param studentID the student id
	 * @param log the log
	 */
	public StudentRepresentation(Student student, String studentID, Logger log) {
		initialise(student, log);
	}

	/**
	 * As item.
	 *
	 * @return the i item
	 */
	public IItem asItem() {
		return wrapperFrame;
	}

	/**
	 * Gets the class name.
	 *
	 * @return the class name
	 */
	public String getClassName() {
		return student.getClassName();
	}

	/**
	 * Gets the gallery.
	 *
	 * @return the gallery
	 */
	public StudentGallery getGallery() {
		return gallery;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return student.getName();
	}

	/**
	 * Gets the student.
	 *
	 * @return the student
	 */
	public Student getStudent() {
		return student;
	}

	/**
	 * Gets the student colour.
	 *
	 * @return the student colour
	 */
	public ColorRGBA getStudentColour() {
		return studentColour;
	}

	/**
	 * Gets the student id.
	 *
	 * @return the student id
	 */
	public String getStudentId() {
		return student.getStudentID();
	}

	/**
	 * Sets the student colour.
	 *
	 * @param studentColour the new student colour
	 */
	public void setStudentColour(ColorRGBA studentColour) {
		this.studentColour = studentColour;
	}

	/**
	 * Generate colour.
	 *
	 * @return the color rgba
	 */
	private ColorRGBA generateColour() {
		float[] colourRGB = ColourManager.getRGBForColour(student.getColour());
		return new ColorRGBA(colourRGB[0], colourRGB[1], colourRGB[2], 1f);
	}

	/**
	 * Initialise.
	 *
	 * @param student the student
	 * @param log the log
	 */
	private void initialise(Student student, Logger log) {
		this.student = student;
		studentColour = generateColour();
		gallery = new StudentGallery(this, log);
	}

}
