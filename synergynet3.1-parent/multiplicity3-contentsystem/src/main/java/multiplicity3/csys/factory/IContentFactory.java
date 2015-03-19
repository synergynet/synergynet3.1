package multiplicity3.csys.factory;

import java.util.UUID;

import multiplicity3.csys.items.item.IItem;

/**
 * Factory (see Factory pattern) for creating items in the content system.
 * 
 * @author dcs0ah1
 *
 */
public interface IContentFactory {
	public <ContentType extends IItem, ConcreteType extends IItem> void register(Class<ContentType> contentType, Class<ConcreteType> concreteType) throws ContentTypeAlreadyBoundException, ContentTypeInvalidException;	
	public <ContentType extends IItem, ConcreteType extends IItem> Class<ConcreteType> getConcreteTypeForContentType(Class<ContentType> contentType);
	public <ContentType extends IItem> boolean canCreate(Class<ContentType> contentType);	
	public <ContentType extends IItem, ConcreteType extends IItem> ConcreteType create(Class<ContentType> clazz, String name, UUID id) throws ContentTypeNotBoundException;
}
