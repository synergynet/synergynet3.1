package synergynet3.museum.table.settingsapp.lensmanager;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import synergynet3.museum.table.settingsapp.MuseumAppPreferences;
import synergynet3.museum.table.settingsapp.SettingsUtil;
import synergynet3.museum.table.utils.LensUtils;

/**
 * The Class LensCreatorGUI.
 */
@SuppressWarnings(
{ "rawtypes", "unchecked" })
public class LensCreatorGUI
{

	/** The jf. */
	protected JFrame jf;

	/**
	 * Instantiates a new lens creator gui.
	 *
	 * @param lensFileName
	 *            the lens file name
	 * @param mainGUI
	 *            the main gui
	 */
	public LensCreatorGUI(final String lensFileName, final LensManagerGUI mainGUI)
	{
		int w = 350;

		int xPadding = 10;
		int yPadding = 10;

		int height = 24;
		int labelWidth = 50;
		int textboxWidth = 250;
		int smallButtonWidth = (w / 3) - (xPadding / 2);

		int x = 0;
		int y = yPadding;

		int h = (yPadding * 5) + (height * 4);

		String colour = "Red";
		String title = "Create";
		String name = "";

		if (!lensFileName.equals(""))
		{
			title = "Edit";

			File file = new File(MuseumAppPreferences.getContentFolder() + File.separator + LensUtils.LENSES_FOLDER + File.separator + lensFileName + ".xml");
			if (file.exists())
			{
				LensXmlManager lensXmlManager = new LensXmlManager(file);
				lensXmlManager.regenerate();
				name = lensXmlManager.getName();
				colour = lensXmlManager.getColour();
			}

		}

		final JFrame jf = new JFrame(title + " Lens");
		jf.getContentPane().setLayout(new BorderLayout());
		jf.setSize(w, h);
		jf.setResizable(false);

		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int screenX = (dim.width - w) / 2;
		int screenY = (dim.height - h) / 2;
		jf.setLocation(screenX, screenY);

		jf.getContentPane().setLayout(null);

		JLabel textLabel = new JLabel("Name:");
		textLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		final JTextField textField = new JTextField();

		textLabel.setBounds(new Rectangle(x, y, labelWidth, height));
		textLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		textField.setText(name);

		x += labelWidth + xPadding;
		textField.setBounds(new Rectangle(x, y, textboxWidth, height));

		jf.getContentPane().add(textLabel);
		jf.getContentPane().add(textField);

		x = 0;
		y += height + yPadding;

		JLabel colourLabel = new JLabel("Colour:");
		colourLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		final JComboBox colourComboBox = new JComboBox(SettingsUtil.COLOUR_CHOICE);
		colourComboBox.setSelectedItem(colour);
		colourLabel.setBounds(new Rectangle(x, y, labelWidth, height));
		colourLabel.setHorizontalAlignment(SwingConstants.RIGHT);

		x += labelWidth + xPadding;
		colourComboBox.setBounds(new Rectangle(x, y, textboxWidth, height));

		jf.getContentPane().add(colourLabel);
		jf.getContentPane().add(colourComboBox);

		x = (w / 2) - ((xPadding + (smallButtonWidth * 2)) / 2);
		y += height + yPadding;

		JButton okButton = new JButton();
		okButton.setText("OK");
		okButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent evt)
			{
				String name = textField.getText();
				name = SettingsUtil.removeSpecialChars(name);
				if (name.equals(""))
				{
					JOptionPane.showMessageDialog(jf, "No name is given.");
				}
				else
				{
					if (mainGUI.listContainsValue(name) && !name.equals(lensFileName))
					{
						JOptionPane.showMessageDialog(jf, "This name is already in use.");
					}
					else
					{
						String selectedColour = (String) colourComboBox.getSelectedItem();

						if (!lensFileName.equals(""))
						{
							if (!name.equals(lensFileName))
							{
								File file = new File(MuseumAppPreferences.getContentFolder() + File.separator + LensUtils.LENSES_FOLDER + File.separator + lensFileName);
								if (file.exists())
								{
									if (!file.delete())
									{
										file.deleteOnExit();
									}
								}
							}
						}

						try
						{
							String fileLoc = MuseumAppPreferences.getContentFolder() + File.separator + LensUtils.LENSES_FOLDER + File.separator + name + ".xml";
							File file = new File(fileLoc);
							if (!file.exists())
							{
								file.createNewFile();
							}

							LensXmlManager lensXmlManager = new LensXmlManager(file);
							lensXmlManager.setName(name);
							lensXmlManager.setColour(selectedColour);
							lensXmlManager.saveXML();

						}
						catch (IOException e)
						{
							e.printStackTrace();
						}

						mainGUI.updateList();
						jf.setVisible(false);
					}
				}
			}
		});
		okButton.setBounds(new Rectangle(x, y, smallButtonWidth, height));
		jf.getContentPane().add(okButton);

		x += smallButtonWidth + xPadding;

		JButton cancelButton = new JButton();
		cancelButton.setText("Cancel");
		cancelButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent evt)
			{
				jf.setVisible(false);
			}
		});
		cancelButton.setBounds(new Rectangle(x, y, smallButtonWidth - xPadding, height));
		jf.getContentPane().add(cancelButton);

		jf.setVisible(true);
	}

}
