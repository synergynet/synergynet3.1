package synergynet3.mediadetection.mediasearchtypes;

/**
 * The Class VideoSearchType.
 */
public class VideoSearchType extends MediaSearchType
{

	/**
	 * Instantiates a new video search type.
	 */
	public VideoSearchType()
	{
		extensions = new String[5];
		extensions[0] = "mp4";
		extensions[1] = "avi";
		extensions[2] = "mpg";
		extensions[3] = "mov";
		extensions[4] = "ogg";
	}

}
