package synergynet3.feedbacksystem;

import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import multiplicity3.csys.factory.ContentTypeNotBoundException;
import multiplicity3.csys.items.item.IItem;
import multiplicity3.csys.items.line.ILine;
import multiplicity3.csys.stage.IStage;
import multiplicity3.input.MultiTouchEventAdapter;
import multiplicity3.input.events.MultiTouchCursorEvent;
import synergynet3.additionalitems.ZManager;
import synergynet3.additionalitems.interfaces.IScrollContainer;

public class FeedbackViewer{
	
	private boolean firstFrame = true;
	private ILine line;
	private FeedbackContainer feedbackContainer;
	private boolean visible = false;
	private ArrayList<FeedbackItem> feedbackItems = new ArrayList<FeedbackItem>();
	private IStage stage;
	private Logger log = Logger.getLogger(FeedbackViewer.class.getName());
	private IScrollContainer scrollContainer;
	
	public FeedbackViewer(IStage stage, FeedbackContainer feedbackContainer) {
		this.stage = stage;
		this.feedbackContainer = feedbackContainer;
		
		try {
			scrollContainer = stage.getContentFactory().create(IScrollContainer.class, "menu", UUID.randomUUID());
			scrollContainer.setDimensions(stage, log, 512, 300);	
			stage.addItem(scrollContainer);
		} catch (ContentTypeNotBoundException e) {
			log.log(Level.SEVERE, "ContentTypeNotBoundException: " + e );
		}			
	
		attachLine();
	}
	
	private void attachLine(){
		try {
			line = stage.getContentFactory().create(ILine.class, "line", UUID.randomUUID());
			line.setLineWidth(10f);
			stage.addItem(line);
		} catch (ContentTypeNotBoundException e) {
			log.log(Level.SEVERE, "ContentTypeNotBoundException: " + e );
		}
		toggleVisibility();
	}
	
	public void toggleVisibility(){
		setFeedbackContainerVisibility(!visible);
	}

	public void setFeedbackContainerVisibility(boolean visibility) {
		if (visibility){
			scrollContainer.setRelativeLocation(feedbackContainer.getWrapper().getRelativeLocation());
			scrollContainer.setRelativeRotation(feedbackContainer.getWrapper().getRelativeRotation());		
			scrollContainer.setVisibility(true);
			scrollContainer.setInteractionEnabled(true);
			line.setVisible(true);
			ZManager.manageLineOrderFull(stage, line, feedbackContainer.getWrapper(), scrollContainer);
		}else{
			line.setVisible(false);
			line.setInteractionEnabled(false);
			scrollContainer.setVisibility(false);
			scrollContainer.setInteractionEnabled(false);
		}
		visible = visibility;
	}

	public void remove(){
		stage.removeItem(line);
		stage.removeItem(scrollContainer);
	}
	
	public void addFeedback(FeedbackItem feedback){	
		feedbackItems.add(feedback);
		if (firstFrame){
			feedback.addFeedbackViewFrame(this, 0);
			firstFrame = false;			
		}else{
			int newFrameNo = scrollContainer.addFrame();
			feedback.addFeedbackViewFrame(this, newFrameNo);
		}		
	}

	
	private void keepLineOnBottom(final IItem item){
		for (IItem child: item.getChildItems()){
			keepLineOnBottom(child);
		}
		
		item.getMultiTouchDispatcher().addListener(new MultiTouchEventAdapter() {
			@Override
			public void cursorPressed(MultiTouchCursorEvent event) {
				stage.getZOrderManager().sendToBottom(line);
			}
		});		
	}
	
	public void addToFeedbackFrame(IItem item, int frameNo, int width, int height) {
		keepLineOnBottom(item);
		scrollContainer.addToFrame(item, frameNo, width, height);		
	}
	
	public void addToStage(IStage stage){		
		this.stage = stage;
		stage.addItem(scrollContainer);
		stage.addItem(line);
		line.setSourceItem(feedbackContainer.getWrapper());
		line.setDestinationItem(scrollContainer);
	}

	/**
	 * @param feedbackItems the feedbackItems to set
	 */
	public void setFeedbackItems(ArrayList<FeedbackItem> feedbackItems) {
		this.feedbackItems = feedbackItems;
	}

	/**
	 * @return the feedbackItems
	 */
	public ArrayList<FeedbackItem> getFeedbackItems() {
		return feedbackItems;
	}

	public IScrollContainer getContainer(){
		return scrollContainer;
	}
	
}
