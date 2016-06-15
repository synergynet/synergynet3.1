package multiplicity3.csys.factory;

import java.util.UUID;

import multiplicity3.csys.items.item.IItem;

/**
 * Factory (see Factory pattern) for creating items in the content system.
 *
 * @author dcs0ah1
 */
public interface IContentFactory
{

	/**
	 * Can create.
	 *
	 * @param <ContentType>
	 *            the generic type
	 * @param contentType
	 *            the content type
	 * @return true, if successful
	 */
	public <ContentType extends IItem> boolean canCreate(Class<ContentType> contentType);

	/**
	 * Creates the.
	 *
	 * @param <ContentType>
	 *            the generic type
	 * @param <ConcreteType>
	 *            the generic type
	 * @param clazz
	 *            the clazz
	 * @param name
	 *            the name
	 * @param id
	 *            the id
	 * @return the concrete type
	 * @throws ContentTypeNotBoundException
	 *             the content type not bound exception
	 */
	public <ContentType extends IItem, ConcreteType extends IItem> ConcreteType create(Class<ContentType> clazz, String name, UUID id) throws ContentTypeNotBoundException;

	/**
	 * Gets the concrete type for content type.
	 *
	 * @param <ContentType>
	 *            the generic type
	 * @param <ConcreteType>
	 *            the generic type
	 * @param contentType
	 *            the content type
	 * @return the concrete type for content type
	 */
	public <ContentType extends IItem, ConcreteType extends IItem> Class<ConcreteType> getConcreteTypeForContentType(Class<ContentType> contentType);

	/**
	 * Register.
	 *
	 * @param <ContentType>
	 *            the generic type
	 * @param <ConcreteType>
	 *            the generic type
	 * @param contentType
	 *            the content type
	 * @param concreteType
	 *            the concrete type
	 * @throws ContentTypeAlreadyBoundException
	 *             the content type already bound exception
	 * @throws ContentTypeInvalidException
	 *             the content type invalid exception
	 */
	public <ContentType extends IItem, ConcreteType extends IItem> void register(Class<ContentType> contentType, Class<ConcreteType> concreteType) throws ContentTypeAlreadyBoundException, ContentTypeInvalidException;
}
