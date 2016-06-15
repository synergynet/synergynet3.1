package synergynet3.cluster.fileshare.messages;

import synergynet3.cluster.fileshare.localfilecache.MD5Hash;

/**
 * The Class RequestFileTransfer.
 */
public class RequestFileTransfer extends FileDistributionMessage
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 7140665603093675333L;

	/** The device asking. */
	private String deviceAsking;

	/** The device that has file. */
	private String deviceThatHasFile;

	/** The id of file. */
	private MD5Hash idOfFile;

	/**
	 * Instantiates a new request file transfer.
	 *
	 * @param deviceAsking
	 *            the device asking
	 * @param idOfFile
	 *            the id of file
	 * @param deviceThatHasFile
	 *            the device that has file
	 */
	public RequestFileTransfer(String deviceAsking, MD5Hash idOfFile, String deviceThatHasFile)
	{
		this.deviceAsking = deviceAsking;
		this.idOfFile = idOfFile;
		this.deviceThatHasFile = deviceThatHasFile;
	}

	/**
	 * Device that has file.
	 *
	 * @return the string
	 */
	public String deviceThatHasFile()
	{
		return deviceThatHasFile;
	}

	/**
	 * Gets the device asking.
	 *
	 * @return the device asking
	 */
	public String getDeviceAsking()
	{
		return deviceAsking;
	}

	/**
	 * Gets the ID of file.
	 *
	 * @return the ID of file
	 */
	public MD5Hash getIDOfFile()
	{
		return idOfFile;
	}
}
