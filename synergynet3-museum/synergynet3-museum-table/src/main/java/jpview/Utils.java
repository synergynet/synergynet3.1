/*
 * Utils.java Created on July 24, 2004, 5:12 PM
 */

package jpview;

import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.DataBufferInt;
import java.awt.image.Kernel;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import javax.imageio.ImageIO;

import jpview.graphics.GaussianKernel;
import jpview.graphics.Vec3f;

/**
 * @author Default
 */
public class Utils {

	/**
	 * As string.
	 *
	 * @param a the a
	 * @return the string
	 */
	public static String asString(double[] a) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < a.length; i++) {
			if (i > 0) {
				sb.append(", ");
			}
			sb.append(a[i]);
		}
		return new String(sb);
	}

	/**
	 * As string.
	 *
	 * @param a the a
	 * @return the string
	 */
	public static String asString(float[] a) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < a.length; i++) {
			if (i > 0) {
				sb.append(", ");
			}
			sb.append(a[i]);
		}
		return new String(sb);
	}

	/**
	 * As string.
	 *
	 * @param a the a
	 * @return the string
	 */
	public static String asString(int[] a) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < a.length; i++) {
			if (i > 0) {
				sb.append(", ");
			}
			sb.append(a[i]);
		}
		return new String(sb);
	}

	/**
	 * Blur image simple.
	 *
	 * @param image the image
	 * @param kSize the k size
	 * @return the buffered image
	 */
	public static BufferedImage blurImageSimple(BufferedImage image, int kSize) {
		float patchval = ((float) 1) / (kSize * kSize);
		float[] k = new float[kSize * kSize];
		Arrays.fill(k, patchval);
		BufferedImage target = new BufferedImage(image.getWidth(),
				image.getHeight(), BufferedImage.TYPE_INT_RGB);
		Kernel kernel = new Kernel(kSize, kSize, k);
		ConvolveOp cop = new ConvolveOp(kernel);
		cop.filter(image, target);
		return target;
	}

	/** clamp to 8-bit int */
	public static int clamp(int i) {
		if (i < 0) {
			return 0;
		}
		if (i > 255) {
			return 255;
		}
		return i;
	}

	/**
	 * Creates the buffered image.
	 *
	 * @param pixels the pixels
	 * @param scanline the scanline
	 * @return the buffered image
	 */
	public static BufferedImage createBufferedImage(int[] pixels, int scanline) {
		BufferedImage bi = new BufferedImage(scanline,
				pixels.length / scanline, BufferedImage.TYPE_INT_RGB);
		int[] p = Utils.grabPixels(bi);
		for (int i = 0; i < p.length; i++) {
			p[i] = pixels[i];
		}
		return bi;
	}

	/**
	 * Creates the buffered image.
	 *
	 * @param pixels the pixels
	 * @param scanline the scanline
	 * @return the buffered image
	 */
	public static BufferedImage createBufferedImage(Vec3f[] pixels, int scanline) {
		BufferedImage bi = new BufferedImage(scanline,
				pixels.length / scanline, BufferedImage.TYPE_INT_RGB);
		int[] p = Utils.grabPixels(bi);
		for (int i = 0; i < p.length; i++) {
			p[i] = pixels[i].toPixel();
		}
		return bi;
	}

	/**
	 * Dot3.
	 *
	 * @param a the a
	 * @param b the b
	 * @return the double
	 */
	public static double dot3(double[] a, double[] b) {
		return ((a[0] * b[0]) + (a[1] * b[1]) + (a[2] * b[2]));
	}

	/**
	 * Dot3.
	 *
	 * @param a the a
	 * @param b the b
	 * @return the float
	 */
	public static float dot3(float[] a, float[] b) {
		return ((a[0] * b[0]) + (a[1] * b[1]) + (a[2] * b[2]));
	}

	/**
	 * Gaussian blur.
	 *
	 * @param image the image
	 * @param kSize the k size
	 * @param sigma the sigma
	 * @return the buffered image
	 */
	public static BufferedImage gaussianBlur(BufferedImage image, int kSize,
			float sigma) {
		BufferedImage target = new BufferedImage(image.getWidth(),
				image.getHeight(), BufferedImage.TYPE_INT_RGB);
		Kernel kernel = new Kernel(kSize, kSize, new GaussianKernel(kSize,
				sigma).getKernel());
		ConvolveOp cop = new ConvolveOp(kernel);
		cop.filter(image, target);
		return target;
	}

	/**
	 * Grab pixels.
	 *
	 * @param image the image
	 * @return the int[]
	 */
	public static int[] grabPixels(BufferedImage image) {
		return ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
	}

	/**
	 * Index of max.
	 *
	 * @param a the a
	 * @return the int
	 */
	public static int indexOfMax(double[] a) {
		double max = a[0];
		int idx = 0;
		for (int i = 0; i < a.length; i++) {
			if (a[i] > max) {
				max = a[i];
				idx = i;
			}
		}
		return idx;
	}

	/**
	 * Int to vec.
	 *
	 * @param a the a
	 * @return the vec3f[]
	 */
	public static Vec3f[] intToVec(int[] a) {
		Vec3f[] v = new Vec3f[a.length];
		for (int i = 0; i < v.length; i++) {
			v[i] = Vec3f.convertPixel(a[i]);
		}
		return v;
	}

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		try {
			double[] normal = new double[] { .4, -.9, 0 };
			double[] incedent = new double[] { 0, 0, 1 };
			normal = Utils.normalize3(normal);
			incedent = Utils.normalize3(incedent);
			double[] reflected = Utils.normalize3(Utils.reflect(normal,
					incedent));
			System.out.println("normal:    " + Utils.asString(normal));
			System.out.println("indecent:  " + Utils.asString(incedent));
			System.out.println("reflected: " + Utils.asString(reflected));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Normalize.
	 *
	 * @param x the x
	 * @param y the y
	 * @param z the z
	 * @return the float[]
	 */
	public static float[] normalize(float x, float y, float z) {
		float len = (float) Math.sqrt((x * x) + (y * y) + (z * z));
		return new float[] { x / len, y / len, z / len };
	}

	/**
	 * Normalize3.
	 *
	 * @param a the a
	 * @return the double[]
	 */
	public static double[] normalize3(double[] a) {
		double len = Math.sqrt((a[0] * a[0]) + (a[1] * a[1]) + (a[2] * a[2]));
		return new double[] { a[0] / len, a[1] / len, a[2] / len };
	}

	/**
	 * Normalize3.
	 *
	 * @param a the a
	 * @return the float[]
	 */
	public static float[] normalize3(float[] a) {
		float len = (float) Math.sqrt((a[0] * a[0]) + (a[1] * a[1])
				+ (a[2] * a[2]));
		return new float[] { a[0] / len, a[1] / len, a[2] / len };
	}

	/**
	 * Read unbuffered.
	 *
	 * @param is the is
	 * @return the buffered image
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static BufferedImage readUnbuffered(InputStream is)
			throws java.io.IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int b;
		while ((b = is.read()) != -1) {
			baos.write(b);
		}
		ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
		return ImageIO.read(bais);
	}

	/**
	 * Reflect.
	 *
	 * @param normal the normal
	 * @param incedent the incedent
	 * @return the double[]
	 */
	public static double[] reflect(double[] normal, double[] incedent) {
		// I - 2.0 * N * dot(N, I);
		// R = V - ( 2 * V [dot] N ) N
		double t1 = dot3(normal, incedent) * 2f;
		double[] n1 = scalarMult3(normal, t1);
		return normalize3(vecSum3(scalarMult3(incedent, -1), n1));
	}

	/**
	 * Reflect.
	 *
	 * @param normal the normal
	 * @param incedent the incedent
	 * @return the float[]
	 */
	public static float[] reflect(float[] normal, float[] incedent) {
		// I - 2.0 * N * dot(N, I);
		// R = 2 * N.I * N - I
		float t1 = dot3(normal, incedent) * 2f;
		float[] n1 = scalarMult3(normal, t1);
		return normalize3(vecSum3(scalarMult3(incedent, -1), n1));
	}

	/**
	 * Scalar mult3.
	 *
	 * @param a the a
	 * @param s the s
	 * @return the double[]
	 */
	public static double[] scalarMult3(double[] a, double s) {
		double[] r = new double[3];
		r[0] = a[0] * s;
		r[1] = a[1] * s;
		r[2] = a[2] * s;
		return r;
	}

	/**
	 * Scalar mult3.
	 *
	 * @param a the a
	 * @param s the s
	 * @return the float[]
	 */
	public static float[] scalarMult3(float[] a, float s) {
		float[] r = new float[3];
		r[0] = a[0] * s;
		r[1] = a[1] * s;
		r[2] = a[2] * s;
		return r;
	}

	/**
	 * Translate.
	 *
	 * @param i the i
	 * @param maxVal the max val
	 * @return the float
	 */
	public static float translate(int i, int maxVal) {
		if (i < 0) {
			i = 0;
		}
		if (i >= maxVal) {
			i = maxVal - 1;
		}
		int d = maxVal / 2;
		return (float) (i - d) / d;
	}

	/**
	 * Unsigned byte to int.
	 *
	 * @param b the b
	 * @return the int
	 */
	public static int unsignedByteToInt(byte b) {
		return b & 0xff;
	}

	/**
	 * Vec sum3.
	 *
	 * @param a the a
	 * @param b the b
	 * @return the double[]
	 */
	public static double[] vecSum3(double[] a, double[] b) {
		return new double[] { a[0] + b[0], a[1] + b[1], a[2] + b[2] };
	}

	/**
	 * Vec sum3.
	 *
	 * @param a the a
	 * @param b the b
	 * @return the float[]
	 */
	public static float[] vecSum3(float[] a, float[] b) {
		return new float[] { a[0] + b[0], a[1] + b[1], a[2] + b[2] };
	}

	/**
	 * Prints the first20.
	 *
	 * @param a the a
	 */
	static void printFirst20(byte[] a) {
		System.out.print(a[0]);
		for (int i = 1; i < 20; i++) {
			System.out.print("," + a[i]);
		}
		System.out.println();
	}

	/**
	 * Prints the first20.
	 *
	 * @param a the a
	 */
	static void printFirst20(int[] a) {
		System.out.print(a[0]);
		for (int i = 1; i < 20; i++) {
			System.out.print("," + a[i]);
		}
		System.out.println();
	}

	/**
	 * Prints the first20.
	 *
	 * @param a the a
	 */
	static void printFirst20(short[] a) {
		System.out.print(a[0]);
		for (int i = 1; i < 20; i++) {
			System.out.print("," + a[i]);
		}
		System.out.println();
	}

}
