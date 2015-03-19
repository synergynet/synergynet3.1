package synergynet3.databasemanagement;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import multiplicity3.csys.items.item.IItem;
import multiplicity3.csys.stage.IStage;

import synergynet3.cachecontrol.ItemCaching;
import synergynet3.config.web.CacheOrganisation;
import synergynet3.personalcontentcontrol.StudentRepresentation;
import synergynet3.web.shared.Database;
import synergynet3.web.shared.Student;

import com.db4o.ObjectContainer;
import com.db4o.cs.Db4oClientServer;
import com.db4o.cs.config.ClientConfiguration;

public class DatabaseActivity {
	
	private static ClientConfiguration generateConfig(){
		ClientConfiguration config = Db4oClientServer.newClientConfiguration();
		config.common().objectClass(StudentDatabaseFormat.class).cascadeOnActivate(true);
		config.common().objectClass(StudentDatabaseFormat.class).cascadeOnUpdate(true);
		config.common().objectClass(StudentDatabaseFormat.class).cascadeOnDelete(true);
		return config;
	}
	
	public static void storeStudent(Student student, String hostName){
		StudentDatabaseFormat dbRep = studentTodb(student);		
		ObjectContainer db = Db4oClientServer.openClient(generateConfig(), hostName, Database.DB_PORT, Database.DB_USER, Database.DB_PASS);		
		try {			
			db.store(dbRep);
		} finally {
		    db.close();
		}
	}
	
	public static ArrayList<Student> getStudentsFromDatabase(String hostName){
		ArrayList<Student> toReturn = new ArrayList<Student>();		
		ObjectContainer db = Db4oClientServer.openClient(generateConfig(), hostName, Database.DB_PORT, Database.DB_USER, Database.DB_PASS);
		try {			
			List <StudentDatabaseFormat> dbReps = db.query(StudentDatabaseFormat.class);	
			
			if (dbReps.size() > 0){
				for (StudentDatabaseFormat dbRep : dbReps){					
					Student student = dbToStudent(dbRep);
					toReturn.add(student);
				}
			}			
		} finally {
		    db.close();
		}	
		return toReturn;
	}
	
	public static StudentRepresentation getStudentRepresentationFromDatabase(final String studentID, String hostName, IStage stage){
		ObjectContainer db = Db4oClientServer.openClient(generateConfig(), hostName, Database.DB_PORT, Database.DB_USER, Database.DB_PASS);
		try {		
			
			List <StudentDatabaseFormat> dbReps = db.query(StudentDatabaseFormat.class);		
			if (dbReps.size() > 0){
				for (StudentDatabaseFormat dbRep : dbReps){
					if (dbRep.getStudentID().equals(studentID)){									
						StudentRepresentation studentRep = dbToStudentRep(dbRep, stage);
						return studentRep;
					}					
				}
			}			
		} finally {
		    db.close();
		}				
		return null;
	}
	
	public static String getStudentColour(String studentID, String hostName) {
		ObjectContainer db = Db4oClientServer.openClient(generateConfig(), hostName, Database.DB_PORT, Database.DB_USER, Database.DB_PASS);
		try {			
			List <StudentDatabaseFormat> dbReps = db.query(StudentDatabaseFormat.class);			
			if (dbReps.size() > 0){
				for (StudentDatabaseFormat dbRep : dbReps){
					if (dbRep.getStudentID().equals(studentID)){
						return dbRep.getColour();
					}					
				}
			}			
		} finally {
		    db.close();
		}
		return "white";
	}
	
	public static ArrayList<GalleryItemDatabaseFormat> getStudentGallery(String studentID, String hostName) {
		ObjectContainer db = Db4oClientServer.openClient(generateConfig(), hostName, Database.DB_PORT, Database.DB_USER, Database.DB_PASS);
		try {			
			List <StudentDatabaseFormat> dbReps = db.query(StudentDatabaseFormat.class);			
			if (dbReps.size() > 0){
				for (StudentDatabaseFormat dbRep : dbReps){
					if (dbRep.getStudentID().equals(studentID)){
						return dbRep.getGalleryItems();
					}					
				}
			}			
		} finally {
		    db.close();
		}
		return new ArrayList<GalleryItemDatabaseFormat>();
	}
	
	
	public static void moveStudent(String studentID, String newClass, String hostName) {
		ObjectContainer db = Db4oClientServer.openClient(generateConfig(), hostName, Database.DB_PORT, Database.DB_USER, Database.DB_PASS);
		try {			
			List <StudentDatabaseFormat> dbReps = db.query(StudentDatabaseFormat.class);			
			if (dbReps.size() > 0){
				for (StudentDatabaseFormat dbRep : dbReps){
					if (dbRep.getStudentID().equals(studentID)){
						dbRep.setClassname(newClass);
						db.delete(dbRep);
						db.store(dbRep);
						break;
					}					
				}
			}			
		} finally {
		    db.close();
		}				
	}
	
	public static void updateStudentRep(final StudentRepresentation student, final String hostName) {	
		Thread cachingThread = new Thread(new Runnable() {	  
			public void run() {	   
				ObjectContainer db = Db4oClientServer.openClient(generateConfig(), hostName, Database.DB_PORT, Database.DB_USER, Database.DB_PASS);
				try {			
					List <StudentDatabaseFormat> dbReps = db.query(StudentDatabaseFormat.class);
					
					if (dbReps != null){
						for (StudentDatabaseFormat dbRep : dbReps){
							if (dbRep.getStudentID().equals(student.getStudentId())){	
								db.delete(dbRep);					
								StudentDatabaseFormat dbRepToStore = studentRepTodb(student);					
								db.store(dbRepToStore);						
								break;
							}
						}
					}			
				} finally {
				    db.close();
				}	
			}
		});	
		cachingThread.start();
	}
	
	public static void modifyStudent(Student student, String hostName) {
		ObjectContainer db = Db4oClientServer.openClient(generateConfig(), hostName, Database.DB_PORT, Database.DB_USER, Database.DB_PASS);
		try {			
			List <StudentDatabaseFormat> dbReps = db.query(StudentDatabaseFormat.class);			
			if (dbReps != null){
				for (StudentDatabaseFormat dbRep : dbReps){
					if (dbRep.getStudentID().equals(student.getStudentID())){		
						ArrayList<GalleryItemDatabaseFormat> galleryItems = dbRep.getGalleryItems();
						db.delete(dbRep);								
						StudentDatabaseFormat dbRepNew = studentTodb(student);
						for (GalleryItemDatabaseFormat galleryItem: galleryItems)dbRepNew.addGalleryItem(galleryItem);
						db.store(dbRepNew);						
						break;
					}
				}
			}			
		} finally {
		    db.close();
		}	
	}

	public static void deleteStudent(String studentID, String hostName) {
		ObjectContainer db = Db4oClientServer.openClient(generateConfig(), hostName, Database.DB_PORT, Database.DB_USER, Database.DB_PASS);
		try {			
			List <StudentDatabaseFormat> dbReps = db.query(StudentDatabaseFormat.class);			
			if (dbReps != null){
				for (StudentDatabaseFormat dbRep : dbReps){
					if (dbRep.getStudentID().equals(studentID)){						
						db.delete(dbRep);					
						break;
					}
				}
			}			
		} finally {
		    db.close();
		}	
	}
	
	public static void removeStudentsOfClass(String className, String hostName) {
		ObjectContainer db = Db4oClientServer.openClient(generateConfig(), hostName, Database.DB_PORT, Database.DB_USER, Database.DB_PASS);
		try {			
			List <StudentDatabaseFormat> dbReps = db.query(StudentDatabaseFormat.class);			
			if (dbReps != null){
				for (StudentDatabaseFormat dbRep : dbReps){
					if (dbRep.getClassName().equalsIgnoreCase(className)){						
						db.delete(dbRep);					
					}
				}
			}			
		} finally {
		    db.close();
		}			
	}
	
	private static StudentRepresentation dbToStudentRep(StudentDatabaseFormat dbRep, IStage stage){
		Student student = new Student();
		student.setName(dbRep.getName());
		student.setClassName(dbRep.getClassName());
		student.setColour(dbRep.getColour());
		student.setStudentID(dbRep.getStudentID());
		StudentRepresentation studentRep = new StudentRepresentation(student);		
		for (int i = 0; i < dbRep.getGalleryItems().size(); i++){	
			try{
				IItem item = ItemCaching.reconstructItem(dbRep.getGalleryItems().get(i), stage, CacheOrganisation.STUDENT_DIR + File.separator + dbRep.getStudentID());		
				if (item != null){
					stage.addItem(item);
					studentRep.getGallery().addToGallery(item, stage);
				}
			}catch(NullPointerException e){}
		}				
		return studentRep;
	}
	
	private static StudentDatabaseFormat studentRepTodb(StudentRepresentation studentRep){
		StudentDatabaseFormat studentDbRep = new StudentDatabaseFormat(studentRep.getName(), studentRep.getStudentId(), studentRep.getClassName(), studentRep.getStudent().getColour());
		for (int i = 0; i < studentRep.getGallery().getGalleryItems().size(); i++){
			GalleryItemDatabaseFormat itemRep = 
				ItemCaching.deconstructItem(studentRep.getGallery().getGalleryItems().get(i), studentRep.getGallery().getGalleryItemsInfo().get(i), 
						CacheOrganisation.STUDENT_DIR + File.separator + studentRep.getStudentId());
			if (itemRep != null)studentDbRep.addGalleryItem(itemRep);			
		}		
		return studentDbRep;
	}
	
	private static Student dbToStudent(StudentDatabaseFormat dbRep){
		Student student = new Student();
		student.setName(dbRep.getName());
		student.setClassName(dbRep.getClassName());
		student.setColour(dbRep.getColour());	
		student.setStudentID(dbRep.getStudentID());
		return student;
	}
	
	private static StudentDatabaseFormat studentTodb(Student student){
		return new StudentDatabaseFormat(student.getName(), student.getStudentID(), student.getClassName(), student.getColour());
	}
	
}
