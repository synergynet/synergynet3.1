package synergynet3.cluster.fileshare.localfilecache;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigInteger;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * The Class MD5Hash.
 */
public class MD5Hash implements Serializable
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 4356452630424788104L;

	/** The hash string. */
	private String hashString;

	/**
	 * Instantiates a new m d5 hash.
	 *
	 * @param hashString
	 *            the hash string
	 */
	public MD5Hash(String hashString)
	{
		this.hashString = hashString;
	}

	/**
	 * Md5.
	 *
	 * @param file
	 *            the file
	 * @return the m d5 hash
	 * @throws NoSuchAlgorithmException
	 *             the no such algorithm exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static MD5Hash md5(File file) throws NoSuchAlgorithmException, IOException
	{
		MessageDigest md = MessageDigest.getInstance("MD5");
		InputStream is = new FileInputStream(file);

		is = new DigestInputStream(is, md);
		byte[] buf = new byte[2048];
		while ((is.read(buf)) != -1)
		{
			//
		}

		byte[] digest = md.digest();
		BigInteger i = new BigInteger(1, digest);
		is.close();
		return new MD5Hash(String.format("%1$032X", i));
	}

	/**
	 * Md5.
	 *
	 * @param s
	 *            the s
	 * @return the m d5 hash
	 * @throws NoSuchAlgorithmException
	 *             the no such algorithm exception
	 */
	public static MD5Hash md5(String s) throws NoSuchAlgorithmException
	{
		MessageDigest m = MessageDigest.getInstance("MD5");
		m.update(s.getBytes(), 0, s.length());
		BigInteger i = new BigInteger(1, m.digest());
		return new MD5Hash(String.format("%1$032X", i));
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof MD5Hash)
		{
			MD5Hash testHash = (MD5Hash) obj;
			return testHash.getHash().equals(getHash());
		}
		return false;
	}

	/**
	 * Gets the hash.
	 *
	 * @return the hash
	 */
	public String getHash()
	{
		return this.hashString;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		return hashString.hashCode();
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return this.hashString;
	}

}
