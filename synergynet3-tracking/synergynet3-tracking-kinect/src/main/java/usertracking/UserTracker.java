// Utilises Andrew Davison's ArniesTracker
// (http://fivedots.coe.psu.ac.th/~ad/jg/nui15/)

package usertracking;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;

import synergynet3.cluster.SynergyNetCluster;
import synergynet3.positioning.SynergyNetPosition;
import synergynet3.tracking.network.core.TrackingDeviceControl;
import usertracking.networking.TeacherControlPanel;
import usertracking.networking.TrackerPositioning;
import usertracking.networking.TrackerSync;

/**
 * The Class UserTracker.
 */
public class UserTracker extends JFrame
{

	/** The Constant ALL_TABLES. */
	public static final String ALL_TABLES = "all";

	/** The gesturing user. */
	public static int GESTURING_USER = -1;

	/** The proximity to table threshold. */
	public static float PROXIMITY_TO_TABLE_THRESHOLD = 3f;

	/** The selected tables. */
	public static ArrayList<String> SELECTED_TABLES = new ArrayList<String>();

	/** The tracker height. */
	public static float TRACKER_HEIGHT = 2048f;

	/** The tracker orientation. */
	public static float TRACKER_LOCATION_X, TRACKER_LOCATION_Y, TRACKER_ORIENTATION = 0;

	/** The tracker sync. */
	public static TrackerSync trackerSync;

	/** The Constant UPDATE_TIME. */
	public final static int UPDATE_TIME = 175;

	/** The Constant VIEW_HEIGHT. */
	public static final int VIEW_HEIGHT = 480;

	/** The Constant VIEW_WIDTH. */
	public static final int VIEW_WIDTH = 640;

	/** The announcement box. */
	private static JTextArea announcementBox;

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -7672032252122915603L;

	/** The track panel. */
	private static TrackerPanel trackPanel;

	/**
	 * Instantiates a new user tracker.
	 */
	public UserTracker()
	{
		super("User Tracker");

		SynergyNetPosition pos = TrackerPositioning.getLocalDeviceLocationPosOnly();
		TRACKER_LOCATION_X = pos.getXinMetres();
		TRACKER_LOCATION_Y = pos.getYinMetres();
		TRACKER_ORIENTATION = pos.getOrientation();
		TRACKER_HEIGHT = pos.getInterfaceHeightFromFloorinMetres();

		TeacherControlPanel teacherControlPanel = new TeacherControlPanel();
		trackPanel = new TrackerPanel(teacherControlPanel);

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
		announcementBox.setText("Tracker started." + "\n  ");

		setLayout(null);

		trackPanel.setBounds(0, 0, VIEW_WIDTH, VIEW_HEIGHT);
		teacherControlPanel.setBounds(0, VIEW_HEIGHT, VIEW_WIDTH / 2, 160);
		scroll.setBounds(VIEW_WIDTH / 2, VIEW_HEIGHT, (VIEW_WIDTH / 2) - 5, 140);

		add(trackPanel);
		add(teacherControlPanel);
		add(scroll);

		ActionListener actionListener = new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent actionEvent)
			{
				trackPanel.closeDown();
			}
		};

		KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
		rootPane.registerKeyboardAction(actionListener, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);

		addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				trackPanel.closeDown();
			}
		});

		setResizable(false);
		setSize(trackPanel.getWidth(), trackPanel.getHeight() + teacherControlPanel.getHeight());
		setLocationRelativeTo(null);
		setVisible(true);

		TrackingDeviceControl trackingDeviceController = new TrackingDeviceControl(SynergyNetCluster.get().getIdentity());
		trackerSync = new TrackerSync(trackingDeviceController);

		GestureActions.poseThread.start();

	}

	/**
	 * Clear sequences.
	 */
	public static void clearSequences()
	{
		trackPanel.clearSequences();
	}

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 */
	public static void main(String args[])
	{
		new UserTracker();
	}

	/**
	 * Write to announcement box.
	 *
	 * @param announcement
	 *            the announcement
	 */
	public static void writeToAnnouncementBox(String announcement)
	{
		if (announcementBox != null)
		{
			String previousAnnouncements = announcementBox.getText();
			previousAnnouncements += " - " + announcement + "\n  ";
			announcementBox.setText(previousAnnouncements);
		}
	}
}
