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
import synergynet3.additionalitems.PerformActionOnAllDescendents;
import synergynet3.additionalitems.ZManager;
import synergynet3.additionalitems.interfaces.ICachableImage;
import synergynet3.additionalitems.interfaces.IScrollContainer;
import synergynet3.personalcontentcontrol.StudentIconGenerator;
import synergynet3.personalcontentcontrol.StudentRepresentation;
import synergynet3.studentmenucontrol.StudentMenu;

/**
 * The Class FeedbackSelect.
 */
public class FeedbackSelect
{

	/** The feedback icons. */
	private ArrayList<IItem> feedbackIcons = new ArrayList<IItem>();

	/** The line. */
	private ILine line;

	/** The log. */
	private Logger log = Logger.getLogger(FeedbackSelect.class.getName());

	/** The menu. */
	private StudentMenu menu;

	/** The scroll container. */
	private IScrollContainer scrollContainer;

	/** The stage. */
	private IStage stage;

	/** The student. */
	private StudentRepresentation student;

	/**
	 * Instantiates a new feedback select.
	 *
	 * @param stage
	 *            the stage
	 * @param log
	 *            the log
	 * @param student
	 *            the student
	 * @param menuIn
	 *            the menu in
	 * @param feedbackTypes
	 *            the feedback types
	 */
	public FeedbackSelect(IStage stage, Logger log, StudentRepresentation student, StudentMenu menuIn, ArrayList<Class<? extends FeedbackItem>> feedbackTypes)
	{

		menu = menuIn;
		this.student = student;
		this.stage = stage;

		try
		{
			scrollContainer = stage.getContentFactory().create(IScrollContainer.class, "menu", UUID.randomUUID());
			scrollContainer.setDimensions(stage, log, 512, 300);
			stage.addItem(scrollContainer);

			line = stage.getContentFactory().create(ILine.class, "line", UUID.randomUUID());
			line.setLineColour(student.getStudentColour());
			line.setLineWidth(10f);
			stage.addItem(line);
		}
		catch (ContentTypeNotBoundException e)
		{
			log.log(Level.SEVERE, "ContentTypeNotBoundException: ", e);
		}

		scrollContainer.setFrameColour(student.getStudentColour());

		ZManager.manageLineOrderFull(stage, line, menu.getRadialMenu(), scrollContainer);

		IItem userIcon = StudentIconGenerator.generateIcon(stage, 40, 40, 3, false, student.getStudentId());
		scrollContainer.addToAllFrames(userIcon, (scrollContainer.getWidth() / 2) - (40 / 2) - 5, (-scrollContainer.getHeight() / 2) + (40 / 2) + 5);

		new PerformActionOnAllDescendents(userIcon, false, false)
		{
			@Override
			protected void actionOnDescendent(IItem child)
			{
				child.setInteractionEnabled(true);
				child.getMultiTouchDispatcher().addListener(new MultiTouchEventAdapter()
				{
					@Override
					public void cursorClicked(MultiTouchCursorEvent event)
					{
						if (menu != null)
						{
							menu.turnFeedbackModeOff();
						}
						tidyAway();
					}
				});
			}
		};

		scrollContainer.setArrowHeightOverride(100);

		if (feedbackTypes.size() > 0)
		{
			for (Class<? extends FeedbackItem> feedbackItem : feedbackTypes)
			{
				try
				{
					addFeedbackType(feedbackItem.newInstance(), menu);
				}
				catch (InstantiationException e)
				{
					log.log(Level.SEVERE, "InstantiationException: " + e);
				}
				catch (IllegalAccessException e)
				{
					log.log(Level.SEVERE, "IllegalAccessException: " + e);
				}
			}

			initialiseFeedbackIcons();

		}
	}

	/**
	 * Adds the feedback type.
	 *
	 * @param feedbackItem
	 *            the feedback item
	 * @param menu
	 *            the menu
	 */
	public void addFeedbackType(final FeedbackItem feedbackItem, final StudentMenu menu)
	{
		try
		{
			String iconAdd = feedbackItem.getIcon();
			ICachableImage icon = stage.getContentFactory().create(ICachableImage.class, "feedback", UUID.randomUUID());
			icon.setImage(iconAdd);
			icon.getMultiTouchDispatcher().addListener(new MultiTouchEventAdapter()
			{
				@Override
				public void cursorClicked(MultiTouchCursorEvent event)
				{
					tidyAway();
					feedbackItem.createSetter(scrollContainer.getRelativeLocation(), scrollContainer.getRelativeRotation(), student, menu, stage, log);
				}
			});
			feedbackIcons.add(icon);
			icon.setSize(100, 100);

		}
		catch (ContentTypeNotBoundException e)
		{
			log.log(Level.SEVERE, "ContentTypeNotBoundException: " + e);
		}
	}

	/**
	 * Gets the container.
	 *
	 * @return the container
	 */
	public IScrollContainer getContainer()
	{
		return scrollContainer;
	}

	/**
	 * Initialise feedback icons.
	 */
	public void initialiseFeedbackIcons()
	{

		int frame = 0;

		for (int i = 0; i < feedbackIcons.size(); i += 3)
		{

			if ((feedbackIcons.size() - i) == 1)
			{
				scrollContainer.addToFrame(feedbackIcons.get(i), frame, 0, 0);
			}
			else if ((feedbackIcons.size() - i) == 2)
			{
				scrollContainer.addToFrame(feedbackIcons.get(i), frame, -60, 0);
				scrollContainer.addToFrame(feedbackIcons.get(i + 1), frame, 60, 0);
			}
			else if ((feedbackIcons.size() - i) >= 3)
			{
				scrollContainer.addToFrame(feedbackIcons.get(i), frame, -110, 0);
				scrollContainer.addToFrame(feedbackIcons.get(i + 1), frame, 0, 0);
				scrollContainer.addToFrame(feedbackIcons.get(i + 2), frame, 110, 0);
			}
			if ((feedbackIcons.size() - i) > 3)
			{
				frame = scrollContainer.addFrame();
			}
		}
	}

	/**
	 * Tidy away.
	 */
	public void tidyAway()
	{
		if (line != null)
		{
			stage.removeItem(line);
		}
		stage.removeItem(scrollContainer);
	}

}
