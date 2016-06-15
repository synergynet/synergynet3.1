package synergynet3.cluster.fileshare.localfilecache;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The Class LocalFileCache.
 */
public class LocalFileCache
{

	/** The Constant log. */
	private static final Logger log = Logger.getLogger(LocalFileCache.class.getName());

	/** The Constant propertiesFileName. */
	private static final String propertiesFileName = "info.properties";

	/** The cache root directory. */
	private File cacheRootDirectory;

	/**
	 * Instantiates a new local file cache.
	 *
	 * @param directory
	 *            the directory
	 */
	public LocalFileCache(File directory)
	{
		this.cacheRootDirectory = directory;
		boolean directoryDoesExist = ensureCacheRootDirectoryExists();
		if (!directoryDoesExist)
		{
			throw new IllegalArgumentException(directory + " is not a valid cache directory");
		}
	}

	/**
	 * Destroy local file cache.
	 *
	 * @param cache
	 *            the cache
	 */
	public static void destroyLocalFileCache(LocalFileCache cache)
	{
		File rootDirectory = cache.getCacheRootDirectory();
		recursiveFileAndDirectoryDelete(rootDirectory);
	}

	/**
	 * Recursive file and directory delete.
	 *
	 * @param directoryToDeleteAllContentsOf
	 *            the directory to delete all contents of
	 */
	private static void recursiveFileAndDirectoryDelete(File directoryToDeleteAllContentsOf)
	{
		File[] allFilesAndDirectories = directoryToDeleteAllContentsOf.listFiles();
		for (int i = 0; i < allFilesAndDirectories.length; i++)
		{
			File current = allFilesAndDirectories[i];
			if (current.isDirectory())
			{
				recursiveFileAndDirectoryDelete(current);
			}
			else
			{
				current.delete();
			}
		}
		directoryToDeleteAllContentsOf.delete();
	}

	/**
	 * Adds the file to cache.
	 *
	 * @param localFile
	 *            the local file
	 * @param device
	 *            the device
	 * @return the local file cache entry
	 * @throws FileNotFoundException
	 *             the file not found exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public LocalFileCacheEntry addFileToCache(File localFile, String device) throws FileNotFoundException, IOException
	{
		return this.addFileToCacheWithName(localFile, localFile.getName(), device);
	}

	/**
	 * Adds the file to cache with name.
	 *
	 * @param localFile
	 *            the local file
	 * @param fileName
	 *            the file name
	 * @param device
	 *            the device
	 * @return the local file cache entry
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public LocalFileCacheEntry addFileToCacheWithName(File localFile, String fileName, String device) throws IOException
	{
		if (!localFile.exists())
		{
			throw new FileNotFoundException(localFile.getAbsolutePath());
		}

		try
		{
			log.fine("Adding " + localFile.getAbsolutePath() + " to content cache at " + cacheRootDirectory.getAbsolutePath());
			MD5Hash id = MD5Hash.md5(localFile);
			File existingFile = getCachedFileForHashIdentity(id);
			if (existingFile != null)
			{
				log.fine("Already have this file in our cache. Done.");
				return new LocalFileCacheEntry(id, existingFile);
			}

			log.fine("File not in the cache, copying in.");

			File directoryToStoreFile = getCacheDirectoryEntryForHashIdentity(id);
			if (!directoryToStoreFile.exists())
			{
				directoryToStoreFile.mkdirs();
			}

			File copyTo = new File(directoryToStoreFile, fileName);
			copyFile(localFile, copyTo);
			writeProperties(id, localFile.getName(), device, directoryToStoreFile);
			return new LocalFileCacheEntry(id, copyTo);

		}
		catch (NoSuchAlgorithmException e)
		{
			log.log(Level.SEVERE, "Don't have access to the MD5 digest.", e);
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	/**
	 * Cache contains file with hash identity.
	 *
	 * @param id
	 *            the id
	 * @return true, if successful
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public boolean cacheContainsFileWithHashIdentity(MD5Hash id) throws IOException
	{
		File f = getCachedFileForHashIdentity(id);
		if (f == null)
		{
			return false;
		}
		if (!f.exists())
		{
			return false;
		}
		return true;
	}

	/**
	 * Gets the cached file for hash identity.
	 *
	 * @param id
	 *            the id
	 * @return the cached file for hash identity
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public File getCachedFileForHashIdentity(MD5Hash id) throws IOException
	{
		log.fine("Getting file reference for id " + id);
		Properties props = getPropertiesForHashIdentity(id);
		if (props == null)
		{
			return null;
		}
		String name = props.getProperty("name");
		return new File(getCacheDirectoryEntryForHashIdentity(id), name);
	}

	/**
	 * Copy file.
	 *
	 * @param f
	 *            the f
	 * @param copyTo
	 *            the copy to
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private void copyFile(File f, File copyTo) throws IOException
	{
		FileInputStream fis = new FileInputStream(f);
		FileOutputStream fos = new FileOutputStream(copyTo);
		byte[] buf = new byte[2048];
		int read;
		while ((read = fis.read(buf)) != -1)
		{
			fos.write(buf, 0, read);
		}
		fos.close();
		fis.close();
	}

	/**
	 * Ensure cache root directory exists.
	 *
	 * @return true, if successful
	 */
	private boolean ensureCacheRootDirectoryExists()
	{
		if (this.cacheRootDirectory.exists() && this.cacheRootDirectory.isDirectory())
		{
			return true;
		}

		if (this.cacheRootDirectory.exists() && this.cacheRootDirectory.isFile())
		{
			return false;
		}

		if (!this.cacheRootDirectory.exists())
		{
			return cacheRootDirectory.mkdirs();
		}

		return false;
	}

	/**
	 * Gets the cache directory entry for hash identity.
	 *
	 * @param id
	 *            the id
	 * @return the cache directory entry for hash identity
	 */
	private File getCacheDirectoryEntryForHashIdentity(MD5Hash id)
	{
		String hashIDString = id.toString();
		File firstDirectoryLevelWithOneCharacter = new File(cacheRootDirectory, hashIDString.substring(0, 1));
		File secondDirectoryLevelWithTwoCharacters = new File(firstDirectoryLevelWithOneCharacter, hashIDString.substring(1, 2));
		File directoryForEntry = new File(secondDirectoryLevelWithTwoCharacters, hashIDString);
		return directoryForEntry;
	}

	/**
	 * Gets the cache root directory.
	 *
	 * @return the cache root directory
	 */
	private File getCacheRootDirectory()
	{
		return this.cacheRootDirectory;
	}

	/**
	 * Gets the properties for hash identity.
	 *
	 * @param id
	 *            the id
	 * @return the properties for hash identity
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private Properties getPropertiesForHashIdentity(MD5Hash id) throws IOException
	{
		if (id == null)
		{
			return null;
		}
		File dir = getCacheDirectoryEntryForHashIdentity(id);
		if (!dir.exists())
		{
			return null;
		}

		File propsFile = new File(dir, propertiesFileName);
		if (!propsFile.exists())
		{
			return null;
		}

		Properties p = new Properties();
		log.fine("Loading properties from " + propsFile.getAbsolutePath());
		FileInputStream fis = new FileInputStream(propsFile);
		p.load(fis);
		fis.close();
		return p;
	}

	/**
	 * Write properties.
	 *
	 * @param id
	 *            the id
	 * @param name
	 *            the name
	 * @param device
	 *            the device
	 * @param idDir
	 *            the id dir
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private void writeProperties(MD5Hash id, String name, String device, File idDir) throws IOException
	{
		Properties p = new Properties();
		p.setProperty("id", id.toString());
		p.setProperty("name", name);
		p.setProperty("device", device);
		FileOutputStream fos = new FileOutputStream(new File(idDir, propertiesFileName));
		p.store(fos, "");
		fos.close();
	}

}
