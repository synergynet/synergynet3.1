package synergynet3.additionalitems.jme;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import multiplicity3.appsystem.MultiplicityClient;
import multiplicity3.csys.annotations.ImplementsContentItem;
import multiplicity3.jme3csys.geometry.CenteredQuad;
import multiplicity3.jme3csys.items.IInitable;
import multiplicity3.jme3csys.items.item.JMEItem;
import multiplicity3.jme3csys.picking.ItemMap;
import synergynet3.additionalitems.interfaces.IActionOnVideoEndListener;
import synergynet3.additionalitems.interfaces.ISimpleMediaPlayer;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.direct.BufferFormat;
import uk.co.caprica.vlcj.player.direct.BufferFormatCallback;
import uk.co.caprica.vlcj.player.direct.DirectMediaPlayer;
import uk.co.caprica.vlcj.player.direct.RenderCallback;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.material.RenderState.FaceCullMode;
import com.jme3.math.FastMath;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.texture.Image;
import com.jme3.texture.Image.Format;
import com.jme3.texture.Texture2D;
import com.sun.jna.Memory;

/**
 * The Class SimpleMediaPlayer.
 */
@ImplementsContentItem(target = ISimpleMediaPlayer.class)
public class SimpleMediaPlayer extends JMEItem implements ISimpleMediaPlayer, IInitable, RenderCallback, BufferFormatCallback
{

	/**
	 * The Class VidThread.
	 */
	class VidThread extends Thread
	{

		/** The event handlers to add. */
		private ArrayList<MediaPlayerEventAdapter> eventHandlersToAdd = new ArrayList<MediaPlayerEventAdapter>();

		/** The initiated. */
		private boolean initiated = false;

		/** The playing. */
		private boolean playing = false;

		/** The start pos. */
		private float startPos = 0;

		/**
		 * Instantiates a new vid thread.
		 */
		public VidThread()
		{
			super();
		}

		/**
		 * Adds the media player event listener.
		 *
		 * @param mediaPlayerEventAdapter
		 *            the media player event adapter
		 */
		public void addMediaPlayerEventListener(MediaPlayerEventAdapter mediaPlayerEventAdapter)
		{
			if (initiated)
			{
				mediaPlayer.addMediaPlayerEventListener(mediaPlayerEventAdapter);
			}
			else
			{
				eventHandlersToAdd.add(mediaPlayerEventAdapter);
			}
		}

		/**
		 * At end.
		 */
		public void atEnd()
		{
			playing = false;
			if (actionOnVideoEndListener != null)
			{
				actionOnVideoEndListener.onVideoEnd();
			}
		}

		/*
		 * (non-Javadoc)
		 * @see java.lang.Thread#destroy()
		 */
		@Override
		public void destroy()
		{
			initiated = false;
		}

		/**
		 * Gets the position.
		 *
		 * @return the position
		 */
		public float getPosition()
		{
			if (!initiated)
			{
				return 0;
			}
			else
			{
				return mediaPlayer.getPosition();
			}
		}

		/**
		 * Checks if is playing.
		 *
		 * @return true, if is playing
		 */
		public boolean isPlaying()
		{
			return playing;
		}

		/**
		 * Pause vid.
		 */
		public void pauseVid()
		{
			if (playing)
			{
				mediaPlayer.setPause(true);
				playing = false;
			}
		}

		/**
		 * Play vid.
		 */
		public void playVid()
		{
			if (initiated)
			{
				mediaPlayer.play();
				playing = true;
			}
		}

		/**
		 * Restart.
		 */
		public void restart()
		{
			mediaPlayer.prepareMedia(videoURL, "");
			mediaPlayer.play();
			playing = true;
			atEnd = false;
		}

		/*
		 * (non-Javadoc)
		 * @see java.lang.Thread#run()
		 */
		@Override
		public void run()
		{
			try
			{
				mediaPlayerFactory = new MediaPlayerFactory("--no-video-title-show", "--quiet");
				mediaPlayer = mediaPlayerFactory.newDirectMediaPlayer(instance, instance);
				addMediaPlayerEventListener(new MediaPlayerEventAdapter()
				{
					@Override
					public void finished(MediaPlayer mediaPlayer)
					{
						atEnd = true;
						if (vidThread != null)
						{
							vidThread.atEnd();
							if (repeat)
							{
								vidThread.restart();
							}
						}
					}
				});
				mediaPlayer.setPlaySubItems(true);
				mediaPlayer.mute(true);
				mediaPlayer.prepareMedia(videoURL, "");
				mediaPlayer.play();
				while (!mediaPlayer.isPlaying())
				{
					Thread.sleep(100);
				}
				initiated = true;
				playing = true;
				mediaPlayer.setPosition(startPos);
				mediaPlayer.mute(false);
				if (!autostart)
				{
					pauseVid();
					while (!firstClick)
					{
						if (mediaPlayer.isPlaying())
						{
							Thread.sleep(100);
							if (!firstClick)
							{
								mediaPlayer.setPause(true);
								mediaPlayer.setPosition(startPos);
							}
						}
					}
				}
				for (MediaPlayerEventAdapter eventHandler : eventHandlersToAdd)
				{
					mediaPlayer.addMediaPlayerEventListener(eventHandler);
				}
			}
			catch (RuntimeException e)
			{
				log.log(Level.SEVERE, "Video won't play.  VLC may not be installed or the same architecture (32/64bit) as the java platform used).", e);
			}
			catch (InterruptedException e)
			{

			}
		}

		/**
		 * Sets the position.
		 *
		 * @param pos
		 *            the new position
		 */
		public void setPosition(float pos)
		{
			if (initiated)
			{
				mediaPlayer.setPosition(pos);
			}
			else
			{
				startPos = pos;
			}
		}

		/*
		 * (non-Javadoc)
		 * @see java.lang.Thread#start()
		 */
		@Override
		public void start()
		{
			super.start();
		}

		/**
		 * Stop vid.
		 */
		public void stopVid()
		{
			if (playing)
			{
				mediaPlayer.stop();
				playing = false;
			}
		}

		/**
		 * Unpause vid.
		 */
		public void unpauseVid()
		{
			if (atEnd)
			{
				restart();
			}
			else if (!playing)
			{
				mediaPlayer.setPause(false);
				playing = true;
			}
		}

	}

	/** The Constant CACHABLE_TYPE. */
	public static final String CACHABLE_TYPE = "CACHABLE_VIDEO";

	/** The media players. */
	public static ArrayList<SimpleMediaPlayer> mediaPlayers = new ArrayList<SimpleMediaPlayer>();

	/** The Constant HEIGHT. */
	private static final int HEIGHT = 240;

	/** The Constant log. */
	private static final Logger log = Logger.getLogger(SimpleMediaPlayer.class.getName());

	/** The texture format. */
	private static Format TEXTURE_FORMAT = Format.RGBA16;

	/** The vid height. */
	private static float vidHeight = 7;

	/** The vid width. */
	private static float vidWidth = 12;

	/** The Constant WIDTH. */
	private static final int WIDTH = 320;

	static
	{
		boolean found = false;
		String vlcLib = ManagementFactory.getRuntimeMXBean().getSystemProperties().get("vlc");
		if (vlcLib != null)
		{
			found = true;
		}
		if (!found)
		{
			found = new NativeDiscovery().discover();
		}
		if (!found)
		{
			log.warning("Cannot play videos.  VLC is either not installed or located in an unexpected directory.  " + "If VLC is installed in an unexpected directory you can provide the path to its library " + "location with the argument: '-Dvlc=\"...\"");
		}
	}

	/** The media player. */
	public DirectMediaPlayer mediaPlayer;

	/** The action on video end listener. */
	private IActionOnVideoEndListener actionOnVideoEndListener = null;

	/** The at end. */
	private boolean atEnd = false;

	/** The autostart. */
	private boolean autostart = false;

	/** The first click. */
	private boolean firstClick = false;

	/** The has started. */
	private boolean hasStarted = false;

	/** The instance. */
	private SimpleMediaPlayer instance;

	/** The mat. */
	private Material mat;

	/** The media player factory. */
	private MediaPlayerFactory mediaPlayerFactory;

	/** The quad. */
	private CenteredQuad quad;

	/** The quad geometry. */
	private Geometry quadGeometry;

	/** The repeat. */
	private boolean repeat = false;

	/** The video image. */
	private Image videoImage;

	/** The video texture. */
	private Texture2D videoTexture;

	/** The video url. */
	private String videoURL = null;

	/** The vid thread. */
	private VidThread vidThread = null;

	/**
	 * Instantiates a new simple media player.
	 *
	 * @param name
	 *            the name
	 * @param uuid
	 *            the uuid
	 */
	public SimpleMediaPlayer(String name, UUID uuid)
	{
		super(name, uuid);
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.additionalitems.interfaces.ISimpleMediaPlayer#
	 * addMediaPlayerEventListener
	 * (uk.co.caprica.vlcj.player.MediaPlayerEventAdapter)
	 */
	@Override
	public void addMediaPlayerEventListener(MediaPlayerEventAdapter mediaPlayerEventAdapter)
	{
		if (vidThread != null)
		{
			vidThread.addMediaPlayerEventListener(mediaPlayerEventAdapter);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.additionalitems.interfaces.ISimpleMediaPlayer#destroy()
	 */
	@Override
	public void destroy()
	{
		if (vidThread != null)
		{
			vidThread.destroy();
			if (mediaPlayer != null)
			{
				unpause();
				mediaPlayer.stop();
				mediaPlayer.release();
			}
			if (mediaPlayerFactory != null)
			{
				mediaPlayerFactory.release();
			}
			if (mediaPlayers.contains(this))
			{
				mediaPlayers.remove(this);
			}
			vidThread.interrupt();

			vidThread = null;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * uk.co.caprica.vlcj.player.direct.RenderCallback#display(uk.co.caprica
	 * .vlcj.player.direct.DirectMediaPlayer, com.sun.jna.Memory[],
	 * uk.co.caprica.vlcj.player.direct.BufferFormat)
	 */
	@Override
	public void display(DirectMediaPlayer mediaPlayer, Memory[] nativeBuffers, BufferFormat bufferFormat)
	{
		ByteBuffer buffer = nativeBuffers[0].getByteBuffer(0, bufferFormat.getWidth() * bufferFormat.getHeight() * 4);
		videoImage.setData(buffer);
		videoTexture.setImage(videoImage);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * uk.co.caprica.vlcj.player.direct.BufferFormatCallback#getBufferFormat
	 * (int, int)
	 */
	@Override
	public BufferFormat getBufferFormat(int sourceWidth, int sourceHeight)
	{
		BufferFormat format = new BufferFormat("RGBA", WIDTH, HEIGHT, new int[]
		{ WIDTH * 4 }, new int[]
		{ HEIGHT });

		return format;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynet3.additionalitems.interfaces.ISimpleMediaPlayer#getHeight()
	 */
	@Override
	public float getHeight()
	{
		return vidHeight;
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.csys.items.item.IItem#getManipulableSpatial()
	 */
	@Override
	public Spatial getManipulableSpatial()
	{
		return quadGeometry;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynet3.additionalitems.interfaces.ISimpleMediaPlayer#getPosition()
	 */
	@Override
	public float getPosition()
	{
		if (vidThread != null)
		{
			return vidThread.getPosition();
		}
		else
		{
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynet3.additionalitems.interfaces.ISimpleMediaPlayer#getRepeat()
	 */
	@Override
	public boolean getRepeat()
	{
		return repeat;
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.additionalitems.interfaces.ISimpleMediaPlayer#getWidth()
	 */
	@Override
	public float getWidth()
	{
		return vidWidth;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.jme3csys.items.IInitable#initializeGeometry(com.jme3.asset
	 * .AssetManager)
	 */
	@Override
	public void initializeGeometry(AssetManager assetManager)
	{
		this.instance = this;

		videoImage = new Image(TEXTURE_FORMAT, WIDTH, HEIGHT, null);
		videoTexture = new Texture2D(videoImage);

		quad = new CenteredQuad(vidWidth, vidHeight);
		quadGeometry = new Geometry("quad_geom", quad);
		mat = new Material(MultiplicityClient.assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		mat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
		mat.setTexture("ColorMap", videoTexture);
		mat.getAdditionalRenderState().setFaceCullMode(FaceCullMode.Front);
		quadGeometry.setMaterial(mat);

		Node transformNode = new Node();
		transformNode.attachChild(quadGeometry);

		transformNode.rotate(0f, 0f, FastMath.DEG_TO_RAD * 180f);
		transformNode.rotate(0f, FastMath.DEG_TO_RAD * 180f, 0f);

		this.setVisible(false);

		ItemMap.register(quadGeometry, this);
		log.fine("Attaching image quad geometry!");
		attachChild(transformNode);
		hasStarted = true;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynet3.additionalitems.interfaces.ISimpleMediaPlayer#isPlaying()
	 */
	@Override
	public boolean isPlaying()
	{
		if (vidThread != null)
		{
			return vidThread.isPlaying();
		}
		else
		{
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.additionalitems.interfaces.ISimpleMediaPlayer#pause()
	 */
	@Override
	public void pause()
	{
		if (vidThread != null)
		{
			vidThread.pauseVid();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.additionalitems.interfaces.ISimpleMediaPlayer#play()
	 */
	@Override
	public void play()
	{
		if (vidThread != null)
		{
			vidThread.playVid();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.additionalitems.interfaces.ISimpleMediaPlayer#
	 * setActionOnVideoEndListener
	 * (synergynet3.additionalitems.interfaces.IActionOnVideoEndListener)
	 */
	@Override
	public void setActionOnVideoEndListener(IActionOnVideoEndListener actionOnVideoEndListener)
	{
		this.actionOnVideoEndListener = actionOnVideoEndListener;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynet3.additionalitems.interfaces.ISimpleMediaPlayer#setLocalResource
	 * (java.io.File, boolean)
	 */
	@Override
	public void setLocalResource(File file, boolean autostart)
	{
		this.autostart = autostart;
		videoURL = file.toString();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynet3.additionalitems.interfaces.ISimpleMediaPlayer#setLocalResource
	 * (java.lang.String, boolean)
	 */
	@Override
	public void setLocalResource(String localPath, boolean autostart)
	{
		this.autostart = autostart;
		videoURL = localPath;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynet3.additionalitems.interfaces.ISimpleMediaPlayer#setPosition
	 * (float)
	 */
	@Override
	public void setPosition(float pos)
	{
		if (vidThread != null)
		{
			vidThread.setPosition(pos);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynet3.additionalitems.interfaces.ISimpleMediaPlayer#setRemoteResource
	 * (java.lang.String, boolean)
	 */
	@Override
	public void setRemoteResource(String remotePath, boolean autostart)
	{
		this.autostart = autostart;
		videoURL = remotePath;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynet3.additionalitems.interfaces.ISimpleMediaPlayer#setRepeat(boolean
	 * )
	 */
	@Override
	public void setRepeat(boolean repeat)
	{
		this.repeat = repeat;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * synergynet3.additionalitems.interfaces.ISimpleMediaPlayer#setSize(float,
	 * float)
	 */
	@Override
	public void setSize(float width, float height)
	{
		vidWidth = width;
		vidHeight = height;
		quad = new CenteredQuad(width, height);
		quadGeometry.setMesh(quad);
	}

	/*
	 * (non-Javadoc)
	 * @see multiplicity3.jme3csys.items.item.JMEItem#setVisible(boolean)
	 */
	@Override
	public void setVisible(boolean isVisible)
	{
		if (!hasStarted)
		{
			super.setVisible(isVisible);
			if (!isVisible)
			{
				if (this.getParentItem() != null)
				{
					this.setInteractionEnabled(false);
				}
			}
		}
		else
		{
			if (!isVisible)
			{
				super.setVisible(isVisible);
				destroy();
			}
			else
			{
				initialise();
				super.setVisible(isVisible);
			}
		}
	}

	/**
	 * Stop.
	 */
	public void stop()
	{
		if (vidThread != null)
		{
			vidThread.stopVid();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see synergynet3.additionalitems.interfaces.ISimpleMediaPlayer#unpause()
	 */
	@Override
	public void unpause()
	{
		if (vidThread != null)
		{
			vidThread.unpauseVid();
			if (!firstClick)
			{
				firstClick = true;
			}
		}
	}

	/**
	 * Initialise.
	 */
	private void initialise()
	{
		vidThread = new VidThread();
		vidThread.start();
		mediaPlayers.add(this);
	}
}
