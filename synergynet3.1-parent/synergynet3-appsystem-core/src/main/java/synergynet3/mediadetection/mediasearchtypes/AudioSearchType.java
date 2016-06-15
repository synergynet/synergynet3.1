package synergynet3.mediadetection.mediasearchtypes;

/**
 * The Class AudioSearchType.
 */
public class AudioSearchType extends MediaSearchType
{

	// TODO: Can add more types

	/**
	 * Instantiates a new audio search type.
	 */
	public AudioSearchType()
	{
		extensions = new String[4];
		extensions[0] = "wav";
		extensions[1] = "mp3";
		extensions[2] = "wma";
		extensions[3] = "acc";
	}

}
