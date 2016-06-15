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
public class WebConfigPanel extends JPanel
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -8701347662757286944L;

	/** The browse button. */
	private JButton browseButton;

	/** The clear capture cache button. */
	private JButton clearCaptureCacheButton;

	/** The clear transfer cache button. */
	private JButton clearTransferCacheButton;

	/** The file chooser. */
	private JFileChooser jFileChooser;

	/** The Web server port label. */
	private JLabel webServerPortLabel;

	/** The web server directory label. */
	private JLabel webServerDirLabel;

	/** The cluster interface label. */
	private JLabel clusterInterfaceLabel;

	/** The cluster name label. */
	private JLabel clusterNameLabel;

	/** The cluster password label. */
	private JLabel clusterPasswordLabel;

	/** The shared location Label. */
	private JLabel sharedLocationLabel;

	/** The prefs. */
	private WebConfigPrefsItem prefs;

	/** The cluster interface text field. */
	private JTextField clusterInterfaceField;

	/** The cluster password text field. */
	private JPasswordField clusterPasswordField;

	/** The cluster username text field. */
	private JTextField clusterUsernameField;

	/** The shared location text field. */
	private JTextField sharedLocationField;

	/** The web server directory text field. */
	private JTextField webServerDirField;

	/** The web server port text field. */
	private JTextField webServerPortField;

	/**
	 * Instantiates a new web config panel.
	 *
	 * @param serverConfigPrefsItem
	 *            the server config prefs item
	 */
	public WebConfigPanel(WebConfigPrefsItem serverConfigPrefsItem)
	{
		this.prefs = serverConfigPrefsItem;
		initComponents();
	}

	/**
	 * Browse folders.
	 */
	private void browseFolders()
	{
		int returnVal = jFileChooser.showOpenDialog(this);

		if (returnVal == JFileChooser.APPROVE_OPTION)
		{
			File file = jFileChooser.getSelectedFile();
			sharedLocationField.setText(file.getAbsolutePath());
			prefs.setSharedLocation(file.getAbsolutePath());
		}
	}

	/**
	 * Capture message.
	 */
	private void captureMessage()
	{
		JOptionPane.showMessageDialog(this, "The Capture Cache has been Cleared.", "", JOptionPane.PLAIN_MESSAGE);
	}

	/**
	 * Inits the components.
	 */
	private void initComponents()
	{

		setName("Form");

		webServerPortLabel = new JLabel("Cluster port: ");
		webServerPortField = new JTextField();
		webServerPortField.setText(prefs.getPort() + "");
		webServerPortField.addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyReleased(KeyEvent e)
			{
				store();
			}

			@Override
			public void keyTyped(KeyEvent e)
			{
				store();
			}

			private void store()
			{
				if (webServerPortField.getText().length() > 0)
				{
					try
					{
						prefs.setPort(Integer.parseInt(webServerPortField.getText()));
						;
						webServerPortField.setForeground(Color.black);
					}
					catch (NumberFormatException e)
					{
						webServerPortField.setForeground(Color.red);
					}
				}
			}
		});

		webServerDirLabel = new JLabel("Cluster host address: ");
		webServerDirField = new JTextField();
		webServerDirField.setText(prefs.getClusterHost());
		webServerDirField.addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyReleased(KeyEvent e)
			{
				store();
			}

			@Override
			public void keyTyped(KeyEvent e)
			{
				store();
			}

			private void store()
			{
				prefs.setClusterHost(webServerDirField.getText());
			}
		});

		clusterNameLabel = new JLabel("Device username: ");
		clusterUsernameField = new JTextField();
		clusterUsernameField.setText(prefs.getClusterUserName());
		clusterUsernameField.addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyReleased(KeyEvent e)
			{
				store();
			}

			@Override
			public void keyTyped(KeyEvent e)
			{
				store();
			}

			private void store()
			{
				prefs.setClusterUserName(clusterUsernameField.getText());
			}
		});

		clusterInterfaceLabel = new JLabel("Cluster interface: ");
		clusterInterfaceField = new JTextField();
		clusterInterfaceField.setText(prefs.getClusterInterface());
		clusterInterfaceField.addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyReleased(KeyEvent e)
			{
				store();
			}

			@Override
			public void keyTyped(KeyEvent e)
			{
				store();
			}

			private void store()
			{
				prefs.setClusterInterface(clusterInterfaceField.getText());
			}
		});

		clusterPasswordLabel = new JLabel("Device password: ");
		clusterPasswordField = new JPasswordField();
		clusterPasswordField.setText(prefs.getClusterPassword());
		clusterPasswordField.addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyReleased(KeyEvent e)
			{
				store();
			}

			@Override
			public void keyTyped(KeyEvent e)
			{
				store();
			}

			private void store()
			{
				prefs.setClusterPassword(new String(clusterPasswordField.getPassword()));
			}
		});

		sharedLocationLabel = new JLabel("Shared location: ");
		sharedLocationField = new JTextField();
		sharedLocationField.setText(prefs.getSharedLocation());
		sharedLocationField.addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyReleased(KeyEvent e)
			{
				store();
			}

			@Override
			public void keyTyped(KeyEvent e)
			{
				store();
			}

			private void store()
			{
				prefs.setSharedLocation(sharedLocationField.getText());
			}
		});

		jFileChooser = new JFileChooser(new File(prefs.getSharedLocation()));
		jFileChooser.setDialogTitle("Browse...");
		jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		jFileChooser.setAcceptAllFileFilterUsed(false);

		browseButton = new javax.swing.JButton();
		browseButton.setText("Browse...");
		browseButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent evt)
			{
				browseFolders();
			}
		});

		clearTransferCacheButton = new javax.swing.JButton();
		clearTransferCacheButton.setText("Clear Transfer Cache");
		clearTransferCacheButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent evt)
			{
				CacheOrganisation.clearTransferCaches();
				transferMessage();
			}
		});

		clearCaptureCacheButton = new javax.swing.JButton();
		clearCaptureCacheButton.setText("Clear Capture Cache");
		clearCaptureCacheButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent evt)
			{
				CacheOrganisation.clearCaptureCache();
				captureMessage();
			}
		});

		setLayout(null);

		int y = 30;

		webServerDirLabel.setBounds(new Rectangle(30, y, 175, 24));
		webServerDirField.setBounds(new Rectangle(215, y, 150, 24));

		y += 30;

		webServerPortLabel.setBounds(new Rectangle(30, y, 130, 24));
		webServerPortField.setBounds(new Rectangle(215, y, 80, 24));

		y += 60;

		clusterNameLabel.setBounds(new Rectangle(30, y, 130, 24));
		clusterUsernameField.setBounds(new Rectangle(215, y, 150, 24));

		y += 30;

		clusterPasswordLabel.setBounds(new Rectangle(30, y, 130, 24));
		clusterPasswordField.setBounds(new Rectangle(215, y, 150, 24));

		y += 30;

		clusterInterfaceLabel.setBounds(new Rectangle(30, y, 130, 24));
		clusterInterfaceField.setBounds(new Rectangle(215, y, 150, 24));

		y += 80;

		sharedLocationLabel.setBounds(new Rectangle(30, y, 210, 24));
		sharedLocationField.setBounds(new Rectangle(150, y, 275, 24));
		browseButton.setBounds(new Rectangle(423, y, 100, 23));

		y += 50;

		clearTransferCacheButton.setBounds(new Rectangle(30, y, 200, 23));
		clearCaptureCacheButton.setBounds(new Rectangle(260, y, 200, 23));

		add(webServerDirLabel);
		add(webServerDirField);
		add(webServerPortLabel);
		add(webServerPortField);
		add(clusterNameLabel);
		add(clusterUsernameField);
		add(clusterPasswordLabel);
		add(clusterPasswordField);
		add(clusterInterfaceLabel);
		add(clusterInterfaceField);
		add(sharedLocationLabel);
		add(sharedLocationField);
		add(browseButton);
		add(clearTransferCacheButton);
		add(clearCaptureCacheButton);
	}

	/**
	 * Transfer message.
	 */
	private void transferMessage()
	{
		JOptionPane.showMessageDialog(this, "The Transfer Cache has been Cleared.", "", JOptionPane.PLAIN_MESSAGE);
	}
}
