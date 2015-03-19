package jpview.io;

import java.io.InputStream;

/*
 *  PTMIO.java
 *
 * Created on July 3, 2004, 11:22 PM
 */

/**
 * 
 * @author Default
 */
public abstract class PTMIO {

	private static final int BUFSIZ = 2048;

	public static float cFinal(int cRaw, int bias, float scale) {
		return (cRaw - bias) * scale;
	}

	public static int cFinalInt(int cRaw, int bias, float scale) {
		return Math.round((cRaw - bias) * scale);
	}

	public static PTMReader getPTMParser(InputStream in) throws Exception {

		try {
			in.mark(BUFSIZ);
			String version = PTMIO.getLine(in);
			String type = PTMIO.getLine(in);

			if (type.equals("PTM_FORMAT_LRGB")) {
				boolean reset = true;
				try {
					in.reset();
				} catch (java.io.IOException e) {
					reset = false;
				}
				LRGBReader r = new LRGBReader(in);
				r.setVersion(version);
				r.reset(reset);
				return r;
			} else if (type.equals("PTM_FORMAT_JPEG_LRGB")) {
				boolean reset = true;
				try {
					in.reset();
				} catch (java.io.IOException e) {
					reset = false;
				}
				JPEGLRGBReader r = new JPEGLRGBReader(in);
				r.reset(reset);
				return r;
			} else if (type.equals("PTM_FORMAT_RGB")) {
				boolean reset = true;
				try {
					in.reset();
				} catch (java.io.IOException e) {
					reset = false;
				}
				RGBReader r = new RGBReader(in);
				r.reset(reset);
				return r;
			} else {
				throw new Exception("Unsupported Type: " + type);
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

		// return null; /* never reached */

	}

	/**
	 * Read a line of text. Add EOF check, or use buffered stream.
	 */
	public static String getLine(InputStream in) {
		int b;
		StringBuffer buf = new StringBuffer();

		try {
			while ((b = in.read()) != '\n') {
				buf.append((char) b);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new String(buf);
	}
}
