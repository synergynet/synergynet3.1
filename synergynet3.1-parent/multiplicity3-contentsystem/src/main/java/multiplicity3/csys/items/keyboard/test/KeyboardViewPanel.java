package multiplicity3.csys.items.keyboard.test;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import multiplicity3.csys.items.keyboard.defs.simple.SimpleAlphaKeyboardRenderer;
import multiplicity3.csys.items.keyboard.model.KeyboardDefinition;

public class KeyboardViewPanel extends JPanel {
	private static final long serialVersionUID = 2179702392158132563L;
	private KeyboardDefinition kbd;
	private SimpleAlphaKeyboardRenderer kir;

	public KeyboardViewPanel(KeyboardDefinition kd) {
		this.kbd = kd;
		kir = new SimpleAlphaKeyboardRenderer(kbd);
	}

	public void paintComponent(Graphics g) {
		clear(g);
		kir.drawKeyboard((Graphics2D)g, false, false, false);
	}
	
	protected void clear(Graphics g) {
		super.paintComponent(g);
	}
}
