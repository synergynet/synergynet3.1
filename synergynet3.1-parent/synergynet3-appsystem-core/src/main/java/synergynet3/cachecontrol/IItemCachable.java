package synergynet3.cachecontrol;

import synergynet3.databasemanagement.GalleryItemDatabaseFormat;

public interface IItemCachable {
	
	public GalleryItemDatabaseFormat deconstruct(String StudentID);
}
