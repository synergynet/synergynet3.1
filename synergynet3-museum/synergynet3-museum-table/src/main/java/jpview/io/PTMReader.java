/*
 * PTMReader.java
 *
 * Created on July 3, 2004, 11:38 PM
 */

package jpview.io;

import jpview.ptms.PTM;

/**
 * 
 * @author Default
 */
public interface PTMReader {

	public PTM readPTM() throws java.io.IOException;

}
