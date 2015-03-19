package synergynet3.activitypack1.core.lightbox.lightboxmodel.items;

import java.awt.geom.Point2D.Float;
import java.io.Serializable;

public class ImageItem extends LightBoxItem implements Serializable {
	private static final long serialVersionUID = 2815846342790448429L;
	
	private String imageFileName;
	private Float imageSize;

	public void setImageFileName(String imageFileName) {
		this.imageFileName = imageFileName;		
	}
	
	public String getImageFileName() {
		return this.imageFileName;
	}

	public void setSize(Float imageSize) {
		this.imageSize = imageSize;		
	}
	
	public Float getSize() {
		return this.imageSize;
	}
}
