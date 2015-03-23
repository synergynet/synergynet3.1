/*
 * LRGBReader.java Created on July 3, 2004, 11:41 PM
 */

package jpview.io;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.imageio.ImageIO;

import jpview.Utils;
import jpview.ptms.LRGBPTM;
import jpview.ptms.PTM;

/**
 * @author Default
 */
public class JPEGLRGBReader implements PTMReader {

	/** The __in. */
	private InputStream __in = null;

	/** The debug. */
	private boolean debug = false;

	/** The reset. */
	private boolean reset = true; /* assume a complete input stream */

	/**
	 * Instantiates a new JPEGLRGB reader.
	 *
	 * @param inputStream the input stream
	 */
	public JPEGLRGBReader(InputStream inputStream) {
		__in = inputStream;
	}

	/**
	 * Combine.
	 *
	 * @param green the green
	 * @param _blue the _blue
	 * @return the int[]
	 */
	public static int[] combine(int[] green, byte[] _blue) {
		int[] blue = new int[_blue.length];
		for (int b = 0; b < blue.length; b++) {
			if (_blue[b] > 0) {
				blue[b] = (green[b] + _blue[b]) - 128;
			} else {
				blue[b] = green[b] + _blue[b] + 128;
			}
			if (blue[b] < 0) {
				blue[b] += 256;
			}
		}
		return blue;
	}

	/**
	 * Convert.
	 *
	 * @param a the a
	 * @return the int[]
	 */
	public static int[] convert(byte[] a) {
		int[] aa = new int[a.length];
		for (int i = 0; i < a.length; i++) {
			aa[i] = Utils.unsignedByteToInt(a[i]);
		}
		return aa;
	}

	/**
	 * Index of.
	 *
	 * @param question the question
	 * @param a the a
	 * @return the int
	 */
	public static int indexOf(int question, int[] a) {
		int answer = -1;
		for (int i = 0; i < a.length; i++) {
			if (a[i] == question) {
				answer = i;
			}
		}
		return answer;
	}

	/**
	 * Invert.
	 *
	 * @param source the source
	 * @return the buffered image
	 */
	public static BufferedImage invert(BufferedImage source) {
		BufferedImage target = new BufferedImage(source.getWidth(),
				source.getHeight(), source.getType());
		byte[] data = ((DataBufferByte) source.getRaster().getDataBuffer())
				.getData();
		byte[] targ = ((DataBufferByte) target.getRaster().getDataBuffer())
				.getData();
		for (int i = 0; i < data.length; i++) {
			targ[i] = (byte) ((255 - (data[i] + 128)) - 128);
		}
		return target;
	}

	/**
	 * Invert.
	 *
	 * @param source the source
	 * @return the int[]
	 */
	public static int[] invert(int[] source) {
		int[] target = new int[source.length];
		for (int i = 0; i < source.length; i++) {
			target[i] = ((255 - (source[i] + 128)) - 128);
		}
		return target;
	}

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		try {
			FileInputStream fis = new FileInputStream(new File(args[0]));
			JPEGLRGBReader me = new JPEGLRGBReader(fis);
			me.readPTM();
		} catch (Exception e) {
			e.printStackTrace();
		}
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

	/*
	 * (non-Javadoc)
	 * @see jpview.io.PTMReader#readPTM()
	 */
	public PTM readPTM() throws java.io.IOException {

		LRGBPTM ptm = new LRGBPTM();

		// try {
		if (reset) {
			String version = PTMIO.getLine(__in);
			debug("Version: " + version);
			String type = PTMIO.getLine(__in);
			debug("Type: " + type);
		}

		InputStream in = __in;

		int width = Integer.parseInt(PTMIO.getLine(in));
		int height = Integer.parseInt(PTMIO.getLine(in));

		debug("width: " + width);
		debug("height: " + height);

		/* scale */
		String[] sa = PTMIO.getLine(in).split(" ");
		float[] scale = new float[sa.length];
		for (int i = 0; i < sa.length; i++) {
			scale[i] = Float.parseFloat(sa[i]);
		}

		/* bias */
		sa = PTMIO.getLine(in).split(" ");
		int[] bias = new int[sa.length];
		for (int i = 0; i < sa.length; i++) {
			bias[i] = Integer.parseInt(sa[i]);
		}

		// int compressionParameter = Integer.parseInt(PTMIO.getLine(in));

		sa = PTMIO.getLine(in).split(" ");
		int[] xforms = new int[sa.length];
		for (int i = 0; i < sa.length; i++) {
			xforms[i] = Integer.parseInt(sa[i]);
		}

		/** TODO throw an error or handle motion vectors */
		PTMIO.getLine(in); /* ignore motion vectors */
		PTMIO.getLine(in); /* ignore more motion vectors */

		sa = PTMIO.getLine(in).split(" ");
		int[] order = new int[sa.length];
		for (int i = 0; i < sa.length; i++) {
			order[i] = Integer.parseInt(sa[i]);
		}

		sa = PTMIO.getLine(in).split(" ");
		int[] referencePlane = new int[sa.length];
		for (int i = 0; i < sa.length; i++) {
			referencePlane[i] = Integer.parseInt(sa[i]);
		}

		sa = PTMIO.getLine(in).split(" ");
		int[] compressedSize = new int[sa.length];
		for (int i = 0; i < sa.length; i++) {
			compressedSize[i] = Integer.parseInt(sa[i]);
		}

		sa = PTMIO.getLine(in).split(" ");
		int[] sideInformation = new int[sa.length];
		for (int i = 0; i < sa.length; i++) {
			sideInformation[i] = Integer.parseInt(sa[i]);
		}

		// BufferedImage[] plane = new BufferedImage[compressedSize.length];
		ByteArrayInputStream[] info = new ByteArrayInputStream[sideInformation.length];

		byte[][] bufs = new byte[9][];

		for (int i = 0; i < compressedSize.length; i++) {

			/**
			 * Read the plane
			 */
			byte[] buf = new byte[compressedSize[i]];
			/* buffering is unreliable - do this byte by byte */
			for (int b = 0; b < buf.length; b++) {
				buf[b] = (byte) in.read();
			}

			ByteArrayInputStream bais = new ByteArrayInputStream(buf);
			BufferedImage bufferedImage = ImageIO.read(bais);
			bais.close();

			/* re-orient */
			AffineTransform tx = AffineTransform.getScaleInstance(1, -1);
			tx.translate(0, -height);
			AffineTransformOp op = new AffineTransformOp(tx,
					AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
			bufferedImage = op.filter(bufferedImage, null);
			bufs[i] = ((DataBufferByte) bufferedImage.getRaster()
					.getDataBuffer()).getData();

			/**
			 * read the side info
			 */

			byte[] buf2 = new byte[sideInformation[i]];
			for (int b = 0; b < buf2.length; b++) {
				buf2[b] = (byte) in.read();
			}
			info[i] = new ByteArrayInputStream(buf2);
		}

		int[][] coef = new int[9][];

		for (int i = 0; i < 9; i++) {
			int index = indexOf(i, order);
			debug("index: " + index);
			if (referencePlane[index] < 0) {
				debug(" > ref plane");
				coef[index] = convert(bufs[index]);
			} else if (xforms[index] == 0) {
				debug(" > ref combine w/" + referencePlane[index]);
				coef[index] = combine(coef[referencePlane[index]], bufs[index]);
			} else if (xforms[index] == 1) {
				debug(" > ref invert+combine w/" + referencePlane[index]);
				coef[index] = combine(invert(coef[referencePlane[index]]),
						bufs[index]);
			} else {
				System.out.println("Error, unhandled format");
				return null;
			}

			correctCoeff(coef[index], info[index], width, height);
			// coef[index] = this.flip(coef[index],width,height);

		}

		ptm.setWidth(width);
		ptm.setHeight(height);
		int[][] coefficients = new int[6][width * height];
		for (int h = height - 1; h >= 0; h--) {
			for (int w = 0; w < ptm.getWidth(); w++) {
				int off = (ptm.getWidth() * h) + w;
				coefficients[0][off] = (int) PTMIO.cFinal(coef[0][off],
						bias[0], scale[0]);
				coefficients[1][off] = (int) PTMIO.cFinal(coef[1][off],
						bias[1], scale[1]);
				coefficients[2][off] = (int) PTMIO.cFinal(coef[2][off],
						bias[2], scale[2]);
				coefficients[3][off] = (int) PTMIO.cFinal(coef[3][off],
						bias[3], scale[3]);
				coefficients[4][off] = (int) PTMIO.cFinal(coef[4][off],
						bias[4], scale[4]);
				coefficients[5][off] = (int) PTMIO.cFinal(coef[5][off],
						bias[5], scale[5]);
			}
		}
		ptm.setA0(coefficients[0]);
		ptm.setA1(coefficients[1]);
		ptm.setA2(coefficients[2]);
		ptm.setA3(coefficients[3]);
		ptm.setA4(coefficients[4]);
		ptm.setA5(coefficients[5]);

		int[] red = coef[6];
		int[] green = coef[7];
		int[] blue = coef[8];

		/* pixels */
		int[] rgb = new int[width * height];
		int rr = 0, gg = 0, bb = 0;
		for (int h = height - 1; h >= 0; h--) {
			for (int w = 0; w < width; w++) {
				int off = (ptm.getWidth() * h) + w;
				rr = red[off] & 0xff;
				gg = green[off] & 0xff;
				bb = blue[off] & 0xff;
				rgb[(h * width) + w] = (rr << 16) | (gg << 8) | bb;
			}
		}

		bufs = null;
		coef = null;
		System.gc();
		ptm.setRGB(rgb);

		// } catch ( Exception e ) {
		// e.printStackTrace();
		// }

		ptm.computeNormals();

		return ptm;

	}

	/**
	 * Correct coeff.
	 *
	 * @param c the c
	 * @param b the b
	 * @param w the w
	 * @param h the h
	 */
	private void correctCoeff(int[] c, ByteArrayInputStream b, int w, int h) {
		while (b.available() > 0) {
			int p3 = b.read();
			int p2 = b.read();
			int p1 = b.read();
			int p0 = b.read();
			int v = b.read();
			int idx = (p3 << 24) | (p2 << 16) | (p1 << 8) | p0;
			int width = idx % w;
			int height = idx / w;
			int h2 = h - height - 1;
			int idx2 = (h2 * w) + width;
			c[idx2] = v;
		}
	}

	/**
	 * Debug.
	 *
	 * @param s the s
	 */
	private void debug(String s) {
		if (debug) {
			System.out.println(s);
		}
	}

	/**
	 * Reset.
	 *
	 * @param b the b
	 */
	protected void reset(boolean b) {
		reset = b;
	}

	// private int[] flip(int[] in, int w, int h) {
	// /* for some reason, this doesn't get everything... */
	// int[] out = new int[w * h];
	// for (int i = 0; i < w; i++) {
	// for (int j = 0; j < h; j++) {
	// int orig = j * w + i;
	// int trans = (h - j - 1) * w + i;
	// out[trans] = in[orig];
	// }
	// }
	// return out;
	// }
}
