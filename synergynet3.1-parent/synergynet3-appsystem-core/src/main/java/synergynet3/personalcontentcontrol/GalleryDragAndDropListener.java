package synergynet3.personalcontentcontrol;

import java.util.logging.Logger;

import multiplicity3.csys.draganddrop.DragAndDropListener;
import multiplicity3.csys.items.item.IItem;
import multiplicity3.csys.stage.IStage;

/**
 * The listener interface for receiving galleryDragAndDrop events. The class
 * that is interested in processing a galleryDragAndDrop event implements this
 * interface, and the object created with that class is registered with a
 * component using the component's
 * <code>addGalleryDragAndDropListener<code> method. When
 * the galleryDragAndDrop event occurs, that object's appropriate
 * method is invoked.
 *
 * @see GalleryDragAndDropEvent
 */
public class GalleryDragAndDropListener implements DragAndDropListener
{

	/** The gallery. */
	private StudentGallery gallery;

	/** The log. */
	private Logger log;

	/** The stage. */
	private IStage stage;

	/**
	 * Instantiates a new gallery drag and drop listener.
	 *
	 * @param gallery
	 *            the gallery
	 * @param stage
	 *            the stage
	 * @param log
	 *            the log
	 */
	public GalleryDragAndDropListener(StudentGallery gallery, IStage stage, Logger log)
	{
		this.gallery = gallery;
		this.log = log;
		this.stage = stage;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * multiplicity3.csys.draganddrop.DragAndDropListener#itemDraggedAndDropped
	 * (multiplicity3.csys.items.item.IItem,
	 * multiplicity3.csys.items.item.IItem, int)
	 */
	@Override
	public void itemDraggedAndDropped(IItem itemDropped, IItem onto, int indexOfDrop)
	{

		if (itemDropped == null)
		{
			log.warning("No actual dropping occurred!");
			return;
		}

		if (!itemDropped.isVisible())
		{
			log.fine("Item is not visible, no further action.");
			return;
		}

		attemptToAddToGallery(itemDropped, stage);
	}

	/**
	 * Attempt to add to gallery.
	 *
	 * @param item
	 *            the item
	 * @param stage
	 *            the stage
	 */
	private void attemptToAddToGallery(IItem item, IStage stage)
	{
		if (!item.equals(stage))
		{
			if (!gallery.addToGallery(item, stage))
			{
				attemptToAddToGallery(item.getParentItem(), stage);
			}
		}
	}

}
