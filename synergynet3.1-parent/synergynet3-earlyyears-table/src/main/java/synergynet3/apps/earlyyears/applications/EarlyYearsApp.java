package synergynet3.apps.earlyyears.applications;

import java.util.ArrayList;

import synergynet3.SynergyNetApp;
import synergynet3.apps.earlyyears.applications.environmentexplorer.EnvironmentExplorerApp;
import synergynet3.apps.earlyyears.applications.stickerbook.StickerbookApp;
import synergynet3.apps.earlyyears.applications.traintracks.TrainTracksApp;
import synergynet3.apps.earlyyears.network.EarlyYearsSync;
import synergynet3.cluster.SynergyNetCluster;
import synergynet3.feedbacksystem.FeedbackSystem;
import synergynet3.studentmenucontrol.StudentMenuUtilities;
import synergynet3.web.earlyyears.core.EarlyYearsDeviceControl;
import synergynet3.web.earlyyears.shared.EarlyYearsActivity;

import multiplicity3.appsystem.IQueueOwner;
import multiplicity3.appsystem.MultiplicityClient;

import multiplicity3.input.MultiTouchInputComponent;

abstract public class EarlyYearsApp extends SynergyNetApp {
	
	private static EarlyYearsSync eySync = null;
	private static String earlyYearsTableIdentity;
	
	public EarlyYearsActivity syncName;
	
	protected int displayWidth = 1024;
	protected int displayHeight = 768;
		
	@Override
	public void shouldStart(MultiTouchInputComponent input, IQueueOwner iqo) {
		super.shouldStart(input, iqo);
		
		displayWidth = (int) (stage.getWorldLocation().x * 2);
		displayHeight = (int) (stage.getWorldLocation().y * 2);
		
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
			
			if (newValue == EarlyYearsActivity.ENVIRONMENT_EXPLORER){
				EnvironmentExplorerApp app = new EnvironmentExplorerApp();
				app.loginAll(names);
				MultiplicityClient.get().setCurrentApp(app);					
			}else if(newValue == EarlyYearsActivity.TRAIN_TRACKS){
				TrainTracksApp app = new TrainTracksApp();
				app.loginAll(names);
				MultiplicityClient.get().setCurrentApp(app);				
			}else if(newValue == EarlyYearsActivity.STICKER_BOOK){
				StickerbookApp app = new StickerbookApp();
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
