package synergynet3.web.appsystem.persistence;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class ClassroomList {
	private XStream xstream;
	
	private ArrayList<String> classroomNames = new ArrayList<String>();
	private static final String CLASSLIST = "classNames.xml";

	@SuppressWarnings("unchecked")
	public ClassroomList(){
		xstream = new XStream(new DomDriver());		
		try{
			if (new File(DatabaseServer.getDatabaseDirectory() + CLASSLIST).isFile()){			
				FileInputStream inputStream = new FileInputStream(getClassroomListFile());
				classroomNames = (ArrayList<String>)xstream.fromXML(inputStream);
				inputStream.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void addClassroom(String name){
		classroomNames.add(name);
		commitChangesToFile();
	}
	
	public void removeClassroom(String name){	
		if (classroomNames.contains(name)){
			classroomNames.remove(name);
			commitChangesToFile();					
		}
	}
	
	public ArrayList<String> getClassroomNames(){
		return classroomNames;
	}
	
	private void commitChangesToFile(){		
		try {
			FileOutputStream outputStream = new FileOutputStream(getClassroomListFile());
			xstream.toXML(classroomNames, outputStream);
			outputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private File getClassroomListFile() throws IOException {
		File classListFile = new File(DatabaseServer.getDatabaseDirectory() + CLASSLIST);
		if (!classListFile.isFile()){
			classListFile.createNewFile();
		}
		return classListFile;
	}	
}
