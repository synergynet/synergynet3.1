package synergynet3.cluster.fileshare.localfilecache;

import java.io.File;
import java.io.Serializable;

public class LocalFileCacheEntry implements Serializable {
	private static final long serialVersionUID = 3191230924406069447L;
	
	private MD5Hash hash;
	private File file;

	public LocalFileCacheEntry(MD5Hash hash, File file) {
		this.hash = hash;
		this.file = file;
	}
	
	public MD5Hash getHash() {
		return hash;
	}

	public File getFile() {
		return file;
	}
	
	public boolean equals(Object obj) {
		if(obj instanceof LocalFileCacheEntry) {
			LocalFileCacheEntry comparison = (LocalFileCacheEntry) obj;
			if(comparison == this) return true;
			if(comparison.getHash().equals(getHash()) && comparison.getFile().equals(getFile()))
				return true;
		}
		return false;
	}
}
