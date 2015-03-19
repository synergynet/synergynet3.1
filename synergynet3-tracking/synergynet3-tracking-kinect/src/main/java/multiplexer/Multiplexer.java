package multiplexer;

import java.awt.event.*;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;

import javax.swing.*;

import multiplexer.networking.MultiplexerSync;
import multiplexer.networking.TableUpdater;

import synergynet3.cluster.SynergyNetCluster;
import synergynet3.tracking.network.core.TrackingDeviceControl;
import synergynet3.tracking.network.shared.CombinedUserEntity;

public class Multiplexer extends JFrame{

	private static final long serialVersionUID = -3316332902141558084L;
	public static final int WINDOW_WIDTH = 400;
	public static final int WINDOW_HEIGHT = 400;	
	
	public static float MULTIPLEXING_THRESHOLD_IN_METRES = 0.1f;

	public static ArrayList<CombinedUserEntity> users = new ArrayList<CombinedUserEntity>();
	
	public static MultiplexerSync multiplexerSync;
	
	public static boolean isRunning = true;
	private static JTextArea announcementBox;

	public static float TRACKER_LOCATION_X, TRACKER_LOCATION_Y, TRACKER_ORIENTATION = 0;
	
	public Multiplexer(){
		super("Multiplexer");
		
		announcementBox = new JTextArea();
		JScrollPane scroll =new JScrollPane(announcementBox);
		scroll.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener(){
			public void adjustmentValueChanged(AdjustmentEvent e){
				announcementBox.select(announcementBox.getHeight()+1000,0);
			}}
		);
		
		announcementBox.setEditable(false);
		announcementBox.setText("Multiplexer started."  + "\n  ");
		add(scroll);
		
		ActionListener actionListener = new ActionListener(){
			public void actionPerformed(ActionEvent actionEvent) {
				isRunning = false;
			}
		};
		
		KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
		rootPane.registerKeyboardAction(actionListener, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
		
		addWindowListener( new WindowAdapter() {
			public void windowClosing(WindowEvent e){
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
	
	public static void writeToAnnouncementBox(String announcement){
		String previousAnnouncements = announcementBox.getText();
		previousAnnouncements += " - " + announcement + "\n  ";
		announcementBox.setText(previousAnnouncements);
	}
		
	public static void main(String args[]){
		new Multiplexer();  
		try{
			MULTIPLEXING_THRESHOLD_IN_METRES = Float.parseFloat(ManagementFactory.getRuntimeMXBean().getSystemProperties().get("threshold"));
		}catch(Exception e){
			writeToAnnouncementBox("No multiplexing threshold argument given, using default.");
		}
		writeToAnnouncementBox("Multiplexing Threshold: " + MULTIPLEXING_THRESHOLD_IN_METRES + "m");		
	}
	
}