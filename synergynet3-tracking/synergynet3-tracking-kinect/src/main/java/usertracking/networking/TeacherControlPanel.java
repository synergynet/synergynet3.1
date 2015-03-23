package usertracking.networking;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import org.OpenNI.StatusException;

import synergynet3.tracking.network.shared.UserColourUtils;
import synergynet3.tracking.network.shared.UserLocation;
import usertracking.GestureActions;
import usertracking.Skeletons;
import usertracking.UserTracker;

/**
 * The Class TeacherControlPanel.
 */
public class TeacherControlPanel extends JPanel {

	/** The instance. */
	private static TeacherControlPanel instance = null;

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -7442033945894959343L;

	/** The column names. */
	private Vector<String> columnNames = new Vector<String>();

	/** The previous teacher i ds. */
	private ArrayList<Integer> previousTeacherIDs = new ArrayList<Integer>();

	/** The skeletons. */
	private Skeletons skeletons;

	/** The table. */
	private JTable table;

	/**
	 * Instantiates a new teacher control panel.
	 */
	public TeacherControlPanel() {

		instance = this;

		columnNames.add("");
		columnNames.add("Person ID");
		columnNames.add("Teacher");

		table = new JTable() {
			private static final long serialVersionUID = 8954651553916743577L;

			public boolean isCellEditable(int row, int col) {
				return false;
			}
		};

		DefaultTableCellRenderer colourCellRenderer = new DefaultTableCellRenderer() {

			private static final long serialVersionUID = -4894645357183806210L;

			@Override
			public Component getTableCellRendererComponent(JTable table,
					Object value, boolean isSelected, boolean hasFocus,
					int row, int column) {
				String number = (String) value;
				try {
					int userID = Integer.parseInt(number);
					Color colour = UserColourUtils
							.getColour(TrackerNetworking.uniqueIDs[userID]);
					setBackground(colour);
					setText("");
				} catch (NumberFormatException e) {
				}
				return this;
			}
		};

		DefaultTableCellRenderer dataCellsRenderer = new DefaultTableCellRenderer();
		dataCellsRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		dataCellsRenderer.setBackground(Color.white);

		fillTable();

		TableColumn col = table.getColumnModel().getColumn(0);
		col.setCellRenderer(colourCellRenderer);
		col.setPreferredWidth(25);

		table.getColumnModel().getColumn(1).setCellRenderer(dataCellsRenderer);
		table.getColumnModel().getColumn(2).setCellRenderer(dataCellsRenderer);

		table.setPreferredScrollableViewportSize(new Dimension(150, 120));
		table.setCellSelectionEnabled(false);
		table.setRowSelectionAllowed(true);
		table.setAutoCreateColumnsFromModel(false);

		JScrollPane tableArea = new JScrollPane(table,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		tableArea.setPreferredSize(new Dimension(150, 120));

		JButton makeTeacherButton = new JButton("Make Teacher");
		makeTeacherButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				int target = table.getSelectedRow();
				if (target >= 0) {
					int userID = Integer.parseInt(table.getValueAt(target, 1)
							.toString());
					makeTeacher(userID);
				}
			}

		});
		JButton makeStudentButton = new JButton("Make Student");
		makeStudentButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				int target = table.getSelectedRow();
				if (target >= 0) {
					int userID = Integer.parseInt(table.getValueAt(target, 1)
							.toString());
					makeStudent(userID);
				}
			}

		});
		setLayout(null);

		tableArea.setBounds(new Rectangle(10, 10, 150, 120));

		makeTeacherButton.setBounds(new Rectangle(170, 40, 125, 24));
		makeStudentButton.setBounds(new Rectangle(170, 80, 125, 24));

		add(tableArea);
		add(makeTeacherButton);
		add(makeStudentButton);
	}

	/**
	 * Gets the single instance of TeacherControlPanel.
	 *
	 * @return single instance of TeacherControlPanel
	 */
	public static TeacherControlPanel getInstance() {
		return instance;
	}

	/**
	 * Fill table.
	 */
	public void fillTable() {
		Vector<Vector<String>> data = new Vector<Vector<String>>();

		ArrayList<Integer> newTeacherIDs = new ArrayList<Integer>();

		synchronized (TrackerNetworking.userLocations) {
			for (UserLocation user : TrackerNetworking.userLocations
					.getUserLocations()) {
				Vector<String> userString = new Vector<String>();
				userString.add("" + user.getID());
				userString.add("" + user.getID());

				if (isTeacher(user.getID())) {
					userString.add("Y");
					newTeacherIDs.add(user.getID());
					if (!previousTeacherIDs.contains(user.getID())) {
						startTeacherTracking(user.getID());
					}
				} else {
					userString.add("");
					if (previousTeacherIDs.contains(user.getID())) {
						stopTeacherTracking(user.getID());
					}
				}

				data.add(userString);
			}
		}

		DefaultTableModel dtm = new DefaultTableModel(data, columnNames);
		table.setModel(dtm);

		previousTeacherIDs = new ArrayList<Integer>(newTeacherIDs);

	}

	/**
	 * Checks if is teacher.
	 *
	 * @param userID the user id
	 * @return true, if is teacher
	 */
	public boolean isTeacher(int userID) {
		if (userID <= 0) {
			return false;
		}
		boolean toReturn = TrackerNetworking.teacherStatuses[userID];
		return toReturn;
	}

	/**
	 * Make student.
	 *
	 * @param userID the user id
	 */
	public void makeStudent(int userID) {
		if (isTeacher(userID)) {
			TrackerNetworking.sendTeacherStatusUpdateMessage(userID, false);
			if (UserTracker.GESTURING_USER == TrackerNetworking.uniqueIDs[userID]) {
				GestureActions.disableGestureControl();
			}
		}
	}

	/**
	 * Make teacher.
	 *
	 * @param userID the user id
	 */
	public void makeTeacher(int userID) {
		if (!isTeacher(userID)) {
			TrackerNetworking.sendTeacherStatusUpdateMessage(userID, true);
		}
	}

	/**
	 * Sets the pose detection.
	 *
	 * @param skeletons the new pose detection
	 */
	public void setPoseDetection(Skeletons skeletons) {
		this.skeletons = skeletons;
	}

	/**
	 * Start teacher tracking.
	 *
	 * @param userID the user id
	 */
	private void startTeacherTracking(int userID) {
		try {
			// try to detect a pose for the new user
			if (userID > -1) {
				skeletons.getSkelCap().startTracking(userID);
			}
		} catch (StatusException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Stop teacher tracking.
	 *
	 * @param userID the user id
	 */
	private void stopTeacherTracking(int userID) {
		try {
			if (userID > -1) {
				if (skeletons.getSkelCap().isSkeletonTracking(userID)) {
					skeletons.getPoseDetectionCap().stopPoseDetection(userID);
					skeletons.getSkelCap().stopTracking(userID);
				}
			}
		} catch (StatusException e) {
			e.printStackTrace();
		}
	}

}
