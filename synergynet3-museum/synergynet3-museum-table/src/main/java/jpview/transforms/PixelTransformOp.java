/*
 * Interface.java Created on September 5, 2004, 9:34 PM
 */

package jpview.transforms;

import jpview.ptms.PTM;

/**
 * @author clyon
 */
public interface PixelTransformOp {

	/**
	 * Clear cache.
	 */
	public void clearCache();

	/**
	 * Force update.
	 */
	public void forceUpdate();

	/**
	 * Release.
	 */
	public void release();

	/**
	 * Transform pixels.
	 *
	 * @param pixels the pixels
	 * @param ptm the ptm
	 */
	public void transformPixels(int[] pixels, PTM ptm);

	/**
	 * Transform pixels.
	 *
	 * @param pixels the pixels
	 * @param ptm the ptm
	 * @param mouseX the mouse x
	 * @param mouseY the mouse y
	 */
	public void transformPixels(int[] pixels, PTM ptm, int mouseX, int mouseY);
}
