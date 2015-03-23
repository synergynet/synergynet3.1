package synergynet3.museum.table.settingsapp.entitymanager;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import multiplicity3.input.events.MultiTouchCursorEvent;
import synergynet3.additionalUtils.IgnoreDoubleClick;
import synergynet3.mediadetection.mediasearchtypes.AudioSearchType;
import synergynet3.museum.table.mainapp.EntityManager;

/**
 * The Class ModerationGUI.
 */
public class ModerationGUI {

	/** The Constant AUDIO_CHECK. */
	private static final AudioSearchType AUDIO_CHECK = new AudioSearchType();

	/** The contents awaiting moderation. */
	private int contentsAwaitingModeration = 0;

	/**
	 * Instantiates a new moderation gui.
	 *
	 * @param name the name
	 * @param loc the loc
	 * @param entityManagerGUI the entity manager gui
	 */
	public ModerationGUI(String name, String loc,
			final EntityManagerGUI entityManagerGUI) {

		int w = 750;

		int xPadding = 10;
		int yPadding = 10;

		int height = 24;
		int labelWidth = w - (xPadding * 2);
		int smallButtonWidth = (w / 3) - (xPadding * 2);

		int x = xPadding;
		int y = yPadding;

		int h = (yPadding * 6) + (height * 5);

		final String location = loc + File.separator + EntityManager.RECORDINGS;

		checkAndMakeDirectory(location);
		checkAndMakeDirectory(location + File.separator
				+ EntityManager.APPROVED);

		File recordingFolder = new File(location);
		if (recordingFolder.isDirectory()) {
			contentsAwaitingModeration = getNumberOfFilesAwaitingModeration(recordingFolder);
		}

		final JFrame jf = new JFrame("Moderate Entity Contents");
		jf.getContentPane().setLayout(new BorderLayout());
		jf.setSize(w, h);
		jf.setResizable(false);

		WindowListener exitListener = new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				entityManagerGUI.updateList();
				jf.setVisible(false);
			}
		};
		jf.addWindowListener(exitListener);

		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int screenX = (dim.width - w) / 2;
		int screenY = (dim.height - h) / 2;
		jf.setLocation(screenX, screenY);

		jf.getContentPane().setLayout(null);

		String contributionText = "contribution";
		if (contentsAwaitingModeration != 1) {
			contributionText += "s";
		}

		JLabel textLabel = new JLabel(name + " has "
				+ contentsAwaitingModeration + " " + contributionText
				+ " awaiting moderation.");
		textLabel.setHorizontalAlignment(SwingConstants.CENTER);
		textLabel.setBounds(new Rectangle(x, y, labelWidth, height));
		jf.getContentPane().add(textLabel);

		y += height + yPadding;

		JButton okButton = new JButton();
		okButton.setText("View Contributions");

		final IgnoreDoubleClick moderateClicker = new IgnoreDoubleClick() {
			@Override
			public void onAction(MultiTouchCursorEvent event) {
				Desktop desktop = null;
				File file = new File(location);
				if (Desktop.isDesktopSupported()) {
					desktop = Desktop.getDesktop();
				}
				try {
					desktop.open(file);
				} catch (IOException e) {
				}
			}
		};

		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				moderateClicker.click(null);
			}
		});
		okButton.setBounds(new Rectangle(x, y, labelWidth, height));
		jf.getContentPane().add(okButton);

		y += height + yPadding;

		JLabel textLabelPartTwo = new JLabel(
				"Move acceptable contributions into the "
						+ EntityManager.APPROVED
						+ " folder and delete the remaining files.");
		textLabelPartTwo.setHorizontalAlignment(SwingConstants.CENTER);
		textLabelPartTwo.setBounds(new Rectangle(x, y, labelWidth, height));
		jf.getContentPane().add(textLabelPartTwo);

		x += (w / 2) - (smallButtonWidth / 2);
		y += height + yPadding;

		JButton cancelButton = new JButton();
		cancelButton.setText("Done");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				entityManagerGUI.updateList();
				jf.setVisible(false);
			}
		});
		cancelButton.setBounds(new Rectangle(x, y, smallButtonWidth, height));
		jf.getContentPane().add(cancelButton);

		jf.setVisible(true);
	}

	/**
	 * Check and make directory.
	 *
	 * @param loc the loc
	 */
	private void checkAndMakeDirectory(String loc) {
		File folder = new File(loc);
		if (!folder.exists()) {
			folder.mkdir();
		}
	}

	/**
	 * Gets the number of files awaiting moderation.
	 *
	 * @param folder the folder
	 * @return the number of files awaiting moderation
	 */
	private int getNumberOfFilesAwaitingModeration(File folder) {
		int toReturn = 0;
		File[] files = folder.listFiles();
		for (File f : files) {
			if (!f.isDirectory()) {
				if (AUDIO_CHECK.isFileOfSearchType(f)) {
					toReturn++;
				}
			}
		}
		return toReturn;
	}

}
