package multiplicity3.config.server;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JPanel;

/**
 * The Class ServerConfigPanel.
 */
public class ServerConfigPanel extends JPanel {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -8701347662757286944L;

	/** The btn select dir. */
	private javax.swing.JButton btnSelectDir;

	/** The j label1. */
	private javax.swing.JLabel jLabel1;

	/** The j label2. */
	private javax.swing.JLabel jLabel2;

	/** The prefs. */
	private ServerConfigPrefsItem prefs;

	/** The txt web server dir. */
	private javax.swing.JTextField txtWebServerDir;

	/** The txt web server port. */
	private javax.swing.JTextField txtWebServerPort;

	/**
	 * Instantiates a new server config panel.
	 *
	 * @param serverConfigPrefsItem the server config prefs item
	 */
	public ServerConfigPanel(ServerConfigPrefsItem serverConfigPrefsItem) {
		this.prefs = serverConfigPrefsItem;
		initComponents();
	}

	/**
	 * Inits the components.
	 */
	private void initComponents() {
		final JPanel instance = this;
		jLabel1 = new javax.swing.JLabel();
		txtWebServerPort = new javax.swing.JTextField();
		jLabel2 = new javax.swing.JLabel();
		txtWebServerDir = new javax.swing.JTextField();
		btnSelectDir = new javax.swing.JButton();

		setName("Form"); // NOI18N

		jLabel1.setText("Web Server Directory:");
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
				prefs.setPort(Integer.parseInt(txtWebServerPort.getText()));
			}
		});

		jLabel2.setText("Web Server Port:");
		txtWebServerDir.setText(prefs.getWebDirectory());
		txtWebServerDir.setEditable(false);

		btnSelectDir.setText("Browse...");
		btnSelectDir.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				final JFileChooser fc = new JFileChooser(new File(prefs
						.getWebDirectory()));
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int returnVal = fc.showOpenDialog(instance);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					txtWebServerDir.setText(file.getAbsolutePath());
					prefs.setWebDirectory(file.getAbsolutePath());
				}
			}

		});

		setLayout(null);

		jLabel1.setBounds(new Rectangle(30, 30, 200, 24));
		txtWebServerDir.setBounds(new Rectangle(190, 30, 250, 24));
		btnSelectDir.setBounds(new Rectangle(450, 30, 120, 24));
		jLabel2.setBounds(new Rectangle(30, 60, 200, 24));
		txtWebServerPort.setBounds(new Rectangle(190, 60, 50, 24));

		add(jLabel1);
		add(txtWebServerDir);
		add(jLabel2);
		add(txtWebServerPort);
		add(btnSelectDir);
	}// </editor-fold>

}