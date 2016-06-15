package synergynet3.mediadetection.mediasearchtypes;

import java.util.logging.Level;

import synergynet3.mediadetection.MediaDetection;

/**
 * The Class CustomSearchType.
 */
public class CustomSearchType extends MediaSearchType
{

	/**
	 * Instantiates a new custom search type.
	 */
	public CustomSearchType()
	{
		extensions = new String[0];
	}

	/**
	 * Adds the extension.
	 *
	 * @param extension
	 *            the extension
	 */
	public void addExtension(String extension)
	{
		if (extension.length() > 1)
		{
			if (extension.subSequence(0, 0).equals("."))
			{
				addExtension(extension.substring(1));
			}
			else
			{
				String[] newExtensions = new String[extensions.length + 1];
				for (int i = 0; i < extensions.length; i++)
				{
					newExtensions[i] = extensions[i];
				}
				newExtensions[extensions.length] = extension;
				extensions = newExtensions;
			}
		}
		else
		{
			MediaDetection.logMediaDetectionError(Level.SEVERE, "Extension string is too short to be valid.", null);
		}
	}

}
