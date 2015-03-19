package synergynet3.tracking.applications.earlyyears;

import java.util.ArrayList;
import java.util.HashMap;

import synergynet3.cluster.SynergyNetCluster;
import synergynet3.feedbacksystem.FeedbackSystem;
import synergynet3.studentmenucontrol.StudentMenuUtilities;
import synergynet3.tracking.applications.TrackedApp;
import synergynet3.tracking.applications.earlyyears.environmentexplorer.EnvironmentExplorerTrackedApp;
import synergynet3.tracking.applications.earlyyears.network.EarlyYearsSync;
import synergynet3.tracking.applications.earlyyears.stickerbook.StickerbookTrackedApp;
import synergynet3.tracking.applications.earlyyears.traintracks.TrainTracksTrackedApp;
import synergynet3.web.earlyyears.core.EarlyYearsDeviceControl;
import synergynet3.web.earlyyears.shared.EarlyYearsActivity;

import multiplicity3.appsystem.IQueueOwner;
import multiplicity3.appsystem.MultiplicityClient;
import multiplicity3.csys.items.border.IRoundedBorder;

import multiplicity3.input.MultiTouchInputComponent;

abstract public class EarlyYearsTrackedApp extends TrackedApp {
	
	private static EarlyYearsSync eySync = null;
	private static String earlyYearsTableIdentity;
	private MultiTouchInputComponent input;
	
	public EarlyYearsActivity syncName;
	
	protected int displayWidth = 1024;
	protected int displayHeight = 768;
	
	private HashMap<Long, IRoundedBorder> cursors = new HashMap<Long, IRoundedBorder>();
		
	@Override
	public void shouldStart(MultiTouchInputComponent input, IQueueOwner iqo) {
		super.shouldStart(input, iqo);
		input.registerMultiTouchEventListener(this);
		
		displayWidth = (int) (stage.getWorldLocation().x * 2);
		displayHeight = (int) (stage.getWorldLocation().y * 2);
		
		this.input = input;
		
		if (eySync == null){
			earlyYearsTableIdentity = SynergyNetCluster.get().getIdentity();
			EarlyYearsDeviceControl earlyYearsDeviceController = new EarlyYearsDeviceControl(earlyYearsTableIdentity);
			eySync = new EarlyYearsSync(earlyYearsDeviceController, this);
		}else{
			eySync.reSync(this);
		}
		
	}
	
	public void setActivity(EarlyYearsActivity newValue) {
		if (newValue != syncName){
			
			ArrayList<String[]> names = new ArrayList<String[]>();
			
			for (int i = 0; i < StudentMenuUtilities.studentRepresentations.size(); i++){
				String[] name = new String[4];
				name[0] = StudentMenuUtilities.studentRepresentations.get(i).getStudentId();
				name[1] = "" + StudentMenuUtilities.studentMenus.get(i).getRadialMenu().getRelativeLocation().x;
				name[2] = "" + StudentMenuUtilities.studentMenus.get(i).getRadialMenu().getRelativeLocation().y;
				name[3] = "" + StudentMenuUtilities.studentMenus.get(i).getRadialMenu().getRelativeRotation();	
				names.add(name);
			}
			
			FeedbackSystem.clearFeedbackEligibleItems();
			
			cursors.clear();
			input.unregisterMultiTouchEventListener(this);
			
			if (newValue == EarlyYearsActivity.ENVIRONMENT_EXPLORER){
				EnvironmentExplorerTrackedApp app = new EnvironmentExplorerTrackedApp();
				app.loginAll(names);
				MultiplicityClient.get().setCurrentApp(app);					
			}else if(newValue == EarlyYearsActivity.TRAIN_TRACKS){
				TrainTracksTrackedApp app = new TrainTracksTrackedApp();
				app.loginAll(names);
				MultiplicityClient.get().setCurrentApp(app);				
			}else if(newValue == EarlyYearsActivity.STICKER_BOOK){
				StickerbookTrackedApp app = new StickerbookTrackedApp();
				app.loginAll(names);	
				MultiplicityClient.get().setCurrentApp(app);								
			}	

		}		
	}
	
	@Override
	public void onDestroy() {
		if (eySync != null)eySync.stop();
		super.onDestroy();
	}
}
