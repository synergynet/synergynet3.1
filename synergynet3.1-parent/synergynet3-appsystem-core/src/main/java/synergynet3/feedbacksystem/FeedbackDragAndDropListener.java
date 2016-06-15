package synergynet3.feedbacksystem;

import java.util.logging.Logger;

import multiplicity3.csys.draganddrop.DragAndDropListener;
import multiplicity3.csys.items.item.IItem;
import multiplicity3.csys.stage.IStage;

/**
 * The listener interface for receiving feedbackDragAndDrop events. The class
 * that is interested in processing a feedbackDragAndDrop event implements this
 * interface, and the object created with that class is registered with a
 * component using the component's
 * <code>addFeedbackDragAndDropListener<code> method. When
 * the feedbackDragAndDrop event occurs, that object's appropriate
 * method is invoked.
 *
 * @see FeedbackDragAndDropEvent
 */
public class FeedbackDragAndDropListener implements DragAndDropListener
{

	/** The feedback item. */
	private FeedbackItem feedbackItem;

	/** The log. */
	private Logger log;

	/** The stage. */
	private IStage stage;

	/**
	 * Instantiates a new feedback drag and drop listener.
	 *
	 * @param feedbackItem
	 *            the feedback item
	 * @param stage
	 *            the stage
	 * @param log
	 *            the log
	 */
	public FeedbackDragAndDropListener(FeedbackItem feedbackItem, IStage stage, Logger log)
	{
		this.feedbackItem = feedbackItem;
		this.log = log;
		this.stage = stage;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.csys.draganddrop.DragAndDropListener#itemDraggedAndDropped
	 * (multiplicity3.csys.items.item.IItem,
	 * multiplicity3.csys.items.item.IItem, int)
	 */
	@Override
	public void itemDraggedAndDropped(IItem itemDropped, IItem onto, int indexOfDrop)
	{

		if (itemDropped == null)
		{
			log.warning("No actual dropping occurred!");
			return;
		}

		if (!itemDropped.isVisible())
		{
			log.fine("Item is not visible, no further action.");
			return;
		}

		attemptToAddFeedbackTo(itemDropped, stage);
	}

	/**
	 * Attempt to add feedback to.
	 *
	 * @param item
	 *            the item
	 * @param stage
	 *            the stage
	 */
	private void attemptToAddFeedbackTo(IItem item, IStage stage)
	{
		if (!item.equals(stage))
		{
			if (!feedbackItem.addFeedbackItem(item))
			{
				attemptToAddFeedbackTo(item.getParentItem(), stage);
			}
		}
	}

}
