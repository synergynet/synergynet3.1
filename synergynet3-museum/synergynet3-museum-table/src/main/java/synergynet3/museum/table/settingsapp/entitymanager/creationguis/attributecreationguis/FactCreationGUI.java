package synergynet3.museum.table.settingsapp.entitymanager.creationguis.attributecreationguis;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import synergynet3.museum.table.settingsapp.entitymanager.creationguis.EntityCreatorGUI;

/**
 * The Class FactCreationGUI.
 */
public class FactCreationGUI {

	/**
	 * Instantiates a new fact creation gui.
	 *
	 * @param factText the fact text
	 * @param parentGUI the parent gui
	 */
	public FactCreationGUI(final String factText,
			final EntityCreatorGUI parentGUI) {

		String factLabel = "Fact";

		String titlePrefix = "Edit ";
		if (factText.equals("")) {
			titlePrefix = "Create ";
		}

		int w = 400;

		int xPadding = 10;
		int yPadding = 10;

		int height = 24;
		int labelWidth = 175;
		int textboxWidth = 300;
		int smallButtonWidth = (w / 4) - (xPadding / 2);

		int x = w - (textboxWidth + labelWidth) - (xPadding * 2);
		int y = yPadding;

		int h = (yPadding * 4) + (height * 5);

		final JFrame jf = new JFrame(titlePrefix + factLabel);
		jf.getContentPane().setLayout(new BorderLayout());
		jf.setSize(w, h);
		jf.setResizable(false);

		parentGUI.relatedFrames.add(jf);

		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int screenX = (dim.width - w) / 2;
		int screenY = (dim.height - h) / 2;
		jf.setLocation(screenX, screenY);

		jf.getContentPane().setLayout(null);

		JLabel textLabel = new JLabel(factLabel + ": ");
		final JTextArea textField = new JTextArea();
		textField.setText(factText);
		textField.setLineWrap(true);
		JScrollPane scrollMailBody = new JScrollPane(textField);

		textLabel.setBounds(new Rectangle(x, y, labelWidth, height));
		textLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		x += labelWidth + xPadding;
		scrollMailBody.setBounds(new Rectangle(x, y, textboxWidth, height * 3));

		jf.getContentPane().add(textLabel);
		jf.getContentPane().add(scrollMailBody);

		x = (w / 2) - ((xPadding + (smallButtonWidth * 2)) / 2);
		y += (height * 3) + yPadding;

		JButton okButton = new JButton();
		okButton.setText("OK");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (textField.getText().equals("")) {
					JOptionPane.showMessageDialog(jf,
							"Cannot add an empty fact.");
				} else {
					if (!factText.equals("")) {
						parentGUI.entity.getFacts().remove(factText);
					}
					parentGUI.entity.getFacts().add(textField.getText());
					parentGUI.updateList(EntityAttribute.Facts);

					jf.setVisible(false);
					parentGUI.relatedFrames.remove(jf);
				}

			}
		});
		okButton.setBounds(new Rectangle(x, y, smallButtonWidth, height));
		jf.getContentPane().add(okButton);

		x += smallButtonWidth + xPadding;

		JButton cancelButton = new JButton();
		cancelButton.setText("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				jf.setVisible(false);
				parentGUI.relatedFrames.remove(jf);
			}
		});
		cancelButton.setBounds(new Rectangle(x, y, smallButtonWidth - xPadding,
				height));
		jf.getContentPane().add(cancelButton);

		jf.setVisible(true);

	}

}
