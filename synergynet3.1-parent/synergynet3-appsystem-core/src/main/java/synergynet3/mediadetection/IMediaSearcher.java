package synergynet3.mediadetection;

import java.io.File;

// This is intended to be implemented by any class intending utilise a search
// listener.
/**
 * The Interface IMediaSearcher.
 */
public interface IMediaSearcher
{

	/**
	 * On find.
	 *
	 * @param files
	 *            the files
	 */
	public void onFind(File[] files);

}
