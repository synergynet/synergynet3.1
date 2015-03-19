package synergynet3.additionalitems.jme;

import java.util.UUID;

import com.jme3.math.Vector2f;

import synergynet3.additionalitems.interfaces.ICachableLine;
import synergynet3.cachecontrol.IItemCachable;
import synergynet3.databasemanagement.GalleryItemDatabaseFormat;
import synergynet3.projector.network.ProjectorTransferUtilities;

import multiplicity3.csys.annotations.ImplementsContentItem;
import multiplicity3.csys.factory.ContentTypeNotBoundException;
import multiplicity3.csys.items.item.IItem;
import multiplicity3.csys.stage.IStage;
import multiplicity3.jme3csys.items.IInitable;
import multiplicity3.jme3csys.items.line.JMELine;

@ImplementsContentItem(target = ICachableLine.class)
public class CachableLine extends JMELine implements ICachableLine, IInitable, IItemCachable {
	
	public static final String CACHABLE_TYPE = "CACHABLE_LINE";
	
	public CachableLine(String name, UUID uuid) {
		super(name, uuid);		
	}
		
	@Override
	public GalleryItemDatabaseFormat deconstruct(String loc) {
		
		boolean itemMode = true;
		
		String from = "";
		String to = "";
		if (getSourceItem() != null && getDestinationItem() != null){
			from = ProjectorTransferUtilities.get().getContentKey(getSourceItem());
			to = ProjectorTransferUtilities.get().getContentKey(getDestinationItem());
			
			if (from == null || to == null){
				itemMode = false;				
			}
		}else{
			itemMode = false;
		}
		
		GalleryItemDatabaseFormat galleryItem = new GalleryItemDatabaseFormat();
		galleryItem.setType(CACHABLE_TYPE);
		galleryItem.addValue(itemMode);
		if (itemMode){
			galleryItem.addValue(from);
			galleryItem.addValue(to);
		}else{
			galleryItem.addValue(getStartPosition().x);
			galleryItem.addValue(getStartPosition().y);
			galleryItem.addValue(getEndPosition().x);
			galleryItem.addValue(getEndPosition().y);
		}

		return galleryItem;
	}

	public static CachableLine reconstruct(GalleryItemDatabaseFormat galleryItem, IStage stage, String loc) {
		try {			
			CachableLine line = stage.getContentFactory().create(ICachableLine.class, "line", UUID.randomUUID());
			line.setLineWidth(4f);
			if ((Boolean)galleryItem.getValues().get(0)){
				String from = (String)galleryItem.getValues().get(1);
				String to = (String)galleryItem.getValues().get(2);
				
				IItem source = ProjectorTransferUtilities.get().getContents().get(from);
				IItem destination = ProjectorTransferUtilities.get().getContents().get(to);
				
				if (source != null && destination != null){
					line.setSourceItem(source);
					line.setDestinationItem(destination);
				}				
				
				line.setStartPosition(source.getRelativeLocation());
				line.setEndPosition(destination.getRelativeLocation());	
				
			}else{
				Vector2f from = new Vector2f((Float)galleryItem.getValues().get(1), (Float)galleryItem.getValues().get(2));
				Vector2f to = new Vector2f((Float)galleryItem.getValues().get(3), (Float)galleryItem.getValues().get(4));
				line.setStartPosition(from);
				line.setEndPosition(to);				
			}
			return line;
		} catch (ContentTypeNotBoundException e) {
			return null;
		} catch (NullPointerException e){
			return null;
		}
	}
	

}
