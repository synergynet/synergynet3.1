package synergynet3.mediadetection.mediasearchtypes;

/**
 * The Class ImageSearchType.
 */
public class ImageSearchType extends MediaSearchType
{

	/**
	 * Instantiates a new image search type.
	 */
	public ImageSearchType()
	{
		extensions = new String[3];
		extensions[0] = "jpg";
		extensions[1] = "png";
		extensions[2] = "gif";
	}

}
