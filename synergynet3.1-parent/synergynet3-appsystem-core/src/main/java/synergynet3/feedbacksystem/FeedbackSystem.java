package synergynet3.feedbacksystem;

import java.util.ArrayList;
import java.util.logging.Logger;

import multiplicity3.csys.items.item.IItem;
import multiplicity3.csys.stage.IStage;
import synergynet3.additionalitems.PerformActionOnAllDescendents;
import synergynet3.personalcontentcontrol.StudentRepresentation;
import synergynet3.projector.network.ProjectorTransferUtilities;
import synergynet3.studentmenucontrol.StudentMenu;

import com.jme3.math.Vector2f;

/**
 * The Class FeedbackSystem.
 */
public abstract class FeedbackSystem {

	/** The feedback containers. */
	private static ArrayList<FeedbackContainer> feedbackContainers = new ArrayList<FeedbackContainer>();

	/** The feedback eligble items. */
	private static ArrayList<IItem> feedbackEligbleItems = new ArrayList<IItem>();

	/** The feedback eligble items dimensions. */
	private static ArrayList<Vector2f> feedbackEligbleItemsDimensions = new ArrayList<Vector2f>();

	/**
	 * Attach feedback viewer to stage.
	 *
	 * @param item the item
	 * @param stage the stage
	 */
	public static void attachFeedbackViewerToStage(IItem item, IStage stage) {
		if (isItemFeedbackContainer(item)) {
			stage.addItem(feedbackContainers
					.get(feedbackEligbleItems.indexOf(item))
					.getFeedbackViewer().getContainer());
		}
	}

	/**
	 * Clear feedback eligible items.
	 */
	public static void clearFeedbackEligibleItems() {
		feedbackEligbleItems.clear();
		feedbackEligbleItemsDimensions.clear();
		feedbackContainers.clear();
	}

	/**
	 * Creates the setter.
	 *
	 * @param stage the stage
	 * @param log the log
	 * @param feedbackTypes the feedback types
	 * @param student the student
	 * @param menu the menu
	 * @return the feedback select
	 */
	public static FeedbackSelect createSetter(IStage stage, Logger log,
			ArrayList<Class<? extends FeedbackItem>> feedbackTypes,
			StudentRepresentation student, StudentMenu menu) {
		if (log == null) {
			log = Logger.getLogger(FeedbackSystem.class.getName());
		}
		FeedbackSelect feedbackSelector = new FeedbackSelect(stage, log,
				student, menu, feedbackTypes);
		stage.addItem(feedbackSelector.getContainer());
		if (student.getGallery() != null) {
			if (student.getGallery().getMenu() != null) {
				feedbackSelector.getContainer().setRelativeLocation(
						student.getGallery().getMenu().getRadialMenu()
								.getRelativeLocation());
				feedbackSelector.getContainer().setRelativeRotation(
						student.getGallery().getMenu().getRadialMenu()
								.getRelativeRotation());
			}
		}
		return feedbackSelector;
	}

	/**
	 * Gets the fedback eligble item dimensions.
	 *
	 * @param item the item
	 * @return the fedback eligble item dimensions
	 */
	public static Vector2f getFedbackEligbleItemDimensions(IItem item) {
		if (feedbackEligbleItems.contains(item)) {
			return feedbackEligbleItemsDimensions.get(feedbackEligbleItems
					.indexOf(item));
		}
		return null;
	}

	/**
	 * Gets the fedback eligble items.
	 *
	 * @return the fedback eligble items
	 */
	public static ArrayList<IItem> getFedbackEligbleItems() {
		return feedbackEligbleItems;
	}

	/**
	 * Gets the feedback container.
	 *
	 * @param item the item
	 * @return the feedback container
	 */
	public static FeedbackContainer getFeedbackContainer(IItem item) {
		if (feedbackEligbleItems.contains(item)) {
			if (feedbackContainers.get(feedbackEligbleItems.indexOf(item)) != null) {
				return feedbackContainers.get(feedbackEligbleItems
						.indexOf(item));
			}
		}
		return null;
	}

	/**
	 * Checks if is item feedback container.
	 *
	 * @param item the item
	 * @return true, if is item feedback container
	 */
	public static boolean isItemFeedbackContainer(IItem item) {
		if (feedbackEligbleItems.contains(item)) {
			if (feedbackContainers.get(feedbackEligbleItems.indexOf(item)) != null) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks if is item feedback eligible.
	 *
	 * @param item the item
	 * @return true, if is item feedback eligible
	 */
	public static boolean isItemFeedbackEligible(IItem item) {
		if (feedbackEligbleItems.contains(item)) {
			return true;
		}
		return false;
	}

	/**
	 * Register as feedback eligible.
	 *
	 * @param item the item
	 * @param width the width
	 * @param height the height
	 * @param container the container
	 * @param stage the stage
	 */
	public static void registerAsFeedbackEligible(IItem item, float width,
			float height, FeedbackContainer container, final IStage stage) {
		if (!feedbackEligbleItems.contains(item)) {
			feedbackEligbleItems.add(item);
			feedbackEligbleItemsDimensions.add(new Vector2f(width, height));
			feedbackContainers.add(container);
			new PerformActionOnAllDescendents(item, false, false) {
				@Override
				protected void actionOnDescendent(IItem child) {
					stage.getDragAndDropSystem().registerDragSource(child);
				}
			};
			ProjectorTransferUtilities.get().addToTransferableContents(item,
					FeedbackSystem.getFedbackEligbleItemDimensions(item).x,
					FeedbackSystem.getFedbackEligbleItemDimensions(item).y,
					item.getName());
		}
	}

	/**
	 * Register as feedback eligible.
	 *
	 * @param item the item
	 * @param width the width
	 * @param height the height
	 * @param stage the stage
	 */
	public static void registerAsFeedbackEligible(IItem item, float width,
			float height, IStage stage) {
		registerAsFeedbackEligible(item, width, height, null, stage);
	}

	/**
	 * Removes the additional media.
	 *
	 * @param stage the stage
	 */
	public static void removeAdditionalMedia(IStage stage) {
		for (IItem item : feedbackEligbleItems) {
			ProjectorTransferUtilities.get().removeFromTransferableContents(
					item);
			stage.removeItem(item);
		}
		feedbackEligbleItems.clear();
		feedbackEligbleItemsDimensions.clear();
		feedbackContainers.clear();
	}

	/**
	 * Removes the feedback viewer from current stage.
	 *
	 * @param item the item
	 */
	public static void removeFeedbackViewerFromCurrentStage(IItem item) {
		FeedbackContainer toReturn = null;
		if (isItemFeedbackContainer(item)) {
			toReturn = feedbackContainers.get(feedbackEligbleItems
					.indexOf(item));
			IItem parent = toReturn.getFeedbackViewer().getContainer()
					.getParentItem();
			parent.removeItem(toReturn.getFeedbackViewer().getContainer());
		}
	}

	/**
	 * Unregister as feedback eligible.
	 *
	 * @param item the item
	 * @param stage the stage
	 */
	public static void unregisterAsFeedbackEligible(IItem item,
			final IStage stage) {
		if (feedbackEligbleItems.contains(item)) {
			int toRemove = feedbackEligbleItems.indexOf(item);
			feedbackEligbleItems.remove(toRemove);
			feedbackEligbleItemsDimensions.remove(toRemove);
			feedbackContainers.remove(toRemove);
			new PerformActionOnAllDescendents(item, false, false) {
				@Override
				protected void actionOnDescendent(IItem child) {
					stage.getDragAndDropSystem().unregisterDragSource(child);
				}
			};
			ProjectorTransferUtilities.get().removeFromTransferableContents(
					item);
		}
	}

}
