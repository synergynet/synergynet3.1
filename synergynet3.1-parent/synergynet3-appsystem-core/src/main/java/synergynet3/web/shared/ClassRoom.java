package synergynet3.web.shared;

import java.io.Serializable;

/**  
 * Representation of a class of students. This class is comparable so that instances of it can be ordered by their name.
 */
public class ClassRoom implements Comparable<ClassRoom>, Serializable {
	
	/** Generated unique serial id for serialisation. */
	private static final long serialVersionUID = 7802827317172799668L;

	/** Name of the class. */
	private String name = "New_Class";
	
	/** Representation of whether the class is in session (i.e. whether students in the class can be logged in). */
	private boolean inSession = false;

	/**
	 * Set the name of the class.
	 * 
	 * @param name The name of the class.
	 */
	public void setName(String name){
		this.name = name;
	}	

	/**
	 * Get the name of the class.
	 * 
	 * @return The name of the class.
	 */
	public String getName() {
		return name;
	}

	@Override
	public int compareTo(ClassRoom otherClass) {
        int lastCmp = name.compareTo(otherClass.getName());
        return (lastCmp != 0 ? lastCmp : name.compareTo(otherClass.getName()));
	}

	/**
	 * Set whether a class is in session.
	 * 
	 * @param inSession A boolean value representing whether the class is in session
	 */
	public void setInSession(boolean inSession) {
		this.inSession = inSession;
	}

	/**
	 * Get whether a class is in session.
	 * 
	 * @return A boolean value representing whether the class is in session.
	 */
	public boolean isInSession() {
		return inSession;
	}	
}
