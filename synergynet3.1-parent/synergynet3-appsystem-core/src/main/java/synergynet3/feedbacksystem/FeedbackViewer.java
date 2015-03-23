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

/**
 * The Class FeedbackViewer.
 */
public class FeedbackViewer {

	/** The feedback container. */
	private FeedbackContainer feedbackContainer;

	/** The feedback items. */
	private ArrayList<FeedbackItem> feedbackItems = new ArrayList<FeedbackItem>();

	/** The first frame. */
	private boolean firstFrame = true;

	/** The line. */
	private ILine line;

	/** The log. */
	private Logger log = Logger.getLogger(FeedbackViewer.class.getName());

	/** The scroll container. */
	private IScrollContainer scrollContainer;

	/** The stage. */
	private IStage stage;

	/** The visible. */
	private boolean visible = false;

	/**
	 * Instantiates a new feedback viewer.
	 *
	 * @param stage the stage
	 * @param feedbackContainer the feedback container
	 */
	public FeedbackViewer(IStage stage, FeedbackContainer feedbackContainer) {
		this.stage = stage;
		this.feedbackContainer = feedbackContainer;

		try {
			scrollContainer = stage.getContentFactory().create(
					IScrollContainer.class, "menu", UUID.randomUUID());
			scrollContainer.setDimensions(stage, log, 512, 300);
			stage.addItem(scrollContainer);
		} catch (ContentTypeNotBoundException e) {
			log.log(Level.SEVERE, "ContentTypeNotBoundException: " + e);
		}

		attachLine();
	}

	/**
	 * Adds the feedback.
	 *
	 * @param feedback the feedback
	 */
	public void addFeedback(FeedbackItem feedback) {
		feedbackItems.add(feedback);
		if (firstFrame) {
			feedback.addFeedbackViewFrame(this, 0);
			firstFrame = false;
		} else {
			int newFrameNo = scrollContainer.addFrame();
			feedback.addFeedbackViewFrame(this, newFrameNo);
		}
	}

	/**
	 * Adds the to feedback frame.
	 *
	 * @param item the item
	 * @param frameNo the frame no
	 * @param width the width
	 * @param height the height
	 */
	public void addToFeedbackFrame(IItem item, int frameNo, int width,
			int height) {
		keepLineOnBottom(item);
		scrollContainer.addToFrame(item, frameNo, width, height);
	}

	/**
	 * Adds the to stage.
	 *
	 * @param stage the stage
	 */
	public void addToStage(IStage stage) {
		this.stage = stage;
		stage.addItem(scrollContainer);
		stage.addItem(line);
		line.setSourceItem(feedbackContainer.getWrapper());
		line.setDestinationItem(scrollContainer);
	}

	/**
	 * Gets the container.
	 *
	 * @return the container
	 */
	public IScrollContainer getContainer() {
		return scrollContainer;
	}

	/**
	 * @return the feedbackItems
	 */
	public ArrayList<FeedbackItem> getFeedbackItems() {
		return feedbackItems;
	}

	/**
	 * Removes the.
	 */
	public void remove() {
		stage.removeItem(line);
		stage.removeItem(scrollContainer);
	}

	/**
	 * Sets the feedback container visibility.
	 *
	 * @param visibility the new feedback container visibility
	 */
	public void setFeedbackContainerVisibility(boolean visibility) {
		if (visibility) {
			scrollContainer.setRelativeLocation(feedbackContainer.getWrapper()
					.getRelativeLocation());
			scrollContainer.setRelativeRotation(feedbackContainer.getWrapper()
					.getRelativeRotation());
			scrollContainer.setVisibility(true);
			scrollContainer.setInteractionEnabled(true);
			line.setVisible(true);
			ZManager.manageLineOrderFull(stage, line,
					feedbackContainer.getWrapper(), scrollContainer);
		} else {
			line.setVisible(false);
			line.setInteractionEnabled(false);
			scrollContainer.setVisibility(false);
			scrollContainer.setInteractionEnabled(false);
		}
		visible = visibility;
	}

	/**
	 * @param feedbackItems the feedbackItems to set
	 */
	public void setFeedbackItems(ArrayList<FeedbackItem> feedbackItems) {
		this.feedbackItems = feedbackItems;
	}

	/**
	 * Toggle visibility.
	 */
	public void toggleVisibility() {
		setFeedbackContainerVisibility(!visible);
	}

	/**
	 * Attach line.
	 */
	private void attachLine() {
		try {
			line = stage.getContentFactory().create(ILine.class, "line",
					UUID.randomUUID());
			line.setLineWidth(10f);
			stage.addItem(line);
		} catch (ContentTypeNotBoundException e) {
			log.log(Level.SEVERE, "ContentTypeNotBoundException: " + e);
		}
		toggleVisibility();
	}

	/**
	 * Keep line on bottom.
	 *
	 * @param item the item
	 */
	private void keepLineOnBottom(final IItem item) {
		for (IItem child : item.getChildItems()) {
			keepLineOnBottom(child);
		}

		item.getMultiTouchDispatcher().addListener(
				new MultiTouchEventAdapter() {
					@Override
					public void cursorPressed(MultiTouchCursorEvent event) {
						stage.getZOrderManager().sendToBottom(line);
					}
				});
	}

}
