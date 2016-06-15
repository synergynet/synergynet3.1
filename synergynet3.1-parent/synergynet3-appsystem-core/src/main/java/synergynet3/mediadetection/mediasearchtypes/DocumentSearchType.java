package synergynet3.mediadetection.mediasearchtypes;

/**
 * The Class DocumentSearchType.
 */
public class DocumentSearchType extends MediaSearchType
{

	/**
	 * Instantiates a new document search type.
	 */
	public DocumentSearchType()
	{
		extensions = new String[2];
		extensions[0] = "doc";
		extensions[1] = "pdf";
	}

}
