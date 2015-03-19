package synergynet3.cluster.xmpp.messaging.appcontrol;

public class SwitchToApplication extends AppControlMessage {	
	private static final long serialVersionUID = 5340810309039900348L;
	private String classname;
	
	public SwitchToApplication(String classname) {
		setClassname(classname);
	}

	public void setClassname(String classname) {
		this.classname = classname;
	}

	public String getClassname() {
		return classname;
	}
}
