package synergynet3.additionalitems;

import multiplicity3.csys.items.item.IItem;
import multiplicity3.csys.items.line.ILine;
import multiplicity3.csys.stage.IStage;
import synergynet3.feedbacksystem.FeedbackSystem;

/**
 * The Class ZManager.
 */
public class ZManager
{

	/**
	 * Bring family to front.
	 *
	 * @param stage
	 *            the stage
	 * @param item
	 *            the item
	 */
	public static void bringFamilyToFront(IItem stage, final IItem item)
	{
		int zOrderBefore = item.getZOrder();
		stage.getZOrderManager().bringToTop(item);

		final int offsetSource = item.getZOrder() - zOrderBefore;

		new PerformActionOnAllDescendents(item, false, false)
		{
			@Override
			protected void actionOnDescendent(IItem child)
			{
				if (!child.equals(item))
				{
					child.setZOrder(child.getZOrder() + offsetSource + 1);
				}
			}
		};

		if (FeedbackSystem.isItemFeedbackContainer(item))
		{
			item.getZOrderManager().bringToTop(FeedbackSystem.getFeedbackContainer(item).getIcon());
		}
	}

	/**
	 * Manage line order.
	 *
	 * @param stage
	 *            the stage
	 * @param line
	 *            the line
	 * @param source
	 *            the source
	 * @param target
	 *            the target
	 * @param invert
	 *            the invert
	 */
	public static void manageLineOrder(IStage stage, ILine line, final IItem source, final IItem target, boolean invert)
	{

		line.setSourceItem(source);
		line.setDestinationItem(target);

		line.setInteractionEnabled(false);
		stage.getZOrderManager().bringToTop(line);
		line.getZOrderManager().setAutoBringToTop(false);
		stage.addItem(line);

		if (invert)
		{
			bringFamilyToFront(stage, source);
			stage.getZOrderManager().bringToTop(target);
		}
		else
		{
			stage.getZOrderManager().bringToTop(source);
			bringFamilyToFront(stage, target);
		}

	}

	/**
	 * Manage line order full.
	 *
	 * @param stage
	 *            the stage
	 * @param line
	 *            the line
	 * @param source
	 *            the source
	 * @param target
	 *            the target
	 */
	public static void manageLineOrderFull(IStage stage, ILine line, final IItem source, final IItem target)
	{
		manageLineOrder(stage, line, source, target, false);
	}

}
