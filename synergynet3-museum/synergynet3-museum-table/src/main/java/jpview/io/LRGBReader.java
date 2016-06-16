/*
 * LRGBReader.java Created on July 3, 2004, 11:41 PM
 */

package jpview.io;

// import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
// import java.io.InputStreamReader;

import jpview.Utils;
import jpview.ptms.LRGBPTM;
import jpview.ptms.PTM;

/**
 * @author Default
 */
public class LRGBReader implements PTMReader
{

	/** The __in. */
	private InputStream __in;

	/** The debug. */
	private boolean DEBUG = false;

	/** The ptm. */
	private LRGBPTM ptm;

	/** The reset. */
	private boolean reset = true; /* assume a complete input stream */

	/** The version. */
	private String version = null;

	/** Creates a new instance of LRGBReader */
	public LRGBReader(InputStream in)
	{
		__in = in;
	}

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 */
	public static void main(String args[])
	{
		try
		{
			LRGBReader me = new LRGBReader(new FileInputStream(new File(args[0])));
			me.readPTM();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see jpview.io.PTMReader#readPTM()
	 */
	@Override
	public PTM readPTM() throws java.io.IOException
	{

		ptm = new LRGBPTM();

		// try {

		if (reset)
		{
			version = PTMIO.getLine(__in);
			debug("Version: " + version);
			String type = PTMIO.getLine(__in);
			debug("Type: " + type);
		}

		/* read headers from this stream */
		// BufferedReader reader = new BufferedReader(new
		// InputStreamReader(__in));

		/* dimensions */
		ptm.setWidth(Integer.parseInt(PTMIO.getLine(__in)));
		ptm.setHeight(Integer.parseInt(PTMIO.getLine(__in)));

		debug("Width: " + ptm.getWidth());
		debug("Height: " + ptm.getHeight());

		String[] sa;

		/* scale */
		sa = PTMIO.getLine(__in).split(" ");
		float[] scale = new float[sa.length];
		for (int i = 0; i < sa.length; i++)
		{
			scale[i] = Float.parseFloat(sa[i]);
		}

		debug("Scale: " + Utils.asString(scale));

		/* bias */
		sa = PTMIO.getLine(__in).split(" ");
		int[] bias = new int[sa.length];
		for (int i = 0; i < sa.length; i++)
		{
			bias[i] = Integer.parseInt(sa[i]);
		}

		debug("Bias: " + Utils.asString(bias));

		int[][] tmp = new int[6][ptm.getWidth() * ptm.getHeight()];

		/* pixels */
		int BUFSIZ = ptm.getWidth() * ptm.getHeight();
		int[] rgb = new int[BUFSIZ];
		int r, g, b, offset;

		/* coefficients */
		for (int h = ptm.getHeight() - 1; h >= 0; h--)
		{
			for (int w = 0; w < ptm.getWidth(); w++)
			{
				offset = (h * ptm.getWidth()) + w;
				int[] raw = new int[6];
				for (int i = 0; i < 6; i++)
				{
					int c = __in.read();
					raw[i] = c;
					tmp[i][offset] = (int) PTMIO.cFinal(c, bias[i], scale[i]);
				}
				/* do it now */
				if (version.equals("PTM_1.1"))
				{
					r = __in.read() & 0xff;
					g = __in.read() & 0xff;
					b = __in.read() & 0xff;
					rgb[offset] = (r << 16) | (g << 8) | b;
				}
			}
		}

		ptm.setA0(tmp[0]);
		ptm.setA1(tmp[1]);
		ptm.setA2(tmp[2]);
		ptm.setA3(tmp[3]);
		ptm.setA4(tmp[4]);
		ptm.setA5(tmp[5]);

		// for ( int j = 0; j < 6; j++ ) {
		// BufferedImage image =
		// new BufferedImage( ptm.getWidth(), ptm.getHeight(),
		// BufferedImage.TYPE_INT_RGB);
		// int [] pixels =
		// ((DataBufferInt)((BufferedImage)image).getRaster().getDataBuffer()).getData();
		// for ( int i = 0; i < pixels.length; i++ ) {
		// int v = Utils.clamp(tmp[j][i]);
		// pixels[i] = v << 16 | v << 8 | v;
		// }
		// JFrame f = new JFrame( "Coeff: " + j );
		// f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// f.getContentPane().add(new ImageScreen(image));
		// f.pack();
		// f.setSize(image.getWidth(),image.getHeight());
		// f.setVisible(true);
		// }

		if (version.equals("PTM_1.2"))
		{
			for (int h = ptm.getHeight() - 1; h >= 0; h--)
			{
				for (int w = 0; w < ptm.getWidth(); w++)
				{
					offset = (h * ptm.getWidth()) + w;
					r = __in.read() & 0xff;
					g = __in.read() & 0xff;
					b = __in.read() & 0xff;
					rgb[offset] = (r << 16) | (g << 8) | b;
				}
			}
		}

		// BufferedImage image =
		// new BufferedImage( ptm.getWidth(), ptm.getHeight(),
		// BufferedImage.TYPE_INT_RGB);
		// int [] pixels =
		// ((DataBufferInt)((BufferedImage)image).getRaster().getDataBuffer()).getData();
		// for ( int i = 0; i < pixels.length; i++ ) {
		// pixels[i] = rgb[i];
		// }
		// JFrame f = new JFrame();
		// f.setTitle("RGB");
		// f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// f.getContentPane().add(new ImageScreen(image));
		// f.pack();
		// f.setSize(image.getWidth(),image.getHeight());
		// f.setVisible(true);

		ptm.setRGB(rgb);
		// }
		// catch ( Exception e ) {
		// e.printStackTrace();
		// }

		ptm.computeNormals();
		return ptm;

	}

	/**
	 * Sets the debug.
	 *
	 * @param b
	 *            the new debug
	 */
	public void setDebug(boolean b)
	{
		DEBUG = b;
	}

	/**
	 * Sets the version.
	 *
	 * @param s
	 *            the new version
	 */
	public void setVersion(String s)
	{
		version = s;
	}

	/**
	 * Debug.
	 *
	 * @param s
	 *            the s
	 */
	private void debug(String s)
	{
		if (DEBUG)
		{
			System.out.println(s);
		}
	}

	/**
	 * Reset.
	 *
	 * @param b
	 *            the b
	 */
	protected void reset(boolean b)
	{
		reset = b;
	}

}
