package synergynet3.activitypack2.table.videotest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import synergynet3.SynergyNetApp;
import synergynet3.additionalUtils.AdditionalSynergyNetUtilities;
import synergynet3.additionalitems.interfaces.IMediaPlayer;
import synergynet3.feedbacksystem.FeedbackSystem;
import synergynet3.feedbacksystem.defaultfeedbacktypes.AudioFeedback;
import synergynet3.feedbacksystem.defaultfeedbacktypes.SimpleTrafficLightFeedback;
import synergynet3.feedbacksystem.defaultfeedbacktypes.SmilieFeedback;
import synergynet3.feedbacksystem.defaultfeedbacktypes.YesOrNoFeedback;
import synergynet3.projector.network.ProjectorTransferUtilities;

import multiplicity3.appsystem.MultiplicityClient;
import multiplicity3.csys.factory.ContentTypeNotBoundException;
import multiplicity3.csys.items.item.IItem;

public class TestVideoApp extends SynergyNetApp {	
	
	private static ArrayList<String> mediaSources = new ArrayList<String>();
	
	@Override
	protected void loadDefaultContent() throws IOException, ContentTypeNotBoundException {	            
		
		ProjectorTransferUtilities.get().setDecelerationOnArrival(-1);		
		feedbackTypes.add(SimpleTrafficLightFeedback.class);
		feedbackTypes.add(AudioFeedback.class);
		feedbackTypes.add(SmilieFeedback.class);
		feedbackTypes.add(YesOrNoFeedback.class);		
		
		this.enableNetworkFlick();
		
		IItem[] items = new IItem[mediaSources.size()];
		
		for (int i = 0; i < mediaSources.size(); i++){			
			IMediaPlayer mediaPlayer = stage.getContentFactory().create(IMediaPlayer.class, "vid", UUID.randomUUID());
			mediaPlayer.setDeceleration(deceleration);
			mediaPlayer.setRemoteResource(mediaSources.get(i), false, true, stage);
			mediaPlayer.setSize(640, 385);			
			stage.addItem(mediaPlayer);
			items[i] = mediaPlayer;
			FeedbackSystem.registerAsFeedbackEligible(mediaPlayer, 640, 385, stage);	
			mediaPlayer.setVisible(false);
			mediaPlayer.setVisible(true);
		}		

		AdditionalSynergyNetUtilities.pile(items, 0, 0, 20, 0);	
		
	}		
	
	public static void main(String[] args) {
		for (String arg: args){
			mediaSources.add(arg);
		}
		
		MultiplicityClient client = MultiplicityClient.get();
		client.start();
		TestVideoApp app = new TestVideoApp();
		client.setCurrentApp(app);
	}
	

}