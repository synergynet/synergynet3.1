/*
 * Interface.java
 *
 * Created on September 5, 2004, 9:34 PM
 */

package jpview.transforms;

import jpview.ptms.PTM;

/**
 * 
 * @author clyon
 */
public interface PixelTransformOp {
	public void transformPixels(int[] pixels, PTM ptm);

	public void transformPixels(int[] pixels, PTM ptm, int mouseX, int mouseY);

	public void forceUpdate();

	public void release();

	public void clearCache();
}
