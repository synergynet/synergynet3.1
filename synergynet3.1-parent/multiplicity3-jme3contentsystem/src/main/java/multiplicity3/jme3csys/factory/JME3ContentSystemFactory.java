package multiplicity3.jme3csys.factory;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import multiplicity3.csys.IUpdateable;
import multiplicity3.csys.factory.ContentTypeAlreadyBoundException;
import multiplicity3.csys.factory.ContentTypeInvalidException;
import multiplicity3.csys.factory.ContentTypeNotBoundException;
import multiplicity3.csys.factory.dynamic.DynamicContentSystemFactory;
import multiplicity3.csys.factory.dynamic.IResourceFinderFilter;
import multiplicity3.csys.items.item.IItem;
import multiplicity3.jme3csys.annotations.RequiresUpdate;
import multiplicity3.jme3csys.items.IInitable;

import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioRenderer;
import com.jme3.renderer.Renderer;

/**
 * A factory for creating JME3ContentSystem objects.
 */
public class JME3ContentSystemFactory extends DynamicContentSystemFactory
{

	/** The Constant log. */
	private static final Logger log = Logger.getLogger(JME3ContentSystemFactory.class.getName());

	/** The asset manager. */
	private AssetManager assetManager;

	/** The audio renderer. */
	private AudioRenderer audioRenderer;

	/** The renderer. */
	private Renderer renderer;

	/** The update list. */
	private List<IUpdateable> updateList;

	/**
	 * Instantiates a new JM e3 content system factory.
	 *
	 * @param renderer
	 *            the renderer
	 * @param audioRenderer
	 *            the audio renderer
	 * @param assetManager
	 *            the asset manager
	 * @param updateList
	 *            the update list
	 * @throws ClassNotFoundException
	 *             the class not found exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws ContentTypeAlreadyBoundException
	 *             the content type already bound exception
	 * @throws ContentTypeInvalidException
	 *             the content type invalid exception
	 */
	public JME3ContentSystemFactory(Renderer renderer, AudioRenderer audioRenderer, AssetManager assetManager, List<IUpdateable> updateList) throws ClassNotFoundException, IOException, ContentTypeAlreadyBoundException, ContentTypeInvalidException
	{
		super("multiplicity3.jme3csys.items", new IResourceFinderFilter()
		{
			@Override
			public boolean accept(String dottedResourcePathname)
			{
				String[] components = dottedResourcePathname.split("\\.");
				if (components.length < 2)
				{
					return false;
				}
				String extension = components[components.length - 1];
				String name = components[components.length - 2];
				return extension.equals("class") && name.startsWith("JME") && !name.endsWith("Delegate") && !name.endsWith("$1");
			}
		});
		this.setRenderer(renderer);
		this.setAudioRenderer(audioRenderer);
		this.assetManager = assetManager;
		this.updateList = updateList;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.csys.factory.dynamic.DynamicContentSystemFactory#create
	 * (java.lang.Class, java.lang.String, java.util.UUID)
	 */
	@Override
	public <ContentType extends IItem, ConcreteType extends IItem> ConcreteType create(Class<ContentType> clazz, String name, UUID id) throws ContentTypeNotBoundException
	{
		log.fine("Creating " + clazz.getName());
		// for some reason, linux jre6 doesn't like this, so need cast +
		// suppress
		@SuppressWarnings("unchecked")
		ConcreteType instance = (ConcreteType) super.create(clazz, name, id);
		if (instance == null)
		{
			log.severe("Could not create an instance of " + clazz);
			return null;
		}
		log.fine("Instance of " + clazz.getName() + " created: " + System.identityHashCode(instance));
		log.fine("Initializing geometry...");
		((IInitable) instance).initializeGeometry(assetManager);
		log.fine("Checking to see if item needs updating in game loop...");
		if (instance.getClass().getAnnotation(RequiresUpdate.class) != null)
		{
			updateList.add((IUpdateable) instance);
		}
		log.fine("Done.");
		return instance;
	}

	/**
	 * Gets the audio renderer.
	 *
	 * @return the audio renderer
	 */
	public AudioRenderer getAudioRenderer()
	{
		return audioRenderer;
	}

	/**
	 * Gets the renderer.
	 *
	 * @return the renderer
	 */
	public Renderer getRenderer()
	{
		return renderer;
	}

	/**
	 * Sets the audio renderer.
	 *
	 * @param audioRenderer
	 *            the new audio renderer
	 */
	public void setAudioRenderer(AudioRenderer audioRenderer)
	{
		this.audioRenderer = audioRenderer;
	}

	/**
	 * Sets the renderer.
	 *
	 * @param renderer
	 *            the new renderer
	 */
	public void setRenderer(Renderer renderer)
	{
		this.renderer = renderer;
	}

}
