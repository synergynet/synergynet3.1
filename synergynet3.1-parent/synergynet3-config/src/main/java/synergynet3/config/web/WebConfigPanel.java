package synergynet3.config.web;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 * The Class WebConfigPanel.
 */
public class WebConfigPanel extends JPanel {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -8701347662757286944L;

	/** The browse button. */
	private JButton browseButton;

	/** The clear capture cache button. */
	private JButton clearCaptureCacheButton;

	/** The clear transfer cache button. */
	private JButton clearTransferCacheButton;

	/** The j file chooser. */
	private JFileChooser jFileChooser;

	/** The j label1. */
	private JLabel jLabel1;

	/** The j label2. */
	private JLabel jLabel2;

	/** The j label3. */
	private JLabel jLabel3;

	/** The j label4. */
	private JLabel jLabel4;

	/** The j label5. */
	private JLabel jLabel5;

	/** The prefs. */
	private WebConfigPrefsItem prefs;

	/** The txt cluster password. */
	private JPasswordField txtClusterPassword;

	/** The txt cluster username. */
	private JTextField txtClusterUsername;

	/** The txt shared location. */
	private JTextField txtSharedLocation;

	/** The txt web server dir. */
	private JTextField txtWebServerDir;

	/** The txt web server port. */
	private JTextField txtWebServerPort;

	/**
	 * Instantiates a new web config panel.
	 *
	 * @param serverConfigPrefsItem the server config prefs item
	 */
	public WebConfigPanel(WebConfigPrefsItem serverConfigPrefsItem) {
		this.prefs = serverConfigPrefsItem;
		initComponents();
	}

	/**
	 * Browse folders.
	 */
	private void browseFolders() {
		int returnVal = jFileChooser.showOpenDialog(this);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = jFileChooser.getSelectedFile();
			txtSharedLocation.setText(file.getAbsolutePath());
			prefs.setSharedLocation(file.getAbsolutePath());
		}
	}

	/**
	 * Capture message.
	 */
	private void captureMessage() {
		JOptionPane.showMessageDialog(this,
				"The Capture Cache has been Cleared.", "",
				JOptionPane.PLAIN_MESSAGE);
	}

	/**
	 * Inits the components.
	 */
	private void initComponents() {

		setName("Form");

		jLabel1 = new JLabel("Cluster port: ");
		txtWebServerPort = new JTextField();
		txtWebServerPort.setText(prefs.getPort() + "");
		txtWebServerPort.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				store();
			}

			@Override
			public void keyTyped(KeyEvent e) {
				store();
			}

			private void store() {
				if (txtWebServerPort.getText().length() > 0) {
					try {
						prefs.setPort(Integer.parseInt(txtWebServerPort
								.getText()));
						;
						txtWebServerPort.setForeground(Color.black);
					} catch (NumberFormatException e) {
						txtWebServerPort.setForeground(Color.red);
					}
				}
			}
		});

		jLabel2 = new JLabel("Cluster host address: ");
		txtWebServerDir = new JTextField();
		txtWebServerDir.setText(prefs.getClusterHost());
		txtWebServerDir.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				store();
			}

			@Override
			public void keyTyped(KeyEvent e) {
				store();
			}

			private void store() {
				prefs.setClusterHost(txtWebServerDir.getText());
			}
		});

		jLabel3 = new JLabel("Device username: ");
		txtClusterUsername = new JTextField();
		txtClusterUsername.setText(prefs.getClusterUserName());
		txtClusterUsername.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				store();
			}

			@Override
			public void keyTyped(KeyEvent e) {
				store();
			}

			private void store() {
				prefs.setClusterUserName(txtClusterUsername.getText());
			}
		});

		jLabel4 = new JLabel("Device password: ");
		txtClusterPassword = new JPasswordField();
		txtClusterPassword.setText(prefs.getClusterPassword());
		txtClusterPassword.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				store();
			}

			@Override
			public void keyTyped(KeyEvent e) {
				store();
			}

			private void store() {
				prefs.setClusterPassword(new String(txtClusterPassword
						.getPassword()));
			}
		});

		jLabel5 = new JLabel("Shared location: ");
		txtSharedLocation = new JTextField();
		txtSharedLocation.setText(prefs.getSharedLocation());
		txtSharedLocation.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				store();
			}

			@Override
			public void keyTyped(KeyEvent e) {
				store();
			}

			private void store() {
				prefs.setSharedLocation(txtSharedLocation.getText());
			}
		});

		jFileChooser = new JFileChooser(new File(prefs.getSharedLocation()));
		jFileChooser.setDialogTitle("Browse...");
		jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		jFileChooser.setAcceptAllFileFilterUsed(false);

		browseButton = new javax.swing.JButton();
		browseButton.setText("Browse...");
		browseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				browseFolders();
			}
		});

		clearTransferCacheButton = new javax.swing.JButton();
		clearTransferCacheButton.setText("Clear Transfer Cache");
		clearTransferCacheButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				CacheOrganisation.clearTransferCaches();
				transferMessage();
			}
		});

		clearCaptureCacheButton = new javax.swing.JButton();
		clearCaptureCacheButton.setText("Clear Capture Cache");
		clearCaptureCacheButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				CacheOrganisation.clearCaptureCache();
				captureMessage();
			}
		});

		setLayout(null);

		jLabel2.setBounds(new Rectangle(30, 30, 175, 24));
		txtWebServerDir.setBounds(new Rectangle(215, 30, 150, 24));

		jLabel1.setBounds(new Rectangle(30, 60, 130, 24));
		txtWebServerPort.setBounds(new Rectangle(215, 60, 80, 24));

		jLabel3.setBounds(new Rectangle(30, 120, 130, 24));
		txtClusterUsername.setBounds(new Rectangle(215, 120, 150, 24));

		jLabel4.setBounds(new Rectangle(30, 150, 130, 24));
		txtClusterPassword.setBounds(new Rectangle(215, 150, 150, 24));

		jLabel5.setBounds(new Rectangle(30, 230, 210, 24));
		txtSharedLocation.setBounds(new Rectangle(150, 230, 275, 24));
		browseButton.setBounds(new Rectangle(423, 230, 100, 23));

		clearTransferCacheButton.setBounds(new Rectangle(30, 280, 200, 23));
		clearCaptureCacheButton.setBounds(new Rectangle(260, 280, 200, 23));

		add(jLabel2);
		add(txtWebServerDir);
		add(jLabel1);
		add(txtWebServerPort);
		add(jLabel3);
		add(txtClusterUsername);
		add(jLabel4);
		add(txtClusterPassword);
		add(jLabel5);
		add(txtSharedLocation);
		add(browseButton);
		add(clearTransferCacheButton);
		add(clearCaptureCacheButton);
	}

	/**
	 * Transfer message.
	 */
	private void transferMessage() {
		JOptionPane.showMessageDialog(this,
				"The Transfer Cache has been Cleared.", "",
				JOptionPane.PLAIN_MESSAGE);
	}
}