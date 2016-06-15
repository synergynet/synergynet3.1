package synergynet3.web.appsystem.persistence;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * The Class ClassroomList.
 */
public class ClassroomList
{

	/** The Constant CLASSLIST. */
	private static final String CLASSLIST = "classNames.xml";

	/** The classroom names. */
	private ArrayList<String> classroomNames = new ArrayList<String>();

	/** The xstream. */
	private XStream xstream;

	/**
	 * Instantiates a new classroom list.
	 */
	@SuppressWarnings("unchecked")
	public ClassroomList()
	{
		xstream = new XStream(new DomDriver());
		try
		{
			if (new File(DatabaseServer.getDatabaseDirectory() + CLASSLIST).isFile())
			{
				FileInputStream inputStream = new FileInputStream(getClassroomListFile());
				classroomNames = (ArrayList<String>) xstream.fromXML(inputStream);
				inputStream.close();
			}
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Adds the classroom.
	 *
	 * @param name
	 *            the name
	 */
	public void addClassroom(String name)
	{
		classroomNames.add(name);
		commitChangesToFile();
	}

	/**
	 * Gets the classroom names.
	 *
	 * @return the classroom names
	 */
	public ArrayList<String> getClassroomNames()
	{
		return classroomNames;
	}

	/**
	 * Removes the classroom.
	 *
	 * @param name
	 *            the name
	 */
	public void removeClassroom(String name)
	{
		if (classroomNames.contains(name))
		{
			classroomNames.remove(name);
			commitChangesToFile();
		}
	}

	/**
	 * Commit changes to file.
	 */
	private void commitChangesToFile()
	{
		try
		{
			FileOutputStream outputStream = new FileOutputStream(getClassroomListFile());
			xstream.toXML(classroomNames, outputStream);
			outputStream.close();
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Gets the classroom list file.
	 *
	 * @return the classroom list file
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private File getClassroomListFile() throws IOException
	{
		File classListFile = new File(DatabaseServer.getDatabaseDirectory() + CLASSLIST);
		if (!classListFile.isFile())
		{
			classListFile.createNewFile();
		}
		return classListFile;
	}
}
