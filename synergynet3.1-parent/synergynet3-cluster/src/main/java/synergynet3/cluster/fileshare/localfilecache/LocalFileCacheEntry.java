package synergynet3.cluster.fileshare.localfilecache;

import java.io.File;
import java.io.Serializable;

/**
 * The Class LocalFileCacheEntry.
 */
public class LocalFileCacheEntry implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 3191230924406069447L;

	/** The file. */
	private File file;

	/** The hash. */
	private MD5Hash hash;

	/**
	 * Instantiates a new local file cache entry.
	 *
	 * @param hash the hash
	 * @param file the file
	 */
	public LocalFileCacheEntry(MD5Hash hash, File file) {
		this.hash = hash;
		this.file = file;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (obj instanceof LocalFileCacheEntry) {
			LocalFileCacheEntry comparison = (LocalFileCacheEntry) obj;
			if (comparison == this) {
				return true;
			}
			if (comparison.getHash().equals(getHash())
					&& comparison.getFile().equals(getFile())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Gets the file.
	 *
	 * @return the file
	 */
	public File getFile() {
		return file;
	}

	/**
	 * Gets the hash.
	 *
	 * @return the hash
	 */
	public MD5Hash getHash() {
		return hash;
	}
}
