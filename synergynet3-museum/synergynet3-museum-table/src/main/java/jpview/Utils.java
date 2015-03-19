/*
 * Utils.java
 *
 * Created on July 24, 2004, 5:12 PM
 */

package jpview;

import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.DataBufferInt;
import java.awt.image.Kernel;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Arrays;

import javax.imageio.ImageIO;

import jpview.graphics.GaussianKernel;
import jpview.graphics.Vec3f;

/**
 * 
 * @author Default
 */
public class Utils {
	static void printFirst20(byte[] a) {
		System.out.print(a[0]);
		for (int i = 1; i < 20; i++) {
			System.out.print("," + a[i]);
		}
		System.out.println();
	}

	static void printFirst20(int[] a) {
		System.out.print(a[0]);
		for (int i = 1; i < 20; i++) {
			System.out.print("," + a[i]);
		}
		System.out.println();
	}

	static void printFirst20(short[] a) {
		System.out.print(a[0]);
		for (int i = 1; i < 20; i++) {
			System.out.print("," + a[i]);
		}
		System.out.println();
	}

	public static int indexOfMax(double[] a) {
		double max = a[0];
		int idx = 0;
		for (int i = 0; i < a.length; i++)
			if (a[i] > max) {
				max = a[i];
				idx = i;
			}
		return idx;
	}

	/** clamp to 8-bit int */
	public static int clamp(int i) {
		if (i < 0)
			return 0;
		if (i > 255)
			return 255;
		return i;
	}

	public static float[] normalize3(float[] a) {
		float len = (float) Math.sqrt(a[0] * a[0] + a[1] * a[1] + a[2] * a[2]);
		return new float[] { a[0] / len, a[1] / len, a[2] / len };
	}

	public static double[] normalize3(double[] a) {
		double len = Math.sqrt(a[0] * a[0] + a[1] * a[1] + a[2] * a[2]);
		return new double[] { a[0] / len, a[1] / len, a[2] / len };
	}

	public static float[] normalize(float x, float y, float z) {
		float len = (float) Math.sqrt(x * x + y * y + z * z);
		return new float[] { x / len, y / len, z / len };
	}

	public static String asString(double[] a) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < a.length; i++) {
			if (i > 0)
				sb.append(", ");
			sb.append(a[i]);
		}
		return new String(sb);
	}

	public static String asString(float[] a) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < a.length; i++) {
			if (i > 0)
				sb.append(", ");
			sb.append(a[i]);
		}
		return new String(sb);
	}

	public static String asString(int[] a) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < a.length; i++) {
			if (i > 0)
				sb.append(", ");
			sb.append(a[i]);
		}
		return new String(sb);
	}

	public static float translate(int i, int maxVal) {
		if (i < 0)
			i = 0;
		if (i >= maxVal)
			i = maxVal - 1;
		int d = maxVal / 2;
		return (float) (i - d) / d;
	}

	public static int unsignedByteToInt(byte b) {
		return (int) b & 0xff;
	}

	public static float[] reflect(float[] normal, float[] incedent) {
		// I - 2.0 * N * dot(N, I);
		// R = 2 * N.I * N - I
		float t1 = dot3(normal, incedent) * 2f;
		float[] n1 = scalarMult3(normal, t1);
		return normalize3(vecSum3(scalarMult3(incedent, -1), n1));
	}

	public static double[] reflect(double[] normal, double[] incedent) {
		// I - 2.0 * N * dot(N, I);
		// R = V - ( 2 * V [dot] N ) N
		double t1 = dot3(normal, incedent) * 2f;
		double[] n1 = scalarMult3(normal, t1);
		return normalize3(vecSum3(scalarMult3(incedent, -1), n1));
	}

	public static float[] vecSum3(float[] a, float[] b) {
		return new float[] { a[0] + b[0], a[1] + b[1], a[2] + b[2] };
	}

	public static double[] vecSum3(double[] a, double[] b) {
		return new double[] { a[0] + b[0], a[1] + b[1], a[2] + b[2] };
	}

	public static float[] scalarMult3(float[] a, float s) {
		float[] r = new float[3];
		r[0] = a[0] * s;
		r[1] = a[1] * s;
		r[2] = a[2] * s;
		return r;
	}

	public static double[] scalarMult3(double[] a, double s) {
		double[] r = new double[3];
		r[0] = a[0] * s;
		r[1] = a[1] * s;
		r[2] = a[2] * s;
		return r;
	}

	public static float dot3(float[] a, float[] b) {
		return (a[0] * b[0] + a[1] * b[1] + a[2] * b[2]);
	}

	public static double dot3(double[] a, double[] b) {
		return (a[0] * b[0] + a[1] * b[1] + a[2] * b[2]);
	}

	public static BufferedImage readUnbuffered(InputStream is)
			throws java.io.IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int b;
		while ((b = is.read()) != -1)
			baos.write(b);
		ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
		return ImageIO.read(bais);
	}

	public static int[] grabPixels(BufferedImage image) {
		return ((DataBufferInt) ((BufferedImage) image).getRaster()
				.getDataBuffer()).getData();
	}

	public static BufferedImage gaussianBlur(BufferedImage image, int kSize,
			float sigma) {
		BufferedImage target = new BufferedImage(image.getWidth(), image
				.getHeight(), BufferedImage.TYPE_INT_RGB);
		Kernel kernel = new Kernel(kSize, kSize, new GaussianKernel(kSize,
				sigma).getKernel());
		ConvolveOp cop = new ConvolveOp(kernel);
		cop.filter(image, target);
		return target;
	}

	public static BufferedImage blurImageSimple(BufferedImage image, int kSize) {
		float patchval = ((float) 1) / (kSize * kSize);
		float[] k = new float[kSize * kSize];
		Arrays.fill(k, patchval);
		BufferedImage target = new BufferedImage(image.getWidth(), image
				.getHeight(), BufferedImage.TYPE_INT_RGB);
		Kernel kernel = new Kernel(kSize, kSize, k);
		ConvolveOp cop = new ConvolveOp(kernel);
		cop.filter(image, target);
		return target;
	}

	public static BufferedImage createBufferedImage(int[] pixels, int scanline) {
		BufferedImage bi = new BufferedImage(scanline,
				pixels.length / scanline, BufferedImage.TYPE_INT_RGB);
		int[] p = Utils.grabPixels(bi);
		for (int i = 0; i < p.length; i++)
			p[i] = pixels[i];
		return bi;
	}

	public static BufferedImage createBufferedImage(Vec3f[] pixels, int scanline) {
		BufferedImage bi = new BufferedImage(scanline,
				pixels.length / scanline, BufferedImage.TYPE_INT_RGB);
		int[] p = Utils.grabPixels(bi);
		for (int i = 0; i < p.length; i++)
			p[i] = pixels[i].toPixel();
		return bi;
	}

	public static Vec3f[] intToVec(int[] a) {
		Vec3f[] v = new Vec3f[a.length];
		for (int i = 0; i < v.length; i++)
			v[i] = Vec3f.convertPixel(a[i]);
		return v;
	}

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

}
