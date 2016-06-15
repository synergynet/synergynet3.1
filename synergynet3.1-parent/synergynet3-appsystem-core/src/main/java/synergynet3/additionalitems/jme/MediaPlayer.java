package synergynet3.additionalitems.jme;

import java.io.File;
import java.util.UUID;

import multiplicity3.csys.annotations.ImplementsContentItem;
import multiplicity3.csys.behaviours.RotateTranslateScaleBehaviour;
import multiplicity3.csys.factory.ContentTypeNotBoundException;
import multiplicity3.csys.items.border.IRoundedBorder;
import multiplicity3.csys.items.image.IImage;
import multiplicity3.csys.items.shapes.IColourRectangle;
import multiplicity3.csys.stage.IStage;
import multiplicity3.input.MultiTouchEventAdapter;
import multiplicity3.input.events.MultiTouchCursorEvent;
import multiplicity3.jme3csys.items.IInitable;
import multiplicity3.jme3csys.items.container.JMEContainer;
import synergynet3.SynergyNetApp;
import synergynet3.additionalitems.interfaces.IActionOnVideoEndListener;
import synergynet3.additionalitems.interfaces.ICachableImage;
import synergynet3.additionalitems.interfaces.IMediaPlayer;
import synergynet3.additionalitems.interfaces.ISimpleMediaPlayer;
import synergynet3.behaviours.networkflick.NetworkFlickBehaviour;
import synergynet3.cachecontrol.IItemCachable;
import synergynet3.cachecontrol.ItemCaching;
import synergynet3.config.web.CacheOrganisation;
import synergynet3.databasemanagement.GalleryItemDatabaseFormat;
import synergynet3.mediadetection.mediasearchtypes.AudioSearchType;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;

/**
 * The Class MediaPlayer.
 */
@ImplementsContentItem(target = IMediaPlayer.class)
public class MediaPlayer extends JMEContainer implements IMediaPlayer, IInitable, IItemCachable, IActionOnVideoEndListener
{

	/**
	 * The Enum LOCATION.
	 */
	private enum LOCATION
	{
		/** The local. */
		LOCAL,
		/** The remote. */
		REMOTE
	}

	/** The Constant CACHABLE_TYPE. */
	public static final String CACHABLE_TYPE = "CACHABLE_MEDIAPLAYER";

	/** The Constant AUDIO_ICON_SIZE. */
	private static final float AUDIO_ICON_SIZE = 100;

	/** The currently playing. */
	private static MediaPlayer currentlyPlaying = null;

	/** The Constant PADDING. */
	private static final float PADDING = 5;

	/** The Constant PP_ICON_SIZE. */
	private static final float PP_ICON_SIZE = 40;

	/** The Constant RESOURCES_DIR. */
	private static final String RESOURCES_DIR = "synergynet3/additionalitems/mediaplayer/";

	/** The audio icon. */
	private ICachableImage audioIcon;

	/** The background. */
	private IColourRectangle background;

	/** The background colour. */
	private ColorRGBA backgroundColour = ColorRGBA.Black;

	/** The border colour. */
	private ColorRGBA borderColour = new ColorRGBA(1, 1, 1, 1);

	/** The cached. */
	private String cached = "";

	/** The deceleration. */
	private float deceleration = 100f;

	/** The filename. */
	private String filename = null;

	/** The height. */
	private float height = 385;

	/** The instance. */
	private MediaPlayer instance;

	/** The is local playing. */
	private boolean isLocalPlaying = false;

	/** The listener. */
	private IImage listener;

	/** The max scale. */
	private float maxScale = -1f;

	/** The media border. */
	private IRoundedBorder mediaBorder;

	/** The min scale. */
	private float minScale = -1f;

	/** The pause icon. */
	private ICachableImage pauseIcon;

	/** The play icon. */
	private ICachableImage playIcon;

	/** The repeat. */
	private boolean repeat = false;

	/** The resource location. */
	private File resourceLocation = null;

	/** The simple media player. */
	private ISimpleMediaPlayer simpleMediaPlayer;

	/** The start position. */
	private float startPosition = 0;

	/** The video loc. */
	private LOCATION videoLoc = null;

	/** The video url. */
	private String videoURL = null;

	/** The width. */
	private float width = 640;

	/**
	 * Instantiates a new media player.
	 *
	 * @param name
	 *            the name
	 * @param uuid
	 *            the uuid
	 */
	public MediaPlayer(String name, UUID uuid)
	{
		super(name, uuid);
	}

	/**
	 * Reconstruct.
	 *
	 * @param galleryItem
	 *            the gallery item
	 * @param stage
	 *            the stage
	 * @param loc
	 *            the loc
	 * @return the media player
	 */
	public static MediaPlayer reconstruct(GalleryItemDatabaseFormat galleryItem, IStage stage, String loc)
	{
		try
		{
			MediaPlayer video = stage.getContentFactory().create(IMediaPlayer.class, (String) galleryItem.getValues().get(0), UUID.randomUUID());
			video.setPosition((Float) galleryItem.getValues().get(2));
			boolean isPlaying = (Boolean) galleryItem.getValues().get(3);
			boolean isRepeated = (Boolean) galleryItem.getValues().get(4);
			if (((String) galleryItem.getValues().get(0)).equals(LOCATION.LOCAL.toString()))
			{
				String mediaLocation = CacheOrganisation.getSpecificDir(loc) + File.separator + (String) galleryItem.getValues().get(1);
				video.setLocalResource(mediaLocation, isPlaying, isRepeated, stage);
			}
			else
			{
				video.setRemoteResource((String) galleryItem.getValues().get(1), isPlaying, isRepeated, stage);
			}
			video.setSize(galleryItem.getWidth(), galleryItem.getHeight());
			video.setCached(loc);

			return video;
		}
		catch (ContentTypeNotBoundException e)
		{
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.cachecontrol.IItemCachable#deconstruct(java.lang.String)
	 */
	@Override
	public GalleryItemDatabaseFormat deconstruct(String loc)
	{
		GalleryItemDatabaseFormat galleryItem = new GalleryItemDatabaseFormat();
		galleryItem.setType(CACHABLE_TYPE);
		galleryItem.setHeight(width);
		galleryItem.setWidth(height);
		if (videoLoc == LOCATION.LOCAL)
		{
			galleryItem.addValue(videoLoc.toString());
			if (!cached.equalsIgnoreCase(loc))
			{
				if (resourceLocation != null)
				{
					ItemCaching.cacheFile(resourceLocation, loc);
				}
			}
			galleryItem.addValue(filename);
		}
		else if (videoLoc == LOCATION.REMOTE)
		{
			galleryItem.addValue(videoLoc.toString());
			galleryItem.addValue(videoURL);
		}
		galleryItem.addValue(simpleMediaPlayer.getPosition());
		galleryItem.addValue(simpleMediaPlayer.isPlaying());
		galleryItem.addValue(repeat);
		return galleryItem;
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.additionalitems.interfaces.IMediaPlayer#destroy()
	 */
	@Override
	public void destroy()
	{
		if (simpleMediaPlayer != null)
		{
			simpleMediaPlayer.destroy();
		}
	}

	/**
	 * Checks if is cached.
	 *
	 * @return the string
	 */
	public String isCached()
	{
		return cached;
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.additionalitems.interfaces.IMediaPlayer#isRepeated()
	 */
	@Override
	public boolean isRepeated()
	{
		return repeat;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynet3.additionalitems.interfaces.IActionOnVideoEndListener#onVideoEnd
	 * ()
	 */
	@Override
	public void onVideoEnd()
	{
		if (simpleMediaPlayer != null)
		{
			if (!simpleMediaPlayer.getRepeat())
			{
				iconsToPauseStatus();
				if (isCurrentPlaying())
				{
					currentlyPlaying = null;
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.additionalitems.interfaces.IMediaPlayer#pause()
	 */
	@Override
	public void pause()
	{
		isLocalPlaying = false;
		if (isCurrentPlaying())
		{
			currentlyPlaying = null;
		}
		simpleMediaPlayer.pause();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynet3.additionalitems.interfaces.IMediaPlayer#setBackgroundColour
	 * (com.jme3.math.ColorRGBA)
	 */
	@Override
	public void setBackgroundColour(ColorRGBA backgroundColour)
	{
		this.backgroundColour = backgroundColour;
		if (background != null)
		{
			background.setSolidBackgroundColour(backgroundColour);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynet3.additionalitems.interfaces.IMediaPlayer#setBorderColour(com
	 * .jme3.math.ColorRGBA)
	 */
	@Override
	public void setBorderColour(ColorRGBA borderColour)
	{
		this.borderColour = borderColour;
	}

	/**
	 * Sets the cached.
	 *
	 * @param cached
	 *            the new cached
	 */
	public void setCached(String cached)
	{
		this.cached = cached;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynet3.additionalitems.interfaces.IMediaPlayer#setDeceleration(float
	 * )
	 */
	@Override
	public void setDeceleration(float deceleration)
	{
		this.deceleration = deceleration;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynet3.additionalitems.interfaces.IMediaPlayer#setLocalResource(
	 * java.io.File, boolean, boolean, multiplicity3.csys.stage.IStage)
	 */
	@Override
	public void setLocalResource(File file, boolean autostart, boolean repeat, IStage stage)
	{
		initialiseVideo(stage, autostart, repeat);
		videoLoc = LOCATION.LOCAL;
		videoURL = file.toString();
		filename = file.getName();
		resourceLocation = file;
		simpleMediaPlayer.setLocalResource(file, autostart);
		audioIconCheck(stage, file);
	};

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynet3.additionalitems.interfaces.IMediaPlayer#setLocalResource(
	 * java.lang.String, boolean, boolean, multiplicity3.csys.stage.IStage)
	 */
	@Override
	public void setLocalResource(String localPath, boolean autostart, boolean repeat, IStage stage)
	{
		initialiseVideo(stage, autostart, repeat);
		videoLoc = LOCATION.LOCAL;
		videoURL = localPath;
		File file = new File(localPath);
		resourceLocation = file;
		filename = file.getName();
		simpleMediaPlayer.setLocalResource(localPath, autostart);
		audioIconCheck(stage, file);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynet3.additionalitems.interfaces.IMediaPlayer#setPosition(float)
	 */
	@Override
	public void setPosition(float pos)
	{
		if (simpleMediaPlayer != null)
		{
			simpleMediaPlayer.setPosition(pos);
		}
		else
		{
			startPosition = pos;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynet3.additionalitems.interfaces.IMediaPlayer#setRemoteResource
	 * (java.lang.String, boolean, boolean, multiplicity3.csys.stage.IStage)
	 */
	@Override
	public void setRemoteResource(String remotePath, boolean autostart, boolean repeat, IStage stage)
	{
		initialiseVideo(stage, autostart, repeat);
		videoLoc = LOCATION.REMOTE;
		videoURL = remotePath;
		simpleMediaPlayer.setRemoteResource(remotePath, autostart);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynet3.additionalitems.interfaces.IMediaPlayer#setRepeated(boolean)
	 */
	@Override
	public void setRepeated(boolean repeat)
	{
		this.repeat = repeat;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynet3.additionalitems.interfaces.IMediaPlayer#setScaleLimits(float,
	 * float)
	 */
	@Override
	public void setScaleLimits(float minScale, float maxScale)
	{
		this.minScale = minScale;
		this.maxScale = maxScale;
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.additionalitems.interfaces.IMediaPlayer#setSize(float,
	 * float)
	 */
	@Override
	public void setSize(float width, float height)
	{
		this.width = width;
		this.height = height;

		if (simpleMediaPlayer != null)
		{
			simpleMediaPlayer.setSize(width, height);
			background.setSize(width, height);
			mediaBorder.setSize(width, height);
			listener.setSize(width, height);

			float rotation = getRelativeRotation();
			Vector2f position = getRelativeLocation();
			float scale = getRelativeScale();
			setRelativeRotation(0);
			setRelativeLocation(new Vector2f());
			setRelativeScale(1);

			playIcon.setRelativeLocation(new Vector2f((-width / 2) + (PP_ICON_SIZE / 2) + PADDING, (height / 2) - (PP_ICON_SIZE / 2) - PADDING));
			pauseIcon.setRelativeLocation(new Vector2f((-width / 2) + (PP_ICON_SIZE / 2) + PADDING, (height / 2) - (PP_ICON_SIZE / 2) - PADDING));
			if (audioIcon != null)
			{
				audioIcon.centerItem();
			}

			setRelativeRotation(rotation);
			setRelativeLocation(position);
			setRelativeScale(scale);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.jme3csys.items.item.JMEItem#setVisible(boolean)
	 */
	@Override
	public void setVisible(boolean isVisible)
	{
		super.setVisible(isVisible);
		if (isVisible)
		{
			if (isLocalPlaying)
			{
				iconsToPlayStatus();
			}
			else
			{
				iconsToPauseStatus();
			}
		}
	}

	/**
	 * Audio icon check.
	 *
	 * @param stage
	 *            the stage
	 * @param file
	 *            the file
	 */
	private void audioIconCheck(IStage stage, File file)
	{
		if (new AudioSearchType().isFileOfSearchType(file))
		{
			try
			{
				audioIcon = stage.getContentFactory().create(ICachableImage.class, "audioIcon", UUID.randomUUID());
				audioIcon.setImage(RESOURCES_DIR + "audioIcon.png");
				audioIcon.setSize(AUDIO_ICON_SIZE, AUDIO_ICON_SIZE);

				this.addItem(audioIcon);

				this.zOrderManager.bringToTop(listener);

			}
			catch (ContentTypeNotBoundException e)
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * Can play.
	 *
	 * @return true, if successful
	 */
	private boolean canPlay()
	{
		if (!SynergyNetApp.ONE_MEDIA_AT_A_TIME || (currentlyPlaying == null))
		{
			return true;
		}
		return false;
	}

	/**
	 * Icons to pause status.
	 */
	private void iconsToPauseStatus()
	{
		playIcon.setVisible(false);
		pauseIcon.setVisible(true);
	}

	/**
	 * Icons to play status.
	 */
	private void iconsToPlayStatus()
	{
		pauseIcon.setVisible(false);
		playIcon.setVisible(true);
	}

	/**
	 * Initialise video.
	 *
	 * @param stage
	 *            the stage
	 * @param autostart
	 *            the autostart
	 * @param repeat
	 *            the repeat
	 */
	private void initialiseVideo(IStage stage, boolean autostart, boolean repeat)
	{
		instance = this;
		try
		{

			simpleMediaPlayer = stage.getContentFactory().create(ISimpleMediaPlayer.class, "button", UUID.randomUUID());
			simpleMediaPlayer.setSize(width, height);
			simpleMediaPlayer.setPosition(startPosition);
			simpleMediaPlayer.setActionOnVideoEndListener(this);

			background = stage.getContentFactory().create(IColourRectangle.class, "mediabg", UUID.randomUUID());
			background.setSolidBackgroundColour(backgroundColour);
			background.setSize(width, height);

			mediaBorder = stage.getContentFactory().create(IRoundedBorder.class, "mediaBorder", UUID.randomUUID());
			mediaBorder.setBorderWidth(15f);
			mediaBorder.setSize(width, height);
			mediaBorder.setColor(borderColour);

			playIcon = stage.getContentFactory().create(ICachableImage.class, "playIcon", UUID.randomUUID());
			playIcon.setImage(RESOURCES_DIR + "playIcon.png");
			playIcon.setSize(PP_ICON_SIZE, PP_ICON_SIZE);
			playIcon.setRelativeLocation(new Vector2f((-width / 2) + (PP_ICON_SIZE / 2) + PADDING, (height / 2) - (PP_ICON_SIZE / 2) - PADDING));

			pauseIcon = stage.getContentFactory().create(ICachableImage.class, "pauseIcon", UUID.randomUUID());
			pauseIcon.setImage(RESOURCES_DIR + "pauseIcon.png");
			pauseIcon.setSize(PP_ICON_SIZE, PP_ICON_SIZE);
			pauseIcon.setRelativeLocation(new Vector2f((-width / 2) + (PP_ICON_SIZE / 2) + PADDING, (height / 2) - (PP_ICON_SIZE / 2) - PADDING));

			listener = stage.getContentFactory().create(IImage.class, "listenBlock", UUID.randomUUID());
			listener.setSize(width, height);

			this.zOrderManager.setAutoBringToTop(false);

			this.addItem(background);
			this.addItem(simpleMediaPlayer);
			this.addItem(mediaBorder);
			this.addItem(playIcon);
			this.addItem(pauseIcon);
			this.addItem(listener);

			if (!autostart)
			{
				iconsToPauseStatus();
			}
			else
			{
				iconsToPlayStatus();
			}

			listener.getMultiTouchDispatcher().addListener(new MultiTouchEventAdapter()
			{
				@Override
				public void cursorClicked(MultiTouchCursorEvent event)
				{
					if (simpleMediaPlayer.isPlaying())
					{
						isLocalPlaying = false;
						if (isCurrentPlaying())
						{
							currentlyPlaying = null;
						}
						simpleMediaPlayer.pause();
						iconsToPauseStatus();
					}
					else
					{
						if (canPlay())
						{
							currentlyPlaying = instance;
							simpleMediaPlayer.unpause();
							isLocalPlaying = true;
							iconsToPlayStatus();
						}
					}
				}
			});

			RotateTranslateScaleBehaviour rtsBackground = stage.getBehaviourMaker().addBehaviour(listener, RotateTranslateScaleBehaviour.class);
			rtsBackground.setItemActingOn(this);
			RotateTranslateScaleBehaviour rtsBorder = stage.getBehaviourMaker().addBehaviour(mediaBorder, RotateTranslateScaleBehaviour.class);
			rtsBorder.setItemActingOn(this);

			if ((minScale != -1) && (maxScale != -1))
			{
				rtsBackground.setScaleLimits(minScale, maxScale);
				rtsBorder.setScaleLimits(minScale, maxScale);
			}

			if (deceleration != -1)
			{
				NetworkFlickBehaviour nf = stage.getBehaviourMaker().addBehaviour(listener, NetworkFlickBehaviour.class);
				nf.setMaxDimension(width);
				nf.setItemActingOn(this);
				nf.setDeceleration(deceleration);
			}

			if (canPlay())
			{
				isLocalPlaying = autostart;
				if (autostart)
				{
					currentlyPlaying = instance;
				}
			}
			else
			{
				isLocalPlaying = false;
			}

		}
		catch (ContentTypeNotBoundException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Checks if is current playing.
	 *
	 * @return true, if is current playing
	 */
	private boolean isCurrentPlaying()
	{
		if (!SynergyNetApp.ONE_MEDIA_AT_A_TIME)
		{
			return true;
		}
		if (currentlyPlaying != null)
		{
			if (currentlyPlaying == instance)
			{
				return true;
			}
		}
		return false;
	}

}
