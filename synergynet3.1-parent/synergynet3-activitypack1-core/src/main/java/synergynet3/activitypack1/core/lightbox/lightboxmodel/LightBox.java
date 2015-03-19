package synergynet3.activitypack1.core.lightbox.lightboxmodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import synergynet3.activitypack1.core.lightbox.lightboxmodel.items.ImageItem;
import synergynet3.activitypack1.core.lightbox.lightboxmodel.items.TextItem;


public class LightBox implements Serializable {
	private static final long serialVersionUID = -8097923181418609702L;
	
	private List<TextItem> textItems;
	private List<ImageItem> imageItems;
	private String name;
	
	public LightBox(String name) {
		this.name = name;
		textItems = new ArrayList<TextItem>();
		imageItems = new ArrayList<ImageItem>();
	}
	
	public String getName() {
		return this.name;
	}

	public void addTextItem(TextItem textItem) {		
		textItems.add(textItem);		
	}

	public void addImageItem(ImageItem imageItem) {
		imageItems.add(imageItem);
	}

	public Collection<TextItem> getTextItems() {		
		return Collections.unmodifiableCollection(textItems);
	}

	public Collection<ImageItem> getImageItems() {
		return Collections.unmodifiableCollection(imageItems);
	}

}
