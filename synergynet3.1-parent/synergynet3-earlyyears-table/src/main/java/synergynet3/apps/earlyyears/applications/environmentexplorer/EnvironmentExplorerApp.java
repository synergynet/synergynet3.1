package synergynet3.apps.earlyyears.applications.environmentexplorer;

import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Level;

import multiplicity3.appsystem.MultiplicityClient;
import multiplicity3.config.identity.IdentityConfigPrefsItem;
import multiplicity3.csys.factory.ContentTypeNotBoundException;
import multiplicity3.csys.items.item.IItem;
import synergynet3.additionalUtils.AdditionalSynergyNetUtilities;
import synergynet3.additionalitems.PerformActionOnAllDescendents;
import synergynet3.additionalitems.interfaces.ICachableImage;
import synergynet3.apps.earlyyears.applications.EarlyYearsApp;
import synergynet3.feedbacksystem.defaultfeedbacktypes.AudioFeedback;
import synergynet3.feedbacksystem.defaultfeedbacktypes.SimpleTrafficLightFeedback;
import synergynet3.feedbacksystem.defaultfeedbacktypes.SmilieFeedback;
import synergynet3.feedbacksystem.defaultfeedbacktypes.YesOrNoFeedback;

import com.jme3.math.Vector2f;

/**
 * The Class EnvironmentExplorerApp.
 */
public class EnvironmentExplorerApp extends EarlyYearsApp
{

	/** The Constant MARKER_IMAGE. */
	private static final String MARKER_IMAGE = "synergynet3/earlyyears/table/applications/environmentexplorer/removeFromGallery.png";

	/** The map. */
	private ICachableImage map;

	/** The markers. */
	private ArrayList<ICachableImage> markers = new ArrayList<ICachableImage>();

	/** The teach table controls. */
	private TeacherTableControls teachTableControls;

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 * @throws SocketException
	 *             the socket exception
	 */
	public static void main(String[] args) throws SocketException
	{
		if (args.length > 0)
		{
			IdentityConfigPrefsItem idprefs = new IdentityConfigPrefsItem();
			idprefs.setID(args[0]);
		}

		MultiplicityClient client = MultiplicityClient.get();
		client.start();
		EnvironmentExplorerApp app = new EnvironmentExplorerApp();
		client.setCurrentApp(app);

	}

	/**
	 * Creates the marker.
	 *
	 * @param relativeLocation
	 *            the relative location
	 * @param relativeRotation
	 *            the relative rotation
	 */
	public void createMarker(Vector2f relativeLocation, float relativeRotation)
	{
		try
		{
			ICachableImage marker = stage.getContentFactory().create(ICachableImage.class, "userIcon", UUID.randomUUID());
			marker.setImage(MARKER_IMAGE);
			marker.setSize(40, 40);
			marker.setInteractionEnabled(false);
			marker.setRelativeLocation(new Vector2f(relativeLocation.x - (displayWidth / 2), relativeLocation.y - (displayHeight / 2)));
			marker.setRelativeRotation(relativeRotation);
			stage.addItem(marker);
			marker.getZOrderManager().setBringToTopPropagatesUp(false);
			marker.getZOrderManager().setAutoBringToTop(false);
			markers.add(marker);
		}
		catch (ContentTypeNotBoundException e)
		{
			AdditionalSynergyNetUtilities.log(Level.SEVERE, "Content Type NotBound Exception Exception.", e);
		}
	}

	/**
	 * Sets the map.
	 *
	 * @param item
	 *            the new map
	 */
	public void setMap(IItem item)
	{

		new PerformActionOnAllDescendents(item, false, true)
		{
			@Override
			protected void actionOnDescendent(IItem child)
			{
				try
				{
					ICachableImage image = (ICachableImage) child;
					map.setImage(image.getImage());

					for (ICachableImage marker : markers)
					{
						stage.removeItem(marker);
					}
					markers.clear();

					stop = true;
				}
				catch (ClassCastException e)
				{
				}
			}
		};
	}

	/**
	 * Sets the teacher control visibility.
	 *
	 * @param visible
	 *            the new teacher control visibility
	 */
	public void setTeacherControlVisibility(boolean visible)
	{
		teachTableControls.setVisibility(visible, stage);
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.SynergyNetApp#getSpecificFriendlyAppName()
	 */
	@Override
	protected String getSpecificFriendlyAppName()
	{
		return "EnvironmentExplorer";
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.SynergyNetApp#loadDefaultContent()
	 */
	@Override
	protected void loadDefaultContent() throws IOException, ContentTypeNotBoundException
	{

		feedbackTypes.add(SimpleTrafficLightFeedback.class);
		feedbackTypes.add(AudioFeedback.class);
		feedbackTypes.add(SmilieFeedback.class);
		feedbackTypes.add(YesOrNoFeedback.class);

		teachTableControls = new TeacherTableControls(stage, this);
		teachTableControls.setVisibility(false, stage);

		map = stage.getContentFactory().create(ICachableImage.class, "userIcon", UUID.randomUUID());
		map.setSize(displayWidth, displayHeight);
		map.setRelativeScale(0.8f);
		map.setInteractionEnabled(false);
		stage.addItem(map);
		stage.getZOrderManager().sendToBottom(map);
		map.getZOrderManager().setBringToTopPropagatesUp(false);
		map.getZOrderManager().setAutoBringToTop(false);

	}

}
