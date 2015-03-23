/*
 * ColorChannelOp.java Created on September 5, 2004, 9:41 PM
 */

package jpview.transforms;

import jpview.ptms.PTM;

/**
 * @author clyon
 */
public class ColorChannelOp implements PixelTransformOp {

	/** Creates a new instance of ColorChannelOp */
	public ColorChannelOp() {
	}

	/*
	 * (non-Javadoc)
	 * @see jpview.transforms.PixelTransformOp#clearCache()
	 */
	public void clearCache() {
	}

	/*
	 * (non-Javadoc)
	 * @see jpview.transforms.PixelTransformOp#forceUpdate()
	 */
	public void forceUpdate() {
	}

	/*
	 * (non-Javadoc)
	 * @see jpview.transforms.PixelTransformOp#release()
	 */
	public void release() {
	}

	/*
	 * (non-Javadoc)
	 * @see jpview.transforms.PixelTransformOp#transformPixels(int[],
	 * jpview.ptms.PTM)
	 */
	public void transformPixels(int[] pixels, PTM ptm) {
		int[] localPixels = pixels;
		PTM localPtm = ptm;
		int length = localPixels.length;
		for (int i = 0; i < length; i++) {
			localPixels[i] = (localPtm.red(i) << 16) | (localPtm.green(i) << 8)
					| localPtm.blue(i);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see jpview.transforms.PixelTransformOp#transformPixels(int[],
	 * jpview.ptms.PTM, int, int)
	 */
	public void transformPixels(int[] pixels, PTM ptm, int mouseX, int mouseY) {
	}

}
