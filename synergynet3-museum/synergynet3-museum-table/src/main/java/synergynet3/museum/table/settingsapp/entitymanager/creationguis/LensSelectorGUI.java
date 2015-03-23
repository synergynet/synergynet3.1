package synergynet3.museum.table.settingsapp.entitymanager.creationguis;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

import synergynet3.museum.table.utils.LensUtils;

/**
 * The Class LensSelectorGUI.
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class LensSelectorGUI {

	/**
	 * Instantiates a new lens selector gui.
	 *
	 * @param parentGUI the parent gui
	 */
	public LensSelectorGUI(final EntityCreatorGUI parentGUI) {

		int w = 400;

		int xPadding = 10;
		int yPadding = 10;

		int height = 24;
		int labelWidth = 175;
		int comboBoxWidth = 300;
		int smallButtonWidth = (w / 4) - (xPadding / 2);

		int x = w - (comboBoxWidth + labelWidth) - (xPadding * 2);
		int y = yPadding;

		int h = (yPadding * 3) + (height * 4);

		final JFrame jf = new JFrame("Add Lens");
		jf.getContentPane().setLayout(new BorderLayout());
		jf.setSize(w, h);
		jf.setResizable(false);

		parentGUI.relatedFrames.add(jf);

		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int screenX = (dim.width - w) / 2;
		int screenY = (dim.height - h) / 2;
		jf.setLocation(screenX, screenY);

		jf.getContentPane().setLayout(null);

		JLabel textLabel = new JLabel("Lense: ");

		textLabel.setBounds(new Rectangle(x, y, labelWidth, height));
		textLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		x += labelWidth + xPadding;

		jf.getContentPane().add(textLabel);

		String[] entitiesArray = LensUtils.getLenses();
		Arrays.sort(entitiesArray);
		final JComboBox entityComboBox = new JComboBox(entitiesArray);

		entityComboBox.setBounds(new Rectangle(x, y, comboBoxWidth, height));

		jf.getContentPane().add(entityComboBox);

		x = (w / 2) - ((xPadding + (smallButtonWidth * 2)) / 2);
		y += height + yPadding;

		JButton okButton = new JButton();
		okButton.setText("OK");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {

				if (entityComboBox.getSelectedIndex() < 0) {
					JOptionPane.showMessageDialog(jf,
							"Cannot add an empty lens.");
				} else {
					String targetName = (String) entityComboBox
							.getSelectedItem();
					if (parentGUI.entity.getLensValues().contains(targetName)) {
						JOptionPane.showMessageDialog(jf,
								"The POI is already visible under this lens.");
					} else {
						parentGUI.entity.getLensValues().add(
								(String) entityComboBox.getSelectedItem());
						parentGUI.updateLensList();
						jf.setVisible(false);
						parentGUI.relatedFrames.remove(jf);
					}
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
