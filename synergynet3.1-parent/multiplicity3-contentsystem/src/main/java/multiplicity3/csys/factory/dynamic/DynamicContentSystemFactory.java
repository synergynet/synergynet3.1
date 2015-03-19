package multiplicity3.csys.factory.dynamic;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import multiplicity3.csys.annotations.ImplementsContentItem;
import multiplicity3.csys.factory.ContentTypeAlreadyBoundException;
import multiplicity3.csys.factory.ContentTypeInvalidException;
import multiplicity3.csys.factory.ContentTypeNotBoundException;
import multiplicity3.csys.factory.IContentFactory;
import multiplicity3.csys.items.item.IItem;

public abstract class DynamicContentSystemFactory implements IContentFactory {

	private static final Logger log = Logger.getLogger(DynamicContentSystemFactory.class.getName());
	private Map<Class<? extends IItem>, Class<? extends IItem>> typeMapping;

	public DynamicContentSystemFactory(String dottedPackageRoot, IResourceFinderFilter filter) throws ClassNotFoundException, IOException, ContentTypeAlreadyBoundException, ContentTypeInvalidException {
		typeMapping = new HashMap<Class<? extends IItem>, Class<? extends IItem>>();
		loadKnownTypes(dottedPackageRoot, filter);
	}

	private void loadKnownTypes(String dottedPackageRoot, IResourceFinderFilter filter) throws ClassNotFoundException, IOException, ContentTypeAlreadyBoundException, ContentTypeInvalidException {
		log.fine("Loading implementations of content system items.");
		List<String> classResourceNames = ResourceFinder.find(dottedPackageRoot, true, filter);
		registerClassesFromResourceStrings(classResourceNames);
	}

	@Override
	public <ContentType extends IItem, ConcreteType extends IItem> void register(
			Class<ContentType> contentType, Class<ConcreteType> concreteType)
	throws ContentTypeAlreadyBoundException,
	ContentTypeInvalidException {
		log.fine("Registering " + concreteType + " as an implementation of " + contentType);
		if(typeMapping.containsKey(contentType)) throw new ContentTypeAlreadyBoundException(getConcreteTypeForContentType(contentType));
		if(!(IItem.class.isAssignableFrom(concreteType))) throw new ContentTypeInvalidException("Item must be a subclass of " + IItem.class.getCanonicalName());
		typeMapping.put(contentType, concreteType);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <ContentType extends IItem, ConcreteType extends IItem> Class<ConcreteType> getConcreteTypeForContentType(
			Class<ContentType> contentType) {
		return (Class<ConcreteType>) typeMapping.get(contentType);
	}

	@Override
	public <ContentType extends IItem> boolean canCreate(
			Class<ContentType> contentType) {
		return getConcreteTypeForContentType(contentType) != null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <ContentType extends IItem, ConcreteType extends IItem> ConcreteType create(
			Class<ContentType> clazz, String name, UUID id)
	throws ContentTypeNotBoundException {
		log.fine("Creating an item: " + clazz.getCanonicalName() + " with id " + id);

		if(!canCreate(clazz)) {
			log.severe("Cannot create " + clazz.getCanonicalName() + ", no concrete type for content type.");
			throw new ContentTypeNotBoundException();
		}
		log.fine("Instantiating.");

		try {
			Class<?>[] parameterTypes = { String.class, UUID.class };
			Class<ConcreteType> concreteType = getConcreteTypeForContentType(clazz);
			Constructor<? extends IItem> constructor = concreteType.getConstructor(parameterTypes);
			log.fine("Using constructor " + constructor);
			IItem instance = (IItem) constructor.newInstance(name, id);
			return (ConcreteType)instance;
		} catch (SecurityException e) {
			log.log(Level.SEVERE, "Could not create item: " + e);
		} catch (IllegalArgumentException e) {
			log.log(Level.SEVERE, "Could not create item: " + e);
		} catch (NoSuchMethodException e) {
			log.log(Level.SEVERE, "Could not create item: " + e);
		} catch (InstantiationException e) {
			log.log(Level.SEVERE, "Could not create item: " + e);
		} catch (IllegalAccessException e) {
			log.log(Level.SEVERE, "Could not create item: " + e);
		} catch (InvocationTargetException e) {
			log.log(Level.SEVERE, "Could not create item: " + e);
		}
		return null;
	}

	public void registerClassesFromResourceStrings(List<String> classResourceNames)
	throws ClassNotFoundException, ContentTypeAlreadyBoundException, ContentTypeInvalidException  {
		for(String classNameWithDotClass : classResourceNames) {
			int index = classNameWithDotClass.indexOf(".class");
			String className = classNameWithDotClass.substring(0, index);
			try {

				Class<? extends IItem> concreteClass = Class.forName(className).asSubclass(IItem.class);
				ImplementsContentItem ici = concreteClass.getAnnotation(ImplementsContentItem.class);				
				if(ici != null) {
					Class<? extends IItem> targetClass = ici.target();
					register(targetClass, concreteClass);
				}else{
					log.fine(concreteClass.getCanonicalName() + " is not annotated with " + ImplementsContentItem.class.getName());
				}
			}catch(ClassCastException ex) {
				log.fine(className + " is not an implementation of a content system item");
			}
		}		
	}
}