package synergynet3.personalcontentcontrol;

import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import multiplicity3.csys.behaviours.BehaviourMaker;
import multiplicity3.csys.behaviours.IBehaviour;
import multiplicity3.csys.factory.ContentTypeNotBoundException;
import multiplicity3.csys.items.item.IItem;
import multiplicity3.csys.items.line.ILine;
import multiplicity3.csys.stage.IStage;
import multiplicity3.input.MultiTouchEventAdapter;
import multiplicity3.input.events.MultiTouchCursorEvent;
import synergynet3.additionalitems.PerformActionOnAllDescendents;
import synergynet3.additionalitems.ZManager;
import synergynet3.additionalitems.interfaces.ICachableImage;
import synergynet3.additionalitems.interfaces.IScrollContainer;
import synergynet3.additionalitems.jme.MediaPlayer;
import synergynet3.behaviours.networkflick.NetworkFlickBehaviour;
import synergynet3.feedbacksystem.FeedbackContainer;
import synergynet3.feedbacksystem.FeedbackSystem;
import synergynet3.studentmenucontrol.StudentMenu;

import com.jme3.math.Vector2f;

/**
 * The Class StudentGallery.
 */
public class StudentGallery {

	/** The Constant BLANK_LOC. */
	private static final String BLANK_LOC = "synergynet3/personalcontentcontrol/blank.png";

	/** The Constant GALLERY_ITEM_DIMENSION_LIMIT. */
	private static final int GALLERY_ITEM_DIMENSION_LIMIT = 250;

	/** The allow gallery add. */
	private boolean allowGalleryAdd = true;

	/** The behaviour maker. */
	private BehaviourMaker behaviourMaker;

	/** The blank. */
	private ICachableImage blank;

	/** The gallery box. */
	private IScrollContainer galleryBox;

	/** The gallery items. */
	private ArrayList<IItem> galleryItems = new ArrayList<IItem>();

	/** The gallery items info. */
	private ArrayList<Object[]> galleryItemsInfo = new ArrayList<Object[]>();

	/** The gallery line. */
	private ILine galleryLine;

	/** The log. */
	private Logger log;

	/** The menu. */
	private StudentMenu menu;

	/** The stage. */
	private IStage stage;

	/** The student. */
	private StudentRepresentation student;

	/**
	 * Instantiates a new student gallery.
	 *
	 * @param student the student
	 */
	public StudentGallery(StudentRepresentation student) {
		Logger log = Logger.getLogger(StudentGallery.class.getName());
		initialise(student, log);
	}

	/**
	 * Instantiates a new student gallery.
	 *
	 * @param student the student
	 * @param log the log
	 */
	public StudentGallery(StudentRepresentation student, Logger log) {
		initialise(student, log);
	}

	/**
	 * Adds the to gallery.
	 *
	 * @param item the item
	 * @param stage the stage
	 * @return true, if successful
	 */
	public boolean addToGallery(IItem item, IStage stage) {
		if (FeedbackSystem.isItemFeedbackEligible(item)) {
			if (galleryBox == null) {
				generateGallery(stage);
				setVisibility(false);
			}
			stage.removeItem(item);

			float width = FeedbackSystem.getFedbackEligbleItemDimensions(item).x;
			float height = FeedbackSystem.getFedbackEligbleItemDimensions(item).y;

			FeedbackContainer feedbackContained = null;

			if (FeedbackSystem.isItemFeedbackContainer(item)) {
				feedbackContained = FeedbackSystem.getFeedbackContainer(item);
			}

			behaviourMaker = stage.getBehaviourMaker();

			new PerformActionOnAllDescendents(item, false, false) {
				@Override
				protected void actionOnDescendent(IItem child) {
					for (IBehaviour nf : behaviourMaker.getBehavior(child,
							NetworkFlickBehaviour.class)) {
						((NetworkFlickBehaviour) nf).reset();
					}
				}
			};

			addItemToGalleryContainer(item, stage, width, height,
					feedbackContained);

			return true;
		} else {
			return false;
		}
	}

	/**
	 * Adds the to stage.
	 *
	 * @param stage the stage
	 */
	public void addToStage(IStage stage) {
		this.stage = stage;
		if (galleryBox.getParentItem() == null) {
			stage.addItem(galleryBox);
		}
		if (menu != null) {
			ZManager.manageLineOrderFull(stage, galleryLine,
					menu.getRadialMenu(), galleryBox);
		}
	}

	/**
	 * As item.
	 *
	 * @return the i item
	 */
	public IItem asItem() {
		return galleryBox;
	}

	/**
	 * Generate gallery.
	 *
	 * @param stage the stage
	 * @return the i item
	 */
	public IItem generateGallery(IStage stage) {
		if (galleryBox == null) {

			this.stage = stage;

			try {
				galleryBox = stage.getContentFactory().create(
						IScrollContainer.class, "menu", UUID.randomUUID());
				galleryBox.setDimensions(stage, log, 512, 300);
				galleryBox.setVisibility(false);
				stage.addItem(galleryBox);
				galleryBox.setRelativeLocation(new Vector2f(-stage
						.getDisplayWidth(), 0));
				galleryBox.setFrameColour(student.getStudentColour());
				galleryBox.setArrowHeightOverride(205);
				galleryBox.setArrowYOverride(-8);

				stage.getDragAndDropSystem().registerDragDestinationListener(
						galleryBox.getBackground(),
						new GalleryDragAndDropListener(this, stage, log));

				IItem studentIcon = StudentIconGenerator.generateIcon(stage,
						40, 40, 3, false, student.getStudentId());
				galleryBox.addToAllFrames(studentIcon,
						(galleryBox.getWidth() / 2) - (40 / 2) - 5,
						(-galleryBox.getHeight() / 2) + (40 / 2) + 5);

				new PerformActionOnAllDescendents(studentIcon, false, false) {
					@Override
					protected void actionOnDescendent(IItem child) {
						child.setInteractionEnabled(true);
						child.getMultiTouchDispatcher().addListener(
								new MultiTouchEventAdapter() {
									@Override
									public void cursorClicked(
											MultiTouchCursorEvent event) {
										if (menu != null) {
											menu.onRootItemClickAction();
										}
									}
								});
					}
				};

				blank = stage.getContentFactory().create(ICachableImage.class,
						"blank", UUID.randomUUID());
				blank.setImage(BLANK_LOC);
				blank.setSize(222, 222);
				blank.getMultiTouchDispatcher().addListener(
						new MultiTouchEventAdapter() {
							@Override
							public void cursorClicked(
									MultiTouchCursorEvent event) {
								addItemToScene();
							}
						});
				galleryBox.addToAllFrames(blank, 0, 0);
				bringBlankToTop();

				galleryBox.addListenerToArrows(new MultiTouchEventAdapter() {
					@Override
					public void cursorClicked(MultiTouchCursorEvent event) {
						bringBlankToTop();
					}
				});

			} catch (ContentTypeNotBoundException e) {
				log.log(Level.SEVERE, "ContentTypeNotBoundException: ", e);
			}

			ArrayList<IItem> galleryItemsCopy = new ArrayList<IItem>();
			for (IItem item : galleryItems) {
				galleryItemsCopy.add(item);
			}
			galleryItems.clear();

			ArrayList<Object[]> galleryItemsSizesCopy = new ArrayList<Object[]>();
			for (Object[] info : galleryItemsInfo) {
				galleryItemsSizesCopy.add(info);
			}
			galleryItemsInfo.clear();

			for (int i = 0; i < galleryItemsCopy.size(); i++) {
				addItemToGalleryContainer(galleryItemsCopy.get(i), stage,
						(Float) galleryItemsSizesCopy.get(i)[0],
						(Float) galleryItemsSizesCopy.get(i)[1],
						(FeedbackContainer) galleryItemsSizesCopy.get(i)[2]);
			}

			generateGalleryLine();

			if (galleryItems.size() > 0) {
				galleryBox.showCurrentFrameContents();
			}
		}
		return galleryBox;
	}

	/**
	 * @return the galleryBox
	 */
	public IScrollContainer getGalleryBox() {
		return galleryBox;
	}

	/**
	 * @return the galleryItems
	 */
	public ArrayList<IItem> getGalleryItems() {
		return galleryItems;
	}

	/**
	 * @return the galleryItemsInfo
	 */
	public ArrayList<Object[]> getGalleryItemsInfo() {
		return galleryItemsInfo;
	}

	/**
	 * Gets the menu.
	 *
	 * @return the menu
	 */
	public StudentMenu getMenu() {
		return menu;
	}

	/**
	 * Removes the from.
	 *
	 * @param stage the stage
	 */
	public void removeFrom(IStage stage) {
		if (galleryBox != null) {
			stage.removeItem(galleryBox);
		}
		if (galleryLine != null) {
			stage.removeItem(galleryLine);
		}
	}

	/**
	 * Reset student gallery.
	 */
	public void resetStudentGallery() {
		galleryBox = null;
	}

	/**
	 * Sets the ability to add content from gallery.
	 *
	 * @param allowGalleryAdd the new ability to add content from gallery
	 */
	public void setAbilityToAddContentFromGallery(boolean allowGalleryAdd) {
		this.allowGalleryAdd = allowGalleryAdd;
	}

	/**
	 * Sets the menu.
	 *
	 * @param menu the new menu
	 */
	public void setMenu(StudentMenu menu) {
		this.menu = menu;
	}

	/**
	 * Sets the visibility.
	 *
	 * @param visibility the new visibility
	 */
	public void setVisibility(boolean visibility) {
		galleryBox.setVisibility(visibility);
		galleryLine.setVisible(visibility);
		if (menu != null) {
			ZManager.manageLineOrder(stage, galleryLine, menu.getRadialMenu(),
					galleryBox, true);
		}
		if (visibility) {
			if (galleryItems.size() < 2) {
				galleryBox.hideScrollButtons();
			}
			bringBlankToTop();
		}
	}

	/**
	 * Adds the item to gallery container.
	 *
	 * @param item the item
	 * @param stage the stage
	 * @param width the width
	 * @param height the height
	 * @param feedbackContainer the feedback container
	 */
	private void addItemToGalleryContainer(IItem item, IStage stage,
			float width, float height, FeedbackContainer feedbackContainer) {
		if (galleryBox == null) {
			generateGallery(stage);
			stage.removeItem(galleryBox);
		}

		galleryItems.add(item);
		Object[] info = { width, height, feedbackContainer };
		galleryItemsInfo.add(info);
		int frame = 0;
		if (galleryItems.size() > 1) {
			frame = galleryBox.addFrame();
		}
		item.setRelativeRotation(0f);

		if (((width * item.getRelativeScale()) > GALLERY_ITEM_DIMENSION_LIMIT)
				|| ((height * item.getRelativeScale()) > GALLERY_ITEM_DIMENSION_LIMIT)) {
			float longestDim = width;
			if (width < height) {
				longestDim = height;
			}
			item.setRelativeScale(GALLERY_ITEM_DIMENSION_LIMIT / longestDim);
		}

		galleryBox.addToFrame(item, frame, 0, 0);
		galleryBox.scrollToFrame(galleryItems.size() - 1);

		if (feedbackContainer != null) {
			mediaCheck((feedbackContainer).getContainedItem());
		} else {
			mediaCheck(item);
		}

		FeedbackSystem.removeFeedbackViewerFromCurrentStage(item);
		FeedbackSystem.unregisterAsFeedbackEligible(item, stage);

		bringBlankToTop();

	}

	/**
	 * Adds the item to scene.
	 */
	private void addItemToScene() {
		if ((galleryItems.size() > 0) && allowGalleryAdd) {
			int toAddToScene = galleryBox.getCurrentFrame();
			IItem item = galleryItems.get(toAddToScene);

			float width = (Float) galleryItemsInfo.get(toAddToScene)[0];
			float height = (Float) galleryItemsInfo.get(toAddToScene)[1];

			FeedbackContainer feedbackContainer = (FeedbackContainer) galleryItemsInfo
					.get(toAddToScene)[2];

			removeCurrentItemFromGallery();
			stage.addItem(item);

			item.setRelativeLocation(galleryBox.getRelativeLocation());
			item.setRelativeRotation(galleryBox.getRelativeRotation());

			FeedbackSystem.registerAsFeedbackEligible(item, width, height,
					feedbackContainer, stage);
			FeedbackSystem.attachFeedbackViewerToStage(item, stage);

			if (menu != null) {
				menu.onRootItemClickAction();
			}

			stage.getZOrderManager().bringToTop(item);
		}
	}

	/**
	 * Bring blank to top.
	 */
	private void bringBlankToTop() {
		if (blank != null) {
			galleryBox.getZOrderManager().bringToTop(blank);
		}
	}

	/**
	 * Generate gallery line.
	 */
	private void generateGalleryLine() {
		try {
			galleryLine = stage.getContentFactory().create(ILine.class, "line",
					UUID.randomUUID());
			stage.addItem(galleryLine);
			galleryLine.setLineColour(student.getStudentColour());
			galleryLine.setLineWidth(10f);
		} catch (ContentTypeNotBoundException e) {
			log.log(Level.SEVERE, "ContentTypeNotBoundException: ", e);
		}
	}

	/**
	 * Initialise.
	 *
	 * @param student the student
	 * @param log the log
	 */
	private void initialise(StudentRepresentation student, Logger log) {
		this.student = student;
		this.log = log;
	}

	/**
	 * Media check.
	 *
	 * @param item the item
	 */
	private void mediaCheck(IItem item) {
		if (item instanceof MediaPlayer) {
			((MediaPlayer) item).pause();
		}
	}

	/**
	 * Removes the current item from gallery.
	 */
	private void removeCurrentItemFromGallery() {
		if (galleryItems.size() > 0) {
			int toRemove = galleryBox.getCurrentFrame();
			galleryBox.removeFrame(toRemove);
			galleryItems.remove(toRemove);
			galleryItemsInfo.remove(toRemove);
		}
	}

}
