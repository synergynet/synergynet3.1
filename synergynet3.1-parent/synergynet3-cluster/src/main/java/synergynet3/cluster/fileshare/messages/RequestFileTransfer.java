package synergynet3.cluster.fileshare.messages;

import synergynet3.cluster.fileshare.localfilecache.MD5Hash;

public class RequestFileTransfer extends FileDistributionMessage {
	private static final long serialVersionUID = 7140665603093675333L;
	private String deviceAsking;
	private MD5Hash idOfFile;
	private String deviceThatHasFile;

	public RequestFileTransfer(String deviceAsking, MD5Hash idOfFile, String deviceThatHasFile) {
		this.deviceAsking = deviceAsking;
		this.idOfFile = idOfFile;
		this.deviceThatHasFile = deviceThatHasFile;
	}
	
	public String getDeviceAsking() {
		return deviceAsking;
	}
	
	public MD5Hash getIDOfFile() {
		return idOfFile;
	}
	
	public String deviceThatHasFile() {
		return deviceThatHasFile;
	}
}
