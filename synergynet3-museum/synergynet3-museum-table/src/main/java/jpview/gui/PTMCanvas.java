/*
 * PTMCanvas.java Created on July 22, 2004, 12:46 AM
 */

package jpview.gui;

import javax.swing.JComponent;

/**
 * @author Default
 */
public abstract class PTMCanvas extends JComponent {

	/** The Constant BUFFERED_IMAGE. */
	public static final int BUFFERED_IMAGE = 1;

	/** The Constant MEMORY_IMAGE_SOURCE. */
	public static final int MEMORY_IMAGE_SOURCE = 0;

	/**
	 *
	 */
	private static final long serialVersionUID = -4063729220344134855L;

	/** The desired height. */
	protected int desiredWidth, desiredHeight;

	/** The display height. */
	protected int displayWidth, displayHeight;

	/** The fixed. */
	protected boolean fixed = false;

	/** The hints. */
	protected boolean hints = false;

	/** The is fast. */
	protected boolean isFast = false;

	/** The pixels. */
	protected int[] pixels;

	/**
	 * Creates the ptm canvas.
	 *
	 * @param width the width
	 * @param height the height
	 * @param type the type
	 * @return the PTM canvas
	 */
	public static PTMCanvas createPTMCanvas(int width, int height, int type) {
		if (type == MEMORY_IMAGE_SOURCE) {
			return null; /* deprecated */
		} else if (type == BUFFERED_IMAGE) {
			return new PTMCanvasBufferedImage(width, height);
		} else {
			return null;
		}
	}

	/**
	 * Detail.
	 */
	public void detail() {
		;
	}

	/**
	 * Fixed size.
	 *
	 * @param b the b
	 */
	public void fixedSize(boolean b) {
		fixed = b;
	}

	/**
	 * Gets the display height.
	 *
	 * @return the display height
	 */
	public int getDisplayHeight() {
		return displayHeight;
	}

	/**
	 * Gets the display width.
	 *
	 * @return the display width
	 */
	public int getDisplayWidth() {
		return displayWidth;
	}

	/**
	 * Gets the pixels.
	 *
	 * @return the pixels
	 */
	public int[] getPixels() {
		return pixels;
	}

	/**
	 * Checks if is fast.
	 *
	 * @return true, if is fast
	 */
	public boolean isFast() {
		return isFast;
	}

	/*
	 * (non-Javadoc)
	 * @see javax.swing.JComponent#paint(java.awt.Graphics)
	 */
	public void paint(java.awt.Graphics g) {
		this.paintComponent(g);
	}

	/**
	 * Sets the display height.
	 *
	 * @param height the new display height
	 */
	public void setDisplayHeight(int height) {
		displayHeight = height;
	}

	/**
	 * Sets the display width.
	 *
	 * @param width the new display width
	 */
	public void setDisplayWidth(int width) {
		displayWidth = width;
	}

	/**
	 * Speed.
	 */
	public void speed() {
		;
	}

	/**
	 * Use hint.
	 *
	 * @param b the b
	 */
	public void useHint(boolean b) {
		hints = b;
	}
}
