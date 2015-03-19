package synergynet3.feedbacksystem;

import java.util.logging.Logger;

import multiplicity3.csys.draganddrop.DragAndDropListener;
import multiplicity3.csys.items.item.IItem;
import multiplicity3.csys.stage.IStage;

public class FeedbackDragAndDropListener implements DragAndDropListener {
	
	private Logger log;	
	private FeedbackItem feedbackItem;
	private IStage stage;
	
	public FeedbackDragAndDropListener(FeedbackItem feedbackItem, IStage stage, Logger log) {
		this.feedbackItem = feedbackItem;
		this.log = log;
		this.stage = stage;
	}
	
	@Override
	public void itemDraggedAndDropped(IItem itemDropped, IItem onto, int indexOfDrop) {
		
		if(itemDropped == null) {
			log.warning("No actual dropping occurred!");
			return;
		}
		
		if(!itemDropped.isVisible()) {
			log.fine("Item is not visible, no further action.");
			return;
		}
		
		attemptToAddFeedbackTo(itemDropped, stage);
	}	
	
	private void attemptToAddFeedbackTo(IItem item, IStage stage){
		if (!item.equals(stage)){
			if (!feedbackItem.addFeedbackItem(item)){
				attemptToAddFeedbackTo(item.getParentItem(), stage);
			}
		}
	}
	
}
