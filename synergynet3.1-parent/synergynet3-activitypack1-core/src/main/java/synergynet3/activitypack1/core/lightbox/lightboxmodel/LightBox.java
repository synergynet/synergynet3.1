package synergynet3.activitypack1.core.lightbox.lightboxmodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import synergynet3.activitypack1.core.lightbox.lightboxmodel.items.ImageItem;
import synergynet3.activitypack1.core.lightbox.lightboxmodel.items.TextItem;

/**
 * The Class LightBox.
 */
public class LightBox implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -8097923181418609702L;

	/** The image items. */
	private List<ImageItem> imageItems;

	/** The name. */
	private String name;

	/** The text items. */
	private List<TextItem> textItems;

	/**
	 * Instantiates a new light box.
	 *
	 * @param name the name
	 */
	public LightBox(String name) {
		this.name = name;
		textItems = new ArrayList<TextItem>();
		imageItems = new ArrayList<ImageItem>();
	}

	/**
	 * Adds the image item.
	 *
	 * @param imageItem the image item
	 */
	public void addImageItem(ImageItem imageItem) {
		imageItems.add(imageItem);
	}

	/**
	 * Adds the text item.
	 *
	 * @param textItem the text item
	 */
	public void addTextItem(TextItem textItem) {
		textItems.add(textItem);
	}

	/**
	 * Gets the image items.
	 *
	 * @return the image items
	 */
	public Collection<ImageItem> getImageItems() {
		return Collections.unmodifiableCollection(imageItems);
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Gets the text items.
	 *
	 * @return the text items
	 */
	public Collection<TextItem> getTextItems() {
		return Collections.unmodifiableCollection(textItems);
	}

}
