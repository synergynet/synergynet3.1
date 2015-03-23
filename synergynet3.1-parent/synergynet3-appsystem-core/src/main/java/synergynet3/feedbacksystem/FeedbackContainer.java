package synergynet3.feedbacksystem;

import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import multiplicity3.csys.MultiplicityEnvironment;
import multiplicity3.csys.behaviours.IBehaviour;
import multiplicity3.csys.factory.ContentTypeNotBoundException;
import multiplicity3.csys.items.container.IContainer;
import multiplicity3.csys.items.item.IItem;
import multiplicity3.csys.stage.IStage;
import multiplicity3.input.MultiTouchEventAdapter;
import multiplicity3.input.events.MultiTouchCursorEvent;
import synergynet3.additionalitems.PerformActionOnAllDescendents;
import synergynet3.additionalitems.interfaces.ICachableImage;
import synergynet3.additionalitems.jme.AudioRecorder;
import synergynet3.behaviours.networkflick.NetworkFlickBehaviour;

import com.jme3.math.Vector2f;

/**
 * The Class FeedbackContainer.
 */
public class FeedbackContainer {

	/** The feedback containers. */
	public static HashMap<IContainer, FeedbackContainer> feedbackContainers = new HashMap<IContainer, FeedbackContainer>();

	/** The Constant FEEDBACK_ICON_IMAGE. */
	private static final String FEEDBACK_ICON_IMAGE = "synergynet3/feedbacksystem/feedbackIcon.png";

	/** The contained item. */
	private IItem containedItem;

	/** The feedback icon. */
	private ICachableImage feedbackIcon;

	/** The feedback viewer. */
	private FeedbackViewer feedbackViewer;

	/** The log. */
	private Logger log;

	/** The stage. */
	private IStage stage;

	/** The wrapper frame. */
	private IContainer wrapperFrame;

	/**
	 * Instantiates a new feedback container.
	 *
	 * @param stage the stage
	 */
	public FeedbackContainer(IStage stage) {
		this.stage = stage;
		this.log = Logger.getLogger(AudioRecorder.class.getName());
	}

	/**
	 * Instantiates a new feedback container.
	 *
	 * @param stage the stage
	 * @param log the log
	 */
	public FeedbackContainer(IStage stage, Logger log) {
		this.stage = stage;
		this.log = log;
	}

	/**
	 * Destroy.
	 */
	public void Destroy() {
		feedbackContainers.remove(wrapperFrame);
		wrapperFrame.removeItem(containedItem);
		float rotation = wrapperFrame.getRelativeRotation();
		Vector2f position = wrapperFrame.getRelativeLocation();
		stage.removeItem(wrapperFrame);
		stage.addItem(containedItem);
		containedItem.setRelativeRotation(rotation);
		containedItem.setRelativeLocation(position);
	}

	/**
	 * @return the containedItem
	 */
	public IItem getContainedItem() {
		return containedItem;
	}

	/**
	 * Gets the feedback viewer.
	 *
	 * @return the feedback viewer
	 */
	public FeedbackViewer getFeedbackViewer() {
		return feedbackViewer;
	}

	/**
	 * Gets the icon.
	 *
	 * @return the icon
	 */
	public IItem getIcon() {
		return feedbackIcon;
	}

	/**
	 * Gets the wrapper.
	 *
	 * @return the wrapper
	 */
	public IItem getWrapper() {
		return wrapperFrame;
	}

	/**
	 * @param containedItem the containedItem to set
	 */
	public void setContainedItem(IItem containedItem) {
		this.containedItem = containedItem;
	}

	/**
	 * Sets the item.
	 *
	 * @param item the new item
	 */
	public void setItem(final IItem item) {
		containedItem = item;
		try {

			new PerformActionOnAllDescendents(item, false, false) {
				@Override
				protected void actionOnDescendent(IItem child) {
					for (IBehaviour nf : stage.getBehaviourMaker().getBehavior(
							child, NetworkFlickBehaviour.class)) {
						((NetworkFlickBehaviour) nf).reset();
					}
				}
			};

			wrapperFrame = stage.getContentFactory().create(IContainer.class,
					"feedBackContainer", UUID.randomUUID());

			float rotation = item.getRelativeRotation();
			Vector2f position = item.getRelativeLocation();
			float scale = item.getRelativeScale();
			containedItem.setRelativeRotation(0);
			containedItem.setRelativeLocation(new Vector2f(0, 0));
			containedItem.setRelativeScale(1);

			Vector2f itemDimensions = FeedbackSystem
					.getFedbackEligbleItemDimensions(item);
			FeedbackSystem.unregisterAsFeedbackEligible(item, stage);

			IItem parent = item.getParentItem();
			if (parent != null) {
				parent.removeItem(item);
			}

			wrapperFrame.addItem(item);

			switchBehaviour(item);

			feedbackIcon = stage.getContentFactory().create(
					ICachableImage.class, "feedbackIcon", UUID.randomUUID());
			feedbackIcon.setImage(FEEDBACK_ICON_IMAGE);
			feedbackIcon.setSize(50, 50);
			feedbackIcon.setRelativeLocation(new Vector2f(itemDimensions.x / 2,
					-itemDimensions.y / 2));
			feedbackIcon.getMultiTouchDispatcher().addListener(
					new MultiTouchEventAdapter() {
						@Override
						public void cursorClicked(MultiTouchCursorEvent event) {
							feedbackViewer.toggleVisibility();
						}
					});
			feedbackIcon.setVisible(false);

			wrapperFrame.addItem(feedbackIcon);
			wrapperFrame.setRelativeRotation(rotation);
			wrapperFrame.setRelativeLocation(position);
			wrapperFrame.setRelativeScale(scale);

			FeedbackSystem.registerAsFeedbackEligible(wrapperFrame,
					itemDimensions.x, itemDimensions.y, this, stage);

			if (parent != null) {
				parent.addItem(wrapperFrame);
				parent.getZOrderManager().bringToTop(wrapperFrame);
			} else {
				MultiplicityEnvironment.get().getLocalStages().get(0)
						.addItem(wrapperFrame);
				MultiplicityEnvironment.get().getLocalStages().get(0)
						.getZOrderManager().bringToTop(wrapperFrame);
			}

			feedbackContainers.put(wrapperFrame, this);

			feedbackViewer = new FeedbackViewer(stage, this);
			feedbackViewer.setFeedbackContainerVisibility(false);

		} catch (ContentTypeNotBoundException e) {
			log.log(Level.SEVERE, "ContentTypeNotBoundException: " + e);
		}

	}

	/**
	 * Switch behaviour.
	 *
	 * @param item the item
	 */
	private void switchBehaviour(final IItem item) {
		for (IBehaviour behaviour : item.getBehaviours()) {
			behaviour.setItemActingOn(wrapperFrame);
		}
		for (IItem child : item.getChildItems()) {
			switchBehaviour(child);
		}

		item.getMultiTouchDispatcher().addListener(
				new MultiTouchEventAdapter() {
					@Override
					public void cursorPressed(MultiTouchCursorEvent event) {
						wrapperFrame.getZOrderManager()
								.bringToTop(feedbackIcon);
						stage.getZOrderManager().bringToTop(wrapperFrame);
					}
				});
	}

}
