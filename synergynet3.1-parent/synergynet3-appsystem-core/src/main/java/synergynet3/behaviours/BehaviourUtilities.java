package synergynet3.behaviours;

import java.io.File;

import multiplicity3.csys.behaviours.BehaviourMaker;
import multiplicity3.csys.behaviours.IBehaviour;
import multiplicity3.csys.items.item.IItem;
import multiplicity3.csys.stage.IStage;
import synergynet3.additionalitems.PerformActionOnAllDescendents;
import synergynet3.behaviours.networkflick.NetworkFlickBehaviour;
import synergynet3.behaviours.networkflick.NetworkFlickLogging;
import synergynet3.behaviours.networkflick.NetworkFlickLogging.FLICKTYPE;
import synergynet3.behaviours.networkflick.messages.FlickMessage;
import synergynet3.cachecontrol.ItemCaching;
import synergynet3.config.web.CacheOrganisation;
import synergynet3.feedbacksystem.FeedbackSystem;
import synergynet3.positioning.SynergyNetPositioning;

import com.jme3.math.Vector2f;

/**
 * The Class BehaviourUtilities.
 */
public class BehaviourUtilities
{

	/**
	 * The Enum RelativePosition.
	 */
	public enum RelativePosition
	{

		/** The bottom. */
		BOTTOM,
		/** The bottomleft. */
		BOTTOMLEFT,
		/** The bottomright. */
		BOTTOMRIGHT,
		/** The left. */
		LEFT,
		/** The right. */
		RIGHT,
		/** The top. */
		TOP,
		/** The topleft. */
		TOPLEFT,
		/** The topright. */
		TOPRIGHT
	}

	/**
	 * Defines behaviour of network flick transfers - by default it is realistic
	 * (i.e. travels on and off the screen edge). Instant mode is an alternative
	 * behaviour where transferred items will instantly be transferred on
	 * bounce.
	 */
	public static FLICKTYPE FLICK_TYPE = FLICKTYPE.NORMAL;

	/**
	 * Retrieves details from the supplied message to recreate the flicked item
	 * and to ensure it appears onscreen in the manner expected, i.e. aligning
	 * with its flick vector from its source device.
	 *
	 * @param message
	 *            Structured message detailing the details of an item's arrival.
	 * @param stage
	 *            The environement's main stage.
	 * @param tableIdentity
	 *            Table's ID on the network cluster.
	 * @param deceleration
	 *            Rate of flick motion reduction.
	 */
	public static IItem onFlickArrival(final FlickMessage message, IStage stage, String tableIdentity, float deceleration)
	{

		IItem item = null;

		if (message.getTargetTableID().equals(tableIdentity))
		{

			item = ItemCaching.reconstructItem(message.getGalleryItem(), stage, CacheOrganisation.TRANSFER_DIR + File.separator + CacheOrganisation.NETWORK_FLICK_DIR, deceleration);

			item.setRelativeLocation(new Vector2f(SynergyNetPositioning.getPixelValue(message.getXinMetres()), SynergyNetPositioning.getPixelValue(message.getYinMetres())));
			item.setRelativeRotation(message.getRotation());
			item.setRelativeScale(message.getScale());

			if (FeedbackSystem.isItemFeedbackContainer(item))
			{
				FeedbackSystem.getFeedbackContainer(item).getContainedItem().setVisible(true);
				FeedbackSystem.getFeedbackContainer(item).getIcon().setVisible(true);
				FeedbackSystem.attachFeedbackViewerToStage(item, stage);
			}
			else
			{
				item.setVisible(true);
			}

			stage.addItem(item);
			Vector2f loc = item.getWorldLocation();
			if (NetworkFlickLogging.LOGGING_ENABLED)
			{
				NetworkFlickLogging.generateFlickArrivalMessage(item.getName(), message.getSourceTableID(), loc);
			}

			final BehaviourMaker behaviourMaker = stage.getBehaviourMaker();

			new PerformActionOnAllDescendents(item, false, false)
			{
				@Override
				protected void actionOnDescendent(IItem child)
				{
					for (IBehaviour nf : behaviourMaker.getBehavior(child, NetworkFlickBehaviour.class))
					{
						((NetworkFlickBehaviour) nf).flick(new Vector2f(message.getXDir(), message.getYDir()));
					}
				}
			};
		}

		return item;
	}

}
