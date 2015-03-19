package multiplicity3.jme3csys.factory;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioRenderer;
import com.jme3.renderer.Renderer;

import multiplicity3.csys.IUpdateable;
import multiplicity3.csys.factory.ContentTypeAlreadyBoundException;
import multiplicity3.csys.factory.ContentTypeInvalidException;
import multiplicity3.csys.factory.ContentTypeNotBoundException;
import multiplicity3.csys.factory.dynamic.DynamicContentSystemFactory;
import multiplicity3.csys.factory.dynamic.IResourceFinderFilter;
import multiplicity3.csys.items.item.IItem;
import multiplicity3.jme3csys.annotations.RequiresUpdate;
import multiplicity3.jme3csys.items.IInitable;


public class JME3ContentSystemFactory extends DynamicContentSystemFactory {	
	private static final Logger log = Logger.getLogger(JME3ContentSystemFactory.class.getName());

	private AssetManager assetManager;
	private Renderer renderer;
	private AudioRenderer audioRenderer;
	private List<IUpdateable> updateList;

	public JME3ContentSystemFactory(Renderer renderer, AudioRenderer audioRenderer, AssetManager assetManager, List<IUpdateable> updateList) throws ClassNotFoundException, IOException, ContentTypeAlreadyBoundException, ContentTypeInvalidException {
		super("multiplicity3.jme3csys.items", new IResourceFinderFilter() {			
			@Override
			public boolean accept(String dottedResourcePathname) {
				String[] components = dottedResourcePathname.split("\\.");
				if(components.length < 2) return false;
				String extension = components[components.length - 1];
				String name = components[components.length - 2];
				return 	extension.equals("class") &&
				name.startsWith("JME") &&
				!name.endsWith("Delegate") && 
				!name.endsWith("$1");
			}
		});
		this.setRenderer(renderer);
		this.setAudioRenderer(audioRenderer);		
		this.assetManager = assetManager;
		this.updateList = updateList;		
	}

	public <ContentType extends IItem, ConcreteType extends IItem> ConcreteType create(
			Class<ContentType> clazz, String name, UUID id)
	throws ContentTypeNotBoundException {
		log.fine("Creating " + clazz.getName());
		// for some reason, linux jre6 doesn't like this, so need cast + suppress
		@SuppressWarnings("unchecked")
		ConcreteType instance = (ConcreteType) super.create(clazz, name, id);
		if(instance == null) {
			log.severe("Could not create an instance of " + clazz);
			return null;
		}
		log.fine("Instance of " + clazz.getName() + " created: " + System.identityHashCode(instance));
		log.fine("Initializing geometry...");
		((IInitable) instance).initializeGeometry(assetManager);
		log.fine("Checking to see if item needs updating in game loop...");
		if(instance.getClass().getAnnotation(RequiresUpdate.class) != null) {
			updateList.add((IUpdateable) instance);
		}
		log.fine("Done.");			
		return instance;
	}

	public void setRenderer(Renderer renderer) {
		this.renderer = renderer;
	}

	public Renderer getRenderer() {
		return renderer;
	}

	public void setAudioRenderer(AudioRenderer audioRenderer) {
		this.audioRenderer = audioRenderer;
	}

	public AudioRenderer getAudioRenderer() {
		return audioRenderer;
	}

}
