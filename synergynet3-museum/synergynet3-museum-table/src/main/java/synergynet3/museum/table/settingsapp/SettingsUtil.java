package synergynet3.museum.table.settingsapp;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import synergynet3.fonts.FontColour;

import com.jme3.math.ColorRGBA;

/**
 * The Class SettingsUtil.
 */
public class SettingsUtil
{

	/** The Constant COLOUR_CHOICE. */
	public static final String[] COLOUR_CHOICE =
	{ "White", "Black", "Red", "Blue", "Yellow", "Green", "Orange", "Pink", "Cyan", "Grey", "Dark Grey", "Invisible" };

	/**
	 * Copy file.
	 *
	 * @param sourceFile
	 *            the source file
	 * @param destFile
	 *            the dest file
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	@SuppressWarnings("resource")
	public static void copyFile(File sourceFile, File destFile) throws IOException
	{
		if (!destFile.exists())
		{
			destFile.createNewFile();
		}

		FileChannel source = null;
		FileChannel destination = null;

		try
		{
			source = new FileInputStream(sourceFile).getChannel();
			destination = new FileOutputStream(destFile).getChannel();
			destination.transferFrom(source, 0, source.size());
		}
		finally
		{
			if (source != null)
			{
				source.close();
			}
			if (destination != null)
			{
				destination.close();
			}
		}
	}

	/**
	 * Generate help button.
	 *
	 * @param frame
	 *            the frame
	 * @param message
	 *            the message
	 * @return the j button
	 */
	public static JButton generateHelpButton(final JFrame frame, final String message)
	{
		JButton button = new JButton();
		try
		{
			Image img = ImageIO.read(SettingsUtil.class.getResource("help.png"));
			button.setIcon(new ImageIcon(img));
		}
		catch (IOException ex)
		{
		}
		button.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				JOptionPane.showMessageDialog(frame, message);
			}
		});

		return button;
	}

	/**
	 * Generate reset button.
	 *
	 * @return the j button
	 */
	public static JButton generateResetButton()
	{
		JButton button = new JButton();
		try
		{
			Image img = ImageIO.read(SettingsUtil.class.getResource("reset.png"));
			button.setIcon(new ImageIcon(img));
		}
		catch (IOException ex)
		{
		}
		button.setToolTipText("Reset to default value.");
		return button;
	}

	/**
	 * Gets the color rgba.
	 *
	 * @param colour
	 *            the colour
	 * @return the color rgba
	 */
	public static ColorRGBA getColorRGBA(String colour)
	{
		ColorRGBA toReturn = ColorRGBA.White;

		if (colour.equals(COLOUR_CHOICE[0]))
		{
			toReturn = ColorRGBA.White;
		}
		else if (colour.equals(COLOUR_CHOICE[1]))
		{
			toReturn = ColorRGBA.Black;
		}
		else if (colour.equals(COLOUR_CHOICE[2]))
		{
			toReturn = ColorRGBA.Red;
		}
		else if (colour.equals(COLOUR_CHOICE[3]))
		{
			toReturn = ColorRGBA.Blue;
		}
		else if (colour.equals(COLOUR_CHOICE[4]))
		{
			toReturn = ColorRGBA.Yellow;
		}
		else if (colour.equals(COLOUR_CHOICE[5]))
		{
			toReturn = ColorRGBA.Green;
		}
		else if (colour.equals(COLOUR_CHOICE[6]))
		{
			toReturn = ColorRGBA.Orange;
		}
		else if (colour.equals(COLOUR_CHOICE[7]))
		{
			toReturn = ColorRGBA.Pink;
		}
		else if (colour.equals(COLOUR_CHOICE[8]))
		{
			toReturn = ColorRGBA.Cyan;
		}
		else if (colour.equals(COLOUR_CHOICE[9]))
		{
			toReturn = ColorRGBA.Gray;
		}
		else if (colour.equals(COLOUR_CHOICE[10]))
		{
			toReturn = ColorRGBA.DarkGray;
		}
		else if (colour.equals(COLOUR_CHOICE[11]))
		{
			return new ColorRGBA(0, 0, 0, 0);
		}

		toReturn.set(toReturn.r, toReturn.g, toReturn.b, 1);
		return toReturn;
	}

	/**
	 * Gets the float from text field.
	 *
	 * @param tf
	 *            the tf
	 * @param previousValue
	 *            the previous value
	 * @return the float from text field
	 */
	public static float getFloatFromTextField(JTextField tf, float previousValue)
	{
		if (tf.getText().length() > 0)
		{
			try
			{
				float num = Float.parseFloat(tf.getText());
				tf.setForeground(Color.black);
				return num;
			}
			catch (NumberFormatException ex)
			{
				tf.setForeground(Color.red);
			}
		}
		return previousValue;
	}

	/**
	 * Gets the font colour.
	 *
	 * @param colour
	 *            the colour
	 * @return the font colour
	 */
	public static FontColour getFontColour(String colour)
	{

		if (colour.equals(COLOUR_CHOICE[0]))
		{
			return FontColour.White;
		}
		else if (colour.equals(COLOUR_CHOICE[1]))
		{
			return FontColour.Black;
		}
		else if (colour.equals(COLOUR_CHOICE[2]))
		{
			return FontColour.Red;
		}
		else if (colour.equals(COLOUR_CHOICE[3]))
		{
			return FontColour.Blue;
		}
		else if (colour.equals(COLOUR_CHOICE[4]))
		{
			return FontColour.Yellow;
		}
		else if (colour.equals(COLOUR_CHOICE[5]))
		{
			return FontColour.Green;
		}
		else if (colour.equals(COLOUR_CHOICE[6]))
		{
			return FontColour.Orange;
		}
		else if (colour.equals(COLOUR_CHOICE[7]))
		{
			return FontColour.Magenta;
		}
		else if (colour.equals(COLOUR_CHOICE[8]))
		{
			return FontColour.Cyan;
		}
		else if (colour.equals(COLOUR_CHOICE[9]))
		{
			return FontColour.Grey;
		}
		else if (colour.equals(COLOUR_CHOICE[10]))
		{
			return FontColour.Dark_Grey;
		}

		return FontColour.White;
	}

	/**
	 * Gets the integer from text field.
	 *
	 * @param tf
	 *            the tf
	 * @param previousValue
	 *            the previous value
	 * @return the integer from text field
	 */
	public static int getIntegerFromTextField(JTextField tf, int previousValue)
	{
		if (tf.getText().length() > 0)
		{
			try
			{
				int num = Integer.parseInt(tf.getText());
				tf.setForeground(Color.black);
				return num;
			}
			catch (NumberFormatException ex)
			{
				tf.setForeground(Color.red);
			}
		}
		return previousValue;
	}

	/**
	 * Removes the special chars.
	 *
	 * @param name
	 *            the name
	 * @return the string
	 */
	public static String removeSpecialChars(String name)
	{
		return name.replaceAll("[^a-zA-Z0-9 ]+", "");
	}

	/**
	 * String array list to string array.
	 *
	 * @param arrayList
	 *            the array list
	 * @return the string[]
	 */
	public static String[] stringArrayListToStringArray(ArrayList<String> arrayList)
	{
		String[] toReturn = new String[arrayList.size()];
		for (int i = 0; i < arrayList.size(); i++)
		{
			toReturn[i] = arrayList.get(i);
		}
		Arrays.sort(toReturn);
		return toReturn;
	}

}
