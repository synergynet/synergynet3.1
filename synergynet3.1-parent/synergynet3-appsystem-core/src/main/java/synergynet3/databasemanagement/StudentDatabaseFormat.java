package synergynet3.databasemanagement;

import java.util.ArrayList;

/**
 * The Class StudentDatabaseFormat.
 */
public class StudentDatabaseFormat {

	/** The classname. */
	private String classname = "";

	/** The colour. */
	private String colour = "white";

	/** The gallery items. */
	private ArrayList<GalleryItemDatabaseFormat> galleryItems = new ArrayList<GalleryItemDatabaseFormat>();

	/** The name. */
	private String name = "";

	/** The student id. */
	private String studentID = "";

	/**
	 * Instantiates a new student database format.
	 *
	 * @param name the name
	 * @param studentID the student id
	 * @param classname the classname
	 * @param colour the colour
	 */
	public StudentDatabaseFormat(String name, String studentID,
			String classname, String colour) {
		this.name = name;
		this.studentID = studentID;
		this.classname = classname;
		this.colour = colour;
	}

	/**
	 * @param galleryItems the galleryItems to set
	 */
	public void addGalleryItem(GalleryItemDatabaseFormat galleryItem) {
		galleryItems.add(galleryItem);
	}

	/**
	 * Gets the class name.
	 *
	 * @return the class name
	 */
	public String getClassName() {
		return classname;
	}

	/**
	 * @return the colour
	 */
	public String getColour() {
		return colour;
	}

	/**
	 * @return the galleryItems
	 */
	public ArrayList<GalleryItemDatabaseFormat> getGalleryItems() {
		return galleryItems;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the student id.
	 *
	 * @return the student id
	 */
	public String getStudentID() {
		return studentID;
	}

	/**
	 * Sets the classname.
	 *
	 * @param classname the new classname
	 */
	public void setClassname(String classname) {
		this.classname = classname;
	}

	/**
	 * @param colour the colour to set
	 */
	public void setColour(String colour) {
		this.colour = colour;
	}

	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Sets the student id.
	 *
	 * @param studentID the new student id
	 */
	public void setStudentId(String studentID) {
		this.studentID = studentID;
	}

}
