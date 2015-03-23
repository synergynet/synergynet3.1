package synergynet3.apps.numbernet.ui.calculator.calckeydef;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;

import multiplicity3.csys.items.keyboard.IKeyboardGraphicsRenderer;
import multiplicity3.csys.items.keyboard.model.KeyboardDefinition;
import multiplicity3.csys.items.keyboard.model.KeyboardKey;

/**
 * The Class CalculatorKeyboardRenderer.
 */
public class CalculatorKeyboardRenderer implements IKeyboardGraphicsRenderer {

	/** The bg gradient paint. */
	private GradientPaint bgGradientPaint;

	/** The font metrics. */
	private FontMetrics fontMetrics;

	/** The kbd. */
	private KeyboardDefinition kbd;

	/** The keyboard font. */
	private Font keyboardFont = new Font("Arial", Font.BOLD, 36);

	/** The key stroke. */
	private Stroke keyStroke = new BasicStroke(6.0f, // Line width
			BasicStroke.CAP_ROUND, // End-cap style
			BasicStroke.JOIN_ROUND);

	/**
	 * Instantiates a new calculator keyboard renderer.
	 *
	 * @param kbd the kbd
	 */
	public CalculatorKeyboardRenderer(KeyboardDefinition kbd) {
		this.kbd = kbd;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.csys.items.keyboard.IKeyboardGraphicsRenderer#drawKeyboard
	 * (java.awt.Graphics2D, boolean, boolean, boolean)
	 */
	@Override
	public void drawKeyboard(Graphics2D g2d, boolean shiftDown,
			boolean altDown, boolean ctlDown) {

		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		if (fontMetrics == null) {
			fontMetrics = g2d.getFontMetrics(keyboardFont);
		}
		bgGradientPaint = new GradientPaint(0, (float) kbd.getBounds()
				.getMaxY() / 3, Color.white, 0, (float) kbd.getBounds()
				.getMaxY(), Color.lightGray);
		g2d.setPaint(bgGradientPaint);
		g2d.fillRect(0, 0, (int) kbd.getBounds().getMaxX(), (int) kbd
				.getBounds().getMaxY());
		g2d.setColor(Color.black);

		for (KeyboardKey k : kbd.getKeysIterator()) {
			Point p = getShapeCenter(k.getKeyShape());
			// if(k.isEnabled()) {
			if (!k.isEnabled()) {
				continue;
			}
			g2d.setColor(k.getBackgroundColour());
			// }else{
			// g2d.setColor(Color.lightGray);
			// }
			g2d.fill(k.getKeyShape());
			g2d.setStroke(keyStroke); // Vertex join style);
			g2d.setColor(k.getKeyTextColour());
			g2d.draw(k.getKeyShape());
			g2d.setFont(keyboardFont);
			int strWidth = fontMetrics.stringWidth(k
					.getKeyStringRepresentation());
			g2d.drawString(k.getKeyStringRepresentation(),
					p.x - (strWidth / 2), p.y + (fontMetrics.getAscent() / 2));

		}

	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.csys.items.keyboard.behaviour.IMultiTouchKeyboardListener
	 * #keyPressed(multiplicity3.csys.items.keyboard.model.KeyboardKey, boolean,
	 * boolean, boolean)
	 */
	@Override
	public void keyPressed(KeyboardKey k, boolean shiftDown, boolean altDown,
			boolean ctlDown) {
		if (k.getKeyCode() == KeyEvent.VK_SHIFT) {
			shiftDown = true;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.csys.items.keyboard.behaviour.IMultiTouchKeyboardListener
	 * #keyReleased(multiplicity3.csys.items.keyboard.model.KeyboardKey,
	 * boolean, boolean, boolean)
	 */
	@Override
	public void keyReleased(KeyboardKey k, boolean shiftDown, boolean altDown,
			boolean ctlDown) {
		if (k.getKeyCode() == KeyEvent.VK_SHIFT) {
			shiftDown = false;
		}
	}

	/**
	 * Gets the shape center.
	 *
	 * @param keyShape the key shape
	 * @return the shape center
	 */
	private Point getShapeCenter(Shape keyShape) {
		Rectangle2D bounds = keyShape.getBounds2D();
		Point p = new Point((int) bounds.getCenterX(),
				(int) bounds.getCenterY());
		return p;
	}
}
