package synergynet3.projector.network;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;

import synergynet3.additionalitems.PerformActionOnAllDescendents;
import synergynet3.additionalitems.jme.CachableLine;
import synergynet3.behaviours.networkflick.NetworkFlickBehaviour;
import synergynet3.cachecontrol.ItemCaching;
import synergynet3.config.web.CacheOrganisation;
import synergynet3.config.web.WebConfigPrefsItem;
import synergynet3.databasemanagement.GalleryItemDatabaseFormat;
import synergynet3.feedbacksystem.FeedbackContainer;
import synergynet3.feedbacksystem.FeedbackSystem;
import synergynet3.projector.network.messages.ContentTransferedMessage;
import synergynet3.projector.web.ProjectorControlComms;
import synergynet3.web.shared.DevicesSelected;
import synergynet3.web.shared.messages.PerformActionMessage.MESSAGESTATE;

import com.jme3.math.Vector2f;

import multiplicity3.csys.MultiplicityEnvironment;
import multiplicity3.csys.items.events.IItemListener;
import multiplicity3.csys.items.item.IItem;
import multiplicity3.csys.stage.IStage;
import multiplicity3.csys.zorder.ZOrderManager;

public class ProjectorTransferUtilities {	

	private static ProjectorTransferUtilities instance;
	
	private IStage stage;
	private String deviceID;
	
	private float decelerationOnArrival = 100f;
	
	private HashMap<String, IItem> contents = new HashMap<String, IItem>();
	private HashMap<IItem, Vector2f> contentDimensions = new HashMap<IItem, Vector2f>();
	private HashMap<IItem, GalleryItemDatabaseFormat> contentTransferrableTypes = new HashMap<IItem, GalleryItemDatabaseFormat>();
			
	private ProjectorTransferUtilities(IStage stage, String deviceID){
		this.stage = stage;
		this.deviceID = deviceID;
	}
	
	/**
	 * Retrieve, and initialise if needed, a static instance of this class. 
	 * 
	 * @return An instance of this class to be accessed statically. 
	 */
	public static ProjectorTransferUtilities get() {
		synchronized(ProjectorTransferUtilities.class) {
			if(instance == null)instance = new ProjectorTransferUtilities(MultiplicityEnvironment.get().getLocalStages().get(0), new WebConfigPrefsItem().getClusterUserName());		
			return instance;
		}
	}
		
	public void addToTransferableContents(IItem item, float width, float height, String contentID){
		if (!contents.containsKey(contentID)){
			contents.put(contentID, item);
			contentDimensions.put(item, new Vector2f(width, height));
		}
	}
	
	public void removeFromTransferableContents(IItem item){		
		String toRemove = getContentKey(item);

		if (toRemove != null){
			contents.remove(toRemove);
			contentDimensions.remove(item);
			if (contentTransferrableTypes.containsKey(item))contentTransferrableTypes.remove(item);
		}
	}
	
	public String getContentKey(IItem item){
		for (Entry<String, IItem> set: contents.entrySet()){
			if (set.getValue().equals(item)){
				return set.getKey();
			}
		}		
		return null;
	}
	
	public ArrayList<ContentTransferedMessage> prepareToTransferAllContents(String[] devicesToSendTo){
		ArrayList<ContentTransferedMessage> messages = new ArrayList<ContentTransferedMessage>();
		ArrayList<IItem> itemsToSend = new ArrayList<IItem>();
		
		for (String itemID: contents.keySet()){
			
			IItem item = contents.get(itemID);
			
			if (item == null){
				removeFromTransferableContents(item);
			}else if (item.getParentItem() == null){
				removeFromTransferableContents(item);
			}else{
				
				float width = contentDimensions.get(item).x;
				float height = contentDimensions.get(item).y;
				
				GalleryItemDatabaseFormat galleryItem ;
				
				if (!contentTransferrableTypes.containsKey(item)){					
					FeedbackContainer feedbackContained = null;				
					if (FeedbackSystem.isItemFeedbackContainer(item))feedbackContained = FeedbackSystem.getFeedbackContainer(item);
					Object[] info = {width, height, feedbackContained};					
					
					galleryItem = ItemCaching.deconstructItem(item, info, CacheOrganisation.TRANSFER_DIR + File.separator + CacheOrganisation.PROJECTOR_TRANSFER_DIR);
					contentTransferrableTypes.put(item, galleryItem);
				}else{
					galleryItem = contentTransferrableTypes.get(item);
				}
				
				if (messageNotAlreadyPresent(item, itemID, messages, itemsToSend)){				
					messages.add(new ContentTransferedMessage(devicesToSendTo, galleryItem, item.getRelativeLocation().x, item.getRelativeLocation().y, 
							item.getRelativeRotation(), item.getRelativeScale(), item.getZOrder(), item.isVisible(), itemID));
					itemsToSend.add(item);
				}
			}
		}		
		Collections.sort(messages);		
		return messages;
	}	
	
	private boolean messageNotAlreadyPresent(IItem item, String itemID, ArrayList<ContentTransferedMessage> messages, ArrayList<IItem> itemsToSend) {
		if (itemsToSend.contains(item))return false;
		for (ContentTransferedMessage message : messages){
			if (message.getItemName().equals(itemID))return false;
		}		
		return true;
	}

	public void transferIndividualItem(final IItem item, final String[] projectorsToSendTo, final float width, final float height){ 
		Object[] info = {width, height, null};		
		GalleryItemDatabaseFormat galleryItem = ItemCaching.deconstructItem(item, info, CacheOrganisation.TRANSFER_DIR + File.separator + CacheOrganisation.PROJECTOR_TRANSFER_DIR);		
		String itemID =	getContentKey(item);		
		if (itemID != null){			
			ArrayList<ContentTransferedMessage> messages = new ArrayList<ContentTransferedMessage>();
			messages.add(new ContentTransferedMessage(projectorsToSendTo, galleryItem, 0, 0, item.getRelativeRotation(), 1, 0, item.isVisible(), itemID));
			
			for (String projector: projectorsToSendTo){
				if (projector.equals(DevicesSelected.ALL_PROJECTORS_ID)){
					ProjectorControlComms.get().allProjectorsReceiveContent(messages);
				}else{
					ProjectorControlComms.get().specificProjectorsReceiveContent(messages, projector);
				}
			}	
		}
	}
	
	/**
	* Retrieves details from the supplied message to recreate the transfered item
	* 
	* @param message
	*         Structured message detailing the details of an item's arrival.
	* 
	*/
	public void onContentArrival(ArrayList<ContentTransferedMessage> messages){			
		ArrayList<ContentTransferedMessage> moveToBack = new ArrayList<ContentTransferedMessage>();
		for (ContentTransferedMessage message: messages){
			if (message.getGalleryItem().getType().equalsIgnoreCase(CachableLine.CACHABLE_TYPE)){
				moveToBack.add(message);
			}
		}
		for (ContentTransferedMessage message: moveToBack){
			messages.remove(message);
			messages.add(message);
		}
		
		for (ContentTransferedMessage message: messages){
			
			if (!message.messageAlreadyReceived() && message.getMessageState() == MESSAGESTATE.ACTIVATE){
				
				boolean isTargetDevice = false;
				
				for (String ID: message.getTargetDeviceIDs()){
					if (ID.equals(deviceID) || ID.equals(DevicesSelected.ALL_PROJECTORS_ID) || ID.equals(DevicesSelected.ALL_TABLES_ID)){
						isTargetDevice = true;
						break;
					}
				}
				if (isTargetDevice){
					if (contents.containsKey(message.getItemName())){
						updateContentLocation(contents.get(message.getItemName()), message.getX(), message.getY(), message.getRotation(), message.getScale(), message.isVisible());
					}else{		
						
						IItem item = ItemCaching.reconstructItem(message.getGalleryItem(), stage, 
								CacheOrganisation.TRANSFER_DIR + File.separator + CacheOrganisation.PROJECTOR_TRANSFER_DIR, decelerationOnArrival);
						
						addToTransferableContents(item, message.getGalleryItem().getWidth(), message.getGalleryItem().getHeight(), message.getItemName());	

						updateContentLocation(item, message.getX(), message.getY(), message.getRotation(), message.getScale(), message.isVisible());						
						
						if (FeedbackSystem.isItemFeedbackContainer(item)){
							FeedbackSystem.getFeedbackContainer(item).getContainedItem().setVisible(message.isVisible());
							FeedbackSystem.getFeedbackContainer(item).getIcon().setVisible(message.isVisible());
							FeedbackSystem.attachFeedbackViewerToStage(item, stage);
						}else{
							item.setVisible(message.isVisible());
						}
						stage.addItem(item);	
						
						if (message.getGalleryItem().getType().equalsIgnoreCase(CachableLine.CACHABLE_TYPE)){
							stage.getZOrderManager().sendToBottom(item);
							item.setInteractionEnabled(false);
						}else{
							bringItemToTop(item);
						}
					}						
				}	
			}
		}
	}
	
	private void updateContentLocation(IItem item, float x, float y, float rotation, float scale, boolean isVisible) {
		item.setRelativeLocation(new Vector2f(x, y));
		item.setRelativeRotation(rotation);
		item.setRelativeScale(scale);
		
		new PerformActionOnAllDescendents(item, false, false){
			@Override
			protected void actionOnDescendent(IItem child){	
				for(NetworkFlickBehaviour behaviour : child.getBehaviours(NetworkFlickBehaviour.class)){
					behaviour.reset();
				}
			}
		};
		item.setVisible(isVisible);
		bringItemToTop(item);		
	}
	
	private void bringItemToTop(IItem item){
		for (IItemListener listener : item.getItemListeners()){
			if (listener instanceof ZOrderManager){
				((ZOrderManager)listener).bringToTop(item);
			}
		}
	}
	
	public void clearContents() {
		contents.clear();
		contentDimensions.clear();
		contentTransferrableTypes.clear();		
	}
	
	public HashMap<String, IItem> getContents() {
		return contents;
	}

	public HashMap<IItem, Vector2f> getContentDimensions() {
		return contentDimensions;
	}

	public HashMap<IItem, GalleryItemDatabaseFormat> getContentTransferrableTypes() {
		return contentTransferrableTypes;
	}

	public float getDecelerationOnArrival() {
		return decelerationOnArrival;
	}

	public void setDecelerationOnArrival(float decelerationOnArrival) {
		this.decelerationOnArrival = decelerationOnArrival;
	}


}