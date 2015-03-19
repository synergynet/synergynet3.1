/*
 * PTMCanvas.java
 *
 * Created on July 22, 2004, 12:46 AM
 */

package jpview.gui;

import javax.swing.JComponent;

/**
 * 
 * @author Default
 */
public abstract class PTMCanvas extends JComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4063729220344134855L;

	public static final int MEMORY_IMAGE_SOURCE = 0;

	public static final int BUFFERED_IMAGE = 1;

	protected int[] pixels;

	protected int desiredWidth, desiredHeight;
	
	protected int displayWidth, displayHeight;

	protected boolean hints = false;

	protected boolean isFast = false;

	protected boolean fixed = false;

	public boolean isFast() {
		return isFast;
	}

	public static PTMCanvas createPTMCanvas(int width, int height, int type)
	{
		if (type == MEMORY_IMAGE_SOURCE)
			return null; /* deprecated */
		else if (type == BUFFERED_IMAGE)
			return new PTMCanvasBufferedImage(width, height);
		else
			return null;
	}

	public int[] getPixels() {
		return pixels;
	}

	public void useHint(boolean b) {
		hints = b;
	}

	public void speed() {
		;
	}

	public void detail() {
		;
	}

	public void fixedSize(boolean b) {
		fixed = b;
	}

	public void setDisplayWidth(int width)
	{
		displayWidth = width;
	}
	
	public void setDisplayHeight(int height)
	{
		displayHeight = height;
	}
	
	public int getDisplayWidth()
	{
		return displayWidth;
	}
	
	public int getDisplayHeight()
	{
		return displayHeight;
	}
	
	public void paint(java.awt.Graphics g) {
		this.paintComponent(g);
	}
}
