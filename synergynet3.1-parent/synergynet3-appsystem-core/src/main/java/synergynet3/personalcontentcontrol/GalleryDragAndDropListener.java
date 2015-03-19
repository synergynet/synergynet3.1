package synergynet3.personalcontentcontrol;

import java.util.logging.Logger;

import multiplicity3.csys.draganddrop.DragAndDropListener;
import multiplicity3.csys.items.item.IItem;
import multiplicity3.csys.stage.IStage;

public class GalleryDragAndDropListener implements DragAndDropListener {
	
	private Logger log;	
	private StudentGallery gallery;
	private IStage stage;
	
	public GalleryDragAndDropListener(StudentGallery gallery, IStage stage, Logger log) {
		this.gallery = gallery;
		this.log = log;
		this.stage = stage;
	}
	
	@Override
	public void itemDraggedAndDropped(IItem itemDropped, IItem onto, int indexOfDrop) {
		
		if(itemDropped == null) {
			log.warning("No actual dropping occurred!");
			return;
		}
		
		if(!itemDropped.isVisible()) {
			log.fine("Item is not visible, no further action.");
			return;
		}
		
		attemptToAddToGallery(itemDropped, stage);
	}	
	
	private void attemptToAddToGallery(IItem item, IStage stage){
		if (!item.equals(stage)){
			if (!gallery.addToGallery(item, stage)){
				attemptToAddToGallery(item.getParentItem(), stage);
			}
		}
	}
	
}
