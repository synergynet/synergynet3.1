/*
 * GaussianKernel.java
 *
 * Created on September 25, 2004, 4:52 PM
 */

package jpview.graphics;

/**
 * This class creates a kernel for use by an AffineTransformOp
 * 
 * @author clyon
 */
public class GaussianKernel {

	private int radius = 5;

	private float sigma = 1;

	private float[] kernel;

	/** Creates a new instance of GaussianKernel */
	public GaussianKernel() {
		kernel = makeKernel();
	}

	/**
	 * Creates a gaussian kernel with the provided radius
	 * 
	 * @param r
	 *            the radius for the gaussian kernel
	 */
	public GaussianKernel(int r) {
		radius = r;
		kernel = makeKernel();
	}

	/**
	 * Creates a gaussian kernel with the provided radius and sigma value
	 * 
	 * @param r
	 *            the radius of the blur
	 * @param s
	 *            the sigma value for the kernel
	 */
	public GaussianKernel(int r, float s) {
		radius = r;
		sigma = s;
		kernel = makeKernel();
	}

	private float[] makeKernel() {
		kernel = new float[radius * radius];
		float sum = 0;
		for (int y = 0; y < radius; y++) {
			for (int x = 0; x < radius; x++) {
				int off = y * radius + x;
				int xx = x - radius / 2;
				int yy = y - radius / 2;
				kernel[off] = (float) Math.pow(Math.E, -(xx * xx + yy * yy)
						/ (2 * (sigma * sigma)));
				sum += kernel[off];
			}
		}
		for (int i = 0; i < kernel.length; i++)
			kernel[i] /= sum;
		return kernel;
	}

	/**
	 * Dumps a string representation of the kernel to standard out
	 */
	public void dump() {
		for (int x = 0; x < radius; x++) {
			for (int y = 0; y < radius; y++) {
				System.out.print(kernel[y * radius + x] + "\t");
			}
			System.out.println();
		}
	}

	/**
	 * returns the kernel as a float [] suitable for use with an affine
	 * transform
	 * 
	 * @return the kernel values
	 */
	public float[] getKernel() {
		return kernel;
	}

	public static void main(String args[]) {
		GaussianKernel gk = new GaussianKernel(5, 100f);
		gk.dump();

	}
}
