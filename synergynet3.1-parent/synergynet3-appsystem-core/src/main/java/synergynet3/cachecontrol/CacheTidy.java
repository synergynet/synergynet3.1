package synergynet3.cachecontrol;

import java.io.File;
import java.util.ArrayList;

import synergynet3.additionalitems.jme.AudioContainer;
import synergynet3.additionalitems.jme.CachableImage;
import synergynet3.additionalitems.jme.MediaPlayer;
import synergynet3.additionalitems.jme.ScreenshotContainer;
import synergynet3.config.web.CacheOrganisation;
import synergynet3.databasemanagement.DatabaseActivity;
import synergynet3.databasemanagement.GalleryItemDatabaseFormat;
import synergynet3.feedbacksystem.defaultfeedbacktypes.AudioFeedback;

/**
 * The Class CacheTidy.
 */
public class CacheTidy
{

	/**
	 * Creates the student cache.
	 *
	 * @param studentID
	 *            the student id
	 */
	public static void createStudentCache(String studentID)
	{
		CacheOrganisation.getSpecificStudentIconDir(studentID);
	}

	/**
	 * Removes the student cache.
	 *
	 * @param studentID
	 *            the student id
	 */
	public static void removeStudentCache(String studentID)
	{
		String cacheLocation = CacheOrganisation.getSpecificStudentDir(studentID);
		deleteCache(cacheLocation);
	}

	/**
	 * Removes the unused student files.
	 *
	 * @param hostname
	 *            the hostname
	 */
	public static void removeUnusedStudentFiles(String hostname)
	{
		File studentsCacheDir = new File(CacheOrganisation.getStudentsDir());
		if (studentsCacheDir.isDirectory())
		{
			for (File folder : studentsCacheDir.listFiles())
			{
				if (folder.isFile())
				{
					if (!folder.delete())
					{
						folder.deleteOnExit();
					}
				}
				else if (folder.isDirectory())
				{
					String studentID = folder.getName();
					File[] cachedFiles = folder.listFiles();
					ArrayList<File> toRemove = new ArrayList<File>();
					ArrayList<GalleryItemDatabaseFormat> galleryItems = DatabaseActivity.getStudentGallery(studentID, hostname);
					for (File cachedFile : cachedFiles)
					{
						if (cachedFile.isFile() && !cachedFile.isDirectory())
						{
							boolean present = false;
							for (GalleryItemDatabaseFormat galleryItem : galleryItems)
							{
								String type = galleryItem.getType();
								if (type.equalsIgnoreCase(CachableImage.CACHABLE_TYPE))
								{
									String imageFileName = (String) (galleryItem.getValues().get(0));
									if (imageFileName.equals(cachedFile.getName()))
									{
										present = true;
										break;
									}
								}
								else if (type.equalsIgnoreCase(ScreenshotContainer.CACHABLE_TYPE))
								{
									String imageFileName = (String) galleryItem.getValues().get(0);
									if (imageFileName.equals(cachedFile.getName()))
									{
										present = true;

										break;
									}
								}
								else if (type.equalsIgnoreCase(MediaPlayer.CACHABLE_TYPE))
								{
									String mediaFileName = (String) galleryItem.getValues().get(1);
									if (mediaFileName.equals(cachedFile.getName()))
									{
										present = true;
										break;
									}
								}
								else if (type.equalsIgnoreCase(AudioContainer.CACHABLE_TYPE))
								{
									String audioFileName = (String) galleryItem.getValues().get(0);
									if (audioFileName.equals(cachedFile.getName()))
									{
										present = true;
										break;
									}
								}
								for (Object[] feedbackItem : galleryItem.getFeedbackItems())
								{
									if (feedbackItem[0].equals(AudioFeedback.CACHABLE_TYPE))
									{
										String audioFileName = (String) feedbackItem[2];
										if (audioFileName.equals(cachedFile.getName()))
										{
											present = true;
											break;
										}
									}
								}
							}
							if (!present)
							{
								toRemove.add(cachedFile);
							}
						}
					}
					for (File unusedFile : toRemove)
					{
						if (!unusedFile.delete())
						{
							unusedFile.deleteOnExit();
						}
					}
				}
			}
		}
	}

	/**
	 * Delete cache.
	 *
	 * @param cacheLocation
	 *            the cache location
	 */
	private static void deleteCache(String cacheLocation)
	{
		File cacheFolder = new File(cacheLocation);
		if (cacheFolder.isDirectory())
		{
			if (!removeDirectory(cacheFolder))
			{
				cacheFolder.deleteOnExit();
			}
		}
	}

	/**
	 * Removes the directory.
	 *
	 * @param directory
	 *            the directory
	 * @return true, if successful
	 */
	private static boolean removeDirectory(File directory)
	{
		if (directory == null)
		{
			return false;
		}
		if (!directory.exists())
		{
			return true;
		}
		if (!directory.isDirectory())
		{
			return false;
		}
		String[] list = directory.list();
		if (list != null)
		{
			for (int i = 0; i < list.length; i++)
			{
				File entry = new File(directory, list[i]);
				if (entry.isDirectory())
				{
					if (!removeDirectory(entry))
					{
						return false;
					}
				}
				else
				{
					if (!entry.delete())
					{
						entry.deleteOnExit();
						return false;
					}
				}
			}
		}

		return directory.delete();
	}
}
