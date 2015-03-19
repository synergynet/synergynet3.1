/*
 * ColorChannelOp.java
 *
 * Created on September 5, 2004, 9:41 PM
 */

package jpview.transforms;

import jpview.ptms.PTM;

/**
 * 
 * @author clyon
 */
public class ColorChannelOp implements PixelTransformOp {

	/** Creates a new instance of ColorChannelOp */
	public ColorChannelOp() {
	}

	public void transformPixels(int[] pixels, PTM ptm) {
		int[] localPixels = pixels;
		PTM localPtm = ptm;
		int length = localPixels.length;
		for (int i = 0; i < length; i++) {
			localPixels[i] = localPtm.red(i) << 16 | localPtm.green(i) << 8
					| localPtm.blue(i);
		}
	}

	public void transformPixels(int[] pixels, PTM ptm, int mouseX, int mouseY) {
	}

	public void release() {
	}

	public void forceUpdate() {
	}

	public void clearCache() {
	}

}
