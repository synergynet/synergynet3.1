package synergynet3.feedbacksystem;

import java.util.ArrayList;
import java.util.logging.Logger;

import com.jme3.math.Vector2f;

import multiplicity3.csys.items.item.IItem;
import multiplicity3.csys.stage.IStage;
import synergynet3.additionalitems.PerformActionOnAllDescendents;
import synergynet3.personalcontentcontrol.StudentRepresentation;
import synergynet3.projector.network.ProjectorTransferUtilities;
import synergynet3.studentmenucontrol.StudentMenu;

public abstract class FeedbackSystem {
	
	private static ArrayList<IItem> feedbackEligbleItems = new ArrayList<IItem>();
	private static ArrayList<Vector2f> feedbackEligbleItemsDimensions = new ArrayList<Vector2f>();
	private static ArrayList<FeedbackContainer> feedbackContainers = new ArrayList<FeedbackContainer>();
	
	public static ArrayList<IItem> getFedbackEligbleItems(){
		return feedbackEligbleItems;
	}
	
	public static Vector2f getFedbackEligbleItemDimensions(IItem item){
		if (feedbackEligbleItems.contains(item)){
			return feedbackEligbleItemsDimensions.get(feedbackEligbleItems.indexOf(item));
		}
		return null;
	}	
	
	public static boolean isItemFeedbackContainer(IItem item){
		if (feedbackEligbleItems.contains(item)){
			if (feedbackContainers.get(feedbackEligbleItems.indexOf(item)) != null)return true;
		}
		return false;
	}
	
	public static FeedbackContainer getFeedbackContainer(IItem item){
		if (feedbackEligbleItems.contains(item)){
			if (feedbackContainers.get(feedbackEligbleItems.indexOf(item)) != null)return feedbackContainers.get(feedbackEligbleItems.indexOf(item));
		}
		return null;
	}
	
	public static boolean isItemFeedbackEligible(IItem item){
		if (feedbackEligbleItems.contains(item)){
			return true;
		}
		return false;
	}
	
	public static void registerAsFeedbackEligible(IItem item, float width, float height, IStage stage){
		registerAsFeedbackEligible(item, width, height, null, stage);	
	}
	
	public static void registerAsFeedbackEligible(IItem item, float width, float height, FeedbackContainer container, final IStage stage){
		if (!feedbackEligbleItems.contains(item)){
			feedbackEligbleItems.add(item);
			feedbackEligbleItemsDimensions.add(new Vector2f(width, height));
			feedbackContainers.add(container);
			new PerformActionOnAllDescendents(item, false, false){
				@Override
				protected void actionOnDescendent(IItem child){	
					stage.getDragAndDropSystem().registerDragSource(child);
				}
			};		
			ProjectorTransferUtilities.get().addToTransferableContents(item, 
					FeedbackSystem.getFedbackEligbleItemDimensions(item).x, FeedbackSystem.getFedbackEligbleItemDimensions(item).y, item.getName());
		}		
	}
	
	public static void unregisterAsFeedbackEligible(IItem item, final IStage stage){
		if (feedbackEligbleItems.contains(item)){
			int toRemove = feedbackEligbleItems.indexOf(item);
			feedbackEligbleItems.remove(toRemove);
			feedbackEligbleItemsDimensions.remove(toRemove);
			feedbackContainers.remove(toRemove);
			new PerformActionOnAllDescendents(item, false, false){
				@Override
				protected void actionOnDescendent(IItem child){	
					stage.getDragAndDropSystem().unregisterDragSource(child);
				}
			};
			ProjectorTransferUtilities.get().removeFromTransferableContents(item);
		}		
	}
	
	public static void clearFeedbackEligibleItems(){
		feedbackEligbleItems.clear();
		feedbackEligbleItemsDimensions.clear();
		feedbackContainers.clear();
	}
	
	public static FeedbackSelect createSetter(IStage stage, Logger log, ArrayList<Class<? extends FeedbackItem>>feedbackTypes, StudentRepresentation student, StudentMenu menu){
		if (log == null){
			log = Logger.getLogger(FeedbackSystem.class.getName());
		}	
		FeedbackSelect feedbackSelector = new FeedbackSelect(stage, log, student, menu, feedbackTypes);
		stage.addItem(feedbackSelector.getContainer());
		if (student.getGallery() != null){
			if (student.getGallery().getMenu() != null){
				feedbackSelector.getContainer().setRelativeLocation(student.getGallery().getMenu().getRadialMenu().getRelativeLocation());
				feedbackSelector.getContainer().setRelativeRotation(student.getGallery().getMenu().getRadialMenu().getRelativeRotation());
			}
		}
		return feedbackSelector;
	}

	public static void removeFeedbackViewerFromCurrentStage(IItem item) {
		FeedbackContainer toReturn = null;
		if (isItemFeedbackContainer(item)){
			toReturn = feedbackContainers.get(feedbackEligbleItems.indexOf(item));
			IItem parent = toReturn.getFeedbackViewer().getContainer().getParentItem();
			parent.removeItem(toReturn.getFeedbackViewer().getContainer());
		}
	}
	
	public static void attachFeedbackViewerToStage(IItem item, IStage stage) {
		if (isItemFeedbackContainer(item)){
			stage.addItem(feedbackContainers.get(feedbackEligbleItems.indexOf(item)).getFeedbackViewer().getContainer());
		}
	}
	
	public static void removeAdditionalMedia(IStage stage){
		for (IItem item: feedbackEligbleItems){
			ProjectorTransferUtilities.get().removeFromTransferableContents(item);
			stage.removeItem(item);
		}
		feedbackEligbleItems.clear();
		feedbackEligbleItemsDimensions.clear();
		feedbackContainers.clear();
	}
	
}
