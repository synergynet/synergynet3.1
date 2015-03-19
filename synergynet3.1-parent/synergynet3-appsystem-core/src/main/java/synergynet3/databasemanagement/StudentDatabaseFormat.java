package synergynet3.databasemanagement;

import java.util.ArrayList;

public class StudentDatabaseFormat {
	
	private String name = "";
	private String studentID = "";
	private String classname = "";
	private String colour = "white";
	private ArrayList<GalleryItemDatabaseFormat> galleryItems = new ArrayList<GalleryItemDatabaseFormat>();
	
	public StudentDatabaseFormat(String name, String studentID, String classname, String colour){
		this.name = name;
		this.studentID = studentID;
		this.classname = classname;
		this.colour = colour;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setStudentId(String studentID) {
		this.studentID = studentID;
	}

	public String getStudentID() {
		return studentID;
	}

	public void setClassname(String classname) {
		this.classname = classname;
	}

	public String getClassName() {
		return classname;
	}

	/**
	 * @param galleryItems the galleryItems to set
	 */
	public void addGalleryItem(GalleryItemDatabaseFormat galleryItem) {
		galleryItems.add(galleryItem);
	}

	/**
	 * @return the galleryItems
	 */
	public ArrayList<GalleryItemDatabaseFormat> getGalleryItems() {
		return galleryItems;
	}

	/**
	 * @param colour the colour to set
	 */
	public void setColour(String colour) {
		this.colour = colour;
	}

	/**
	 * @return the colour
	 */
	public String getColour() {
		return colour;
	}

}
