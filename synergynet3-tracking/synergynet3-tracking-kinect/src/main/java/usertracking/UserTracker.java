// Utilises Andrew Davison's ArniesTracker (http://fivedots.coe.psu.ac.th/~ad/jg/nui15/)

package usertracking;

import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;

import synergynet3.cluster.SynergyNetCluster;
import synergynet3.positioning.SynergyNetPosition;
import synergynet3.tracking.network.core.TrackingDeviceControl;
import usertracking.networking.TeacherControlPanel;
import usertracking.networking.TrackerPositioning;
import usertracking.networking.TrackerSync;

public class UserTracker extends JFrame{
	
	private static final long serialVersionUID = -7672032252122915603L;
	
	public static float PROXIMITY_TO_TABLE_THRESHOLD = 3f;
	
	public final static int UPDATE_TIME = 175;
	
	public static final int VIEW_WIDTH = 640;
	public static final int VIEW_HEIGHT = 480;	
	
	public static float TRACKER_HEIGHT = 2048f;
	public static TrackerSync trackerSync;
	
	public static final String ALL_TABLES = "all";
	public static ArrayList<String> SELECTED_TABLES = new ArrayList<String>();
	public static int GESTURING_USER = -1;
		
	private static TrackerPanel trackPanel; 	

	private static JTextArea announcementBox;

	public static float TRACKER_LOCATION_X, TRACKER_LOCATION_Y, TRACKER_ORIENTATION = 0;
	
	public UserTracker(){
		super("User Tracker");
		
		SynergyNetPosition pos = TrackerPositioning.getLocalDeviceLocationPosOnly();			
		TRACKER_LOCATION_X = pos.getXinMetres();
		TRACKER_LOCATION_Y = pos.getYinMetres();
		TRACKER_ORIENTATION = pos.getOrientation(); 
		TRACKER_HEIGHT = pos.getInterfaceHeightFromFloorinMetres();
				
		TeacherControlPanel teacherControlPanel = new TeacherControlPanel();		
		trackPanel = new TrackerPanel(teacherControlPanel);
		
		announcementBox = new JTextArea();
		JScrollPane scroll =new JScrollPane(announcementBox);
		scroll.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener(){
			public void adjustmentValueChanged(AdjustmentEvent e){
				announcementBox.select(announcementBox.getHeight()+1000,0);
			}}
		);
		
		announcementBox.setEditable(false);
		announcementBox.setText("Tracker started." + "\n  ");
		
		setLayout(null); 
		
		trackPanel.setBounds(0, 0, VIEW_WIDTH, VIEW_HEIGHT);
		teacherControlPanel.setBounds(0, VIEW_HEIGHT, VIEW_WIDTH/2, 160);
		scroll.setBounds(VIEW_WIDTH/2, VIEW_HEIGHT, VIEW_WIDTH/2-5, 140);
		
		add(trackPanel);
		add(teacherControlPanel);
		add(scroll);
		
		ActionListener actionListener = new ActionListener(){
			public void actionPerformed(ActionEvent actionEvent) {
				trackPanel.closeDown(); 
			}
		};
		
		KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
		rootPane.registerKeyboardAction(actionListener, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
		
		addWindowListener( new WindowAdapter() {
			public void windowClosing(WindowEvent e){
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
	
	public static void writeToAnnouncementBox(String announcement){
		if (announcementBox != null){
			String previousAnnouncements = announcementBox.getText();
			previousAnnouncements += " - " + announcement + "\n  ";
			announcementBox.setText(previousAnnouncements);
		}
	}
		
	
	public static void clearSequences(){
		trackPanel.clearSequences();
	}
	
	public static void main(String args[]){
		new UserTracker();  
	}
}