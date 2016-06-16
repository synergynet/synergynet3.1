/*
 * PTMReader.java Created on July 3, 2004, 11:38 PM
 */

package jpview.io;

import java.io.IOException;

import jpview.ptms.PTM;

/**
 * @author Default
 */
public interface PTMReader
{

	/**
	 * Read ptm.
	 *
	 * @return the ptm
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public PTM readPTM() throws java.io.IOException;

}
