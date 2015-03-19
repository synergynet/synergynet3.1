/*
 * ColorChannelOp.java
 *
 * Created on September 5, 2004, 9:41 PM
 */

package jpview.transforms;

import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

import jpview.Utils;
import jpview.graphics.Vec3f;
import jpview.ptms.PTM;

/**
 * 
 * @author clyon
 */
public class ReflectionMapOp implements PixelTransformOp {

	public void transformPixels(int[] pixels, PTM ptm) {
		int[] localPixels = pixels;
		Vec3f eye = new Vec3f(0, 0, 1);
		int length = localPixels.length;
		for (int i = 0; i < length; i++) {
			Vec3f N = ptm.normal(i);
			if (N.x() == 0 && N.y() == 0 && N.z() == 0) {
				localPixels[i] = 0;
				continue;
			}
			Vec3f R = Vec3f.reflect(N, eye);
			localPixels[i] = R.toPixel();
		}
		// apply gaussian blur.

		// make a copy
		BufferedImage tmp = new BufferedImage(ptm.getWidth(), ptm.getHeight(),
				BufferedImage.TYPE_INT_RGB);
		int[] buf = Utils.grabPixels(tmp);

		for (int i = 0; i < buf.length; i++)
			buf[i] = localPixels[i];

		float sum = (2 + 4 + 5 + 4 + 2) * 2 + (4 + 9 + 12 + 9 + 4) * 2 + 5 + 12
				+ 15 + 12 + 5;

		float[] elements = { 2f / sum, 4f / sum, 5f / sum, 4f / sum, 2f / sum,
				4f / sum, 9f / sum, 12f / sum, 9f / sum, 4f / sum, 5f / sum,
				12f / sum, 15f / sum, 12f / sum, 5f / sum, 4f / sum, 9f / sum,
				12f / sum, 9f / sum, 4f / sum, 2f / sum, 4f / sum, 5f / sum,
				4f / sum, 2f / sum };

		Kernel kernel = new Kernel(5, 5, elements);
		ConvolveOp cop = new ConvolveOp(kernel);
		BufferedImage tmp2 = new BufferedImage(ptm.getWidth(), ptm.getHeight(),
				BufferedImage.TYPE_INT_RGB);
		cop.filter(tmp, tmp2);
		
		for (int i = 0; i < buf.length; i++)
			localPixels[i] = buf[i];
	}

	public void transformPixels(int[] pixels, PTM ptm, int mouseX, int mouseY) {
		transformPixels(pixels, ptm);
	}

	public void release() {
	}

	public void forceUpdate() {
	}

	public void clearCache() {
	}
}
