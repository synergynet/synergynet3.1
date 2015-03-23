package multiplicity3.csys.items.keyboard.test;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import multiplicity3.csys.items.keyboard.defs.simple.SimpleAlphaKeyboardDefinition;
import multiplicity3.csys.items.keyboard.model.KeyboardDefinition;

/**
 * The Class TestKeyboard.
 */
public class TestKeyboard {

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				JFrame jf = new JFrame();
				KeyboardDefinition kd = new SimpleAlphaKeyboardDefinition();
				KeyboardViewPanel kvp = new KeyboardViewPanel(kd);
				jf.add(kvp);
				jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				jf.setSize(840, 480);
				jf.setVisible(true);
			}
		});

	}
}
