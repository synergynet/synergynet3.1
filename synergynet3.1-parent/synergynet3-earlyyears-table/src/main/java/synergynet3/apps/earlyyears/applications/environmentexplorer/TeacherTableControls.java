package synergynet3.apps.earlyyears.applications.environmentexplorer;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import multiplicity3.csys.factory.ContentTypeNotBoundException;
import multiplicity3.csys.stage.IStage;
import multiplicity3.input.MultiTouchEventAdapter;
import multiplicity3.input.events.MultiTouchCursorEvent;
import synergynet3.additionalUtils.AdditionalSynergyNetUtilities;
import synergynet3.additionalitems.interfaces.ICachableImage;
import synergynet3.additionalitems.interfaces.IScrollContainer;
import synergynet3.feedbacksystem.FeedbackSystem;

import com.jme3.math.FastMath;

/**
 * The Class TeacherTableControls.
 */
public class TeacherTableControls
{

	/** The Constant RESOURCE_DIR. */
	private static final String RESOURCE_DIR = "synergynet3/earlyyears/table/applications/environmentexplorer/";

	/** The Constant SCROLL_ARROW_LOC. */
	private static final String SCROLL_ARROW_LOC = "synergynet3/additionalitems/scrollButton.png";

	/** The application. */
	private EnvironmentExplorerApp application;

	/** The control box. */
	private IScrollContainer controlBox;

	/** The leave marker button. */
	private ICachableImage leaveMarkerButton;

	/** The set as map button. */
	private ICachableImage setAsMapButton;

	/** The target line. */
	private TargetLine targetLine;

	/** The toggle down button. */
	private ICachableImage toggleDownButton;

	/** The toggle up button. */
	private ICachableImage toggleUpButton;

	/**
	 * Instantiates a new teacher table controls.
	 *
	 * @param stage
	 *            the stage
	 * @param app
	 *            the app
	 */
	public TeacherTableControls(IStage stage, EnvironmentExplorerApp app)
	{

		application = app;

		try
		{

			Logger log = Logger.getLogger(TeacherTableControls.class.getName());

			controlBox = stage.getContentFactory().create(IScrollContainer.class, "menu", UUID.randomUUID());
			controlBox.setDimensions(stage, log, 151, 50);
			stage.addItem(controlBox);

			targetLine = new TargetLine(stage, log, controlBox, FeedbackSystem.getFedbackEligbleItems());

			int optionsHeight = (controlBox.getHeight() / 2) - 25;
			int optionsWidth = (-controlBox.getWidth() / 2) + 25;

			toggleUpButton = stage.getContentFactory().create(ICachableImage.class, "userIcon", UUID.randomUUID());
			toggleUpButton.setImage(SCROLL_ARROW_LOC);
			toggleUpButton.setSize(40, 40);

			toggleUpButton.getMultiTouchDispatcher().addListener(new MultiTouchEventAdapter()
			{
				@Override
				public void cursorClicked(MultiTouchCursorEvent event)
				{
					targetLine.getPreviousTarget();
					if (targetLine.getCurrentTarget() != null)
					{
						setAsMapButton.setImage(RESOURCE_DIR + "addToGallery.png");
					}
					else
					{
						setAsMapButton.setImage(RESOURCE_DIR + "addToGalleryFaded.png");
					}
				}
			});
			controlBox.addToAllFrames(toggleUpButton, optionsWidth, optionsHeight);

			setAsMapButton = stage.getContentFactory().create(ICachableImage.class, "userIcon", UUID.randomUUID());
			setAsMapButton.setImage(RESOURCE_DIR + "addToGalleryFaded.png");
			setAsMapButton.setSize(40, 40);
			setAsMapButton.getMultiTouchDispatcher().addListener(new MultiTouchEventAdapter()
			{
				@Override
				public void cursorClicked(MultiTouchCursorEvent event)
				{
					if (targetLine.getCurrentTarget() != null)
					{
						if (targetLine.getCurrentTarget() != null)
						{
							targetLine.hideLine();
							application.setMap(targetLine.getCurrentTarget());
						}
					}
				}
			});
			controlBox.addToAllFrames(setAsMapButton, optionsWidth + 50, optionsHeight);

			toggleDownButton = stage.getContentFactory().create(ICachableImage.class, "userIcon", UUID.randomUUID());
			toggleDownButton.setImage(SCROLL_ARROW_LOC);
			toggleDownButton.setSize(40, 40);
			toggleDownButton.setRelativeRotation(FastMath.DEG_TO_RAD * -180);
			toggleDownButton.getMultiTouchDispatcher().addListener(new MultiTouchEventAdapter()
			{
				@Override
				public void cursorClicked(MultiTouchCursorEvent event)
				{
					targetLine.getNextTarget();
					if (targetLine.getCurrentTarget() != null)
					{
						setAsMapButton.setImage(RESOURCE_DIR + "addToGallery.png");
					}
					else
					{
						setAsMapButton.setImage(RESOURCE_DIR + "addToGalleryFaded.png");
					}
				}
			});
			controlBox.addToAllFrames(toggleDownButton, optionsWidth + 100, optionsHeight);

			leaveMarkerButton = stage.getContentFactory().create(ICachableImage.class, "userIcon", UUID.randomUUID());
			leaveMarkerButton.setImage(RESOURCE_DIR + "removeFromGallery.png");
			leaveMarkerButton.setSize(40, 40);
			leaveMarkerButton.getMultiTouchDispatcher().addListener(new MultiTouchEventAdapter()
			{
				@Override
				public void cursorClicked(MultiTouchCursorEvent event)
				{
					application.createMarker(leaveMarkerButton.getWorldLocation(), controlBox.getRelativeRotation());
				}
			});
			controlBox.addToAllFrames(leaveMarkerButton, optionsWidth + 175, optionsHeight);

		}
		catch (ContentTypeNotBoundException e)
		{
			AdditionalSynergyNetUtilities.log(Level.SEVERE, "ContentTypeNotBoundException: ", e);
		}

	}

	/**
	 * Sets the visibility.
	 *
	 * @param visibility
	 *            the visibility
	 * @param stage
	 *            the stage
	 */
	public void setVisibility(boolean visibility, IStage stage)
	{
		if (!visibility)
		{
			setAsMapButton.setImage(RESOURCE_DIR + "addToGalleryFaded.png");
		}
		else
		{
			stage.getZOrderManager().bringToTop(controlBox);
		}
		controlBox.setVisibility(visibility);
		targetLine.hideLine();
	}

}
