package synergynet3.cachecontrol;

import synergynet3.databasemanagement.GalleryItemDatabaseFormat;

/**
 * The Interface IItemCachable.
 */
public interface IItemCachable {

	/**
	 * Deconstruct.
	 *
	 * @param StudentID the student id
	 * @return the gallery item database format
	 */
	public GalleryItemDatabaseFormat deconstruct(String StudentID);
}
