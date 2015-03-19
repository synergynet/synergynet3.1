package synergynet3.web.shared.messages;

import java.io.Serializable;

public class PerformActionMessage implements Serializable {
	
	public enum MESSAGESTATE{DO_NOTHING, ACTIVATE, DEACTIVATE}
	
	private static final long serialVersionUID = 8614541915205145862L;
	private String messageID = "";
	private MESSAGESTATE state = MESSAGESTATE.DO_NOTHING;
	
	public PerformActionMessage(){
		messageID = MessageSystem.generateMessageID();
	}
	
	public PerformActionMessage(MESSAGESTATE state){
		this.state = state;
		messageID = MessageSystem.generateMessageID();
	}
	
	public boolean messageAlreadyReceived(){
		return MessageSystem.messageAlreadyReceived(this);
	}
	
	public void regenerateID(){
		messageID = MessageSystem.generateMessageID();
	}	
	
	public String getMessageID(){
		return messageID;
	}
	
	public MESSAGESTATE getMessageState(){
		return state;
	}	
}