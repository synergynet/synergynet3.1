package synergynet3.museum.table.settingsapp.general;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import multiplicity3.input.events.MultiTouchCursorEvent;
import synergynet3.additionalUtils.IgnoreDoubleClick;
import synergynet3.museum.table.MuseumSettingsApp;
import synergynet3.museum.table.settingsapp.MuseumAppPreferences;
import synergynet3.museum.table.settingsapp.SettingsUtil;
import synergynet3.museum.table.settingsapp.appearance.AppearanceConfigPrefsItem;
import synergynet3.museum.table.settingsapp.entitymanager.EntityManagerGUI;
import synergynet3.museum.table.settingsapp.lensmanager.LensManagerGUI;

/**
 * The Class GeneralConfigPanel.
 */
public class GeneralConfigPanel extends JPanel {

	/** The backgroundloc. */
	public static String BACKGROUNDLOC = "Background";

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -3846068982836447192L;

	/** The entity manager. */
	private EntityManagerGUI entityManager;

	/** The lens manager gui. */
	private LensManagerGUI lensManagerGUI;

	/** The prefs. */
	private GeneralConfigPrefsItem prefs;

	/** The text limit. */
	private int textLimit = 50;

	/**
	 * Instantiates a new general config panel.
	 *
	 * @param serverConfigPrefsItem the server config prefs item
	 */
	public GeneralConfigPanel(GeneralConfigPrefsItem serverConfigPrefsItem) {
		this.prefs = serverConfigPrefsItem;
		initComponents();
	}

	/**
	 * Browse folders.
	 *
	 * @param jFileChooser the j file chooser
	 * @return the string
	 */
	private String browseFolders(JFileChooser jFileChooser) {
		String toReturn = "";
		int returnVal = jFileChooser.showOpenDialog(this);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = jFileChooser.getSelectedFile();
			toReturn = file.getAbsolutePath();
		}
		return toReturn;
	}

	/**
	 * Gets the int from text field.
	 *
	 * @param tf the tf
	 * @param previousValue the previous value
	 * @return the int from text field
	 */
	private int getIntFromTextField(JTextField tf, int previousValue) {
		if (tf.getText().length() > 0) {
			try {
				int num = Integer.parseInt(tf.getText());
				tf.setForeground(Color.black);
				return num;
			} catch (NumberFormatException ex) {
				tf.setForeground(Color.red);
			}
		}
		return previousValue;
	}

	/**
	 * Gets the int from text field.
	 *
	 * @param tf the tf
	 * @param previousValue the previous value
	 * @return the int from text field
	 */
	private String getIntFromTextField(JTextField tf, String previousValue) {
		if (tf.getText().length() > 0) {
			try {
				Integer.parseInt(tf.getText());
				tf.setForeground(Color.black);
				return tf.getText();
			} catch (NumberFormatException ex) {
				tf.setForeground(Color.red);
			}
		}
		return previousValue;
	}

	/**
	 * Inits the components.
	 */
	private void initComponents() {

		JLabel enableLabel = new JLabel("Enable: ");
		Font labelFont = new Font(enableLabel.getFont().getName(), Font.ITALIC,
				enableLabel.getFont().getSize());

		final JCheckBox locationsCheckbox = new JCheckBox();
		locationsCheckbox.setText("Locations");
		locationsCheckbox.setSelected(prefs.getLocationsEnabled());
		locationsCheckbox
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						prefs.setLocationsEnabled(locationsCheckbox
								.isSelected());
					}
				});

		final JCheckBox userRecordingsCheckbox = new JCheckBox();
		userRecordingsCheckbox.setText("User Recordings");
		userRecordingsCheckbox.setSelected(prefs.getUserRecordingsEnabled());
		userRecordingsCheckbox
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						prefs.setUserRecordingsEnabled(userRecordingsCheckbox
								.isSelected());
					}
				});

		JLabel adminPINLabel = new JLabel("Admin PIN: ");
		final JTextField adminPinTextField = new JTextField();
		adminPinTextField.setText(prefs.getAdminPIN());
		adminPinTextField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				prefs.setAdminPIN(getIntFromTextField(adminPinTextField,
						prefs.getAdminPIN()));
			}
		});

		JLabel maxRecordingLengthLabel = new JLabel(
				"Max Recording Length (seconds): ");
		final JTextField maxRecordingLengthField = new JTextField();
		maxRecordingLengthField.setText("" + prefs.getMaxRecordingTime());
		maxRecordingLengthField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				prefs.setMaxRecordingTime(getIntFromTextField(
						maxRecordingLengthField, prefs.getMaxRecordingTime()));
			}
		});

		JLabel contentLocationLabel = new JLabel("Workspace: ");
		final JLabel txtContentLocation = new JLabel();
		txtContentLocation.setText(setTruncatedText(prefs.getContentFolder()));
		txtContentLocation.setFont(labelFont);

		final JFileChooser contentFileChooser = new JFileChooser(new File(
				prefs.getContentFolder()));
		contentFileChooser.setDialogTitle("Browse for Workspace");
		contentFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		contentFileChooser.setAcceptAllFileFilterUsed(false);

		JButton contentFolderBrowseButton = new JButton();
		contentFolderBrowseButton.setText("Browse");
		contentFolderBrowseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				String file = browseFolders(contentFileChooser);
				txtContentLocation.setText(setTruncatedText(file));
				AppearanceConfigPrefsItem.savetoXML();
				prefs.setContentFolder(file);
				AppearanceConfigPrefsItem.loadFromXML();
			}
		});

		JButton viewContentsButton = new JButton();
		viewContentsButton.setText("View Background Image Folder");

		final IgnoreDoubleClick clickerBackground = new IgnoreDoubleClick() {
			@Override
			public void onAction(MultiTouchCursorEvent event) {
				Desktop desktop = null;
				File contentFolder = new File(
						MuseumAppPreferences.getContentFolder());
				if (contentFolder.exists()) {
					File file = new File(
							MuseumAppPreferences.getContentFolder()
									+ File.separator + BACKGROUNDLOC);
					if (!file.exists()) {
						file.mkdir();
					}
					if (Desktop.isDesktopSupported()) {
						desktop = Desktop.getDesktop();
					}
					try {
						desktop.open(file);
					} catch (IOException e) {
					}
				}
			}
		};

		viewContentsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				clickerBackground.click(null);
			}
		});
		JButton helpMap = SettingsUtil
				.generateHelpButton(
						MuseumSettingsApp.jf,
						"Place a single image in this folder for it to be used as the app's background."
								+ "\nLeave the folder empty to use the background colour.");

		JButton entityManagerButton = new JButton();
		entityManagerButton.setText("Entity Manager");
				entityManagerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				boolean generateNew = false;
				if (entityManager == null) {
					generateNew = true;
				} else {
					if (!entityManager.isVisible()) {
						generateNew = true;
					}
				}
				if (generateNew) {
					entityManager = new EntityManagerGUI();
					entityManager.show();
				}
			}
		});

		JButton lensManagerButton = new JButton();
		lensManagerButton.setText("Lens Manager");
				lensManagerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				boolean generateNew = false;
				if (lensManagerGUI == null) {
					generateNew = true;
				} else {
					if (!lensManagerGUI.isVisible()) {
						generateNew = true;
					}
				}
				if (generateNew) {
					lensManagerGUI = new LensManagerGUI();
					lensManagerGUI.show();
				}
			}
		});

		final JCheckBox metricsCheckbox = new JCheckBox();
				metricsCheckbox.setText("Collect additional metrics");
				metricsCheckbox.setSelected(prefs.getMetricsEnabled());

		final JLabel metricsLabel = new JLabel("Metrics Folder: ");

		final JLabel metricsLocation = new JLabel();
		metricsLocation.setText(setTruncatedText(prefs.getMetricsFolder()));
		metricsLocation.setFont(labelFont);

		final JFileChooser metricsFolderChooser = new JFileChooser(new File(
				prefs.getMetricsFolder()));
		metricsFolderChooser.setDialogTitle("Browse for metrics location");
				metricsFolderChooser
				.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				metricsFolderChooser.setAcceptAllFileFilterUsed(false);

		final JButton metricsFolderBrowseButton = new JButton();
		metricsFolderBrowseButton.setText("Browse");
		metricsFolderBrowseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				String file = browseFolders(metricsFolderChooser);
				metricsLocation.setText(setTruncatedText(file));
				prefs.setMetricsFolder(file);
			}
		});

		metricsCheckbox.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						prefs.setMetricsEnabled(metricsCheckbox.isSelected());
				metricsLabel.setEnabled(metricsCheckbox.isSelected());
						metricsLocation.setEnabled(metricsCheckbox.isSelected());
						metricsFolderBrowseButton.setEnabled(metricsCheckbox
						.isSelected());

			}
				});

		metricsLabel.setEnabled(metricsCheckbox.isSelected());
				metricsLocation.setEnabled(metricsCheckbox.isSelected());
				metricsFolderBrowseButton.setEnabled(metricsCheckbox.isSelected());

		setLayout(null);

		int xPadding = 10;
		int yPadding = 10;

		int x = xPadding;
		int y = yPadding;

		int height = 24;
		int labelWidth = 125;
		int textBoxWidth = 125;
		int buttonWidth = 120;
		int checkButtonWidth = 175;

		int locationWidth = (((buttonWidth * 4) + xPadding) - buttonWidth)
				- xPadding;

		enableLabel.setBounds(new Rectangle(x, y, labelWidth, height));
		enableLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		x += labelWidth + xPadding;
		locationsCheckbox.setBounds(new Rectangle(x, y, checkButtonWidth,
				height));
		x += checkButtonWidth + xPadding;
		userRecordingsCheckbox.setBounds(new Rectangle(x, y, checkButtonWidth,
				height));

		x = xPadding;
		y += height + yPadding;

		adminPINLabel.setBounds(new Rectangle(x, y, labelWidth, height));
		adminPINLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		x += labelWidth + xPadding;
		adminPinTextField.setBounds(new Rectangle(x, y, textBoxWidth, height));
		x += textBoxWidth + xPadding;
		maxRecordingLengthLabel.setBounds(new Rectangle(x, y, labelWidth * 2,
				height));
		maxRecordingLengthLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		x += (labelWidth * 2) + xPadding;
		maxRecordingLengthField.setBounds(new Rectangle(x, y, textBoxWidth,
				height));

		x = xPadding;
		y += height + yPadding;

		contentLocationLabel.setBounds(new Rectangle(x, y, labelWidth, height));
		contentLocationLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		x += labelWidth + xPadding;
		txtContentLocation
				.setBounds(new Rectangle(x, y, locationWidth, height));
		x += locationWidth + xPadding;
		contentFolderBrowseButton.setBounds(new Rectangle(x, y, buttonWidth,
				height));

		x = xPadding;
		y += height + yPadding;

		x += labelWidth + xPadding;
		viewContentsButton.setBounds(new Rectangle(x, y, buttonWidth * 2,
				height));
		x += (buttonWidth * 2) + xPadding;
		helpMap.setBounds(new Rectangle(x, y, height, height));

		x = xPadding;
		y += height + yPadding;
		y += height + yPadding;
		x += labelWidth + xPadding;

		entityManagerButton.setBounds(new Rectangle(x, y, buttonWidth * 2,
				height));

		x += (buttonWidth * 2) + xPadding;

		lensManagerButton
				.setBounds(new Rectangle(x, y, buttonWidth * 2, height));

		x = xPadding;
		y += height + yPadding;
		y += height + yPadding;
		y += height + yPadding;

		metricsCheckbox.setBounds(new Rectangle(x, y, buttonWidth * 4, height));

		x = xPadding * 2;
		y += height + yPadding;

		metricsLabel.setBounds(new Rectangle(x, y, labelWidth, height));
		metricsLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		x += labelWidth + xPadding;
		metricsLocation.setBounds(new Rectangle(x, y, locationWidth, height));
		x += locationWidth + xPadding;
		metricsFolderBrowseButton.setBounds(new Rectangle(x, y, buttonWidth,
				height));

		add(enableLabel);
		add(locationsCheckbox);
		add(userRecordingsCheckbox);

		add(adminPinTextField);
		add(adminPINLabel);

		add(maxRecordingLengthLabel);
		add(maxRecordingLengthField);

		add(contentLocationLabel);
		add(txtContentLocation);
		add(contentFolderBrowseButton);

		add(viewContentsButton);
		add(helpMap);

		add(entityManagerButton);
		add(lensManagerButton);

		add(metricsCheckbox);
		add(metricsLabel);
		add(metricsLocation);
		add(metricsFolderBrowseButton);
	}

	/**
	 * Sets the truncated text.
	 *
	 * @param loc the loc
	 * @return the string
	 */
	private String setTruncatedText(String loc) {
		String truncatedFile = loc;
		if (truncatedFile.length() > textLimit) {
			truncatedFile = "..."
					+ truncatedFile.substring(truncatedFile.length()
							- textLimit, truncatedFile.length());
		}
		return truncatedFile;
	}

}