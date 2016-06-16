/*
 * EnvironmentMap.java Created on September 11, 2004, 11:09 PM
 */

package jpview.graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JComponent;

import jpview.Utils;
import jpview.ptms.PTM;

/**
 * The environment map class stores the environment images and provides a method
 * to rotate the map
 *
 * @author clyon
 */
public class EnvironmentMap
{

	/**
	 * The Class Monitor.
	 */
	class Monitor extends JComponent
	{

		/**
		 *
		 */
		private static final long serialVersionUID = 1290062082051184463L;

		/** The hits. */
		Point[] hits = null;

		/*
		 * (non-Javadoc)
		 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
		 */
		@Override
		public void paintComponent(Graphics g)
		{
			Rectangle r = this.getBounds();
			Graphics2D g2d = (Graphics2D) g;
			if (buf != null)
			{
				g.drawImage(Utils.createBufferedImage(buf, width), 0, 0, r.width, r.height, this);
			}
			if (hits != null)
			{
				g2d.setColor(Color.YELLOW);

				for (int i = 0; i < hits.length; i++)
				{
					g2d.drawRect((hits[i].x * r.width) / width, (hits[i].y * r.height) / height, 1, 1);
				}
			}
		}

		/**
		 * Sets the hits.
		 *
		 * @param p
		 *            the new hits
		 */
		public void setHits(Point[] p)
		{
			hits = p;
		}
	}

	/**
	 * constant indicating a gaussian blur
	 */
	public static final int BLUR_TYPE_GAUSSIAN = 1;

	/**
	 * constant indicating no blur
	 */
	public static final int BLUR_TYPE_NONE = -1;

	// private int[] lookedup = null;

	/**
	 * constant indicating simple blur (no weighting)
	 */
	public static final int BLUR_TYPE_SIMPLE = 0;

	/**
	 * default setting for downsampling the map
	 */
	public static final int DEFAULT_DOWNSAMPLE = 2;

	/**
	 * default gaussian kernel size
	 */
	public static final int DEFAULT_KERNEL_SIZE = 18;

	/**
	 * the default sigma setting for the gaussian kernel
	 */
	public static final float DEFAULT_GAUSSIAN_SIGMA = ((float) DEFAULT_KERNEL_SIZE) / 4;

	/** The eye. */
	private static Vec3f eye = new Vec3f(0, 0, 1);

	/**
	 * The maximum downsample size, determined by the environment map image
	 */
	public int MAX_DOWNSAMPLE_SZ = -1;

	/** The __pixels. */
	private int[] __pixels = null;

	/** The blur type. */
	private int blurType = BLUR_TYPE_GAUSSIAN;

	/** The buf. */
	private int[] buf = null;

	/** The cos. */
	private double[] cos = new double[360];

	/** The down sample. */
	private int downSample = 2;

	/** The gaussian sigma. */
	private float gaussianSigma = DEFAULT_GAUSSIAN_SIGMA;

	/** The height. */
	private int height = 0;

	/** The kernel size. */
	private int kernelSize = DEFAULT_KERNEL_SIZE;

	/** The m. */
	private Monitor m;

	/** The original. */
	private BufferedImage original = null;

	/** The sin. */
	private double[] sin = new double[360];

	/** The theta. */
	private int theta = 0;
	/** The width. */
	private int width = 0;;

	{
		for (int i = 0; i < cos.length; i++)
		{
			cos[i] = Math.cos(Math.toRadians(i));
			sin[i] = Math.sin(Math.toRadians(i));
		}
	}

	/**
	 * Creates a new instance of EnvironmentMap
	 *
	 * @param source
	 *            the sphere map image
	 * @param ptm
	 *            the parent PTM to be mapped
	 */
	public EnvironmentMap(BufferedImage source, PTM ptm)
	{

		if (source == null)
		{
			return;
		}

		/**
		 * Keep a copy for later
		 */
		original = new BufferedImage(source.getWidth(), source.getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D) original.getGraphics();
		g.drawImage(source, 0, 0, source.getWidth(), source.getHeight(), null);
		this.MAX_DOWNSAMPLE_SZ = Math.min(original.getWidth() / 16, original.getHeight() / 16);

		refresh();

		// JFrame f = new JFrame("map");
		// m = new Monitor();
		// m.setSize(width,height);
		// m.setPreferredSize(new Dimension(width*2,height*2));
		// f.getContentPane().add(m);
		// f.pack();
		// f.setVisible(true);
		// f.show();

	}

	/**
	 * Gets the down sample.
	 *
	 * @return the down sample
	 */
	public int getDownSample()
	{
		return downSample;
	}

	/**
	 * returns the height of the environment map
	 *
	 * @return the height of the environment map
	 */
	public int getHeight()
	{
		return height;
	}

	/**
	 * Returns the sphere map image used for the environment map
	 *
	 * @return the image used for the environment map
	 */
	public BufferedImage getImage()
	{
		return original;
	}

	/**
	 * Returns the pixel value indexed by the provided normal in a flat (1D)
	 * pixel buffer
	 *
	 * @param normal
	 *            a normal from the PTM surface
	 * @return the index of a pixel in a flat buffer indexed by the provided
	 *         normal
	 */
	public int getMapIndex(Vec3f normal)
	{
		Vec3f R = Vec3f.reflect(normal, eye);
		R.normalize();
		// float m = (float) Math.sqrt(2*(R.z()+1)); /* less standard */
		float m = (float) (Math.sqrt(sqr(R.x()) + sqr(R.y()) + sqr(R.z() + 1)));
		float u = R.x() / m; /* mirror */
		float v = -R.y() / m;
		int u1 = Math.max(Math.min(Math.round(((u + 1) * width) / 2), width - 1), 0);
		int v1 = Math.max(Math.min(Math.round(((v + 1) * height) / 2), height - 1), 0);
		return (v1 * width) + u1;
	};

	/**
	 * Returns the current amount by which the map can be downsampled
	 *
	 * @return maximum amount by which this map can be downsampled
	 */
	public int getMaxDownsample()
	{
		return this.MAX_DOWNSAMPLE_SZ;
	}

	/**
	 * Returns the pixel indexed by light reflected by the provided normal
	 *
	 * @param normal
	 *            the normal on the PTM
	 * @return the pixel indexed by the provided normal
	 */
	public int getPixel(Vec3f normal)
	{
		Vec3f R = Vec3f.reflect(normal, eye);
		R.normalize();
		// float m = (float) Math.sqrt(2*(R.z()+1));
		float m = (float) (Math.sqrt(sqr(R.x()) + sqr(R.y()) + sqr(R.z() + 1)));
		float u = R.x() / m; /* mirror */
		float v = -R.y() / m;

		int u1 = Math.max(Math.min(Math.round(((u + 1) * width) / 2), width - 1), 0);
		int v1 = Math.max(Math.min(Math.round(((v + 1) * height) / 2), height - 1), 0);

		// if ( theta > 0 ) {
		//
		// int s = u1 - width/2;
		// int t = v1 - height/2;
		// int ss = (int) Math.round( s*cos[theta] + t*sin[theta]);
		// int tt = (int) Math.round( t*cos[theta] - s*sin[theta]);
		// ss += width/2;
		// tt += height/2;
		// u1 = ss;
		// v1 = tt;
		// }

		if (u1 < 0)
		{
			u1 = 0;
		}
		if (v1 < 0)
		{
			v1 = 0;
		}
		if (u1 >= width)
		{
			u1 = width - 1;
		}
		if (v1 >= height)
		{
			v1 = height - 1;
		}
		return __pixels[(v1 * width) + u1];

	}

	/**
	 * Returns the point on a 2D plane indexed by the provided normal
	 *
	 * @param normal
	 *            normal from the PTM
	 * @return The point on the map indexed by the provided normal
	 */
	public Point getPosition(Vec3f normal)
	{
		if (normal == null)
		{
			return null;
		}

		Vec3f R = Vec3f.reflect(normal, eye);
		R.normalize();
		// float m = (float) Math.sqrt(2*(R.z()+1));
		float m = (float) (Math.sqrt(sqr(R.x()) + sqr(R.y()) + sqr(R.z() + 1)));
		float u = R.x() / m; /* mirror */
		float v = -R.y() / m;
		int u1 = Math.max(Math.min(Math.round(((u + 1) * width) / 2), width - 1), 0);
		int v1 = Math.max(Math.min(Math.round(((v + 1) * height) / 2), height - 1), 0);
		return new Point(u1, v1);
	}

	/**
	 * Returns the width of the PTM
	 *
	 * @return the width of the environment map
	 */
	public int getWidth()
	{
		return width;
	}

	/**
	 * Refreshes the sampling and blur routines
	 */
	public void refresh()
	{

		if (original == null)
		{
			System.out.println("Original was null");
			return;
		}

		/**
		 * Create a buffer for the Environment map, at image size
		 */
		width = original.getWidth() / downSample;
		height = original.getHeight() / downSample;
		// int length = width * height;

		/**
		 * Create application compatible image
		 */
		BufferedImage imageTmp = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D) imageTmp.getGraphics();
		g.drawImage(original, 0, 0, width, height, null);
		BufferedImage image = null;

		switch (blurType)
		{
			case EnvironmentMap.BLUR_TYPE_SIMPLE:
				image = Utils.blurImageSimple(imageTmp, kernelSize);
				break;
			case EnvironmentMap.BLUR_TYPE_GAUSSIAN:
				image = Utils.gaussianBlur(imageTmp, kernelSize, gaussianSigma);
				break;
			case EnvironmentMap.BLUR_TYPE_NONE:
			default:
				image = imageTmp;
		}
		__pixels = null;
		__pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
		// lookedup = new int[__pixels.length];
	}

	/**
	 * free all storage used by this class
	 */
	public void release()
	{
		buf = null;
		// lookedup = null;
		__pixels = null;
		original = null;
		m = null;
		cos = null;
		sin = null;
	}

	/**
	 * Returns a pixel buffer of the rotated map
	 *
	 * @return A buffer of the rotated pixels
	 */
	public int[] rotatedMap()
	{
		int radius = Math.min(width / 2, height / 2);
		buf = new int[__pixels.length];
		for (int i = 0; i < width; i++)
		{
			for (int j = 0; j < height; j++)
			{
				int offset = (j * width) + i;
				int x = i - (width / 2);
				int y = j - (height / 2);
				double d = Math.sqrt((x * x) + (y * y));
				if (d > radius)
				{
					buf[offset] = __pixels[offset];
				}
				else
				{
					int rx = (int) Math.round((x * cos[theta]) + (y * sin[theta]));
					int ry = (int) Math.round((y * cos[theta]) - (x * sin[theta]));
					rx += width / 2;
					ry += height / 2;
					if (rx < 0)
					{
						rx = 0;
					}
					if (ry < 0)
					{
						ry = 0;
					}
					if (rx > (width - 1))
					{
						rx = width - 1;
					}
					if (ry > (width - 1))
					{
						ry = height - 1;
					}
					buf[offset] = __pixels[(ry * width) + rx];
				}
			}
		}
		return buf;
	}

	/**
	 * Sets the angle for rotation of the map
	 *
	 * @param degrees
	 *            angle in degrees to rotate the map
	 */
	public void setAngle(int degrees)
	{
		theta = degrees;
	}

	/**
	 * Sets the kernel size for the gaussian blur
	 *
	 * @param i
	 *            sets the blur kernel size
	 */
	public void setBlurKernelSize(int i)
	{
		this.kernelSize = i;
	}

	/**
	 * Set the blur type to gaussian blur
	 */
	public void setGaussianBlur()
	{
		this.blurType = EnvironmentMap.BLUR_TYPE_GAUSSIAN;
	}

	/**
	 * Sets the sigma value for the gaussian blur algorithm
	 *
	 * @param f
	 *            the sigma value for the gaussian blur
	 */
	public void setGaussianSigma(float f)
	{
		this.gaussianSigma = f;
	}

	/**
	 * Set the sphere map image to be used
	 *
	 * @param source
	 *            the sphere map image
	 */
	public void setImage(BufferedImage source)
	{
		if (source == null)
		{
			original = new BufferedImage(original.getWidth(), original.getHeight(), BufferedImage.TYPE_INT_RGB);
			Graphics2D g = (Graphics2D) original.getGraphics();
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, original.getWidth(), original.getHeight());
		}
		else
		{
			original = new BufferedImage(source.getWidth(), source.getHeight(), BufferedImage.TYPE_INT_RGB);
			Graphics2D g = (Graphics2D) original.getGraphics();
			g.drawImage(source, 0, 0, source.getWidth(), source.getHeight(), null);
		}
		this.MAX_DOWNSAMPLE_SZ = Math.min(original.getWidth() / 16, original.getHeight() / 16);
		// refresh();
		this.refresh();
		this.setAngle(0);
		rotatedMap();
	}

	/**
	 * Turn off blurring
	 */
	public void setNoBlur()
	{
		this.blurType = EnvironmentMap.BLUR_TYPE_NONE;
	}

	/**
	 * Sets the downsample size for the map
	 *
	 * @param i
	 *            the downsample size
	 */
	public void setSampleSize(int i)
	{
		this.downSample = i;
	}

	/**
	 * Set the blur type to a simple blur (no weights)
	 */
	public void setSimpleBlur()
	{
		this.blurType = EnvironmentMap.BLUR_TYPE_SIMPLE;
	}

	/**
	 * Updates the local monitor (for debugging)
	 *
	 * @param normals
	 *            list of normals to highlight for debugging
	 */
	public void updateMonitor(Vec3f[] normals)
	{
		Point[] p = new Point[normals.length];
		for (int i = 0; i < normals.length; i++)
		{
			p[i] = getPosition(normals[i]);
		}
		m.setHits(p);
		m.repaint();
	}

	/**
	 * Sqr.
	 *
	 * @param f
	 *            the f
	 * @return the float
	 */
	private float sqr(float f)
	{
		return f * f;
	}
}
