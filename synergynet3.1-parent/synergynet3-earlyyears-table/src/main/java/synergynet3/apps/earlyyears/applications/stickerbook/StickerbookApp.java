package synergynet3.apps.earlyyears.applications.stickerbook;

import java.io.IOException;
import java.net.SocketException;
import java.util.HashMap;

import synergynet3.additionalUtils.AdditionalSynergyNetUtilities;
import synergynet3.apps.earlyyears.applications.EarlyYearsApp;
import synergynet3.behaviours.networkflick.NetworkFlickBehaviour;
import synergynet3.feedbacksystem.defaultfeedbacktypes.AudioFeedback;
import synergynet3.feedbacksystem.defaultfeedbacktypes.SimpleTrafficLightFeedback;
import synergynet3.feedbacksystem.defaultfeedbacktypes.SmilieFeedback;
import synergynet3.web.earlyyears.shared.EarlyYearsActivity;

import multiplicity3.appsystem.MultiplicityClient;
import multiplicity3.config.identity.IdentityConfigPrefsItem;
import multiplicity3.csys.behaviours.IBehaviour;
import multiplicity3.csys.factory.ContentTypeNotBoundException;
import multiplicity3.csys.items.item.IItem;
import multiplicity3.input.MultiTouchEventAdapter;
import multiplicity3.input.events.MultiTouchCursorEvent;


public class StickerbookApp extends EarlyYearsApp {
	
	private HashMap<IItem, Boolean> touchEnabled = new HashMap<IItem, Boolean>();
	
	public static void main(String[] args) throws SocketException {
		if(args.length > 0) {
			IdentityConfigPrefsItem idprefs = new IdentityConfigPrefsItem();
			idprefs.setID(args[0]);
		}
		
		MultiplicityClient client = MultiplicityClient.get();
		client.start();
		StickerbookApp app = new StickerbookApp();
		client.setCurrentApp(app);
		
	}
	
	@Override
	protected void loadDefaultContent() throws IOException, ContentTypeNotBoundException {
		
		feedbackTypes.add(SimpleTrafficLightFeedback.class);
		feedbackTypes.add(AudioFeedback.class);
		feedbackTypes.add(SmilieFeedback.class);
		
		syncName = EarlyYearsActivity.STICKER_BOOK;

	}

	@Override
	protected String getSpecificFriendlyAppName(){
		return "Stickerbook";
	}
	
	@Override
	protected void onAddingAdditionalMaterials(IItem[] items){
		for (final IItem item: items){
			touchEnabled.put(item, true);
			item.getMultiTouchDispatcher().addListener(new MultiTouchEventAdapter() {
				@Override
				public void cursorClicked(MultiTouchCursorEvent event) {	
					if (touchEnabled.get(item)){
						stopFlick(item);		
						for (IBehaviour behaviour: item.getBehaviours()){
							behaviour.setActive(false);
						}
						stage.getZOrderManager().sendToBottom(item);
						stage.getZOrderManager().unregisterForZOrdering(item);
						touchEnabled.put(item, false);
					}else{
						for (IBehaviour behaviour: item.getBehaviours()){
							behaviour.setActive(true);
						}
						stage.getZOrderManager().registerForZOrdering(item);
						touchEnabled.put(item, true);
					}
				}
			});
		}
		AdditionalSynergyNetUtilities.pile(items, 0, 0, 20, 0);	
	}
	
	private void stopFlick(IItem item){
			for(NetworkFlickBehaviour behaviour : item.getBehaviours(NetworkFlickBehaviour.class)){
				behaviour.reset();
			}
	}

}
