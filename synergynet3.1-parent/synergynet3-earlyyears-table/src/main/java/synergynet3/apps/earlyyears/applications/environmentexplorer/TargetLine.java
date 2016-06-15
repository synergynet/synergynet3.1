package synergynet3.apps.earlyyears.applications.environmentexplorer;

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
import synergynet3.additionalitems.jme.AudioRecorder;

import com.jme3.math.ColorRGBA;

/**
 * The Class TargetLine.
 */
public class TargetLine
{

	/** The line. */
	private ILine line;

	/** The log. */
	private Logger log;

	/** The source. */
	private IItem source;

	/** The stage. */
	private IStage stage;

	/** The target. */
	private IItem target;

	/** The target index. */
	private int targetIndex = 0;

	/** The targets. */
	private ArrayList<IItem> targets = new ArrayList<IItem>();

	/**
	 * Instantiates a new target line.
	 *
	 * @param stage
	 *            the stage
	 * @param log
	 *            the log
	 * @param source
	 *            the source
	 * @param targets
	 *            the targets
	 */
	public TargetLine(IStage stage, Logger log, IItem source, ArrayList<IItem> targets)
	{
		this.stage = stage;
		if (log == null)
		{
			this.log = Logger.getLogger(AudioRecorder.class.getName());
		}
		else
		{
			this.log = log;
		}
		this.source = source;
		createTargetLine(targets);
	}

	/**
	 * Gets the current target.
	 *
	 * @return the current target
	 */
	public IItem getCurrentTarget()
	{
		if (target.equals(source))
		{
			return null;
		}
		return target;
	}

	/**
	 * Gets the next target.
	 *
	 * @return the next target
	 */
	public boolean getNextTarget()
	{
		if (targets.size() > 1)
		{
			targetIndex++;
			if (targetIndex >= targets.size())
			{
				targetIndex = 0;
			}
			if (targets.get(targetIndex).equals(line) || targets.get(targetIndex).equals(source))
			{
				getNextTarget();
			}
			else
			{
				setTarget(targets.get(targetIndex));
				return true;
			}
		}
		else if (targets.size() == 1)
		{
			setTarget(targets.get(0));
			return true;
		}

		return false;
	}

	/**
	 * Gets the previous target.
	 *
	 * @return the previous target
	 */
	public boolean getPreviousTarget()
	{
		if (targets.size() > 1)
		{
			targetIndex--;
			if (targetIndex < 0)
			{
				targetIndex = targets.size() - 1;
			}
			if (targets.get(targetIndex).equals(line) || targets.get(targetIndex).equals(source))
			{
				getPreviousTarget();
			}
			else
			{
				setTarget(targets.get(targetIndex));
				return true;
			}
		}
		else if (targets.size() == 1)
		{
			setTarget(targets.get(0));
			return true;
		}

		return false;
	}

	/**
	 * Hide line.
	 */
	public void hideLine()
	{
		if (line.isVisible())
		{
			line.setVisible(false);
		}
	}

	/**
	 * Removes the.
	 */
	public void remove()
	{
		stage.removeItem(line);
	}

	/**
	 * Sets the colour.
	 *
	 * @param colour
	 *            the new colour
	 */
	public void setColour(ColorRGBA colour)
	{
		line.setLineColour(colour);
	}

	/**
	 * Show line.
	 */
	public void showLine()
	{
		if (!line.isVisible())
		{
			line.setVisible(true);
		}
	}

	/**
	 * Creates the target line.
	 *
	 * @param targets
	 *            the targets
	 */
	private void createTargetLine(ArrayList<IItem> targets)
	{
		try
		{
			this.targets = targets;
			line = this.stage.getContentFactory().create(ILine.class, "line", UUID.randomUUID());
			line.setSourceItem(source);
			line.setLineWidth(10f);
			line.getMultiTouchDispatcher().addListener(new MultiTouchEventAdapter()
			{
				@Override
				public void cursorPressed(MultiTouchCursorEvent event)
				{
					source.setZOrder(line.getZOrder() + 1);
					target.setZOrder(line.getZOrder() + 1);
				}
			});

			line.setInteractionEnabled(false);

			setTarget(source);
			stage.addItem(line);

			hideLine();

		}
		catch (ContentTypeNotBoundException e)
		{
			this.log.log(Level.SEVERE, "Content not Bound.", e);
		}
	}

	/**
	 * Sets the target.
	 *
	 * @param target
	 *            the new target
	 */
	private void setTarget(IItem target)
	{
		showLine();
		this.target = target;
		ZManager.manageLineOrderFull(stage, line, line.getSourceItem(), target);
	}

}
