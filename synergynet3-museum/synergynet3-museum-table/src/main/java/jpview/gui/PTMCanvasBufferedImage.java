/*
 * PTMCanvas.java Created on July 20, 2004, 10:26 PM
 */

package jpview.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

// import javax.swing.JScrollPane;

/**
 * @author Default
 */
public final class PTMCanvasBufferedImage extends PTMCanvas {
	/**
	 *
	 */
	private static final long serialVersionUID = -2468135368246948618L;

	/** The cached height. */
	private int cachedWidth = 0, cachedHeight = 0;

	/** The max height. */
	private int maxHeight = -1;

	// private JScrollPane pane = null;

	/** The max width. */
	private int maxWidth = -1;

	/** The image. */
	protected BufferedImage image;

	/** The ratio. */
	protected float ratio = 0;

	/** Creates a new instance of PTMCanvas */
	public PTMCanvasBufferedImage(int width, int height) {
		desiredWidth = width;
		desiredHeight = height;
		cachedWidth = width;
		cachedHeight = height;
		displayWidth = width;
		displayHeight = height;
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
		ratio = ((float) image.getWidth()) / image.getHeight();
		maxWidth = (int) (width * 1.3);
		maxHeight = (int) (height * 1.3);
	}

	/*
	 * (non-Javadoc)
	 * @see jpview.gui.PTMCanvas#detail()
	 */
	public void detail() {
		isFast = false;
		int w = cachedWidth;
		int h = cachedHeight;
		image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
	}

	/**
	 * Dump avg values.
	 */
	public void dumpAvgValues() {
		/*
		 * int r=0, g=0, b=0; for ( int i = 0; i < pixels.length; i++ ) { r += (
		 * ( pixels[ i ] >> 16 ) & 0xff ); g += ( ( pixels[ i ] >> 8 ) & 0xff );
		 * b += ( ( pixels[ i ] ) & 0xff ); } System.out.println ( "Red: " +
		 * ((float) r) / pixels.length ); System.out.println ( "Grn: " +
		 * ((float) g) / pixels.length ); System.out.println ( "Blu: " +
		 * ((float) b) / pixels.length );
		 */
	}

	/**
	 * Gets the image.
	 *
	 * @return the image
	 */
	public BufferedImage getImage() {
		return image;
	}

	/*
	 * (non-Javadoc)
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;

		if (!fixed) {
			Rectangle r = this.getParent().getBounds();
			float rectangleRatio = ((float) r.width) / r.height;
			int imageWidth = image.getWidth();
			int imageHeight = image.getHeight();

			if (ratio <= rectangleRatio) /* image too wide */
			{
				desiredWidth = r.width;
				desiredHeight = Math.round(((float) (r.width * imageHeight))
						/ imageWidth);
			} else {
				desiredHeight = r.height;
				desiredWidth = Math.round(((float) (r.height * imageWidth))
						/ imageHeight);
			}

			if ((desiredWidth > maxWidth) || (desiredHeight > maxHeight)) {
				desiredWidth = maxWidth;
				desiredHeight = maxHeight;
			}
		}

		this.setPreferredSize(new Dimension(desiredWidth, desiredHeight));
		this.revalidate();

		// TODO: this is only for the frame
		// desiredHeight = Math.min(r.height,image.getHeight());
		// desiredWidth = Math.min(r.width,image.getWidth());

		if (hints) {
			g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
					RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		}

		int w1 = 0, h1 = 0;

		if (fixed) {
			Rectangle r = this.getParent().getBounds();
			w1 = (r.width - displayWidth) / 2;
			h1 = (r.height - displayHeight) / 2;
			desiredWidth = displayWidth;
			desiredHeight = displayHeight;
		}

		g2d.drawImage(image, w1, h1, desiredWidth, desiredHeight, this);
	}

	/*
	 * (non-Javadoc)
	 * @see jpview.gui.PTMCanvas#speed()
	 */
	public void speed() {
		isFast = true;
		int w = cachedWidth / 2;
		int h = cachedHeight / 2;
		image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
	}
}
