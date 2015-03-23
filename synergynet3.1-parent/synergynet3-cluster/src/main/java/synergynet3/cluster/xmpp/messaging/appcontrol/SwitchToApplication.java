package synergynet3.cluster.xmpp.messaging.appcontrol;

/**
 * The Class SwitchToApplication.
 */
public class SwitchToApplication extends AppControlMessage {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 5340810309039900348L;

	/** The classname. */
	private String classname;

	/**
	 * Instantiates a new switch to application.
	 *
	 * @param classname the classname
	 */
	public SwitchToApplication(String classname) {
		setClassname(classname);
	}

	/**
	 * Gets the classname.
	 *
	 * @return the classname
	 */
	public String getClassname() {
		return classname;
	}

	/**
	 * Sets the classname.
	 *
	 * @param classname the new classname
	 */
	public void setClassname(String classname) {
		this.classname = classname;
	}
}
