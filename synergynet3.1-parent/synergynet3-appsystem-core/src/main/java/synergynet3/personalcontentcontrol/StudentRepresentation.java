package synergynet3.personalcontentcontrol;

import java.util.logging.Logger;

import synergynet3.web.shared.ColourManager;
import synergynet3.web.shared.Student;

import com.jme3.math.ColorRGBA;

import multiplicity3.csys.items.container.IContainer;
import multiplicity3.csys.items.item.IItem;

public class StudentRepresentation{
	
	private StudentGallery gallery;
	private Student student;
	private IContainer wrapperFrame;
	private ColorRGBA studentColour;
	
	public StudentRepresentation (Student student){
		Logger log = Logger.getLogger(StudentRepresentation.class.getName());
		initialise(student, log);
	}
	
	public StudentRepresentation (Student student, String studentID, Logger log){
		initialise(student, log);
	}

	private void initialise(Student student, Logger log){
		this.student = student;
		studentColour = generateColour();
		gallery = new StudentGallery(this, log);
	}
	
	private ColorRGBA generateColour() {
		float[] colourRGB = ColourManager.getRGBForColour(student.getColour());
		return new ColorRGBA(colourRGB[0], colourRGB[1], colourRGB[2], 1f);
	}
	
	public IItem asItem(){
		return wrapperFrame;
	}
	
	public Student getStudent() {
		return student;
	}

	public void setStudentColour(ColorRGBA studentColour) {
		this.studentColour = studentColour;
	}

	public ColorRGBA getStudentColour() {
		return studentColour;
	}

	public StudentGallery getGallery(){
		return gallery;
	}
	
	public String getName() {
		return student.getName();
	}
	
	public String getClassName(){
		return student.getClassName();
	}

	public String getStudentId() {
		return student.getStudentID();
	}



}
