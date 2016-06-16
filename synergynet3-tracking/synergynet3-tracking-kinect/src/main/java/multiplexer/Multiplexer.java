package multiplexer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;

import multiplexer.networking.MultiplexerSync;
import multiplexer.networking.TableUpdater;
import synergynet3.cluster.SynergyNetCluster;
import synergynet3.tracking.network.core.TrackingDeviceControl;
import synergynet3.tracking.network.shared.CombinedUserEntity;

/**
 * The Class Multiplexer.
 */
public class Multiplexer extends JFrame
{

	/** The is running. */
	public static boolean isRunning = true;

	/** The multiplexer sync. */
	public static MultiplexerSync multiplexerSync;

	/** The multiplexing threshold in metres. */
	public static float MULTIPLEXING_THRESHOLD_IN_METRES = 0.1f;

	/** The tracker orientation. */
	public static float TRACKER_LOCATION_X, TRACKER_LOCATION_Y, TRACKER_ORIENTATION = 0;

	/** The users. */
	public static ArrayList<CombinedUserEntity> users = new ArrayList<CombinedUserEntity>();

	/** The Constant WINDOW_HEIGHT. */
	public static final int WINDOW_HEIGHT = 400;

	/** The Constant WINDOW_WIDTH. */
	public static final int WINDOW_WIDTH = 400;

	/** The announcement box. */
	private static JTextArea announcementBox;

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -3316332902141558084L;

	/**
	 * Instantiates a new multiplexer.
	 */
	public Multiplexer()
	{
		super("Multiplexer");

		announcementBox = new JTextArea();
		JScrollPane scroll = new JScrollPane(announcementBox);
		scroll.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener()
		{
			@Override
			public void adjustmentValueChanged(AdjustmentEvent e)
			{
				announcementBox.select(announcementBox.getHeight() + 1000, 0);
			}
		});

		announcementBox.setEditable(false);
		announcementBox.setText("Multiplexer started." + "\n  ");
		add(scroll);

		ActionListener actionListener = new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent actionEvent)
			{
				isRunning = false;
			}
		};

		KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
		rootPane.registerKeyboardAction(actionListener, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);

		addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				isRunning = false;
			}
		});

		setResizable(false);
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		setLocationRelativeTo(null);
		setVisible(true);

		TrackingDeviceControl multiplexerDeviceController = new TrackingDeviceControl(SynergyNetCluster.get().getIdentity());
		multiplexerSync = new MultiplexerSync(multiplexerDeviceController);

		TableUpdater.updateTablesThread.start();

	}

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 */
	public static void main(String args[])
	{
		new Multiplexer();
		try
		{
			MULTIPLEXING_THRESHOLD_IN_METRES = Float.parseFloat(ManagementFactory.getRuntimeMXBean().getSystemProperties().get("threshold"));
		}
		catch (Exception e)
		{
			writeToAnnouncementBox("No multiplexing threshold argument given, using default.");
		}
		writeToAnnouncementBox("Multiplexing Threshold: " + MULTIPLEXING_THRESHOLD_IN_METRES + "m");
	}

	/**
	 * Write to announcement box.
	 *
	 * @param announcement
	 *            the announcement
	 */
	public static void writeToAnnouncementBox(String announcement)
	{
		String previousAnnouncements = announcementBox.getText();
		previousAnnouncements += " - " + announcement + "\n  ";
		announcementBox.setText(previousAnnouncements);
	}

}
