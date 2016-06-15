package synergynet3.additionalitems.jme;

import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import multiplicity3.csys.annotations.ImplementsContentItem;
import multiplicity3.csys.behaviours.RotateTranslateScaleBehaviour;
import multiplicity3.csys.factory.ContentTypeNotBoundException;
import multiplicity3.csys.factory.IContentFactory;
import multiplicity3.csys.items.border.IRoundedBorder;
import multiplicity3.csys.items.item.IItem;
import multiplicity3.csys.items.shapes.IColourRectangle;
import multiplicity3.csys.stage.IStage;
import multiplicity3.input.MultiTouchEventAdapter;
import multiplicity3.input.events.MultiTouchCursorEvent;
import multiplicity3.jme3csys.items.IInitable;
import multiplicity3.jme3csys.items.container.JMEContainer;
import synergynet3.additionalitems.PerformActionOnAllDescendents;
import synergynet3.additionalitems.interfaces.ICachableImage;
import synergynet3.additionalitems.interfaces.IScrollContainer;

import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;

/**
 * The Class ScrollContainer.
 */
@ImplementsContentItem(target = IScrollContainer.class)
public class ScrollContainer extends JMEContainer implements IScrollContainer, IInitable
{

	/** The Constant SCROLL_BUTTON_IMAGE. */
	private static final String SCROLL_BUTTON_IMAGE = "synergynet3/additionalitems/scrollButton.png";

	/** The arrow height. */
	private float arrowHeight = 0;

	/** The arrow width. */
	private float arrowWidth = 60;

	/** The arrow y. */
	private float arrowY = 0;

	/** The background. */
	private IColourRectangle background;

	/** The border width. */
	private float borderWidth = 15f;

	/** The current frame. */
	private int currentFrame = 0;

	/** The frame border. */
	private IRoundedBorder frameBorder;

	/** The frames. */
	private ArrayList<ArrayList<IItem>> frames = new ArrayList<ArrayList<IItem>>();

	/** The owner. */
	private String owner = "";

	/** The rts. */
	private RotateTranslateScaleBehaviour rts;

	/** The rtsbg. */
	private RotateTranslateScaleBehaviour rtsbg;

	/** The scalable. */
	private boolean scalable = false;

	/** The arrows present. */
	protected boolean arrowsPresent = false;

	/** The content factory. */
	protected IContentFactory contentFactory;

	/** The height. */
	protected int height = 300;

	/** The log. */
	protected Logger log;

	/** The scroll down. */
	protected ICachableImage scrollDown;

	/** The scroll up. */
	protected ICachableImage scrollUp;

	/** The stage. */
	protected IStage stage;

	/** The width. */
	protected int width = 512;

	/**
	 * Instantiates a new scroll container.
	 *
	 * @param name
	 *            the name
	 * @param uuid
	 *            the uuid
	 */
	public ScrollContainer(String name, UUID uuid)
	{
		super(name, uuid);
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.additionalitems.interfaces.IScrollContainer#addFrame()
	 */
	@Override
	public int addFrame()
	{
		if (frames.size() == 1)
		{
			showScrollButtons();
		}
		frames.add(new ArrayList<IItem>());
		return frames.size() - 1;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynet3.additionalitems.interfaces.IScrollContainer#addListenerToArrows
	 * (multiplicity3.input.MultiTouchEventAdapter)
	 */
	@Override
	public void addListenerToArrows(MultiTouchEventAdapter mTEA)
	{
		if (scrollUp != null)
		{
			scrollUp.getMultiTouchDispatcher().addListener(mTEA);
		}
		if (scrollDown != null)
		{
			scrollDown.getMultiTouchDispatcher().addListener(mTEA);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynet3.additionalitems.interfaces.IScrollContainer#addToAllFrames
	 * (multiplicity3.csys.items.item.IItem, int, int)
	 */
	@Override
	public void addToAllFrames(IItem item, int x, int y)
	{
		addItem(item);
		positionCorrectlyOnFrame(item, x, y);
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.additionalitems.interfaces.IScrollContainer#addToFrame(
	 * multiplicity3.csys.items.item.IItem, int, int, int)
	 */
	@Override
	public void addToFrame(IItem item, int frame, int x, int y)
	{
		if ((frame >= 0) && (frame < frames.size()))
		{
			frames.get(frame).add(item);
			positionCorrectlyOnFrame(item, x, y);
			if (frame != currentFrame)
			{
				hideItem(item);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynet3.additionalitems.interfaces.IScrollContainer#getBackground()
	 */
	@Override
	public IColourRectangle getBackground()
	{
		return background;
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.additionalitems.interfaces.IScrollContainer#getBorder()
	 */
	@Override
	public IRoundedBorder getBorder()
	{
		return frameBorder;
	}

	/**
	 * Gets the border width.
	 *
	 * @return the border width
	 */
	public float getBorderWidth()
	{
		return borderWidth;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynet3.additionalitems.interfaces.IScrollContainer#getCurrentFrame()
	 */
	@Override
	public int getCurrentFrame()
	{
		return currentFrame;
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.additionalitems.interfaces.IScrollContainer#getHeight()
	 */
	@Override
	public int getHeight()
	{
		return height;
	}

	/**
	 * Gets the number of frames.
	 *
	 * @return the number of frames
	 */
	public int getNumberOfFrames()
	{
		return frames.size();
	}

	/**
	 * Gets the owner.
	 *
	 * @return the owner
	 */
	public String getOwner()
	{
		return this.owner;
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.additionalitems.interfaces.IScrollContainer#getWidth()
	 */
	@Override
	public int getWidth()
	{
		return width;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynet3.additionalitems.interfaces.IScrollContainer#hideScrollButtons
	 * ()
	 */
	@Override
	public void hideScrollButtons()
	{
		hideItem(scrollUp);
		hideItem(scrollDown);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynet3.additionalitems.interfaces.IScrollContainer#removeFrame(int)
	 */
	@Override
	public void removeFrame(int toRemove)
	{
		if (toRemove >= 0)
		{
			tidyAwayFrameContents(toRemove);
			if (frames.size() == 1)
			{
				frames.get(0).clear();
			}
			else if (frames.size() > 1)
			{
				frames.remove(toRemove);
				if (frames.size() == 1)
				{
					hideScrollButtons();
				}
				if (currentFrame == toRemove)
				{
					scrollBack();
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.jme3csys.items.item.JMEItem#removeItem(multiplicity3.csys
	 * .items.item.IItem)
	 */
	@Override
	public void removeItem(IItem item)
	{
		for (ArrayList<IItem> items : frames)
		{
			for (IItem isItem : items)
			{
				if (isItem.equals(item))
				{
					super.removeItem(item);
				}
				items.remove(isItem);
				break;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynet3.additionalitems.interfaces.IScrollContainer#scrollToFrame
	 * (int)
	 */
	@Override
	public void scrollToFrame(int frame)
	{
		hideCurrentFrameContents();
		currentFrame = frame;
		showCurrentFrameContents();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynet3.additionalitems.interfaces.IScrollContainer#setActive(boolean
	 * )
	 */
	@Override
	public void setActive(boolean active)
	{
		if (rts != null)
		{
			rts.setActive(active);
		}
		if (rtsbg != null)
		{
			rtsbg.setActive(active);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.additionalitems.interfaces.IScrollContainer#
	 * setArrowHeightOverride(float)
	 */
	@Override
	public void setArrowHeightOverride(float newHeight)
	{
		arrowHeight = newHeight;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynet3.additionalitems.interfaces.IScrollContainer#setArrowWidthOverride
	 * (float)
	 */
	@Override
	public void setArrowWidthOverride(float newWidth)
	{
		arrowWidth = newWidth;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynet3.additionalitems.interfaces.IScrollContainer#setArrowYOverride
	 * (float)
	 */
	@Override
	public void setArrowYOverride(float newY)
	{
		arrowY = newY;
	}

	/**
	 * Sets the background colour.
	 *
	 * @param colour
	 *            the new background colour
	 */
	public void setBackgroundColour(ColorRGBA colour)
	{
		background.setSolidBackgroundColour(colour);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynet3.additionalitems.interfaces.IScrollContainer#setDimensions
	 * (multiplicity3.csys.stage.IStage, java.util.logging.Logger, int, int)
	 */
	@Override
	public void setDimensions(IStage stage, Logger log, int width, int height)
	{

		this.width = width;
		this.height = height;

		try
		{
			contentFactory = stage.getContentFactory();

			frames.add(new ArrayList<IItem>());

			background = contentFactory.create(IColourRectangle.class, "bg", UUID.randomUUID());
			background.setSolidBackgroundColour(ColorRGBA.Black);
			background.setSize(width, height);

			frameBorder = contentFactory.create(IRoundedBorder.class, "border", UUID.randomUUID());
			frameBorder.setBorderWidth(borderWidth);
			frameBorder.setSize(width, height);
			frameBorder.setColor(new ColorRGBA(1, 1, 1, 0.75f));

			arrowHeight = height - (frameBorder.getBorderWidth() * 2);
			arrowY = 0;

			addItem(background);
			addItem(frameBorder);

			this.getZOrderManager().setAutoBringToTop(false);

			rts = stage.getBehaviourMaker().addBehaviour(frameBorder, RotateTranslateScaleBehaviour.class);
			rts.setItemActingOn(this);
			rtsbg = stage.getBehaviourMaker().addBehaviour(background, RotateTranslateScaleBehaviour.class);
			rtsbg.setItemActingOn(this);
			applyScaleability();

		}
		catch (ContentTypeNotBoundException e)
		{
			log.log(Level.SEVERE, "ContentTypeNotBoundException: " + e);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynet3.additionalitems.interfaces.IScrollContainer#setFrameColour
	 * (com.jme3.math.ColorRGBA)
	 */
	@Override
	public void setFrameColour(ColorRGBA colour)
	{
		frameBorder.setColor(colour);
	}

	/**
	 * Sets the owner.
	 *
	 * @param owner
	 *            the new owner
	 */
	public void setOwner(String owner)
	{
		this.owner = owner;
	}

	/**
	 * Sets the scalable.
	 *
	 * @param scalable
	 *            the new scalable
	 */
	public void setScalable(boolean scalable)
	{
		this.scalable = scalable;
		applyScaleability();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynet3.additionalitems.interfaces.IScrollContainer#setVisibility
	 * (java.lang.Boolean)
	 */
	@Override
	public void setVisibility(final Boolean isVisible)
	{

		if (!isVisible)
		{
			new PerformActionOnAllDescendents(this, false, false)
			{
				@Override
				protected void actionOnDescendent(IItem child)
				{
					child.setVisible(isVisible);
				}
			};
		}
		else
		{
			setVisible(isVisible);
			for (int i = 0; i < frames.size(); i++)
			{
				scrollForward();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.additionalitems.interfaces.IScrollContainer#
	 * showCurrentFrameContents()
	 */
	@Override
	public void showCurrentFrameContents()
	{
		for (IItem item : frames.get(currentFrame))
		{
			showItem(item);
		}
	}

	/**
	 * Apply scaleability.
	 */
	private void applyScaleability()
	{
		if (rts != null)
		{
			rts.setScaleEnabled(scalable);
		}
		if (rtsbg != null)
		{
			rtsbg.setScaleEnabled(scalable);
		}
	}

	/**
	 * Hide current frame contents.
	 */
	private void hideCurrentFrameContents()
	{
		if (currentFrame < frames.size())
		{
			for (IItem item : frames.get(currentFrame))
			{
				hideItem(item);
			}
		}
	}

	/**
	 * Hide item.
	 *
	 * @param item
	 *            the item
	 */
	private void hideItem(IItem item)
	{
		if (item != null)
		{
			item.setVisible(false);
			item.setInteractionEnabled(false);
		}
	}

	/**
	 * Position correctly on frame.
	 *
	 * @param item
	 *            the item
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 */
	private void positionCorrectlyOnFrame(IItem item, int x, int y)
	{
		float rotation = getRelativeRotation();
		Vector2f position = getRelativeLocation();
		float scale = getRelativeScale();
		setRelativeRotation(0);
		setRelativeLocation(new Vector2f());
		setRelativeScale(1);
		item.setRelativeLocation(new Vector2f(x, y));
		addItem(item);
		setRelativeRotation(rotation);
		setRelativeLocation(position);
		setRelativeScale(scale);
	}

	/**
	 * Scroll back.
	 */
	private void scrollBack()
	{
		int targetFrame = currentFrame - 1;
		if (targetFrame < 0)
		{
			targetFrame = frames.size() - 1;
		}
		scrollToFrame(targetFrame);
	}

	/**
	 * Scroll forward.
	 */
	private void scrollForward()
	{
		int targetFrame = currentFrame + 1;
		if (targetFrame >= frames.size())
		{
			targetFrame = 0;
		}
		scrollToFrame(targetFrame);
	}

	/**
	 * Show item.
	 *
	 * @param item
	 *            the item
	 */
	private void showItem(IItem item)
	{
		if (item != null)
		{
			item.setVisible(true);
			item.setInteractionEnabled(true);
		}
	}

	/**
	 * Show scroll buttons.
	 */
	private void showScrollButtons()
	{
		if (!arrowsPresent)
		{
			createArrows();
		}
		showItem(scrollUp);
		showItem(scrollDown);
	}

	/**
	 * Tidy away frame contents.
	 *
	 * @param toRemove
	 *            the to remove
	 */
	private void tidyAwayFrameContents(int toRemove)
	{
		for (IItem item : frames.get(toRemove))
		{
			super.removeItem(item);
		}
	}

	/**
	 * Creates the arrows.
	 */
	protected void createArrows()
	{
		try
		{
			scrollUp = contentFactory.create(ICachableImage.class, "scrollUp", UUID.randomUUID());
			scrollUp.setImage(SCROLL_BUTTON_IMAGE);
			scrollUp.setSize(arrowWidth, arrowHeight);
			scrollUp.setRelativeRotation(FastMath.DEG_TO_RAD * 180);
			scrollUp.setRelativeLocation(new Vector2f((width / 2) - (((scrollUp.getWidth() / 2) + frameBorder.getBorderWidth())), arrowY));

			scrollUp.getMultiTouchDispatcher().addListener(new MultiTouchEventAdapter()
			{
				@Override
				public void cursorClicked(MultiTouchCursorEvent event)
				{
					scrollForward();
				}
			});

			scrollDown = contentFactory.create(ICachableImage.class, "scrollDown", UUID.randomUUID());
			scrollDown.setImage(SCROLL_BUTTON_IMAGE);
			scrollDown.setSize(arrowWidth, arrowHeight);
			scrollDown.setRelativeLocation(new Vector2f(-((width / 2) - (((scrollDown.getWidth() / 2) + frameBorder.getBorderWidth()))), arrowY));

			scrollDown.getMultiTouchDispatcher().addListener(new MultiTouchEventAdapter()
			{
				@Override
				public void cursorClicked(MultiTouchCursorEvent event)
				{
					scrollBack();
				}
			});

			addItem(scrollUp);
			addItem(scrollDown);

			arrowsPresent = true;

		}
		catch (ContentTypeNotBoundException e)
		{
			log.log(Level.SEVERE, "ContentTypeNotBoundException: " + e);
		}
	}
}
