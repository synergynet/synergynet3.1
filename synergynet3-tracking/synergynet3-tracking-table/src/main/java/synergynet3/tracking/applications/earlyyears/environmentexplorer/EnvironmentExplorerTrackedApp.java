package synergynet3.tracking.applications.earlyyears.environmentexplorer;

import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Level;

import com.jme3.math.Vector2f;

import synergynet3.additionalUtils.AdditionalSynergyNetUtilities;
import synergynet3.additionalitems.PerformActionOnAllDescendents;
import synergynet3.additionalitems.interfaces.ICachableImage;
import synergynet3.feedbacksystem.defaultfeedbacktypes.AudioFeedback;
import synergynet3.feedbacksystem.defaultfeedbacktypes.SimpleTrafficLightFeedback;
import synergynet3.feedbacksystem.defaultfeedbacktypes.SmilieFeedback;
import synergynet3.feedbacksystem.defaultfeedbacktypes.YesOrNoFeedback;
import synergynet3.tracking.applications.TrackedApp;
import synergynet3.tracking.applications.earlyyears.EarlyYearsTrackedApp;

import multiplicity3.appsystem.MultiplicityClient;
import multiplicity3.config.identity.IdentityConfigPrefsItem;
import multiplicity3.csys.factory.ContentTypeNotBoundException;
import multiplicity3.csys.items.item.IItem;

public class EnvironmentExplorerTrackedApp extends EarlyYearsTrackedApp {
	
	private static final String MARKER_IMAGE = "synergynet3/earlyyears/table/applications/environmentexplorer/removeFromGallery.png";
	
	private TeacherTableControls teachTableControls;
	private ICachableImage map;
	private ArrayList<ICachableImage> markers = new ArrayList<ICachableImage>();
	
	@Override
	protected void loadDefaultContent() throws IOException, ContentTypeNotBoundException {
		
		feedbackTypes.add(SimpleTrafficLightFeedback.class);
		feedbackTypes.add(AudioFeedback.class);
		feedbackTypes.add(SmilieFeedback.class);
		feedbackTypes.add(YesOrNoFeedback.class);
		
		teachTableControls = new TeacherTableControls(stage, this);
		teachTableControls.setVisibility(false, stage);
		
		map = stage.getContentFactory().create(ICachableImage.class, "userIcon", UUID.randomUUID());
		map.setSize(displayWidth, displayHeight);
		map.setRelativeScale(0.8f);
		map.setInteractionEnabled(false);
		stage.addItem(map);
		stage.getZOrderManager().sendToBottom(map);
		map.getZOrderManager().setBringToTopPropagatesUp(false);
		map.getZOrderManager().setAutoBringToTop(false);	
		
	}

	@Override
	protected String getSpecificFriendlyAppName(){
		return "EnvironmentExplorer";
	}
	
	public void setTeacherControlVisibility(boolean visible){
		teachTableControls.setVisibility(visible, stage);
	}
	
	public void setMap(IItem item){
		
		new PerformActionOnAllDescendents(item, false, true){
			@Override
			protected void actionOnDescendent(IItem child){	
				try{
					ICachableImage image = (ICachableImage)child;
					map.setImage(image.getImage());		
					
					for (ICachableImage marker: markers){
						stage.removeItem(marker);
					}
					markers.clear();
					
					stop = true;
				}catch(ClassCastException e){}
			}
		};
	}

	public void createMarker(Vector2f relativeLocation, float relativeRotation) {
		try{
			ICachableImage marker = stage.getContentFactory().create(ICachableImage.class, "userIcon", UUID.randomUUID());
			marker.setImage(MARKER_IMAGE);		
			marker.setSize(40, 40);	
			marker.setInteractionEnabled(false);	
			marker.setRelativeLocation(new Vector2f(relativeLocation.x - displayWidth/2, relativeLocation.y - displayHeight/2));
			marker.setRelativeRotation(relativeRotation);
			stage.addItem(marker);
			marker.getZOrderManager().setBringToTopPropagatesUp(false);
			marker.getZOrderManager().setAutoBringToTop(false);			
			markers.add(marker);
		}catch(ContentTypeNotBoundException e){
			AdditionalSynergyNetUtilities.log(Level.SEVERE, "Content Type NotBound Exception Exception.", e);
		}		
	}
	
	public static void main(String[] args) throws SocketException {
		if(args.length > 0) {
			IdentityConfigPrefsItem idprefs = new IdentityConfigPrefsItem();
			idprefs.setID(args[0]);
		}
		
		TrackedApp.initialiseTrackingAppArgs(args);
		
		MultiplicityClient client = MultiplicityClient.get();
		client.start();
		EnvironmentExplorerTrackedApp app = new EnvironmentExplorerTrackedApp();
		client.setCurrentApp(app);
		
	}
}
