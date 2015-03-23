package multiplicity3.csys.items.keyboard.test;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import multiplicity3.csys.items.keyboard.defs.simple.SimpleAlphaKeyboardRenderer;
import multiplicity3.csys.items.keyboard.model.KeyboardDefinition;

/**
 * The Class KeyboardViewPanel.
 */
public class KeyboardViewPanel extends JPanel {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 2179702392158132563L;

	/** The kbd. */
	private KeyboardDefinition kbd;

	/** The kir. */
	private SimpleAlphaKeyboardRenderer kir;

	/**
	 * Instantiates a new keyboard view panel.
	 *
	 * @param kd the kd
	 */
	public KeyboardViewPanel(KeyboardDefinition kd) {
		this.kbd = kd;
		kir = new SimpleAlphaKeyboardRenderer(kbd);
	}

	/*
	 * (non-Javadoc)
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	public void paintComponent(Graphics g) {
		clear(g);
		kir.drawKeyboard((Graphics2D) g, false, false, false);
	}

	/**
	 * Clear.
	 *
	 * @param g the g
	 */
	protected void clear(Graphics g) {
		super.paintComponent(g);
	}
}
